const modal = document.getElementById('modal');
const btn = document.getElementById('edit-btn');
const close = document.querySelector('.close');
const msg = document.querySelector('.msg');
const msgSection = document.querySelector('.msg-section');
const nicknameInput = document.getElementById('nickname');
let isChangeBtn = false;

// 버튼을 클릭하면 모달을 열어줍니다
btn.onclick = function(){
    modal.style.display = 'block';
}
// (x)를 클릭하면 모달을 닫습니다
close.onclick = function() {
    modal.style.display = 'none';
}
// 모달 외부를 클릭하면 모달을 닫습니다
window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = 'none';
    }
}
nicknameInput.onkeyup = function (){
    msgSection.style.display = 'none';
}
function changeNickname(nickname){
    if(isChangeBtn)return;
    isChangeBtn=true;
    const data={
        email:user.email,
        nickname:nickname.value.trim()
    }
    fetch("/user/changeNickname",{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body: JSON.stringify(data)
    })
        .then(response=>response.text())
        .then(result=>{
            isChangeBtn = false;
            if(result === 'badRequest') {
                location.href = "/error"
                return;
            }
            if(result === 'same'){
                setMsg("기존 닉네임과 동일합니다.")
                return;
            }
            if(result === 'not valid'){
                setMsg("이미 사용중인 닉네임입니다.")
                return;
            }
            if(result === 'not length'){
                setMsg("3글자 이상 입력해주세요.")
                return;
            }
            if(result === 'ok'){
                const nicknameElemnet = document.querySelector('.nickname');
                alert("닉네임 변경이 완료되었습니다.")
                modal.style.display = 'none';
                nicknameElemnet.innerHTML = `<p>`+ data.nickname +`</p>`;
            }
        })
        .catch(error=>{
            console.log('Error : ',error);
        })
}
function setMsg(massage){
    msgSection.style.display="block";
    msg.innerText = massage;
}

