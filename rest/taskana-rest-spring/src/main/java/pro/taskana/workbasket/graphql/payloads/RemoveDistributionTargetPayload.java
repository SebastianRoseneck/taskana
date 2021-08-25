package pro.taskana.workbasket.graphql.payloads;

public class RemoveDistributionTargetPayload {

  private final String message;
  private final String id;

  public RemoveDistributionTargetPayload(String message, String id) {
    this.message = message;
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public String getId() {
    return id;
  }
}
