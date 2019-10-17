//心跳检测
var heartCheck = {
    timeout: 300000,        //5分钟发一次心跳
    timeoutObj: null,
    serverTimeoutObj: null,
    reset: function () {
        clearTimeout(this.timeoutObj);
        clearTimeout(this.serverTimeoutObj);
        return this;
    },
    start: function () {
        var self = this;
        this.timeoutObj = setTimeout(function () {
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            socket.send("ping");
            console.log("heart check !");
            self.serverTimeoutObj = setTimeout(function () {//如果超过一定时间还没重置，说明后端主动断开了
                socket.close();     //如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
            }, self.timeout)
        }, this.timeout)
    }
};

var socket;

//初始化socket连接
function initSocket(wsUrl) {
    if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
        alert("您的浏览器不支持WebSocket");
    } else {
        console.log("您的浏览器支持WebSocket/websocket");
    }
    //socket连接地址: 注意是ws协议
    socket = new WebSocket(wsUrl);
    socket.onopen = function () {
        console.log("Socket 已打开");
        heartCheck.reset().start();      //心跳检测重置
    };
    //错误事件
    socket.onerror = function () {
        alert("Socket发生了错误");
    };
    $(window).unload(function () {
        socket.close();
    });
}