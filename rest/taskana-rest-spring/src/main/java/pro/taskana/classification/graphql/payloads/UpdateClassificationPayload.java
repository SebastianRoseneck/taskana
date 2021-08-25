package pro.taskana.classification.graphql.payloads;

import pro.taskana.classification.common.models.ClassificationRepresentationModel;

public class UpdateClassificationPayload {

  private final String message;
  private final ClassificationRepresentationModel classification;

  public UpdateClassificationPayload(
      String message, ClassificationRepresentationModel classification) {
    this.message = message;
    this.classification = classification;
  }

  public String getMessage() {
    return message;
  }

  public ClassificationRepresentationModel getClassification() {
    return classification;
  }
}
