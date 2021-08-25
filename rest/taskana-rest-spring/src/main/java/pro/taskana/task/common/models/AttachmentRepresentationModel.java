package pro.taskana.task.common.models;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.util.HashMap;
import java.util.Map;

import pro.taskana.task.api.models.Attachment;

/** EntityModel class for {@link Attachment}. */
@GraphQLType(
    description = "An Attachment offers the possibility of providing further information to Tasks.")
public class AttachmentRepresentationModel extends AttachmentSummaryRepresentationModel {

  /** All additional information of the Attachment. */
  private Map<String, String> customAttributes = new HashMap<>();

  @GraphQLQuery(description = "All additional information of the Attachment.")
  public Map<String, String> getCustomAttributes() {
    return customAttributes;
  }

  public void setCustomAttributes(Map<String, String> customAttributes) {
    this.customAttributes = customAttributes;
  }
}
