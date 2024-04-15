var socket = new SockJS('/ws?partyId=' + partyId);
var stompClient = Stomp.over(socket);
window.onload = function(){
    if(!messages){
        alert("Empty")
        return;
    }
    for(let i = 0 ; i < messages.length; i++){
        showMessage(messages[i]);
    }
};

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
    if (messageContent) {
        stompClient.send("/app/send/"+partyId, {}, messageContent);
        messageInput.value = '';
    }
}

function showMessage(message) {
    var messageArea = document.getElementById('messageArea');
    var messageElement = document.createElement('p');
    messageElement.textContent = message.writer + ': ' + message.content+'\t'+(message.date.substring(message.date.indexOf(" ")));
    messageArea.appendChild(messageElement);
}
