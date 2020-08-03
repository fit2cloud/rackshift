package io.rackshift.mybatis.domain;

import java.util.ArrayList;
import java.util.List;

public class NetworkExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public NetworkExample() {
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

        public Criteria andVlanIdIsNull() {
            addCriterion("vlan_id is null");
            return (Criteria) this;
        }

        public Criteria andVlanIdIsNotNull() {
            addCriterion("vlan_id is not null");
            return (Criteria) this;
        }

        public Criteria andVlanIdEqualTo(String value) {
            addCriterion("vlan_id =", value, "vlanId");
            return (Criteria) this;
        }

        public Criteria andVlanIdNotEqualTo(String value) {
            addCriterion("vlan_id <>", value, "vlanId");
            return (Criteria) this;
        }

        public Criteria andVlanIdGreaterThan(String value) {
            addCriterion("vlan_id >", value, "vlanId");
            return (Criteria) this;
        }

        public Criteria andVlanIdGreaterThanOrEqualTo(String value) {
            addCriterion("vlan_id >=", value, "vlanId");
            return (Criteria) this;
        }

        public Criteria andVlanIdLessThan(String value) {
            addCriterion("vlan_id <", value, "vlanId");
            return (Criteria) this;
        }

        public Criteria andVlanIdLessThanOrEqualTo(String value) {
            addCriterion("vlan_id <=", value, "vlanId");
            return (Criteria) this;
        }

        public Criteria andVlanIdLike(String value) {
            addCriterion("vlan_id like", value, "vlanId");
            return (Criteria) this;
        }

        public Criteria andVlanIdNotLike(String value) {
            addCriterion("vlan_id not like", value, "vlanId");
            return (Criteria) this;
        }

        public Criteria andVlanIdIn(List<String> values) {
            addCriterion("vlan_id in", values, "vlanId");
            return (Criteria) this;
        }

        public Criteria andVlanIdNotIn(List<String> values) {
            addCriterion("vlan_id not in", values, "vlanId");
            return (Criteria) this;
        }

        public Criteria andVlanIdBetween(String value1, String value2) {
            addCriterion("vlan_id between", value1, value2, "vlanId");
            return (Criteria) this;
        }

        public Criteria andVlanIdNotBetween(String value1, String value2) {
            addCriterion("vlan_id not between", value1, value2, "vlanId");
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

        public Criteria andNetmaskIsNull() {
            addCriterion("netmask is null");
            return (Criteria) this;
        }

        public Criteria andNetmaskIsNotNull() {
            addCriterion("netmask is not null");
            return (Criteria) this;
        }

        public Criteria andNetmaskEqualTo(String value) {
            addCriterion("netmask =", value, "netmask");
            return (Criteria) this;
        }

        public Criteria andNetmaskNotEqualTo(String value) {
            addCriterion("netmask <>", value, "netmask");
            return (Criteria) this;
        }

        public Criteria andNetmaskGreaterThan(String value) {
            addCriterion("netmask >", value, "netmask");
            return (Criteria) this;
        }

        public Criteria andNetmaskGreaterThanOrEqualTo(String value) {
            addCriterion("netmask >=", value, "netmask");
            return (Criteria) this;
        }

        public Criteria andNetmaskLessThan(String value) {
            addCriterion("netmask <", value, "netmask");
            return (Criteria) this;
        }

        public Criteria andNetmaskLessThanOrEqualTo(String value) {
            addCriterion("netmask <=", value, "netmask");
            return (Criteria) this;
        }

        public Criteria andNetmaskLike(String value) {
            addCriterion("netmask like", value, "netmask");
            return (Criteria) this;
        }

        public Criteria andNetmaskNotLike(String value) {
            addCriterion("netmask not like", value, "netmask");
            return (Criteria) this;
        }

        public Criteria andNetmaskIn(List<String> values) {
            addCriterion("netmask in", values, "netmask");
            return (Criteria) this;
        }

        public Criteria andNetmaskNotIn(List<String> values) {
            addCriterion("netmask not in", values, "netmask");
            return (Criteria) this;
        }

        public Criteria andNetmaskBetween(String value1, String value2) {
            addCriterion("netmask between", value1, value2, "netmask");
            return (Criteria) this;
        }

        public Criteria andNetmaskNotBetween(String value1, String value2) {
            addCriterion("netmask not between", value1, value2, "netmask");
            return (Criteria) this;
        }

        public Criteria andDhcpEnableIsNull() {
            addCriterion("dhcp_enable is null");
            return (Criteria) this;
        }

        public Criteria andDhcpEnableIsNotNull() {
            addCriterion("dhcp_enable is not null");
            return (Criteria) this;
        }

        public Criteria andDhcpEnableEqualTo(Boolean value) {
            addCriterion("dhcp_enable =", value, "dhcpEnable");
            return (Criteria) this;
        }

        public Criteria andDhcpEnableNotEqualTo(Boolean value) {
            addCriterion("dhcp_enable <>", value, "dhcpEnable");
            return (Criteria) this;
        }

        public Criteria andDhcpEnableGreaterThan(Boolean value) {
            addCriterion("dhcp_enable >", value, "dhcpEnable");
            return (Criteria) this;
        }

        public Criteria andDhcpEnableGreaterThanOrEqualTo(Boolean value) {
            addCriterion("dhcp_enable >=", value, "dhcpEnable");
            return (Criteria) this;
        }

        public Criteria andDhcpEnableLessThan(Boolean value) {
            addCriterion("dhcp_enable <", value, "dhcpEnable");
            return (Criteria) this;
        }

        public Criteria andDhcpEnableLessThanOrEqualTo(Boolean value) {
            addCriterion("dhcp_enable <=", value, "dhcpEnable");
            return (Criteria) this;
        }

        public Criteria andDhcpEnableIn(List<Boolean> values) {
            addCriterion("dhcp_enable in", values, "dhcpEnable");
            return (Criteria) this;
        }

        public Criteria andDhcpEnableNotIn(List<Boolean> values) {
            addCriterion("dhcp_enable not in", values, "dhcpEnable");
            return (Criteria) this;
        }

        public Criteria andDhcpEnableBetween(Boolean value1, Boolean value2) {
            addCriterion("dhcp_enable between", value1, value2, "dhcpEnable");
            return (Criteria) this;
        }

        public Criteria andDhcpEnableNotBetween(Boolean value1, Boolean value2) {
            addCriterion("dhcp_enable not between", value1, value2, "dhcpEnable");
            return (Criteria) this;
        }

        public Criteria andPxeEnableIsNull() {
            addCriterion("pxe_enable is null");
            return (Criteria) this;
        }

        public Criteria andPxeEnableIsNotNull() {
            addCriterion("pxe_enable is not null");
            return (Criteria) this;
        }

        public Criteria andPxeEnableEqualTo(Boolean value) {
            addCriterion("pxe_enable =", value, "pxeEnable");
            return (Criteria) this;
        }

        public Criteria andPxeEnableNotEqualTo(Boolean value) {
            addCriterion("pxe_enable <>", value, "pxeEnable");
            return (Criteria) this;
        }

        public Criteria andPxeEnableGreaterThan(Boolean value) {
            addCriterion("pxe_enable >", value, "pxeEnable");
            return (Criteria) this;
        }

        public Criteria andPxeEnableGreaterThanOrEqualTo(Boolean value) {
            addCriterion("pxe_enable >=", value, "pxeEnable");
            return (Criteria) this;
        }

        public Criteria andPxeEnableLessThan(Boolean value) {
            addCriterion("pxe_enable <", value, "pxeEnable");
            return (Criteria) this;
        }

        public Criteria andPxeEnableLessThanOrEqualTo(Boolean value) {
            addCriterion("pxe_enable <=", value, "pxeEnable");
            return (Criteria) this;
        }

        public Criteria andPxeEnableIn(List<Boolean> values) {
            addCriterion("pxe_enable in", values, "pxeEnable");
            return (Criteria) this;
        }

        public Criteria andPxeEnableNotIn(List<Boolean> values) {
            addCriterion("pxe_enable not in", values, "pxeEnable");
            return (Criteria) this;
        }

        public Criteria andPxeEnableBetween(Boolean value1, Boolean value2) {
            addCriterion("pxe_enable between", value1, value2, "pxeEnable");
            return (Criteria) this;
        }

        public Criteria andPxeEnableNotBetween(Boolean value1, Boolean value2) {
            addCriterion("pxe_enable not between", value1, value2, "pxeEnable");
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