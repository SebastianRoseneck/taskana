package acceptance.builder;

import static org.assertj.core.api.Assertions.assertThat;
import static pro.taskana.workbasket.internal.WorkbasketAccessItemBuilder.newWorkbasketAccessItem;
import static pro.taskana.workbasket.internal.WorkbasketBuilder.newWorkbasket;

import acceptance.FooBar;
import java.time.Instant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import pro.taskana.common.api.TaskanaEngine;
import pro.taskana.common.test.security.JaasExtension;
import pro.taskana.common.test.security.WithAccessId;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.WorkbasketType;
import pro.taskana.workbasket.api.models.Workbasket;
import pro.taskana.workbasket.internal.WorkbasketBuilder;

@ExtendWith(JaasExtension.class)
public class WorkbasketBuilderTest {

  private static WorkbasketService workbasketService;

  @BeforeAll
  static void setup() throws Exception {
    TaskanaEngine taskanaEngine = FooBar.getTaskanaEngineForTests();
    workbasketService = taskanaEngine.getWorkbasketService();
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_CreateWorkbasket_When_UsingWorkbasketBuilder() throws Exception {
    Workbasket workbasket =
        newWorkbasket()
            .key("key0_G")
            .domain("DOMAIN_A")
            .name("Megabasket")
            .type(WorkbasketType.GROUP)
            .buildAndStore(workbasketService);
    Workbasket receivedWorkbasket = workbasketService.getWorkbasket(workbasket.getId());
    assertThat(receivedWorkbasket).isEqualTo(workbasket);
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_CreateWorkbasketAsUser_When_UsingWorkbasketBuilder() throws Exception {
    Workbasket workbasket =
        newWorkbasket()
            .domain("DOMAIN_A")
            .description("PPK User 2 KSC 1")
            .name("PPK User 2 KSC 1")
            .key("key1_G")
            .type(WorkbasketType.GROUP)
            .buildAndStore(workbasketService, "businessadmin");
    newWorkbasketAccessItem()
        .accessId("user-1-1")
        .permRead(true)
        .workbasketId(workbasket.getId())
        .buildAndStore(workbasketService, "businessadmin");

    Workbasket receivedWorkbasket = workbasketService.getWorkbasket(workbasket.getId());
    assertThat(receivedWorkbasket).isEqualTo(workbasket);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_CreateWorkbasketWithCreatedTimestamp_When_UsingWorkbasketBuilder() throws Exception {
    Instant created = Instant.parse("2021-05-17T07:16:26.747Z");
    Workbasket workbasket =
        newWorkbasket()
            .key("key2_G")
            .domain("DOMAIN_A")
            .name("Megabasket")
            .type(WorkbasketType.GROUP)
            .created(created)
            .buildAndStore(workbasketService);
    Workbasket receivedWorkbasket = workbasketService.getWorkbasket(workbasket.getId());
    assertThat(receivedWorkbasket.getCreated()).isEqualTo(created);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_CreateWorkbasketWithModifiedTimestamp_When_UsingWorkbasketBuilder() throws Exception {
    Instant modified = Instant.parse("2021-05-17T07:16:26.747Z");
    Workbasket workbasket =
        newWorkbasket()
            .key("key3_G")
            .domain("DOMAIN_A")
            .name("Megabasket")
            .type(WorkbasketType.GROUP)
            .modified(modified)
            .buildAndStore(workbasketService);
    Workbasket receivedWorkbasket = workbasketService.getWorkbasket(workbasket.getId());
    assertThat(receivedWorkbasket.getModified()).isEqualTo(modified);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_AllowReuseOfChangedDefaultValues_When_UsingWorkbasketBuilder() throws Exception {
    Instant modified1 = Instant.parse("2021-05-17T07:16:26.747Z");
    Instant modified2 = Instant.parse("2021-05-17T07:17:26.747Z");
    Instant created = Instant.parse("2021-05-17T07:16:25.747Z");

    WorkbasketBuilder workbasketBuilder =
        newWorkbasket()
            .domain("DOMAIN_A")
            .name("Megabasket")
            .type(WorkbasketType.GROUP)
            .created(created);
    Workbasket workbasket1 =
        workbasketBuilder.key("key4_G").modified(modified1).buildAndStore(workbasketService);
    Workbasket workbasket2 =
        workbasketBuilder.key("key5_G").modified(modified2).buildAndStore(workbasketService);

    Workbasket receivedWorkbasket1 = workbasketService.getWorkbasket(workbasket1.getId());
    Workbasket receivedWorkbasket2 = workbasketService.getWorkbasket(workbasket2.getId());
    assertThat(receivedWorkbasket1.getModified()).isEqualTo(modified1);
    assertThat(receivedWorkbasket2.getModified()).isEqualTo(modified2);
    assertThat(receivedWorkbasket1.getCreated())
        .isEqualTo(receivedWorkbasket2.getCreated())
        .isEqualTo(created);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_AllowReassignmentOfCreatedValue_When_UsingWorkbasketBuilder() throws Exception {
    Instant created1 = Instant.parse("2021-05-17T07:16:26.747Z");
    Instant created2 = Instant.parse("2021-05-17T07:17:26.747Z");

    WorkbasketBuilder workbasketBuilder =
        newWorkbasket()
            .domain("DOMAIN_A")
            .name("Megabasket")
            .type(WorkbasketType.GROUP)
            .created(created1);
    Workbasket workbasket1 = workbasketBuilder.key("key6_G").buildAndStore(workbasketService);
    Workbasket workbasket2 =
        workbasketBuilder.key("key7_G").created(created2).buildAndStore(workbasketService);

    Workbasket receivedWorkbasket1 = workbasketService.getWorkbasket(workbasket1.getId());
    Workbasket receivedWorkbasket2 = workbasketService.getWorkbasket(workbasket2.getId());
    assertThat(receivedWorkbasket1.getCreated()).isEqualTo(created1);
    assertThat(receivedWorkbasket2.getCreated()).isEqualTo(created2);
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_AllowOverwritingOfCreatedValue_When_UsingWorkbasketBuilder() throws Exception {
    Instant created = Instant.parse("2021-05-17T07:16:26.747Z");

    Workbasket workbasket =
        newWorkbasket()
            .key("key8_G")
            .domain("DOMAIN_A")
            .name("Megabasket")
            .type(WorkbasketType.GROUP)
            .created(created)
            .created(null)
            .buildAndStore(workbasketService);

    Workbasket receivedWorkbasket = workbasketService.getWorkbasket(workbasket.getId());
    assertThat(receivedWorkbasket.getCreated()).isNotNull().isNotEqualTo(created);
  }
}
