package pro.taskana.workbasket.common.models;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.time.Instant;

import pro.taskana.workbasket.api.models.Workbasket;

/** EntityModel class for {@link Workbasket}. */
@GraphQLType(
    description =
        "Workbaskets are the main structure to distribute the tasks to the available users. There"
            + " are personal, group/team and topic Workbaskets.")
public class WorkbasketRepresentationModel extends WorkbasketSummaryRepresentationModel {

  /**
   * The creation timestamp of the workbasket in the system.
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

  @GraphQLQuery(
      description =
          "The creation timestamp of the workbasket in the system.\n"
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
}
