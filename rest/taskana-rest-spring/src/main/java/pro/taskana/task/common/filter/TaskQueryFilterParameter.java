package pro.taskana.task.common.filter;

import static pro.taskana.common.internal.util.CheckedConsumer.wrap;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.leangen.graphql.annotations.GraphQLInputField;
import java.beans.ConstructorProperties;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import pro.taskana.common.api.KeyDomain;
import pro.taskana.common.api.TimeInterval;
import pro.taskana.common.api.exceptions.InvalidArgumentException;
import pro.taskana.common.internal.util.Pair;
import pro.taskana.common.rest.QueryParameter;
import pro.taskana.task.api.CallbackState;
import pro.taskana.task.api.TaskCustomField;
import pro.taskana.task.api.TaskQuery;
import pro.taskana.task.api.TaskState;
import pro.taskana.task.api.WildcardSearchField;
import pro.taskana.task.api.models.ObjectReference;

public class TaskQueryFilterParameter implements QueryParameter<TaskQuery, Void> {

  /** Filter by the name of the task. This is an exact match. */
  @JsonProperty("name")
  private final String[] name;
  /**
   * Filter by the name of the task. This results in a substring search (% is appended to the front
   * and end of the requested value). Further SQL "LIKE" wildcard characters will be resolved
   * correctly.
   */
  @JsonProperty("nameLike")
  private final String[] nameLike;

  /** Filter by the priority of the task. This is an exact match. */
  @JsonProperty("priority")
  private final int[] priority;

  /** Filter by the task state. This is an exact match. */
  @JsonProperty("state")
  private final TaskState[] state;

  /** Filter by the classification id of the task. This is an exact match. */
  @JsonProperty("classificationId")
  private final String[] classificationId;

  /** Filter by the classification key of the task. This is an exact match. */
  @JsonProperty("classificationKey")
  private final String[] classificationKeys;

  /**
   * Filter by the classification key of the task. This results in a substring search (% is appended
   * to the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("classificationKeyLike")
  private final String[] classificationKeysLike;

  /** Filter by the classification key of the task. This is an exact match. */
  @JsonProperty("classificationKeyNotIn")
  private final String[] classificationKeysNotIn;

  /** Filter by the is read flag of the task. This is an exact match. */
  @JsonProperty("isRead")
  private final Boolean isRead;

  /** Filter by the is transferred flag of the task. This is an exact match. */
  @JsonProperty("isTransferred")
  private final Boolean isTransferred;

  /** Filter by the primary object reference of the task. This is an exact match. */
  @JsonProperty("objectReference")
  private final ObjectReference[] objectReferences;

  /** Filter by the callback state of the task. This is an exact match. */
  @JsonProperty("callbackState")
  private final CallbackState[] callbackStates;

  /** Filter by the attachment classification key of the task. This is an exact match. */
  @JsonProperty("attachmentClassificationKey")
  private final String[] attachmentClassificationKeys;

  /**
   * Filter by the attachment classification key of the task. This results in a substring search (%
   * is appended to the front and end of the requested value). Further SQL "LIKE" wildcard
   * characters will be resolved correctly.
   */
  @JsonProperty("attachmentClassificationKeyLike")
  private final String[] attachmentClassificationKeysLike;

  /** Filter by the attachment classification id of the task. This is an exact match. */
  @JsonProperty("attachmentClassificationId")
  private final String[] attachmentClassificationId;

  /**
   * Filter by the attachment classification id of the task. This results in a substring search (%
   * is appended to the front and end of the requested value). Further SQL "LIKE" wildcard
   * characters will be resolved correctly.
   */
  @JsonProperty("attachmentClassificationIdLike")
  private final String[] attachmentClassificationIdLike;

  /** Filter by the attachment channel of the task. This is an exact match. */
  @JsonProperty("attachmentChannel")
  private final String[] attachmentChannel;

  /**
   * Filter by the attachment channel of the task. This results in a substring search (% is appended
   * to the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("attachmentChannelLike")
  private final String[] attachmentChannelLike;

  /** Filter by the attachment reference of the task. This is an exact match. */
  @JsonProperty("attachmentReference")
  private final String[] attachmentReference;

  /**
   * Filter by the attachment reference of the task. This results in a substring search (% is
   * appended to the front and end of the requested value). Further SQL "LIKE" wildcard characters
   * will be resolved correctly.
   */
  @JsonProperty("attachmentReferenceLike")
  private final String[] attachmentReferenceLike;

  /**
   * Filter by a time interval within which the attachment of the task was received. To create an
   * open interval you can just leave it blank.
   *
   * <p>The format is ISO-8601.
   */
  @JsonProperty("attachmentReceived")
  private final Instant[] attachmentReceived;

  /**
   * Filter by a time interval within which the task was created. To create an open interval you can
   * just leave it blank.
   *
   * <p>The format is ISO-8601.
   */
  @JsonProperty("created")
  private final Instant[] created;

  /**
   * Filter since a given created timestamp.
   *
   * <p>The format is ISO-8601.
   *
   * <p>This parameter can't be used together with 'created'.
   */
  @JsonProperty("createdFrom")
  private final Instant createdFrom;

  /**
   * Filter until a given created timestamp.
   *
   * <p>The format is ISO-8601.
   *
   * <p>This parameter can't be used together with 'created'.
   */
  @JsonProperty("createdUntil")
  private final Instant createdUntil;

  /**
   * Filter by a time interval within which the task was claimed. To create an open interval you can
   * just leave it blank.
   *
   * <p>The format is ISO-8601.
   */
  @JsonProperty("claimed")
  private final Instant[] claimed;

  /**
   * Filter by a time interval within which the task was completed. To create an open interval you
   * can just leave it blank.
   *
   * <p>The format is ISO-8601.
   */
  @JsonProperty("completed")
  private final Instant[] completed;

  /**
   * Filter since a given completed timestamp.
   *
   * <p>The format is ISO-8601.
   *
   * <p>This parameter can't be used together with 'completed'.
   */
  @JsonProperty("completedFrom")
  private final Instant completedFrom;

  /**
   * Filter until a given completed timestamp.
   *
   * <p>The format is ISO-8601.
   *
   * <p>This parameter can't be used together with 'completed'.
   */
  @JsonProperty("completedUntil")
  private final Instant completedUntil;

  /**
   * Filter by a time interval within which the task was modified. To create an open interval you
   * can just leave it blank.
   *
   * <p>The format is ISO-8601.
   */
  @JsonProperty("modified")
  private final Instant[] modified;

  /** Filter by the classification category of the task. This is an exact match. */
  @JsonProperty("classificationCategory")
  private final String[] classificationCategories;

  /**
   * Filter by the classification category of the task. This results in a substring search (% is
   * appended to the front and end of the requested value). Further SQL "LIKE" wildcard characters
   * will be resolved correctly.
   */
  @JsonProperty("classificationCategoryLike")
  private final String[] classificationCategoriesLike;

  /** Filter by the classification name of the task. This is an exact match. */
  @JsonProperty("classificationName")
  private final String[] classificationNames;

  /**
   * Filter by the classification name of the task. This results in a substring search (% is
   * appended to the front and end of the requested value). Further SQL "LIKE" wildcard characters
   * will be resolved correctly.
   */
  @JsonProperty("classificationNameLike")
  private final String[] classificationNamesLike;

  /** Filter by the attachment classification name of the task. This is an exact match. */
  @JsonProperty("attachmentClassificationName")
  private final String[] attachmentClassificationNames;

  /**
   * Filter by the attachment classification name of the task. This results in a substring search (%
   * is appended to the front and end of the requested value). Further SQL "LIKE" wildcard
   * characters will be resolved correctly.
   */
  @JsonProperty("attachmentClassificationNameLike")
  private final String[] attachmentClassificationNamesLike;

  /** Filter by the parent business process id of the task. This is an exact match. */
  @JsonProperty("parentBusinessProcessId")
  private final String[] parentBusinessProcessIds;

  /**
   * Filter by the parent business process id of the task. This results in a substring search (% is
   * appended to the front and end of the requested value). Further SQL "LIKE" wildcard characters
   * will be resolved correctly.
   */
  @JsonProperty("parentBusinessProcessIdLike")
  private final String[] parentBusinessProcessIdsLike;

  /** Filter by the business process id of the task. This is an exact match. */
  @JsonProperty("businessProcessId")
  private final String[] businessProcessIds;

  /**
   * Filter by the business process id of the task. This results in a substring search (% is
   * appended to the front and end of the requested value). Further SQL "LIKE" wildcard characters
   * will be resolved correctly.
   */
  @JsonProperty("businessProcessIdLike")
  private final String[] businessProcessIdsLike;

  /** Filter by task id. This is an exact match. */
  @JsonProperty("taskId")
  private final String[] taskIds;

  /** Filter by workbasket id of the task. This is an exact match. */
  @JsonProperty("workbasketId")
  private final String[] workbasketIds;

  /**
   * Filter by workbasket keys of the task. This parameter can only be used in combination with
   * 'domain'
   */
  @JsonProperty("workbasketKey")
  private final String[] workbasketKeys;

  /** Filter by domain of the task. This is an exact match. */
  @JsonProperty("domain")
  private final String domain;

  /** Filter by owner of the task. This is an exact match. */
  @JsonProperty("owner")
  private final String[] owner;

  /**
   * Filter by the owner of the task. This results in a substring search (% is appended to the front
   * and end of the requested value). Further SQL "LIKE" wildcard characters will be resolved
   * correctly.
   */
  @JsonProperty("ownerLike")
  private final String[] ownerLike;

  /** Filter by creator of the task. This is an exact match. */
  @JsonProperty("creator")
  private final String[] creator;

  /**
   * Filter by the creator of the task. This results in a substring search (% is appended to the
   * front and end of the requested value). Further SQL "LIKE" wildcard characters will be resolved
   * correctly.
   */
  @JsonProperty("creatorLike")
  private final String[] creatorLike;

  /**
   * Filter by the note of the task. This results in a substring search (% is appended to the front
   * and end of the requested value). Further SQL "LIKE" wildcard characters will be resolved
   * correctly.
   */
  @JsonProperty("noteLike")
  private final String[] noteLike;

  /** Filter by the company of the primary object reference of the task. This is an exact match. */
  @JsonProperty("porCompany")
  private final String[] porCompany;

  /**
   * Filter by the company of the primary object reference of the task. This results in a substring
   * search (% is appended to the front and end of the requested value). Further SQL "LIKE" wildcard
   * characters will be resolved correctly.
   */
  @JsonProperty("porCompanyLike")
  private final String[] porCompanyLike;

  /** Filter by the system of the primary object reference of the task. This is an exact match. */
  @JsonProperty("porSystem")
  private final String[] porSystem;

  /**
   * Filter by the he system of the primary object reference of the task. This results in a
   * substring search (% is appended to the front and end of the requested value). Further SQL
   * "LIKE" wildcard characters will be resolved correctly.
   */
  @JsonProperty("porSystemLike")
  private final String[] porSystemLike;

  /**
   * Filter by the system instance of the primary object reference of the task. This is an exact
   * match.
   */
  @JsonProperty("porInstance")
  private final String[] porInstance;

  /**
   * Filter by the system instance of the primary object reference of the task. This results in a
   * substring search (% is appended to the front and end of the requested value). Further SQL
   * "LIKE" wildcard characters will be resolved correctly.
   */
  @JsonProperty("porInstanceLike")
  private final String[] porInstanceLike;

  /** Filter by the type of the primary object reference of the task. This is an exact match. */
  @JsonProperty("porType")
  private final String[] porType;

  /**
   * Filter by the type of the primary object reference of the task. This results in a substring
   * search (% is appended to the front and end of the requested value). Further SQL "LIKE" wildcard
   * characters will be resolved correctly.
   */
  @JsonProperty("porTypeLike")
  private final String[] porTypeLike;

  /** Filter by the value of the primary object reference of the task. This is an exact match. */
  @JsonProperty("porValue")
  private final String[] porValue;

  /**
   * Filter by the value of the primary object reference of the task. This results in a substring
   * search (% is appended to the front and end of the requested value). Further SQL "LIKE" wildcard
   * characters will be resolved correctly.
   */
  @JsonProperty("porValueLike")
  private final String[] porValueLike;

  /**
   * Filter by a time interval within which the task was planned. To create an open interval you can
   * just leave it blank.
   *
   * <p>The format is ISO-8601.
   *
   * <p>This parameter can't be used together with 'planned-from' or 'planned-until'.
   */
  @JsonProperty("planned")
  private final Instant[] planned;

  /**
   * Filter since a given planned timestamp.
   *
   * <p>The format is ISO-8601.
   *
   * <p>This parameter can't be used together with 'planned'.
   */
  @JsonProperty("plannedFrom")
  private final Instant plannedFrom;

  /**
   * Filter until a given planned timestamp.
   *
   * <p>The format is ISO-8601.
   *
   * <p>This parameter can't be used together with 'planned'.
   */
  @JsonProperty("plannedUntil")
  private final Instant plannedUntil;

  /**
   * Filter by a time interval within which the task was received. To create an open interval you
   * can just leave it blank.
   *
   * <p>The format is ISO-8601.
   *
   * <p>This parameter can't be used together with 'received-from' or 'received-until'.
   */
  @JsonProperty("received")
  private final Instant[] received;

  /**
   * Filter since a given received timestamp.
   *
   * <p>The format is ISO-8601.
   *
   * <p>This parameter can't be used together with 'received'.
   */
  @JsonProperty("receivedFrom")
  private final Instant receivedFrom;

  /**
   * Filter until a given received timestamp.
   *
   * <p>The format is ISO-8601.
   *
   * <p>This parameter can't be used together with 'received'.
   */
  @JsonProperty("receivedUntil")
  private final Instant receivedUntil;

  /**
   * Filter by a time interval within which the task was due. To create an open interval you can
   * just leave it blank.
   *
   * <p>The format is ISO-8601.
   *
   * <p>This parameter can't be used together with 'due-from' or 'due-until'.
   */
  @JsonProperty("due")
  private final Instant[] due;

  /**
   * Filter since a given due timestamp.
   *
   * <p>The format is ISO-8601.
   *
   * <p>This parameter can't be used together with 'due'.
   */
  @JsonProperty("dueFrom")
  private final Instant dueFrom;

  /**
   * Filter until a given due timestamp.
   *
   * <p>The format is ISO-8601.
   *
   * <p>This parameter can't be used together with 'due'.
   */
  @JsonProperty("dueUntil")
  private final Instant dueUntil;

  /**
   * Filter by wildcard search field of the task.
   *
   * <p>This must be used in combination with 'wildcard-search-value'
   */
  @JsonProperty("wildcardSearchFields")
  private final WildcardSearchField[] wildcardSearchFields;

  /**
   * Filter by wildcard search field of the task. This is an exact match.
   *
   * <p>This must be used in combination with 'wildcard-search-value'
   */
  @JsonProperty("wildcardSearchValue")
  private final String wildcardSearchValue;

  /** Filter by the external id of the task. This is an exact match. */
  @JsonProperty("externalId")
  private final String[] externalIds;
  /**
   * Filter by the externalId of the task. This results in a substring search (% is appended to the
   * front and end of the requested value). Further SQL "LIKE" wildcard characters will be resolved
   * correctly.
   */
  @JsonProperty("externalIdLike")
  private final String[] externalIdsLike;

  /** Filter by the value of the field custom1 of the task. This is an exact match. */
  @JsonProperty("custom1")
  private final String[] custom1;

  /** Exclude values of the field custom1 of the task. */
  @JsonProperty("custom1NotIn")
  private final String[] custom1NotIn;

  /**
   * Filter by the custom1 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom1Like")
  private final String[] custom1Like;

  /** Filter by the value of the field custom2 of the task. This is an exact match. */
  @JsonProperty("custom2")
  private final String[] custom2;

  /** Filter out by values of the field custom2 of the task. This is an exact match. */
  @JsonProperty("custom2NotIn")
  private final String[] custom2NotIn;

  /**
   * Filter by the custom2 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom2Like")
  private final String[] custom2Like;

  /** Filter by the value of the field custom3 of the task. This is an exact match. */
  @JsonProperty("custom3")
  private final String[] custom3;

  /** Filter out by values of the field custom3 of the task. This is an exact match. */
  @JsonProperty("custom3NotIn")
  private final String[] custom3NotIn;

  /**
   * Filter by the custom3 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom3Like")
  private final String[] custom3Like;

  /** Filter by the value of the field custom4 of the task. This is an exact match. */
  @JsonProperty("custom4")
  private final String[] custom4;

  /** Filter out by values of the field custom4 of the task. This is an exact match. */
  @JsonProperty("custom4NotIn")
  private final String[] custom4NotIn;

  /**
   * Filter by the custom4 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom4Like")
  private final String[] custom4Like;

  /** Filter by the value of the field custom5 of the task. This is an exact match. */
  @JsonProperty("custom5")
  private final String[] custom5;

  /** Filter out by values of the field custom5 of the task. This is an exact match. */
  @JsonProperty("custom5NotIn")
  private final String[] custom5NotIn;

  /**
   * Filter by the custom5 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom5Like")
  private final String[] custom5Like;

  /** Filter by the value of the field custom6 of the task. This is an exact match. */
  @JsonProperty("custom6")
  private final String[] custom6;

  /** Filter out by values of the field custom6 of the task. This is an exact match. */
  @JsonProperty("custom6NotIn")
  private final String[] custom6NotIn;

  /**
   * Filter by the custom6 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom6Like")
  private final String[] custom6Like;

  /** Filter by the value of the field custom7 of the task. This is an exact match. */
  @JsonProperty("custom7")
  private final String[] custom7;

  /** Filter out by values of the field custom7 of the task. This is an exact match. */
  @JsonProperty("custom7NotIn")
  private final String[] custom7NotIn;

  /**
   * Filter by the custom7 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom7Like")
  private final String[] custom7Like;

  /** Filter by the value of the field custom8 of the task. This is an exact match. */
  @JsonProperty("custom8")
  private final String[] custom8;

  /** Filter out by values of the field custom8 of the task. This is an exact match. */
  @JsonProperty("custom8NotIn")
  private final String[] custom8NotIn;

  /**
   * Filter by the custom8 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom8Like")
  private final String[] custom8Like;

  /** Filter by the value of the field custom9 of the task. This is an exact match. */
  @JsonProperty("custom9")
  private final String[] custom9;

  /** Filter out by values of the field custom9 of the task. This is an exact match. */
  @JsonProperty("custom9NotIn")
  private final String[] custom9NotIn;

  /**
   * Filter by the custom9 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom9Like")
  private final String[] custom9Like;

  /** Filter by the value of the field custom10 of the task. This is an exact match. */
  @JsonProperty("custom10")
  private final String[] custom10;

  /** Filter out by values of the field custom10 of the task. This is an exact match. */
  @JsonProperty("custom10NotIn")
  private final String[] custom10NotIn;

  /**
   * Filter by the custom10 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom10Like")
  private final String[] custom10Like;

  /** Filter by the value of the field custom11 of the task. This is an exact match. */
  @JsonProperty("custom11")
  private final String[] custom11;

  /** Filter out by values of the field custom11 of the task. This is an exact match. */
  @JsonProperty("custom11NotIn")
  private final String[] custom11NotIn;

  /**
   * Filter by the custom11 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom11Like")
  private final String[] custom11Like;

  /** Filter by the value of the field custom12 of the task. This is an exact match. */
  @JsonProperty("custom12")
  private final String[] custom12;

  /** Filter out by values of the field custom12 of the task. This is an exact match. */
  @JsonProperty("custom12NotIn")
  private final String[] custom12NotIn;

  /**
   * Filter by the custom12 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom12Like")
  private final String[] custom12Like;

  /** Filter by the value of the field custom13 of the task. This is an exact match. */
  @JsonProperty("custom13")
  private final String[] custom13;

  /** Filter out by values of the field custom13 of the task. This is an exact match. */
  @JsonProperty("custom13NotIn")
  private final String[] custom13NotIn;

  /**
   * Filter by the custom13 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom13Like")
  private final String[] custom13Like;

  /** Filter by the value of the field custom14 of the task. This is an exact match. */
  @JsonProperty("custom14")
  private final String[] custom14;

  /** Filter out by values of the field custom14 of the task. This is an exact match. */
  @JsonProperty("custom14NotIn")
  private final String[] custom14NotIn;

  /**
   * Filter by the custom14 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom14Like")
  private final String[] custom14Like;

  /** Filter by the value of the field custom15 of the task. This is an exact match. */
  @JsonProperty("custom15")
  private final String[] custom15;

  /** Filter out by values of the field custom15 of the task. This is an exact match. */
  @JsonProperty("custom15NotIn")
  private final String[] custom15NotIn;

  /**
   * Filter by the custom15 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom15Like")
  private final String[] custom15Like;
  /** Filter by the value of the field custom16 of the task. This is an exact match. */
  @JsonProperty("custom16")
  private final String[] custom16;
  /** Filter out by values of the field custom16 of the task. This is an exact match. */
  @JsonProperty("custom16NotIn")
  private final String[] custom16NotIn;
  /**
   * Filter by the custom16 field of the task. This results in a substring search (% is appended to
   * the front and end of the requested value). Further SQL "LIKE" wildcard characters will be
   * resolved correctly.
   */
  @JsonProperty("custom16Like")
  private final String[] custom16Like;

  @SuppressWarnings("indentation")
  @ConstructorProperties({
    "name",
    "nameLike",
    "priority",
    "state",
    "classificationId",
    "classificationKey",
    "classificationKeyLike",
    "classificationKeyNotIn",
    "isRead",
    "isTransferred",
    "objectReference",
    "callbackState",
    "attachmentClassificationKey",
    "attachmentClassificationKeyLike",
    "attachmentClassificationId",
    "attachmentClassificationIdLike",
    "attachmentChannel",
    "attachmentChannelLike",
    "attachmentReference",
    "attachmentReferenceLike",
    "attachmentReceived",
    "created",
    "createdFrom",
    "createdUntil",
    "claimed",
    "completed",
    "completedFrom",
    "completeUntil",
    "modified",
    "classificationCategory",
    "classificationCategoryLike",
    "classificationName",
    "classificationNameLike",
    "attachmentClassificationName",
    "attachmentClassificationNameLike",
    "parentBusinessProcessId",
    "parentBusinessProcessIdLike",
    "businessProcessId",
    "businessProcessIdLike",
    "taskId",
    "workbasketId",
    "workbasketKey",
    "domain",
    "owner",
    "ownerLike",
    "creator",
    "creatorLike",
    "noteLike",
    "porCompany",
    "porCompanyLike",
    "porSystem",
    "porSystemLike",
    "porInstance",
    "porInstanceLike",
    "porType",
    "porTypeLike",
    "porValue",
    "porValueLike",
    "planned",
    "plannedFrom",
    "plannedUntil",
    "received",
    "receivedFrom",
    "receivedUntil",
    "due",
    "dueFrom",
    "dueUntil",
    "wildcardSearchFields",
    "wildcardSearchValue",
    "externalId",
    "externalIdlike",
    "custom1",
    "custom1NotIn",
    "custom1Like",
    "custom2",
    "custom2NotIn",
    "custom2Like",
    "custom3",
    "custom3NotIn",
    "custom3Like",
    "custom4",
    "custom4NotIn",
    "custom4Like",
    "custom5",
    "custom5NotIn",
    "custom5Like",
    "custom6",
    "custom6NotIn",
    "custom6like",
    "custom7",
    "custom7NotIn",
    "custom7like",
    "custom8",
    "custom8NotIn",
    "custom8Like",
    "custom9",
    "custom9NotIn",
    "custom9Like",
    "custom10",
    "custom10NotIn",
    "custom10Like",
    "custom11",
    "custom11NotIn",
    "custom11Like",
    "custom12",
    "custom12NotIn",
    "custom12Like",
    "custom13",
    "custom13NotIn",
    "custom13Like",
    "custom14",
    "custom14NotIn",
    "custom14Like",
    "custom15",
    "custom15NotIn",
    "custom15Like",
    "custom16",
    "custom16NotIn",
    "custom16Like"
  })
  public TaskQueryFilterParameter(
      @GraphQLInputField(description = "Filter by the name of the task. This is an exact match.")
          String[] name,
      @GraphQLInputField(
              description =
                  "Filter by the name of the task. This results in a substring search (% is"
                      + " appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] nameLike,
      @GraphQLInputField(
              description = "Filter by the priority of the task. This is an exact match.")
          int[] priority,
      @GraphQLInputField(
              description =
                  "Filter by the task state. This is an exact match.\n"
                      + "\n"
                      + "Must be one of [READY, CLAIMED, COMPLETED, CANCELLED, TERMINATED].")
          TaskState[] state,
      @GraphQLInputField(
              description = "Filter by the classification id of the task. This is an exact match.")
          String[] classificationId,
      @GraphQLInputField(
              description = "Filter by the classification key of the task. This is an exact match.")
          String[] classificationKeys,
      @GraphQLInputField(
              description =
                  "Filter by the classification key of the task. This results in a substring search"
                      + " (% is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] classificationKeysLike,
      @GraphQLInputField(
              description = "Filter by the classification key of the task. This is an exact match.")
          String[] classificationKeysNotIn,
      @GraphQLInputField(
              description = "Filter by the is read flag of the task. This is an exact match.")
          Boolean isRead,
      @GraphQLInputField(
              description =
                  "Filter by the is transferred flag of the task. This is an exact match.")
          Boolean isTransferred,
      @GraphQLInputField(
              description =
                  "Filter by the primary object reference of the task. This is an exact match.")
          ObjectReference[] objectReferences,
      @GraphQLInputField(
              description =
                  "Filter by the callback state of the task. This is an exact match.\n"
                      + "\n"
                      + "Must be one of [NONE, CALLBACK_PROCESSING_REQUIRED, CLAIMED,"
                      + " CALLBACK_PROCESSING_COMPLETED].")
          CallbackState[] callbackStates,
      @GraphQLInputField(
              description =
                  "Filter by the attachment classification key of the task. This is an exact"
                      + " match.")
          String[] attachmentClassificationKeys,
      @GraphQLInputField(
              description =
                  "Filter by the attachment classification key of the task. This results in a"
                      + " substring search (% is appended to the front and end of the requested"
                      + " value). Further SQL \"LIKE\" wildcard characters will be resolved"
                      + " correctly.")
          String[] attachmentClassificationKeysLike,
      @GraphQLInputField(
              description =
                  "Filter by the attachment classification id of the task. This is an exact match.")
          String[] attachmentClassificationId,
      @GraphQLInputField(
              description =
                  "Filter by the attachment classification id of the task. This results in a"
                      + " substring search (% is appended to the front and end of the requested"
                      + " value). Further SQL \"LIKE\" wildcard characters will be resolved"
                      + " correctly.")
          String[] attachmentClassificationIdLike,
      @GraphQLInputField(
              description = "Filter by the attachment channel of the task. This is an exact match.")
          String[] attachmentChannel,
      @GraphQLInputField(
              description =
                  "Filter by the attachment channel of the task. This results in a substring search"
                      + " (% is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] attachmentChannelLike,
      @GraphQLInputField(
              description =
                  "Filter by the attachment reference of the task. This is an exact match.")
          String[] attachmentReference,
      @GraphQLInputField(
              description =
                  "Filter by the attachment reference of the task. This results in a substring"
                      + " search (% is appended to the front and end of the requested value)."
                      + " Further SQL \"LIKE\" wildcard characters will be resolved correctly.")
          String[] attachmentReferenceLike,
      @GraphQLInputField(
              description =
                  "Filter by a time interval within which the attachment of the task was received."
                      + " To create an open interval you can just leave it blank.\n"
                      + "\n"
                      + "The format is ISO-8601.")
          Instant[] attachmentReceived,
      @GraphQLInputField(
              description =
                  "Filter by a time interval within which the task was created. To create an open"
                      + " interval you can just leave it blank.\n"
                      + "\n"
                      + "The format is ISO-8601.")
          Instant[] created,
      @GraphQLInputField(
              description =
                  "Filter since a given created timestamp.\n"
                      + "\n"
                      + "The format is ISO-8601.\n"
                      + "\n"
                      + "This parameter can’t be used together with 'created'.")
          Instant createdFrom,
      @GraphQLInputField(
              description =
                  "Filter until a given created timestamp.\n"
                      + "\n"
                      + "The format is ISO-8601.\n"
                      + "\n"
                      + "This parameter can’t be used together with 'created'.")
          Instant createdUntil,
      @GraphQLInputField(
              description =
                  "Filter by a time interval within which the task was claimed. To create an open"
                      + " interval you can just leave it blank.\n"
                      + "\n"
                      + "The format is ISO-8601.")
          Instant[] claimed,
      @GraphQLInputField(
              description =
                  "Filter by a time interval within which the task was completed. To create an open"
                      + " interval you can just leave it blank.\n"
                      + "\n"
                      + "The format is ISO-8601.")
          Instant[] completed,
      @GraphQLInputField(
              description =
                  "Filter since a given completed timestamp.\n"
                      + "\n"
                      + "The format is ISO-8601.\n"
                      + "\n"
                      + "This parameter can’t be used together with 'completed'.")
          Instant completedFrom,
      @GraphQLInputField(
              description =
                  "Filter until a given completed timestamp.\n"
                      + "\n"
                      + "The format is ISO-8601.\n"
                      + "\n"
                      + "This parameter can’t be used together with 'completed'.")
          Instant completedUntil,
      @GraphQLInputField(
              description =
                  "Filter by a time interval within which the task was modified. To create an open"
                      + " interval you can just leave it blank.\n"
                      + "\n"
                      + "The format is ISO-8601.")
          Instant[] modified,
      @GraphQLInputField(
              description =
                  "Filter by the classification category of the task. This is an exact match.")
          String[] classificationCategories,
      @GraphQLInputField(
              description =
                  "Filter by the classification category of the task. This results in a substring"
                      + " search (% is appended to the front and end of the requested value)."
                      + " Further SQL \"LIKE\" wildcard characters will be resolved correctly.")
          String[] classificationCategoriesLike,
      @GraphQLInputField(
              description =
                  "Filter by the classification name of the task. This is an exact match.")
          String[] classificationNames,
      @GraphQLInputField(
              description =
                  "Filter by the classification name of the task. This results in a substring"
                      + " search (% is appended to the front and end of the requested value)."
                      + " Further SQL \"LIKE\" wildcard characters will be resolved correctly.")
          String[] classificationNamesLike,
      @GraphQLInputField(
              description =
                  "Filter by the attachment classification name of the task. This is an exact"
                      + " match.")
          String[] attachmentClassificationNames,
      @GraphQLInputField(
              description =
                  "Filter by the attachment classification name of the task. This results in a"
                      + " substring search (% is appended to the front and end of the requested"
                      + " value). Further SQL \"LIKE\" wildcard characters will be resolved"
                      + " correctly.")
          String[] attachmentClassificationNamesLike,
      @GraphQLInputField(
              description =
                  "Filter by the parent business process id of the task. This is an exact match.")
          String[] parentBusinessProcessIds,
      @GraphQLInputField(
              description =
                  "Filter by the parent business process id of the task. This results in a"
                      + " substring search (% is appended to the front and end of the requested"
                      + " value). Further SQL \"LIKE\" wildcard characters will be resolved"
                      + " correctly.")
          String[] parentBusinessProcessIdsLike,
      @GraphQLInputField(
              description =
                  "Filter by the business process id of the task. This is an exact match.")
          String[] businessProcessIds,
      @GraphQLInputField(
              description =
                  "Filter by the business process id of the task. This results in a substring"
                      + " search (% is appended to the front and end of the requested value)."
                      + " Further SQL \"LIKE\" wildcard characters will be resolved correctly.")
          String[] businessProcessIdsLike,
      @GraphQLInputField(description = "Filter by task id. This is an exact match.")
          String[] taskIds,
      @GraphQLInputField(
              description = "Filter by workbasket id of the task. This is an exact match.")
          String[] workbasketIds,
      @GraphQLInputField(
              description =
                  "Filter by workbasket keys of the task. This parameter can only be used in"
                      + " combination with 'domain'.")
          String[] workbasketKeys,
      @GraphQLInputField(description = "Filter by domain of the task. This is an exact match.")
          String domain,
      @GraphQLInputField(description = "Filter by owner of the task. This is an exact match.")
          String[] owner,
      @GraphQLInputField(
              description =
                  "Filter by the owner of the task. This results in a substring search (% is"
                      + " appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] ownerLike,
      @GraphQLInputField(description = "Filter by creator of the task. This is an exact match.")
          String[] creator,
      @GraphQLInputField(
              description =
                  "Filter by the creator of the task. This results in a substring search (% is"
                      + " appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] creatorLike,
      @GraphQLInputField(
              description =
                  "Filter by the note of the task. This results in a substring search (% is"
                      + " appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] noteLike,
      @GraphQLInputField(
              description =
                  "Filter by the company of the primary object reference of the task. This is an"
                      + " exact match.")
          String[] porCompany,
      @GraphQLInputField(
              description =
                  "Filter by the company of the primary object reference of the task. This results"
                      + " in a substring search (% is appended to the front and end of the"
                      + " requested value). Further SQL \"LIKE\" wildcard characters will be"
                      + " resolved correctly.")
          String[] porCompanyLike,
      @GraphQLInputField(
              description =
                  "Filter by the system of the primary object reference of the task. This is an"
                      + " exact match.")
          String[] porSystem,
      @GraphQLInputField(
              description =
                  "Filter by the he system of the primary object reference of the task. This"
                      + " results in a substring search (% is appended to the front and end of the"
                      + " requested value). Further SQL \"LIKE\" wildcard characters will be"
                      + " resolved correctly.")
          String[] porSystemLike,
      @GraphQLInputField(
              description =
                  "Filter by the system instance of the primary object reference of the task. This"
                      + " is an exact match.")
          String[] porInstance,
      @GraphQLInputField(
              description =
                  "Filter by the system instance of the primary object reference of the task. This"
                      + " results in a substring search (% is appended to the front and end of the"
                      + " requested value). Further SQL \"LIKE\" wildcard characters will be"
                      + " resolved correctly.")
          String[] porInstanceLike,
      @GraphQLInputField(
              description =
                  "Filter by the type of the primary object reference of the task. This is an exact"
                      + " match.")
          String[] porType,
      @GraphQLInputField(
              description =
                  "Filter by the type of the primary object reference of the task. This results in"
                      + " a substring search (% is appended to the front and end of the requested"
                      + " value). Further SQL \"LIKE\" wildcard characters will be resolved"
                      + " correctly.")
          String[] porTypeLike,
      @GraphQLInputField(
              description =
                  "Filter by the value of the primary object reference of the task. This is an"
                      + " exact match.")
          String[] porValue,
      @GraphQLInputField(
              description =
                  "Filter by the value of the primary object reference of the task. This results in"
                      + " a substring search (% is appended to the front and end of the requested"
                      + " value). Further SQL \"LIKE\" wildcard characters will be resolved"
                      + " correctly.")
          String[] porValueLike,
      @GraphQLInputField(
              description =
                  "Filter by a time interval within which the task was planned. To create an open"
                      + " interval you can just leave it blank.\n"
                      + "\n"
                      + "The format is ISO-8601.\n"
                      + "\n"
                      + "This parameter can’t be used together with 'planned-from' or"
                      + " 'planned-until'.")
          Instant[] planned,
      @GraphQLInputField(
              description =
                  "Filter since a given planned timestamp.\n"
                      + "\n"
                      + "The format is ISO-8601.\n"
                      + "\n"
                      + "This parameter can’t be used together with 'planned'.")
          Instant plannedFrom,
      @GraphQLInputField(
              description =
                  "Filter until a given planned timestamp.\n"
                      + "\n"
                      + "The format is ISO-8601.\n"
                      + "\n"
                      + "This parameter can’t be used together with 'planned'.")
          Instant plannedUntil,
      @GraphQLInputField(
              description =
                  "Filter by a time interval within which the task was received. To create an open"
                      + " interval you can just leave it blank.\n"
                      + "\n"
                      + "The format is ISO-8601.\n"
                      + "\n"
                      + "This parameter can’t be used together with 'received-from' or"
                      + " 'received-until'.")
          Instant[] received,
      @GraphQLInputField(
              description =
                  "Filter since a given received timestamp.\n"
                      + "\n"
                      + "The format is ISO-8601.\n"
                      + "\n"
                      + "This parameter can’t be used together with 'received'.")
          Instant receivedFrom,
      @GraphQLInputField(
              description =
                  "Filter until a given received timestamp.\n"
                      + "\n"
                      + "The format is ISO-8601.\n"
                      + "\n"
                      + "This parameter can’t be used together with 'received'.")
          Instant receivedUntil,
      @GraphQLInputField(
              description =
                  "Filter by a time interval within which the task was due. To create an open"
                      + " interval you can just leave it blank.\n"
                      + "\n"
                      + "The format is ISO-8601.\n"
                      + "\n"
                      + "This parameter can’t be used together with 'due-from' or 'due-until'.")
          Instant[] due,
      @GraphQLInputField(
              description =
                  "Filter since a given due timestamp.\n"
                      + "\n"
                      + "The format is ISO-8601.\n"
                      + "\n"
                      + "This parameter can’t be used together with 'due'.")
          Instant dueFrom,
      @GraphQLInputField(
              description =
                  "Filter until a given due timestamp.\n"
                      + "\n"
                      + "The format is ISO-8601.\n"
                      + "\n"
                      + "This parameter can’t be used together with 'due'.")
          Instant dueUntil,
      @GraphQLInputField(
              description =
                  "Filter by wildcard search field of the task.\n"
                      + "\n"
                      + "This must be used in combination with 'wildcard-search-value'.\n"
                      + "\n"
                      + "Must be one of [NAME, DESCRIPTION, CUSTOM_1, CUSTOM_2, CUSTOM_3, CUSTOM_4,"
                      + " CUSTOM_5, CUSTOM_6, CUSTOM_7, CUSTOM_8, CUSTOM_9, CUSTOM_10, CUSTOM_11,"
                      + " CUSTOM_12, CUSTOM_13, CUSTOM_14, CUSTOM_15, CUSTOM_16].")
          WildcardSearchField[] wildcardSearchFields,
      @GraphQLInputField(
              description =
                  "Filter by wildcard search field of the task. This is an exact match.\n"
                      + "\n"
                      + "This must be used in combination with 'wildcard-search-value'.")
          String wildcardSearchValue,
      @GraphQLInputField(
              description = "Filter by the external id of the task. This is an exact match.")
          String[] externalIds,
      @GraphQLInputField(
              description =
                  "Filter by the externalId of the task. This results in a substring search (% is"
                      + " appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] externalIdsLike,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom1 of the task. This is an exact match.")
          String[] custom1,
      @GraphQLInputField(description = "Exclude values of the field custom1 of the task.")
          String[] custom1NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom1 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom1Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom2 of the task. This is an exact match.")
          String[] custom2,
      @GraphQLInputField(description = "Exclude values of the field custom2 of the task.")
          String[] custom2NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom2 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom2Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom3 of the task. This is an exact match.")
          String[] custom3,
      @GraphQLInputField(description = "Exclude values of the field custom3 of the task.")
          String[] custom3NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom3 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom3Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom4 of the task. This is an exact match.")
          String[] custom4,
      @GraphQLInputField(description = "Exclude values of the field custom4 of the task.")
          String[] custom4NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom4 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom4Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom5 of the task. This is an exact match.")
          String[] custom5,
      @GraphQLInputField(description = "Exclude values of the field custom5 of the task.")
          String[] custom5NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom5 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom5Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom6 of the task. This is an exact match.")
          String[] custom6,
      @GraphQLInputField(description = "Exclude values of the field custom6 of the task.")
          String[] custom6NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom6 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom6Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom7 of the task. This is an exact match.")
          String[] custom7,
      @GraphQLInputField(description = "Exclude values of the field custom7 of the task.")
          String[] custom7NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom7 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom7Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom8 of the task. This is an exact match.")
          String[] custom8,
      @GraphQLInputField(description = "Exclude values of the field custom8 of the task.")
          String[] custom8NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom8 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom8Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom9 of the task. This is an exact match.")
          String[] custom9,
      @GraphQLInputField(description = "Exclude values of the field custom9 of the task.")
          String[] custom9NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom9 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom9Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom10 of the task. This is an exact match.")
          String[] custom10,
      @GraphQLInputField(description = "Exclude values of the field custom10 of the task.")
          String[] custom10NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom10 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom10Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom11 of the task. This is an exact match.")
          String[] custom11,
      @GraphQLInputField(description = "Exclude values of the field custom11 of the task.")
          String[] custom11NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom11 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom11Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom12 of the task. This is an exact match.")
          String[] custom12,
      @GraphQLInputField(description = "Exclude values of the field custom12 of the task.")
          String[] custom12NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom12 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom12Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom13 of the task. This is an exact match.")
          String[] custom13,
      @GraphQLInputField(description = "Exclude values of the field custom13 of the task.")
          String[] custom13NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom13 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom13Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom14 of the task. This is an exact match.")
          String[] custom14,
      @GraphQLInputField(description = "Exclude values of the field custom14 of the task.")
          String[] custom14NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom14 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom14Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom15 of the task. This is an exact match.")
          String[] custom15,
      @GraphQLInputField(description = "Exclude values of the field custom15 of the task.")
          String[] custom15NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom15 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom15Like,
      @GraphQLInputField(
              description =
                  "Filter by the value of the field custom16 of the task. This is an exact match.")
          String[] custom16,
      @GraphQLInputField(description = "Exclude values of the field custom16 of the task.")
          String[] custom16NotIn,
      @GraphQLInputField(
              description =
                  "Filter by the custom16 field of the task. This results in a substring search (%"
                      + " is appended to the front and end of the requested value). Further SQL"
                      + " \"LIKE\" wildcard characters will be resolved correctly.")
          String[] custom16Like)
      throws InvalidArgumentException {
    this.name = name;
    this.nameLike = nameLike;
    this.priority = priority;
    this.state = state;
    this.classificationId = classificationId;
    this.classificationKeys = classificationKeys;
    this.classificationKeysLike = classificationKeysLike;
    this.classificationKeysNotIn = classificationKeysNotIn;
    this.isRead = isRead;
    this.isTransferred = isTransferred;
    this.objectReferences = objectReferences;
    this.callbackStates = callbackStates;
    this.attachmentClassificationKeys = attachmentClassificationKeys;
    this.attachmentClassificationKeysLike = attachmentClassificationKeysLike;
    this.attachmentClassificationId = attachmentClassificationId;
    this.attachmentClassificationIdLike = attachmentClassificationIdLike;
    this.attachmentChannel = attachmentChannel;
    this.attachmentChannelLike = attachmentChannelLike;
    this.attachmentReference = attachmentReference;
    this.attachmentReferenceLike = attachmentReferenceLike;
    this.attachmentReceived = attachmentReceived;
    this.created = created;
    this.createdFrom = createdFrom;
    this.createdUntil = createdUntil;
    this.claimed = claimed;
    this.completed = completed;
    this.completedFrom = completedFrom;
    this.completedUntil = completedUntil;
    this.modified = modified;
    this.classificationCategories = classificationCategories;
    this.classificationCategoriesLike = classificationCategoriesLike;
    this.classificationNames = classificationNames;
    this.classificationNamesLike = classificationNamesLike;
    this.attachmentClassificationNames = attachmentClassificationNames;
    this.attachmentClassificationNamesLike = attachmentClassificationNamesLike;
    this.parentBusinessProcessIds = parentBusinessProcessIds;
    this.parentBusinessProcessIdsLike = parentBusinessProcessIdsLike;
    this.businessProcessIds = businessProcessIds;
    this.businessProcessIdsLike = businessProcessIdsLike;
    this.taskIds = taskIds;
    this.workbasketIds = workbasketIds;
    this.workbasketKeys = workbasketKeys;
    this.domain = domain;
    this.owner = owner;
    this.ownerLike = ownerLike;
    this.creator = creator;
    this.creatorLike = creatorLike;
    this.noteLike = noteLike;
    this.porCompany = porCompany;
    this.porCompanyLike = porCompanyLike;
    this.porSystem = porSystem;
    this.porSystemLike = porSystemLike;
    this.porInstance = porInstance;
    this.porInstanceLike = porInstanceLike;
    this.porType = porType;
    this.porTypeLike = porTypeLike;
    this.porValue = porValue;
    this.porValueLike = porValueLike;
    this.planned = planned;
    this.plannedFrom = plannedFrom;
    this.plannedUntil = plannedUntil;
    this.received = received;
    this.receivedFrom = receivedFrom;
    this.receivedUntil = receivedUntil;
    this.due = due;
    this.dueFrom = dueFrom;
    this.dueUntil = dueUntil;
    this.wildcardSearchFields = wildcardSearchFields;
    this.wildcardSearchValue = wildcardSearchValue;
    this.externalIds = externalIds;
    this.externalIdsLike = externalIdsLike;
    this.custom1 = custom1;
    this.custom1NotIn = custom1NotIn;
    this.custom1Like = custom1Like;
    this.custom2 = custom2;
    this.custom2NotIn = custom2NotIn;
    this.custom2Like = custom2Like;
    this.custom3 = custom3;
    this.custom3NotIn = custom3NotIn;
    this.custom3Like = custom3Like;
    this.custom4 = custom4;
    this.custom4NotIn = custom4NotIn;
    this.custom4Like = custom4Like;
    this.custom5 = custom5;
    this.custom5NotIn = custom5NotIn;
    this.custom5Like = custom5Like;
    this.custom6 = custom6;
    this.custom6NotIn = custom6NotIn;
    this.custom6Like = custom6Like;
    this.custom7 = custom7;
    this.custom7NotIn = custom7NotIn;
    this.custom7Like = custom7Like;
    this.custom8 = custom8;
    this.custom8NotIn = custom8NotIn;
    this.custom8Like = custom8Like;
    this.custom9 = custom9;
    this.custom9NotIn = custom9NotIn;
    this.custom9Like = custom9Like;
    this.custom10 = custom10;
    this.custom10NotIn = custom10NotIn;
    this.custom10Like = custom10Like;
    this.custom11 = custom11;
    this.custom11NotIn = custom11NotIn;
    this.custom11Like = custom11Like;
    this.custom12 = custom12;
    this.custom12NotIn = custom12NotIn;
    this.custom12Like = custom12Like;
    this.custom13 = custom13;
    this.custom13NotIn = custom13NotIn;
    this.custom13Like = custom13Like;
    this.custom14 = custom14;
    this.custom14NotIn = custom14NotIn;
    this.custom14Like = custom14Like;
    this.custom15 = custom15;
    this.custom15NotIn = custom15NotIn;
    this.custom15Like = custom15Like;
    this.custom16 = custom16;
    this.custom16NotIn = custom16NotIn;
    this.custom16Like = custom16Like;

    validateFilterParameters();
  }

  @Override
  public Void apply(TaskQuery query) {
    Optional.ofNullable(name).ifPresent(query::nameIn);
    Optional.ofNullable(nameLike).map(this::wrapElementsInLikeStatement).ifPresent(query::nameLike);
    Optional.ofNullable(priority).ifPresent(query::priorityIn);
    Optional.ofNullable(state).ifPresent(query::stateIn);
    Optional.ofNullable(classificationId).ifPresent(query::classificationIdIn);
    Optional.ofNullable(classificationKeys).ifPresent(query::classificationKeyIn);
    Optional.ofNullable(classificationKeysLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::classificationKeyLike);
    Optional.ofNullable(classificationKeysNotIn).ifPresent(query::classificationKeyNotIn);
    Optional.ofNullable(isRead).ifPresent(query::readEquals);
    Optional.ofNullable(isTransferred).ifPresent(query::transferredEquals);
    Optional.ofNullable(objectReferences).ifPresent(query::primaryObjectReferenceIn);
    Optional.ofNullable(callbackStates).ifPresent(query::callbackStateIn);
    Optional.ofNullable(attachmentClassificationKeys)
        .ifPresent(query::attachmentClassificationKeyIn);
    Optional.ofNullable(attachmentClassificationKeysLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::attachmentClassificationKeyLike);
    Optional.ofNullable(attachmentClassificationId).ifPresent(query::attachmentClassificationIdIn);
    Optional.ofNullable(attachmentClassificationIdLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::attachmentClassificationIdLike);
    Optional.ofNullable(attachmentChannel).ifPresent(query::attachmentChannelIn);
    Optional.ofNullable(attachmentChannelLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::attachmentChannelLike);
    Optional.ofNullable(attachmentReference).ifPresent(query::attachmentReferenceValueIn);
    Optional.ofNullable(attachmentReferenceLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::attachmentReferenceValueLike);
    Optional.ofNullable(attachmentReceived)
        .map(this::extractTimeIntervals)
        .ifPresent(query::attachmentReceivedWithin);
    Optional.ofNullable(created).map(this::extractTimeIntervals).ifPresent(query::createdWithin);
    if (createdFrom != null || createdUntil != null) {
      query.createdWithin(new TimeInterval(createdFrom, createdUntil));
    }
    Optional.ofNullable(claimed).map(this::extractTimeIntervals).ifPresent(query::claimedWithin);
    Optional.ofNullable(completed)
        .map(this::extractTimeIntervals)
        .ifPresent(query::completedWithin);
    if (completedFrom != null || completedUntil != null) {
      query.completedWithin(new TimeInterval(completedFrom, completedUntil));
    }
    Optional.ofNullable(modified).map(this::extractTimeIntervals).ifPresent(query::modifiedWithin);
    Optional.ofNullable(classificationCategories).ifPresent(query::classificationCategoryIn);
    Optional.ofNullable(classificationCategoriesLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::classificationCategoryLike);
    Optional.ofNullable(classificationNames).ifPresent(query::classificationNameIn);
    Optional.ofNullable(classificationNamesLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::classificationNameLike);
    Optional.ofNullable(attachmentClassificationNames)
        .ifPresent(query::attachmentClassificationNameIn);
    Optional.ofNullable(attachmentClassificationNamesLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::attachmentClassificationNameLike);
    Optional.ofNullable(parentBusinessProcessIds).ifPresent(query::parentBusinessProcessIdIn);
    Optional.ofNullable(parentBusinessProcessIdsLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::parentBusinessProcessIdLike);
    Optional.ofNullable(businessProcessIds).ifPresent(query::businessProcessIdIn);
    Optional.ofNullable(businessProcessIdsLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::businessProcessIdLike);
    Optional.ofNullable(taskIds).ifPresent(query::idIn);
    Optional.ofNullable(workbasketIds).ifPresent(query::workbasketIdIn);
    Optional.ofNullable(workbasketKeys)
        .map(
            keys ->
                Arrays.stream(keys)
                    .map(key -> new KeyDomain(key, domain))
                    .toArray(KeyDomain[]::new))
        .ifPresent(query::workbasketKeyDomainIn);
    Optional.ofNullable(owner).ifPresent(query::ownerIn);
    Optional.ofNullable(ownerLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::ownerLike);
    Optional.ofNullable(creator).ifPresent(query::creatorIn);
    Optional.ofNullable(creatorLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::creatorLike);
    Optional.ofNullable(noteLike).map(this::wrapElementsInLikeStatement).ifPresent(query::noteLike);
    Optional.ofNullable(porCompany).ifPresent(query::primaryObjectReferenceCompanyIn);
    Optional.ofNullable(porCompanyLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::primaryObjectReferenceCompanyLike);
    Optional.ofNullable(porSystem).ifPresent(query::primaryObjectReferenceSystemIn);
    Optional.ofNullable(porSystemLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::primaryObjectReferenceSystemLike);
    Optional.ofNullable(porInstance).ifPresent(query::primaryObjectReferenceSystemInstanceIn);
    Optional.ofNullable(porInstanceLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::primaryObjectReferenceSystemInstanceLike);
    Optional.ofNullable(porType).ifPresent(query::primaryObjectReferenceTypeIn);
    Optional.ofNullable(porTypeLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::primaryObjectReferenceTypeLike);
    Optional.ofNullable(porValue).ifPresent(query::primaryObjectReferenceValueIn);
    Optional.ofNullable(porValueLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::primaryObjectReferenceValueLike);
    Optional.ofNullable(planned).map(this::extractTimeIntervals).ifPresent(query::plannedWithin);
    if (plannedFrom != null || plannedUntil != null) {
      query.plannedWithin(new TimeInterval(plannedFrom, plannedUntil));
    }
    Optional.ofNullable(received).map(this::extractTimeIntervals).ifPresent(query::receivedWithin);
    if (receivedFrom != null || receivedUntil != null) {
      query.receivedWithin(new TimeInterval(receivedFrom, receivedUntil));
    }
    Optional.ofNullable(due).map(this::extractTimeIntervals).ifPresent(query::dueWithin);
    if (dueFrom != null || dueUntil != null) {
      query.dueWithin(new TimeInterval(dueFrom, dueUntil));
    }
    if (wildcardSearchFields != null) {
      query.wildcardSearchFieldsIn(wildcardSearchFields);
      query.wildcardSearchValueLike("%" + wildcardSearchValue + "%");
    }
    Optional.ofNullable(externalIds).ifPresent(query::externalIdIn);
    Optional.ofNullable(externalIdsLike)
        .map(this::wrapElementsInLikeStatement)
        .ifPresent(query::externalIdLike);
    Stream.of(
            Pair.of(TaskCustomField.CUSTOM_1, custom1),
            Pair.of(TaskCustomField.CUSTOM_2, custom2),
            Pair.of(TaskCustomField.CUSTOM_3, custom3),
            Pair.of(TaskCustomField.CUSTOM_4, custom4),
            Pair.of(TaskCustomField.CUSTOM_5, custom5),
            Pair.of(TaskCustomField.CUSTOM_6, custom6),
            Pair.of(TaskCustomField.CUSTOM_7, custom7),
            Pair.of(TaskCustomField.CUSTOM_8, custom8),
            Pair.of(TaskCustomField.CUSTOM_9, custom9),
            Pair.of(TaskCustomField.CUSTOM_10, custom10),
            Pair.of(TaskCustomField.CUSTOM_11, custom11),
            Pair.of(TaskCustomField.CUSTOM_12, custom12),
            Pair.of(TaskCustomField.CUSTOM_13, custom13),
            Pair.of(TaskCustomField.CUSTOM_14, custom14),
            Pair.of(TaskCustomField.CUSTOM_15, custom15),
            Pair.of(TaskCustomField.CUSTOM_16, custom16))
        .forEach(
            pair ->
                Optional.ofNullable(pair.getRight())
                    .ifPresent(wrap(l -> query.customAttributeIn(pair.getLeft(), l))));
    Stream.of(
            Pair.of(TaskCustomField.CUSTOM_1, custom1NotIn),
            Pair.of(TaskCustomField.CUSTOM_2, custom2NotIn),
            Pair.of(TaskCustomField.CUSTOM_3, custom3NotIn),
            Pair.of(TaskCustomField.CUSTOM_4, custom4NotIn),
            Pair.of(TaskCustomField.CUSTOM_5, custom5NotIn),
            Pair.of(TaskCustomField.CUSTOM_6, custom6NotIn),
            Pair.of(TaskCustomField.CUSTOM_7, custom7NotIn),
            Pair.of(TaskCustomField.CUSTOM_8, custom8NotIn),
            Pair.of(TaskCustomField.CUSTOM_9, custom9NotIn),
            Pair.of(TaskCustomField.CUSTOM_10, custom10NotIn),
            Pair.of(TaskCustomField.CUSTOM_11, custom11NotIn),
            Pair.of(TaskCustomField.CUSTOM_12, custom12NotIn),
            Pair.of(TaskCustomField.CUSTOM_13, custom13NotIn),
            Pair.of(TaskCustomField.CUSTOM_14, custom14NotIn),
            Pair.of(TaskCustomField.CUSTOM_15, custom15NotIn),
            Pair.of(TaskCustomField.CUSTOM_16, custom16NotIn))
        .forEach(
            pair ->
                Optional.ofNullable(pair.getRight())
                    .ifPresent(wrap(l -> query.customAttributeNotIn(pair.getLeft(), l))));
    Stream.of(
            Pair.of(TaskCustomField.CUSTOM_1, custom1Like),
            Pair.of(TaskCustomField.CUSTOM_2, custom2Like),
            Pair.of(TaskCustomField.CUSTOM_3, custom3Like),
            Pair.of(TaskCustomField.CUSTOM_4, custom4Like),
            Pair.of(TaskCustomField.CUSTOM_5, custom5Like),
            Pair.of(TaskCustomField.CUSTOM_6, custom6Like),
            Pair.of(TaskCustomField.CUSTOM_7, custom7Like),
            Pair.of(TaskCustomField.CUSTOM_8, custom8Like),
            Pair.of(TaskCustomField.CUSTOM_9, custom9Like),
            Pair.of(TaskCustomField.CUSTOM_10, custom10Like),
            Pair.of(TaskCustomField.CUSTOM_11, custom11Like),
            Pair.of(TaskCustomField.CUSTOM_12, custom12Like),
            Pair.of(TaskCustomField.CUSTOM_13, custom13Like),
            Pair.of(TaskCustomField.CUSTOM_14, custom14Like),
            Pair.of(TaskCustomField.CUSTOM_15, custom15Like),
            Pair.of(TaskCustomField.CUSTOM_16, custom16Like))
        .forEach(
            pair ->
                Optional.ofNullable(pair.getRight())
                    .map(this::wrapElementsInLikeStatement)
                    .ifPresent(wrap(l -> query.customAttributeLike(pair.getLeft(), l))));
    return null;
  }

  private void validateFilterParameters() throws InvalidArgumentException {
    if (planned != null && (plannedFrom != null || plannedUntil != null)) {
      throw new InvalidArgumentException(
          "It is prohibited to use the param 'planned' in combination "
              + "with the params 'planned-from'  and / or 'planned-until'");
    }

    if (received != null && (receivedFrom != null || receivedUntil != null)) {
      throw new InvalidArgumentException(
          "It is prohibited to use the param 'received' in combination "
              + "with the params 'received-from'  and / or 'received-until'");
    }

    if (due != null && (dueFrom != null || dueUntil != null)) {
      throw new InvalidArgumentException(
          "It is prohibited to use the param 'due' in combination with the params "
              + "'due-from'  and / or 'due-until'");
    }

    if (created != null && (createdFrom != null || createdUntil != null)) {
      throw new InvalidArgumentException(
          "It is prohibited to use the param 'created' in combination with the params "
              + "'created-from'  and / or 'created-until'");
    }

    if (completed != null && (completedFrom != null || completedUntil != null)) {
      throw new InvalidArgumentException(
          "It is prohibited to use the param 'completed' in combination with the params "
              + "'completed-from'  and / or 'completed-until'");
    }

    if (wildcardSearchFields == null ^ wildcardSearchValue == null) {
      throw new InvalidArgumentException(
          "The params 'wildcard-search-field' and 'wildcard-search-value' must be used together");
    }

    if (workbasketKeys != null && domain == null) {
      throw new InvalidArgumentException("'workbasket-key' requires exactly one domain.");
    }

    if (planned != null && planned.length % 2 != 0) {
      throw new InvalidArgumentException(
          "provided length of the property 'planned' is not dividable by 2");
    }

    if (received != null && received.length % 2 != 0) {
      throw new InvalidArgumentException(
          "provided length of the property 'received' is not dividable by 2");
    }

    if (due != null && due.length % 2 != 0) {
      throw new InvalidArgumentException(
          "provided length of the property 'due' is not dividable by 2");
    }

    if (modified != null && modified.length % 2 != 0) {
      throw new InvalidArgumentException(
          "provided length of the property 'modified' is not dividable by 2");
    }

    if (created != null && created.length % 2 != 0) {
      throw new InvalidArgumentException(
          "provided length of the property 'created' is not dividable by 2");
    }

    if (completed != null && completed.length % 2 != 0) {
      throw new InvalidArgumentException(
          "provided length of the property 'completed' is not dividable by 2");
    }

    if (claimed != null && claimed.length % 2 != 0) {
      throw new InvalidArgumentException(
          "provided length of the property 'claimed' is not dividable by 2");
    }

    if (attachmentReceived != null && attachmentReceived.length % 2 != 0) {
      throw new InvalidArgumentException(
          "provided length of the property 'attachmentReceived' is not dividable by 2");
    }
  }
}
