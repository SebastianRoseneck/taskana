package pro.taskana.workbasket.common.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.leangen.graphql.annotations.GraphQLInputField;
import java.beans.ConstructorProperties;
import java.util.Optional;

import pro.taskana.common.rest.QueryParameter;
import pro.taskana.workbasket.api.WorkbasketAccessItemQuery;

public class WorkbasketAccessItemQueryFilterParameter
    implements QueryParameter<WorkbasketAccessItemQuery, Void> {

  /** Filter by the key of the workbasket. This is an exact match. */
  @JsonProperty("workbasketKey")
  private final String[] workbasketKey;

  /**
   * Filter by the key of the workbasket. This results in a substring search.. (% is appended to the
   * beginning and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("workbasketKeyLike")
  private final String[] workbasketKeyLike;

  /** Filter by the name of the access id. This is an exact match. */
  @JsonProperty("accessId")
  private final String[] accessId;

  /**
   * Filter by the name of the access id. This results in a substring search.. (% is appended to the
   * beginning and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("accessIdLike")
  private final String[] accessIdLike;

  @ConstructorProperties({"workbasketKey", "workbasketKeyLike", "access-id", "access-id-like"})
  public WorkbasketAccessItemQueryFilterParameter(
      @GraphQLInputField(
              description = "Filter by the key of the workbasket. This is an exact match.")
          String[] workbasketKey,
      @GraphQLInputField(
              description =
                  "Filter by the key of the workbasket. This results in a substring search.. (% is"
                      + " appended to the beginning and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] workbasketKeyLike,
      @GraphQLInputField(
              description = "Filter by the name of the access id. This is an exact match.")
          String[] accessId,
      @GraphQLInputField(
              description =
                  "Filter by the name of the access id. This results in a substring search.. (% is"
                      + " appended to the beginning and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] accessIdLike) {
    this.workbasketKey = workbasketKey;
    this.workbasketKeyLike = workbasketKeyLike;
    this.accessId = accessId;
    this.accessIdLike = accessIdLike;
  }

  @Override
  public Void apply(WorkbasketAccessItemQuery query) {
    Optional.ofNullable(workbasketKey).ifPresent(query::workbasketKeyIn);
    Optional.ofNullable(workbasketKeyLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::workbasketKeyLike);
    Optional.ofNullable(accessId).ifPresent(query::accessIdIn);
    Optional.ofNullable(accessIdLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::accessIdLike);
    return null;
  }
}
