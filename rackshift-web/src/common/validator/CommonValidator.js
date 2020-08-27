function requiredValidator(rule, value, callback) {
    if (value === '' || !value) {
        if (rule.field == "userName") {
            callback(new Error(rule.vue.$t('pls_input_username')));
        }
        if (rule.field == "password") {
            callback(new Error(rule.vue.$t('pls_input_password')));
        }
        callback(new Error(rule.vue.$t('pls_input_params')));
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
        callback(new Error(rule.vue.$t('pls_select_' + rule.name)));
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
        callback(new Error(rule.vue.$t('invalid_format')));
    }
    callback();
}

export {
    requiredValidator, hostnameValidator, ipValidator, requiredSelectValidator
}