package io.rackshift.mybatis.domain;

import java.util.ArrayList;
import java.util.List;

public class BareMetalRuleExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BareMetalRuleExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andStartIpIsNull() {
            addCriterion("start_ip is null");
            return (Criteria) this;
        }

        public Criteria andStartIpIsNotNull() {
            addCriterion("start_ip is not null");
            return (Criteria) this;
        }

        public Criteria andStartIpEqualTo(String value) {
            addCriterion("start_ip =", value, "startIp");
            return (Criteria) this;
        }

        public Criteria andStartIpNotEqualTo(String value) {
            addCriterion("start_ip <>", value, "startIp");
            return (Criteria) this;
        }

        public Criteria andStartIpGreaterThan(String value) {
            addCriterion("start_ip >", value, "startIp");
            return (Criteria) this;
        }

        public Criteria andStartIpGreaterThanOrEqualTo(String value) {
            addCriterion("start_ip >=", value, "startIp");
            return (Criteria) this;
        }

        public Criteria andStartIpLessThan(String value) {
            addCriterion("start_ip <", value, "startIp");
            return (Criteria) this;
        }

        public Criteria andStartIpLessThanOrEqualTo(String value) {
            addCriterion("start_ip <=", value, "startIp");
            return (Criteria) this;
        }

        public Criteria andStartIpLike(String value) {
            addCriterion("start_ip like", value, "startIp");
            return (Criteria) this;
        }

        public Criteria andStartIpNotLike(String value) {
            addCriterion("start_ip not like", value, "startIp");
            return (Criteria) this;
        }

        public Criteria andStartIpIn(List<String> values) {
            addCriterion("start_ip in", values, "startIp");
            return (Criteria) this;
        }

        public Criteria andStartIpNotIn(List<String> values) {
            addCriterion("start_ip not in", values, "startIp");
            return (Criteria) this;
        }

        public Criteria andStartIpBetween(String value1, String value2) {
            addCriterion("start_ip between", value1, value2, "startIp");
            return (Criteria) this;
        }

        public Criteria andStartIpNotBetween(String value1, String value2) {
            addCriterion("start_ip not between", value1, value2, "startIp");
            return (Criteria) this;
        }

        public Criteria andEndIpIsNull() {
            addCriterion("end_ip is null");
            return (Criteria) this;
        }

        public Criteria andEndIpIsNotNull() {
            addCriterion("end_ip is not null");
            return (Criteria) this;
        }

        public Criteria andEndIpEqualTo(String value) {
            addCriterion("end_ip =", value, "endIp");
            return (Criteria) this;
        }

        public Criteria andEndIpNotEqualTo(String value) {
            addCriterion("end_ip <>", value, "endIp");
            return (Criteria) this;
        }

        public Criteria andEndIpGreaterThan(String value) {
            addCriterion("end_ip >", value, "endIp");
            return (Criteria) this;
        }

        public Criteria andEndIpGreaterThanOrEqualTo(String value) {
            addCriterion("end_ip >=", value, "endIp");
            return (Criteria) this;
        }

        public Criteria andEndIpLessThan(String value) {
            addCriterion("end_ip <", value, "endIp");
            return (Criteria) this;
        }

        public Criteria andEndIpLessThanOrEqualTo(String value) {
            addCriterion("end_ip <=", value, "endIp");
            return (Criteria) this;
        }

        public Criteria andEndIpLike(String value) {
            addCriterion("end_ip like", value, "endIp");
            return (Criteria) this;
        }

        public Criteria andEndIpNotLike(String value) {
            addCriterion("end_ip not like", value, "endIp");
            return (Criteria) this;
        }

        public Criteria andEndIpIn(List<String> values) {
            addCriterion("end_ip in", values, "endIp");
            return (Criteria) this;
        }

        public Criteria andEndIpNotIn(List<String> values) {
            addCriterion("end_ip not in", values, "endIp");
            return (Criteria) this;
        }

        public Criteria andEndIpBetween(String value1, String value2) {
            addCriterion("end_ip between", value1, value2, "endIp");
            return (Criteria) this;
        }

        public Criteria andEndIpNotBetween(String value1, String value2) {
            addCriterion("end_ip not between", value1, value2, "endIp");
            return (Criteria) this;
        }

        public Criteria andMaskIsNull() {
            addCriterion("mask is null");
            return (Criteria) this;
        }

        public Criteria andMaskIsNotNull() {
            addCriterion("mask is not null");
            return (Criteria) this;
        }

        public Criteria andMaskEqualTo(String value) {
            addCriterion("mask =", value, "mask");
            return (Criteria) this;
        }

        public Criteria andMaskNotEqualTo(String value) {
            addCriterion("mask <>", value, "mask");
            return (Criteria) this;
        }

        public Criteria andMaskGreaterThan(String value) {
            addCriterion("mask >", value, "mask");
            return (Criteria) this;
        }

        public Criteria andMaskGreaterThanOrEqualTo(String value) {
            addCriterion("mask >=", value, "mask");
            return (Criteria) this;
        }

        public Criteria andMaskLessThan(String value) {
            addCriterion("mask <", value, "mask");
            return (Criteria) this;
        }

        public Criteria andMaskLessThanOrEqualTo(String value) {
            addCriterion("mask <=", value, "mask");
            return (Criteria) this;
        }

        public Criteria andMaskLike(String value) {
            addCriterion("mask like", value, "mask");
            return (Criteria) this;
        }

        public Criteria andMaskNotLike(String value) {
            addCriterion("mask not like", value, "mask");
            return (Criteria) this;
        }

        public Criteria andMaskIn(List<String> values) {
            addCriterion("mask in", values, "mask");
            return (Criteria) this;
        }

        public Criteria andMaskNotIn(List<String> values) {
            addCriterion("mask not in", values, "mask");
            return (Criteria) this;
        }

        public Criteria andMaskBetween(String value1, String value2) {
            addCriterion("mask between", value1, value2, "mask");
            return (Criteria) this;
        }

        public Criteria andMaskNotBetween(String value1, String value2) {
            addCriterion("mask not between", value1, value2, "mask");
            return (Criteria) this;
        }

        public Criteria andProviderIdIsNull() {
            addCriterion("provider_id is null");
            return (Criteria) this;
        }

        public Criteria andProviderIdIsNotNull() {
            addCriterion("provider_id is not null");
            return (Criteria) this;
        }

        public Criteria andProviderIdEqualTo(String value) {
            addCriterion("provider_id =", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdNotEqualTo(String value) {
            addCriterion("provider_id <>", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdGreaterThan(String value) {
            addCriterion("provider_id >", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdGreaterThanOrEqualTo(String value) {
            addCriterion("provider_id >=", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdLessThan(String value) {
            addCriterion("provider_id <", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdLessThanOrEqualTo(String value) {
            addCriterion("provider_id <=", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdLike(String value) {
            addCriterion("provider_id like", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdNotLike(String value) {
            addCriterion("provider_id not like", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdIn(List<String> values) {
            addCriterion("provider_id in", values, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdNotIn(List<String> values) {
            addCriterion("provider_id not in", values, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdBetween(String value1, String value2) {
            addCriterion("provider_id between", value1, value2, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdNotBetween(String value1, String value2) {
            addCriterion("provider_id not between", value1, value2, "providerId");
            return (Criteria) this;
        }

        public Criteria andSyncStatusIsNull() {
            addCriterion("sync_status is null");
            return (Criteria) this;
        }

        public Criteria andSyncStatusIsNotNull() {
            addCriterion("sync_status is not null");
            return (Criteria) this;
        }

        public Criteria andSyncStatusEqualTo(String value) {
            addCriterion("sync_status =", value, "syncStatus");
            return (Criteria) this;
        }

        public Criteria andSyncStatusNotEqualTo(String value) {
            addCriterion("sync_status <>", value, "syncStatus");
            return (Criteria) this;
        }

        public Criteria andSyncStatusGreaterThan(String value) {
            addCriterion("sync_status >", value, "syncStatus");
            return (Criteria) this;
        }

        public Criteria andSyncStatusGreaterThanOrEqualTo(String value) {
            addCriterion("sync_status >=", value, "syncStatus");
            return (Criteria) this;
        }

        public Criteria andSyncStatusLessThan(String value) {
            addCriterion("sync_status <", value, "syncStatus");
            return (Criteria) this;
        }

        public Criteria andSyncStatusLessThanOrEqualTo(String value) {
            addCriterion("sync_status <=", value, "syncStatus");
            return (Criteria) this;
        }

        public Criteria andSyncStatusLike(String value) {
            addCriterion("sync_status like", value, "syncStatus");
            return (Criteria) this;
        }

        public Criteria andSyncStatusNotLike(String value) {
            addCriterion("sync_status not like", value, "syncStatus");
            return (Criteria) this;
        }

        public Criteria andSyncStatusIn(List<String> values) {
            addCriterion("sync_status in", values, "syncStatus");
            return (Criteria) this;
        }

        public Criteria andSyncStatusNotIn(List<String> values) {
            addCriterion("sync_status not in", values, "syncStatus");
            return (Criteria) this;
        }

        public Criteria andSyncStatusBetween(String value1, String value2) {
            addCriterion("sync_status between", value1, value2, "syncStatus");
            return (Criteria) this;
        }

        public Criteria andSyncStatusNotBetween(String value1, String value2) {
            addCriterion("sync_status not between", value1, value2, "syncStatus");
            return (Criteria) this;
        }

        public Criteria andLastSyncTimestampIsNull() {
            addCriterion("last_sync_timestamp is null");
            return (Criteria) this;
        }

        public Criteria andLastSyncTimestampIsNotNull() {
            addCriterion("last_sync_timestamp is not null");
            return (Criteria) this;
        }

        public Criteria andLastSyncTimestampEqualTo(Long value) {
            addCriterion("last_sync_timestamp =", value, "lastSyncTimestamp");
            return (Criteria) this;
        }

        public Criteria andLastSyncTimestampNotEqualTo(Long value) {
            addCriterion("last_sync_timestamp <>", value, "lastSyncTimestamp");
            return (Criteria) this;
        }

        public Criteria andLastSyncTimestampGreaterThan(Long value) {
            addCriterion("last_sync_timestamp >", value, "lastSyncTimestamp");
            return (Criteria) this;
        }

        public Criteria andLastSyncTimestampGreaterThanOrEqualTo(Long value) {
            addCriterion("last_sync_timestamp >=", value, "lastSyncTimestamp");
            return (Criteria) this;
        }

        public Criteria andLastSyncTimestampLessThan(Long value) {
            addCriterion("last_sync_timestamp <", value, "lastSyncTimestamp");
            return (Criteria) this;
        }

        public Criteria andLastSyncTimestampLessThanOrEqualTo(Long value) {
            addCriterion("last_sync_timestamp <=", value, "lastSyncTimestamp");
            return (Criteria) this;
        }

        public Criteria andLastSyncTimestampIn(List<Long> values) {
            addCriterion("last_sync_timestamp in", values, "lastSyncTimestamp");
            return (Criteria) this;
        }

        public Criteria andLastSyncTimestampNotIn(List<Long> values) {
            addCriterion("last_sync_timestamp not in", values, "lastSyncTimestamp");
            return (Criteria) this;
        }

        public Criteria andLastSyncTimestampBetween(Long value1, Long value2) {
            addCriterion("last_sync_timestamp between", value1, value2, "lastSyncTimestamp");
            return (Criteria) this;
        }

        public Criteria andLastSyncTimestampNotBetween(Long value1, Long value2) {
            addCriterion("last_sync_timestamp not between", value1, value2, "lastSyncTimestamp");
            return (Criteria) this;
        }

        public Criteria andConfigIsNull() {
            addCriterion("config is null");
            return (Criteria) this;
        }

        public Criteria andConfigIsNotNull() {
            addCriterion("config is not null");
            return (Criteria) this;
        }

        public Criteria andConfigEqualTo(Boolean value) {
            addCriterion("config =", value, "config");
            return (Criteria) this;
        }

        public Criteria andConfigNotEqualTo(Boolean value) {
            addCriterion("config <>", value, "config");
            return (Criteria) this;
        }

        public Criteria andConfigGreaterThan(Boolean value) {
            addCriterion("config >", value, "config");
            return (Criteria) this;
        }

        public Criteria andConfigGreaterThanOrEqualTo(Boolean value) {
            addCriterion("config >=", value, "config");
            return (Criteria) this;
        }

        public Criteria andConfigLessThan(Boolean value) {
            addCriterion("config <", value, "config");
            return (Criteria) this;
        }

        public Criteria andConfigLessThanOrEqualTo(Boolean value) {
            addCriterion("config <=", value, "config");
            return (Criteria) this;
        }

        public Criteria andConfigIn(List<Boolean> values) {
            addCriterion("config in", values, "config");
            return (Criteria) this;
        }

        public Criteria andConfigNotIn(List<Boolean> values) {
            addCriterion("config not in", values, "config");
            return (Criteria) this;
        }

        public Criteria andConfigBetween(Boolean value1, Boolean value2) {
            addCriterion("config between", value1, value2, "config");
            return (Criteria) this;
        }

        public Criteria andConfigNotBetween(Boolean value1, Boolean value2) {
            addCriterion("config not between", value1, value2, "config");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}