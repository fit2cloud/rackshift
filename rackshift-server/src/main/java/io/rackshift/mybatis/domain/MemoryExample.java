package io.rackshift.mybatis.domain;

import java.util.ArrayList;
import java.util.List;

public class MemoryExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MemoryExample() {
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

        public Criteria andMemCpuNumIsNull() {
            addCriterion("mem_cpu_num is null");
            return (Criteria) this;
        }

        public Criteria andMemCpuNumIsNotNull() {
            addCriterion("mem_cpu_num is not null");
            return (Criteria) this;
        }

        public Criteria andMemCpuNumEqualTo(String value) {
            addCriterion("mem_cpu_num =", value, "memCpuNum");
            return (Criteria) this;
        }

        public Criteria andMemCpuNumNotEqualTo(String value) {
            addCriterion("mem_cpu_num <>", value, "memCpuNum");
            return (Criteria) this;
        }

        public Criteria andMemCpuNumGreaterThan(String value) {
            addCriterion("mem_cpu_num >", value, "memCpuNum");
            return (Criteria) this;
        }

        public Criteria andMemCpuNumGreaterThanOrEqualTo(String value) {
            addCriterion("mem_cpu_num >=", value, "memCpuNum");
            return (Criteria) this;
        }

        public Criteria andMemCpuNumLessThan(String value) {
            addCriterion("mem_cpu_num <", value, "memCpuNum");
            return (Criteria) this;
        }

        public Criteria andMemCpuNumLessThanOrEqualTo(String value) {
            addCriterion("mem_cpu_num <=", value, "memCpuNum");
            return (Criteria) this;
        }

        public Criteria andMemCpuNumLike(String value) {
            addCriterion("mem_cpu_num like", value, "memCpuNum");
            return (Criteria) this;
        }

        public Criteria andMemCpuNumNotLike(String value) {
            addCriterion("mem_cpu_num not like", value, "memCpuNum");
            return (Criteria) this;
        }

        public Criteria andMemCpuNumIn(List<String> values) {
            addCriterion("mem_cpu_num in", values, "memCpuNum");
            return (Criteria) this;
        }

        public Criteria andMemCpuNumNotIn(List<String> values) {
            addCriterion("mem_cpu_num not in", values, "memCpuNum");
            return (Criteria) this;
        }

        public Criteria andMemCpuNumBetween(String value1, String value2) {
            addCriterion("mem_cpu_num between", value1, value2, "memCpuNum");
            return (Criteria) this;
        }

        public Criteria andMemCpuNumNotBetween(String value1, String value2) {
            addCriterion("mem_cpu_num not between", value1, value2, "memCpuNum");
            return (Criteria) this;
        }

        public Criteria andMemModNumIsNull() {
            addCriterion("mem_mod_num is null");
            return (Criteria) this;
        }

        public Criteria andMemModNumIsNotNull() {
            addCriterion("mem_mod_num is not null");
            return (Criteria) this;
        }

        public Criteria andMemModNumEqualTo(String value) {
            addCriterion("mem_mod_num =", value, "memModNum");
            return (Criteria) this;
        }

        public Criteria andMemModNumNotEqualTo(String value) {
            addCriterion("mem_mod_num <>", value, "memModNum");
            return (Criteria) this;
        }

        public Criteria andMemModNumGreaterThan(String value) {
            addCriterion("mem_mod_num >", value, "memModNum");
            return (Criteria) this;
        }

        public Criteria andMemModNumGreaterThanOrEqualTo(String value) {
            addCriterion("mem_mod_num >=", value, "memModNum");
            return (Criteria) this;
        }

        public Criteria andMemModNumLessThan(String value) {
            addCriterion("mem_mod_num <", value, "memModNum");
            return (Criteria) this;
        }

        public Criteria andMemModNumLessThanOrEqualTo(String value) {
            addCriterion("mem_mod_num <=", value, "memModNum");
            return (Criteria) this;
        }

        public Criteria andMemModNumLike(String value) {
            addCriterion("mem_mod_num like", value, "memModNum");
            return (Criteria) this;
        }

        public Criteria andMemModNumNotLike(String value) {
            addCriterion("mem_mod_num not like", value, "memModNum");
            return (Criteria) this;
        }

        public Criteria andMemModNumIn(List<String> values) {
            addCriterion("mem_mod_num in", values, "memModNum");
            return (Criteria) this;
        }

        public Criteria andMemModNumNotIn(List<String> values) {
            addCriterion("mem_mod_num not in", values, "memModNum");
            return (Criteria) this;
        }

        public Criteria andMemModNumBetween(String value1, String value2) {
            addCriterion("mem_mod_num between", value1, value2, "memModNum");
            return (Criteria) this;
        }

        public Criteria andMemModNumNotBetween(String value1, String value2) {
            addCriterion("mem_mod_num not between", value1, value2, "memModNum");
            return (Criteria) this;
        }

        public Criteria andMemModSizeIsNull() {
            addCriterion("mem_mod_size is null");
            return (Criteria) this;
        }

        public Criteria andMemModSizeIsNotNull() {
            addCriterion("mem_mod_size is not null");
            return (Criteria) this;
        }

        public Criteria andMemModSizeEqualTo(String value) {
            addCriterion("mem_mod_size =", value, "memModSize");
            return (Criteria) this;
        }

        public Criteria andMemModSizeNotEqualTo(String value) {
            addCriterion("mem_mod_size <>", value, "memModSize");
            return (Criteria) this;
        }

        public Criteria andMemModSizeGreaterThan(String value) {
            addCriterion("mem_mod_size >", value, "memModSize");
            return (Criteria) this;
        }

        public Criteria andMemModSizeGreaterThanOrEqualTo(String value) {
            addCriterion("mem_mod_size >=", value, "memModSize");
            return (Criteria) this;
        }

        public Criteria andMemModSizeLessThan(String value) {
            addCriterion("mem_mod_size <", value, "memModSize");
            return (Criteria) this;
        }

        public Criteria andMemModSizeLessThanOrEqualTo(String value) {
            addCriterion("mem_mod_size <=", value, "memModSize");
            return (Criteria) this;
        }

        public Criteria andMemModSizeLike(String value) {
            addCriterion("mem_mod_size like", value, "memModSize");
            return (Criteria) this;
        }

        public Criteria andMemModSizeNotLike(String value) {
            addCriterion("mem_mod_size not like", value, "memModSize");
            return (Criteria) this;
        }

        public Criteria andMemModSizeIn(List<String> values) {
            addCriterion("mem_mod_size in", values, "memModSize");
            return (Criteria) this;
        }

        public Criteria andMemModSizeNotIn(List<String> values) {
            addCriterion("mem_mod_size not in", values, "memModSize");
            return (Criteria) this;
        }

        public Criteria andMemModSizeBetween(String value1, String value2) {
            addCriterion("mem_mod_size between", value1, value2, "memModSize");
            return (Criteria) this;
        }

        public Criteria andMemModSizeNotBetween(String value1, String value2) {
            addCriterion("mem_mod_size not between", value1, value2, "memModSize");
            return (Criteria) this;
        }

        public Criteria andMemModTypeIsNull() {
            addCriterion("mem_mod_type is null");
            return (Criteria) this;
        }

        public Criteria andMemModTypeIsNotNull() {
            addCriterion("mem_mod_type is not null");
            return (Criteria) this;
        }

        public Criteria andMemModTypeEqualTo(String value) {
            addCriterion("mem_mod_type =", value, "memModType");
            return (Criteria) this;
        }

        public Criteria andMemModTypeNotEqualTo(String value) {
            addCriterion("mem_mod_type <>", value, "memModType");
            return (Criteria) this;
        }

        public Criteria andMemModTypeGreaterThan(String value) {
            addCriterion("mem_mod_type >", value, "memModType");
            return (Criteria) this;
        }

        public Criteria andMemModTypeGreaterThanOrEqualTo(String value) {
            addCriterion("mem_mod_type >=", value, "memModType");
            return (Criteria) this;
        }

        public Criteria andMemModTypeLessThan(String value) {
            addCriterion("mem_mod_type <", value, "memModType");
            return (Criteria) this;
        }

        public Criteria andMemModTypeLessThanOrEqualTo(String value) {
            addCriterion("mem_mod_type <=", value, "memModType");
            return (Criteria) this;
        }

        public Criteria andMemModTypeLike(String value) {
            addCriterion("mem_mod_type like", value, "memModType");
            return (Criteria) this;
        }

        public Criteria andMemModTypeNotLike(String value) {
            addCriterion("mem_mod_type not like", value, "memModType");
            return (Criteria) this;
        }

        public Criteria andMemModTypeIn(List<String> values) {
            addCriterion("mem_mod_type in", values, "memModType");
            return (Criteria) this;
        }

        public Criteria andMemModTypeNotIn(List<String> values) {
            addCriterion("mem_mod_type not in", values, "memModType");
            return (Criteria) this;
        }

        public Criteria andMemModTypeBetween(String value1, String value2) {
            addCriterion("mem_mod_type between", value1, value2, "memModType");
            return (Criteria) this;
        }

        public Criteria andMemModTypeNotBetween(String value1, String value2) {
            addCriterion("mem_mod_type not between", value1, value2, "memModType");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyIsNull() {
            addCriterion("mem_mod_frequency is null");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyIsNotNull() {
            addCriterion("mem_mod_frequency is not null");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyEqualTo(String value) {
            addCriterion("mem_mod_frequency =", value, "memModFrequency");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyNotEqualTo(String value) {
            addCriterion("mem_mod_frequency <>", value, "memModFrequency");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyGreaterThan(String value) {
            addCriterion("mem_mod_frequency >", value, "memModFrequency");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyGreaterThanOrEqualTo(String value) {
            addCriterion("mem_mod_frequency >=", value, "memModFrequency");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyLessThan(String value) {
            addCriterion("mem_mod_frequency <", value, "memModFrequency");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyLessThanOrEqualTo(String value) {
            addCriterion("mem_mod_frequency <=", value, "memModFrequency");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyLike(String value) {
            addCriterion("mem_mod_frequency like", value, "memModFrequency");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyNotLike(String value) {
            addCriterion("mem_mod_frequency not like", value, "memModFrequency");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyIn(List<String> values) {
            addCriterion("mem_mod_frequency in", values, "memModFrequency");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyNotIn(List<String> values) {
            addCriterion("mem_mod_frequency not in", values, "memModFrequency");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyBetween(String value1, String value2) {
            addCriterion("mem_mod_frequency between", value1, value2, "memModFrequency");
            return (Criteria) this;
        }

        public Criteria andMemModFrequencyNotBetween(String value1, String value2) {
            addCriterion("mem_mod_frequency not between", value1, value2, "memModFrequency");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumIsNull() {
            addCriterion("mem_mod_part_num is null");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumIsNotNull() {
            addCriterion("mem_mod_part_num is not null");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumEqualTo(String value) {
            addCriterion("mem_mod_part_num =", value, "memModPartNum");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumNotEqualTo(String value) {
            addCriterion("mem_mod_part_num <>", value, "memModPartNum");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumGreaterThan(String value) {
            addCriterion("mem_mod_part_num >", value, "memModPartNum");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumGreaterThanOrEqualTo(String value) {
            addCriterion("mem_mod_part_num >=", value, "memModPartNum");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumLessThan(String value) {
            addCriterion("mem_mod_part_num <", value, "memModPartNum");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumLessThanOrEqualTo(String value) {
            addCriterion("mem_mod_part_num <=", value, "memModPartNum");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumLike(String value) {
            addCriterion("mem_mod_part_num like", value, "memModPartNum");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumNotLike(String value) {
            addCriterion("mem_mod_part_num not like", value, "memModPartNum");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumIn(List<String> values) {
            addCriterion("mem_mod_part_num in", values, "memModPartNum");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumNotIn(List<String> values) {
            addCriterion("mem_mod_part_num not in", values, "memModPartNum");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumBetween(String value1, String value2) {
            addCriterion("mem_mod_part_num between", value1, value2, "memModPartNum");
            return (Criteria) this;
        }

        public Criteria andMemModPartNumNotBetween(String value1, String value2) {
            addCriterion("mem_mod_part_num not between", value1, value2, "memModPartNum");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltIsNull() {
            addCriterion("mem_mod_min_volt is null");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltIsNotNull() {
            addCriterion("mem_mod_min_volt is not null");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltEqualTo(String value) {
            addCriterion("mem_mod_min_volt =", value, "memModMinVolt");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltNotEqualTo(String value) {
            addCriterion("mem_mod_min_volt <>", value, "memModMinVolt");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltGreaterThan(String value) {
            addCriterion("mem_mod_min_volt >", value, "memModMinVolt");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltGreaterThanOrEqualTo(String value) {
            addCriterion("mem_mod_min_volt >=", value, "memModMinVolt");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltLessThan(String value) {
            addCriterion("mem_mod_min_volt <", value, "memModMinVolt");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltLessThanOrEqualTo(String value) {
            addCriterion("mem_mod_min_volt <=", value, "memModMinVolt");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltLike(String value) {
            addCriterion("mem_mod_min_volt like", value, "memModMinVolt");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltNotLike(String value) {
            addCriterion("mem_mod_min_volt not like", value, "memModMinVolt");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltIn(List<String> values) {
            addCriterion("mem_mod_min_volt in", values, "memModMinVolt");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltNotIn(List<String> values) {
            addCriterion("mem_mod_min_volt not in", values, "memModMinVolt");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltBetween(String value1, String value2) {
            addCriterion("mem_mod_min_volt between", value1, value2, "memModMinVolt");
            return (Criteria) this;
        }

        public Criteria andMemModMinVoltNotBetween(String value1, String value2) {
            addCriterion("mem_mod_min_volt not between", value1, value2, "memModMinVolt");
            return (Criteria) this;
        }

        public Criteria andSnIsNull() {
            addCriterion("sn is null");
            return (Criteria) this;
        }

        public Criteria andSnIsNotNull() {
            addCriterion("sn is not null");
            return (Criteria) this;
        }

        public Criteria andSnEqualTo(String value) {
            addCriterion("sn =", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnNotEqualTo(String value) {
            addCriterion("sn <>", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnGreaterThan(String value) {
            addCriterion("sn >", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnGreaterThanOrEqualTo(String value) {
            addCriterion("sn >=", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnLessThan(String value) {
            addCriterion("sn <", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnLessThanOrEqualTo(String value) {
            addCriterion("sn <=", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnLike(String value) {
            addCriterion("sn like", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnNotLike(String value) {
            addCriterion("sn not like", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnIn(List<String> values) {
            addCriterion("sn in", values, "sn");
            return (Criteria) this;
        }

        public Criteria andSnNotIn(List<String> values) {
            addCriterion("sn not in", values, "sn");
            return (Criteria) this;
        }

        public Criteria andSnBetween(String value1, String value2) {
            addCriterion("sn between", value1, value2, "sn");
            return (Criteria) this;
        }

        public Criteria andSnNotBetween(String value1, String value2) {
            addCriterion("sn not between", value1, value2, "sn");
            return (Criteria) this;
        }

        public Criteria andSyncTimeIsNull() {
            addCriterion("sync_time is null");
            return (Criteria) this;
        }

        public Criteria andSyncTimeIsNotNull() {
            addCriterion("sync_time is not null");
            return (Criteria) this;
        }

        public Criteria andSyncTimeEqualTo(Long value) {
            addCriterion("sync_time =", value, "syncTime");
            return (Criteria) this;
        }

        public Criteria andSyncTimeNotEqualTo(Long value) {
            addCriterion("sync_time <>", value, "syncTime");
            return (Criteria) this;
        }

        public Criteria andSyncTimeGreaterThan(Long value) {
            addCriterion("sync_time >", value, "syncTime");
            return (Criteria) this;
        }

        public Criteria andSyncTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("sync_time >=", value, "syncTime");
            return (Criteria) this;
        }

        public Criteria andSyncTimeLessThan(Long value) {
            addCriterion("sync_time <", value, "syncTime");
            return (Criteria) this;
        }

        public Criteria andSyncTimeLessThanOrEqualTo(Long value) {
            addCriterion("sync_time <=", value, "syncTime");
            return (Criteria) this;
        }

        public Criteria andSyncTimeIn(List<Long> values) {
            addCriterion("sync_time in", values, "syncTime");
            return (Criteria) this;
        }

        public Criteria andSyncTimeNotIn(List<Long> values) {
            addCriterion("sync_time not in", values, "syncTime");
            return (Criteria) this;
        }

        public Criteria andSyncTimeBetween(Long value1, Long value2) {
            addCriterion("sync_time between", value1, value2, "syncTime");
            return (Criteria) this;
        }

        public Criteria andSyncTimeNotBetween(Long value1, Long value2) {
            addCriterion("sync_time not between", value1, value2, "syncTime");
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

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
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