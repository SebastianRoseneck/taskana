package pro.taskana.workbasket.common.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.leangen.graphql.annotations.GraphQLInputField;
import java.beans.ConstructorProperties;
import java.util.Optional;

import pro.taskana.common.rest.QueryParameter;
import pro.taskana.workbasket.api.WorkbasketPermission;
import pro.taskana.workbasket.api.WorkbasketQuery;
import pro.taskana.workbasket.api.WorkbasketType;

public class WorkbasketQueryFilterParameter implements QueryParameter<WorkbasketQuery, Void> {

  /** Filter by the name of the workbasket. This is an exact match. */
  @JsonProperty("name")
  private final String[] name;

  /**
   * Filter by the name of the workbasket. This results in a substring search. (% is appended to the
   * beginning and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("nameLike")
  private final String[] nameLike;

  /** Filter by the key of the workbasket. This is an exact match. */
  @JsonProperty("key")
  private final String[] key;

  /**
   * Filter by the key of the workbasket. This results in a substring search.. (% is appended to the
   * beginning and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("keyLike")
  private final String[] keyLike;

  /** Filter by the owner of the workbasket. This is an exact match. */
  @JsonProperty("owner")
  private final String[] owner;

  /**
   * Filter by the owner of the workbasket. This results in a substring search.. (% is appended to
   * the beginning and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("ownerLike")
  private final String[] ownerLike;

  /**
   * Filter by the description of the workbasket. This results in a substring search.. (% is
   * appended to the beginning and end of the requested value). Further SQL "LIKE" wildcard
   * characters will be resolved correctly.
   */
  @JsonProperty("descriptionLike")
  private final String[] descriptionLike;

  /** Filter by the domain of the workbasket. This is an exact match. */
  @JsonProperty("domain")
  private final String[] domain;

  /** Filter by the type of the workbasket. This is an exact match. */
  @JsonProperty("type")
  private final WorkbasketType[] type;

  /** Filter by the required permission for the workbasket. */
  @JsonProperty("requiredPermission")
  private final WorkbasketPermission requiredPermissions;

  @SuppressWarnings("indentation")
  @ConstructorProperties({
    "name",
    "nameLike",
    "key",
    "keyLike",
    "owner",
    "ownerLike",
    "descriptionLike",
    "domain",
    "type",
    "requiredPermission"
  })
  public WorkbasketQueryFilterParameter(
      @GraphQLInputField(
              description = "Filter by the name of the workbasket. This is an exact match.")
          String[] name,
      @GraphQLInputField(
              description =
                  "Filter by the name of the workbasket. This results in a substring search. (% is"
                      + " appended to the beginning and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] nameLike,
      @GraphQLInputField(
              description = "Filter by the key of the workbasket. This is an exact match.")
          String[] key,
      @GraphQLInputField(
              description =
                  "Filter by the key of the workbasket. This results in a substring search.. (% is"
                      + " appended to the beginning and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] keyLike,
      @GraphQLInputField(
              description = "Filter by the owner of the workbasket. This is an exact match.")
          String[] owner,
      @GraphQLInputField(
              description =
                  "Filter by the owner of the workbasket. This results in a substring search.. (%"
                      + " is appended to the beginning and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] ownerLike,
      @GraphQLInputField(
              description =
                  "Filter by the description of the workbasket. This results in a substring"
                      + " search.. (% is appended to the beginning and end of the requested value)."
                      + " Further SQL \"LIKE\" wildcard characters will be resolved correctly.")
          String[] descriptionLike,
      @GraphQLInputField(
              description = "Filter by the domain of the workbasket. This is an exact match.")
          String[] domain,
      @GraphQLInputField(
              description =
                  "Filter by the type of the workbasket. This is an exact match.\n"
                      + "\n"
                      + "Must be one of [GROUP, PERSONAL, TOPIC, CLEARANCE].")
          WorkbasketType[] type,
      @GraphQLInputField(
              description =
                  "Filter by the required permission for the workbasket.\n"
                      + "\n"
                      + "Must be one of [READ, OPEN, APPEND, TRANSFER, DISTRIBUTE, CUSTOM_1,"
                      + " CUSTOM_2, CUSTOM_3, CUSTOM_4, CUSTOM_5, CUSTOM_6, CUSTOM_7, CUSTOM_8,"
                      + " CUSTOM_9, CUSTOM_10, CUSTOM_11, CUSTOM_12].")
          WorkbasketPermission requiredPermissions) {
    this.name = name;
    this.nameLike = nameLike;
    this.key = key;
    this.keyLike = keyLike;
    this.owner = owner;
    this.ownerLike = ownerLike;
    this.descriptionLike = descriptionLike;
    this.domain = domain;
    this.type = type;
    this.requiredPermissions = requiredPermissions;
  }

  @Override
  public Void apply(WorkbasketQuery query) {
    Optional.ofNullable(name).ifPresent(query::nameIn);
    Optional.ofNullable(nameLike).map(this::wrapElementsInLikeStatement).ifPresent(query::nameLike);
    Optional.ofNullable(key).ifPresent(query::keyIn);
    Optional.ofNullable(keyLike).map(this::wrapElementsInLikeStatement).ifPresent(query::keyLike);
    Optional.ofNullable(owner).ifPresent(query::ownerIn);
    Optional.ofNullable(ownerLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::ownerLike);
    Optional.ofNullable(descriptionLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::descriptionLike);
    Optional.ofNullable(domain).ifPresent(query::domainIn);
    Optional.ofNullable(type).ifPresent(query::typeIn);
    Optional.ofNullable(requiredPermissions).ifPresent(query::callerHasPermission);
    return null;
  }
}
