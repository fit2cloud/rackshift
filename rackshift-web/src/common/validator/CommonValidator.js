function requiredValidator(rule, value, callback) {
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
    callback();
}

function requiredSelectValidator(rule, value, callback) {
    if (value === '' || !value) {
        if (rule.field == "userName") {
            callback(new Error(rule.vue.$t('pls_select_username')));
        }
        if (rule.field == "password") {
            callback(new Error(rule.vue.$t('pls_select_password')));
        }
        if (rule.name) {
            callback(new Error(rule.vue.$t('pls_select_' + rule.name)));
        } else {
            callback(new Error(rule.vue.$t('pls_select')));
        }
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

function ipValidator(rule, value, callback) {
    debugger
    if (value === '' || !value) {
        callback(new Error(rule.vue.$t('cannt_be_null')));
    }
    if (!/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(value)) {
        callback(new Error(rule.vue.$t('ip_invalid_format')));
    }
    callback();
}

export {
    requiredValidator, hostnameValidator, ipValidator, requiredSelectValidator
}