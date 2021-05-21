package acceptance.builder;

import acceptance.FooBar;
import org.junit.jupiter.api.BeforeAll;

import pro.taskana.common.api.TaskanaEngine;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.models.Workbasket;

public class WorkbasketAccessItemBuilderTest {

  private static WorkbasketService workbasketService;
  private static Workbasket workbasket;

  @BeforeAll
  static void setup() throws Exception {
    TaskanaEngine taskanaEngine = FooBar.getTaskanaEngineForTests();
    workbasketService = taskanaEngine.getWorkbasketService();

    // workbasket = defaultTestWorkbasket().key("key0_F").buildAndStore(workbasketService);
  }
}
