let WebSocketUtil = {
    openSocket: function (obj) {
        if (typeof (WebSocket) == "undefined") {
            console.log("您的浏览器不支持WebSocket");
        } else {
            console.log("您的浏览器支持WebSocket");
            var socketUrl = "ws://127.0.0.1:8082/webSocket/";
            let socket = new WebSocket(socketUrl);
            //打开事件
            socket.onopen = function () {
                console.log("websocket已打开");
            };
            if (obj) {
                socket.onmessage = (obj.onmessage);
            }

            //获得消息事件
            socket.onmessage = function (msg) {
                var serverMsg = "收到服务端信息：" + msg.data;
                //发现消息进入    开始处理前端触发逻辑
            };
            //关闭事件
            socket.onclose = function () {
                console.log("websocket已关闭");
            };
            //发生了错误事件
            socket.onerror = function () {
                console.log("websocket发生了错误");
            }

            return socket;
        }
    },
    sendMessage: function (msg) {
        if (typeof (WebSocket) == "undefined") {
            console.log("您的浏览器不支持WebSocket");
        } else {
            socket.send(msg);
        }
    }
}

export {WebSocketUtil}