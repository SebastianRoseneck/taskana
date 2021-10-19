export class TaskanaQueryParameters {
  static parameters = {
    // Sorting
    SORTBY: 'sortBy',
    SORTDIRECTION: 'order',
    // Filtering
    NAME: 'name',
    NAMELIKE: 'nameLike',
    DESCLIKE: 'descriptionLike',
    OWNER: 'owner',
    OWNERLIKE: 'ownerLike',
    TYPE: 'type',
    KEY: 'key',
    CREATED: 'created',
    WORKBASKET_KEY: 'workbasketKey',
    KEYLIKE: 'keyLike',
    PRIORITY: 'priority',
    STATE: 'state',
    WORKBASKET_ID: 'workbasket-id',
    TASK_PRIMARY_OBJ_REF_TYPE_LIKE: 'porType',
    TASK_PRIMARY_OBJ_REF_VALUE_LIKE: 'porValue',
    // Access
    REQUIREDPERMISSION: 'requiredPermission',
    ACCESSIDS: 'accessIds',
    ACCESSIDLIKE: 'accessIdLike',
    WORKBASKETKEYLIKE: 'workbasketKeyLike',
    // Pagination
    PAGE: 'page',
    PAGESIZE: 'pageSize',
    // Domain
    DOMAIN: 'domain',

    // Task history events
    TASK_ID_LIKE: 'task-id-like',
    PARENT_BUSINESS_PROCESS_ID_LIKE: 'parentBusinessProcessIdLike',
    BUSINESS_PROCESS_ID_LIKE: 'businessProcessIdLike',
    EVENT_TYPE_LIKE: 'eventTypeLike',
    CREATED_LIKE: 'createdLike',
    USER_ID_LIKE: 'userIdLike',
    POR_COMPANY_LIKE: 'porCompanyLike',
    POR_SYSTEM_LIKE: 'porSystemLike',
    POR_INSTANCE_LIKE: 'porInstance-Like',
    POR_TYPE_LIKE: 'porTypeLike',
    POR_VALUE_LIKE: 'porValueLike',
    TASK_CLASSIFICATION_KEY_LIKE: 'taskClassificationKeyLike',
    TASK_CLASSIFICATION_CATEGORY_LIKE: 'taskClassificationCategoryLike',
    ATTACHMENT_CLASSIFICATION_KEY_LIKE: 'attachmentClassificationKeyLike',
    CUSTOM_1_LIKE: 'custom1Like',
    CUSTOM_2_LIKE: 'custom2Like',
    CUSTOM_3_LIKE: 'custom3Like',
    CUSTOM_4_LIKE: 'custom4Like',
    COMMENT_LIKE: 'commentLike'
  };

  static page = 1;
  static pageSize = 9;
}
