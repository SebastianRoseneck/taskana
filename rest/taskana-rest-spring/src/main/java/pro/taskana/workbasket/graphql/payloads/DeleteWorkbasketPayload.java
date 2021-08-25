package pro.taskana.workbasket.graphql.payloads;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.workbasket.graphql.resolvers.WorkbasketResolver#deleteWorkbasket deleteWorkbasket}
 * mutation.
 */
public class DeleteWorkbasketPayload {

  private final String custom;

  public DeleteWorkbasketPayload(String custom) {
    this.custom = custom;
  }

  public String getCustom() {
    return custom;
  }
}
