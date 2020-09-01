package io.rackshift.mybatis.domain;

import java.util.ArrayList;
import java.util.List;

public class OutBandExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OutBandExample() {
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

        public Criteria andBareMetalIdIsNull() {
            addCriterion("bare_metal_id is null");
            return (Criteria) this;
        }

        public Criteria andBareMetalIdIsNotNull() {
            addCriterion("bare_metal_id is not null");
            return (Criteria) this;
        }

        public Criteria andBareMetalIdEqualTo(String value) {
            addCriterion("bare_metal_id =", value, "bareMetalId");
            return (Criteria) this;
        }

        public Criteria andBareMetalIdNotEqualTo(String value) {
            addCriterion("bare_metal_id <>", value, "bareMetalId");
            return (Criteria) this;
        }

        public Criteria andBareMetalIdGreaterThan(String value) {
            addCriterion("bare_metal_id >", value, "bareMetalId");
            return (Criteria) this;
        }

        public Criteria andBareMetalIdGreaterThanOrEqualTo(String value) {
            addCriterion("bare_metal_id >=", value, "bareMetalId");
            return (Criteria) this;
        }

        public Criteria andBareMetalIdLessThan(String value) {
            addCriterion("bare_metal_id <", value, "bareMetalId");
            return (Criteria) this;
        }

        public Criteria andBareMetalIdLessThanOrEqualTo(String value) {
            addCriterion("bare_metal_id <=", value, "bareMetalId");
            return (Criteria) this;
        }

        public Criteria andBareMetalIdLike(String value) {
            addCriterion("bare_metal_id like", value, "bareMetalId");
            return (Criteria) this;
        }

        public Criteria andBareMetalIdNotLike(String value) {
            addCriterion("bare_metal_id not like", value, "bareMetalId");
            return (Criteria) this;
        }

        public Criteria andBareMetalIdIn(List<String> values) {
            addCriterion("bare_metal_id in", values, "bareMetalId");
            return (Criteria) this;
        }

        public Criteria andBareMetalIdNotIn(List<String> values) {
            addCriterion("bare_metal_id not in", values, "bareMetalId");
            return (Criteria) this;
        }

        public Criteria andBareMetalIdBetween(String value1, String value2) {
            addCriterion("bare_metal_id between", value1, value2, "bareMetalId");
            return (Criteria) this;
        }

        public Criteria andBareMetalIdNotBetween(String value1, String value2) {
            addCriterion("bare_metal_id not between", value1, value2, "bareMetalId");
            return (Criteria) this;
        }

        public Criteria andEndpointIdIsNull() {
            addCriterion("endpoint_id is null");
            return (Criteria) this;
        }

        public Criteria andEndpointIdIsNotNull() {
            addCriterion("endpoint_id is not null");
            return (Criteria) this;
        }

        public Criteria andEndpointIdEqualTo(String value) {
            addCriterion("endpoint_id =", value, "endpointId");
            return (Criteria) this;
        }

        public Criteria andEndpointIdNotEqualTo(String value) {
            addCriterion("endpoint_id <>", value, "endpointId");
            return (Criteria) this;
        }

        public Criteria andEndpointIdGreaterThan(String value) {
            addCriterion("endpoint_id >", value, "endpointId");
            return (Criteria) this;
        }

        public Criteria andEndpointIdGreaterThanOrEqualTo(String value) {
            addCriterion("endpoint_id >=", value, "endpointId");
            return (Criteria) this;
        }

        public Criteria andEndpointIdLessThan(String value) {
            addCriterion("endpoint_id <", value, "endpointId");
            return (Criteria) this;
        }

        public Criteria andEndpointIdLessThanOrEqualTo(String value) {
            addCriterion("endpoint_id <=", value, "endpointId");
            return (Criteria) this;
        }

        public Criteria andEndpointIdLike(String value) {
            addCriterion("endpoint_id like", value, "endpointId");
            return (Criteria) this;
        }

        public Criteria andEndpointIdNotLike(String value) {
            addCriterion("endpoint_id not like", value, "endpointId");
            return (Criteria) this;
        }

        public Criteria andEndpointIdIn(List<String> values) {
            addCriterion("endpoint_id in", values, "endpointId");
            return (Criteria) this;
        }

        public Criteria andEndpointIdNotIn(List<String> values) {
            addCriterion("endpoint_id not in", values, "endpointId");
            return (Criteria) this;
        }

        public Criteria andEndpointIdBetween(String value1, String value2) {
            addCriterion("endpoint_id between", value1, value2, "endpointId");
            return (Criteria) this;
        }

        public Criteria andEndpointIdNotBetween(String value1, String value2) {
            addCriterion("endpoint_id not between", value1, value2, "endpointId");
            return (Criteria) this;
        }

        public Criteria andMacIsNull() {
            addCriterion("mac is null");
            return (Criteria) this;
        }

        public Criteria andMacIsNotNull() {
            addCriterion("mac is not null");
            return (Criteria) this;
        }

        public Criteria andMacEqualTo(String value) {
            addCriterion("mac =", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacNotEqualTo(String value) {
            addCriterion("mac <>", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacGreaterThan(String value) {
            addCriterion("mac >", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacGreaterThanOrEqualTo(String value) {
            addCriterion("mac >=", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacLessThan(String value) {
            addCriterion("mac <", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacLessThanOrEqualTo(String value) {
            addCriterion("mac <=", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacLike(String value) {
            addCriterion("mac like", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacNotLike(String value) {
            addCriterion("mac not like", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacIn(List<String> values) {
            addCriterion("mac in", values, "mac");
            return (Criteria) this;
        }

        public Criteria andMacNotIn(List<String> values) {
            addCriterion("mac not in", values, "mac");
            return (Criteria) this;
        }

        public Criteria andMacBetween(String value1, String value2) {
            addCriterion("mac between", value1, value2, "mac");
            return (Criteria) this;
        }

        public Criteria andMacNotBetween(String value1, String value2) {
            addCriterion("mac not between", value1, value2, "mac");
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

        public Criteria andUserNameIsNull() {
            addCriterion("user_name is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("user_name <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("user_name >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_name >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("user_name <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("user_name <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("user_name like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("user_name not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("user_name in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("user_name not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("user_name between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("user_name not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andPwdIsNull() {
            addCriterion("pwd is null");
            return (Criteria) this;
        }

        public Criteria andPwdIsNotNull() {
            addCriterion("pwd is not null");
            return (Criteria) this;
        }

        public Criteria andPwdEqualTo(String value) {
            addCriterion("pwd =", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdNotEqualTo(String value) {
            addCriterion("pwd <>", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdGreaterThan(String value) {
            addCriterion("pwd >", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdGreaterThanOrEqualTo(String value) {
            addCriterion("pwd >=", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdLessThan(String value) {
            addCriterion("pwd <", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdLessThanOrEqualTo(String value) {
            addCriterion("pwd <=", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdLike(String value) {
            addCriterion("pwd like", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdNotLike(String value) {
            addCriterion("pwd not like", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdIn(List<String> values) {
            addCriterion("pwd in", values, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdNotIn(List<String> values) {
            addCriterion("pwd not in", values, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdBetween(String value1, String value2) {
            addCriterion("pwd between", value1, value2, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdNotBetween(String value1, String value2) {
            addCriterion("pwd not between", value1, value2, "pwd");
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

        public Criteria andOriginIsNull() {
            addCriterion("origin is null");
            return (Criteria) this;
        }

        public Criteria andOriginIsNotNull() {
            addCriterion("origin is not null");
            return (Criteria) this;
        }

        public Criteria andOriginEqualTo(Byte value) {
            addCriterion("origin =", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginNotEqualTo(Byte value) {
            addCriterion("origin <>", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginGreaterThan(Byte value) {
            addCriterion("origin >", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginGreaterThanOrEqualTo(Byte value) {
            addCriterion("origin >=", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginLessThan(Byte value) {
            addCriterion("origin <", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginLessThanOrEqualTo(Byte value) {
            addCriterion("origin <=", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginIn(List<Byte> values) {
            addCriterion("origin in", values, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginNotIn(List<Byte> values) {
            addCriterion("origin not in", values, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginBetween(Byte value1, Byte value2) {
            addCriterion("origin between", value1, value2, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginNotBetween(Byte value1, Byte value2) {
            addCriterion("origin not between", value1, value2, "origin");
            return (Criteria) this;
        }

        public Criteria andAssetIdIsNull() {
            addCriterion("asset_id is null");
            return (Criteria) this;
        }

        public Criteria andAssetIdIsNotNull() {
            addCriterion("asset_id is not null");
            return (Criteria) this;
        }

        public Criteria andAssetIdEqualTo(String value) {
            addCriterion("asset_id =", value, "assetId");
            return (Criteria) this;
        }

        public Criteria andAssetIdNotEqualTo(String value) {
            addCriterion("asset_id <>", value, "assetId");
            return (Criteria) this;
        }

        public Criteria andAssetIdGreaterThan(String value) {
            addCriterion("asset_id >", value, "assetId");
            return (Criteria) this;
        }

        public Criteria andAssetIdGreaterThanOrEqualTo(String value) {
            addCriterion("asset_id >=", value, "assetId");
            return (Criteria) this;
        }

        public Criteria andAssetIdLessThan(String value) {
            addCriterion("asset_id <", value, "assetId");
            return (Criteria) this;
        }

        public Criteria andAssetIdLessThanOrEqualTo(String value) {
            addCriterion("asset_id <=", value, "assetId");
            return (Criteria) this;
        }

        public Criteria andAssetIdLike(String value) {
            addCriterion("asset_id like", value, "assetId");
            return (Criteria) this;
        }

        public Criteria andAssetIdNotLike(String value) {
            addCriterion("asset_id not like", value, "assetId");
            return (Criteria) this;
        }

        public Criteria andAssetIdIn(List<String> values) {
            addCriterion("asset_id in", values, "assetId");
            return (Criteria) this;
        }

        public Criteria andAssetIdNotIn(List<String> values) {
            addCriterion("asset_id not in", values, "assetId");
            return (Criteria) this;
        }

        public Criteria andAssetIdBetween(String value1, String value2) {
            addCriterion("asset_id between", value1, value2, "assetId");
            return (Criteria) this;
        }

        public Criteria andAssetIdNotBetween(String value1, String value2) {
            addCriterion("asset_id not between", value1, value2, "assetId");
            return (Criteria) this;
        }

        public Criteria andMachineRoomIsNull() {
            addCriterion("machine_room is null");
            return (Criteria) this;
        }

        public Criteria andMachineRoomIsNotNull() {
            addCriterion("machine_room is not null");
            return (Criteria) this;
        }

        public Criteria andMachineRoomEqualTo(String value) {
            addCriterion("machine_room =", value, "machineRoom");
            return (Criteria) this;
        }

        public Criteria andMachineRoomNotEqualTo(String value) {
            addCriterion("machine_room <>", value, "machineRoom");
            return (Criteria) this;
        }

        public Criteria andMachineRoomGreaterThan(String value) {
            addCriterion("machine_room >", value, "machineRoom");
            return (Criteria) this;
        }

        public Criteria andMachineRoomGreaterThanOrEqualTo(String value) {
            addCriterion("machine_room >=", value, "machineRoom");
            return (Criteria) this;
        }

        public Criteria andMachineRoomLessThan(String value) {
            addCriterion("machine_room <", value, "machineRoom");
            return (Criteria) this;
        }

        public Criteria andMachineRoomLessThanOrEqualTo(String value) {
            addCriterion("machine_room <=", value, "machineRoom");
            return (Criteria) this;
        }

        public Criteria andMachineRoomLike(String value) {
            addCriterion("machine_room like", value, "machineRoom");
            return (Criteria) this;
        }

        public Criteria andMachineRoomNotLike(String value) {
            addCriterion("machine_room not like", value, "machineRoom");
            return (Criteria) this;
        }

        public Criteria andMachineRoomIn(List<String> values) {
            addCriterion("machine_room in", values, "machineRoom");
            return (Criteria) this;
        }

        public Criteria andMachineRoomNotIn(List<String> values) {
            addCriterion("machine_room not in", values, "machineRoom");
            return (Criteria) this;
        }

        public Criteria andMachineRoomBetween(String value1, String value2) {
            addCriterion("machine_room between", value1, value2, "machineRoom");
            return (Criteria) this;
        }

        public Criteria andMachineRoomNotBetween(String value1, String value2) {
            addCriterion("machine_room not between", value1, value2, "machineRoom");
            return (Criteria) this;
        }

        public Criteria andMachineRackIsNull() {
            addCriterion("machine_rack is null");
            return (Criteria) this;
        }

        public Criteria andMachineRackIsNotNull() {
            addCriterion("machine_rack is not null");
            return (Criteria) this;
        }

        public Criteria andMachineRackEqualTo(String value) {
            addCriterion("machine_rack =", value, "machineRack");
            return (Criteria) this;
        }

        public Criteria andMachineRackNotEqualTo(String value) {
            addCriterion("machine_rack <>", value, "machineRack");
            return (Criteria) this;
        }

        public Criteria andMachineRackGreaterThan(String value) {
            addCriterion("machine_rack >", value, "machineRack");
            return (Criteria) this;
        }

        public Criteria andMachineRackGreaterThanOrEqualTo(String value) {
            addCriterion("machine_rack >=", value, "machineRack");
            return (Criteria) this;
        }

        public Criteria andMachineRackLessThan(String value) {
            addCriterion("machine_rack <", value, "machineRack");
            return (Criteria) this;
        }

        public Criteria andMachineRackLessThanOrEqualTo(String value) {
            addCriterion("machine_rack <=", value, "machineRack");
            return (Criteria) this;
        }

        public Criteria andMachineRackLike(String value) {
            addCriterion("machine_rack like", value, "machineRack");
            return (Criteria) this;
        }

        public Criteria andMachineRackNotLike(String value) {
            addCriterion("machine_rack not like", value, "machineRack");
            return (Criteria) this;
        }

        public Criteria andMachineRackIn(List<String> values) {
            addCriterion("machine_rack in", values, "machineRack");
            return (Criteria) this;
        }

        public Criteria andMachineRackNotIn(List<String> values) {
            addCriterion("machine_rack not in", values, "machineRack");
            return (Criteria) this;
        }

        public Criteria andMachineRackBetween(String value1, String value2) {
            addCriterion("machine_rack between", value1, value2, "machineRack");
            return (Criteria) this;
        }

        public Criteria andMachineRackNotBetween(String value1, String value2) {
            addCriterion("machine_rack not between", value1, value2, "machineRack");
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

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andApplyUserIsNull() {
            addCriterion("apply_user is null");
            return (Criteria) this;
        }

        public Criteria andApplyUserIsNotNull() {
            addCriterion("apply_user is not null");
            return (Criteria) this;
        }

        public Criteria andApplyUserEqualTo(String value) {
            addCriterion("apply_user =", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserNotEqualTo(String value) {
            addCriterion("apply_user <>", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserGreaterThan(String value) {
            addCriterion("apply_user >", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserGreaterThanOrEqualTo(String value) {
            addCriterion("apply_user >=", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserLessThan(String value) {
            addCriterion("apply_user <", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserLessThanOrEqualTo(String value) {
            addCriterion("apply_user <=", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserLike(String value) {
            addCriterion("apply_user like", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserNotLike(String value) {
            addCriterion("apply_user not like", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserIn(List<String> values) {
            addCriterion("apply_user in", values, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserNotIn(List<String> values) {
            addCriterion("apply_user not in", values, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserBetween(String value1, String value2) {
            addCriterion("apply_user between", value1, value2, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserNotBetween(String value1, String value2) {
            addCriterion("apply_user not between", value1, value2, "applyUser");
            return (Criteria) this;
        }

        public Criteria andOptimisticLockVersionIsNull() {
            addCriterion("optimistic_lock_version is null");
            return (Criteria) this;
        }

        public Criteria andOptimisticLockVersionIsNotNull() {
            addCriterion("optimistic_lock_version is not null");
            return (Criteria) this;
        }

        public Criteria andOptimisticLockVersionEqualTo(Integer value) {
            addCriterion("optimistic_lock_version =", value, "optimisticLockVersion");
            return (Criteria) this;
        }

        public Criteria andOptimisticLockVersionNotEqualTo(Integer value) {
            addCriterion("optimistic_lock_version <>", value, "optimisticLockVersion");
            return (Criteria) this;
        }

        public Criteria andOptimisticLockVersionGreaterThan(Integer value) {
            addCriterion("optimistic_lock_version >", value, "optimisticLockVersion");
            return (Criteria) this;
        }

        public Criteria andOptimisticLockVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("optimistic_lock_version >=", value, "optimisticLockVersion");
            return (Criteria) this;
        }

        public Criteria andOptimisticLockVersionLessThan(Integer value) {
            addCriterion("optimistic_lock_version <", value, "optimisticLockVersion");
            return (Criteria) this;
        }

        public Criteria andOptimisticLockVersionLessThanOrEqualTo(Integer value) {
            addCriterion("optimistic_lock_version <=", value, "optimisticLockVersion");
            return (Criteria) this;
        }

        public Criteria andOptimisticLockVersionIn(List<Integer> values) {
            addCriterion("optimistic_lock_version in", values, "optimisticLockVersion");
            return (Criteria) this;
        }

        public Criteria andOptimisticLockVersionNotIn(List<Integer> values) {
            addCriterion("optimistic_lock_version not in", values, "optimisticLockVersion");
            return (Criteria) this;
        }

        public Criteria andOptimisticLockVersionBetween(Integer value1, Integer value2) {
            addCriterion("optimistic_lock_version between", value1, value2, "optimisticLockVersion");
            return (Criteria) this;
        }

        public Criteria andOptimisticLockVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("optimistic_lock_version not between", value1, value2, "optimisticLockVersion");
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