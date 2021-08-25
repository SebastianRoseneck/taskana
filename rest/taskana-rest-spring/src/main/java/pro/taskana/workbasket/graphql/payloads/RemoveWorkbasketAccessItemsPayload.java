package pro.taskana.workbasket.graphql.payloads;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.workbasket.graphql.resolvers.WorkbasketAccessItemResolver#removeWorkbasketAccessItems
 * removeWorkbasketAccessItems} mutation.
 */
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
