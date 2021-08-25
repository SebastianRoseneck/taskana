package pro.taskana.task.graphql.payloads;

public class CompleteTaskPayload {
  private final String message;
  private final String id;

  public CompleteTaskPayload(String message, String id) {
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
