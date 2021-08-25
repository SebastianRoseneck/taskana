package pro.taskana.task.graphql.payloads;

import pro.taskana.task.common.models.TaskRepresentationModel;

public class UpdateTaskPayload {
  private final String message;
  private final TaskRepresentationModel task;

  public UpdateTaskPayload(String message, TaskRepresentationModel task) {
    this.message = message;
    this.task = task;
  }

  public String getMessage() {
    return message;
  }

  public TaskRepresentationModel getTask() {
    return task;
  }
}
