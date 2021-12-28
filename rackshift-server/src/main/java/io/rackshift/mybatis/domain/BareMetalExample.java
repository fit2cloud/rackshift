package io.rackshift.mybatis.domain;

import java.util.ArrayList;
import java.util.List;

public class BareMetalExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BareMetalExample() {
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

        public Criteria andHostnameIsNull() {
            addCriterion("hostname is null");
            return (Criteria) this;
        }

        public Criteria andHostnameIsNotNull() {
            addCriterion("hostname is not null");
            return (Criteria) this;
        }

        public Criteria andHostnameEqualTo(String value) {
            addCriterion("hostname =", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameNotEqualTo(String value) {
            addCriterion("hostname <>", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameGreaterThan(String value) {
            addCriterion("hostname >", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameGreaterThanOrEqualTo(String value) {
            addCriterion("hostname >=", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameLessThan(String value) {
            addCriterion("hostname <", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameLessThanOrEqualTo(String value) {
            addCriterion("hostname <=", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameLike(String value) {
            addCriterion("hostname like", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameNotLike(String value) {
            addCriterion("hostname not like", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameIn(List<String> values) {
            addCriterion("hostname in", values, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameNotIn(List<String> values) {
            addCriterion("hostname not in", values, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameBetween(String value1, String value2) {
            addCriterion("hostname between", value1, value2, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameNotBetween(String value1, String value2) {
            addCriterion("hostname not between", value1, value2, "hostname");
            return (Criteria) this;
        }

        public Criteria andMachineTypeIsNull() {
            addCriterion("machine_type is null");
            return (Criteria) this;
        }

        public Criteria andMachineTypeIsNotNull() {
            addCriterion("machine_type is not null");
            return (Criteria) this;
        }

        public Criteria andMachineTypeEqualTo(String value) {
            addCriterion("machine_type =", value, "machineType");
            return (Criteria) this;
        }

        public Criteria andMachineTypeNotEqualTo(String value) {
            addCriterion("machine_type <>", value, "machineType");
            return (Criteria) this;
        }

        public Criteria andMachineTypeGreaterThan(String value) {
            addCriterion("machine_type >", value, "machineType");
            return (Criteria) this;
        }

        public Criteria andMachineTypeGreaterThanOrEqualTo(String value) {
            addCriterion("machine_type >=", value, "machineType");
            return (Criteria) this;
        }

        public Criteria andMachineTypeLessThan(String value) {
            addCriterion("machine_type <", value, "machineType");
            return (Criteria) this;
        }

        public Criteria andMachineTypeLessThanOrEqualTo(String value) {
            addCriterion("machine_type <=", value, "machineType");
            return (Criteria) this;
        }

        public Criteria andMachineTypeLike(String value) {
            addCriterion("machine_type like", value, "machineType");
            return (Criteria) this;
        }

        public Criteria andMachineTypeNotLike(String value) {
            addCriterion("machine_type not like", value, "machineType");
            return (Criteria) this;
        }

        public Criteria andMachineTypeIn(List<String> values) {
            addCriterion("machine_type in", values, "machineType");
            return (Criteria) this;
        }

        public Criteria andMachineTypeNotIn(List<String> values) {
            addCriterion("machine_type not in", values, "machineType");
            return (Criteria) this;
        }

        public Criteria andMachineTypeBetween(String value1, String value2) {
            addCriterion("machine_type between", value1, value2, "machineType");
            return (Criteria) this;
        }

        public Criteria andMachineTypeNotBetween(String value1, String value2) {
            addCriterion("machine_type not between", value1, value2, "machineType");
            return (Criteria) this;
        }

        public Criteria andCpuIsNull() {
            addCriterion("cpu is null");
            return (Criteria) this;
        }

        public Criteria andCpuIsNotNull() {
            addCriterion("cpu is not null");
            return (Criteria) this;
        }

        public Criteria andCpuEqualTo(Integer value) {
            addCriterion("cpu =", value, "cpu");
            return (Criteria) this;
        }

        public Criteria andCpuNotEqualTo(Integer value) {
            addCriterion("cpu <>", value, "cpu");
            return (Criteria) this;
        }

        public Criteria andCpuGreaterThan(Integer value) {
            addCriterion("cpu >", value, "cpu");
            return (Criteria) this;
        }

        public Criteria andCpuGreaterThanOrEqualTo(Integer value) {
            addCriterion("cpu >=", value, "cpu");
            return (Criteria) this;
        }

        public Criteria andCpuLessThan(Integer value) {
            addCriterion("cpu <", value, "cpu");
            return (Criteria) this;
        }

        public Criteria andCpuLessThanOrEqualTo(Integer value) {
            addCriterion("cpu <=", value, "cpu");
            return (Criteria) this;
        }

        public Criteria andCpuIn(List<Integer> values) {
            addCriterion("cpu in", values, "cpu");
            return (Criteria) this;
        }

        public Criteria andCpuNotIn(List<Integer> values) {
            addCriterion("cpu not in", values, "cpu");
            return (Criteria) this;
        }

        public Criteria andCpuBetween(Integer value1, Integer value2) {
            addCriterion("cpu between", value1, value2, "cpu");
            return (Criteria) this;
        }

        public Criteria andCpuNotBetween(Integer value1, Integer value2) {
            addCriterion("cpu not between", value1, value2, "cpu");
            return (Criteria) this;
        }

        public Criteria andCpuTypeIsNull() {
            addCriterion("cpu_type is null");
            return (Criteria) this;
        }

        public Criteria andCpuTypeIsNotNull() {
            addCriterion("cpu_type is not null");
            return (Criteria) this;
        }

        public Criteria andCpuTypeEqualTo(String value) {
            addCriterion("cpu_type =", value, "cpuType");
            return (Criteria) this;
        }

        public Criteria andCpuTypeNotEqualTo(String value) {
            addCriterion("cpu_type <>", value, "cpuType");
            return (Criteria) this;
        }

        public Criteria andCpuTypeGreaterThan(String value) {
            addCriterion("cpu_type >", value, "cpuType");
            return (Criteria) this;
        }

        public Criteria andCpuTypeGreaterThanOrEqualTo(String value) {
            addCriterion("cpu_type >=", value, "cpuType");
            return (Criteria) this;
        }

        public Criteria andCpuTypeLessThan(String value) {
            addCriterion("cpu_type <", value, "cpuType");
            return (Criteria) this;
        }

        public Criteria andCpuTypeLessThanOrEqualTo(String value) {
            addCriterion("cpu_type <=", value, "cpuType");
            return (Criteria) this;
        }

        public Criteria andCpuTypeLike(String value) {
            addCriterion("cpu_type like", value, "cpuType");
            return (Criteria) this;
        }

        public Criteria andCpuTypeNotLike(String value) {
            addCriterion("cpu_type not like", value, "cpuType");
            return (Criteria) this;
        }

        public Criteria andCpuTypeIn(List<String> values) {
            addCriterion("cpu_type in", values, "cpuType");
            return (Criteria) this;
        }

        public Criteria andCpuTypeNotIn(List<String> values) {
            addCriterion("cpu_type not in", values, "cpuType");
            return (Criteria) this;
        }

        public Criteria andCpuTypeBetween(String value1, String value2) {
            addCriterion("cpu_type between", value1, value2, "cpuType");
            return (Criteria) this;
        }

        public Criteria andCpuTypeNotBetween(String value1, String value2) {
            addCriterion("cpu_type not between", value1, value2, "cpuType");
            return (Criteria) this;
        }

        public Criteria andCpuFreIsNull() {
            addCriterion("cpu_fre is null");
            return (Criteria) this;
        }

        public Criteria andCpuFreIsNotNull() {
            addCriterion("cpu_fre is not null");
            return (Criteria) this;
        }

        public Criteria andCpuFreEqualTo(String value) {
            addCriterion("cpu_fre =", value, "cpuFre");
            return (Criteria) this;
        }

        public Criteria andCpuFreNotEqualTo(String value) {
            addCriterion("cpu_fre <>", value, "cpuFre");
            return (Criteria) this;
        }

        public Criteria andCpuFreGreaterThan(String value) {
            addCriterion("cpu_fre >", value, "cpuFre");
            return (Criteria) this;
        }

        public Criteria andCpuFreGreaterThanOrEqualTo(String value) {
            addCriterion("cpu_fre >=", value, "cpuFre");
            return (Criteria) this;
        }

        public Criteria andCpuFreLessThan(String value) {
            addCriterion("cpu_fre <", value, "cpuFre");
            return (Criteria) this;
        }

        public Criteria andCpuFreLessThanOrEqualTo(String value) {
            addCriterion("cpu_fre <=", value, "cpuFre");
            return (Criteria) this;
        }

        public Criteria andCpuFreLike(String value) {
            addCriterion("cpu_fre like", value, "cpuFre");
            return (Criteria) this;
        }

        public Criteria andCpuFreNotLike(String value) {
            addCriterion("cpu_fre not like", value, "cpuFre");
            return (Criteria) this;
        }

        public Criteria andCpuFreIn(List<String> values) {
            addCriterion("cpu_fre in", values, "cpuFre");
            return (Criteria) this;
        }

        public Criteria andCpuFreNotIn(List<String> values) {
            addCriterion("cpu_fre not in", values, "cpuFre");
            return (Criteria) this;
        }

        public Criteria andCpuFreBetween(String value1, String value2) {
            addCriterion("cpu_fre between", value1, value2, "cpuFre");
            return (Criteria) this;
        }

        public Criteria andCpuFreNotBetween(String value1, String value2) {
            addCriterion("cpu_fre not between", value1, value2, "cpuFre");
            return (Criteria) this;
        }

        public Criteria andCoreIsNull() {
            addCriterion("core is null");
            return (Criteria) this;
        }

        public Criteria andCoreIsNotNull() {
            addCriterion("core is not null");
            return (Criteria) this;
        }

        public Criteria andCoreEqualTo(Integer value) {
            addCriterion("core =", value, "core");
            return (Criteria) this;
        }

        public Criteria andCoreNotEqualTo(Integer value) {
            addCriterion("core <>", value, "core");
            return (Criteria) this;
        }

        public Criteria andCoreGreaterThan(Integer value) {
            addCriterion("core >", value, "core");
            return (Criteria) this;
        }

        public Criteria andCoreGreaterThanOrEqualTo(Integer value) {
            addCriterion("core >=", value, "core");
            return (Criteria) this;
        }

        public Criteria andCoreLessThan(Integer value) {
            addCriterion("core <", value, "core");
            return (Criteria) this;
        }

        public Criteria andCoreLessThanOrEqualTo(Integer value) {
            addCriterion("core <=", value, "core");
            return (Criteria) this;
        }

        public Criteria andCoreIn(List<Integer> values) {
            addCriterion("core in", values, "core");
            return (Criteria) this;
        }

        public Criteria andCoreNotIn(List<Integer> values) {
            addCriterion("core not in", values, "core");
            return (Criteria) this;
        }

        public Criteria andCoreBetween(Integer value1, Integer value2) {
            addCriterion("core between", value1, value2, "core");
            return (Criteria) this;
        }

        public Criteria andCoreNotBetween(Integer value1, Integer value2) {
            addCriterion("core not between", value1, value2, "core");
            return (Criteria) this;
        }

        public Criteria andThreadIsNull() {
            addCriterion("thread is null");
            return (Criteria) this;
        }

        public Criteria andThreadIsNotNull() {
            addCriterion("thread is not null");
            return (Criteria) this;
        }

        public Criteria andThreadEqualTo(Integer value) {
            addCriterion("thread =", value, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadNotEqualTo(Integer value) {
            addCriterion("thread <>", value, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadGreaterThan(Integer value) {
            addCriterion("thread >", value, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadGreaterThanOrEqualTo(Integer value) {
            addCriterion("thread >=", value, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadLessThan(Integer value) {
            addCriterion("thread <", value, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadLessThanOrEqualTo(Integer value) {
            addCriterion("thread <=", value, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadIn(List<Integer> values) {
            addCriterion("thread in", values, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadNotIn(List<Integer> values) {
            addCriterion("thread not in", values, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadBetween(Integer value1, Integer value2) {
            addCriterion("thread between", value1, value2, "thread");
            return (Criteria) this;
        }

        public Criteria andThreadNotBetween(Integer value1, Integer value2) {
            addCriterion("thread not between", value1, value2, "thread");
            return (Criteria) this;
        }

        public Criteria andMemoryIsNull() {
            addCriterion("memory is null");
            return (Criteria) this;
        }

        public Criteria andMemoryIsNotNull() {
            addCriterion("memory is not null");
            return (Criteria) this;
        }

        public Criteria andMemoryEqualTo(Integer value) {
            addCriterion("memory =", value, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryNotEqualTo(Integer value) {
            addCriterion("memory <>", value, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryGreaterThan(Integer value) {
            addCriterion("memory >", value, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryGreaterThanOrEqualTo(Integer value) {
            addCriterion("memory >=", value, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryLessThan(Integer value) {
            addCriterion("memory <", value, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryLessThanOrEqualTo(Integer value) {
            addCriterion("memory <=", value, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryIn(List<Integer> values) {
            addCriterion("memory in", values, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryNotIn(List<Integer> values) {
            addCriterion("memory not in", values, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryBetween(Integer value1, Integer value2) {
            addCriterion("memory between", value1, value2, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryNotBetween(Integer value1, Integer value2) {
            addCriterion("memory not between", value1, value2, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeIsNull() {
            addCriterion("memory_type is null");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeIsNotNull() {
            addCriterion("memory_type is not null");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeEqualTo(String value) {
            addCriterion("memory_type =", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeNotEqualTo(String value) {
            addCriterion("memory_type <>", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeGreaterThan(String value) {
            addCriterion("memory_type >", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeGreaterThanOrEqualTo(String value) {
            addCriterion("memory_type >=", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeLessThan(String value) {
            addCriterion("memory_type <", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeLessThanOrEqualTo(String value) {
            addCriterion("memory_type <=", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeLike(String value) {
            addCriterion("memory_type like", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeNotLike(String value) {
            addCriterion("memory_type not like", value, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeIn(List<String> values) {
            addCriterion("memory_type in", values, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeNotIn(List<String> values) {
            addCriterion("memory_type not in", values, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeBetween(String value1, String value2) {
            addCriterion("memory_type between", value1, value2, "memoryType");
            return (Criteria) this;
        }

        public Criteria andMemoryTypeNotBetween(String value1, String value2) {
            addCriterion("memory_type not between", value1, value2, "memoryType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeIsNull() {
            addCriterion("disk_type is null");
            return (Criteria) this;
        }

        public Criteria andDiskTypeIsNotNull() {
            addCriterion("disk_type is not null");
            return (Criteria) this;
        }

        public Criteria andDiskTypeEqualTo(String value) {
            addCriterion("disk_type =", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeNotEqualTo(String value) {
            addCriterion("disk_type <>", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeGreaterThan(String value) {
            addCriterion("disk_type >", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeGreaterThanOrEqualTo(String value) {
            addCriterion("disk_type >=", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeLessThan(String value) {
            addCriterion("disk_type <", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeLessThanOrEqualTo(String value) {
            addCriterion("disk_type <=", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeLike(String value) {
            addCriterion("disk_type like", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeNotLike(String value) {
            addCriterion("disk_type not like", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeIn(List<String> values) {
            addCriterion("disk_type in", values, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeNotIn(List<String> values) {
            addCriterion("disk_type not in", values, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeBetween(String value1, String value2) {
            addCriterion("disk_type between", value1, value2, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeNotBetween(String value1, String value2) {
            addCriterion("disk_type not between", value1, value2, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskIsNull() {
            addCriterion("disk is null");
            return (Criteria) this;
        }

        public Criteria andDiskIsNotNull() {
            addCriterion("disk is not null");
            return (Criteria) this;
        }

        public Criteria andDiskEqualTo(Integer value) {
            addCriterion("disk =", value, "disk");
            return (Criteria) this;
        }

        public Criteria andDiskNotEqualTo(Integer value) {
            addCriterion("disk <>", value, "disk");
            return (Criteria) this;
        }

        public Criteria andDiskGreaterThan(Integer value) {
            addCriterion("disk >", value, "disk");
            return (Criteria) this;
        }

        public Criteria andDiskGreaterThanOrEqualTo(Integer value) {
            addCriterion("disk >=", value, "disk");
            return (Criteria) this;
        }

        public Criteria andDiskLessThan(Integer value) {
            addCriterion("disk <", value, "disk");
            return (Criteria) this;
        }

        public Criteria andDiskLessThanOrEqualTo(Integer value) {
            addCriterion("disk <=", value, "disk");
            return (Criteria) this;
        }

        public Criteria andDiskIn(List<Integer> values) {
            addCriterion("disk in", values, "disk");
            return (Criteria) this;
        }

        public Criteria andDiskNotIn(List<Integer> values) {
            addCriterion("disk not in", values, "disk");
            return (Criteria) this;
        }

        public Criteria andDiskBetween(Integer value1, Integer value2) {
            addCriterion("disk between", value1, value2, "disk");
            return (Criteria) this;
        }

        public Criteria andDiskNotBetween(Integer value1, Integer value2) {
            addCriterion("disk not between", value1, value2, "disk");
            return (Criteria) this;
        }

        public Criteria andManagementIpIsNull() {
            addCriterion("management_ip is null");
            return (Criteria) this;
        }

        public Criteria andManagementIpIsNotNull() {
            addCriterion("management_ip is not null");
            return (Criteria) this;
        }

        public Criteria andManagementIpEqualTo(String value) {
            addCriterion("management_ip =", value, "managementIp");
            return (Criteria) this;
        }

        public Criteria andManagementIpNotEqualTo(String value) {
            addCriterion("management_ip <>", value, "managementIp");
            return (Criteria) this;
        }

        public Criteria andManagementIpGreaterThan(String value) {
            addCriterion("management_ip >", value, "managementIp");
            return (Criteria) this;
        }

        public Criteria andManagementIpGreaterThanOrEqualTo(String value) {
            addCriterion("management_ip >=", value, "managementIp");
            return (Criteria) this;
        }

        public Criteria andManagementIpLessThan(String value) {
            addCriterion("management_ip <", value, "managementIp");
            return (Criteria) this;
        }

        public Criteria andManagementIpLessThanOrEqualTo(String value) {
            addCriterion("management_ip <=", value, "managementIp");
            return (Criteria) this;
        }

        public Criteria andManagementIpLike(String value) {
            addCriterion("management_ip like", value, "managementIp");
            return (Criteria) this;
        }

        public Criteria andManagementIpNotLike(String value) {
            addCriterion("management_ip not like", value, "managementIp");
            return (Criteria) this;
        }

        public Criteria andManagementIpIn(List<String> values) {
            addCriterion("management_ip in", values, "managementIp");
            return (Criteria) this;
        }

        public Criteria andManagementIpNotIn(List<String> values) {
            addCriterion("management_ip not in", values, "managementIp");
            return (Criteria) this;
        }

        public Criteria andManagementIpBetween(String value1, String value2) {
            addCriterion("management_ip between", value1, value2, "managementIp");
            return (Criteria) this;
        }

        public Criteria andManagementIpNotBetween(String value1, String value2) {
            addCriterion("management_ip not between", value1, value2, "managementIp");
            return (Criteria) this;
        }

        public Criteria andBmcMacIsNull() {
            addCriterion("bmc_mac is null");
            return (Criteria) this;
        }

        public Criteria andBmcMacIsNotNull() {
            addCriterion("bmc_mac is not null");
            return (Criteria) this;
        }

        public Criteria andBmcMacEqualTo(String value) {
            addCriterion("bmc_mac =", value, "bmcMac");
            return (Criteria) this;
        }

        public Criteria andBmcMacNotEqualTo(String value) {
            addCriterion("bmc_mac <>", value, "bmcMac");
            return (Criteria) this;
        }

        public Criteria andBmcMacGreaterThan(String value) {
            addCriterion("bmc_mac >", value, "bmcMac");
            return (Criteria) this;
        }

        public Criteria andBmcMacGreaterThanOrEqualTo(String value) {
            addCriterion("bmc_mac >=", value, "bmcMac");
            return (Criteria) this;
        }

        public Criteria andBmcMacLessThan(String value) {
            addCriterion("bmc_mac <", value, "bmcMac");
            return (Criteria) this;
        }

        public Criteria andBmcMacLessThanOrEqualTo(String value) {
            addCriterion("bmc_mac <=", value, "bmcMac");
            return (Criteria) this;
        }

        public Criteria andBmcMacLike(String value) {
            addCriterion("bmc_mac like", value, "bmcMac");
            return (Criteria) this;
        }

        public Criteria andBmcMacNotLike(String value) {
            addCriterion("bmc_mac not like", value, "bmcMac");
            return (Criteria) this;
        }

        public Criteria andBmcMacIn(List<String> values) {
            addCriterion("bmc_mac in", values, "bmcMac");
            return (Criteria) this;
        }

        public Criteria andBmcMacNotIn(List<String> values) {
            addCriterion("bmc_mac not in", values, "bmcMac");
            return (Criteria) this;
        }

        public Criteria andBmcMacBetween(String value1, String value2) {
            addCriterion("bmc_mac between", value1, value2, "bmcMac");
            return (Criteria) this;
        }

        public Criteria andBmcMacNotBetween(String value1, String value2) {
            addCriterion("bmc_mac not between", value1, value2, "bmcMac");
            return (Criteria) this;
        }

        public Criteria andIpArrayIsNull() {
            addCriterion("ip_array is null");
            return (Criteria) this;
        }

        public Criteria andIpArrayIsNotNull() {
            addCriterion("ip_array is not null");
            return (Criteria) this;
        }

        public Criteria andIpArrayEqualTo(String value) {
            addCriterion("ip_array =", value, "ipArray");
            return (Criteria) this;
        }

        public Criteria andIpArrayNotEqualTo(String value) {
            addCriterion("ip_array <>", value, "ipArray");
            return (Criteria) this;
        }

        public Criteria andIpArrayGreaterThan(String value) {
            addCriterion("ip_array >", value, "ipArray");
            return (Criteria) this;
        }

        public Criteria andIpArrayGreaterThanOrEqualTo(String value) {
            addCriterion("ip_array >=", value, "ipArray");
            return (Criteria) this;
        }

        public Criteria andIpArrayLessThan(String value) {
            addCriterion("ip_array <", value, "ipArray");
            return (Criteria) this;
        }

        public Criteria andIpArrayLessThanOrEqualTo(String value) {
            addCriterion("ip_array <=", value, "ipArray");
            return (Criteria) this;
        }

        public Criteria andIpArrayLike(String value) {
            addCriterion("ip_array like", value, "ipArray");
            return (Criteria) this;
        }

        public Criteria andIpArrayNotLike(String value) {
            addCriterion("ip_array not like", value, "ipArray");
            return (Criteria) this;
        }

        public Criteria andIpArrayIn(List<String> values) {
            addCriterion("ip_array in", values, "ipArray");
            return (Criteria) this;
        }

        public Criteria andIpArrayNotIn(List<String> values) {
            addCriterion("ip_array not in", values, "ipArray");
            return (Criteria) this;
        }

        public Criteria andIpArrayBetween(String value1, String value2) {
            addCriterion("ip_array between", value1, value2, "ipArray");
            return (Criteria) this;
        }

        public Criteria andIpArrayNotBetween(String value1, String value2) {
            addCriterion("ip_array not between", value1, value2, "ipArray");
            return (Criteria) this;
        }

        public Criteria andOsTypeIsNull() {
            addCriterion("os_type is null");
            return (Criteria) this;
        }

        public Criteria andOsTypeIsNotNull() {
            addCriterion("os_type is not null");
            return (Criteria) this;
        }

        public Criteria andOsTypeEqualTo(String value) {
            addCriterion("os_type =", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeNotEqualTo(String value) {
            addCriterion("os_type <>", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeGreaterThan(String value) {
            addCriterion("os_type >", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeGreaterThanOrEqualTo(String value) {
            addCriterion("os_type >=", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeLessThan(String value) {
            addCriterion("os_type <", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeLessThanOrEqualTo(String value) {
            addCriterion("os_type <=", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeLike(String value) {
            addCriterion("os_type like", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeNotLike(String value) {
            addCriterion("os_type not like", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeIn(List<String> values) {
            addCriterion("os_type in", values, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeNotIn(List<String> values) {
            addCriterion("os_type not in", values, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeBetween(String value1, String value2) {
            addCriterion("os_type between", value1, value2, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeNotBetween(String value1, String value2) {
            addCriterion("os_type not between", value1, value2, "osType");
            return (Criteria) this;
        }

        public Criteria andOsVersionIsNull() {
            addCriterion("os_version is null");
            return (Criteria) this;
        }

        public Criteria andOsVersionIsNotNull() {
            addCriterion("os_version is not null");
            return (Criteria) this;
        }

        public Criteria andOsVersionEqualTo(String value) {
            addCriterion("os_version =", value, "osVersion");
            return (Criteria) this;
        }

        public Criteria andOsVersionNotEqualTo(String value) {
            addCriterion("os_version <>", value, "osVersion");
            return (Criteria) this;
        }

        public Criteria andOsVersionGreaterThan(String value) {
            addCriterion("os_version >", value, "osVersion");
            return (Criteria) this;
        }

        public Criteria andOsVersionGreaterThanOrEqualTo(String value) {
            addCriterion("os_version >=", value, "osVersion");
            return (Criteria) this;
        }

        public Criteria andOsVersionLessThan(String value) {
            addCriterion("os_version <", value, "osVersion");
            return (Criteria) this;
        }

        public Criteria andOsVersionLessThanOrEqualTo(String value) {
            addCriterion("os_version <=", value, "osVersion");
            return (Criteria) this;
        }

        public Criteria andOsVersionLike(String value) {
            addCriterion("os_version like", value, "osVersion");
            return (Criteria) this;
        }

        public Criteria andOsVersionNotLike(String value) {
            addCriterion("os_version not like", value, "osVersion");
            return (Criteria) this;
        }

        public Criteria andOsVersionIn(List<String> values) {
            addCriterion("os_version in", values, "osVersion");
            return (Criteria) this;
        }

        public Criteria andOsVersionNotIn(List<String> values) {
            addCriterion("os_version not in", values, "osVersion");
            return (Criteria) this;
        }

        public Criteria andOsVersionBetween(String value1, String value2) {
            addCriterion("os_version between", value1, value2, "osVersion");
            return (Criteria) this;
        }

        public Criteria andOsVersionNotBetween(String value1, String value2) {
            addCriterion("os_version not between", value1, value2, "osVersion");
            return (Criteria) this;
        }

        public Criteria andMachineBrandIsNull() {
            addCriterion("machine_brand is null");
            return (Criteria) this;
        }

        public Criteria andMachineBrandIsNotNull() {
            addCriterion("machine_brand is not null");
            return (Criteria) this;
        }

        public Criteria andMachineBrandEqualTo(String value) {
            addCriterion("machine_brand =", value, "machineBrand");
            return (Criteria) this;
        }

        public Criteria andMachineBrandNotEqualTo(String value) {
            addCriterion("machine_brand <>", value, "machineBrand");
            return (Criteria) this;
        }

        public Criteria andMachineBrandGreaterThan(String value) {
            addCriterion("machine_brand >", value, "machineBrand");
            return (Criteria) this;
        }

        public Criteria andMachineBrandGreaterThanOrEqualTo(String value) {
            addCriterion("machine_brand >=", value, "machineBrand");
            return (Criteria) this;
        }

        public Criteria andMachineBrandLessThan(String value) {
            addCriterion("machine_brand <", value, "machineBrand");
            return (Criteria) this;
        }

        public Criteria andMachineBrandLessThanOrEqualTo(String value) {
            addCriterion("machine_brand <=", value, "machineBrand");
            return (Criteria) this;
        }

        public Criteria andMachineBrandLike(String value) {
            addCriterion("machine_brand like", value, "machineBrand");
            return (Criteria) this;
        }

        public Criteria andMachineBrandNotLike(String value) {
            addCriterion("machine_brand not like", value, "machineBrand");
            return (Criteria) this;
        }

        public Criteria andMachineBrandIn(List<String> values) {
            addCriterion("machine_brand in", values, "machineBrand");
            return (Criteria) this;
        }

        public Criteria andMachineBrandNotIn(List<String> values) {
            addCriterion("machine_brand not in", values, "machineBrand");
            return (Criteria) this;
        }

        public Criteria andMachineBrandBetween(String value1, String value2) {
            addCriterion("machine_brand between", value1, value2, "machineBrand");
            return (Criteria) this;
        }

        public Criteria andMachineBrandNotBetween(String value1, String value2) {
            addCriterion("machine_brand not between", value1, value2, "machineBrand");
            return (Criteria) this;
        }

        public Criteria andMachineModelIsNull() {
            addCriterion("machine_model is null");
            return (Criteria) this;
        }

        public Criteria andMachineModelIsNotNull() {
            addCriterion("machine_model is not null");
            return (Criteria) this;
        }

        public Criteria andMachineModelEqualTo(String value) {
            addCriterion("machine_model =", value, "machineModel");
            return (Criteria) this;
        }

        public Criteria andMachineModelNotEqualTo(String value) {
            addCriterion("machine_model <>", value, "machineModel");
            return (Criteria) this;
        }

        public Criteria andMachineModelGreaterThan(String value) {
            addCriterion("machine_model >", value, "machineModel");
            return (Criteria) this;
        }

        public Criteria andMachineModelGreaterThanOrEqualTo(String value) {
            addCriterion("machine_model >=", value, "machineModel");
            return (Criteria) this;
        }

        public Criteria andMachineModelLessThan(String value) {
            addCriterion("machine_model <", value, "machineModel");
            return (Criteria) this;
        }

        public Criteria andMachineModelLessThanOrEqualTo(String value) {
            addCriterion("machine_model <=", value, "machineModel");
            return (Criteria) this;
        }

        public Criteria andMachineModelLike(String value) {
            addCriterion("machine_model like", value, "machineModel");
            return (Criteria) this;
        }

        public Criteria andMachineModelNotLike(String value) {
            addCriterion("machine_model not like", value, "machineModel");
            return (Criteria) this;
        }

        public Criteria andMachineModelIn(List<String> values) {
            addCriterion("machine_model in", values, "machineModel");
            return (Criteria) this;
        }

        public Criteria andMachineModelNotIn(List<String> values) {
            addCriterion("machine_model not in", values, "machineModel");
            return (Criteria) this;
        }

        public Criteria andMachineModelBetween(String value1, String value2) {
            addCriterion("machine_model between", value1, value2, "machineModel");
            return (Criteria) this;
        }

        public Criteria andMachineModelNotBetween(String value1, String value2) {
            addCriterion("machine_model not between", value1, value2, "machineModel");
            return (Criteria) this;
        }

        public Criteria andServerIdIsNull() {
            addCriterion("server_id is null");
            return (Criteria) this;
        }

        public Criteria andServerIdIsNotNull() {
            addCriterion("server_id is not null");
            return (Criteria) this;
        }

        public Criteria andServerIdEqualTo(String value) {
            addCriterion("server_id =", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotEqualTo(String value) {
            addCriterion("server_id <>", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdGreaterThan(String value) {
            addCriterion("server_id >", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdGreaterThanOrEqualTo(String value) {
            addCriterion("server_id >=", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdLessThan(String value) {
            addCriterion("server_id <", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdLessThanOrEqualTo(String value) {
            addCriterion("server_id <=", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdLike(String value) {
            addCriterion("server_id like", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotLike(String value) {
            addCriterion("server_id not like", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdIn(List<String> values) {
            addCriterion("server_id in", values, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotIn(List<String> values) {
            addCriterion("server_id not in", values, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdBetween(String value1, String value2) {
            addCriterion("server_id between", value1, value2, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotBetween(String value1, String value2) {
            addCriterion("server_id not between", value1, value2, "serverId");
            return (Criteria) this;
        }

        public Criteria andMachineSnIsNull() {
            addCriterion("machine_sn is null");
            return (Criteria) this;
        }

        public Criteria andMachineSnIsNotNull() {
            addCriterion("machine_sn is not null");
            return (Criteria) this;
        }

        public Criteria andMachineSnEqualTo(String value) {
            addCriterion("machine_sn =", value, "machineSn");
            return (Criteria) this;
        }

        public Criteria andMachineSnNotEqualTo(String value) {
            addCriterion("machine_sn <>", value, "machineSn");
            return (Criteria) this;
        }

        public Criteria andMachineSnGreaterThan(String value) {
            addCriterion("machine_sn >", value, "machineSn");
            return (Criteria) this;
        }

        public Criteria andMachineSnGreaterThanOrEqualTo(String value) {
            addCriterion("machine_sn >=", value, "machineSn");
            return (Criteria) this;
        }

        public Criteria andMachineSnLessThan(String value) {
            addCriterion("machine_sn <", value, "machineSn");
            return (Criteria) this;
        }

        public Criteria andMachineSnLessThanOrEqualTo(String value) {
            addCriterion("machine_sn <=", value, "machineSn");
            return (Criteria) this;
        }

        public Criteria andMachineSnLike(String value) {
            addCriterion("machine_sn like", value, "machineSn");
            return (Criteria) this;
        }

        public Criteria andMachineSnNotLike(String value) {
            addCriterion("machine_sn not like", value, "machineSn");
            return (Criteria) this;
        }

        public Criteria andMachineSnIn(List<String> values) {
            addCriterion("machine_sn in", values, "machineSn");
            return (Criteria) this;
        }

        public Criteria andMachineSnNotIn(List<String> values) {
            addCriterion("machine_sn not in", values, "machineSn");
            return (Criteria) this;
        }

        public Criteria andMachineSnBetween(String value1, String value2) {
            addCriterion("machine_sn between", value1, value2, "machineSn");
            return (Criteria) this;
        }

        public Criteria andMachineSnNotBetween(String value1, String value2) {
            addCriterion("machine_sn not between", value1, value2, "machineSn");
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

        public Criteria andPowerIsNull() {
            addCriterion("power is null");
            return (Criteria) this;
        }

        public Criteria andPowerIsNotNull() {
            addCriterion("power is not null");
            return (Criteria) this;
        }

        public Criteria andPowerEqualTo(String value) {
            addCriterion("power =", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerNotEqualTo(String value) {
            addCriterion("power <>", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerGreaterThan(String value) {
            addCriterion("power >", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerGreaterThanOrEqualTo(String value) {
            addCriterion("power >=", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerLessThan(String value) {
            addCriterion("power <", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerLessThanOrEqualTo(String value) {
            addCriterion("power <=", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerLike(String value) {
            addCriterion("power like", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerNotLike(String value) {
            addCriterion("power not like", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerIn(List<String> values) {
            addCriterion("power in", values, "power");
            return (Criteria) this;
        }

        public Criteria andPowerNotIn(List<String> values) {
            addCriterion("power not in", values, "power");
            return (Criteria) this;
        }

        public Criteria andPowerBetween(String value1, String value2) {
            addCriterion("power between", value1, value2, "power");
            return (Criteria) this;
        }

        public Criteria andPowerNotBetween(String value1, String value2) {
            addCriterion("power not between", value1, value2, "power");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdIsNull() {
            addCriterion("workspace_id is null");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdIsNotNull() {
            addCriterion("workspace_id is not null");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdEqualTo(String value) {
            addCriterion("workspace_id =", value, "workspaceId");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdNotEqualTo(String value) {
            addCriterion("workspace_id <>", value, "workspaceId");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdGreaterThan(String value) {
            addCriterion("workspace_id >", value, "workspaceId");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdGreaterThanOrEqualTo(String value) {
            addCriterion("workspace_id >=", value, "workspaceId");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdLessThan(String value) {
            addCriterion("workspace_id <", value, "workspaceId");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdLessThanOrEqualTo(String value) {
            addCriterion("workspace_id <=", value, "workspaceId");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdLike(String value) {
            addCriterion("workspace_id like", value, "workspaceId");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdNotLike(String value) {
            addCriterion("workspace_id not like", value, "workspaceId");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdIn(List<String> values) {
            addCriterion("workspace_id in", values, "workspaceId");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdNotIn(List<String> values) {
            addCriterion("workspace_id not in", values, "workspaceId");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdBetween(String value1, String value2) {
            addCriterion("workspace_id between", value1, value2, "workspaceId");
            return (Criteria) this;
        }

        public Criteria andWorkspaceIdNotBetween(String value1, String value2) {
            addCriterion("workspace_id not between", value1, value2, "workspaceId");
            return (Criteria) this;
        }

        public Criteria andRecycledTimeIsNull() {
            addCriterion("recycled_time is null");
            return (Criteria) this;
        }

        public Criteria andRecycledTimeIsNotNull() {
            addCriterion("recycled_time is not null");
            return (Criteria) this;
        }

        public Criteria andRecycledTimeEqualTo(Long value) {
            addCriterion("recycled_time =", value, "recycledTime");
            return (Criteria) this;
        }

        public Criteria andRecycledTimeNotEqualTo(Long value) {
            addCriterion("recycled_time <>", value, "recycledTime");
            return (Criteria) this;
        }

        public Criteria andRecycledTimeGreaterThan(Long value) {
            addCriterion("recycled_time >", value, "recycledTime");
            return (Criteria) this;
        }

        public Criteria andRecycledTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("recycled_time >=", value, "recycledTime");
            return (Criteria) this;
        }

        public Criteria andRecycledTimeLessThan(Long value) {
            addCriterion("recycled_time <", value, "recycledTime");
            return (Criteria) this;
        }

        public Criteria andRecycledTimeLessThanOrEqualTo(Long value) {
            addCriterion("recycled_time <=", value, "recycledTime");
            return (Criteria) this;
        }

        public Criteria andRecycledTimeIn(List<Long> values) {
            addCriterion("recycled_time in", values, "recycledTime");
            return (Criteria) this;
        }

        public Criteria andRecycledTimeNotIn(List<Long> values) {
            addCriterion("recycled_time not in", values, "recycledTime");
            return (Criteria) this;
        }

        public Criteria andRecycledTimeBetween(Long value1, Long value2) {
            addCriterion("recycled_time between", value1, value2, "recycledTime");
            return (Criteria) this;
        }

        public Criteria andRecycledTimeNotBetween(Long value1, Long value2) {
            addCriterion("recycled_time not between", value1, value2, "recycledTime");
            return (Criteria) this;
        }

        public Criteria andSshUserIsNull() {
            addCriterion("ssh_user is null");
            return (Criteria) this;
        }

        public Criteria andSshUserIsNotNull() {
            addCriterion("ssh_user is not null");
            return (Criteria) this;
        }

        public Criteria andSshUserEqualTo(String value) {
            addCriterion("ssh_user =", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserNotEqualTo(String value) {
            addCriterion("ssh_user <>", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserGreaterThan(String value) {
            addCriterion("ssh_user >", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserGreaterThanOrEqualTo(String value) {
            addCriterion("ssh_user >=", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserLessThan(String value) {
            addCriterion("ssh_user <", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserLessThanOrEqualTo(String value) {
            addCriterion("ssh_user <=", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserLike(String value) {
            addCriterion("ssh_user like", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserNotLike(String value) {
            addCriterion("ssh_user not like", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserIn(List<String> values) {
            addCriterion("ssh_user in", values, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserNotIn(List<String> values) {
            addCriterion("ssh_user not in", values, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserBetween(String value1, String value2) {
            addCriterion("ssh_user between", value1, value2, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserNotBetween(String value1, String value2) {
            addCriterion("ssh_user not between", value1, value2, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshPwdIsNull() {
            addCriterion("ssh_pwd is null");
            return (Criteria) this;
        }

        public Criteria andSshPwdIsNotNull() {
            addCriterion("ssh_pwd is not null");
            return (Criteria) this;
        }

        public Criteria andSshPwdEqualTo(String value) {
            addCriterion("ssh_pwd =", value, "sshPwd");
            return (Criteria) this;
        }

        public Criteria andSshPwdNotEqualTo(String value) {
            addCriterion("ssh_pwd <>", value, "sshPwd");
            return (Criteria) this;
        }

        public Criteria andSshPwdGreaterThan(String value) {
            addCriterion("ssh_pwd >", value, "sshPwd");
            return (Criteria) this;
        }

        public Criteria andSshPwdGreaterThanOrEqualTo(String value) {
            addCriterion("ssh_pwd >=", value, "sshPwd");
            return (Criteria) this;
        }

        public Criteria andSshPwdLessThan(String value) {
            addCriterion("ssh_pwd <", value, "sshPwd");
            return (Criteria) this;
        }

        public Criteria andSshPwdLessThanOrEqualTo(String value) {
            addCriterion("ssh_pwd <=", value, "sshPwd");
            return (Criteria) this;
        }

        public Criteria andSshPwdLike(String value) {
            addCriterion("ssh_pwd like", value, "sshPwd");
            return (Criteria) this;
        }

        public Criteria andSshPwdNotLike(String value) {
            addCriterion("ssh_pwd not like", value, "sshPwd");
            return (Criteria) this;
        }

        public Criteria andSshPwdIn(List<String> values) {
            addCriterion("ssh_pwd in", values, "sshPwd");
            return (Criteria) this;
        }

        public Criteria andSshPwdNotIn(List<String> values) {
            addCriterion("ssh_pwd not in", values, "sshPwd");
            return (Criteria) this;
        }

        public Criteria andSshPwdBetween(String value1, String value2) {
            addCriterion("ssh_pwd between", value1, value2, "sshPwd");
            return (Criteria) this;
        }

        public Criteria andSshPwdNotBetween(String value1, String value2) {
            addCriterion("ssh_pwd not between", value1, value2, "sshPwd");
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

        public Criteria andSshPortEqualTo(Integer value) {
            addCriterion("ssh_port =", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortNotEqualTo(Integer value) {
            addCriterion("ssh_port <>", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortGreaterThan(Integer value) {
            addCriterion("ssh_port >", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("ssh_port >=", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortLessThan(Integer value) {
            addCriterion("ssh_port <", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortLessThanOrEqualTo(Integer value) {
            addCriterion("ssh_port <=", value, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortIn(List<Integer> values) {
            addCriterion("ssh_port in", values, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortNotIn(List<Integer> values) {
            addCriterion("ssh_port not in", values, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortBetween(Integer value1, Integer value2) {
            addCriterion("ssh_port between", value1, value2, "sshPort");
            return (Criteria) this;
        }

        public Criteria andSshPortNotBetween(Integer value1, Integer value2) {
            addCriterion("ssh_port not between", value1, value2, "sshPort");
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

        public Criteria andContainerIdIsNull() {
            addCriterion("container_id is null");
            return (Criteria) this;
        }

        public Criteria andContainerIdIsNotNull() {
            addCriterion("container_id is not null");
            return (Criteria) this;
        }

        public Criteria andContainerIdEqualTo(String value) {
            addCriterion("container_id =", value, "containerId");
            return (Criteria) this;
        }

        public Criteria andContainerIdNotEqualTo(String value) {
            addCriterion("container_id <>", value, "containerId");
            return (Criteria) this;
        }

        public Criteria andContainerIdGreaterThan(String value) {
            addCriterion("container_id >", value, "containerId");
            return (Criteria) this;
        }

        public Criteria andContainerIdGreaterThanOrEqualTo(String value) {
            addCriterion("container_id >=", value, "containerId");
            return (Criteria) this;
        }

        public Criteria andContainerIdLessThan(String value) {
            addCriterion("container_id <", value, "containerId");
            return (Criteria) this;
        }

        public Criteria andContainerIdLessThanOrEqualTo(String value) {
            addCriterion("container_id <=", value, "containerId");
            return (Criteria) this;
        }

        public Criteria andContainerIdLike(String value) {
            addCriterion("container_id like", value, "containerId");
            return (Criteria) this;
        }

        public Criteria andContainerIdNotLike(String value) {
            addCriterion("container_id not like", value, "containerId");
            return (Criteria) this;
        }

        public Criteria andContainerIdIn(List<String> values) {
            addCriterion("container_id in", values, "containerId");
            return (Criteria) this;
        }

        public Criteria andContainerIdNotIn(List<String> values) {
            addCriterion("container_id not in", values, "containerId");
            return (Criteria) this;
        }

        public Criteria andContainerIdBetween(String value1, String value2) {
            addCriterion("container_id between", value1, value2, "containerId");
            return (Criteria) this;
        }

        public Criteria andContainerIdNotBetween(String value1, String value2) {
            addCriterion("container_id not between", value1, value2, "containerId");
            return (Criteria) this;
        }

        public Criteria andPxeMacIsNull() {
            addCriterion("pxe_mac is null");
            return (Criteria) this;
        }

        public Criteria andPxeMacIsNotNull() {
            addCriterion("pxe_mac is not null");
            return (Criteria) this;
        }

        public Criteria andPxeMacEqualTo(String value) {
            addCriterion("pxe_mac =", value, "pxeMac");
            return (Criteria) this;
        }

        public Criteria andPxeMacNotEqualTo(String value) {
            addCriterion("pxe_mac <>", value, "pxeMac");
            return (Criteria) this;
        }

        public Criteria andPxeMacGreaterThan(String value) {
            addCriterion("pxe_mac >", value, "pxeMac");
            return (Criteria) this;
        }

        public Criteria andPxeMacGreaterThanOrEqualTo(String value) {
            addCriterion("pxe_mac >=", value, "pxeMac");
            return (Criteria) this;
        }

        public Criteria andPxeMacLessThan(String value) {
            addCriterion("pxe_mac <", value, "pxeMac");
            return (Criteria) this;
        }

        public Criteria andPxeMacLessThanOrEqualTo(String value) {
            addCriterion("pxe_mac <=", value, "pxeMac");
            return (Criteria) this;
        }

        public Criteria andPxeMacLike(String value) {
            addCriterion("pxe_mac like", value, "pxeMac");
            return (Criteria) this;
        }

        public Criteria andPxeMacNotLike(String value) {
            addCriterion("pxe_mac not like", value, "pxeMac");
            return (Criteria) this;
        }

        public Criteria andPxeMacIn(List<String> values) {
            addCriterion("pxe_mac in", values, "pxeMac");
            return (Criteria) this;
        }

        public Criteria andPxeMacNotIn(List<String> values) {
            addCriterion("pxe_mac not in", values, "pxeMac");
            return (Criteria) this;
        }

        public Criteria andPxeMacBetween(String value1, String value2) {
            addCriterion("pxe_mac between", value1, value2, "pxeMac");
            return (Criteria) this;
        }

        public Criteria andPxeMacNotBetween(String value1, String value2) {
            addCriterion("pxe_mac not between", value1, value2, "pxeMac");
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