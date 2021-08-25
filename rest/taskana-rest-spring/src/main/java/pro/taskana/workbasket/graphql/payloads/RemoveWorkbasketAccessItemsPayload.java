package pro.taskana.workbasket.graphql.payloads;

public class RemoveWorkbasketAccessItemsPayload {

  private final String message;
  private final String id;

  public RemoveWorkbasketAccessItemsPayload(String message, String id) {
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
