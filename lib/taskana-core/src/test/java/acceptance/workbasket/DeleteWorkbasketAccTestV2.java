package acceptance.workbasket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import acceptance.FooBar;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import pro.taskana.common.api.TaskanaEngine;
import pro.taskana.common.api.exceptions.InvalidArgumentException;
import pro.taskana.common.api.exceptions.NotAuthorizedException;
import pro.taskana.common.test.security.JaasExtension;
import pro.taskana.common.test.security.WithAccessId;
import pro.taskana.task.api.TaskService;
import pro.taskana.task.internal.models.TaskImpl;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.exceptions.WorkbasketInUseException;
import pro.taskana.workbasket.api.exceptions.WorkbasketNotFoundException;
import pro.taskana.workbasket.api.models.Workbasket;
import pro.taskana.workbasket.api.models.WorkbasketAccessItem;
import pro.taskana.workbasket.internal.WorkbasketAccessItemBuilder;
import pro.taskana.workbasket.internal.WorkbasketBuilder;

/** Acceptance test which does test the deletion of a workbasket and all wanted failures. */
@ExtendWith(JaasExtension.class)
class DeleteWorkbasketAccTestV2 {

  private static WorkbasketService workbasketService;
  private static TaskService taskService;

  @BeforeAll
  static void setup() throws Exception {
    TaskanaEngine taskanaEngine = FooBar.getTaskanaEngineForTests();
    workbasketService = taskanaEngine.getWorkbasketService();
    taskService = taskanaEngine.getTaskService();
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testDeleteWorkbasket() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket().key("key0_B").buildAndStore(workbasketService);

    ThrowingCallable call =
        () -> {
          workbasketService.deleteWorkbasket(w.getId());
          workbasketService.getWorkbasket(w.getKey(), w.getDomain());
        };

    assertThatThrownBy(call)
        .describedAs("There should be no result for a deleted Workbasket.")
        .isInstanceOf(WorkbasketNotFoundException.class);
  }

  @WithAccessId(user = "user-1-1")
  @WithAccessId(user = "taskadmin")
  @TestTemplate
  void should_ThrowException_When_UserRoleIsNotAdminOrBusinessAdmin() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key1_B")
            .buildAndStore(workbasketService, "businessadmin");

    ThrowingCallable deleteWorkbasketCall =
        () -> {
          Workbasket wb = workbasketService.getWorkbasket(w.getKey(), w.getDomain());
          workbasketService.deleteWorkbasket(wb.getId());
        };
    assertThatThrownBy(deleteWorkbasketCall).isInstanceOf(NotAuthorizedException.class);

    deleteWorkbasketCall =
        () -> {
          Workbasket wb = workbasketService.getWorkbasket(w.getId());
          workbasketService.deleteWorkbasket(wb.getId());
        };
    assertThatThrownBy(deleteWorkbasketCall).isInstanceOf(NotAuthorizedException.class);
  }

  @Test
  void testGetWorkbasketNotAuthorized() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key2_B")
            .buildAndStore(workbasketService, "businessadmin");
    assertThatThrownBy(() -> workbasketService.getWorkbasket(w.getId()))
        .isInstanceOf(NotAuthorizedException.class);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testDeleteWorkbasketAlsoAsDistributionTarget() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket().key("key3_B").buildAndStore(workbasketService);
    Workbasket wbDistTarget =
        WorkbasketBuilder.defaultTestWorkbasket().key("key4_B").buildAndStore(workbasketService);
    workbasketService.addDistributionTarget(w.getId(), wbDistTarget.getId());
    int distTargets = workbasketService.getDistributionTargets(w.getId()).size();

    ThrowingCallable deleteWorkbasketCall =
        () -> {
          workbasketService.deleteWorkbasket(wbDistTarget.getId());
          workbasketService.getWorkbasket(wbDistTarget.getId());
        };
    assertThatThrownBy(deleteWorkbasketCall)
        .describedAs("There should be no result for a deleted Workbasket.")
        .isInstanceOf(WorkbasketNotFoundException.class);

    int newDistTargets = workbasketService.getDistributionTargets(w.getId()).size();
    assertThat(newDistTargets).isEqualTo(distTargets - 1);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testDeleteWorkbasketWithNullOrEmptyParam() {
    // Test Null-Value
    assertThatThrownBy(() -> workbasketService.deleteWorkbasket(null))
        .describedAs(
            "delete() should have thrown an InvalidArgumentException, "
                + "when the param ID is null.")
        .isInstanceOf(InvalidArgumentException.class);

    // Test EMPTY-Value
    assertThatThrownBy(() -> workbasketService.deleteWorkbasket(""))
        .describedAs(
            "delete() should have thrown an InvalidArgumentException, \"\n"
                + "            + \"when the param ID is EMPTY-String.")
        .isInstanceOf(InvalidArgumentException.class);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testDeleteWorkbasketButNotExisting() {
    assertThatThrownBy(() -> workbasketService.deleteWorkbasket("SOME NOT EXISTING ID"))
        .isInstanceOf(WorkbasketNotFoundException.class);
  }

  // TODO: Create Task that uses this WB
  @WithAccessId(user = "user-1-2", groups = "businessadmin")
  @Test
  void testDeleteWorkbasketWhichIsUsed() throws Exception {
    Workbasket wb =
        workbasketService.getWorkbasket("user-1-2", "DOMAIN_A"); // all rights, DOMAIN_A with Tasks
    assertThatThrownBy(() -> workbasketService.deleteWorkbasket(wb.getId()))
        .isInstanceOf(WorkbasketInUseException.class);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testCascadingDeleteOfAccessItems() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket().key("key6_B").buildAndStore(workbasketService);
    String wbId = w.getId();
    // create 2 access Items
    WorkbasketAccessItemBuilder.newWorkbasketAccessItem()
        .accessId("TEAMLEAD-2")
        .workbasketId(wbId)
        .permAppend(true)
        .permRead(true)
        .permOpen(true)
        .buildAndStore(workbasketService);
    WorkbasketAccessItemBuilder.newWorkbasketAccessItem()
        .accessId("elena")
        .workbasketId(wbId)
        .permAppend(true)
        .permRead(true)
        .permOpen(true)
        .buildAndStore(workbasketService);

    List<WorkbasketAccessItem> accessItemsBefore =
        workbasketService.getWorkbasketAccessItems(w.getId());
    assertThat(accessItemsBefore).hasSize(2);

    ThrowingCallable call =
        () -> {
          workbasketService.deleteWorkbasket(wbId);
          workbasketService.getWorkbasket(wbId);
        };
    assertThatThrownBy(call)
        .describedAs("There should be no result for a deleted Workbasket.")
        .isInstanceOf(WorkbasketNotFoundException.class);

    List<WorkbasketAccessItem> accessItemsAfter = workbasketService.getWorkbasketAccessItems(wbId);
    assertThat(accessItemsAfter).isEmpty();
  }

  // TODO: Create Tasks that uses this WB
  @WithAccessId(user = "admin")
  @Test
  void testMarkWorkbasketForDeletion() throws Exception {
    final Workbasket wb =
        workbasketService.getWorkbasket("WBI:100000000000000000000000000000000006");

    TaskImpl task = (TaskImpl) taskService.getTask("TKI:000000000000000000000000000000000000");
    taskService.forceCompleteTask(task.getId());
    task = (TaskImpl) taskService.getTask("TKI:000000000000000000000000000000000001");
    taskService.forceCompleteTask(task.getId());
    task = (TaskImpl) taskService.getTask("TKI:000000000000000000000000000000000002");
    taskService.forceCompleteTask(task.getId());
    task = (TaskImpl) taskService.getTask("TKI:000000000000000000000000000000000066");
    taskService.forceCompleteTask(task.getId());

    boolean markedForDeletion = workbasketService.deleteWorkbasket(wb.getId());
    assertThat(markedForDeletion).isFalse();

    Workbasket wb2 = workbasketService.getWorkbasket(wb.getId());
    assertThat(wb2.isMarkedForDeletion()).isTrue();
  }
}
