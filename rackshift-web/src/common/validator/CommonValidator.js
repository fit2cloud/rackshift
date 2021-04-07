import {checkMask} from "@/common/utils/CommonUtil";

let _ = require("lodash");

function requiredValidator(rule, value, callback) {
    if (value === '' || !value || value.length === 0) {
        if (rule.field == "userName") {
            callback(new Error(rule.vue.$t('pls_input_username')));
        }
        if (rule.field == "password") {
            callback(new Error(rule.vue.$t('pls_input_password')));
        }
        if (rule.field == "uplinks") {
            callback(new Error(rule.vue.$t('pls_select_')));
        }
        if (rule.msg) {
            callback(new Error(rule.msg));
        } else {
            callback(new Error(rule.vue.$t('pls_input_params')));
        }
    }
    callback();
}

var emailReg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
var httpReg = /http:////\w+/;
var phoneReg = /^((0\d{2,3}-\d{7,8})|(1[3456789]\d{9}))$/;

function emailValidator(rule, value, callback) {
    if (value === '' || !value) {
        if (rule.field == "userName") {
            callback(new Error(rule.vue.$t('pls_input_username')));
        }
        if (rule.field == "password") {
            callback(new Error(rule.vue.$t('pls_input_password')));
        }
        if (rule.msg) {
            callback(new Error(rule.msg));
        } else {
            callback(new Error(rule.vue.$t('pls_input_params')));
        }
    }

    if (!emailReg.test(value)) {
        callback(new Error(rule.vue.$t('email_format_error')));
    }
    callback();
}

function phoneValidator(rule, value, callback) {
    if (value === '' || !value) {
        if (rule.field == "userName") {
            callback(new Error(rule.vue.$t('pls_input_username')));
        }
        if (rule.field == "password") {
            callback(new Error(rule.vue.$t('pls_input_password')));
        }
        if (rule.msg) {
            callback(new Error(rule.msg));
        } else {
            callback(new Error(rule.vue.$t('pls_input_params')));
        }
    }

    if (!phoneReg.test(value)) {
        callback(new Error(rule.vue.$t('phone_format_error')));
    }
    callback();
}

function requiredSelectValidator(rule, value, callback) {
    if (value === '' || !value || !value.length) {
        if (rule.field == "userName") {
            callback(new Error(rule.vue.$t('pls_select_username')));
        }
        if (rule.field == "password") {
            callback(new Error(rule.vue.$t('pls_select_password')));
        }
        if (rule.name) {
            callback(new Error(rule.vue.$t('pls_select_') + rule.vue.$t(rule.name)));
        } else {
            callback(new Error(rule.vue.$t('pls_select')));
        }
    }
    callback();
}

function repoSelectValidator(rule, value, callback) {
    if (value === '' || !value || !value.length) {
        if (rule.name) {
            callback(new Error(rule.vue.$t('pls_select_') + rule.vue.$t(rule.name)));
        } else {
            callback(new Error(rule.vue.$t('pls_select')));
        }
    }

    if (!/[a-zA-z]+:\/\/[^\s]+/.test(value)) {
        callback(new Error(rule.vue.$t('invalid_format')));
        return;
    }
    callback();
}

function hostnameValidator(rule, value, callback) {
    if (value === '' || !value) {
        callback(new Error(rule.vue.$t('pls_input_hostname')));
    }
    if (/[^0-9a-zA-Z\-]+/.test(value)) {
        callback(new Error(rule.vue.$t('hostname_must_not_have_invalid_word')));
    }
    callback();
}

function domainValidator(rule, value, callback) {
    if (value === '' || !value) {
        callback(new Error(rule.vue.$t('pls_input_')));
    }
    if (/[^0-9a-zA-Z\-]+/.test(value)) {
        callback(new Error(rule.vue.$t('invalid_format')));
    }
    callback();
}

function dnsValidator(rule, value, callback) {
    if (value === '' || !value) {
        callback();
    } else {
        if (value.length)
            if (typeof value == Array) {
                for (let i = 0; i < value.length; i++) {
                    if (!/\w+.\w+/.test(value[i]) && !/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(value[i])) {
                        callback(new Error(rule.vue.$t('invalid_format')));
                        return;
                    }
                }
            } else {
                if (!/\w+.\w+/.test(value) && !/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(value)) {
                    callback(new Error(rule.vue.$t('invalid_format')));
                    return;
                }
            }
    }
    callback();
}

function ipValidator(rule, value, callback) {
    if (value === '' || !value) {
        callback(new Error(rule.vue.$t('cannt_be_null')));
    }
    if (!/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(value)) {
        callback(new Error(rule.vue.$t('ip_invalid_format')));
    }
    callback();
}

function vlanValidator(rule, value, callback) {
    if (rule.require) {
        if (value === '' || !value || value.length === 0) {
            callback(new Error(rule.vue.$t('cannt_be_null')));
        }
    }
    let vlanIds = _.uniqBy(value);
    if (vlanIds.length != value.length)
        callback(new Error(rule.vue.$t("")));
    for (let i = 0; i < value.length; i++)
        if (!/\d+$/.test(value[i])) {
            callback(new Error(rule.vue.$t('must_be_number')));
        } else {
            if (value[i] < 0 || value[i] > 4095)
                callback(new Error(rule.vue.$t('must_between_0_4095')));
        }
    callback();
}

function maskValidator(rule, value, callback) {
    if (value === '' || !value) {
        callback();
    }
    if (!/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(value)) {
        callback(new Error(rule.vue.$t('ip_invalid_format')));
    }

    if (!checkMask(value)) {
        callback(new Error(rule.vue.$t('netmask_validate_error')));
    }
    callback();
}

export {
    requiredValidator,
    repoSelectValidator,
    hostnameValidator,
    domainValidator,
    ipValidator,
    requiredSelectValidator,
    emailValidator,
    phoneValidator,
    maskValidator,
    vlanValidator,
    dnsValidator
}