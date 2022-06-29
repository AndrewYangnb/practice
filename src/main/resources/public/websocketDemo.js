
// 创建WebSocket连接,
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");
//alert(location.hostname + location.port + " OK");

webSocket.onmessage = function (msg) { updateChat(msg); };
webSocket.onclose = function () { alert("WebSocket connection closed") };

// 点击Send按钮，发送消息
id("send").addEventListener("click", function () {

//    sendMessage(id("message").value);
    //
    alert(id("message").value);

});

// 在输入框中按下回车则发送消息
id("message").addEventListener("keypress", function (e) {
    // 键盘输入keycode===13，即回车键
    if (e.keyCode === 13) {
//    alert(e.target.value);
    sendMessage(e.target.value);
//    updateChat();
    }
});


// 不为空则发送消息，然后清除输入框
function sendMessage(message) {
    if (message !== "") {
        webSocket.send(message);
        id("message").value = "";
    }
}

// 更新聊天面板，更新用户列表
function updateChat(msg) {
    var data = JSON.parse(msg.data);
    /*此处有改动*/
    // userMessage
    insert("chat", "<p>" + data.userMessage + "</p>");
//    id("chat").innerText(data.userMessage);
    id("userlist").innerHTML = "";
    data.userlist.forEach(function (user) {
        //
//        alert("OK");
        // 更新用户列表
        insert("userlist", "<li>" + user + "</li>");
    });
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    // 在当前元素节点的里面，在第一个子元素之前插入元素
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    // 用来拼凑命令
    return document.getElementById(id);
}