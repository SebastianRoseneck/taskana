package acceptance.builder;

import static org.assertj.core.api.Assertions.assertThat;
import static pro.taskana.classification.internal.ClassificationBuilder.newClassification;
import static pro.taskana.task.internal.ObjectReferenceBuilder.defaultTestObjectReference;
import static pro.taskana.task.internal.TaskCommentBuilder.newTaskComment;
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
import pro.taskana.task.api.models.TaskComment;
import pro.taskana.task.internal.TaskBuilder;
import pro.taskana.task.internal.TaskCommentBuilder;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.models.Workbasket;

@ExtendWith(JaasExtension.class)
public class TaskCommentBuilderTest {

  private static TaskService taskService;
  private static Task task;

  @WithAccessId(user = "businessadmin")
  @BeforeAll
  static void setup() throws Exception {
    TaskanaEngine taskanaEngine = FooBar.getTaskanaEngineForTests();
    taskService = taskanaEngine.getTaskService();
    WorkbasketService workbasketService = taskanaEngine.getWorkbasketService();
    ClassificationService classificationService = taskanaEngine.getClassificationService();

    ObjectReference objectReference = defaultTestObjectReference().build();
    Workbasket workbasket =
        defaultTestWorkbasket().owner("user-1-1").key("key0_E").buildAndStore(workbasketService);
    Classification classification =
        newClassification().key("key0_E").domain("DOMAIN_A").buildAndStore(classificationService);
    newWorkbasketAccessItem()
        .workbasketId(workbasket.getId())
        .permRead(true)
        .accessId("user-1-1")
        .buildAndStore(workbasketService);
    newWorkbasketAccessItem()
        .workbasketId(workbasket.getId())
        .permRead(true)
        .permAppend(true)
        .accessId("businessadmin")
        .buildAndStore(workbasketService);
    task =
        TaskBuilder.newTask()
            .workbasketSummary(workbasket)
            .classificationSummary(classification)
            .primaryObjRef(objectReference)
            .buildAndStore(taskService);
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_CreateTaskComment_When_UsingTaskCommentBuilder() throws Exception {
    TaskComment taskComment = newTaskComment().taskId(task.getId()).buildAndStore(taskService);
    TaskComment receivedTaskComment = taskService.getTaskComment(taskComment.getId());
    assertThat(receivedTaskComment).isEqualTo(taskComment);
  }

  @WithAccessId(user = "admin")
  @Test
  void should_CreateTaskCommentAsUser_When_UsingTaskCommentBuilder() throws Exception {
    TaskComment taskComment =
        newTaskComment().taskId(task.getId()).buildAndStore(taskService, "user-1-1");
    TaskComment receivedTaskComment = taskService.getTaskComment(taskComment.getId());
    assertThat(receivedTaskComment).isEqualTo(taskComment);
    assertThat(receivedTaskComment.getCreator()).isEqualTo("user-1-1");
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_CreateTaskWithCreatedTimestamp_When_UsingTaskBuilder() throws Exception {
    Instant created = Instant.parse("2021-05-17T07:16:26.747Z");

    TaskComment taskComment =
        newTaskComment()
            .taskId(task.getId())
            .created(created)
            .buildAndStore(taskService, "user-1-1");

    TaskComment receivedTaskComment = taskService.getTaskComment(taskComment.getId());
    assertThat(receivedTaskComment.getCreated()).isEqualTo(created);
  }

  @WithAccessId(user = "admin")
  @Test
  void should_CreateTaskCommentWithModified_When_UsingTaskCommentBuilder() throws Exception {
    Instant modified = Instant.parse("2021-05-17T07:16:26.747Z");

    TaskComment taskComment =
        newTaskComment()
            .taskId(task.getId())
            .modified(modified)
            .buildAndStore(taskService, "user-1-1");

    TaskComment receivedTaskComment = taskService.getTaskComment(taskComment.getId());
    assertThat(receivedTaskComment.getModified()).isEqualTo(modified);
  }

  @WithAccessId(user = "admin")
  @Test
  void should_AllowReuseOfChangedDefaultValues_When_UsingTaskBuilder() throws Exception {
    Instant modified1 = Instant.parse("2021-05-17T07:16:26.747Z");
    Instant modified2 = Instant.parse("2021-05-17T07:17:26.747Z");
    Instant created = Instant.parse("2021-05-17T07:16:25.747Z");

    TaskCommentBuilder taskCommentBuilder = newTaskComment().taskId(task.getId()).created(created);

    TaskComment taskComment1 = taskCommentBuilder.modified(modified1).buildAndStore(taskService);
    TaskComment taskComment2 = taskCommentBuilder.modified(modified2).buildAndStore(taskService);

    TaskComment receivedComment1 = taskService.getTaskComment(taskComment1.getId());
    TaskComment receivedComment2 = taskService.getTaskComment(taskComment2.getId());
    assertThat(receivedComment1.getModified()).isEqualTo(modified1);
    assertThat(receivedComment2.getModified()).isEqualTo(modified2);
    assertThat(receivedComment1.getCreated())
        .isEqualTo(receivedComment2.getCreated())
        .isEqualTo(created);
  }

  @WithAccessId(user = "admin")
  @Test
  void should_AllowReassignmentOfCreatedValue_When_UsingTaskBuilder() throws Exception {
    Instant created1 = Instant.parse("2021-05-17T07:16:26.747Z");
    Instant created2 = Instant.parse("2021-05-17T07:17:26.747Z");

    TaskCommentBuilder taskBuilder = newTaskComment().taskId(task.getId()).created(created1);
    TaskComment taskComment1 = taskBuilder.buildAndStore(taskService);
    TaskComment taskComment2 = taskBuilder.created(created2).buildAndStore(taskService);

    TaskComment receivedTaskComment1 = taskService.getTaskComment(taskComment1.getId());
    TaskComment receivedTaskComment2 = taskService.getTaskComment(taskComment2.getId());
    assertThat(receivedTaskComment1.getCreated()).isEqualTo(created1);
    assertThat(receivedTaskComment2.getCreated()).isEqualTo(created2);
  }

  @WithAccessId(user = "admin")
  @Test
  void should_AllowOverwritingOfCreatedValue_When_UsingTaskBuilder() throws Exception {
    Instant created1 = Instant.parse("2021-05-17T07:16:26.747Z");

    TaskComment taskComment =
        newTaskComment()
            .taskId(task.getId())
            .created(created1)
            .created(null)
            .buildAndStore(taskService);

    TaskComment receivedTaskComment1 = taskService.getTaskComment(taskComment.getId());
    assertThat(receivedTaskComment1.getCreated()).isNotNull().isNotEqualTo(created1);
  }
}
