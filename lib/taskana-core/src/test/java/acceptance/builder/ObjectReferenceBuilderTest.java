package acceptance.builder;

import static org.assertj.core.api.Assertions.assertThat;
import static pro.taskana.classification.internal.ClassificationBuilder.newClassification;
import static pro.taskana.task.internal.ObjectReferenceBuilder.newObjectReference;

import acceptance.FooBar;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import pro.taskana.classification.api.ClassificationService;
import pro.taskana.classification.api.models.Classification;
import pro.taskana.common.api.TaskanaEngine;
import pro.taskana.common.test.security.JaasExtension;
import pro.taskana.common.test.security.WithAccessId;
import pro.taskana.task.api.TaskService;
import pro.taskana.task.api.models.ObjectReference;
import pro.taskana.task.api.models.Task;
import pro.taskana.task.internal.ObjectReferenceBuilder;
import pro.taskana.task.internal.TaskBuilder;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.models.Workbasket;
import pro.taskana.workbasket.internal.WorkbasketAccessItemBuilder;
import pro.taskana.workbasket.internal.WorkbasketBuilder;

@ExtendWith(JaasExtension.class)
public class ObjectReferenceBuilderTest {

  private static ClassificationService classificationService;
  private static TaskService taskService;
  private static WorkbasketService workbasketService;
  private static Classification classification;
  private static Workbasket workbasket;

  @WithAccessId(user = "businessadmin")
  @BeforeAll
  static void setup() throws Exception {
    TaskanaEngine taskanaEngine = FooBar.getTaskanaEngineForTests();
    classificationService = taskanaEngine.getClassificationService();
    taskService = taskanaEngine.getTaskService();
    workbasketService = taskanaEngine.getWorkbasketService();

    classification =
        newClassification().key("key0_B").domain("DOMAIN_A").buildAndStore(classificationService);
    workbasket =
        WorkbasketBuilder.defaultTestWorkbasket().key("key0_B").buildAndStore(workbasketService);
    WorkbasketAccessItemBuilder.newWorkbasketAccessItem()
        .workbasketId(workbasket.getId())
        .accessId("user-1-1")
        .permAppend(true)
        .permRead(true)
        .buildAndStore(workbasketService);
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_CreateObjectReference_When_UsingObjectReferenceBuilder() throws Exception {
    ObjectReference objectReference =
        newObjectReference()
            .company("Company1")
            .system("System1")
            .systemInstance("Instance1")
            .type("Type1")
            .value("Value1")
            .build();
    Task task =
        TaskBuilder.newTask()
            .classificationSummary(classification)
            .workbasketSummary(workbasket)
            .primaryObjRef(objectReference)
            .buildAndStore(taskService);

    ObjectReference receivedObjectReference = task.getPrimaryObjRef();

    assertThat(receivedObjectReference).isEqualTo(objectReference);
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_AllowReassignmentOfCreatedValue_When_UsingObjectReferenceBuilder() throws Exception {
    String value1 = "Value1";
    String value2 = "Value2";
    ObjectReferenceBuilder objectReferenceBuilder =
        newObjectReference()
            .company("Company")
            .system("System1")
            .systemInstance("Instance1")
            .value(value1)
            .type("Type1");
    ObjectReference objectReference1 = objectReferenceBuilder.build();
    ObjectReference objectReference2 = objectReferenceBuilder.value(value2).build();

    Task task1 =
        TaskBuilder.newTask()
            .classificationSummary(classification)
            .workbasketSummary(workbasket)
            .primaryObjRef(objectReference1)
            .buildAndStore(taskService);
    Task task2 =
        TaskBuilder.newTask()
            .classificationSummary(classification)
            .workbasketSummary(workbasket)
            .primaryObjRef(objectReference2)
            .buildAndStore(taskService);

    ObjectReference receivedObjectReference1 = task1.getPrimaryObjRef();
    ObjectReference receivedObjectReference2 = task2.getPrimaryObjRef();

    assertThat(receivedObjectReference1.getValue()).isEqualTo(value1);
    assertThat(receivedObjectReference2.getValue()).isEqualTo(value2);
  }
}
