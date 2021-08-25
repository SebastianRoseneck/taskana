package pro.taskana.workbasket.common.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.beans.ConstructorProperties;
import java.util.Collection;

import pro.taskana.common.rest.models.PageMetadata;
import pro.taskana.common.rest.models.PagedRepresentationModel;

@GraphQLType(
    description =
        "Workbaskets are the main structure to distribute the tasks to the available users. There"
            + " are personal, group/team and topic Workbaskets.\n"
            + "\n"
            + "This type is used to return multiple Workbaskets depending on paging parameters.")
public class WorkbasketSummaryPagedRepresentationModel
    extends PagedRepresentationModel<WorkbasketSummaryRepresentationModel> {

  @ConstructorProperties({"workbaskets", "page"})
  public WorkbasketSummaryPagedRepresentationModel(
      Collection<WorkbasketSummaryRepresentationModel> content, PageMetadata pageMetadata) {
    super(content, pageMetadata);
  }

  /** the embedded workbaskets. */
  @JsonProperty("workbaskets")
  @Override
  public Collection<WorkbasketSummaryRepresentationModel> getContent() {
    return super.getContent();
  }
}
