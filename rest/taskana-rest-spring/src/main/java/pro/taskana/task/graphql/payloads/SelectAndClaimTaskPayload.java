package pro.taskana.task.graphql.payloads;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.task.graphql.resolvers.TaskResolver#selectAndClaimTask selectAndClaimTask} mutation.
 */
@GraphQLType(description = "Payload used as a return type for the selectAndClaimTask mutation.")
public class SelectAndClaimTaskPayload {

  private final String message;
  private final String id;

  public SelectAndClaimTaskPayload(String message, String id) {
    this.message = message;
    this.id = id;
  }

  @GraphQLQuery(description = "Returned message of the called mutation.")
  public String getMessage() {
    return message;
  }

  @GraphQLQuery(description = "The ID of the selected and claimed Task.")
  public String getId() {
    return id;
  }
}
