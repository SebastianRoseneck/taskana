package pro.taskana.task.graphql.payloads;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.task.graphql.resolvers.TaskResolver#forceCancelClaimTask forceCancelClaimTask}
 * mutation.
 */
@GraphQLType(description = "Payload used as a return type for the forceCancelClaimTask mutation.")
public class ForceCancelClaimTaskPayload {

  private final String message;
  private final String id;

  public ForceCancelClaimTaskPayload(String message, String id) {
    this.message = message;
    this.id = id;
  }

  @GraphQLQuery(description = "Returned message of the called mutation.")
  public String getMessage() {
    return message;
  }

  @GraphQLQuery(description = "ID of the Task the claim was force-canceled on.")
  public String getId() {
    return id;
  }
}
