let SockJS = require('sockjs-client');
let Stomp = require('stompjs');
let WebSocketUtil = {
    stompClient: null,
    openSocket: function (topic, callback) {
        let that = this;
        let cal = callback;
        var socket = new SockJS('/rs-websocket');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, function (frame) {
            that.stompClient.subscribe('/topic/' + topic, function (greeting) {
                if (cal) {
                    cal();
                }
            });
        });

        return this.stompClient;
    },
    sendMessage: function (msg) {
        this.stompClient.send("/app/hello", {}, msg);
    },
    checkDoingThings(list, statusPro, topic, callback) {
        let exists = false;
        if (list) {
            for (let i = 0; i < list.length; i++) {
                if (list[i][statusPro] && ((list[i][statusPro].indexOf("ing") != -1) || list[i][statusPro].indexOf("ING") != -1)) {
                    exists = true;
                    break;
                }
            }
        }
        if (exists) {
            if (!this.webSocket) {
                this.webSocket = WebSocketUtil.openSocket(topic, callback);
            }
        } else {
            this.webSocket = WebSocketUtil.openSocket(topic, callback);
        }
    },
}

export {WebSocketUtil}