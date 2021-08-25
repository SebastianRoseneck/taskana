package pro.taskana.workbasket.common.models;

import org.springframework.hateoas.RepresentationModel;

import pro.taskana.workbasket.api.models.WorkbasketAccessItem;

/** EntityModel class for {@link WorkbasketAccessItem}. */
public class WorkbasketAccessItemRepresentationModel
    extends RepresentationModel<WorkbasketAccessItemRepresentationModel> {

  /** Unique Id. */
  private String accessItemId;
  /** The workbasket Id. */
  private String workbasketId;
  /** The Access Id. This could be either a user Id or a full qualified group Id. */
  private String accessId;
  /** The workbasket key. */
  private String workbasketKey;
  /** The name. */
  private String accessName;
  /** The permission to read the information about the workbasket. */
  private Boolean permRead;
  /** The permission to view the content (the tasks) of a workbasket. */
  private Boolean permOpen;
  /**
   * The permission to add tasks to the workbasket. Required for creation and transferring of tasks.
   */
  private Boolean permAppend;
  /** The permission to transfer tasks (out of the current workbasket). */
  private Boolean permTransfer;
  /** The permission to distribute tasks from the workbasket. */
  private Boolean permDistribute;
  /** The custom permission with the name "1". */
  private Boolean permCustom1;
  /** The custom permission with the name "2". */
  private Boolean permCustom2;
  /** The custom permission with the name "3". */
  private Boolean permCustom3;
  /** The custom permission with the name "4". */
  private Boolean permCustom4;
  /** The custom permission with the name "5". */
  private Boolean permCustom5;
  /** The custom permission with the name "6". */
  private Boolean permCustom6;
  /** The custom permission with the name "7". */
  private Boolean permCustom7;
  /** The custom permission with the name "8". */
  private Boolean permCustom8;
  /** The custom permission with the name "9". */
  private Boolean permCustom9;
  /** The custom permission with the name "10". */
  private Boolean permCustom10;
  /** The custom permission with the name "11". */
  private Boolean permCustom11;
  /** The custom permission with the name "12". */
  private Boolean permCustom12;

  public String getAccessItemId() {
    return accessItemId;
  }

  public void setAccessItemId(String accessItemId) {
    this.accessItemId = accessItemId;
  }

  public String getWorkbasketId() {
    return workbasketId;
  }

  public void setWorkbasketId(String workbasketId) {
    this.workbasketId = workbasketId;
  }

  public String getWorkbasketKey() {
    return workbasketKey;
  }

  public void setWorkbasketKey(String workbasketKey) {
    this.workbasketKey = workbasketKey;
  }

  public String getAccessId() {
    return accessId;
  }

  public void setAccessId(String accessId) {
    this.accessId = accessId;
  }

  public String getAccessName() {
    return accessName;
  }

  public void setAccessName(String accessName) {
    this.accessName = accessName;
  }

  public Boolean isPermRead() {
    return permRead;
  }

  public void setPermRead(Boolean permRead) {
    this.permRead = permRead;
  }

  public Boolean isPermOpen() {
    return permOpen;
  }

  public void setPermOpen(Boolean permOpen) {
    this.permOpen = permOpen;
  }

  public Boolean isPermAppend() {
    return permAppend;
  }

  public void setPermAppend(Boolean permAppend) {
    this.permAppend = permAppend;
  }

  public Boolean isPermTransfer() {
    return permTransfer;
  }

  public void setPermTransfer(Boolean permTransfer) {
    this.permTransfer = permTransfer;
  }

  public Boolean isPermDistribute() {
    return permDistribute;
  }

  public void setPermDistribute(Boolean permDistribute) {
    this.permDistribute = permDistribute;
  }

  public Boolean isPermCustom1() {
    return permCustom1;
  }

  public void setPermCustom1(Boolean permCustom1) {
    this.permCustom1 = permCustom1;
  }

  public Boolean isPermCustom2() {
    return permCustom2;
  }

  public void setPermCustom2(Boolean permCustom2) {
    this.permCustom2 = permCustom2;
  }

  public Boolean isPermCustom3() {
    return permCustom3;
  }

  public void setPermCustom3(Boolean permCustom3) {
    this.permCustom3 = permCustom3;
  }

  public Boolean isPermCustom4() {
    return permCustom4;
  }

  public void setPermCustom4(Boolean permCustom4) {
    this.permCustom4 = permCustom4;
  }

  public Boolean isPermCustom5() {
    return permCustom5;
  }

  public void setPermCustom5(Boolean permCustom5) {
    this.permCustom5 = permCustom5;
  }

  public Boolean isPermCustom6() {
    return permCustom6;
  }

  public void setPermCustom6(Boolean permCustom6) {
    this.permCustom6 = permCustom6;
  }

  public Boolean isPermCustom7() {
    return permCustom7;
  }

  public void setPermCustom7(Boolean permCustom7) {
    this.permCustom7 = permCustom7;
  }

  public Boolean isPermCustom8() {
    return permCustom8;
  }

  public void setPermCustom8(Boolean permCustom8) {
    this.permCustom8 = permCustom8;
  }

  public Boolean isPermCustom9() {
    return permCustom9;
  }

  public void setPermCustom9(Boolean permCustom9) {
    this.permCustom9 = permCustom9;
  }

  public Boolean isPermCustom10() {
    return permCustom10;
  }

  public void setPermCustom10(Boolean permCustom10) {
    this.permCustom10 = permCustom10;
  }

  public Boolean isPermCustom11() {
    return permCustom11;
  }

  public void setPermCustom11(Boolean permCustom11) {
    this.permCustom11 = permCustom11;
  }

  public Boolean isPermCustom12() {
    return permCustom12;
  }

  public void setPermCustom12(Boolean permCustom12) {
    this.permCustom12 = permCustom12;
  }
}
