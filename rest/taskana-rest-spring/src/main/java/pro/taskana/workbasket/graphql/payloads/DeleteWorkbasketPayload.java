package pro.taskana.workbasket.graphql.payloads;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.workbasket.graphql.resolvers.WorkbasketResolver#deleteWorkbasket deleteWorkbasket}
 * mutation.
 */
public class DeleteWorkbasketPayload {

  private final String message;

  public DeleteWorkbasketPayload(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
