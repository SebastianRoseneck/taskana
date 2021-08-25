package pro.taskana.task.common.models;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.hateoas.RepresentationModel;

import pro.taskana.classification.common.models.ClassificationSummaryRepresentationModel;
import pro.taskana.task.api.TaskState;
import pro.taskana.workbasket.api.models.WorkbasketSummary;
import pro.taskana.workbasket.common.models.WorkbasketSummaryRepresentationModel;

/** EntityModel class for {@link WorkbasketSummary}. */
@GraphQLType(
    description =
        "Tasks are the main entity of Taskana. Each task has its describing attributes like"
            + " priority and due date.This is a specific short model-object which only requires the"
            + " most important information.")
public class TaskSummaryRepresentationModel
    extends RepresentationModel<TaskSummaryRepresentationModel> {

  /** Unique Id. */
  protected String taskId;
  /**
   * External Id. Can be used to enforce idempotence at task creation. Can identify an external
   * task.
   */
  protected String externalId;
  /** The creation timestamp in the system. */
  protected Instant created;
  /** The timestamp of the last claim-operation. */
  protected Instant claimed;
  /** The timestamp of the completion. */
  protected Instant completed;
  /** The timestamp of the last modification. */
  protected Instant modified;
  /**
   * Planned start of the task. The actual completion of the task should be between PLANNED and DUE.
   */
  protected Instant planned;
  /**
   * Timestamp when the task has been received. It notes when the surrounding process started and
   * not just when the actual task was created.
   */
  protected Instant received;
  /**
   * Timestamp when the task is due. The actual completion of the task should be between PLANNED and
   * DUE.
   */
  protected Instant due;
  /** The name of the task. */
  protected String name;
  /** the creator of the task. */
  protected String creator;
  /** note. */
  protected String note;
  /** The description of the task. */
  protected String description;
  /** The priority of the task. */
  @NotNull protected Integer priority;
  /** The current task state. */
  protected TaskState state;
  /** The classification of this task. */
  @NotNull protected ClassificationSummaryRepresentationModel classificationSummary;
  /** The workbasket this task resides in. */
  @NotNull protected WorkbasketSummaryRepresentationModel workbasketSummary;
  /** The business process id. */
  protected String businessProcessId;
  /** the parent business process id. */
  protected String parentBusinessProcessId;
  /** The owner of the task. The owner is set upon claiming of the task. */
  protected String owner;
  /** The Objects primary ObjectReference. */
  @NotNull protected ObjectReferenceRepresentationModel primaryObjRef;
  /** Indicator if the task has been read. */
  protected Boolean isRead;
  /** Indicator if the task has been transferred. */
  protected Boolean isTransferred;
  /** A custom property with name "1". */
  protected String custom1;
  /** A custom property with name "2". */
  protected String custom2;
  /** A custom property with name "3". */
  protected String custom3;
  /** A custom property with name "4". */
  protected String custom4;
  /** A custom property with name "5". */
  protected String custom5;
  /** A custom property with name "6". */
  protected String custom6;
  /** A custom property with name "7". */
  protected String custom7;
  /** A custom property with name "8". */
  protected String custom8;
  /** A custom property with name "9". */
  protected String custom9;
  /** A custom property with name "10". */
  protected String custom10;
  /** A custom property with name "11". */
  protected String custom11;
  /** A custom property with name "12". */
  protected String custom12;
  /** A custom property with name "13". */
  protected String custom13;
  /** A custom property with name "14". */
  protected String custom14;
  /** A custom property with name "15". */
  protected String custom15;
  /** A custom property with name "16". */
  protected String custom16;

  /** The attachment summaries of this task. */
  private List<AttachmentSummaryRepresentationModel> attachmentSummaries = new ArrayList<>();

  @GraphQLQuery(description = "Unique Id.")
  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  @GraphQLQuery(
      description =
          "External Id. Can be used to enforce idempotence at task creation. Can identify an"
              + " external task.")
  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  @GraphQLQuery(description = "The creation timestamp in the system.")
  public Instant getCreated() {
    return created;
  }

  public void setCreated(Instant created) {
    this.created = created;
  }

  @GraphQLQuery(description = "The timestamp of the last claim-operation.")
  public Instant getClaimed() {
    return claimed;
  }

  public void setClaimed(Instant claimed) {
    this.claimed = claimed;
  }

  @GraphQLQuery(description = "The timestamp of the completion.")
  public Instant getCompleted() {
    return completed;
  }

  public void setCompleted(Instant completed) {
    this.completed = completed;
  }

  @GraphQLQuery(description = "The timestamp of the last modification.")
  public Instant getModified() {
    return modified;
  }

  public void setModified(Instant modified) {
    this.modified = modified;
  }

  @GraphQLQuery(
      description =
          "Planned start of the task. The actual completion of the task should be between PLANNED"
              + " and DUE.")
  public Instant getPlanned() {
    return planned;
  }

  public void setPlanned(Instant planned) {
    this.planned = planned;
  }

  @GraphQLQuery(
      description =
          "Timestamp when the task has been received. It notes when the surrounding process started"
              + " and not just when the actual task was created.")
  public Instant getReceived() {
    return received;
  }

  public void setReceived(Instant received) {
    this.received = received;
  }

  @GraphQLQuery(
      description =
          "Timestamp when the task is due. The actual completion of the task should be between"
              + " PLANNED and DUE.")
  public Instant getDue() {
    return due;
  }

  public void setDue(Instant due) {
    this.due = due;
  }

  @GraphQLQuery(description = "The name of the task.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @GraphQLQuery(description = "The creator of the task.")
  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  @GraphQLQuery(description = "Note.")
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  @GraphQLQuery(description = "The description of the task.")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @GraphQLQuery(description = "The priority of the task.")
  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  @GraphQLQuery(
      description =
          "The current task state.\n"
              + "\n"
              + "Must be one of [READY, CLAIMED, COMPLETED, CANCELLED, TERMINATED].")
  public TaskState getState() {
    return state;
  }

  public void setState(TaskState state) {
    this.state = state;
  }

  @GraphQLQuery(description = "The classification of this task.")
  public ClassificationSummaryRepresentationModel getClassificationSummary() {
    return classificationSummary;
  }

  public void setClassificationSummary(
      ClassificationSummaryRepresentationModel classificationSummary) {
    this.classificationSummary = classificationSummary;
  }

  @GraphQLQuery(description = "The workbasket this task resides in.")
  public WorkbasketSummaryRepresentationModel getWorkbasketSummary() {
    return workbasketSummary;
  }

  public void setWorkbasketSummary(WorkbasketSummaryRepresentationModel workbasketSummary) {
    this.workbasketSummary = workbasketSummary;
  }

  @GraphQLQuery(description = "The business process id.")
  public String getBusinessProcessId() {
    return businessProcessId;
  }

  public void setBusinessProcessId(String businessProcessId) {
    this.businessProcessId = businessProcessId;
  }

  @GraphQLQuery(description = "The parent business process id.")
  public String getParentBusinessProcessId() {
    return parentBusinessProcessId;
  }

  public void setParentBusinessProcessId(String parentBusinessProcessId) {
    this.parentBusinessProcessId = parentBusinessProcessId;
  }

  @GraphQLQuery(description = "The owner of the task. The owner is set upon claiming of the task.")
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  @GraphQLQuery(description = "The Objects primary ObjectReference.")
  public ObjectReferenceRepresentationModel getPrimaryObjRef() {
    return primaryObjRef;
  }

  public void setPrimaryObjRef(ObjectReferenceRepresentationModel primaryObjRef) {
    this.primaryObjRef = primaryObjRef;
  }

  @GraphQLQuery(description = "Indicator if the task has been read.")
  public Boolean isRead() {
    return isRead;
  }

  public void setRead(Boolean isRead) {
    this.isRead = isRead;
  }

  @GraphQLQuery(description = "Indicator if the task has been transferred.")
  public Boolean isTransferred() {
    return isTransferred;
  }

  public void setTransferred(Boolean isTransferred) {
    this.isTransferred = isTransferred;
  }

  @GraphQLQuery(description = "Attachments of the task.")
  public List<AttachmentSummaryRepresentationModel> getAttachmentSummaries() {
    return attachmentSummaries;
  }

  public void setAttachmentSummaries(
      List<AttachmentSummaryRepresentationModel> attachmentSummaries) {
    this.attachmentSummaries = attachmentSummaries;
  }

  @GraphQLQuery(description = "A custom property with name \"1\".")
  public String getCustom1() {
    return custom1;
  }

  public void setCustom1(String custom1) {
    this.custom1 = custom1;
  }

  @GraphQLQuery(description = "A custom property with name \"2\".")
  public String getCustom2() {
    return custom2;
  }

  public void setCustom2(String custom2) {
    this.custom2 = custom2;
  }

  @GraphQLQuery(description = "A custom property with name \"3\".")
  public String getCustom3() {
    return custom3;
  }

  public void setCustom3(String custom3) {
    this.custom3 = custom3;
  }

  @GraphQLQuery(description = "A custom property with name \"4\".")
  public String getCustom4() {
    return custom4;
  }

  public void setCustom4(String custom4) {
    this.custom4 = custom4;
  }

  @GraphQLQuery(description = "A custom property with name \"5\".")
  public String getCustom5() {
    return custom5;
  }

  public void setCustom5(String custom5) {
    this.custom5 = custom5;
  }

  @GraphQLQuery(description = "A custom property with name \"6\".")
  public String getCustom6() {
    return custom6;
  }

  public void setCustom6(String custom6) {
    this.custom6 = custom6;
  }

  @GraphQLQuery(description = "A custom property with name \"7\".")
  public String getCustom7() {
    return custom7;
  }

  public void setCustom7(String custom7) {
    this.custom7 = custom7;
  }

  @GraphQLQuery(description = "A custom property with name \"8\".")
  public String getCustom8() {
    return custom8;
  }

  public void setCustom8(String custom8) {
    this.custom8 = custom8;
  }

  @GraphQLQuery(description = "A custom property with name \"9\".")
  public String getCustom9() {
    return custom9;
  }

  public void setCustom9(String custom9) {
    this.custom9 = custom9;
  }

  @GraphQLQuery(description = "A custom property with name \"10\".")
  public String getCustom10() {
    return custom10;
  }

  public void setCustom10(String custom10) {
    this.custom10 = custom10;
  }

  @GraphQLQuery(description = "A custom property with name \"11\".")
  public String getCustom11() {
    return custom11;
  }

  public void setCustom11(String custom11) {
    this.custom11 = custom11;
  }

  @GraphQLQuery(description = "A custom property with name \"12\".")
  public String getCustom12() {
    return custom12;
  }

  public void setCustom12(String custom12) {
    this.custom12 = custom12;
  }

  @GraphQLQuery(description = "A custom property with name \"13\".")
  public String getCustom13() {
    return custom13;
  }

  public void setCustom13(String custom13) {
    this.custom13 = custom13;
  }

  @GraphQLQuery(description = "A custom property with name \"14\".")
  public String getCustom14() {
    return custom14;
  }

  public void setCustom14(String custom14) {
    this.custom14 = custom14;
  }

  @GraphQLQuery(description = "A custom property with name \"15\".")
  public String getCustom15() {
    return custom15;
  }

  public void setCustom15(String custom15) {
    this.custom15 = custom15;
  }

  @GraphQLQuery(description = "A custom property with name \"16\".")
  public String getCustom16() {
    return custom16;
  }

  public void setCustom16(String custom16) {
    this.custom16 = custom16;
  }
}
