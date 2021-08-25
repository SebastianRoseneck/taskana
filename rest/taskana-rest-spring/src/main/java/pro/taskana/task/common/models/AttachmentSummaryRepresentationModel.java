package pro.taskana.task.common.models;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.time.Instant;
import org.springframework.hateoas.RepresentationModel;

import pro.taskana.classification.common.models.ClassificationSummaryRepresentationModel;
import pro.taskana.task.api.models.AttachmentSummary;

/** EntityModel class for {@link AttachmentSummary}. */
@GraphQLType(
    description =
        "An Attachment offers the possibility of providing  further information to Tasks. \n"
            + "This is a specific short model-object which only requires the most important"
            + " information.")
public class AttachmentSummaryRepresentationModel
    extends RepresentationModel<AttachmentSummaryRepresentationModel> {

  /** Unique Id. */
  protected String attachmentId;
  /** the referenced task id. */
  protected String taskId;
  /** The creation timestamp in the system. */
  protected Instant created;
  /** The timestamp of the last modification. */
  protected Instant modified;
  /** The timestamp of the entry date. */
  protected Instant received;
  /** The classification of this attachment. */
  protected ClassificationSummaryRepresentationModel classificationSummary;
  /** The Objects primary ObjectReference. */
  protected ObjectReferenceRepresentationModel objectReference;
  /** Determines on which channel this attachment was received. */
  protected String channel;

  @GraphQLQuery(description = "Unique Id.")
  public String getAttachmentId() {
    return attachmentId;
  }

  public void setAttachmentId(String attachmentId) {
    this.attachmentId = attachmentId;
  }

  @GraphQLQuery(description = "The referenced task id.")
  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  @GraphQLQuery(description = "The creation timestamp in the system.")
  public Instant getCreated() {
    return created;
  }

  public void setCreated(Instant created) {
    this.created = created;
  }

  @GraphQLQuery(description = "The timestamp of the last modification.")
  public Instant getModified() {
    return modified;
  }

  public void setModified(Instant modified) {
    this.modified = modified;
  }

  @GraphQLQuery(description = "The timestamp of the entry date.")
  public Instant getReceived() {
    return received;
  }

  public void setReceived(Instant received) {
    this.received = received;
  }

  @GraphQLQuery(description = "The classification of this attachment.")
  public ClassificationSummaryRepresentationModel getClassificationSummary() {
    return classificationSummary;
  }

  public void setClassificationSummary(
      ClassificationSummaryRepresentationModel classificationSummary) {
    this.classificationSummary = classificationSummary;
  }

  @GraphQLQuery(description = "The Objects primary ObjectReference.")
  public ObjectReferenceRepresentationModel getObjectReference() {
    return objectReference;
  }

  public void setObjectReference(ObjectReferenceRepresentationModel objectReference) {
    this.objectReference = objectReference;
  }

  @GraphQLQuery(description = "Determines on which channel this attachment was received.")
  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }
}
