package pro.taskana.task.common.models;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.time.Instant;
import org.springframework.hateoas.RepresentationModel;

import pro.taskana.task.api.models.TaskComment;

/** EntityModel class for {@link TaskComment}. */
@GraphQLType(description = "Representing a comment in a Task.")
public class TaskCommentRepresentationModel
    extends RepresentationModel<TaskCommentRepresentationModel> {

  /** Unique Id. */
  private String taskCommentId;
  /** Task Id. Can identify the task the comment belongs to. */
  private String taskId;
  /** The content of the comment. */
  private String textField;
  /** The creator of the task comment. */
  private String creator;
  /** The creation timestamp in the system. */
  private Instant created;
  /** Timestamp of the last task comment modification. */
  private Instant modified;

  @GraphQLQuery(description = "Unique Id.")
  public String getTaskCommentId() {
    return taskCommentId;
  }

  public void setTaskCommentId(String taskCommentId) {
    this.taskCommentId = taskCommentId;
  }

  @GraphQLQuery(description = "Task Id. Can identify the task the comment belongs to.")
  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  @GraphQLQuery(description = "The content of the comment.")
  public String getTextField() {
    return textField;
  }

  public void setTextField(String textField) {
    this.textField = textField;
  }

  @GraphQLQuery(description = "The creator of the task comment.")
  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  @GraphQLQuery(description = "The creation timestamp in the system.")
  public Instant getCreated() {
    return created;
  }

  public void setCreated(Instant created) {
    this.created = created;
  }

  @GraphQLQuery(description = "Timestamp of the last task comment modification.")
  public Instant getModified() {
    return modified;
  }

  public void setModified(Instant modified) {
    this.modified = modified;
  }
}
