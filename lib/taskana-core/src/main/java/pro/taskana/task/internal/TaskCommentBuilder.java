package pro.taskana.task.internal;

import static pro.taskana.common.internal.util.CheckedFunction.wrap;

import java.security.PrivilegedAction;
import java.time.Instant;
import java.util.function.Function;
import javax.security.auth.Subject;

import pro.taskana.common.api.security.UserPrincipal;
import pro.taskana.task.api.TaskService;
import pro.taskana.task.api.models.TaskComment;

public class TaskCommentBuilder {

  private final TaskCommentTestImpl testTaskComment = new TaskCommentTestImpl();

  public static TaskCommentBuilder newTaskComment() {
    return new TaskCommentBuilder();
  }

  public TaskCommentBuilder taskId(String taskId) {
    testTaskComment.setTaskId(taskId);
    return this;
  }

  public TaskCommentBuilder textField(String textField) {
    testTaskComment.setTextField(textField);
    return this;
  }

  public TaskCommentBuilder created(Instant created) {
    testTaskComment.setCreatedIgnoringFreeze(created);
    if (created != null) {
      testTaskComment.freezeCreated();
    } else {
      testTaskComment.unfreezeCreated();
    }
    return this;
  }

  public TaskCommentBuilder modified(Instant modified) {
    testTaskComment.setModifiedIgnoringFreeze(modified);
    if (modified != null) {
      testTaskComment.freezeModified();
    } else {
      testTaskComment.unfreezeModified();
    }
    return this;
  }

  public TaskComment buildAndStore(TaskService taskService) throws Exception {
    testTaskComment.setId(null);
    TaskComment t = taskService.createTaskComment(testTaskComment);
    return taskService.getTaskComment(t.getId());
  }

  public TaskComment buildAndStore(TaskService taskService, String userId) throws Exception {
    Subject subject = new Subject();
    subject.getPrincipals().add(new UserPrincipal(userId));
    Function<TaskService, TaskComment> buildAndStore = wrap(this::buildAndStore);
    PrivilegedAction<TaskComment> performBuildAndStore = () -> buildAndStore.apply(taskService);

    return Subject.doAs(subject, performBuildAndStore);
  }
}
