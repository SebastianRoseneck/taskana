package pro.taskana.workbasket.graphql.payloads;

import pro.taskana.workbasket.common.models.WorkbasketRepresentationModel;

public class UpdateWorkbasketPayload {

  private final String message;
  private final WorkbasketRepresentationModel workbasket;

  public UpdateWorkbasketPayload(String message, WorkbasketRepresentationModel workbasket) {
    this.message = message;
    this.workbasket = workbasket;
  }

  public String getMessage() {
    return message;
  }

  public WorkbasketRepresentationModel getWorkbasket() {
    return workbasket;
  }
}
