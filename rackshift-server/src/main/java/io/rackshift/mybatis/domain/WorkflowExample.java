package io.rackshift.mybatis.domain;

import java.util.ArrayList;
import java.util.List;

public class WorkflowExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public WorkflowExample() {
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

        public Criteria andInjectableNameIsNull() {
            addCriterion("injectable_name is null");
            return (Criteria) this;
        }

        public Criteria andInjectableNameIsNotNull() {
            addCriterion("injectable_name is not null");
            return (Criteria) this;
        }

        public Criteria andInjectableNameEqualTo(String value) {
            addCriterion("injectable_name =", value, "injectableName");
            return (Criteria) this;
        }

        public Criteria andInjectableNameNotEqualTo(String value) {
            addCriterion("injectable_name <>", value, "injectableName");
            return (Criteria) this;
        }

        public Criteria andInjectableNameGreaterThan(String value) {
            addCriterion("injectable_name >", value, "injectableName");
            return (Criteria) this;
        }

        public Criteria andInjectableNameGreaterThanOrEqualTo(String value) {
            addCriterion("injectable_name >=", value, "injectableName");
            return (Criteria) this;
        }

        public Criteria andInjectableNameLessThan(String value) {
            addCriterion("injectable_name <", value, "injectableName");
            return (Criteria) this;
        }

        public Criteria andInjectableNameLessThanOrEqualTo(String value) {
            addCriterion("injectable_name <=", value, "injectableName");
            return (Criteria) this;
        }

        public Criteria andInjectableNameLike(String value) {
            addCriterion("injectable_name like", value, "injectableName");
            return (Criteria) this;
        }

        public Criteria andInjectableNameNotLike(String value) {
            addCriterion("injectable_name not like", value, "injectableName");
            return (Criteria) this;
        }

        public Criteria andInjectableNameIn(List<String> values) {
            addCriterion("injectable_name in", values, "injectableName");
            return (Criteria) this;
        }

        public Criteria andInjectableNameNotIn(List<String> values) {
            addCriterion("injectable_name not in", values, "injectableName");
            return (Criteria) this;
        }

        public Criteria andInjectableNameBetween(String value1, String value2) {
            addCriterion("injectable_name between", value1, value2, "injectableName");
            return (Criteria) this;
        }

        public Criteria andInjectableNameNotBetween(String value1, String value2) {
            addCriterion("injectable_name not between", value1, value2, "injectableName");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameIsNull() {
            addCriterion("friendly_name is null");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameIsNotNull() {
            addCriterion("friendly_name is not null");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameEqualTo(String value) {
            addCriterion("friendly_name =", value, "friendlyName");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameNotEqualTo(String value) {
            addCriterion("friendly_name <>", value, "friendlyName");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameGreaterThan(String value) {
            addCriterion("friendly_name >", value, "friendlyName");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameGreaterThanOrEqualTo(String value) {
            addCriterion("friendly_name >=", value, "friendlyName");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameLessThan(String value) {
            addCriterion("friendly_name <", value, "friendlyName");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameLessThanOrEqualTo(String value) {
            addCriterion("friendly_name <=", value, "friendlyName");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameLike(String value) {
            addCriterion("friendly_name like", value, "friendlyName");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameNotLike(String value) {
            addCriterion("friendly_name not like", value, "friendlyName");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameIn(List<String> values) {
            addCriterion("friendly_name in", values, "friendlyName");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameNotIn(List<String> values) {
            addCriterion("friendly_name not in", values, "friendlyName");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameBetween(String value1, String value2) {
            addCriterion("friendly_name between", value1, value2, "friendlyName");
            return (Criteria) this;
        }

        public Criteria andFriendlyNameNotBetween(String value1, String value2) {
            addCriterion("friendly_name not between", value1, value2, "friendlyName");
            return (Criteria) this;
        }

        public Criteria andEventTypeIsNull() {
            addCriterion("event_type is null");
            return (Criteria) this;
        }

        public Criteria andEventTypeIsNotNull() {
            addCriterion("event_type is not null");
            return (Criteria) this;
        }

        public Criteria andEventTypeEqualTo(String value) {
            addCriterion("event_type =", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotEqualTo(String value) {
            addCriterion("event_type <>", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeGreaterThan(String value) {
            addCriterion("event_type >", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeGreaterThanOrEqualTo(String value) {
            addCriterion("event_type >=", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLessThan(String value) {
            addCriterion("event_type <", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLessThanOrEqualTo(String value) {
            addCriterion("event_type <=", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLike(String value) {
            addCriterion("event_type like", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotLike(String value) {
            addCriterion("event_type not like", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeIn(List<String> values) {
            addCriterion("event_type in", values, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotIn(List<String> values) {
            addCriterion("event_type not in", values, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeBetween(String value1, String value2) {
            addCriterion("event_type between", value1, value2, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotBetween(String value1, String value2) {
            addCriterion("event_type not between", value1, value2, "eventType");
            return (Criteria) this;
        }

        public Criteria andBrandsIsNull() {
            addCriterion("brands is null");
            return (Criteria) this;
        }

        public Criteria andBrandsIsNotNull() {
            addCriterion("brands is not null");
            return (Criteria) this;
        }

        public Criteria andBrandsEqualTo(String value) {
            addCriterion("brands =", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsNotEqualTo(String value) {
            addCriterion("brands <>", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsGreaterThan(String value) {
            addCriterion("brands >", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsGreaterThanOrEqualTo(String value) {
            addCriterion("brands >=", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsLessThan(String value) {
            addCriterion("brands <", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsLessThanOrEqualTo(String value) {
            addCriterion("brands <=", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsLike(String value) {
            addCriterion("brands like", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsNotLike(String value) {
            addCriterion("brands not like", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsIn(List<String> values) {
            addCriterion("brands in", values, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsNotIn(List<String> values) {
            addCriterion("brands not in", values, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsBetween(String value1, String value2) {
            addCriterion("brands between", value1, value2, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsNotBetween(String value1, String value2) {
            addCriterion("brands not between", value1, value2, "brands");
            return (Criteria) this;
        }

        public Criteria andSettableIsNull() {
            addCriterion("settable is null");
            return (Criteria) this;
        }

        public Criteria andSettableIsNotNull() {
            addCriterion("settable is not null");
            return (Criteria) this;
        }

        public Criteria andSettableEqualTo(String value) {
            addCriterion("settable =", value, "settable");
            return (Criteria) this;
        }

        public Criteria andSettableNotEqualTo(String value) {
            addCriterion("settable <>", value, "settable");
            return (Criteria) this;
        }

        public Criteria andSettableGreaterThan(String value) {
            addCriterion("settable >", value, "settable");
            return (Criteria) this;
        }

        public Criteria andSettableGreaterThanOrEqualTo(String value) {
            addCriterion("settable >=", value, "settable");
            return (Criteria) this;
        }

        public Criteria andSettableLessThan(String value) {
            addCriterion("settable <", value, "settable");
            return (Criteria) this;
        }

        public Criteria andSettableLessThanOrEqualTo(String value) {
            addCriterion("settable <=", value, "settable");
            return (Criteria) this;
        }

        public Criteria andSettableLike(String value) {
            addCriterion("settable like", value, "settable");
            return (Criteria) this;
        }

        public Criteria andSettableNotLike(String value) {
            addCriterion("settable not like", value, "settable");
            return (Criteria) this;
        }

        public Criteria andSettableIn(List<String> values) {
            addCriterion("settable in", values, "settable");
            return (Criteria) this;
        }

        public Criteria andSettableNotIn(List<String> values) {
            addCriterion("settable not in", values, "settable");
            return (Criteria) this;
        }

        public Criteria andSettableBetween(String value1, String value2) {
            addCriterion("settable between", value1, value2, "settable");
            return (Criteria) this;
        }

        public Criteria andSettableNotBetween(String value1, String value2) {
            addCriterion("settable not between", value1, value2, "settable");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Long value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Long value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Long value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Long value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Long value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Long> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Long> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Long value1, Long value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Long value1, Long value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
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