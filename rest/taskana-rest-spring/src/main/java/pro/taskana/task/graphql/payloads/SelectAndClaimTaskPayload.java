package pro.taskana.task.graphql.payloads;

public class SelectAndClaimTaskPayload {

  private final String message;
  private final String id;

  public SelectAndClaimTaskPayload(String message, String id) {
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
