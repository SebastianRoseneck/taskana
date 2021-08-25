package pro.taskana.task.common.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.beans.ConstructorProperties;
import java.util.Collection;

import pro.taskana.common.rest.models.PageMetadata;
import pro.taskana.common.rest.models.PagedRepresentationModel;

@GraphQLType(
    description =
        "Tasks are the main entity of TASKANA. Each task has its describing attributes like"
            + " priority and due date.\n"
            + "\n"
            + "This type is used to return multiple Tasks depending on paging parameters.")
public class TaskSummaryPagedRepresentationModel
    extends PagedRepresentationModel<TaskSummaryRepresentationModel> {

  @ConstructorProperties({"tasks", "page"})
  public TaskSummaryPagedRepresentationModel(
      Collection<TaskSummaryRepresentationModel> content, PageMetadata pageMetadata) {
    super(content, pageMetadata);
  }

  /** The embedded tasks. */
  @JsonProperty("tasks")
  @Override
  public Collection<TaskSummaryRepresentationModel> getContent() {
    return super.getContent();
  }
}
