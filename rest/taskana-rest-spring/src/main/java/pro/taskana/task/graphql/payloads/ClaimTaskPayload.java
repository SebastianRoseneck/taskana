package pro.taskana.task.graphql.payloads;

public class ClaimTaskPayload {
  private final String message;
  private final String id;

  public ClaimTaskPayload(String message, String id) {
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
