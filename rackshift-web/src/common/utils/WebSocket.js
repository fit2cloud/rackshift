let SockJS = require('sockjs-client');
let Stomp = require('stompjs');
let WebSocketUtil = function () {
    return {
        stompClient: null,
        openSocket: function (topic, callback) {
            let that = this;
            let cal = callback;
            var socket = new SockJS('/rs-websocket');
            this.stompClient = Stomp.over(socket);
            this.stompClient.connect({}, function (frame) {
                try {
                    that.stompClient.subscribe('/topic/' + topic, function (res) {
                        if (cal) {
                            cal(res.body);
                        }
                    });
                } catch (e) {
                    console.log("订阅" + "/topic/" + topic + " 失败！");
                }
            });

            return this.stompClient;
        },
        close: function () {
            if (this.stompClient) {
                this.stompClient.disconnect();
            }
        },
        sendMessage: function (msg) {
            this.stompClient.send("/app/hello", {}, msg);
        },
    }
}

export {WebSocketUtil}