import ca from "element-ui/src/locale/lang/ca";

let SockJS = require('sockjs-client');
let Stomp = require('_stompjs@2.3.3@stompjs');
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
    }
}

export {WebSocketUtil}