package pro.taskana.classification.graphql.payloads;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

import pro.taskana.classification.common.models.ClassificationRepresentationModel;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.classification.graphql.resolvers.ClassificationResolver#updateClassification
 * updateClassification} mutation.
 */
@GraphQLType(description = "Payload used as a return type for the updateClassification mutation.")
public class UpdateClassificationPayload {

  private final String message;
  private final ClassificationRepresentationModel classification;

  public UpdateClassificationPayload(
      String message, ClassificationRepresentationModel classification) {
    this.message = message;
    this.classification = classification;
  }

  @GraphQLQuery(description = "Returned message of the called mutation.")
  public String getMessage() {
    return message;
  }

  @GraphQLQuery(description = "Updated Classification.")
  public ClassificationRepresentationModel getClassification() {
    return classification;
  }
}
