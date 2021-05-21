package pro.taskana.workbasket.internal;

import static pro.taskana.common.internal.util.CheckedFunction.wrap;

import java.security.PrivilegedAction;
import java.util.function.Function;
import javax.security.auth.Subject;

import pro.taskana.common.api.security.UserPrincipal;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.models.WorkbasketAccessItem;
import pro.taskana.workbasket.internal.models.WorkbasketAccessItemImpl;

public class WorkbasketAccessItemBuilder {

  WorkbasketAccessItemImpl testWorkbasketAccessItem = new WorkbasketAccessItemImpl();

  public static WorkbasketAccessItemBuilder newWorkbasketAccessItem() {
    return new WorkbasketAccessItemBuilder();
  }

  public WorkbasketAccessItemBuilder workbasketId(String workbasketId) {
    testWorkbasketAccessItem.setWorkbasketId(workbasketId);
    return this;
  }

  public WorkbasketAccessItemBuilder accessId(String accessId) {
    testWorkbasketAccessItem.setAccessId(accessId);
    return this;
  }

  public WorkbasketAccessItemBuilder accessName(String accessName) {
    testWorkbasketAccessItem.setAccessName(accessName);
    return this;
  }

  public WorkbasketAccessItemBuilder permRead(boolean permRead) {
    testWorkbasketAccessItem.setPermRead(permRead);
    return this;
  }

  public WorkbasketAccessItemBuilder permOpen(boolean permOpen) {
    testWorkbasketAccessItem.setPermOpen(permOpen);
    return this;
  }

  public WorkbasketAccessItemBuilder permAppend(boolean permAppend) {
    testWorkbasketAccessItem.setPermAppend(permAppend);
    return this;
  }

  public WorkbasketAccessItemBuilder permTransfer(boolean permTransfer) {
    testWorkbasketAccessItem.setPermTransfer(permTransfer);
    return this;
  }

  public WorkbasketAccessItemBuilder permDistribute(boolean permDistribute) {
    testWorkbasketAccessItem.setPermDistribute(permDistribute);
    return this;
  }

  public WorkbasketAccessItemBuilder permCustom1(boolean permCustom1) {
    testWorkbasketAccessItem.setPermCustom1(permCustom1);
    return this;
  }

  public WorkbasketAccessItemBuilder permCustom2(boolean permCustom2) {
    testWorkbasketAccessItem.setPermCustom2(permCustom2);
    return this;
  }

  public WorkbasketAccessItemBuilder permCustom3(boolean permCustom3) {
    testWorkbasketAccessItem.setPermCustom3(permCustom3);
    return this;
  }

  public WorkbasketAccessItemBuilder permCustom4(boolean permCustom4) {
    testWorkbasketAccessItem.setPermCustom4(permCustom4);
    return this;
  }

  public WorkbasketAccessItemBuilder permCustom5(boolean permCustom5) {
    testWorkbasketAccessItem.setPermCustom5(permCustom5);
    return this;
  }

  public WorkbasketAccessItemBuilder permCustom6(boolean permCustom6) {
    testWorkbasketAccessItem.setPermCustom6(permCustom6);
    return this;
  }

  public WorkbasketAccessItemBuilder permCustom7(boolean permCustom7) {
    testWorkbasketAccessItem.setPermCustom7(permCustom7);
    return this;
  }

  public WorkbasketAccessItemBuilder permCustom8(boolean permCustom8) {
    testWorkbasketAccessItem.setPermCustom8(permCustom8);
    return this;
  }

  public WorkbasketAccessItemBuilder permCustom9(boolean permCustom9) {
    testWorkbasketAccessItem.setPermCustom9(permCustom9);
    return this;
  }

  public WorkbasketAccessItemBuilder permCustom10(boolean permCustom10) {
    testWorkbasketAccessItem.setPermCustom10(permCustom10);
    return this;
  }

  public WorkbasketAccessItemBuilder permCustom11(boolean permCustom11) {
    testWorkbasketAccessItem.setPermCustom11(permCustom11);
    return this;
  }

  public WorkbasketAccessItemBuilder permCustom12(boolean permCustom12) {
    testWorkbasketAccessItem.setPermCustom12(permCustom12);
    return this;
  }

  public WorkbasketAccessItem buildAndStore(WorkbasketService workbasketService) throws Exception {
    testWorkbasketAccessItem.setId(null);
    WorkbasketAccessItem w = workbasketService.createWorkbasketAccessItem(testWorkbasketAccessItem);
    WorkbasketAccessItem workbasketAccessItem = w.copy();
    ((WorkbasketAccessItemImpl) workbasketAccessItem).setId(w.getId());
    return workbasketAccessItem;
  }

  public WorkbasketAccessItem buildAndStore(WorkbasketService workbasketService, String userId)
      throws Exception {
    Subject subject = new Subject();
    subject.getPrincipals().add(new UserPrincipal(userId));
    Function<WorkbasketService, WorkbasketAccessItem> buildAndStore = wrap(this::buildAndStore);
    PrivilegedAction<WorkbasketAccessItem> performBuildAndStore =
        () -> buildAndStore.apply(workbasketService);

    return Subject.doAs(subject, performBuildAndStore);
  }
}
