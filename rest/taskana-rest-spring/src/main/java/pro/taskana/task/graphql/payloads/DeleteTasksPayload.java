package pro.taskana.task.graphql.payloads;

import java.util.List;

public class DeleteTasksPayload {

  private final String message;
  private final List<String> ids;

  public DeleteTasksPayload(String message, List<String> ids) {
    this.message = message;
    this.ids = ids;
  }

  public String getMessage() {
    return message;
  }

  public List<String> getIds() {
    return ids;
  }
}
