const btn = document.querySelector('.joinParty');
let isJoinBtn=false;
btn.onclick = function (event){
    if(isJoinBtn)return;
    isJoinBtn = true;
    const type = event.target.getAttribute("data-text");
    const partyId = document.getElementById('partyId').value.trim();
    let data = {
        isBlack:false,
        state:'N'
    }
    if(type==='join')data.state = 'W';
    fetch("/party/applyJoinParty/"+ partyId,{
        method:'PUT',
        headers:{
            'Content-Type':'application/json'
        },
        body:JSON.stringify(data)
    })
        .then(response => response.text())
        .then(result =>{
            isJoinBtn = false;
            //비로그인
            if(result === 'login'){
                alert("로그인 후 이용해주세요.");
                location.href = '/user/loginForm'
            }
            //성공
            if(result === 'success'){
                alert("요청이 완료되었습니다.");
                location.reload();
            }
            if(result === 'host'){
                alert("호스트는 요청이 불가합니다.");
            }
            if(result === 'black'){
                alert("가입신청 할 수 없습니다.")
            }
        })
}