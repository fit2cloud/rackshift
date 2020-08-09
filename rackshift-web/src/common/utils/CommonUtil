let _ = require('lodash');

function isAnyBlank() {
    let isBlank = false;
    if (arguments && arguments.length > 0) {
        for (let i = 0; i < arguments.length; i++) {
            if (!arguments[i] || (arguments[i] && (arguments[i] == "" || arguments[i].length == 0))) {
                isBlank =  true;
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
function toLine(name) {
    return name.replace(/([A-Z])/g, "_$1").toLowerCase();
}

export {isAnyBlank, isAnyPropertyBlank, toLine, toHump}