import axios from 'axios'
import {MessageBox} from 'element-ui';

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

    post: (url, data, resolve, reject) => {
        axios.post(url, data).then((res) => {
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