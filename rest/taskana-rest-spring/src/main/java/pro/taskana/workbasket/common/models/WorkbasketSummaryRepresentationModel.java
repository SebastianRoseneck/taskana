package pro.taskana.workbasket.common.models;

import io.leangen.graphql.annotations.GraphQLInputField;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import javax.validation.constraints.NotNull;
import org.springframework.hateoas.RepresentationModel;

import pro.taskana.workbasket.api.WorkbasketType;
import pro.taskana.workbasket.api.models.WorkbasketSummary;

/** EntityModel class for {@link WorkbasketSummary}. */
@GraphQLType(
    description =
        "Workbaskets are the main structure to distribute the tasks to the available users. There"
            + " are personal, group/team and topic Workbaskets.\n"
            + "\n"
            + "This is a specific short model-object which only requires the most important"
            + " information.")
public class WorkbasketSummaryRepresentationModel
    extends RepresentationModel<WorkbasketSummaryRepresentationModel> {

  /** Unique Id. */
  protected String workbasketId;
  /** the professional key for the workbasket. */
  @NotNull protected String key;
  /** The name of the workbasket. */
  @NotNull protected String name;
  /** The domain the workbasket belongs to. */
  @NotNull protected String domain;
  /** The type of the workbasket. */
  @NotNull protected WorkbasketType type;
  /** the description of the workbasket. */
  protected String description;
  /**
   * The owner of the workbasket. The owner is responsible for the on-time completion of all tasks
   * in the workbasket.
   */
  protected String owner;
  /** A custom property with name "1". */
  protected String custom1;
  /** A custom property with name "2". */
  protected String custom2;
  /** A custom property with name "3". */
  protected String custom3;
  /** A custom property with name "4". */
  protected String custom4;
  /**
   * The first Org Level (the top one).
   *
   * <p>The Org Level is an association with an org hierarchy level in the organization. The values
   * are used for monitoring and statistical purposes and should reflect who is responsible of the
   * tasks in the workbasket.
   */
  protected String orgLevel1;
  /** The second Org Level. */
  protected String orgLevel2;
  /** The third Org Level. */
  protected String orgLevel3;
  /** The fourth Org Level (the lowest one). */
  protected String orgLevel4;
  /** Identifier to tell if this workbasket can be deleted. */
  @GraphQLInputField(defaultValue = "false")
  private boolean markedForDeletion;

  @GraphQLQuery(description = "Unique Id.")
  public String getWorkbasketId() {
    return workbasketId;
  }

  public void setWorkbasketId(String workbasketId) {
    this.workbasketId = workbasketId;
  }

  @GraphQLQuery(description = "The professional key for the workbasket.")
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @GraphQLQuery(description = "The name of the workbasket.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @GraphQLQuery(description = "The domain the workbasket belongs to.")
  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  @GraphQLQuery(
      description =
          "The type of the workbasket.\n" + "Must be one of [GROUP, PERSONAL, TOPIC, CLEARANCE].")
  public WorkbasketType getType() {
    return type;
  }

  public void setType(WorkbasketType type) {
    this.type = type;
  }

  @GraphQLQuery(description = "The description of the workbasket.")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @GraphQLQuery(
      description =
          "The owner of the workbasket. The owner is responsible for the on-time completion of all"
              + " tasks in the workbasket.")
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  @GraphQLQuery(description = "A custom property with name \"1\".")
  public String getCustom1() {
    return custom1;
  }

  public void setCustom1(String custom1) {
    this.custom1 = custom1;
  }

  @GraphQLQuery(description = "A custom property with name \"2\".")
  public String getCustom2() {
    return custom2;
  }

  public void setCustom2(String custom2) {
    this.custom2 = custom2;
  }

  @GraphQLQuery(description = "A custom property with name \"3\".")
  public String getCustom3() {
    return custom3;
  }

  public void setCustom3(String custom3) {
    this.custom3 = custom3;
  }

  @GraphQLQuery(description = "A custom property with name \"4\".")
  public String getCustom4() {
    return custom4;
  }

  public void setCustom4(String custom4) {
    this.custom4 = custom4;
  }

  @GraphQLQuery(
      description =
          "The first Org Level (the top one).\n"
              + "The Org Level is an association with an org hierarchy level in the organization."
              + " The values are used for monitoring and statistical purposes and should reflect"
              + " who is responsible of the tasks in the workbasket.")
  public String getOrgLevel1() {
    return orgLevel1;
  }

  public void setOrgLevel1(String orgLevel1) {
    this.orgLevel1 = orgLevel1;
  }

  @GraphQLQuery(description = "The second Org Level.")
  public String getOrgLevel2() {
    return orgLevel2;
  }

  public void setOrgLevel2(String orgLevel2) {
    this.orgLevel2 = orgLevel2;
  }

  @GraphQLQuery(description = "The third Org Level.")
  public String getOrgLevel3() {
    return orgLevel3;
  }

  public void setOrgLevel3(String orgLevel3) {
    this.orgLevel3 = orgLevel3;
  }

  @GraphQLQuery(description = "The fourth Org Level (the lowest one).")
  public String getOrgLevel4() {
    return orgLevel4;
  }

  public void setOrgLevel4(String orgLevel4) {
    this.orgLevel4 = orgLevel4;
  }

  @GraphQLQuery(description = "Identifier to tell if this workbasket can be deleted.")
  public boolean getMarkedForDeletion() {
    return markedForDeletion;
  }

  public void setMarkedForDeletion(boolean markedForDeletion) {
    this.markedForDeletion = markedForDeletion;
  }
}
