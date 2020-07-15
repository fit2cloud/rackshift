package io.rackshift.mybatis.domain;

import java.util.ArrayList;
import java.util.List;

public class CpuExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CpuExample() {
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

        public Criteria andProcNameIsNull() {
            addCriterion("proc_name is null");
            return (Criteria) this;
        }

        public Criteria andProcNameIsNotNull() {
            addCriterion("proc_name is not null");
            return (Criteria) this;
        }

        public Criteria andProcNameEqualTo(String value) {
            addCriterion("proc_name =", value, "procName");
            return (Criteria) this;
        }

        public Criteria andProcNameNotEqualTo(String value) {
            addCriterion("proc_name <>", value, "procName");
            return (Criteria) this;
        }

        public Criteria andProcNameGreaterThan(String value) {
            addCriterion("proc_name >", value, "procName");
            return (Criteria) this;
        }

        public Criteria andProcNameGreaterThanOrEqualTo(String value) {
            addCriterion("proc_name >=", value, "procName");
            return (Criteria) this;
        }

        public Criteria andProcNameLessThan(String value) {
            addCriterion("proc_name <", value, "procName");
            return (Criteria) this;
        }

        public Criteria andProcNameLessThanOrEqualTo(String value) {
            addCriterion("proc_name <=", value, "procName");
            return (Criteria) this;
        }

        public Criteria andProcNameLike(String value) {
            addCriterion("proc_name like", value, "procName");
            return (Criteria) this;
        }

        public Criteria andProcNameNotLike(String value) {
            addCriterion("proc_name not like", value, "procName");
            return (Criteria) this;
        }

        public Criteria andProcNameIn(List<String> values) {
            addCriterion("proc_name in", values, "procName");
            return (Criteria) this;
        }

        public Criteria andProcNameNotIn(List<String> values) {
            addCriterion("proc_name not in", values, "procName");
            return (Criteria) this;
        }

        public Criteria andProcNameBetween(String value1, String value2) {
            addCriterion("proc_name between", value1, value2, "procName");
            return (Criteria) this;
        }

        public Criteria andProcNameNotBetween(String value1, String value2) {
            addCriterion("proc_name not between", value1, value2, "procName");
            return (Criteria) this;
        }

        public Criteria andProcSocketIsNull() {
            addCriterion("proc_socket is null");
            return (Criteria) this;
        }

        public Criteria andProcSocketIsNotNull() {
            addCriterion("proc_socket is not null");
            return (Criteria) this;
        }

        public Criteria andProcSocketEqualTo(String value) {
            addCriterion("proc_socket =", value, "procSocket");
            return (Criteria) this;
        }

        public Criteria andProcSocketNotEqualTo(String value) {
            addCriterion("proc_socket <>", value, "procSocket");
            return (Criteria) this;
        }

        public Criteria andProcSocketGreaterThan(String value) {
            addCriterion("proc_socket >", value, "procSocket");
            return (Criteria) this;
        }

        public Criteria andProcSocketGreaterThanOrEqualTo(String value) {
            addCriterion("proc_socket >=", value, "procSocket");
            return (Criteria) this;
        }

        public Criteria andProcSocketLessThan(String value) {
            addCriterion("proc_socket <", value, "procSocket");
            return (Criteria) this;
        }

        public Criteria andProcSocketLessThanOrEqualTo(String value) {
            addCriterion("proc_socket <=", value, "procSocket");
            return (Criteria) this;
        }

        public Criteria andProcSocketLike(String value) {
            addCriterion("proc_socket like", value, "procSocket");
            return (Criteria) this;
        }

        public Criteria andProcSocketNotLike(String value) {
            addCriterion("proc_socket not like", value, "procSocket");
            return (Criteria) this;
        }

        public Criteria andProcSocketIn(List<String> values) {
            addCriterion("proc_socket in", values, "procSocket");
            return (Criteria) this;
        }

        public Criteria andProcSocketNotIn(List<String> values) {
            addCriterion("proc_socket not in", values, "procSocket");
            return (Criteria) this;
        }

        public Criteria andProcSocketBetween(String value1, String value2) {
            addCriterion("proc_socket between", value1, value2, "procSocket");
            return (Criteria) this;
        }

        public Criteria andProcSocketNotBetween(String value1, String value2) {
            addCriterion("proc_socket not between", value1, value2, "procSocket");
            return (Criteria) this;
        }

        public Criteria andProcStatusIsNull() {
            addCriterion("proc_status is null");
            return (Criteria) this;
        }

        public Criteria andProcStatusIsNotNull() {
            addCriterion("proc_status is not null");
            return (Criteria) this;
        }

        public Criteria andProcStatusEqualTo(String value) {
            addCriterion("proc_status =", value, "procStatus");
            return (Criteria) this;
        }

        public Criteria andProcStatusNotEqualTo(String value) {
            addCriterion("proc_status <>", value, "procStatus");
            return (Criteria) this;
        }

        public Criteria andProcStatusGreaterThan(String value) {
            addCriterion("proc_status >", value, "procStatus");
            return (Criteria) this;
        }

        public Criteria andProcStatusGreaterThanOrEqualTo(String value) {
            addCriterion("proc_status >=", value, "procStatus");
            return (Criteria) this;
        }

        public Criteria andProcStatusLessThan(String value) {
            addCriterion("proc_status <", value, "procStatus");
            return (Criteria) this;
        }

        public Criteria andProcStatusLessThanOrEqualTo(String value) {
            addCriterion("proc_status <=", value, "procStatus");
            return (Criteria) this;
        }

        public Criteria andProcStatusLike(String value) {
            addCriterion("proc_status like", value, "procStatus");
            return (Criteria) this;
        }

        public Criteria andProcStatusNotLike(String value) {
            addCriterion("proc_status not like", value, "procStatus");
            return (Criteria) this;
        }

        public Criteria andProcStatusIn(List<String> values) {
            addCriterion("proc_status in", values, "procStatus");
            return (Criteria) this;
        }

        public Criteria andProcStatusNotIn(List<String> values) {
            addCriterion("proc_status not in", values, "procStatus");
            return (Criteria) this;
        }

        public Criteria andProcStatusBetween(String value1, String value2) {
            addCriterion("proc_status between", value1, value2, "procStatus");
            return (Criteria) this;
        }

        public Criteria andProcStatusNotBetween(String value1, String value2) {
            addCriterion("proc_status not between", value1, value2, "procStatus");
            return (Criteria) this;
        }

        public Criteria andProcSpeedIsNull() {
            addCriterion("proc_speed is null");
            return (Criteria) this;
        }

        public Criteria andProcSpeedIsNotNull() {
            addCriterion("proc_speed is not null");
            return (Criteria) this;
        }

        public Criteria andProcSpeedEqualTo(String value) {
            addCriterion("proc_speed =", value, "procSpeed");
            return (Criteria) this;
        }

        public Criteria andProcSpeedNotEqualTo(String value) {
            addCriterion("proc_speed <>", value, "procSpeed");
            return (Criteria) this;
        }

        public Criteria andProcSpeedGreaterThan(String value) {
            addCriterion("proc_speed >", value, "procSpeed");
            return (Criteria) this;
        }

        public Criteria andProcSpeedGreaterThanOrEqualTo(String value) {
            addCriterion("proc_speed >=", value, "procSpeed");
            return (Criteria) this;
        }

        public Criteria andProcSpeedLessThan(String value) {
            addCriterion("proc_speed <", value, "procSpeed");
            return (Criteria) this;
        }

        public Criteria andProcSpeedLessThanOrEqualTo(String value) {
            addCriterion("proc_speed <=", value, "procSpeed");
            return (Criteria) this;
        }

        public Criteria andProcSpeedLike(String value) {
            addCriterion("proc_speed like", value, "procSpeed");
            return (Criteria) this;
        }

        public Criteria andProcSpeedNotLike(String value) {
            addCriterion("proc_speed not like", value, "procSpeed");
            return (Criteria) this;
        }

        public Criteria andProcSpeedIn(List<String> values) {
            addCriterion("proc_speed in", values, "procSpeed");
            return (Criteria) this;
        }

        public Criteria andProcSpeedNotIn(List<String> values) {
            addCriterion("proc_speed not in", values, "procSpeed");
            return (Criteria) this;
        }

        public Criteria andProcSpeedBetween(String value1, String value2) {
            addCriterion("proc_speed between", value1, value2, "procSpeed");
            return (Criteria) this;
        }

        public Criteria andProcSpeedNotBetween(String value1, String value2) {
            addCriterion("proc_speed not between", value1, value2, "procSpeed");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledIsNull() {
            addCriterion("proc_num_cores_enabled is null");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledIsNotNull() {
            addCriterion("proc_num_cores_enabled is not null");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledEqualTo(String value) {
            addCriterion("proc_num_cores_enabled =", value, "procNumCoresEnabled");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledNotEqualTo(String value) {
            addCriterion("proc_num_cores_enabled <>", value, "procNumCoresEnabled");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledGreaterThan(String value) {
            addCriterion("proc_num_cores_enabled >", value, "procNumCoresEnabled");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledGreaterThanOrEqualTo(String value) {
            addCriterion("proc_num_cores_enabled >=", value, "procNumCoresEnabled");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledLessThan(String value) {
            addCriterion("proc_num_cores_enabled <", value, "procNumCoresEnabled");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledLessThanOrEqualTo(String value) {
            addCriterion("proc_num_cores_enabled <=", value, "procNumCoresEnabled");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledLike(String value) {
            addCriterion("proc_num_cores_enabled like", value, "procNumCoresEnabled");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledNotLike(String value) {
            addCriterion("proc_num_cores_enabled not like", value, "procNumCoresEnabled");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledIn(List<String> values) {
            addCriterion("proc_num_cores_enabled in", values, "procNumCoresEnabled");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledNotIn(List<String> values) {
            addCriterion("proc_num_cores_enabled not in", values, "procNumCoresEnabled");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledBetween(String value1, String value2) {
            addCriterion("proc_num_cores_enabled between", value1, value2, "procNumCoresEnabled");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEnabledNotBetween(String value1, String value2) {
            addCriterion("proc_num_cores_enabled not between", value1, value2, "procNumCoresEnabled");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresIsNull() {
            addCriterion("proc_num_cores is null");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresIsNotNull() {
            addCriterion("proc_num_cores is not null");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresEqualTo(String value) {
            addCriterion("proc_num_cores =", value, "procNumCores");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresNotEqualTo(String value) {
            addCriterion("proc_num_cores <>", value, "procNumCores");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresGreaterThan(String value) {
            addCriterion("proc_num_cores >", value, "procNumCores");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresGreaterThanOrEqualTo(String value) {
            addCriterion("proc_num_cores >=", value, "procNumCores");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresLessThan(String value) {
            addCriterion("proc_num_cores <", value, "procNumCores");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresLessThanOrEqualTo(String value) {
            addCriterion("proc_num_cores <=", value, "procNumCores");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresLike(String value) {
            addCriterion("proc_num_cores like", value, "procNumCores");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresNotLike(String value) {
            addCriterion("proc_num_cores not like", value, "procNumCores");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresIn(List<String> values) {
            addCriterion("proc_num_cores in", values, "procNumCores");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresNotIn(List<String> values) {
            addCriterion("proc_num_cores not in", values, "procNumCores");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresBetween(String value1, String value2) {
            addCriterion("proc_num_cores between", value1, value2, "procNumCores");
            return (Criteria) this;
        }

        public Criteria andProcNumCoresNotBetween(String value1, String value2) {
            addCriterion("proc_num_cores not between", value1, value2, "procNumCores");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsIsNull() {
            addCriterion("proc_num_threads is null");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsIsNotNull() {
            addCriterion("proc_num_threads is not null");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsEqualTo(String value) {
            addCriterion("proc_num_threads =", value, "procNumThreads");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsNotEqualTo(String value) {
            addCriterion("proc_num_threads <>", value, "procNumThreads");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsGreaterThan(String value) {
            addCriterion("proc_num_threads >", value, "procNumThreads");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsGreaterThanOrEqualTo(String value) {
            addCriterion("proc_num_threads >=", value, "procNumThreads");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsLessThan(String value) {
            addCriterion("proc_num_threads <", value, "procNumThreads");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsLessThanOrEqualTo(String value) {
            addCriterion("proc_num_threads <=", value, "procNumThreads");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsLike(String value) {
            addCriterion("proc_num_threads like", value, "procNumThreads");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsNotLike(String value) {
            addCriterion("proc_num_threads not like", value, "procNumThreads");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsIn(List<String> values) {
            addCriterion("proc_num_threads in", values, "procNumThreads");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsNotIn(List<String> values) {
            addCriterion("proc_num_threads not in", values, "procNumThreads");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsBetween(String value1, String value2) {
            addCriterion("proc_num_threads between", value1, value2, "procNumThreads");
            return (Criteria) this;
        }

        public Criteria andProcNumThreadsNotBetween(String value1, String value2) {
            addCriterion("proc_num_threads not between", value1, value2, "procNumThreads");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyIsNull() {
            addCriterion("proc_mem_technology is null");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyIsNotNull() {
            addCriterion("proc_mem_technology is not null");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyEqualTo(String value) {
            addCriterion("proc_mem_technology =", value, "procMemTechnology");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyNotEqualTo(String value) {
            addCriterion("proc_mem_technology <>", value, "procMemTechnology");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyGreaterThan(String value) {
            addCriterion("proc_mem_technology >", value, "procMemTechnology");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyGreaterThanOrEqualTo(String value) {
            addCriterion("proc_mem_technology >=", value, "procMemTechnology");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyLessThan(String value) {
            addCriterion("proc_mem_technology <", value, "procMemTechnology");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyLessThanOrEqualTo(String value) {
            addCriterion("proc_mem_technology <=", value, "procMemTechnology");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyLike(String value) {
            addCriterion("proc_mem_technology like", value, "procMemTechnology");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyNotLike(String value) {
            addCriterion("proc_mem_technology not like", value, "procMemTechnology");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyIn(List<String> values) {
            addCriterion("proc_mem_technology in", values, "procMemTechnology");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyNotIn(List<String> values) {
            addCriterion("proc_mem_technology not in", values, "procMemTechnology");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyBetween(String value1, String value2) {
            addCriterion("proc_mem_technology between", value1, value2, "procMemTechnology");
            return (Criteria) this;
        }

        public Criteria andProcMemTechnologyNotBetween(String value1, String value2) {
            addCriterion("proc_mem_technology not between", value1, value2, "procMemTechnology");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheIsNull() {
            addCriterion("proc_num_l1cache is null");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheIsNotNull() {
            addCriterion("proc_num_l1cache is not null");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheEqualTo(String value) {
            addCriterion("proc_num_l1cache =", value, "procNumL1cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheNotEqualTo(String value) {
            addCriterion("proc_num_l1cache <>", value, "procNumL1cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheGreaterThan(String value) {
            addCriterion("proc_num_l1cache >", value, "procNumL1cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheGreaterThanOrEqualTo(String value) {
            addCriterion("proc_num_l1cache >=", value, "procNumL1cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheLessThan(String value) {
            addCriterion("proc_num_l1cache <", value, "procNumL1cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheLessThanOrEqualTo(String value) {
            addCriterion("proc_num_l1cache <=", value, "procNumL1cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheLike(String value) {
            addCriterion("proc_num_l1cache like", value, "procNumL1cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheNotLike(String value) {
            addCriterion("proc_num_l1cache not like", value, "procNumL1cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheIn(List<String> values) {
            addCriterion("proc_num_l1cache in", values, "procNumL1cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheNotIn(List<String> values) {
            addCriterion("proc_num_l1cache not in", values, "procNumL1cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheBetween(String value1, String value2) {
            addCriterion("proc_num_l1cache between", value1, value2, "procNumL1cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL1cacheNotBetween(String value1, String value2) {
            addCriterion("proc_num_l1cache not between", value1, value2, "procNumL1cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheIsNull() {
            addCriterion("proc_num_l2cache is null");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheIsNotNull() {
            addCriterion("proc_num_l2cache is not null");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheEqualTo(String value) {
            addCriterion("proc_num_l2cache =", value, "procNumL2cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheNotEqualTo(String value) {
            addCriterion("proc_num_l2cache <>", value, "procNumL2cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheGreaterThan(String value) {
            addCriterion("proc_num_l2cache >", value, "procNumL2cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheGreaterThanOrEqualTo(String value) {
            addCriterion("proc_num_l2cache >=", value, "procNumL2cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheLessThan(String value) {
            addCriterion("proc_num_l2cache <", value, "procNumL2cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheLessThanOrEqualTo(String value) {
            addCriterion("proc_num_l2cache <=", value, "procNumL2cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheLike(String value) {
            addCriterion("proc_num_l2cache like", value, "procNumL2cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheNotLike(String value) {
            addCriterion("proc_num_l2cache not like", value, "procNumL2cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheIn(List<String> values) {
            addCriterion("proc_num_l2cache in", values, "procNumL2cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheNotIn(List<String> values) {
            addCriterion("proc_num_l2cache not in", values, "procNumL2cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheBetween(String value1, String value2) {
            addCriterion("proc_num_l2cache between", value1, value2, "procNumL2cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL2cacheNotBetween(String value1, String value2) {
            addCriterion("proc_num_l2cache not between", value1, value2, "procNumL2cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheIsNull() {
            addCriterion("proc_num_l3cache is null");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheIsNotNull() {
            addCriterion("proc_num_l3cache is not null");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheEqualTo(String value) {
            addCriterion("proc_num_l3cache =", value, "procNumL3cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheNotEqualTo(String value) {
            addCriterion("proc_num_l3cache <>", value, "procNumL3cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheGreaterThan(String value) {
            addCriterion("proc_num_l3cache >", value, "procNumL3cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheGreaterThanOrEqualTo(String value) {
            addCriterion("proc_num_l3cache >=", value, "procNumL3cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheLessThan(String value) {
            addCriterion("proc_num_l3cache <", value, "procNumL3cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheLessThanOrEqualTo(String value) {
            addCriterion("proc_num_l3cache <=", value, "procNumL3cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheLike(String value) {
            addCriterion("proc_num_l3cache like", value, "procNumL3cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheNotLike(String value) {
            addCriterion("proc_num_l3cache not like", value, "procNumL3cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheIn(List<String> values) {
            addCriterion("proc_num_l3cache in", values, "procNumL3cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheNotIn(List<String> values) {
            addCriterion("proc_num_l3cache not in", values, "procNumL3cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheBetween(String value1, String value2) {
            addCriterion("proc_num_l3cache between", value1, value2, "procNumL3cache");
            return (Criteria) this;
        }

        public Criteria andProcNumL3cacheNotBetween(String value1, String value2) {
            addCriterion("proc_num_l3cache not between", value1, value2, "procNumL3cache");
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