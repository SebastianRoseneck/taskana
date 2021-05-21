package acceptance.workbasket;

import static acceptance.AbstractAccTest.GROUP_1_DN;
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
import pro.taskana.common.api.exceptions.NotAuthorizedException;
import pro.taskana.common.test.security.JaasExtension;
import pro.taskana.common.test.security.WithAccessId;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.exceptions.WorkbasketNotFoundException;
import pro.taskana.workbasket.api.models.Workbasket;
import pro.taskana.workbasket.api.models.WorkbasketSummary;
import pro.taskana.workbasket.internal.WorkbasketAccessItemBuilder;
import pro.taskana.workbasket.internal.WorkbasketBuilder;

/** Acceptance test for all "get workbasket" scenarios. */
@ExtendWith(JaasExtension.class)
class DistributionTargetsAccTestV2 {

  private static WorkbasketService workbasketService;

  @BeforeAll
  static void setup() throws Exception {
    TaskanaEngine taskanaEngine = FooBar.getTaskanaEngineForTests();
    workbasketService = taskanaEngine.getWorkbasketService();
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testGetDistributionTargetsSucceedsById() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket().key("key0_C").buildAndStore(workbasketService);
    Workbasket wbTarget1 =
        WorkbasketBuilder.defaultTestWorkbasket().key("key1_C").buildAndStore(workbasketService);
    Workbasket wbTarget2 =
        WorkbasketBuilder.defaultTestWorkbasket().key("key2_C").buildAndStore(workbasketService);
    workbasketService.addDistributionTarget(w.getId(), wbTarget1.getId());
    workbasketService.addDistributionTarget(w.getId(), wbTarget2.getId());

    List<WorkbasketSummary> retrievedDistributionTargets =
        workbasketService.getDistributionTargets(w.getId());

    assertThat(retrievedDistributionTargets)
        .extracting(WorkbasketSummary::getId)
        .containsExactlyInAnyOrderElementsOf(List.of(wbTarget1.getId(), wbTarget2.getId()));
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testGetDistributionTargetsSucceeds() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket().key("key3_C").buildAndStore(workbasketService);
    Workbasket wbTarget1 =
        WorkbasketBuilder.defaultTestWorkbasket().key("key4_C").buildAndStore(workbasketService);
    Workbasket wbTarget2 =
        WorkbasketBuilder.defaultTestWorkbasket().key("key5_C").buildAndStore(workbasketService);
    workbasketService.addDistributionTarget(w.getId(), wbTarget1.getId());
    workbasketService.addDistributionTarget(w.getId(), wbTarget2.getId());

    List<WorkbasketSummary> retrievedDistributionTargets =
        workbasketService.getDistributionTargets(w.getKey(), w.getDomain());

    assertThat(retrievedDistributionTargets)
        .extracting(WorkbasketSummary::getId)
        .containsExactlyInAnyOrderElementsOf(List.of(wbTarget1.getId(), wbTarget2.getId()));
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testDistributionTargetCallsWithNonExistingWorkbaskets() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket().key("key6_C").buildAndStore(workbasketService);
    String existingWId = w.getId();
    String nonExistingWId = "WBI:100000000000000000000000000000000xx1";

    ThrowingCallable call = () -> workbasketService.getDistributionTargets(nonExistingWId);
    assertThatThrownBy(call).isInstanceOf(WorkbasketNotFoundException.class);

    call = () -> workbasketService.setDistributionTargets(existingWId, List.of(nonExistingWId));
    assertThatThrownBy(call).isInstanceOf(WorkbasketNotFoundException.class);

    call = () -> workbasketService.addDistributionTarget(existingWId, nonExistingWId);
    assertThatThrownBy(call).isInstanceOf(WorkbasketNotFoundException.class);

    int beforeCount = workbasketService.getDistributionTargets(existingWId).size();
    workbasketService.removeDistributionTarget(existingWId, nonExistingWId);
    int afterCount = workbasketService.getDistributionTargets(existingWId).size();
    assertThat(beforeCount).isEqualTo(afterCount);
  }

  @WithAccessId(user = "admin")
  @WithAccessId(user = "businessadmin")
  @WithAccessId(user = "taskadmin")
  @TestTemplate
  void should_ReturnDistributionTargets_When_NoExplicitPermissionsButUserIsInAdministrativeRole()
      throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key7_C")
            .buildAndStore(workbasketService, "businessadmin");
    Workbasket wbTarget1 =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key8_C")
            .buildAndStore(workbasketService, "businessadmin");
    Workbasket wbTarget2 =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key9_C")
            .buildAndStore(workbasketService, "businessadmin");

    workbasketService.addDistributionTarget(w.getId(), wbTarget1.getId());
    workbasketService.addDistributionTarget(w.getId(), wbTarget2.getId());

    List<WorkbasketSummary> distributionTargets =
        workbasketService.getDistributionTargets(w.getId());
    assertThat(distributionTargets).hasSize(2);
  }

  @WithAccessId(user = "user-1-1")
  @WithAccessId(user = "taskadmin")
  @TestTemplate
  void should_ThrowException_When_UserRoleIsNotAdminOrBusinessAdminAndMakesDistTargetCalls()
      throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key10_C")
            .buildAndStore(workbasketService, "businessadmin");
    Workbasket wbTarget =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key11_C")
            .buildAndStore(workbasketService, "businessadmin");

    ThrowingCallable call =
        () -> workbasketService.setDistributionTargets(w.getId(), List.of(wbTarget.getId()));
    assertThatThrownBy(call).isInstanceOf(NotAuthorizedException.class);

    call = () -> workbasketService.addDistributionTarget(w.getId(), wbTarget.getId());
    assertThatThrownBy(call).isInstanceOf(NotAuthorizedException.class);

    call = () -> workbasketService.removeDistributionTarget(w.getId(), wbTarget.getId());
    assertThatThrownBy(call).isInstanceOf(NotAuthorizedException.class);
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_ThrowException_When_UserTriesToGetDistributionTargetsAndRoleIsNotAdministrative()
      throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key12_C")
            .buildAndStore(workbasketService, "businessadmin");

    ThrowingCallable getDistributionTargetsCall =
        () -> workbasketService.getDistributionTargets(w.getId());
    assertThatThrownBy(getDistributionTargetsCall).isInstanceOf(NotAuthorizedException.class);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testAddAndRemoveDistributionTargets() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket().key("key13_C").buildAndStore(workbasketService);

    List<WorkbasketSummary> distributionTargets =
        workbasketService.getDistributionTargets(w.getId());
    assertThat(distributionTargets).hasSize(0);

    // add a new distribution target
    Workbasket newTarget =
        WorkbasketBuilder.defaultTestWorkbasket().key("key14_C").buildAndStore(workbasketService);
    workbasketService.addDistributionTarget(w.getId(), newTarget.getId());

    distributionTargets = workbasketService.getDistributionTargets(w.getId());
    assertThat(distributionTargets).hasSize(1);

    // remove the new target
    workbasketService.removeDistributionTarget(w.getId(), newTarget.getId());
    distributionTargets = workbasketService.getDistributionTargets(w.getId());
    assertThat(distributionTargets).hasSize(0);

    // remove the new target again Question: should this throw an exception?
    workbasketService.removeDistributionTarget(w.getId(), newTarget.getId());
    distributionTargets = workbasketService.getDistributionTargets(w.getId());
    assertThat(distributionTargets).hasSize(0);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testAddAndRemoveDistributionTargetsOnWorkbasketWithoutReadPermission() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket().key("key15_C").buildAndStore(workbasketService);

    List<WorkbasketSummary> distributionTargets =
        workbasketService.getDistributionTargets(w.getId());
    assertThat(distributionTargets).isEmpty();

    // add a new distribution target
    Workbasket newTarget =
        WorkbasketBuilder.defaultTestWorkbasket().key("key16_C").buildAndStore(workbasketService);
    workbasketService.addDistributionTarget(w.getId(), newTarget.getId());

    distributionTargets = workbasketService.getDistributionTargets(w.getId());
    assertThat(distributionTargets).hasSize(1);

    // remove the new target
    workbasketService.removeDistributionTarget(w.getId(), newTarget.getId());
    distributionTargets = workbasketService.getDistributionTargets(w.getId());
    assertThat(distributionTargets).isEmpty();
  }

  @WithAccessId(user = "user-1-1", groups = GROUP_1_DN)
  @Test
  void testAddDistributionTargetsFailsNotAuthorized() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key17_C")
            .buildAndStore(workbasketService, "businessadmin");
    WorkbasketAccessItemBuilder.newWorkbasketAccessItem()
        .accessId("user-1-1")
        .workbasketId(w.getId())
        .permRead(true)
        .buildAndStore(workbasketService, "businessadmin");

    List<WorkbasketSummary> distributionTargets =
        workbasketService.getDistributionTargets(w.getId());

    assertThat(distributionTargets).hasSize(0);

    // add a new distribution target
    Workbasket newTarget =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key18_C")
            .buildAndStore(workbasketService, "businessadmin");

    ThrowingCallable call =
        () -> {
          workbasketService.addDistributionTarget(w.getId(), newTarget.getId());
        };
    assertThatThrownBy(call).isInstanceOf(NotAuthorizedException.class);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testSetDistributionTargets() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket().key("key19_C").buildAndStore(workbasketService);
    Workbasket wbTargetToBeReplaced =
        WorkbasketBuilder.defaultTestWorkbasket().key("key20_C").buildAndStore(workbasketService);
    workbasketService.addDistributionTarget(w.getId(), wbTargetToBeReplaced.getId());

    List<WorkbasketSummary> initialDistributionTargets =
        workbasketService.getDistributionTargets(w.getId());
    assertThat(initialDistributionTargets).hasSize(1);

    Workbasket wbTarget1 =
        WorkbasketBuilder.defaultTestWorkbasket().key("key21_C").buildAndStore(workbasketService);
    Workbasket wbTarget2 =
        WorkbasketBuilder.defaultTestWorkbasket().key("key22_C").buildAndStore(workbasketService);
    List<WorkbasketSummary> newDistributionTargets = List.of(wbTarget1, wbTarget2);
    assertThat(newDistributionTargets).hasSize(2);

    workbasketService.setDistributionTargets(
        w.getId(), List.of(wbTarget1.getId(), wbTarget2.getId()));
    List<WorkbasketSummary> changedTargets = workbasketService.getDistributionTargets(w.getId());
    assertThat(changedTargets).hasSize(2);

    // reset DB to original state
    // resetDb(false);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testGetDistributionSourcesById() throws Exception {
    Workbasket wbSource1 =
        WorkbasketBuilder.defaultTestWorkbasket().key("key23_C").buildAndStore(workbasketService);
    Workbasket wbSource2 =
        WorkbasketBuilder.defaultTestWorkbasket().key("key24_C").buildAndStore(workbasketService);
    Workbasket wbTarget =
        WorkbasketBuilder.defaultTestWorkbasket().key("key25_C").buildAndStore(workbasketService);
    workbasketService.addDistributionTarget(wbSource1.getId(), wbTarget.getId());
    workbasketService.addDistributionTarget(wbSource2.getId(), wbTarget.getId());

    List<WorkbasketSummary> distributionSources =
        workbasketService.getDistributionSources(wbTarget.getId());

    assertThat(distributionSources)
        .extracting(WorkbasketSummary::getId)
        .containsExactlyInAnyOrder(wbSource1.getId(), wbSource2.getId());
  }

  // TODO teamlead cant addDistributionTarget
  @WithAccessId(user = "teamlead-1")
  @Test
  void testGetDistributionSourcesByKeyDomain() throws Exception {
    Workbasket wbSource1 =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key26_C")
            .buildAndStore(workbasketService, "businessadmin");
    Workbasket wbSource2 =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key27_C")
            .buildAndStore(workbasketService, "businessadmin");
    Workbasket wbTarget =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key28_C")
            .buildAndStore(workbasketService, "businessadmin");
    WorkbasketAccessItemBuilder.newWorkbasketAccessItem()
        .accessId("teamlead-1")
        .workbasketId(wbSource1.getId())
        .permAppend(true)
        .permOpen(true);
    workbasketService.addDistributionTarget(wbSource1.getId(), wbTarget.getId());
    workbasketService.addDistributionTarget(wbSource2.getId(), wbTarget.getId());

    List<WorkbasketSummary> distributionSources =
        workbasketService.getDistributionSources(wbTarget.getKey(), wbTarget.getDomain());

    assertThat(distributionSources)
        .extracting(WorkbasketSummary::getId)
        .containsExactlyInAnyOrder(wbSource1.getId(), wbSource2.getId());
  }

  @WithAccessId(user = "unknownuser")
  @Test
  void testQueryDistributionSourcesThrowsNotAuthorized() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key29_C")
            .buildAndStore(workbasketService, "businessadmin");

    ThrowingCallable call = () -> workbasketService.getDistributionSources(w.getId());
    assertThatThrownBy(call).isInstanceOf(NotAuthorizedException.class);
  }

  @WithAccessId(user = "user-2-2")
  @Test
  void testQueryDistributionSourcesThrowsWorkbasketNotFound() {
    ThrowingCallable call =
        () -> workbasketService.getDistributionSources("WBI:10dasgibtsdochnicht00000000000000004");
    assertThatThrownBy(call).isInstanceOf(WorkbasketNotFoundException.class);
  }
}
