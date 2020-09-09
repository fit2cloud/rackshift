import axios from 'axios'
import {MessageBox} from 'element-ui';

axios.interceptors.response.use(function (response) {
    if (JSON.stringify(response.data).indexOf('Authentication Status Invalid') != -1) {
        MessageBox.alert('登录已失效', '提示', {
            confirmButtonText: '确定',
            callback: action => {
                localStorage.removeItem("login");
                window.location.href = "/";
            }
        });
    }
    return response;
}, function (error) {
    if (JSON.stringify(response.data).indexOf('Authentication Status Invalid') != -1) {
        MessageBox.alert('登录已失效', '提示', {
            confirmButtonText: '确定',
            callback: action => {
                localStorage.removeItem("login");
                window.location.href = "/";
            }
        });
    } else {
        return Promise.reject(error);
    }
});
// axios.defaults.headers.post['Content-Type'] = 'application/json';

const HttpUtil = {
    get: function (url, params, resolve, reject) {
        axios.get(url, params).then((res) => {
            if (res.data.success) {
                resolve(res.data);
            } else {
                if (reject) {
                    reject(res.data.message);
                } else {
                    MessageBox.alert(res.data.message);
                }
            }
        }).catch((e) => {
            MessageBox.alert(e);
        });
    },

    getAsync: async function (url, params) {
        return axios.get(url, params);
    },

    post: (url, data, resolve, reject) => {
        axios.post(url, data, {
            headers: {
                "Content-Type": "application/json"
            }
        }).then((res) => {
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
        ).catch((e) => {
            MessageBox.alert(e);
        });
    }
}

export default HttpUtil