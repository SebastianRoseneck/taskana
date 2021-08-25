package pro.taskana.workbasket.graphql.payloads;

public class DeleteWorkbasketPayload {

  private final String custom;

  public DeleteWorkbasketPayload(String custom) {
    this.custom = custom;
  }

  public String getCustom() {
    return custom;
  }
}
