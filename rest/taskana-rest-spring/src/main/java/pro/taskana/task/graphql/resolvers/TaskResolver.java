package pro.taskana.task.graphql.resolvers;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pro.taskana.classification.api.exceptions.ClassificationNotFoundException;
import pro.taskana.common.api.BulkOperationResults;
import pro.taskana.common.api.exceptions.ConcurrencyException;
import pro.taskana.common.api.exceptions.InvalidArgumentException;
import pro.taskana.common.api.exceptions.NotAuthorizedException;
import pro.taskana.common.api.exceptions.TaskanaException;
import pro.taskana.common.rest.QueryPagingParameter;
import pro.taskana.task.api.TaskQuery;
import pro.taskana.task.api.TaskService;
import pro.taskana.task.api.exceptions.AttachmentPersistenceException;
import pro.taskana.task.api.exceptions.InvalidOwnerException;
import pro.taskana.task.api.exceptions.InvalidStateException;
import pro.taskana.task.api.exceptions.TaskAlreadyExistException;
import pro.taskana.task.api.exceptions.TaskNotFoundException;
import pro.taskana.task.api.models.Task;
import pro.taskana.task.api.models.TaskSummary;
import pro.taskana.task.common.assembler.TaskRepresentationModelAssembler;
import pro.taskana.task.common.assembler.TaskSummaryRepresentationModelAssembler;
import pro.taskana.task.common.filter.TaskQueryFilterParameter;
import pro.taskana.task.common.models.TaskRepresentationModel;
import pro.taskana.task.common.models.TaskSummaryPagedRepresentationModel;
import pro.taskana.task.graphql.payloads.CancelClaimTaskPayload;
import pro.taskana.task.graphql.payloads.CancelTaskPayload;
import pro.taskana.task.graphql.payloads.ClaimTaskPayload;
import pro.taskana.task.graphql.payloads.CompleteTaskPayload;
import pro.taskana.task.graphql.payloads.CreateTaskPayload;
import pro.taskana.task.graphql.payloads.DeleteTaskPayload;
import pro.taskana.task.graphql.payloads.DeleteTasksPayload;
import pro.taskana.task.graphql.payloads.ForceCancelClaimTaskPayload;
import pro.taskana.task.graphql.payloads.SelectAndClaimTaskPayload;
import pro.taskana.task.graphql.payloads.TransferTaskPayload;
import pro.taskana.task.graphql.payloads.UpdateTaskPayload;
import pro.taskana.task.rest.TaskController.TaskQuerySortParameter;
import pro.taskana.workbasket.api.exceptions.WorkbasketNotFoundException;

/** Resolver for all {@linkplain Task} related Queries. */
@GraphQLApi
@Service
public class TaskResolver {

  private final TaskService taskService;
  private final TaskRepresentationModelAssembler taskModelAssembler;
  private final TaskSummaryRepresentationModelAssembler taskSummaryModelAssembler;

  @Autowired
  public TaskResolver(
      TaskService taskService,
      TaskRepresentationModelAssembler taskModelAssembler,
      TaskSummaryRepresentationModelAssembler taskSummaryModelAssembler) {
    this.taskService = taskService;
    this.taskModelAssembler = taskModelAssembler;
    this.taskSummaryModelAssembler = taskSummaryModelAssembler;
  }

  @GraphQLQuery(
      name = "getTasks",
      description = "This endpoint retrieves a list of existing Tasks. Filters can be applied.")
  public TaskSummaryPagedRepresentationModel getTasks(
      @GraphQLArgument(name = "filterParameter") TaskQueryFilterParameter filterParameter,
      @GraphQLArgument(name = "sortParameter") TaskQuerySortParameter sortParameter,
      @GraphQLArgument(name = "pagingParameter")
          QueryPagingParameter<TaskSummary, TaskQuery> pagingParameter) {
    TaskQuery query = taskService.createTaskQuery();

    if (filterParameter != null) {
      filterParameter.apply(query);
    }
    if (sortParameter != null) {
      sortParameter.apply(query);
    }

    if (pagingParameter != null) {
      List<TaskSummary> taskSummaries = pagingParameter.apply(query);
      return taskSummaryModelAssembler.toPagedModel(
          taskSummaries, pagingParameter.getPageMetadata());

    } else {
      List<TaskSummary> taskSummaries = query.list();
      return taskSummaryModelAssembler.toPagedModel(taskSummaries, null);
    }
  }

  @GraphQLQuery(name = "getTask", description = "This endpoint retrieves a specific Task.")
  public TaskRepresentationModel getTask(@GraphQLArgument(name = "taskId") String id)
      throws TaskNotFoundException, NotAuthorizedException {
    Task task = taskService.getTask(id);
    return taskModelAssembler.toModelWithoutLinks(task);
  }

  @GraphQLMutation(
      name = "deleteTasks",
      description =
          "This endpoint deletes an aggregation of Tasks and returns the deleted Tasks. Filters can"
              + " be applied.")
  public DeleteTasksPayload deleteTasks(
      @GraphQLArgument(name = "filterParameter") TaskQueryFilterParameter filterParameter)
      throws InvalidArgumentException, NotAuthorizedException {
    TaskQuery query = taskService.createTaskQuery();

    if (filterParameter != null) {
      filterParameter.apply(query);
    } else {
      return new DeleteTasksPayload("Filter Parameter can't be empty", null);
    }

    List<TaskSummary> taskSummaries = query.list();

    List<String> taskIdsToDelete =
        taskSummaries.stream().map(TaskSummary::getId).collect(Collectors.toList());

    BulkOperationResults<String, TaskanaException> result =
        taskService.deleteTasks(taskIdsToDelete);

    List<String> successfullyDeletedTaskSummaries =
        taskSummaries.stream()
            .map(TaskSummary::getId)
            .filter(summaryId -> !result.getFailedIds().contains(summaryId))
            .collect(Collectors.toList());

    return new DeleteTasksPayload("Tasks successfully deleted", successfullyDeletedTaskSummaries);
  }

  @GraphQLMutation(name = "claimTask", description = "This endpoint claims a Task if possible.")
  public ClaimTaskPayload claimTask(
      @GraphQLArgument(name = "taskId") String id,
      @GraphQLArgument(name = "userName") String userName)
      throws TaskNotFoundException, InvalidOwnerException, InvalidStateException,
          NotAuthorizedException {
    // TODO verify user

    taskService.claim(id);
    Task updatedTask = taskService.getTask(id);

    return new ClaimTaskPayload("Task successfully claimed", id);
  }

  @GraphQLMutation(
      name = "selectAndClaimTask",
      description =
          "This endpoint selects the first Task returned by the Task Query and claims it.")
  public SelectAndClaimTaskPayload selectAndClaimTask(
      @GraphQLArgument(name = "filterParameter") TaskQueryFilterParameter filterParameter,
      @GraphQLArgument(name = "sortParameter") TaskQuerySortParameter sortParameter)
      throws InvalidOwnerException, NotAuthorizedException {
    TaskQuery query = taskService.createTaskQuery();
    if (filterParameter != null) {
      filterParameter.apply(query);
    }
    if (sortParameter != null) {
      sortParameter.apply(query);
    }

    Task selectedAndClaimedTask = taskService.selectAndClaim(query);
    return new SelectAndClaimTaskPayload(
        "Task successfully selected and claimed", selectedAndClaimedTask.getId());
  }

  @GraphQLMutation(
      name = "cancelClaimTask",
      description =
          "This endpoint cancels the claim of an existing Task if it was claimed by the current"
              + " user before.")
  public CancelClaimTaskPayload cancelClaimTask(@GraphQLArgument(name = "taskId") String id)
      throws TaskNotFoundException, InvalidOwnerException, InvalidStateException,
          NotAuthorizedException {
    Task updatedTask = taskService.cancelClaim(id);
    return new CancelClaimTaskPayload("Successfully canceled claim of a Task", id);
  }

  @GraphQLMutation(
      name = "forceCancelClaimTask",
      description = "This endpoint force cancels the claim of an existing Task.")
  public ForceCancelClaimTaskPayload forceCancelClaimTask(
      @GraphQLArgument(name = "taskId") String id)
      throws TaskNotFoundException, InvalidOwnerException, InvalidStateException,
          NotAuthorizedException {
    Task updatedTask = taskService.forceCancelClaim(id);
    return new ForceCancelClaimTaskPayload("Successfully force canceled claim of a Task", id);
  }

  @GraphQLMutation(name = "completeTask", description = "This endpoint completes a Task.")
  public CompleteTaskPayload completeTask(@GraphQLArgument(name = "taskId") String id)
      throws TaskNotFoundException, InvalidOwnerException, InvalidStateException,
          NotAuthorizedException {
    Task updatedTask = taskService.forceCompleteTask(id);
    return new CompleteTaskPayload("Task successfully completed", id);
  }

  @GraphQLMutation(name = "deleteTask", description = "This endpoint deletes a Task.")
  public DeleteTaskPayload deleteTask(@GraphQLArgument(name = "taskId") String id)
      throws TaskNotFoundException, InvalidStateException, NotAuthorizedException {
    taskService.forceDeleteTask(id);
    return new DeleteTaskPayload("Task successfully deleted", id);
  }

  @GraphQLMutation(
      name = "cancelTask",
      description =
          "This endpoint cancels a Task. Cancellation marks a Task as obsolete. The actual work the"
              + " Task was referring to is no longer required.")
  public CancelTaskPayload cancelTask(@GraphQLArgument(name = "taskId") String id)
      throws TaskNotFoundException, InvalidStateException, NotAuthorizedException {
    Task cancelledTask = taskService.cancelTask(id);
    return new CancelTaskPayload("Task successfully canceled", id);
  }

  @GraphQLMutation(name = "createTask", description = "This endpoint creates a persistent Task.")
  public CreateTaskPayload createTask(
      @GraphQLArgument(name = "task") TaskRepresentationModel taskRepModel)
      throws TaskAlreadyExistException, InvalidArgumentException, WorkbasketNotFoundException,
          ClassificationNotFoundException, NotAuthorizedException, AttachmentPersistenceException {
    Task task = taskModelAssembler.toEntityModel(taskRepModel);
    task = taskService.createTask(task);

    return new CreateTaskPayload(
        "Task successfully created", taskModelAssembler.toModelWithoutLinks(task));
  }

  @GraphQLMutation(
      name = "transferTask",
      description = "This endpoint transfers a given Task to a given Workbasket, if possible.")
  public TransferTaskPayload transferTask(
      @GraphQLArgument(name = "taskId") String taskId,
      @GraphQLArgument(name = "workbasketId") String workbasketId,
      @GraphQLArgument(name = "setTransferFlag") Boolean setTransferFlag)
      throws TaskNotFoundException, WorkbasketNotFoundException, NotAuthorizedException,
          InvalidStateException {
    Task updatedTask =
        taskService.transfer(taskId, workbasketId, setTransferFlag == null || setTransferFlag);

    return new TransferTaskPayload("Task successfully transferred", taskId, workbasketId);
  }

  @GraphQLMutation(name = "updateTask", description = "This endpoint updates a requested Task.")
  public UpdateTaskPayload updateTask(
      @GraphQLArgument(name = "taskId") String id,
      @GraphQLArgument(name = "task") TaskRepresentationModel taskRepModel)
      throws InvalidArgumentException, ConcurrencyException, TaskNotFoundException,
          ClassificationNotFoundException, NotAuthorizedException, InvalidStateException,
          AttachmentPersistenceException {
    Task task = taskModelAssembler.toEntityModel(taskRepModel);

    if (!id.equals(task.getId())) {
      throw new InvalidArgumentException(
          String.format(
              "TaskId ('%s') is not identical with the taskId of to "
                  + "object in the payload which should be updated. ID=('%s')",
              id, task.getId()));
    }

    task = taskService.updateTask(task);
    return new UpdateTaskPayload(
        "Task successfully updated", taskModelAssembler.toModelWithoutLinks(task));
  }
}
