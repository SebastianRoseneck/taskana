package pro.taskana.task.graphql.payloads;

import pro.taskana.task.common.models.TaskCommentRepresentationModel;

public class CreateTaskCommentPayload {

  private final String message;
  private final TaskCommentRepresentationModel taskComment;

  public CreateTaskCommentPayload(String message, TaskCommentRepresentationModel taskComment) {
    this.message = message;
    this.taskComment = taskComment;
  }

  public String getMessage() {
    return message;
  }

  public TaskCommentRepresentationModel getTaskComment() {
    return taskComment;
  }
}
