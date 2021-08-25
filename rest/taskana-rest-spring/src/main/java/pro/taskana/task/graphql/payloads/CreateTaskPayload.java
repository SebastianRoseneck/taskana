package pro.taskana.task.graphql.payloads;

import pro.taskana.task.common.models.TaskRepresentationModel;

public class CreateTaskPayload {

  private final String message;
  private final TaskRepresentationModel task;

  public CreateTaskPayload(String message, TaskRepresentationModel task) {
    this.message = message;
    this.task = task;
  }

  public TaskRepresentationModel getTask() {
    return task;
  }

  public String getMessage() {
    return message;
  }
}
