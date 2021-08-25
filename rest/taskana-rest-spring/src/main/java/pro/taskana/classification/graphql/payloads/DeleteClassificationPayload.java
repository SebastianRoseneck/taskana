package pro.taskana.classification.graphql.payloads;

public class DeleteClassificationPayload {

  private final String message;
  private final String id;

  public DeleteClassificationPayload(String message, String id) {
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
