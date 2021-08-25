package pro.taskana.classification.common.models;

import io.leangen.graphql.annotations.GraphQLInputField;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.time.Instant;

import pro.taskana.classification.api.models.Classification;

/** EntityModel class for {@link Classification}. */
@GraphQLType(
    description =
        "Classifications allow to identify the type of a task. The task derives some major"
            + " attributes from the classification, such as the service level and the priority.")
public class ClassificationRepresentationModel extends ClassificationSummaryRepresentationModel {

  /** True, if this classification to objects in this domain. */
  @GraphQLInputField(defaultValue = "true")
  private boolean isValidInDomain;
  /**
   * The creation timestamp of the classification in the system.
   *
   * <p>The format is ISO-8601.
   */
  private Instant created;
  /**
   * The timestamp of the last modification.
   *
   * <p>The format is ISO-8601.
   */
  private Instant modified;
  /** The description of the classification. */
  private String description;

  @GraphQLQuery(description = "True, if this classification to objects in this domain.")
  public boolean getIsValidInDomain() {
    return isValidInDomain;
  }

  public void setIsValidInDomain(boolean isValidInDomain) {
    this.isValidInDomain = isValidInDomain;
  }

  @GraphQLQuery(
      description =
          "The creation timestamp of the classification in the system.\n"
              + "\n"
              + "The format is ISO-8601.")
  public Instant getCreated() {
    return created;
  }

  public void setCreated(Instant created) {
    this.created = created;
  }

  @GraphQLQuery(
      description = "The timestamp of the last modification.\n" + "\n" + "The format is ISO-8601.")
  public Instant getModified() {
    return modified;
  }

  public void setModified(Instant modified) {
    this.modified = modified;
  }

  @GraphQLQuery(description = "The description of the classification.")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
