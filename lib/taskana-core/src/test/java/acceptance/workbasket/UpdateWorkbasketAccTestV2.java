package acceptance.workbasket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import acceptance.FooBar;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import pro.taskana.common.api.TaskanaEngine;
import pro.taskana.common.api.exceptions.ConcurrencyException;
import pro.taskana.common.api.exceptions.NotAuthorizedException;
import pro.taskana.common.test.security.JaasExtension;
import pro.taskana.common.test.security.WithAccessId;
import pro.taskana.workbasket.api.WorkbasketCustomField;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.WorkbasketType;
import pro.taskana.workbasket.api.exceptions.InvalidWorkbasketException;
import pro.taskana.workbasket.api.exceptions.WorkbasketNotFoundException;
import pro.taskana.workbasket.api.models.Workbasket;
import pro.taskana.workbasket.internal.WorkbasketBuilder;
import pro.taskana.workbasket.internal.models.WorkbasketImpl;

/** Acceptance test for all "update workbasket" scenarios. */
@ExtendWith(JaasExtension.class)
class UpdateWorkbasketAccTestV2 {

  private static WorkbasketService workbasketService;

  @BeforeAll
  static void setup() throws Exception {
    TaskanaEngine taskanaEngine = FooBar.getTaskanaEngineForTests();
    workbasketService = taskanaEngine.getWorkbasketService();
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testUpdateWorkbasket() throws Exception {
    Workbasket workbasket =
        WorkbasketBuilder.defaultTestWorkbasket().key("key0_E").buildAndStore(workbasketService);

    final Instant modified = workbasket.getModified();

    workbasket.setName("new name");
    workbasket.setDescription("new description");
    workbasket.setType(WorkbasketType.TOPIC);
    workbasket.setOrgLevel1("new level 1");
    workbasket.setOrgLevel2("new level 2");
    workbasket.setOrgLevel3("new level 3");
    workbasket.setOrgLevel4("new level 4");
    workbasket.setCustomAttribute(WorkbasketCustomField.CUSTOM_1, "new custom 1");
    workbasket.setCustomAttribute(WorkbasketCustomField.CUSTOM_2, "new custom 2");
    workbasket.setCustomAttribute(WorkbasketCustomField.CUSTOM_3, "new custom 3");
    workbasket.setCustomAttribute(WorkbasketCustomField.CUSTOM_4, "new custom 4");
    workbasket.setDescription("new description");
    workbasketService.updateWorkbasket(workbasket);

    Workbasket updatedWorkbasket = workbasketService.getWorkbasket(workbasket.getId());

    assertThat(updatedWorkbasket.getId()).isEqualTo(workbasket.getId());
    assertThat(updatedWorkbasket.getCreated()).isEqualTo(workbasket.getCreated());
    assertThat(updatedWorkbasket.getName()).isEqualTo("new name");
    assertThat(updatedWorkbasket.getType()).isEqualTo(WorkbasketType.TOPIC);
    assertThat(updatedWorkbasket.getModified()).isNotEqualTo(modified);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_ThrowException_When_UpdatingWorkbasketWithInvalidName() throws Exception {
    Workbasket workbasket =
        WorkbasketBuilder.defaultTestWorkbasket().key("key1_E").buildAndStore(workbasketService);
    workbasket.setName(null);
    assertThatThrownBy(() -> workbasketService.updateWorkbasket(workbasket))
        .isInstanceOf(InvalidWorkbasketException.class);

    workbasket.setName("");
    assertThatThrownBy(() -> workbasketService.updateWorkbasket(workbasket))
        .isInstanceOf(InvalidWorkbasketException.class);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_ThrowException_When_UpdatingWorkbasketWithTypeNull() throws Exception {
    Workbasket workbasket =
        WorkbasketBuilder.defaultTestWorkbasket().key("key2_E").buildAndStore(workbasketService);

    workbasket.setType(null);

    assertThatThrownBy(() -> workbasketService.updateWorkbasket(workbasket))
        .isInstanceOf(InvalidWorkbasketException.class);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testUpdateWorkbasketWithConcurrentModificationShouldThrowException() throws Exception {
    WorkbasketImpl workbasket =
        (WorkbasketImpl)
            WorkbasketBuilder.defaultTestWorkbasket()
                .key("key3_E")
                .buildAndStore(workbasketService);

    workbasket.setModified(workbasket.getModified().minus(1, ChronoUnit.SECONDS));

    assertThatExceptionOfType(ConcurrencyException.class)
        .isThrownBy(() -> workbasketService.updateWorkbasket(workbasket));
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void testUpdateWorkbasketOfNonExistingWorkbasketShouldThrowException() throws Exception {
    WorkbasketImpl workbasket =
        (WorkbasketImpl)
            WorkbasketBuilder.defaultTestWorkbasket()
                .key("key4_E")
                .buildAndStore(workbasketService);

    workbasket.setDomain("InvalidDomain");
    workbasket.setKey("InvalidKey");

    assertThatExceptionOfType(WorkbasketNotFoundException.class)
        .isThrownBy(() -> workbasketService.updateWorkbasket(workbasket));
  }

  @WithAccessId(user = "user-1-1")
  @WithAccessId(user = "taskadmin")
  @TestTemplate
  void should_ThrowException_When_UserRoleIsNotAdminOrBusinessAdmin() throws Exception {
    Workbasket workbasket =
        WorkbasketBuilder.defaultTestWorkbasket()
            .key("key5_E")
            .buildAndStore(workbasketService, "businessadmin");

    workbasket.setName("new name");

    assertThatThrownBy(() -> workbasketService.updateWorkbasket(workbasket))
        .isInstanceOf(NotAuthorizedException.class);
  }
}
