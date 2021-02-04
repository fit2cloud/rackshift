// 将时间戳转日期格式的过滤器

let _ = require('lodash');
import i18n from "@/i18n/i18n";

function dateFormat(dataStr) {
    var time = new Date(dataStr);

    function timeAdd0(str) {
        if (str < 10) {
            str = '0' + str;
        }
        return str
    }

    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y + '-' + timeAdd0(m) + '-' + timeAdd0(d) + ' ' + timeAdd0(h) + ':' + timeAdd0(mm) + ':' + timeAdd0(s);
}

function brandsFormat(brands) {
    if (brands && brands.lenghth != 0) {
        let brandList = eval(brands);
        let r = "";
        brandList.forEach(b => r += b + ",");
        return r.substr(0, r.length - 1);
    }

    return "";

}

function endpointFormat(endpointId) {
    let enpoint = _.find(JSON.parse(localStorage.getItem("allEndPoints")), (o) => o.id == endpointId);
    if (enpoint) {
        return i18n.t(enpoint.name);
    }
    return "";
}

export {dateFormat, brandsFormat, endpointFormat}