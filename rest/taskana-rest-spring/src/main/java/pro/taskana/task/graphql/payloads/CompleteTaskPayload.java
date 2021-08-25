package pro.taskana.task.graphql.payloads;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.task.graphql.resolvers.TaskResolver#completeTask completeTask} mutation.
 */
@GraphQLType(description = "Payload used as a return type for the completeTask mutation.")
public class CompleteTaskPayload {
  private final String message;
  private final String id;

  public CompleteTaskPayload(String message, String id) {
    this.message = message;
    this.id = id;
  }

  @GraphQLQuery(description = "Returned message of the called mutation.")
  public String getMessage() {
    return message;
  }

  @GraphQLQuery(description = "The ID of the completed Task.")
  public String getId() {
    return id;
  }
}
