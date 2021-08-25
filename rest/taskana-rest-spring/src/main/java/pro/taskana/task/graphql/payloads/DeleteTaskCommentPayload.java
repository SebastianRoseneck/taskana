package pro.taskana.task.graphql.payloads;

public class DeleteTaskCommentPayload {

  private final String message;
  private final String id;

  public DeleteTaskCommentPayload(String message, String id) {
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
