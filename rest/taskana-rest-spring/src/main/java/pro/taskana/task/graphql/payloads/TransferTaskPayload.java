package pro.taskana.task.graphql.payloads;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.task.graphql.resolvers.TaskResolver#transferTask transferTask} mutation.
 */
@GraphQLType(description = "Payload used as a return type for the transferTask mutation.")
public class TransferTaskPayload {

  private final String message;
  private final String taskId;
  private final String workbasketId;

  public TransferTaskPayload(String message, String taskId, String workbasketId) {
    this.message = message;
    this.taskId = taskId;
    this.workbasketId = workbasketId;
  }

  @GraphQLQuery(description = "Returned message of the called mutation.")
  public String getMessage() {
    return message;
  }

  @GraphQLQuery(description = "The ID of the transferred Task.")
  public String getTaskId() {
    return taskId;
  }

  @GraphQLQuery(description = "The ID of the Workbasket the Task was transferred to.")
  public String getWorkbasketId() {
    return workbasketId;
  }
}
