package pro.taskana.workbasket.graphql.payloads;

import pro.taskana.workbasket.common.models.WorkbasketAccessItemCollectionRepresentationModel;

public class SetWorkbasketAccessItemsPayload {

  private final String message;
  private final String id;
  private final WorkbasketAccessItemCollectionRepresentationModel workbasketAccessItemsRepModel;

  public SetWorkbasketAccessItemsPayload(
      String message,
      String id,
      WorkbasketAccessItemCollectionRepresentationModel workbasketAccessItemsRepModel) {
    this.message = message;
    this.id = id;
    this.workbasketAccessItemsRepModel = workbasketAccessItemsRepModel;
  }

  public String getMessage() {
    return message;
  }

  public String getId() {
    return id;
  }

  public WorkbasketAccessItemCollectionRepresentationModel getWorkbasketAccessItemsRepModel() {
    return workbasketAccessItemsRepModel;
  }
}
