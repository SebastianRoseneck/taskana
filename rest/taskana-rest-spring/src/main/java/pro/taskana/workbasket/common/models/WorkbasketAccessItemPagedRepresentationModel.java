package pro.taskana.workbasket.common.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.beans.ConstructorProperties;
import java.util.Collection;

import pro.taskana.common.rest.models.PageMetadata;
import pro.taskana.common.rest.models.PagedRepresentationModel;

@GraphQLType(
    description =
        "WorkbasketAccessItems define the rights of a user or group for a Workbasket.\n"
            + "\n"
            + "This type is used to return multiple WorkbasketAccessItems depending on paging"
            + " parameters.")
public class WorkbasketAccessItemPagedRepresentationModel
    extends PagedRepresentationModel<WorkbasketAccessItemRepresentationModel> {

  @ConstructorProperties({"accessItems", "page"})
  public WorkbasketAccessItemPagedRepresentationModel(
      Collection<WorkbasketAccessItemRepresentationModel> content, PageMetadata pageMetadata) {
    super(content, pageMetadata);
  }

  /** the embedded access items. */
  @JsonProperty("accessItems")
  @Override
  public Collection<WorkbasketAccessItemRepresentationModel> getContent() {
    return super.getContent();
  }
}
