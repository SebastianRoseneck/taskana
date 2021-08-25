package pro.taskana.task.graphql.payloads;

public class TransferTaskPayload {

  private final String message;
  private final String taskId;
  private final String workbasketId;

  public TransferTaskPayload(String message, String taskId, String workbasketId) {
    this.message = message;
    this.taskId = taskId;
    this.workbasketId = workbasketId;
  }

  public String getMessage() {
    return message;
  }

  public String getTaskId() {
    return taskId;
  }

  public String getWorkbasketId() {
    return workbasketId;
  }
}
