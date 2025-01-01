package com.example.analytics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Request Type required for KDA app to process the event. If we remove the event as a whole then
 * only removal from this enum makes sense There should be an enum value for each requestType that
 * is sent to KDA app
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum RequestTypeEnum {
    // Queries
    GET_PLANS("getPlans", ApiEnum.GRAPH_QL),
    GET_PLAN("getPlan", ApiEnum.GRAPH_QL),
    GET_PLAN_VERSION("getPlanVersion", ApiEnum.GRAPH_QL),
    GET_RULES("getRules", ApiEnum.GRAPH_QL),
    GET_PLAN_SERVICES("getPlanServices", ApiEnum.GRAPH_QL),
    GET_ACCOUNT("getAccount", ApiEnum.GRAPH_QL),
    GET_DEVICE("getDevice", ApiEnum.GRAPH_QL),
    GET_RELATED_ACCOUNTS_BY_DEVICE("getRelatedAccountsByDevice", ApiEnum.GRAPH_QL),
    GET_EVENT_DATA_RECORDS_BY_ACCOUNT("getEventDataRecordsByAccount", ApiEnum.GRAPH_QL),
    GET_EVENT_DATA_RECORDS_BY_DEVICE("getEventDataRecordsByDevice", ApiEnum.GRAPH_QL),
    GET_SETTINGS("getSettings", ApiEnum.GRAPH_QL),
    GET_FIELD_MAPPINGS("getFieldMappings", ApiEnum.GRAPH_QL),
    GET_DEPLOYED_FIELD_MAPPINGS("getDeployedFieldMappings", ApiEnum.GRAPH_QL),
    GET_RATING_GROUP_HIERARCHY("getRatingGroupHierarchy", ApiEnum.GRAPH_QL),
    GET_USER("getUser", ApiEnum.GRAPH_QL),
    GET_CURRENT_USER("getCurrentUser", ApiEnum.GRAPH_QL),
    LIST_USERS("listUsers", ApiEnum.GRAPH_QL),
    GET_PROVIDER_CONFIG("getProviderConfig", ApiEnum.GRAPH_QL),
    GET_MY_PROVIDER_CONFIG("getMyProviderConfig", ApiEnum.GRAPH_QL),
    GET_MY_RESTORE_JOBS("getMyRestoreJobs", ApiEnum.GRAPH_QL),
    GET_RESTORE_JOBS("getRestoreJobs", ApiEnum.GRAPH_QL),
    GET_SGSN_TABLE("getSGSNTable", ApiEnum.GRAPH_QL),
    GET_VPC_ENDPOINT_CONNECTIONS("getVpcEndpointConnections", ApiEnum.GRAPH_QL),
    GET_MY_EXPORT_JOBS("getMyExportJobs", ApiEnum.GRAPH_QL),
    GET_EXPORT_JOBS("getExportJobs", ApiEnum.GRAPH_QL),
    GET_BULK_UPDATE_JOBS("getBulkUpdateJobs", ApiEnum.GRAPH_QL),
    GET_OFFERS("getOffers", ApiEnum.GRAPH_QL),
    GET_BULK_METADATA_JOB_STATUS("getBulkMetadataJobStatus", ApiEnum.GRAPH_QL),
    GET_ACCOUNT_COHORT("getAccountCohort", ApiEnum.GRAPH_QL),
    GET_ACCOUNT_COHORTS("getAccountCohorts", ApiEnum.GRAPH_QL),
    GET_CAMPAIGN("getCampaign", ApiEnum.GRAPH_QL),
    GET_CAMPAIGNS("getCampaigns", ApiEnum.GRAPH_QL),
    GET_CAMPAIGN_STATISTICS("getCampaignStatistics", ApiEnum.GRAPH_QL),
    GET_BALANCE_TYPE("getBalanceType", ApiEnum.GRAPH_QL),
    GET_BALANCE_TYPES("getBalanceTypes", ApiEnum.GRAPH_QL),
    GET_BALANCE_TYPE_COUNTERS("getBalanceTypeCounters", ApiEnum.GRAPH_QL),
    GET_ACCOUNT_BALANCE_TYPE_COUNTERS("getAccountBalanceTypeCounters", ApiEnum.GRAPH_QL),

    // Mutations
    CREATE_PLAN("createPlan", ApiEnum.GRAPH_QL),
    COPY_PLAN("copyPlan", ApiEnum.GRAPH_QL),
    UPDATE_PLAN("updatePlan", ApiEnum.GRAPH_QL),
    DEPLOY_PLAN("deployPlan", ApiEnum.GRAPH_QL),
    MAKE_PLAN_ASSIGNABLE("makePlanAssignable", ApiEnum.GRAPH_QL),
    MAKE_PLAN_NOT_ASSIGNABLE("makePlanNotAssignable", ApiEnum.GRAPH_QL),
    SUBSCRIBE_TO_PLAN("subscribeToPlan", ApiEnum.GRAPH_QL),
    UPDATE_PLAN_SUBSCRIPTION("updatePlanSubscription", ApiEnum.GRAPH_QL),
    CANCEL_PLAN_SUBSCRIPTION("cancelPlanSubscription", ApiEnum.GRAPH_QL),
    DELETE_PLAN("deletePlan", ApiEnum.GRAPH_QL),
    CREATE_PLAN_SERVICE("createPlanService", ApiEnum.GRAPH_QL),
    UPDATE_PLAN_SERVICE("updatePlanService", ApiEnum.GRAPH_QL),
    COPY_PLAN_SERVICE("copyPlanService", ApiEnum.GRAPH_QL),
    DELETE_PLAN_SERVICE("deletePlanService", ApiEnum.GRAPH_QL),
    CREATE_ACCOUNT("createAccount", ApiEnum.GRAPH_QL),
    UPDATE_ACCOUNT("updateAccount", ApiEnum.GRAPH_QL),
    DELETE_ACCOUNT("deleteAccount", ApiEnum.GRAPH_QL),
    CREATE_DEVICE("createDevice", ApiEnum.GRAPH_QL),
    UPDATE_DEVICE("updateDevice", ApiEnum.GRAPH_QL),
    DELETE_DEVICE("deleteDevice", ApiEnum.GRAPH_QL),
    CREATE_PLAN_FROM_INITIAL_TEMPLATE("createPlanFromInitialTemplate", ApiEnum.GRAPH_QL),
    CREATE_PLAN_FROM_INITIAL_RECURRING_FIRST_USAGE_TEMPLATE(
            "createPlanFromInitialRecurringFirstUsageTemplate", ApiEnum.GRAPH_QL),
    UPDATE_PLAN_VERSION_FROM_INITIAL_TEMPLATE(
            "updatePlanVersionFromInitialTemplate", ApiEnum.GRAPH_QL),
    UPDATE_PLAN_VERSION_FROM_INITIAL_RECURRING_FIRST_USAGE_TEMPLATE(
            "updatePlanVersionFromInitialRecurringFirstUsageTemplate", ApiEnum.GRAPH_QL),
    CREATE_PLAN_VERSION_FROM_INITIAL_TEMPLATE(
            "createPlanVersionFromInitialTemplate", ApiEnum.GRAPH_QL),
    CREATE_PLAN_VERSION_FROM_INITIAL_RECURRING_FIRST_USAGE_TEMPLATE(
            "createPlanVersionFromInitialRecurringFirstUsageTemplate", ApiEnum.GRAPH_QL),
    DELETE_PLAN_VERSION("deletePlanVersion", ApiEnum.GRAPH_QL),
    ARCHIVE_PLAN_VERSION("archivePlanVersion", ApiEnum.GRAPH_QL),
    UPDATE_SETTINGS("updateSettings", ApiEnum.GRAPH_QL),
    UPDATE_RATING_GROUP_HIERARCHY("updateRatingGroupHierarchy", ApiEnum.GRAPH_QL),
    FORCE_UPDATE_RATING_GROUP_HIERARCHY("forceUpdateRatingGroupHierarchy", ApiEnum.GRAPH_QL),
    UPDATE_SGSN_TABLE("updateSGSNTable", ApiEnum.GRAPH_QL),
    START_LOW_LEVEL_LOGGING("startLowLevelLogging", ApiEnum.GRAPH_QL),
    STOP_LOW_LEVEL_LOGGING("stopLowLevelLogging", ApiEnum.GRAPH_QL),
    // TODO: These should be deleted, but doesn't work
    CREDIT_ACCOUNT("creditAccount", ApiEnum.GRAPH_QL),
    DEBIT_ACCOUNT("debitAccount", ApiEnum.GRAPH_QL),
    UPDATE_DEBIT("updateDebit", ApiEnum.GRAPH_QL),
    // TODO: These should be deleted, but doesn't work
    UPDATE_ARCHIVING_POLICY("updateArchivingPolicy", ApiEnum.GRAPH_QL),
    CREATE_FIELD_MAPPING("createFieldMapping", ApiEnum.GRAPH_QL),
    UPDATE_FIELD_MAPPING("updateFieldMapping", ApiEnum.GRAPH_QL),
    DELETE_FIELD_MAPPING("deleteFieldMapping", ApiEnum.GRAPH_QL),
    DEPLOY_FIELD_MAPPINGS("deployFieldMappings", ApiEnum.GRAPH_QL),
    DELETE_USER("deleteUser", ApiEnum.GRAPH_QL),
    CREATE_USER("createUser", ApiEnum.GRAPH_QL),
    UPDATE_USER_PROFILE("updateUserProfile", ApiEnum.GRAPH_QL),
    UPDATE_USER("updateUser", ApiEnum.GRAPH_QL),
    RESET_USER_PASSWORD("resetUserPassword", ApiEnum.GRAPH_QL),
    CREATE_PROVIDER("createProvider", ApiEnum.GRAPH_QL),
    UPDATE_PROVIDER_CONFIG("updateProviderConfig", ApiEnum.GRAPH_QL),
    UPDATE_MY_PROVIDER_CONFIG("updateMyProviderConfig", ApiEnum.GRAPH_QL),
    TRANSITION_PROVIDER_LIFECYCLE_STAGE("transitionProviderLifecycleStage", ApiEnum.GRAPH_QL),
    DELETE_PROVIDER("deleteProvider", ApiEnum.GRAPH_QL),
    DELETE_PROVIDERS("deleteProviders", ApiEnum.GRAPH_QL),
    RESTORE_DATA("restoreData", ApiEnum.GRAPH_QL),
    SET_API_USAGE_TUMBLING_WINDOW("setApiUsageTumblingWindow", ApiEnum.GRAPH_QL),
    SET_API_USAGE_NOTIFICATION_WINDOW("setApiUsageNotificationWindow", ApiEnum.GRAPH_QL),
    ADD_WHITELIST_IP("addWhitelistIp", ApiEnum.GRAPH_QL),
    REMOVE_WHITELIST_IP("removeWhitelistIp", ApiEnum.GRAPH_QL),
    ADD_BLACKLIST_IP("addBlacklistIp", ApiEnum.GRAPH_QL),
    REMOVE_BLACKLIST_IP("removeBlacklistIp", ApiEnum.GRAPH_QL),
    UPDATE_CHURN_SCORES("updateChurnScores", ApiEnum.GRAPH_QL),
    ADD_SUBTENANT("addSubtenant", ApiEnum.GRAPH_QL),
    REMOVE_SUBTENANT("removeSubtenant", ApiEnum.GRAPH_QL),
    ACCEPT_VPC_ENDPOINT_CONNECTION("acceptVpcEndpointConnection", ApiEnum.GRAPH_QL),
    REJECT_VPC_ENDPOINT_CONNECTION("rejectVpcEndpointConnection", ApiEnum.GRAPH_QL),
    CANCEL_VPC_ENDPOINT_CONNECTION("cancelVpcEndpointConnection", ApiEnum.GRAPH_QL),
    EXPORT_DATA("exportData", ApiEnum.GRAPH_QL),
    BULK_UPDATE("bulkUpdate", ApiEnum.GRAPH_QL),
    TRANSITION_BULK_UPDATE_STATUS("transitionBulkUpdateStatus", ApiEnum.GRAPH_QL),
    CREATE_OFFER("createOffer", ApiEnum.GRAPH_QL),
    UPDATE_OFFER("updateOffer", ApiEnum.GRAPH_QL),
    DELETE_OFFER("deleteOffer", ApiEnum.GRAPH_QL),
    UPLOAD_METADATA("uploadMetadata", ApiEnum.GRAPH_QL),
    BULK_UPLOAD_METADATA("bulkUploadMetadata", ApiEnum.GRAPH_QL),
    CREATE_ACCOUNT_COHORT("createAccountCohort", ApiEnum.GRAPH_QL),
    UPDATE_ACCOUNT_COHORT("updateAccountCohort", ApiEnum.GRAPH_QL),
    DELETE_ACCOUNT_COHORT("deleteAccountCohort", ApiEnum.GRAPH_QL),
    CREATE_CAMPAIGN("createCampaign", ApiEnum.GRAPH_QL),
    UPDATE_CAMPAIGN("updateCampaign", ApiEnum.GRAPH_QL),
    DELETE_CAMPAIGN("deleteCampaign", ApiEnum.GRAPH_QL),
    CREATE_BALANCE("createBalance", ApiEnum.GRAPH_QL),
    UPDATE_BALANCE("updateBalance", ApiEnum.GRAPH_QL),
    DELETE_BALANCE("deleteBalance", ApiEnum.GRAPH_QL),
    CREATE_BALANCE_TYPE("createBalanceType", ApiEnum.GRAPH_QL),
    UPDATE_BALANCE_TYPE("updateBalanceType", ApiEnum.GRAPH_QL),
    DELETE_BALANCE_TYPE("deleteBalanceType", ApiEnum.GRAPH_QL),
    UPDATE_BALANCE_TYPE_COUNTER("updateBalanceTypeCounter", ApiEnum.GRAPH_QL),
    DELETE_BALANCE_TYPE_COUNTER("deleteBalanceTypeCounter", ApiEnum.GRAPH_QL),
    UPDATE_ACCOUNT_BALANCE_TYPE_COUNTER("updateAccountBalanceTypeCounter", ApiEnum.GRAPH_QL),
    DELETE_ACCOUNT_BALANCE_TYPE_COUNTER("deleteAccountBalanceTypeCounter", ApiEnum.GRAPH_QL),
    TRIGGER_RAR("triggerRar", ApiEnum.GRAPH_QL),

    // Charge
    INITIAL("Initial", ApiEnum.CHARGE),
    UPDATE("Update", ApiEnum.CHARGE),
    FINAL("Final", ApiEnum.CHARGE),

    // Spending Limit Control
    SPENDING_LIMIT_SUBSCRIBE("SpendingLimitSubscribe", ApiEnum.SPENDING_LIMIT),
    SPENDING_LIMIT_MODIFY_SUBSCRIPTION("SpendingLimitModifySubscription", ApiEnum.SPENDING_LIMIT),
    SPENDING_LIMIT_DELETE_SUBSCRIPTION("SpendingLimitDeleteSubscription", ApiEnum.SPENDING_LIMIT),

    // Lifecycles
    CREATE_LIFECYCLE("createLifecycle", ApiEnum.GRAPH_QL),
    UPDATE_LIFECYCLE("updateLifecycle", ApiEnum.GRAPH_QL),
    DELETE_LIFECYCLE("deleteLifecycle", ApiEnum.GRAPH_QL),
    GET_LIFECYCLES("getLifecycles", ApiEnum.GRAPH_QL),
    GET_LIFECYCLE("getLifecycle", ApiEnum.GRAPH_QL);

    private final String value;
    @Getter private final ApiEnum api;

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Create from value.
     *
     * @param value the string value.
     * @return {@link RequestTypeEnum}.
     */
    @JsonCreator
    public static RequestTypeEnum fromValue(final String value) {
        for (final RequestTypeEnum reason : RequestTypeEnum.values()) {
            if (reason.value.equals(value)) {
                return reason;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
