package pro.taskana.task.graphql.resolvers;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pro.taskana.common.api.BaseQuery.SortDirection;
import pro.taskana.common.api.exceptions.ConcurrencyException;
import pro.taskana.common.api.exceptions.InvalidArgumentException;
import pro.taskana.common.api.exceptions.NotAuthorizedException;
import pro.taskana.common.rest.QuerySortParameter;
import pro.taskana.task.api.TaskService;
import pro.taskana.task.api.exceptions.TaskCommentNotFoundException;
import pro.taskana.task.api.exceptions.TaskNotFoundException;
import pro.taskana.task.api.models.TaskComment;
import pro.taskana.task.common.assembler.TaskCommentRepresentationModelAssembler;
import pro.taskana.task.common.models.TaskCommentCollectionRepresentationModel;
import pro.taskana.task.common.models.TaskCommentRepresentationModel;
import pro.taskana.task.graphql.payloads.CreateTaskCommentPayload;
import pro.taskana.task.graphql.payloads.DeleteTaskCommentPayload;
import pro.taskana.task.graphql.payloads.UpdateTaskCommentPayload;
import pro.taskana.task.rest.TaskCommentController.TaskCommentsSortBy;

/** Resolver for all {@linkplain TaskComment} related Queries. */
@GraphQLApi
@Service
public class TaskCommentResolver {

  private final TaskService taskService;
  private final TaskCommentRepresentationModelAssembler taskCommentModelAssembler;

  @Autowired
  public TaskCommentResolver(
      TaskService taskService, TaskCommentRepresentationModelAssembler taskCommentModelAssembler) {
    this.taskService = taskService;
    this.taskCommentModelAssembler = taskCommentModelAssembler;
  }

  @GraphQLQuery(name = "getTaskComment", description = "This endpoint retrieves a Task Comment.")
  public TaskCommentRepresentationModel getTaskComment(
      @GraphQLArgument(name = "taskCommentId") String id)
      throws InvalidArgumentException, TaskNotFoundException, TaskCommentNotFoundException,
          NotAuthorizedException {
    TaskComment taskComment = taskService.getTaskComment(id);
    return taskCommentModelAssembler.toModelWithoutLinks(taskComment);
  }

  @GraphQLQuery(
      name = "getTaskComments",
      description =
          "This endpoint retrieves all Task Comments for a specific Task. Further filters can be"
              + " applied.")
  public TaskCommentCollectionRepresentationModel getTaskComments(
      @GraphQLArgument(name = "taskId") String taskId,
      @GraphQLArgument(name = "sortBy") List<TaskCommentsSortBy> sortBy,
      @GraphQLArgument(name = "order") List<SortDirection> order)
      throws InvalidArgumentException, TaskNotFoundException, NotAuthorizedException {
    Optional<Comparator<TaskComment>> comparator = getTaskCommentComparator(sortBy, order);
    List<TaskComment> taskComments = taskService.getTaskComments(taskId);
    comparator.ifPresent(taskComments::sort);

    return taskCommentModelAssembler.toTaskanaCollectionModel(taskComments);
  }

  @GraphQLMutation(
      name = "deleteTaskComment",
      description = "This endpoint deletes a given Task Comment.")
  public DeleteTaskCommentPayload deleteTaskComment(
      @GraphQLArgument(name = "taskCommentId") String id)
      throws InvalidArgumentException, TaskNotFoundException, TaskCommentNotFoundException,
          NotAuthorizedException {
    taskService.deleteTaskComment(id);
    return new DeleteTaskCommentPayload("TaskComment successfully deleted", id);
  }

  @GraphQLMutation(
      name = "updateTaskComment",
      description = "This endpoint updates a given Task Comment.")
  public UpdateTaskCommentPayload updateTaskComment(
      @GraphQLArgument(name = "taskCommentId") String id,
      @GraphQLArgument(name = "taskComment") TaskCommentRepresentationModel taskCommentRepModel)
      throws InvalidArgumentException, ConcurrencyException, TaskNotFoundException,
          TaskCommentNotFoundException, NotAuthorizedException {
    TaskComment taskComment = taskCommentModelAssembler.toEntityModel(taskCommentRepModel);
    if (!id.equals(taskComment.getId())) {
      throw new InvalidArgumentException(
          String.format(
              "TaskCommentId ('%s') is not identical with the id"
                  + " of the object in the payload which should be updated",
              taskComment.getId()));
    }

    taskComment = taskService.updateTaskComment(taskComment);

    return new UpdateTaskCommentPayload(
        "Successfully updated TaskComment",
        taskCommentModelAssembler.toModelWithoutLinks(taskComment));
  }

  @GraphQLMutation(
      name = "createTaskComment",
      description = "This endpoint creates a Task Comment.")
  public CreateTaskCommentPayload createTaskComment(
      @GraphQLArgument(name = "taskComment") TaskCommentRepresentationModel taskCommentRepModel)
      throws InvalidArgumentException, TaskNotFoundException, NotAuthorizedException {
    TaskComment taskComment = taskCommentModelAssembler.toEntityModel(taskCommentRepModel);
    taskComment = taskService.createTaskComment(taskComment);
    return new CreateTaskCommentPayload(
        "TaskComment successfully created",
        taskCommentModelAssembler.toModelWithoutLinks(taskComment));
  }

  private Optional<Comparator<TaskComment>> getTaskCommentComparator(
      List<TaskCommentsSortBy> sortBy, List<SortDirection> order) throws InvalidArgumentException {
    QuerySortParameter.verifyNotOnlyOrderByExists(sortBy, order);
    QuerySortParameter.verifyAmountOfSortByAndOrderByMatches(sortBy, order);
    Comparator<TaskComment> comparator = null;
    if (sortBy != null) {
      for (int i = 0; i < sortBy.size(); i++) {
        SortDirection sortDirection = order == null ? SortDirection.ASCENDING : order.get(i);
        Comparator<TaskComment> temp = sortBy.get(i).getComparator();
        if (sortDirection == SortDirection.DESCENDING) {
          temp = temp.reversed();
        }
        comparator = comparator == null ? temp : comparator.thenComparing(temp);
      }
    }
    return Optional.ofNullable(comparator);
  }
}
