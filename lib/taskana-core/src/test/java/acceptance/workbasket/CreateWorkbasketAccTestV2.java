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
import pro.taskana.common.api.exceptions.DomainNotFoundException;
import pro.taskana.common.api.exceptions.NotAuthorizedException;
import pro.taskana.common.test.security.JaasExtension;
import pro.taskana.common.test.security.WithAccessId;
import pro.taskana.workbasket.api.WorkbasketPermission;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.WorkbasketType;
import pro.taskana.workbasket.api.exceptions.InvalidWorkbasketException;
import pro.taskana.workbasket.api.exceptions.WorkbasketAccessItemAlreadyExistException;
import pro.taskana.workbasket.api.exceptions.WorkbasketAlreadyExistException;
import pro.taskana.workbasket.api.models.Workbasket;
import pro.taskana.workbasket.api.models.WorkbasketAccessItem;
import pro.taskana.workbasket.internal.WorkbasketBuilder;

/** Acceptance test for all "create workbasket" scenarios. */
@ExtendWith(JaasExtension.class)
class CreateWorkbasketAccTestV2 {

  private static WorkbasketService workbasketService;

  @BeforeAll
  static void setup() throws Exception {
    TaskanaEngine taskanaEngine = FooBar.getTaskanaEngineForTests();
    workbasketService = taskanaEngine.getWorkbasketService();
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testCreateWorkbasket() throws Exception {
    final int before = workbasketService.createWorkbasketQuery().domainIn("DOMAIN_A").list().size();

    Workbasket workbasket = workbasketService.newWorkbasket("key0_A", "DOMAIN_A");
    workbasket.setName("Megabasket");
    workbasket.setType(WorkbasketType.GROUP);
    workbasket.setOrgLevel1("company");
    workbasket = workbasketService.createWorkbasket(workbasket);
    WorkbasketAccessItem wbai =
        workbasketService.newWorkbasketAccessItem(workbasket.getId(), "user-1-2");
    wbai.setPermission(WorkbasketPermission.READ, true);
    workbasketService.createWorkbasketAccessItem(wbai);

    int after = workbasketService.createWorkbasketQuery().domainIn("DOMAIN_A").list().size();
    assertThat(after).isEqualTo(before + 1);
    Workbasket createdWorkbasket = workbasketService.getWorkbasket("key0_A", "DOMAIN_A");
    assertThat(createdWorkbasket).isNotNull();
    assertThat(createdWorkbasket.getId()).isNotNull();
    assertThat(createdWorkbasket.getId().startsWith("WBI")).isTrue();
    assertThat(createdWorkbasket).isEqualTo(workbasket);
    Workbasket createdWorkbasket2 = workbasketService.getWorkbasket(createdWorkbasket.getId());
    assertThat(createdWorkbasket).isNotNull();
    assertThat(createdWorkbasket2).isEqualTo(createdWorkbasket);
  }

  @WithAccessId(user = "user-1-1")
  @WithAccessId(user = "taskadmin")
  @TestTemplate
  void should_ThrowException_When_UserRoleIsNotAdminOrBusinessAdmin() throws Exception {
    ThrowingCallable call =
        () ->
            WorkbasketBuilder.defaultTestWorkbasket()
                .key("key1_A")
                .buildAndStore(workbasketService);

    assertThatThrownBy(call).isInstanceOf(NotAuthorizedException.class);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_BeAbleToCreateNewWorkbasket_When_WorkbasketCopy() throws Exception {
    Workbasket oldWorkbasket =
        WorkbasketBuilder.defaultTestWorkbasket().key("key2_A").buildAndStore(workbasketService);

    Workbasket newWorkbasket = oldWorkbasket.copy("key3_A");
    newWorkbasket = workbasketService.createWorkbasket(newWorkbasket);

    assertThat(newWorkbasket.getId()).isNotNull();
    assertThat(newWorkbasket.getId()).isNotEqualTo(oldWorkbasket.getId());
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testCreateWorkbasketWithInvalidDomain() {
    ThrowingCallable call =
        () ->
            WorkbasketBuilder.newWorkbasket()
                .key("key4_A")
                .domain("UNKNOWN_DOMAIN")
                .name("Megabasket")
                .type(WorkbasketType.GROUP)
                .orgLevel1("company")
                .buildAndStore(workbasketService);

    assertThatThrownBy(call).isInstanceOf(DomainNotFoundException.class);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testCreateWorkbasketWithMissingRequiredField() {
    WorkbasketBuilder wb1 =
        WorkbasketBuilder.newWorkbasket()
            .key(null)
            .domain("novatec")
            .name("Megabasket")
            .type(WorkbasketType.GROUP);
    // missing key
    assertThatThrownBy(() -> wb1.buildAndStore(workbasketService))
        .isInstanceOf(InvalidWorkbasketException.class);

    WorkbasketBuilder wb2 =
        WorkbasketBuilder.newWorkbasket()
            .key("key4_A")
            .domain("novatec")
            .name(null)
            .type(WorkbasketType.GROUP);
    // missing name
    assertThatThrownBy(() -> wb2.buildAndStore(workbasketService))
        .isInstanceOf(InvalidWorkbasketException.class);

    WorkbasketBuilder wb3 =
        WorkbasketBuilder.newWorkbasket()
            .key("key5_A")
            .domain("novatec")
            .name("Megabasket")
            .type(null);
    // missing type
    assertThatThrownBy(() -> wb3.buildAndStore(workbasketService))
        .isInstanceOf(InvalidWorkbasketException.class);

    WorkbasketBuilder wb4 =
        WorkbasketBuilder.newWorkbasket()
            .key("key6_A")
            .domain(null)
            .name("Megabasket")
            .type(WorkbasketType.GROUP);
    // missing domain
    assertThatThrownBy(() -> wb4.buildAndStore(workbasketService))
        .isInstanceOf(InvalidWorkbasketException.class);

    WorkbasketBuilder wb5 =
        WorkbasketBuilder.newWorkbasket()
            .key("")
            .domain("novatec")
            .name("Megabasket")
            .type(WorkbasketType.GROUP);
    // empty key
    assertThatThrownBy(() -> wb5.buildAndStore(workbasketService))
        .isInstanceOf(InvalidWorkbasketException.class);

    WorkbasketBuilder wb6 =
        WorkbasketBuilder.newWorkbasket()
            .key("key7_A")
            .domain("novatec")
            .name("")
            .type(WorkbasketType.GROUP);
    // empty name
    assertThatThrownBy(() -> wb6.buildAndStore(workbasketService))
        .isInstanceOf(InvalidWorkbasketException.class);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testThrowsExceptionIfWorkbasketWithCaseInsensitiveSameKeyDomainIsCreated() throws Exception {
    WorkbasketBuilder.newWorkbasket()
        .key("X123456")
        .domain("DOMAIN_A")
        .name("Personal Workbasket for UID X123456")
        .type(WorkbasketType.PERSONAL)
        .buildAndStore(workbasketService);

    WorkbasketBuilder duplicateWorkbasketWithSmallX =
        WorkbasketBuilder.newWorkbasket()
            .key("x123456")
            .domain("DOMAIN_A")
            .name("Personal Workbasket for UID X123456")
            .type(WorkbasketType.PERSONAL);

    assertThatThrownBy(() -> duplicateWorkbasketWithSmallX.buildAndStore(workbasketService))
        .isInstanceOf(WorkbasketAlreadyExistException.class);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testCreateWorkbasketWithAlreadyExistingKeyAndDomainAndEmptyIdUpdatesOlderWorkbasket()
      throws Exception {
    // First create a new Workbasket.
    WorkbasketBuilder.defaultTestWorkbasket().key("newKey").buildAndStore(workbasketService);
    // Second create a new Workbasket with same Key and Domain.
    WorkbasketBuilder sameKeyAndDomain =
        WorkbasketBuilder.defaultTestWorkbasket().key("newkey").name("new name");

    assertThatThrownBy(() -> sameKeyAndDomain.buildAndStore(workbasketService))
        .isInstanceOf(WorkbasketAlreadyExistException.class);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testWorkbasketAccessItemSetName() throws Exception {
    Workbasket workbasket =
        WorkbasketBuilder.newWorkbasket()
            .key("key8_A")
            .domain("DOMAIN_A")
            .name("MyNewBasket")
            .type(WorkbasketType.PERSONAL)
            .orgLevel1("company")
            .buildAndStore(workbasketService);
    WorkbasketAccessItem wbai =
        workbasketService.newWorkbasketAccessItem(workbasket.getId(), "user-1-2");
    wbai.setPermission(WorkbasketPermission.READ, true);
    wbai.setAccessName("Karl Napf");
    workbasketService.createWorkbasketAccessItem(wbai);

    Workbasket createdWorkbasket = workbasketService.getWorkbasket(workbasket.getId());
    assertThat(createdWorkbasket).isNotNull();
    assertThat(createdWorkbasket.getId()).isNotNull();

    List<WorkbasketAccessItem> accessItems =
        workbasketService.getWorkbasketAccessItems(createdWorkbasket.getId());
    WorkbasketAccessItem item =
        accessItems.stream().filter(t -> wbai.getId().equals(t.getId())).findFirst().orElse(null);
    assertThat(item)
        .isNotNull()
        .extracting(WorkbasketAccessItem::getAccessName)
        .isEqualTo("Karl Napf");
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testCreateDuplicateWorkbasketAccessListFails() throws Exception {
    Workbasket workbasket =
        WorkbasketBuilder.defaultTestWorkbasket().key("key9_A").buildAndStore(workbasketService);
    WorkbasketAccessItem wbai =
        workbasketService.newWorkbasketAccessItem(workbasket.getId(), "user-3-2");
    wbai.setPermission(WorkbasketPermission.READ, true);
    workbasketService.createWorkbasketAccessItem(wbai);

    assertThatThrownBy(() -> workbasketService.createWorkbasketAccessItem(wbai))
        .isInstanceOf(WorkbasketAccessItemAlreadyExistException.class);
  }
}
