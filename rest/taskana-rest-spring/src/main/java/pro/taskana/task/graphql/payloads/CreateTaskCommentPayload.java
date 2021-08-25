package pro.taskana.task.graphql.payloads;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

import pro.taskana.task.common.models.TaskCommentRepresentationModel;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.task.graphql.resolvers.TaskCommentResolver#createTaskComment createTaskComment}
 * mutation.
 */
@GraphQLType(description = "Payload used as a return type for the createTaskComment mutation.")
public class CreateTaskCommentPayload {

  private final String message;
  private final TaskCommentRepresentationModel taskComment;

  public CreateTaskCommentPayload(String message, TaskCommentRepresentationModel taskComment) {
    this.message = message;
    this.taskComment = taskComment;
  }

  @GraphQLQuery(description = "Returned message of the called mutation.")
  public String getMessage() {
    return message;
  }

  @GraphQLQuery(description = "The created TaskComment")
  public TaskCommentRepresentationModel getTaskComment() {
    return taskComment;
  }
}
