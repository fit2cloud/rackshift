package io.rackshift.mybatis.domain;

import java.util.ArrayList;
import java.util.List;

public class SwitchExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SwitchExample() {
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

        public Criteria andIpIsNull() {
            addCriterion("ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ip not between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("NAME is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("NAME is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("NAME =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("NAME <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("NAME >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("NAME >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("NAME <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("NAME <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("NAME like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("NAME not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("NAME in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("NAME not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("NAME between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("NAME not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andRuleIdIsNull() {
            addCriterion("rule_id is null");
            return (Criteria) this;
        }

        public Criteria andRuleIdIsNotNull() {
            addCriterion("rule_id is not null");
            return (Criteria) this;
        }

        public Criteria andRuleIdEqualTo(String value) {
            addCriterion("rule_id =", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotEqualTo(String value) {
            addCriterion("rule_id <>", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdGreaterThan(String value) {
            addCriterion("rule_id >", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdGreaterThanOrEqualTo(String value) {
            addCriterion("rule_id >=", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdLessThan(String value) {
            addCriterion("rule_id <", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdLessThanOrEqualTo(String value) {
            addCriterion("rule_id <=", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdLike(String value) {
            addCriterion("rule_id like", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotLike(String value) {
            addCriterion("rule_id not like", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdIn(List<String> values) {
            addCriterion("rule_id in", values, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotIn(List<String> values) {
            addCriterion("rule_id not in", values, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdBetween(String value1, String value2) {
            addCriterion("rule_id between", value1, value2, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotBetween(String value1, String value2) {
            addCriterion("rule_id not between", value1, value2, "ruleId");
            return (Criteria) this;
        }

        public Criteria andVendorIsNull() {
            addCriterion("vendor is null");
            return (Criteria) this;
        }

        public Criteria andVendorIsNotNull() {
            addCriterion("vendor is not null");
            return (Criteria) this;
        }

        public Criteria andVendorEqualTo(String value) {
            addCriterion("vendor =", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorNotEqualTo(String value) {
            addCriterion("vendor <>", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorGreaterThan(String value) {
            addCriterion("vendor >", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorGreaterThanOrEqualTo(String value) {
            addCriterion("vendor >=", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorLessThan(String value) {
            addCriterion("vendor <", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorLessThanOrEqualTo(String value) {
            addCriterion("vendor <=", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorLike(String value) {
            addCriterion("vendor like", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorNotLike(String value) {
            addCriterion("vendor not like", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorIn(List<String> values) {
            addCriterion("vendor in", values, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorNotIn(List<String> values) {
            addCriterion("vendor not in", values, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorBetween(String value1, String value2) {
            addCriterion("vendor between", value1, value2, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorNotBetween(String value1, String value2) {
            addCriterion("vendor not between", value1, value2, "vendor");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityIsNull() {
            addCriterion("snmp_community is null");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityIsNotNull() {
            addCriterion("snmp_community is not null");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityEqualTo(String value) {
            addCriterion("snmp_community =", value, "snmpCommunity");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityNotEqualTo(String value) {
            addCriterion("snmp_community <>", value, "snmpCommunity");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityGreaterThan(String value) {
            addCriterion("snmp_community >", value, "snmpCommunity");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityGreaterThanOrEqualTo(String value) {
            addCriterion("snmp_community >=", value, "snmpCommunity");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityLessThan(String value) {
            addCriterion("snmp_community <", value, "snmpCommunity");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityLessThanOrEqualTo(String value) {
            addCriterion("snmp_community <=", value, "snmpCommunity");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityLike(String value) {
            addCriterion("snmp_community like", value, "snmpCommunity");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityNotLike(String value) {
            addCriterion("snmp_community not like", value, "snmpCommunity");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityIn(List<String> values) {
            addCriterion("snmp_community in", values, "snmpCommunity");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityNotIn(List<String> values) {
            addCriterion("snmp_community not in", values, "snmpCommunity");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityBetween(String value1, String value2) {
            addCriterion("snmp_community between", value1, value2, "snmpCommunity");
            return (Criteria) this;
        }

        public Criteria andSnmpCommunityNotBetween(String value1, String value2) {
            addCriterion("snmp_community not between", value1, value2, "snmpCommunity");
            return (Criteria) this;
        }

        public Criteria andSnmpPortIsNull() {
            addCriterion("snmp_port is null");
            return (Criteria) this;
        }

        public Criteria andSnmpPortIsNotNull() {
            addCriterion("snmp_port is not null");
            return (Criteria) this;
        }

        public Criteria andSnmpPortEqualTo(String value) {
            addCriterion("snmp_port =", value, "snmpPort");
            return (Criteria) this;
        }

        public Criteria andSnmpPortNotEqualTo(String value) {
            addCriterion("snmp_port <>", value, "snmpPort");
            return (Criteria) this;
        }

        public Criteria andSnmpPortGreaterThan(String value) {
            addCriterion("snmp_port >", value, "snmpPort");
            return (Criteria) this;
        }

        public Criteria andSnmpPortGreaterThanOrEqualTo(String value) {
            addCriterion("snmp_port >=", value, "snmpPort");
            return (Criteria) this;
        }

        public Criteria andSnmpPortLessThan(String value) {
            addCriterion("snmp_port <", value, "snmpPort");
            return (Criteria) this;
        }

        public Criteria andSnmpPortLessThanOrEqualTo(String value) {
            addCriterion("snmp_port <=", value, "snmpPort");
            return (Criteria) this;
        }

        public Criteria andSnmpPortLike(String value) {
            addCriterion("snmp_port like", value, "snmpPort");
            return (Criteria) this;
        }

        public Criteria andSnmpPortNotLike(String value) {
            addCriterion("snmp_port not like", value, "snmpPort");
            return (Criteria) this;
        }

        public Criteria andSnmpPortIn(List<String> values) {
            addCriterion("snmp_port in", values, "snmpPort");
            return (Criteria) this;
        }

        public Criteria andSnmpPortNotIn(List<String> values) {
            addCriterion("snmp_port not in", values, "snmpPort");
            return (Criteria) this;
        }

        public Criteria andSnmpPortBetween(String value1, String value2) {
            addCriterion("snmp_port between", value1, value2, "snmpPort");
            return (Criteria) this;
        }

        public Criteria andSnmpPortNotBetween(String value1, String value2) {
            addCriterion("snmp_port not between", value1, value2, "snmpPort");
            return (Criteria) this;
        }

        public Criteria andRoomIsNull() {
            addCriterion("room is null");
            return (Criteria) this;
        }

        public Criteria andRoomIsNotNull() {
            addCriterion("room is not null");
            return (Criteria) this;
        }

        public Criteria andRoomEqualTo(String value) {
            addCriterion("room =", value, "room");
            return (Criteria) this;
        }

        public Criteria andRoomNotEqualTo(String value) {
            addCriterion("room <>", value, "room");
            return (Criteria) this;
        }

        public Criteria andRoomGreaterThan(String value) {
            addCriterion("room >", value, "room");
            return (Criteria) this;
        }

        public Criteria andRoomGreaterThanOrEqualTo(String value) {
            addCriterion("room >=", value, "room");
            return (Criteria) this;
        }

        public Criteria andRoomLessThan(String value) {
            addCriterion("room <", value, "room");
            return (Criteria) this;
        }

        public Criteria andRoomLessThanOrEqualTo(String value) {
            addCriterion("room <=", value, "room");
            return (Criteria) this;
        }

        public Criteria andRoomLike(String value) {
            addCriterion("room like", value, "room");
            return (Criteria) this;
        }

        public Criteria andRoomNotLike(String value) {
            addCriterion("room not like", value, "room");
            return (Criteria) this;
        }

        public Criteria andRoomIn(List<String> values) {
            addCriterion("room in", values, "room");
            return (Criteria) this;
        }

        public Criteria andRoomNotIn(List<String> values) {
            addCriterion("room not in", values, "room");
            return (Criteria) this;
        }

        public Criteria andRoomBetween(String value1, String value2) {
            addCriterion("room between", value1, value2, "room");
            return (Criteria) this;
        }

        public Criteria andRoomNotBetween(String value1, String value2) {
            addCriterion("room not between", value1, value2, "room");
            return (Criteria) this;
        }

        public Criteria andRackIsNull() {
            addCriterion("rack is null");
            return (Criteria) this;
        }

        public Criteria andRackIsNotNull() {
            addCriterion("rack is not null");
            return (Criteria) this;
        }

        public Criteria andRackEqualTo(String value) {
            addCriterion("rack =", value, "rack");
            return (Criteria) this;
        }

        public Criteria andRackNotEqualTo(String value) {
            addCriterion("rack <>", value, "rack");
            return (Criteria) this;
        }

        public Criteria andRackGreaterThan(String value) {
            addCriterion("rack >", value, "rack");
            return (Criteria) this;
        }

        public Criteria andRackGreaterThanOrEqualTo(String value) {
            addCriterion("rack >=", value, "rack");
            return (Criteria) this;
        }

        public Criteria andRackLessThan(String value) {
            addCriterion("rack <", value, "rack");
            return (Criteria) this;
        }

        public Criteria andRackLessThanOrEqualTo(String value) {
            addCriterion("rack <=", value, "rack");
            return (Criteria) this;
        }

        public Criteria andRackLike(String value) {
            addCriterion("rack like", value, "rack");
            return (Criteria) this;
        }

        public Criteria andRackNotLike(String value) {
            addCriterion("rack not like", value, "rack");
            return (Criteria) this;
        }

        public Criteria andRackIn(List<String> values) {
            addCriterion("rack in", values, "rack");
            return (Criteria) this;
        }

        public Criteria andRackNotIn(List<String> values) {
            addCriterion("rack not in", values, "rack");
            return (Criteria) this;
        }

        public Criteria andRackBetween(String value1, String value2) {
            addCriterion("rack between", value1, value2, "rack");
            return (Criteria) this;
        }

        public Criteria andRackNotBetween(String value1, String value2) {
            addCriterion("rack not between", value1, value2, "rack");
            return (Criteria) this;
        }

        public Criteria andUNumberIsNull() {
            addCriterion("u_number is null");
            return (Criteria) this;
        }

        public Criteria andUNumberIsNotNull() {
            addCriterion("u_number is not null");
            return (Criteria) this;
        }

        public Criteria andUNumberEqualTo(String value) {
            addCriterion("u_number =", value, "uNumber");
            return (Criteria) this;
        }

        public Criteria andUNumberNotEqualTo(String value) {
            addCriterion("u_number <>", value, "uNumber");
            return (Criteria) this;
        }

        public Criteria andUNumberGreaterThan(String value) {
            addCriterion("u_number >", value, "uNumber");
            return (Criteria) this;
        }

        public Criteria andUNumberGreaterThanOrEqualTo(String value) {
            addCriterion("u_number >=", value, "uNumber");
            return (Criteria) this;
        }

        public Criteria andUNumberLessThan(String value) {
            addCriterion("u_number <", value, "uNumber");
            return (Criteria) this;
        }

        public Criteria andUNumberLessThanOrEqualTo(String value) {
            addCriterion("u_number <=", value, "uNumber");
            return (Criteria) this;
        }

        public Criteria andUNumberLike(String value) {
            addCriterion("u_number like", value, "uNumber");
            return (Criteria) this;
        }

        public Criteria andUNumberNotLike(String value) {
            addCriterion("u_number not like", value, "uNumber");
            return (Criteria) this;
        }

        public Criteria andUNumberIn(List<String> values) {
            addCriterion("u_number in", values, "uNumber");
            return (Criteria) this;
        }

        public Criteria andUNumberNotIn(List<String> values) {
            addCriterion("u_number not in", values, "uNumber");
            return (Criteria) this;
        }

        public Criteria andUNumberBetween(String value1, String value2) {
            addCriterion("u_number between", value1, value2, "uNumber");
            return (Criteria) this;
        }

        public Criteria andUNumberNotBetween(String value1, String value2) {
            addCriterion("u_number not between", value1, value2, "uNumber");
            return (Criteria) this;
        }

        public Criteria andTelnetPortIsNull() {
            addCriterion("telnet_port is null");
            return (Criteria) this;
        }

        public Criteria andTelnetPortIsNotNull() {
            addCriterion("telnet_port is not null");
            return (Criteria) this;
        }

        public Criteria andTelnetPortEqualTo(String value) {
            addCriterion("telnet_port =", value, "telnetPort");
            return (Criteria) this;
        }

        public Criteria andTelnetPortNotEqualTo(String value) {
            addCriterion("telnet_port <>", value, "telnetPort");
            return (Criteria) this;
        }

        public Criteria andTelnetPortGreaterThan(String value) {
            addCriterion("telnet_port >", value, "telnetPort");
            return (Criteria) this;
        }

        public Criteria andTelnetPortGreaterThanOrEqualTo(String value) {
            addCriterion("telnet_port >=", value, "telnetPort");
            return (Criteria) this;
        }

        public Criteria andTelnetPortLessThan(String value) {
            addCriterion("telnet_port <", value, "telnetPort");
            return (Criteria) this;
        }

        public Criteria andTelnetPortLessThanOrEqualTo(String value) {
            addCriterion("telnet_port <=", value, "telnetPort");
            return (Criteria) this;
        }

        public Criteria andTelnetPortLike(String value) {
            addCriterion("telnet_port like", value, "telnetPort");
            return (Criteria) this;
        }

        public Criteria andTelnetPortNotLike(String value) {
            addCriterion("telnet_port not like", value, "telnetPort");
            return (Criteria) this;
        }

        public Criteria andTelnetPortIn(List<String> values) {
            addCriterion("telnet_port in", values, "telnetPort");
            return (Criteria) this;
        }

        public Criteria andTelnetPortNotIn(List<String> values) {
            addCriterion("telnet_port not in", values, "telnetPort");
            return (Criteria) this;
        }

        public Criteria andTelnetPortBetween(String value1, String value2) {
            addCriterion("telnet_port between", value1, value2, "telnetPort");
            return (Criteria) this;
        }

        public Criteria andTelnetPortNotBetween(String value1, String value2) {
            addCriterion("telnet_port not between", value1, value2, "telnetPort");
            return (Criteria) this;
        }

        public Criteria andSshPortIsNull() {
            addCriterion("ssh_port is null");
            return (Criteria) this;
        }

        public Criteria andSshPortIsNotNull() {
            addCriterion("ssh_port is not null");
            return (Criteria) this;
        }

        public Criteria andSshPortEqualTo(String value) {
            addCriterion("ssh_port =", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortNotEqualTo(String value) {
            addCriterion("ssh_port <>", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortGreaterThan(String value) {
            addCriterion("ssh_port >", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortGreaterThanOrEqualTo(String value) {
            addCriterion("ssh_port >=", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortLessThan(String value) {
            addCriterion("ssh_port <", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortLessThanOrEqualTo(String value) {
            addCriterion("ssh_port <=", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortLike(String value) {
            addCriterion("ssh_port like", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortNotLike(String value) {
            addCriterion("ssh_port not like", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortIn(List<String> values) {
            addCriterion("ssh_port in", values, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortNotIn(List<String> values) {
            addCriterion("ssh_port not in", values, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortBetween(String value1, String value2) {
            addCriterion("ssh_port between", value1, value2, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortNotBetween(String value1, String value2) {
            addCriterion("ssh_port not between", value1, value2, "sshPort");
            return (Criteria) this;
        }

        public Criteria andWebPortIsNull() {
            addCriterion("web_port is null");
            return (Criteria) this;
        }

        public Criteria andWebPortIsNotNull() {
            addCriterion("web_port is not null");
            return (Criteria) this;
        }

        public Criteria andWebPortEqualTo(String value) {
            addCriterion("web_port =", value, "webPort");
            return (Criteria) this;
        }

        public Criteria andWebPortNotEqualTo(String value) {
            addCriterion("web_port <>", value, "webPort");
            return (Criteria) this;
        }

        public Criteria andWebPortGreaterThan(String value) {
            addCriterion("web_port >", value, "webPort");
            return (Criteria) this;
        }

        public Criteria andWebPortGreaterThanOrEqualTo(String value) {
            addCriterion("web_port >=", value, "webPort");
            return (Criteria) this;
        }

        public Criteria andWebPortLessThan(String value) {
            addCriterion("web_port <", value, "webPort");
            return (Criteria) this;
        }

        public Criteria andWebPortLessThanOrEqualTo(String value) {
            addCriterion("web_port <=", value, "webPort");
            return (Criteria) this;
        }

        public Criteria andWebPortLike(String value) {
            addCriterion("web_port like", value, "webPort");
            return (Criteria) this;
        }

        public Criteria andWebPortNotLike(String value) {
            addCriterion("web_port not like", value, "webPort");
            return (Criteria) this;
        }

        public Criteria andWebPortIn(List<String> values) {
            addCriterion("web_port in", values, "webPort");
            return (Criteria) this;
        }

        public Criteria andWebPortNotIn(List<String> values) {
            addCriterion("web_port not in", values, "webPort");
            return (Criteria) this;
        }

        public Criteria andWebPortBetween(String value1, String value2) {
            addCriterion("web_port between", value1, value2, "webPort");
            return (Criteria) this;
        }

        public Criteria andWebPortNotBetween(String value1, String value2) {
            addCriterion("web_port not between", value1, value2, "webPort");
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

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Long value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Long value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Long value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Long value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Long value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Long> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Long> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Long value1, Long value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Long value1, Long value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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