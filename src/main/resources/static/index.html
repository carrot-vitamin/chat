<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!--静态资源 核心静态资源-->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!--icon-->
    <link rel="shortcut icon" href="favicon.ico">

    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>

    <script type="application/javascript">
        var socket;

        //登录过后初始化socket连接
        function initSocket(userId) {
            if (typeof (WebSocket) == "undefined") {
                console.log("您的浏览器不支持WebSocket");
                alert("您的浏览器不支持WebSocket");
            } else {
                console.log("您的浏览器支持WebSocket/websocket");
            }
            //socket连接地址: 注意是ws协议
            socket = new WebSocket("ws://localhost:8080/chat/websocket/" + userId);
            socket.onopen = function () {
                console.log("Socket 已打开");
            };
            //获得消息事件
            socket.onmessage = function (msg) {
                console.log("获得消息" + msg.data);
                var histroy = $("#history").text();
                $("#history").text(histroy + "\r\n" + msg.data);
                console.log($(msg));
            };
            //关闭事件
            socket.onclose = function () {
                console.log("Socket已关闭");
            };
            //错误事件
            socket.onerror = function () {
                alert("Socket发生了错误");
            }
            $(window).unload(function () {
                socket.close();
            });
        }

        function isEmpty(obj) {
            return typeof obj == "undefined" || obj == null || obj === "";
        }

        //点击按钮发送消息
        function send() {
            var msg = $("#msg").val();
            if (!isEmpty(msg)) {
                console.log("发送消息: " + msg);
                socket.send($("#msg").val());
                $("#msg").reload();
            } else {
                alert("不可发送空消息！");
            }

        }

        //登录
        function login() {
            $.ajax({
                url: "/chat/login",
                data: $("#loginForm").serialize(),
                type: "POST",
                success: function (userId) {
                    if (userId) {
                        console.log("登录成功!");
                        initSocket(userId);
                    }
                }
            });
        }
    </script>
</head>
<body class="container-fluid">
<div class="row" style="margin-top: 30px;">
    <div class="col-md-3"></div>
    <div class="col-md-6">
        <pre style="height: 400px;" id="history"></pre>
    </div>
    <div class="col-md-3"></div>
</div>
<div class="row">
    <div class="col-md-3"></div>
    <div class="col-md-6">
        <textarea id="msg" class="form-control" rows="3" placeholder="格式:@xxx#消息 , 或者@ALL#消息"></textarea>
    </div>
    <div class="col-md-3"></div>
</div>
<div class="row" style="margin-top: 10px;">
    <div class="col-md-3"></div>
    <div class="col-md-6">
        <button type="button" class="btn btn-primary" style="width: 100%;" onclick="send()">发送</button>
    </div>
    <div class="col-md-3"></div>
</div>
</body>
</html>