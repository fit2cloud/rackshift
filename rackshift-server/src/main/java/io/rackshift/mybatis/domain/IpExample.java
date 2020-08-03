package io.rackshift.mybatis.domain;

import java.util.ArrayList;
import java.util.List;

public class IpExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public IpExample() {
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

        public Criteria andGatewayIsNull() {
            addCriterion("gateway is null");
            return (Criteria) this;
        }

        public Criteria andGatewayIsNotNull() {
            addCriterion("gateway is not null");
            return (Criteria) this;
        }

        public Criteria andGatewayEqualTo(String value) {
            addCriterion("gateway =", value, "gateway");
            return (Criteria) this;
        }

        public Criteria andGatewayNotEqualTo(String value) {
            addCriterion("gateway <>", value, "gateway");
            return (Criteria) this;
        }

        public Criteria andGatewayGreaterThan(String value) {
            addCriterion("gateway >", value, "gateway");
            return (Criteria) this;
        }

        public Criteria andGatewayGreaterThanOrEqualTo(String value) {
            addCriterion("gateway >=", value, "gateway");
            return (Criteria) this;
        }

        public Criteria andGatewayLessThan(String value) {
            addCriterion("gateway <", value, "gateway");
            return (Criteria) this;
        }

        public Criteria andGatewayLessThanOrEqualTo(String value) {
            addCriterion("gateway <=", value, "gateway");
            return (Criteria) this;
        }

        public Criteria andGatewayLike(String value) {
            addCriterion("gateway like", value, "gateway");
            return (Criteria) this;
        }

        public Criteria andGatewayNotLike(String value) {
            addCriterion("gateway not like", value, "gateway");
            return (Criteria) this;
        }

        public Criteria andGatewayIn(List<String> values) {
            addCriterion("gateway in", values, "gateway");
            return (Criteria) this;
        }

        public Criteria andGatewayNotIn(List<String> values) {
            addCriterion("gateway not in", values, "gateway");
            return (Criteria) this;
        }

        public Criteria andGatewayBetween(String value1, String value2) {
            addCriterion("gateway between", value1, value2, "gateway");
            return (Criteria) this;
        }

        public Criteria andGatewayNotBetween(String value1, String value2) {
            addCriterion("gateway not between", value1, value2, "gateway");
            return (Criteria) this;
        }

        public Criteria andDns1IsNull() {
            addCriterion("dns1 is null");
            return (Criteria) this;
        }

        public Criteria andDns1IsNotNull() {
            addCriterion("dns1 is not null");
            return (Criteria) this;
        }

        public Criteria andDns1EqualTo(String value) {
            addCriterion("dns1 =", value, "dns1");
            return (Criteria) this;
        }

        public Criteria andDns1NotEqualTo(String value) {
            addCriterion("dns1 <>", value, "dns1");
            return (Criteria) this;
        }

        public Criteria andDns1GreaterThan(String value) {
            addCriterion("dns1 >", value, "dns1");
            return (Criteria) this;
        }

        public Criteria andDns1GreaterThanOrEqualTo(String value) {
            addCriterion("dns1 >=", value, "dns1");
            return (Criteria) this;
        }

        public Criteria andDns1LessThan(String value) {
            addCriterion("dns1 <", value, "dns1");
            return (Criteria) this;
        }

        public Criteria andDns1LessThanOrEqualTo(String value) {
            addCriterion("dns1 <=", value, "dns1");
            return (Criteria) this;
        }

        public Criteria andDns1Like(String value) {
            addCriterion("dns1 like", value, "dns1");
            return (Criteria) this;
        }

        public Criteria andDns1NotLike(String value) {
            addCriterion("dns1 not like", value, "dns1");
            return (Criteria) this;
        }

        public Criteria andDns1In(List<String> values) {
            addCriterion("dns1 in", values, "dns1");
            return (Criteria) this;
        }

        public Criteria andDns1NotIn(List<String> values) {
            addCriterion("dns1 not in", values, "dns1");
            return (Criteria) this;
        }

        public Criteria andDns1Between(String value1, String value2) {
            addCriterion("dns1 between", value1, value2, "dns1");
            return (Criteria) this;
        }

        public Criteria andDns1NotBetween(String value1, String value2) {
            addCriterion("dns1 not between", value1, value2, "dns1");
            return (Criteria) this;
        }

        public Criteria andDns2IsNull() {
            addCriterion("dns2 is null");
            return (Criteria) this;
        }

        public Criteria andDns2IsNotNull() {
            addCriterion("dns2 is not null");
            return (Criteria) this;
        }

        public Criteria andDns2EqualTo(String value) {
            addCriterion("dns2 =", value, "dns2");
            return (Criteria) this;
        }

        public Criteria andDns2NotEqualTo(String value) {
            addCriterion("dns2 <>", value, "dns2");
            return (Criteria) this;
        }

        public Criteria andDns2GreaterThan(String value) {
            addCriterion("dns2 >", value, "dns2");
            return (Criteria) this;
        }

        public Criteria andDns2GreaterThanOrEqualTo(String value) {
            addCriterion("dns2 >=", value, "dns2");
            return (Criteria) this;
        }

        public Criteria andDns2LessThan(String value) {
            addCriterion("dns2 <", value, "dns2");
            return (Criteria) this;
        }

        public Criteria andDns2LessThanOrEqualTo(String value) {
            addCriterion("dns2 <=", value, "dns2");
            return (Criteria) this;
        }

        public Criteria andDns2Like(String value) {
            addCriterion("dns2 like", value, "dns2");
            return (Criteria) this;
        }

        public Criteria andDns2NotLike(String value) {
            addCriterion("dns2 not like", value, "dns2");
            return (Criteria) this;
        }

        public Criteria andDns2In(List<String> values) {
            addCriterion("dns2 in", values, "dns2");
            return (Criteria) this;
        }

        public Criteria andDns2NotIn(List<String> values) {
            addCriterion("dns2 not in", values, "dns2");
            return (Criteria) this;
        }

        public Criteria andDns2Between(String value1, String value2) {
            addCriterion("dns2 between", value1, value2, "dns2");
            return (Criteria) this;
        }

        public Criteria andDns2NotBetween(String value1, String value2) {
            addCriterion("dns2 not between", value1, value2, "dns2");
            return (Criteria) this;
        }

        public Criteria andRegionIsNull() {
            addCriterion("region is null");
            return (Criteria) this;
        }

        public Criteria andRegionIsNotNull() {
            addCriterion("region is not null");
            return (Criteria) this;
        }

        public Criteria andRegionEqualTo(String value) {
            addCriterion("region =", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionNotEqualTo(String value) {
            addCriterion("region <>", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionGreaterThan(String value) {
            addCriterion("region >", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionGreaterThanOrEqualTo(String value) {
            addCriterion("region >=", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionLessThan(String value) {
            addCriterion("region <", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionLessThanOrEqualTo(String value) {
            addCriterion("region <=", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionLike(String value) {
            addCriterion("region like", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionNotLike(String value) {
            addCriterion("region not like", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionIn(List<String> values) {
            addCriterion("region in", values, "region");
            return (Criteria) this;
        }

        public Criteria andRegionNotIn(List<String> values) {
            addCriterion("region not in", values, "region");
            return (Criteria) this;
        }

        public Criteria andRegionBetween(String value1, String value2) {
            addCriterion("region between", value1, value2, "region");
            return (Criteria) this;
        }

        public Criteria andRegionNotBetween(String value1, String value2) {
            addCriterion("region not between", value1, value2, "region");
            return (Criteria) this;
        }

        public Criteria andNetworkIdIsNull() {
            addCriterion("network_id is null");
            return (Criteria) this;
        }

        public Criteria andNetworkIdIsNotNull() {
            addCriterion("network_id is not null");
            return (Criteria) this;
        }

        public Criteria andNetworkIdEqualTo(String value) {
            addCriterion("network_id =", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdNotEqualTo(String value) {
            addCriterion("network_id <>", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdGreaterThan(String value) {
            addCriterion("network_id >", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdGreaterThanOrEqualTo(String value) {
            addCriterion("network_id >=", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdLessThan(String value) {
            addCriterion("network_id <", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdLessThanOrEqualTo(String value) {
            addCriterion("network_id <=", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdLike(String value) {
            addCriterion("network_id like", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdNotLike(String value) {
            addCriterion("network_id not like", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdIn(List<String> values) {
            addCriterion("network_id in", values, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdNotIn(List<String> values) {
            addCriterion("network_id not in", values, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdBetween(String value1, String value2) {
            addCriterion("network_id between", value1, value2, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdNotBetween(String value1, String value2) {
            addCriterion("network_id not between", value1, value2, "networkId");
            return (Criteria) this;
        }

        public Criteria andResourceTypeIsNull() {
            addCriterion("resource_type is null");
            return (Criteria) this;
        }

        public Criteria andResourceTypeIsNotNull() {
            addCriterion("resource_type is not null");
            return (Criteria) this;
        }

        public Criteria andResourceTypeEqualTo(String value) {
            addCriterion("resource_type =", value, "resourceType");
            return (Criteria) this;
        }

        public Criteria andResourceTypeNotEqualTo(String value) {
            addCriterion("resource_type <>", value, "resourceType");
            return (Criteria) this;
        }

        public Criteria andResourceTypeGreaterThan(String value) {
            addCriterion("resource_type >", value, "resourceType");
            return (Criteria) this;
        }

        public Criteria andResourceTypeGreaterThanOrEqualTo(String value) {
            addCriterion("resource_type >=", value, "resourceType");
            return (Criteria) this;
        }

        public Criteria andResourceTypeLessThan(String value) {
            addCriterion("resource_type <", value, "resourceType");
            return (Criteria) this;
        }

        public Criteria andResourceTypeLessThanOrEqualTo(String value) {
            addCriterion("resource_type <=", value, "resourceType");
            return (Criteria) this;
        }

        public Criteria andResourceTypeLike(String value) {
            addCriterion("resource_type like", value, "resourceType");
            return (Criteria) this;
        }

        public Criteria andResourceTypeNotLike(String value) {
            addCriterion("resource_type not like", value, "resourceType");
            return (Criteria) this;
        }

        public Criteria andResourceTypeIn(List<String> values) {
            addCriterion("resource_type in", values, "resourceType");
            return (Criteria) this;
        }

        public Criteria andResourceTypeNotIn(List<String> values) {
            addCriterion("resource_type not in", values, "resourceType");
            return (Criteria) this;
        }

        public Criteria andResourceTypeBetween(String value1, String value2) {
            addCriterion("resource_type between", value1, value2, "resourceType");
            return (Criteria) this;
        }

        public Criteria andResourceTypeNotBetween(String value1, String value2) {
            addCriterion("resource_type not between", value1, value2, "resourceType");
            return (Criteria) this;
        }

        public Criteria andResourceIdIsNull() {
            addCriterion("resource_id is null");
            return (Criteria) this;
        }

        public Criteria andResourceIdIsNotNull() {
            addCriterion("resource_id is not null");
            return (Criteria) this;
        }

        public Criteria andResourceIdEqualTo(String value) {
            addCriterion("resource_id =", value, "resourceId");
            return (Criteria) this;
        }

        public Criteria andResourceIdNotEqualTo(String value) {
            addCriterion("resource_id <>", value, "resourceId");
            return (Criteria) this;
        }

        public Criteria andResourceIdGreaterThan(String value) {
            addCriterion("resource_id >", value, "resourceId");
            return (Criteria) this;
        }

        public Criteria andResourceIdGreaterThanOrEqualTo(String value) {
            addCriterion("resource_id >=", value, "resourceId");
            return (Criteria) this;
        }

        public Criteria andResourceIdLessThan(String value) {
            addCriterion("resource_id <", value, "resourceId");
            return (Criteria) this;
        }

        public Criteria andResourceIdLessThanOrEqualTo(String value) {
            addCriterion("resource_id <=", value, "resourceId");
            return (Criteria) this;
        }

        public Criteria andResourceIdLike(String value) {
            addCriterion("resource_id like", value, "resourceId");
            return (Criteria) this;
        }

        public Criteria andResourceIdNotLike(String value) {
            addCriterion("resource_id not like", value, "resourceId");
            return (Criteria) this;
        }

        public Criteria andResourceIdIn(List<String> values) {
            addCriterion("resource_id in", values, "resourceId");
            return (Criteria) this;
        }

        public Criteria andResourceIdNotIn(List<String> values) {
            addCriterion("resource_id not in", values, "resourceId");
            return (Criteria) this;
        }

        public Criteria andResourceIdBetween(String value1, String value2) {
            addCriterion("resource_id between", value1, value2, "resourceId");
            return (Criteria) this;
        }

        public Criteria andResourceIdNotBetween(String value1, String value2) {
            addCriterion("resource_id not between", value1, value2, "resourceId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdIsNull() {
            addCriterion("order_item_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdIsNotNull() {
            addCriterion("order_item_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdEqualTo(String value) {
            addCriterion("order_item_id =", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdNotEqualTo(String value) {
            addCriterion("order_item_id <>", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdGreaterThan(String value) {
            addCriterion("order_item_id >", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdGreaterThanOrEqualTo(String value) {
            addCriterion("order_item_id >=", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdLessThan(String value) {
            addCriterion("order_item_id <", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdLessThanOrEqualTo(String value) {
            addCriterion("order_item_id <=", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdLike(String value) {
            addCriterion("order_item_id like", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdNotLike(String value) {
            addCriterion("order_item_id not like", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdIn(List<String> values) {
            addCriterion("order_item_id in", values, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdNotIn(List<String> values) {
            addCriterion("order_item_id not in", values, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdBetween(String value1, String value2) {
            addCriterion("order_item_id between", value1, value2, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdNotBetween(String value1, String value2) {
            addCriterion("order_item_id not between", value1, value2, "orderItemId");
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