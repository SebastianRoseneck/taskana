package pro.taskana.classification.common.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.beans.ConstructorProperties;
import java.util.Collection;
import javax.validation.constraints.NotNull;

import pro.taskana.common.rest.models.PageMetadata;
import pro.taskana.common.rest.models.PagedRepresentationModel;

@GraphQLType(
    description =
        "Classifications allow to identify the type of a task. The task derives some major"
            + " attributes from the classification, such as the service level and the priority.\n"
            + "\n"
            + "This type is used to return multiple Classifications depending on paging"
            + " parameters.")
public class ClassificationSummaryPagedRepresentationModel
    extends PagedRepresentationModel<ClassificationSummaryRepresentationModel> {

  @ConstructorProperties({"classifications", "page"})
  public ClassificationSummaryPagedRepresentationModel(
      Collection<ClassificationSummaryRepresentationModel> content, PageMetadata pageMetadata) {
    super(content, pageMetadata);
  }

  /** the embedded classifications. */
  @Override
  @JsonProperty("classifications")
  public @NotNull Collection<ClassificationSummaryRepresentationModel> getContent() {
    return super.getContent();
  }
}
