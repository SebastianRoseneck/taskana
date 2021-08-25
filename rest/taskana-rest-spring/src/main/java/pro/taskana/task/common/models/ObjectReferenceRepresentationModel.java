package pro.taskana.task.common.models;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import org.springframework.hateoas.RepresentationModel;

@GraphQLType(
    description =
        "Every Task and Attachment in Taskana must be linked to a real subject (e.g. a specific"
            + " claim). This link is implemented by the ObjectReference.")
public class ObjectReferenceRepresentationModel
    extends RepresentationModel<ObjectReferenceRepresentationModel> {

  /** Unique ID. */
  private String id;
  /** The company the referenced primary object belongs to. */
  private String company;
  /** The (kind of) system, the referenced primary object resides in (e.g. SAP, MySystem A, ...). */
  private String system;
  /** The instance of the system where the referenced primary object is located. */
  private String systemInstance;
  /** The type of the referenced primary object (contract, claim, policy, customer, ...). */
  private String type;
  /** The value of the primary object reference. */
  private String value;

  @GraphQLQuery(description = "Unique ID.")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @GraphQLQuery(description = "The company the referenced primary object belongs to.")
  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  @GraphQLQuery(
      description =
          "The (kind of) system, the referenced primary object resides in (e.g. SAP, MySystem A,"
              + " ...).")
  public String getSystem() {
    return system;
  }

  public void setSystem(String system) {
    this.system = system;
  }

  @GraphQLQuery(
      description = "The instance of the system where the referenced primary object is located.")
  public String getSystemInstance() {
    return systemInstance;
  }

  public void setSystemInstance(String systemInstance) {
    this.systemInstance = systemInstance;
  }

  @GraphQLQuery(
      description =
          "The type of the referenced primary object (contract, claim, policy, customer,...).")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @GraphQLQuery(description = "The value of the primary object reference.")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
