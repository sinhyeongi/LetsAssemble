var socket = new SockJS('/ws?partyId=' + partyId);
var stompClient = Stomp.over(socket);
var email = document.getElementById("email").value;
const messageInput = document.getElementById('messageInput');
const chat_home = document.getElementsByClassName("chat_home")[0];
chat_home.addEventListener("click",()=>{
    location.href="/";
})
window.onload = function(){
    if(note){
        showNotification(note);
    }
    if(!messages){
        return;
    }
    for(let i = 0 ; i < messages.length; i++){
        showMessage(messages[i]);
    }

};
messageInput.addEventListener('keyup',e =>{
    if(e.key === 'Enter'){
        sendMessage();
    }
})
stompClient.connect({}, function (frame) {
    stompClient.subscribe('/topic/party-'+partyId, function (message) {
        message = JSON.parse(message.body)
        fetch("/chat/read/"+message.id,{
            method : "DELETE",
            body : JSON.stringify(message),
            headers : {
                'Content-Type' : 'application/json'
            }
        }).then(data=>{
            if(data === 'ok'){
                alert('ok');
            }
        }).catch(error =>{
            alert('err = '+error);
        });
        showMessage(message);
    });
},function(error){
    location.href="/error";
});

function sendMessage() {
    var messageInput = document.getElementById('messageInput');
    var messageContent = messageInput.value.trim();
    if(messageContent.length > 1000){
        alert('1000글자 까지만 입력이 가능합니다!');
        messageInput.value = '';
        messageInput.focus();
        return;
    }
    if (messageContent) {
        stompClient.send("/app/send/"+partyId, {}, messageContent);
        messageInput.value = '';
    }
}

function showMessage(message) {
    var messageArea = document.getElementById('messageArea');
    var messageElement = document.createElement('div');

    if(email === message.writer){
        messageElement.classList.add("chat_content_box_right")
    }else{
        messageElement.classList.add("chat_content_box")
        const messageTitle = document.createElement("div");
        messageTitle.classList.add("chat_writer");
        messageTitle.textContent = message.writer;
        messageElement.appendChild(messageTitle);
    }
    const div = document.createElement('div');

    const messageContent_box = document.createElement('div');
    messageContent_box.classList.add('messageContent_box_wrap');

    const messageContent = document.createElement("div");
    messageContent.classList.add("chat_content");
    messageContent.textContent = message.content;
    messageContent_box.appendChild(messageContent);

    const messageContent_date = document.createElement("div");
    messageContent_date.classList.add("chat_content_date");
    messageContent_date.textContent = getViewDate(message.date);
    messageContent_box.appendChild(messageContent_date);
    messageElement.appendChild(messageContent_box);



    messageArea.appendChild(messageElement);

    const mainElement = document.getElementById("Chat_main");
    mainElement.scrollTop = mainElement.scrollHeight;
}
function showNotification(message) {
    var messageArea = document.getElementById('messageArea');
    var messageElement = document.createElement('div');
    messageElement.classList.add("chat_content_box_notification")
    const messageTitle = document.createElement("div");
    messageTitle.classList.add("chat_notification");
    messageTitle.textContent = "공지사항";
    messageElement.appendChild(messageTitle);

    const div = document.createElement('div');

    const messageContent_box = document.createElement('div');
    messageContent_box.classList.add('messageContent_box_wrap');

    const messageContent = document.createElement("div");
    messageContent.classList.add("chat_content");
    messageContent.textContent = message;
    messageContent_box.appendChild(messageContent);

    const messageContent_date = document.createElement("div");
    messageContent_date.classList.add("chat_content_date");
    messageContent_box.appendChild(messageContent_date);
    messageElement.appendChild(messageContent_box);



    messageArea.appendChild(messageElement);

    const mainElement = document.getElementById("Chat_main");
    mainElement.scrollTop = mainElement.scrollHeight;
}
function getViewDate(date){
    const today = Today();
    let datearray = date.split(" ");
    if(datearray[0] === today){
        return datearray[1];
    }
    return date;
}
function Today(){
    const date = new Date();
    return date.getFullYear() + '-' + ('0'+(date.getMonth()+1)).slice(-2) + '-' + ('0'+(date.getDate())).slice(-2);
}
