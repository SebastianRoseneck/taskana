package acceptance.builder;

import static org.assertj.core.api.Assertions.assertThat;
import static pro.taskana.classification.internal.ClassificationBuilder.newClassification;
import static pro.taskana.task.internal.ObjectReferenceBuilder.defaultTestObjectReference;
import static pro.taskana.workbasket.internal.WorkbasketAccessItemBuilder.newWorkbasketAccessItem;
import static pro.taskana.workbasket.internal.WorkbasketBuilder.defaultTestWorkbasket;

import acceptance.FooBar;
import java.time.Instant;
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
import pro.taskana.task.internal.TaskBuilder;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.models.Workbasket;

@ExtendWith(JaasExtension.class)
public class TaskBuilderTest {
  private static TaskService taskService;
  private static Workbasket workbasket;
  private static Classification classification;
  private static ObjectReference objectReference;

  @WithAccessId(user = "businessadmin")
  @BeforeAll
  static void setup() throws Exception {
    TaskanaEngine taskanaEngine = FooBar.getTaskanaEngineForTests();
    taskService = taskanaEngine.getTaskService();
    WorkbasketService workbasketService = taskanaEngine.getWorkbasketService();
    ClassificationService classificationService = taskanaEngine.getClassificationService();

    objectReference = defaultTestObjectReference().build();
    workbasket =
        defaultTestWorkbasket().owner("user-1-1").key("key0_D").buildAndStore(workbasketService);
    classification =
        newClassification().key("key0_D").domain("DOMAIN_A").buildAndStore(classificationService);
    newWorkbasketAccessItem()
        .workbasketId(workbasket.getId())
        .accessId("user-1-1")
        .permAppend(true)
        .permRead(true)
        .buildAndStore(workbasketService);
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_CreateTask_When_UsingTaskBuilder() throws Exception {
    Task task =
        TaskBuilder.newTask()
            .workbasketSummary(workbasket)
            .classificationSummary(classification)
            .primaryObjRef(objectReference)
            .buildAndStore(taskService);

    Task receivedTask = taskService.getTask(task.getId());
    assertThat(receivedTask).isEqualTo(task);
  }

  @WithAccessId(user = "admin")
  @Test
  void should_CreateTaskAsUser_When_UsingTaskBuilder() throws Exception {
    Task task =
        TaskBuilder.newTask()
            .workbasketSummary(workbasket)
            .classificationSummary(classification)
            .primaryObjRef(objectReference)
            .buildAndStore(taskService, "user-1-1");

    Task receivedClassification = taskService.getTask(task.getId());
    assertThat(receivedClassification).isEqualTo(task);
    assertThat(receivedClassification.getCreator()).isEqualTo("user-1-1");
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_CreateTaskWithCreatedTimestamp_When_UsingTaskBuilder() throws Exception {
    Instant created = Instant.parse("2021-05-17T07:16:26.747Z");

    Task task =
        TaskBuilder.newTask()
            .workbasketSummary(workbasket)
            .classificationSummary(classification)
            .primaryObjRef(objectReference)
            .created(created)
            .buildAndStore(taskService);

    Task receivedTask = taskService.getTask(task.getId());
    assertThat(receivedTask.getCreated()).isEqualTo(created);
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_CreateTaskWithReadFlag_When_UsingTaskBuilder() throws Exception {
    Task task =
        TaskBuilder.newTask()
            .workbasketSummary(workbasket)
            .classificationSummary(classification)
            .primaryObjRef(objectReference)
            .read(true)
            .buildAndStore(taskService);

    Task receivedTask = taskService.getTask(task.getId());
    assertThat(receivedTask.isRead()).isEqualTo(true);
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_AllowReuseOfChangedDefaultValues_When_UsingTaskBuilder() throws Exception {
    Instant modified1 = Instant.parse("2021-05-17T07:16:26.747Z");
    Instant modified2 = Instant.parse("2021-05-17T07:17:26.747Z");
    Instant created = Instant.parse("2021-05-17T07:16:25.747Z");

    TaskBuilder taskBuilder =
        TaskBuilder.newTask()
            .workbasketSummary(workbasket)
            .classificationSummary(classification)
            .primaryObjRef(objectReference)
            .created(created);
    Task task1 = taskBuilder.modified(modified1).buildAndStore(taskService);
    Task task2 = taskBuilder.modified(modified2).buildAndStore(taskService);

    Task receivedTask1 = taskService.getTask(task1.getId());
    Task receivedTask2 = taskService.getTask(task2.getId());
    assertThat(receivedTask1.getModified()).isEqualTo(modified1);
    assertThat(receivedTask2.getModified()).isEqualTo(modified2);
    assertThat(receivedTask1.getCreated()).isEqualTo(receivedTask2.getCreated()).isEqualTo(created);
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_AllowReassignmentOfCreatedValue_When_UsingTaskBuilder() throws Exception {
    Instant created1 = Instant.parse("2021-05-17T07:16:26.747Z");
    Instant created2 = Instant.parse("2021-05-17T07:17:26.747Z");

    TaskBuilder taskBuilder =
        TaskBuilder.newTask()
            .workbasketSummary(workbasket)
            .classificationSummary(classification)
            .primaryObjRef(objectReference)
            .created(created1);
    Task task1 = taskBuilder.buildAndStore(taskService);
    Task task2 = taskBuilder.created(created2).buildAndStore(taskService);

    Task receivedTask1 = taskService.getTask(task1.getId());
    Task receivedTask2 = taskService.getTask(task2.getId());
    assertThat(receivedTask1.getCreated()).isEqualTo(created1);
    assertThat(receivedTask2.getCreated()).isEqualTo(created2);
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_AllowOverwritingOfCreatedValue_When_UsingTaskBuilder() throws Exception {
    Instant created = Instant.parse("2021-05-17T07:16:26.747Z");

    Task task =
        TaskBuilder.newTask()
            .workbasketSummary(workbasket)
            .classificationSummary(classification)
            .primaryObjRef(objectReference)
            .created(created)
            .created(null)
            .buildAndStore(taskService);

    Task receivedTask = taskService.getTask(task.getId());
    assertThat(receivedTask.getCreated()).isNotNull().isNotEqualTo(created);
  }
}
