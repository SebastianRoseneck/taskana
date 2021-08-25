package pro.taskana.task.graphql.payloads;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

import pro.taskana.task.common.models.TaskRepresentationModel;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.task.graphql.resolvers.TaskResolver#createTask createTask} mutation.
 */
@GraphQLType(description = "Payload used as a return type for the createTask mutation.")
public class CreateTaskPayload {

  private final String message;
  private final TaskRepresentationModel task;

  public CreateTaskPayload(String message, TaskRepresentationModel task) {
    this.message = message;
    this.task = task;
  }

  @GraphQLQuery(description = "Returned message of the called mutation.")
  public String getMessage() {
    return message;
  }

  @GraphQLQuery(description = "The created Task.")
  public TaskRepresentationModel getTask() {
    return task;
  }
}
