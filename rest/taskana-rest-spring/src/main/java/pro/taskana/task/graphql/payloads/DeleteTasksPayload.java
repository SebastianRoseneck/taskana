package pro.taskana.task.graphql.payloads;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.util.List;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.task.graphql.resolvers.TaskResolver#deleteTasks deleteTasks} mutation.
 */
@GraphQLType(description = "Payload used as a return type for the deleteTasks mutation.")
public class DeleteTasksPayload {

  private final String message;
  private final List<String> ids;

  public DeleteTasksPayload(String message, List<String> ids) {
    this.message = message;
    this.ids = ids;
  }

  @GraphQLQuery(description = "Returned message of the called mutation.")
  public String getMessage() {
    return message;
  }

  @GraphQLQuery(description = "The IDs of the deleted Tasks.")
  public List<String> getIds() {
    return ids;
  }
}
