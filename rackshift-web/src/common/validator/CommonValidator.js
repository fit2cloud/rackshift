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

function hostnameValidator(rule, value, callback) {
    if (value === '' || !value) {
        callback(new Error(rule.vue.$t('pls_input_hostname')));
    }
    if (/[^0-9a-zA-Z\-]+/.test(value)) {
        callback(new Error(rule.vue.$t('hostname_must_not_have_invalid_word')));
    }
    callback();
}

export {
    requiredValidator, hostnameValidator
}