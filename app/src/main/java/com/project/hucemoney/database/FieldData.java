package com.project.hucemoney.database;

public class FieldData {

    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_GOALS = "goals";
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String TABLE_BUDGETS = "budgets";


    // Field names common to all tables
    public static final String FIELD_UUID = "UUID";
    public static final String FIELD_ID = "id";
    public static final String FIELD_LOCAL_ID = "localID";
    public static final String FIELD_CREATED_AT = "createdAt";
    public static final String FIELD_UPDATED_AT = "updatedAt";
    public static final String FIELD_DELETED_AT = "deletedAt";
    public static final String FIELD_IS_DELETED = "isDeleted";
    public static final String FIELD_IS_LOCKED = "isLocked";
    public static final String FIELD_UPDATED_BY = "updatedBy";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_DELETED_BY = "deletedBy";
    public static final String FIELD_IS_SYNCED = "isSynced";
    public static final String FIELD_IS_NEW = "isNew";
    public static final String FIELD_IS_UPDATED = "isUpdated";



    // Users table fields
    public static final String USER_FIELD_USERNAME = "username";
    public static final String USER_FIELD_EMAIL = "email";
    public static final String USER_FIELD_PASSWORD = "password";

    // Accounts table fields
    public static final String ACCOUNT_FIELD_NAME = "name";
    public static final String ACCOUNT_FIELD_AMOUNT = "amount";
    public static final String ACCOUNT_FIELD_NOTE = "note";
    public static final String ACCOUNT_FIELD_USER = "user";

    // Categories table fields
    public static final String CATEGORY_FIELD_USER = "user";
    public static final String CATEGORY_FIELD_NAME = "name";
    public static final String CATEGORY_FIELD_TYPE = "type";
    public static final String CATEGORY_FIELD_PARENT = "parent";
    public static final String CATEGORY_FIELD_NOTE = "note";

    // Goals table fields
    public static final String GOAL_FIELD_USER = "user";
    public static final String GOAL_FIELD_NAME = "name";
    public static final String GOAL_FIELD_TARGET_AMOUNT = "targetAmount";
    public static final String GOAL_FIELD_CURRENT_AMOUNT = "currentAmount";
    public static final String GOAL_FIELD_START_DATE = "startDate";
    public static final String GOAL_FIELD_END_DATE = "endDate";
    public static final String GOAL_FIELD_NOTE = "note";

    // Transaction table fields
    public static final String TRANSACTION_FIELD_ACCOUNT = "account";
    public static final String TRANSACTION_FIELD_NAME = "name";
    public static final String TRANSACTION_FIELD_DATE = "date";
    public static final String TRANSACTION_FIELD_TYPE = "type";
    public static final String TRANSACTION_FIELD_CATEGORY = "category";
    public static final String TRANSACTION_FIELD_AMOUNT = "amount";
    public static final String TRANSACTION_FIELD_NOTE = "note";


    // Budgets table fields
    public static final String BUDGET_FIELD_NAME = "name";
    public static final String BUDGET_FIELD_START_DATE = "startDate";
    public static final String BUDGET_FIELD_END_DATE = "endDate";
    public static final String BUDGET_FIELD_INITIAL_LIMIT = "initialLimit";
    public static final String BUDGET_FIELD_CURRENT_BALANCE = "currentBalance";
    public static final String BUDGET_FIELD_CATEGORY = "category";
    public static final String BUDGET_FIELD_NOTE = "note";

}
