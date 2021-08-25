package pro.taskana.classification.graphql.payloads;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.classification.graphql.resolvers.ClassificationResolver#deleteClassification
 * deleteClassification} mutation.
 */
@GraphQLType(description = "Payload used as a return type for the deleteClassification mutation.")
public class DeleteClassificationPayload {

  private final String message;
  private final String id;

  public DeleteClassificationPayload(String message, String id) {
    this.message = message;
    this.id = id;
  }

  @GraphQLQuery(description = "Returned message of the called mutation.")
  public String getMessage() {
    return message;
  }

  @GraphQLQuery(description = "ID of the deleted Classification.")
  public String getId() {
    return id;
  }
}
