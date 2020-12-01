import axios from 'axios'
import {MessageBox} from 'element-ui';
import i18n from "@/i18n/i18n";

function LoginError(message) {
    this.message = message;
    this.name = 'LoginError';
    Error.captureStackTrace(this, LoginError);
}

LoginError.prototype = new Error;
LoginError.prototype.constructor = LoginError;

function loginFail() {
    MessageBox.alert(i18n.t('login_timeout'), i18n.t('tips'), {
        confirmButtonText: i18n.t('confirm'),
        callback: action => {
            localStorage.removeItem("login");
            window.location.href = "/";
        }
    });
}

axios.interceptors.response.use(function (response) {
    if (response && response.data && typeof response.data == 'string' && response.data.indexOf("Authentication Status Invalid") != -1) {
        throw new LoginError("login time out");
    }
    return response;
}, function (error) {
    if (error.response.data && error.response.data.indexOf('Authentication Status Invalid') != -1) {
        throw new LoginError("login time out");
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
            if (LoginError.prototype.isPrototypeOf(e)) {
                loginFail();
            } else {
                MessageBox.alert(e);
            }
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
                if (LoginError.prototype.isPrototypeOf(e)) {
                    loginFail();
                } else {
                    MessageBox.alert(e);
                }
            }
        });
    }
}

export default HttpUtil