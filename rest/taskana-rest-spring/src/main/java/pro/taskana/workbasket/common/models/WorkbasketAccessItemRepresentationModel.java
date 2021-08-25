package pro.taskana.workbasket.common.models;

import io.leangen.graphql.annotations.GraphQLInputField;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import javax.validation.constraints.NotNull;
import org.springframework.hateoas.RepresentationModel;

import pro.taskana.workbasket.api.models.WorkbasketAccessItem;

/** EntityModel class for {@link WorkbasketAccessItem}. */
@GraphQLType(
    description = "WorkbasketAccessItems define the rights of a user or group for a Workbasket.")
public class WorkbasketAccessItemRepresentationModel
    extends RepresentationModel<WorkbasketAccessItemRepresentationModel> {

  /** Unique Id. */
  private String accessItemId;
  /** The workbasket Id. */
  @NotNull private String workbasketId;
  /** The Access Id. This could be either a user Id or a full qualified group Id. */
  @NotNull private String accessId;
  /** The workbasket key. */
  private String workbasketKey;
  /** The name. */
  private String accessName;
  /** The permission to read the information about the workbasket. */
  @GraphQLInputField(defaultValue = "false")
  private boolean permRead;
  /** The permission to view the content (the tasks) of a workbasket. */
  @GraphQLInputField(defaultValue = "false")
  private boolean permOpen;
  /**
   * The permission to add tasks to the workbasket. Required for creation and transferring of tasks.
   */
  @GraphQLInputField(defaultValue = "false")
  private boolean permAppend;
  /** The permission to transfer tasks (out of the current workbasket). */
  @GraphQLInputField(defaultValue = "false")
  private boolean permTransfer;
  /** The permission to distribute tasks from the workbasket. */
  @GraphQLInputField(defaultValue = "false")
  private boolean permDistribute;
  /** The custom permission with the name "1". */
  @GraphQLInputField(defaultValue = "false")
  private boolean permCustom1;
  /** The custom permission with the name "2". */
  @GraphQLInputField(defaultValue = "false")
  private boolean permCustom2;
  /** The custom permission with the name "3". */
  @GraphQLInputField(defaultValue = "false")
  private boolean permCustom3;
  /** The custom permission with the name "4". */
  @GraphQLInputField(defaultValue = "false")
  private boolean permCustom4;
  /** The custom permission with the name "5". */
  @GraphQLInputField(defaultValue = "false")
  private boolean permCustom5;
  /** The custom permission with the name "6". */
  @GraphQLInputField(defaultValue = "false")
  private boolean permCustom6;
  /** The custom permission with the name "7". */
  @GraphQLInputField(defaultValue = "false")
  private boolean permCustom7;
  /** The custom permission with the name "8". */
  @GraphQLInputField(defaultValue = "false")
  private boolean permCustom8;
  /** The custom permission with the name "9". */
  @GraphQLInputField(defaultValue = "false")
  private boolean permCustom9;
  /** The custom permission with the name "10". */
  @GraphQLInputField(defaultValue = "false")
  private boolean permCustom10;
  /** The custom permission with the name "11". */
  @GraphQLInputField(defaultValue = "false")
  private boolean permCustom11;
  /** The custom permission with the name "12". */
  @GraphQLInputField(defaultValue = "false")
  private boolean permCustom12;

  @GraphQLQuery(description = "Unique Id.")
  public String getAccessItemId() {
    return accessItemId;
  }

  public void setAccessItemId(String accessItemId) {
    this.accessItemId = accessItemId;
  }

  @GraphQLQuery(description = "The workbasket Id.")
  public String getWorkbasketId() {
    return workbasketId;
  }

  public void setWorkbasketId(String workbasketId) {
    this.workbasketId = workbasketId;
  }

  @GraphQLQuery(description = "The workbasket key.")
  public String getWorkbasketKey() {
    return workbasketKey;
  }

  public void setWorkbasketKey(String workbasketKey) {
    this.workbasketKey = workbasketKey;
  }

  @GraphQLQuery(
      description = "The Access Id. This could be either a user Id or a full qualified group Id.")
  public String getAccessId() {
    return accessId;
  }

  public void setAccessId(String accessId) {
    this.accessId = accessId;
  }

  @GraphQLQuery(description = "The name.")
  public String getAccessName() {
    return accessName;
  }

  public void setAccessName(String accessName) {
    this.accessName = accessName;
  }

  @GraphQLQuery(description = "The permission to read the information about the workbasket.")
  public boolean isPermRead() {
    return permRead;
  }

  public void setPermRead(boolean permRead) {
    this.permRead = permRead;
  }

  @GraphQLQuery(description = "The permission to view the content (the tasks) of a workbasket.")
  public boolean isPermOpen() {
    return permOpen;
  }

  public void setPermOpen(boolean permOpen) {
    this.permOpen = permOpen;
  }

  @GraphQLQuery(
      description =
          "The permission to add tasks to the workbasket. Required for creation and transferring of"
              + " tasks.")
  public boolean isPermAppend() {
    return permAppend;
  }

  public void setPermAppend(boolean permAppend) {
    this.permAppend = permAppend;
  }

  @GraphQLQuery(description = "The permission to transfer tasks (out of the current workbasket).")
  public boolean isPermTransfer() {
    return permTransfer;
  }

  public void setPermTransfer(boolean permTransfer) {
    this.permTransfer = permTransfer;
  }

  @GraphQLQuery(description = "The permission to distribute tasks from the workbasket.")
  public boolean isPermDistribute() {
    return permDistribute;
  }

  public void setPermDistribute(boolean permDistribute) {
    this.permDistribute = permDistribute;
  }

  @GraphQLQuery(description = "The custom permission with the name \"1\".")
  public boolean isPermCustom1() {
    return permCustom1;
  }

  public void setPermCustom1(boolean permCustom1) {
    this.permCustom1 = permCustom1;
  }

  @GraphQLQuery(description = "The custom permission with the name \"2\".")
  public boolean isPermCustom2() {
    return permCustom2;
  }

  public void setPermCustom2(boolean permCustom2) {
    this.permCustom2 = permCustom2;
  }

  @GraphQLQuery(description = "The custom permission with the name \"3\".")
  public boolean isPermCustom3() {
    return permCustom3;
  }

  public void setPermCustom3(boolean permCustom3) {
    this.permCustom3 = permCustom3;
  }

  @GraphQLQuery(description = "The custom permission with the name \"4\".")
  public boolean isPermCustom4() {
    return permCustom4;
  }

  public void setPermCustom4(boolean permCustom4) {
    this.permCustom4 = permCustom4;
  }

  @GraphQLQuery(description = "The custom permission with the name \"5\".")
  public boolean isPermCustom5() {
    return permCustom5;
  }

  public void setPermCustom5(boolean permCustom5) {
    this.permCustom5 = permCustom5;
  }

  @GraphQLQuery(description = "The custom permission with the name \"6\".")
  public boolean isPermCustom6() {
    return permCustom6;
  }

  public void setPermCustom6(boolean permCustom6) {
    this.permCustom6 = permCustom6;
  }

  @GraphQLQuery(description = "The custom permission with the name \"7\".")
  public boolean isPermCustom7() {
    return permCustom7;
  }

  public void setPermCustom7(boolean permCustom7) {
    this.permCustom7 = permCustom7;
  }

  @GraphQLQuery(description = "The custom permission with the name \"8\".")
  public boolean isPermCustom8() {
    return permCustom8;
  }

  public void setPermCustom8(boolean permCustom8) {
    this.permCustom8 = permCustom8;
  }

  @GraphQLQuery(description = "The custom permission with the name \"9\".")
  public boolean isPermCustom9() {
    return permCustom9;
  }

  public void setPermCustom9(boolean permCustom9) {
    this.permCustom9 = permCustom9;
  }

  @GraphQLQuery(description = "The custom permission with the name \"10\".")
  public boolean isPermCustom10() {
    return permCustom10;
  }

  public void setPermCustom10(boolean permCustom10) {
    this.permCustom10 = permCustom10;
  }

  @GraphQLQuery(description = "The custom permission with the name \"11\".")
  public boolean isPermCustom11() {
    return permCustom11;
  }

  public void setPermCustom11(boolean permCustom11) {
    this.permCustom11 = permCustom11;
  }

  @GraphQLQuery(description = "The custom permission with the name \"12\".")
  public boolean isPermCustom12() {
    return permCustom12;
  }

  public void setPermCustom12(boolean permCustom12) {
    this.permCustom12 = permCustom12;
  }
}
