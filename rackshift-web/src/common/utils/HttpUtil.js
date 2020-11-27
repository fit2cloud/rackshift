import axios from 'axios'
import {MessageBox} from 'element-ui';
import i18n from "@/i18n/i18n";

axios.interceptors.response.use(function (response) {
    if (JSON.stringify(response.data).indexOf('Authentication Status Invalid') != -1) {
        MessageBox.alert(i18n.t('login_timeout'), i18n.t('tips'), {
            confirmButtonText: i18n.t('confirm'),
            callback: action => {
                localStorage.removeItem("login");
                window.location.href = "/";
            }
        });
    }
    return response;
}, function (error) {
    if (JSON.stringify(error.response).indexOf('Authentication Status Invalid') != -1) {
        MessageBox.alert(i18n.t('login_timeout'), i18n.t('tips'), {
            confirmButtonText: i18n.t('confirm'),
            callback: action => {
                localStorage.removeItem("login");
                window.location.href = "/";
            }
        });
    } else {
        return Promise.reject(error);
    }
});

const HttpUtil = {
    get: function (url, params, resolve, reject) {
        axios.get(url, params).then((res) => {
            if (res) {
                if (res.data.success) {
                    resolve(res.data);
                } else {
                    if (reject) {
                        reject(res.data.message);
                    } else {
                        MessageBox.alert(res.data.message);
                    }
                }
            }
        }).catch((e) => {
            MessageBox.alert(e);
        });
    },

    getAsync: async function (url, params) {
        return axios.get(url, params);
    },

    post: (url, data, resolve, errorHandler) => {
        axios.post(url, data, {
            headers: {
                "Content-Type": "application/json"
            }
        }).then((res) => {
                resolve(res.data);
            }
        ).catch((e) => {
            if (errorHandler) {
                errorHandler(e);
            } else {
                MessageBox.alert(e);
            }
        });
    }
}

export default HttpUtil