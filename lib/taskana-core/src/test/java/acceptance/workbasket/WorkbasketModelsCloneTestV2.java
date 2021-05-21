package acceptance.workbasket;

import static org.assertj.core.api.Assertions.assertThat;

import acceptance.FooBar;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import pro.taskana.common.api.TaskanaEngine;
import pro.taskana.common.test.security.JaasExtension;
import pro.taskana.common.test.security.WithAccessId;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.models.Workbasket;
import pro.taskana.workbasket.api.models.WorkbasketSummary;
import pro.taskana.workbasket.internal.WorkbasketAccessItemBuilder;
import pro.taskana.workbasket.internal.WorkbasketBuilder;
import pro.taskana.workbasket.internal.models.WorkbasketAccessItemImpl;

@ExtendWith(JaasExtension.class)
class WorkbasketModelsCloneTestV2 {

  private static WorkbasketService workbasketService;

  @BeforeAll
  static void setup() throws Exception {
    TaskanaEngine taskanaEngine = FooBar.getTaskanaEngineForTests();
    workbasketService = taskanaEngine.getWorkbasketService();
  }

  @WithAccessId(user = "admin")
  @Test
  void should_CopyWithoutIdAndKey_When_WorkbasketSummaryClone() throws Exception {
    Workbasket workbasketDummy =
        WorkbasketBuilder.defaultTestWorkbasket().key("key0_F").buildAndStore(workbasketService);
    WorkbasketSummary workbasketSummaryDummy = workbasketDummy.asSummary();

    WorkbasketSummary workbasketSummaryCopy = workbasketSummaryDummy.copy();

    assertThat(workbasketSummaryCopy)
        .usingRecursiveComparison()
        .ignoringFields("id", "key")
        .isEqualTo(workbasketSummaryDummy);
    assertThat(workbasketSummaryCopy.getId()).isNotEqualTo(workbasketSummaryDummy.getId());
    assertThat(workbasketSummaryCopy.getKey()).isNotEqualTo(workbasketSummaryDummy.getKey());
  }

  @WithAccessId(user = "admin")
  @Test
  void should_CopyWithoutIdAndKey_When_WorkbasketClone() throws Exception {
    Workbasket workbasketDummy =
        WorkbasketBuilder.defaultTestWorkbasket().key("key1_F").buildAndStore(workbasketService);

    Workbasket workbasketCopy = workbasketDummy.copy(workbasketDummy.getKey());

    assertThat(workbasketCopy)
        .usingRecursiveComparison()
        .ignoringFields("id", "key")
        .isEqualTo(workbasketDummy)
        .isNotSameAs(workbasketDummy);
    assertThat(workbasketCopy.getId()).isNotEqualTo(workbasketDummy.getId());
  }

  @WithAccessId(user = "businessadmin")
  @Test
  void should_CopyWithoutId_When_WorkbasketAccessItemClone() throws Exception {
    Workbasket w =
        WorkbasketBuilder.defaultTestWorkbasket().key("key2_F").buildAndStore(workbasketService);
    WorkbasketAccessItemImpl dummyWorkbasketAccessItem =
        (WorkbasketAccessItemImpl)
            WorkbasketAccessItemBuilder.newWorkbasketAccessItem()
                .accessName("dummyAccessName")
                .accessId("dummyAccessId")
                .workbasketId(w.getId())
                .permOpen(false)
                .permRead(false)
                .buildAndStore(workbasketService);

    WorkbasketAccessItemImpl dummyWorkbasketAccessItemCloned = dummyWorkbasketAccessItem.copy();

    assertThat(dummyWorkbasketAccessItemCloned)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(dummyWorkbasketAccessItem)
        .isNotSameAs(dummyWorkbasketAccessItem);
    assertThat(dummyWorkbasketAccessItemCloned.getId())
        .isNotEqualTo(dummyWorkbasketAccessItem.getId());
  }
}
