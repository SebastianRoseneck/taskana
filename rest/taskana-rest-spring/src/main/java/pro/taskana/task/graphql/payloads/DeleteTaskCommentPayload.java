package pro.taskana.task.graphql.payloads;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.task.graphql.resolvers.TaskCommentResolver#deleteTaskComment deleteTaskComment}
 * mutation.
 */
@GraphQLType(description = "Payload used as a return type for the deleteTaskComment mutation.")
public class DeleteTaskCommentPayload {

  private final String message;
  private final String id;

  public DeleteTaskCommentPayload(String message, String id) {
    this.message = message;
    this.id = id;
  }

  @GraphQLQuery(description = "Returned message of the called mutation.")
  public String getMessage() {
    return message;
  }

  @GraphQLQuery(description = "The ID of the deleted TaskComment.")
  public String getId() {
    return id;
  }
}
