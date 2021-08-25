package pro.taskana.task.graphql.payloads;

import pro.taskana.task.common.models.TaskCommentRepresentationModel;

public class UpdateTaskCommentPayload {

  private final String message;
  private final TaskCommentRepresentationModel taskComment;

  public UpdateTaskCommentPayload(String message, TaskCommentRepresentationModel taskComment) {
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
