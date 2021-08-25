package pro.taskana.task.graphql.payloads;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

import pro.taskana.task.common.models.TaskRepresentationModel;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.task.graphql.resolvers.TaskResolver#updateTask updateTask} mutation.
 */
@GraphQLType(description = "Payload used as a return type for the updateTask mutation.")
public class UpdateTaskPayload {
  private final String message;
  private final TaskRepresentationModel task;

  public UpdateTaskPayload(String message, TaskRepresentationModel task) {
    this.message = message;
    this.task = task;
  }

  @GraphQLQuery(description = "Returned message of the called mutation.")
  public String getMessage() {
    return message;
  }

  @GraphQLQuery(description = "The updated Task.")
  public TaskRepresentationModel getTask() {
    return task;
  }
}
