package pro.taskana.workbasket.graphql.payloads;

import pro.taskana.workbasket.common.models.WorkbasketRepresentationModel;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.workbasket.graphql.resolvers.WorkbasketResolver#createWorkbasket createWorkbasket}
 * mutation.
 */
public class CreateWorkbasketPayload {

  private final String message;
  private final WorkbasketRepresentationModel workbasket;

  public CreateWorkbasketPayload(String message, WorkbasketRepresentationModel workbasket) {
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
