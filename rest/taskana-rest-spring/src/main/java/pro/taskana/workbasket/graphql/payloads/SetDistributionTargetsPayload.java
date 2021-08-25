package pro.taskana.workbasket.graphql.payloads;

import java.util.List;

/**
 * Payload used as a return type for the {@linkplain
 * pro.taskana.workbasket.graphql.resolvers.WorkbasketResolver#setDistriubtionTargets
 * setDistributionTargets} mutation.
 */
public class SetDistributionTargetsPayload {

  private final String message;
  private final String sourceWorkbasketId;
  private final List<String> targetWorkbasketIds;

  public SetDistributionTargetsPayload(
      String message, String sourceWorkbasketId, List<String> targetWorkbasketIds) {
    this.message = message;
    this.sourceWorkbasketId = sourceWorkbasketId;
    this.targetWorkbasketIds = targetWorkbasketIds;
  }

  public String getMessage() {
    return message;
  }

  public String getSourceWorkbasketId() {
    return sourceWorkbasketId;
  }

  public List<String> getTargetWorkbasketIds() {
    return targetWorkbasketIds;
  }
}
