import axios from 'axios'
import Vue from 'vue'
import {MessageBox} from 'element-ui'

Vue.use(MessageBox);
const HttpUtil = {
    get: function (url, params, resolve, reject) {
        axios.get(url, params).then((res) => {
            if (res.data.success) {
                resolve(res.data.data);
            } else {
                if (reject) {
                    reject(res.data.message);
                } else {
                    Vue.$alert(res.data.message);
                }
            }
        }).catch((e) => {
            MessageBox.alert(e);
        });
    },

    post: (url, data, resolve, reject) => {
        axios.post(url, data).then((res) => {
                if (res.data.success) {
                    resolve(res.data.data);
                } else {
                    if (reject) {
                        reject(res.data.message);
                    } else {
                        Vue.$alert(res.data.message);
                    }
                }
            }
        ).catch((e) => {
            MessageBox.alert(e);
        });

    }
}

export default HttpUtil