package io.rackshift.mybatis.domain;

import java.util.ArrayList;
import java.util.List;

public class DiskExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DiskExample() {
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

        public Criteria andEnclosureIdIsNull() {
            addCriterion("enclosure_id is null");
            return (Criteria) this;
        }

        public Criteria andEnclosureIdIsNotNull() {
            addCriterion("enclosure_id is not null");
            return (Criteria) this;
        }

        public Criteria andEnclosureIdEqualTo(Integer value) {
            addCriterion("enclosure_id =", value, "enclosureId");
            return (Criteria) this;
        }

        public Criteria andEnclosureIdNotEqualTo(Integer value) {
            addCriterion("enclosure_id <>", value, "enclosureId");
            return (Criteria) this;
        }

        public Criteria andEnclosureIdGreaterThan(Integer value) {
            addCriterion("enclosure_id >", value, "enclosureId");
            return (Criteria) this;
        }

        public Criteria andEnclosureIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("enclosure_id >=", value, "enclosureId");
            return (Criteria) this;
        }

        public Criteria andEnclosureIdLessThan(Integer value) {
            addCriterion("enclosure_id <", value, "enclosureId");
            return (Criteria) this;
        }

        public Criteria andEnclosureIdLessThanOrEqualTo(Integer value) {
            addCriterion("enclosure_id <=", value, "enclosureId");
            return (Criteria) this;
        }

        public Criteria andEnclosureIdIn(List<Integer> values) {
            addCriterion("enclosure_id in", values, "enclosureId");
            return (Criteria) this;
        }

        public Criteria andEnclosureIdNotIn(List<Integer> values) {
            addCriterion("enclosure_id not in", values, "enclosureId");
            return (Criteria) this;
        }

        public Criteria andEnclosureIdBetween(Integer value1, Integer value2) {
            addCriterion("enclosure_id between", value1, value2, "enclosureId");
            return (Criteria) this;
        }

        public Criteria andEnclosureIdNotBetween(Integer value1, Integer value2) {
            addCriterion("enclosure_id not between", value1, value2, "enclosureId");
            return (Criteria) this;
        }

        public Criteria andControllerIdIsNull() {
            addCriterion("controller_id is null");
            return (Criteria) this;
        }

        public Criteria andControllerIdIsNotNull() {
            addCriterion("controller_id is not null");
            return (Criteria) this;
        }

        public Criteria andControllerIdEqualTo(Integer value) {
            addCriterion("controller_id =", value, "controllerId");
            return (Criteria) this;
        }

        public Criteria andControllerIdNotEqualTo(Integer value) {
            addCriterion("controller_id <>", value, "controllerId");
            return (Criteria) this;
        }

        public Criteria andControllerIdGreaterThan(Integer value) {
            addCriterion("controller_id >", value, "controllerId");
            return (Criteria) this;
        }

        public Criteria andControllerIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("controller_id >=", value, "controllerId");
            return (Criteria) this;
        }

        public Criteria andControllerIdLessThan(Integer value) {
            addCriterion("controller_id <", value, "controllerId");
            return (Criteria) this;
        }

        public Criteria andControllerIdLessThanOrEqualTo(Integer value) {
            addCriterion("controller_id <=", value, "controllerId");
            return (Criteria) this;
        }

        public Criteria andControllerIdIn(List<Integer> values) {
            addCriterion("controller_id in", values, "controllerId");
            return (Criteria) this;
        }

        public Criteria andControllerIdNotIn(List<Integer> values) {
            addCriterion("controller_id not in", values, "controllerId");
            return (Criteria) this;
        }

        public Criteria andControllerIdBetween(Integer value1, Integer value2) {
            addCriterion("controller_id between", value1, value2, "controllerId");
            return (Criteria) this;
        }

        public Criteria andControllerIdNotBetween(Integer value1, Integer value2) {
            addCriterion("controller_id not between", value1, value2, "controllerId");
            return (Criteria) this;
        }

        public Criteria andDriveIsNull() {
            addCriterion("drive is null");
            return (Criteria) this;
        }

        public Criteria andDriveIsNotNull() {
            addCriterion("drive is not null");
            return (Criteria) this;
        }

        public Criteria andDriveEqualTo(String value) {
            addCriterion("drive =", value, "drive");
            return (Criteria) this;
        }

        public Criteria andDriveNotEqualTo(String value) {
            addCriterion("drive <>", value, "drive");
            return (Criteria) this;
        }

        public Criteria andDriveGreaterThan(String value) {
            addCriterion("drive >", value, "drive");
            return (Criteria) this;
        }

        public Criteria andDriveGreaterThanOrEqualTo(String value) {
            addCriterion("drive >=", value, "drive");
            return (Criteria) this;
        }

        public Criteria andDriveLessThan(String value) {
            addCriterion("drive <", value, "drive");
            return (Criteria) this;
        }

        public Criteria andDriveLessThanOrEqualTo(String value) {
            addCriterion("drive <=", value, "drive");
            return (Criteria) this;
        }

        public Criteria andDriveLike(String value) {
            addCriterion("drive like", value, "drive");
            return (Criteria) this;
        }

        public Criteria andDriveNotLike(String value) {
            addCriterion("drive not like", value, "drive");
            return (Criteria) this;
        }

        public Criteria andDriveIn(List<String> values) {
            addCriterion("drive in", values, "drive");
            return (Criteria) this;
        }

        public Criteria andDriveNotIn(List<String> values) {
            addCriterion("drive not in", values, "drive");
            return (Criteria) this;
        }

        public Criteria andDriveBetween(String value1, String value2) {
            addCriterion("drive between", value1, value2, "drive");
            return (Criteria) this;
        }

        public Criteria andDriveNotBetween(String value1, String value2) {
            addCriterion("drive not between", value1, value2, "drive");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("type like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("type not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andSizeIsNull() {
            addCriterion("size is null");
            return (Criteria) this;
        }

        public Criteria andSizeIsNotNull() {
            addCriterion("size is not null");
            return (Criteria) this;
        }

        public Criteria andSizeEqualTo(String value) {
            addCriterion("size =", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotEqualTo(String value) {
            addCriterion("size <>", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThan(String value) {
            addCriterion("size >", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThanOrEqualTo(String value) {
            addCriterion("size >=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThan(String value) {
            addCriterion("size <", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThanOrEqualTo(String value) {
            addCriterion("size <=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLike(String value) {
            addCriterion("size like", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotLike(String value) {
            addCriterion("size not like", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeIn(List<String> values) {
            addCriterion("size in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotIn(List<String> values) {
            addCriterion("size not in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeBetween(String value1, String value2) {
            addCriterion("size between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotBetween(String value1, String value2) {
            addCriterion("size not between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andRaidIsNull() {
            addCriterion("raid is null");
            return (Criteria) this;
        }

        public Criteria andRaidIsNotNull() {
            addCriterion("raid is not null");
            return (Criteria) this;
        }

        public Criteria andRaidEqualTo(String value) {
            addCriterion("raid =", value, "raid");
            return (Criteria) this;
        }

        public Criteria andRaidNotEqualTo(String value) {
            addCriterion("raid <>", value, "raid");
            return (Criteria) this;
        }

        public Criteria andRaidGreaterThan(String value) {
            addCriterion("raid >", value, "raid");
            return (Criteria) this;
        }

        public Criteria andRaidGreaterThanOrEqualTo(String value) {
            addCriterion("raid >=", value, "raid");
            return (Criteria) this;
        }

        public Criteria andRaidLessThan(String value) {
            addCriterion("raid <", value, "raid");
            return (Criteria) this;
        }

        public Criteria andRaidLessThanOrEqualTo(String value) {
            addCriterion("raid <=", value, "raid");
            return (Criteria) this;
        }

        public Criteria andRaidLike(String value) {
            addCriterion("raid like", value, "raid");
            return (Criteria) this;
        }

        public Criteria andRaidNotLike(String value) {
            addCriterion("raid not like", value, "raid");
            return (Criteria) this;
        }

        public Criteria andRaidIn(List<String> values) {
            addCriterion("raid in", values, "raid");
            return (Criteria) this;
        }

        public Criteria andRaidNotIn(List<String> values) {
            addCriterion("raid not in", values, "raid");
            return (Criteria) this;
        }

        public Criteria andRaidBetween(String value1, String value2) {
            addCriterion("raid between", value1, value2, "raid");
            return (Criteria) this;
        }

        public Criteria andRaidNotBetween(String value1, String value2) {
            addCriterion("raid not between", value1, value2, "raid");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskIsNull() {
            addCriterion("virtual_disk is null");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskIsNotNull() {
            addCriterion("virtual_disk is not null");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskEqualTo(String value) {
            addCriterion("virtual_disk =", value, "virtualDisk");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskNotEqualTo(String value) {
            addCriterion("virtual_disk <>", value, "virtualDisk");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskGreaterThan(String value) {
            addCriterion("virtual_disk >", value, "virtualDisk");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskGreaterThanOrEqualTo(String value) {
            addCriterion("virtual_disk >=", value, "virtualDisk");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskLessThan(String value) {
            addCriterion("virtual_disk <", value, "virtualDisk");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskLessThanOrEqualTo(String value) {
            addCriterion("virtual_disk <=", value, "virtualDisk");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskLike(String value) {
            addCriterion("virtual_disk like", value, "virtualDisk");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskNotLike(String value) {
            addCriterion("virtual_disk not like", value, "virtualDisk");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskIn(List<String> values) {
            addCriterion("virtual_disk in", values, "virtualDisk");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskNotIn(List<String> values) {
            addCriterion("virtual_disk not in", values, "virtualDisk");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskBetween(String value1, String value2) {
            addCriterion("virtual_disk between", value1, value2, "virtualDisk");
            return (Criteria) this;
        }

        public Criteria andVirtualDiskNotBetween(String value1, String value2) {
            addCriterion("virtual_disk not between", value1, value2, "virtualDisk");
            return (Criteria) this;
        }

        public Criteria andManufactorIsNull() {
            addCriterion("manufactor is null");
            return (Criteria) this;
        }

        public Criteria andManufactorIsNotNull() {
            addCriterion("manufactor is not null");
            return (Criteria) this;
        }

        public Criteria andManufactorEqualTo(String value) {
            addCriterion("manufactor =", value, "manufactor");
            return (Criteria) this;
        }

        public Criteria andManufactorNotEqualTo(String value) {
            addCriterion("manufactor <>", value, "manufactor");
            return (Criteria) this;
        }

        public Criteria andManufactorGreaterThan(String value) {
            addCriterion("manufactor >", value, "manufactor");
            return (Criteria) this;
        }

        public Criteria andManufactorGreaterThanOrEqualTo(String value) {
            addCriterion("manufactor >=", value, "manufactor");
            return (Criteria) this;
        }

        public Criteria andManufactorLessThan(String value) {
            addCriterion("manufactor <", value, "manufactor");
            return (Criteria) this;
        }

        public Criteria andManufactorLessThanOrEqualTo(String value) {
            addCriterion("manufactor <=", value, "manufactor");
            return (Criteria) this;
        }

        public Criteria andManufactorLike(String value) {
            addCriterion("manufactor like", value, "manufactor");
            return (Criteria) this;
        }

        public Criteria andManufactorNotLike(String value) {
            addCriterion("manufactor not like", value, "manufactor");
            return (Criteria) this;
        }

        public Criteria andManufactorIn(List<String> values) {
            addCriterion("manufactor in", values, "manufactor");
            return (Criteria) this;
        }

        public Criteria andManufactorNotIn(List<String> values) {
            addCriterion("manufactor not in", values, "manufactor");
            return (Criteria) this;
        }

        public Criteria andManufactorBetween(String value1, String value2) {
            addCriterion("manufactor between", value1, value2, "manufactor");
            return (Criteria) this;
        }

        public Criteria andManufactorNotBetween(String value1, String value2) {
            addCriterion("manufactor not between", value1, value2, "manufactor");
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

        public Criteria andModelIsNull() {
            addCriterion("model is null");
            return (Criteria) this;
        }

        public Criteria andModelIsNotNull() {
            addCriterion("model is not null");
            return (Criteria) this;
        }

        public Criteria andModelEqualTo(String value) {
            addCriterion("model =", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotEqualTo(String value) {
            addCriterion("model <>", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelGreaterThan(String value) {
            addCriterion("model >", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelGreaterThanOrEqualTo(String value) {
            addCriterion("model >=", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLessThan(String value) {
            addCriterion("model <", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLessThanOrEqualTo(String value) {
            addCriterion("model <=", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLike(String value) {
            addCriterion("model like", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotLike(String value) {
            addCriterion("model not like", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelIn(List<String> values) {
            addCriterion("model in", values, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotIn(List<String> values) {
            addCriterion("model not in", values, "model");
            return (Criteria) this;
        }

        public Criteria andModelBetween(String value1, String value2) {
            addCriterion("model between", value1, value2, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotBetween(String value1, String value2) {
            addCriterion("model not between", value1, value2, "model");
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