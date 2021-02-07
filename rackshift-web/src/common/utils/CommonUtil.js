let _ = require('lodash');

function isAnyBlank() {
    let isBlank = false;
    if (arguments && arguments.length > 0) {
        for (let i = 0; i < arguments.length; i++) {
            if (!arguments[i] || (arguments[i] && (arguments[i] == "" || arguments[i].length == 0))) {
                isBlank = true;
            }
        }
    }
    return isBlank;
}

function isAnyPropertyBlank() {
    if (arguments && arguments.length > 0) {
        for (let i = 0; i < arguments.length; i++) {
            for (let j = 0; j < Object.keys(arguments[i]).length; j++) {
                if (arguments[i][Object.keys(arguments[i])[j]] == "" || arguments[i][Object.keys(arguments[i])[j]].length == 0) {
                    return true;
                }
            }
        }
    }
    return false;
}

function toHump(name) {
    return name.replace(/\_(\w)/g, function (all, letter) {
        return letter.toUpperCase();
    });
}

// 驼峰转换下划线
function humpToLine(name) {
    return name.replace(/([A-Z])/g, "_$1").toLowerCase();
}

function checkMask(mask) {
    var obj = mask;
    var exp = /^(254|252|248|240|224|192|128|0)\.0\.0\.0|255\.(254|252|248|240|224|192|128|0)\.0\.0|255\.255\.(254|252|248|240|224|192|128|0)\.0|255\.255\.255\.(254|252|248|240|224|192|128|0)$/;
    var reg = obj.match(exp);
    if (reg == null) {
        return false; //"非法"
    } else {
        return true; //"合法"
    }
}

export {isAnyBlank, isAnyPropertyBlank, humpToLine, toHump, checkMask}