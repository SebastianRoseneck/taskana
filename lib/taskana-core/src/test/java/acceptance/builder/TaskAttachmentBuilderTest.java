package acceptance.builder;

import static org.assertj.core.api.Assertions.assertThat;

import acceptance.FooBar;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import pro.taskana.classification.api.ClassificationService;
import pro.taskana.classification.api.models.Classification;
import pro.taskana.classification.internal.ClassificationBuilder;
import pro.taskana.common.api.TaskanaEngine;
import pro.taskana.common.test.security.JaasExtension;
import pro.taskana.common.test.security.WithAccessId;
import pro.taskana.task.api.TaskService;
import pro.taskana.task.api.models.Attachment;
import pro.taskana.task.api.models.ObjectReference;
import pro.taskana.task.api.models.Task;
import pro.taskana.task.internal.ObjectReferenceBuilder;
import pro.taskana.task.internal.TaskAttachmentBuilder;
import pro.taskana.task.internal.TaskBuilder;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.models.Workbasket;
import pro.taskana.workbasket.internal.WorkbasketAccessItemBuilder;
import pro.taskana.workbasket.internal.WorkbasketBuilder;

@ExtendWith(JaasExtension.class)
public class TaskAttachmentBuilderTest {

  private static ClassificationService classificationService;
  private static TaskService taskService;
  private static WorkbasketService workbasketService;

  private static ObjectReference objectReference;
  private static Classification classification;
  private static Workbasket workbasket;

  @WithAccessId(user = "businessadmin")
  @BeforeAll
  static void setup() throws Exception {
    TaskanaEngine taskanaEngine = FooBar.getTaskanaEngineForTests();
    classificationService = taskanaEngine.getClassificationService();
    taskService = taskanaEngine.getTaskService();
    workbasketService = taskanaEngine.getWorkbasketService();

    objectReference =
        ObjectReferenceBuilder.newObjectReference()
            .company("Company1")
            .system("System1")
            .systemInstance("Instance1")
            .type("Type1")
            .value("Value1")
            .build();
    classification =
        ClassificationBuilder.newClassification()
            .key("key0_C")
            .domain("DOMAIN_A")
            .buildAndStore(classificationService);
    workbasket =
        WorkbasketBuilder.defaultTestWorkbasket().key("key0_C").buildAndStore(workbasketService);
    WorkbasketAccessItemBuilder.newWorkbasketAccessItem()
        .workbasketId(workbasket.getId())
        .accessId("user-1-1")
        .permAppend(true)
        .permRead(true)
        .buildAndStore(workbasketService);
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_CreateAttachment_When_UsingTaskAttachmentBuilder() throws Exception {
    Attachment attachment =
        TaskAttachmentBuilder.newAttachment()
            .classificationSummary(classification.asSummary())
            .objectReference(objectReference)
            .build();
    Task task =
        TaskBuilder.newTask()
            .classificationSummary(classification)
            .workbasketSummary(workbasket)
            .primaryObjRef(objectReference)
            .attachments(List.of(attachment))
            .buildAndStore(taskService);
    assertThat(task.getAttachments()).hasSize(1);

    Attachment receivedAttachment = task.getAttachments().get(0);
    assertThat(receivedAttachment.getTaskId()).isEqualTo(task.getId());
    assertThat(receivedAttachment.getId()).isNotNull();
  }

  @WithAccessId(user = "admin")
  @Test
  void should_AllowReassignmentOfCreatedValue_When_UsingTaskAttachmentBuilder() throws Exception {
    String value1 = "Value1";
    String value2 = "Value2";
    ObjectReferenceBuilder objectReferenceBuilder =
        ObjectReferenceBuilder.newObjectReference()
            .company("Company")
            .system("System1")
            .systemInstance("Instance1")
            .value(value1)
            .type("Type1");
    ObjectReference objectReference1 = objectReferenceBuilder.build();
    ObjectReference objectReference2 = objectReferenceBuilder.value(value2).build();
    Classification classification =
        ClassificationBuilder.newClassification()
            .key("key0")
            .domain("DOMAIN_A")
            .buildAndStore(classificationService);
    Workbasket workbasket =
        WorkbasketBuilder.defaultTestWorkbasket().key("key0").buildAndStore(workbasketService);
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
