const content = document.querySelector('.content');
const atag = document.querySelectorAll('.link');
let isASKB_Btn = false;
let partyId;
let isDisbandBtn = false;

/* 모달 */
const $msg = document.querySelector('.msg');
const msgSection = document.querySelector('.msg-section');


atag.forEach(tag => {
    tag.addEventListener("click",event => {
        if(event.target.tagName ==='button'){
            event.preventDefault();
        }
    })
})
function memberList(id){
    partyId = id;
    fetch("/info/status/"+id)
        .then(response => {
            if(!response.ok){
                throw new Error("response not ok");
            }
            return response.json();
        })
        .then(data => makelist(data))
        .catch(error =>console.log("error =",error));
}

function makelist(data){

    let htmlList=`
    <div class="memberList">
    <div>
        <div class="memberList-title">
            <h3>닉네임</h3>
            <h3>연락처</h3>
            <h3>가입신청일</h3>
            <h3>성별</h3>
        </div>      
    `
    data.sort(function(a,b){
        if(a.state==='W' && b.state==='Y'){
            return -1;
        }
        if(a.user.email === a.party.user.email){
            return -1;
        }
        if(a.state==='Y' && b.state==='W'){
            return 1;
        }
        if(a.black && !b.black){
            return 1;
        }
        return 0;
    })
    data.forEach(obj=>{
        if(obj.state === 'N')return;
        //연락처
        const nickname = obj.user.nickname;
        const phone = obj.user.phone;
        //가입신청날짜
        const registDay = obj.aplicant_day;
        const gender = obj.user.gender;
        if(obj.state === 'W'){
            htmlList +=`
                <div style="text-align: end;color: red">가입 승인 대기자</div>
            `
        }
        if(obj.state === 'Y' && (obj.user.email !== obj.party.user.email)){
            htmlList += `<div class="member-info-wrap">
            <input type="radio" name="member" id="member-radio-${obj.user.id}" value="${obj.user.id}">`;
        }
        htmlList += `
            <div class="member-info" " onclick="document.getElementById('member-radio-${obj.user.id}').click();" for="member">
                <div class="nickname">
                    <strong>${nickname}</strong>
                </div>
                <div class="phone">
                    <p>${phone}</p>
                </div>
                <div class="registDay">
                    <p>${registDay}</p>
                </div>
                <div class="gender">
                    <p>${gender==='M'?'남자':gender==='F'?'여자':'기타'}</p>
                </div>
        `
        if(obj.state === 'Y' && (obj.user.email !== obj.party.user.email)){
            htmlList +=`
                <button class="kick arkb_btn btn" onclick="ARKB(${obj.id},'kick')">추방</button>
                <button class="black arkb_btn btn" onclick="ARKB(${obj.id},'black')">블랙</button>
                `
        }
        if(obj.state === 'W'){
            htmlList +=`
                <button class="approve arkb_btn btn" onclick="ARKB(${obj.id},'approve')">승인</button>
                <button class="reject arkb_btn btn" onclick="ARKB(${obj.id},'reject')">거절</button>
                `
        }
        if(obj.state === 'Y' && (obj.user.email !== obj.party.user.email)){
            htmlList += `</div>`;
        }
        htmlList += `</div>`;
        htmlList += `<hr>`
    });
    htmlList +=`</div>`
    htmlList +=`
              <div class="bottom-btn">
                    <button id="disband_btn" class="disband_btn btn">파티해체</button>  
                    <button class="delegate_btn btn" onclick="delegate()">파티위임</button>  
              </div>`
    htmlList +=`</div>`
    content.innerHTML = htmlList;


    /* 모달 */
    const modal = document.getElementById('modal');
    const btn = document.getElementById('disband_btn');
    const close = document.querySelectorAll('.close');

    btn.onclick = function(){
        modal.style.display = 'block';
    }
// (x)를 클릭하면 모달을 닫습니다
    close.forEach(closebtn=>{
        closebtn.onclick = function() {
            modal.style.display = 'none';
        }
    })

// 모달 외부를 클릭하면 모달을 닫습니다
    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    }
}

function ARKB(idx,type){
    if(isASKB_Btn)return;
    isASKB_Btn = true;
    let response = confirm(msg(type))
    if(!response)return;
    let data={
        state: null,
        isBlack: false,
        nickname: null
    }
    if(type === 'kick'){
        data.state = 'N';
    }
    if(type === 'black'){
        data.state = 'N';
        data.isBlack = true;
    }
    if(type === 'approve'){
        data.state = 'Y';
    }
    if(type === 'reject'){
        data.state = 'N';
    }
    if(type === 'cancel'){
        data.state = 'N';
    }
    fetch("/party/changeStatus/"+idx,{
        method : "PUT",
        headers:{
            "Content-Type":"application/json"
        },
        body: JSON.stringify(data)
    })
        .then(response => response.text())
        .then(data => {
            isASKB_Btn = false;
            if(data === 'ok'){
                alert("정상적으로 처리 되었습니다.")
                location.reload();
            }
            if(data === 'no login'){
                alert("로그인 후 이용가능 합니다.");
                location.href="/user/loginForm";
            }
            if(data === 'no partyInfo'){
                alert("데이터가 없습니다.");
                location.reload();
            }
            if(data === 'no host'){
                alert("호스트만 이용가능합니다.")
                location.reload();
            }
            if(data === 'host'){
                alert("호스트는 변경 불가합니다.")
                location.reload();
            }

        })
        .catch(error => console.log("Error =",error))
}
let msg =(type)=>{
    let data;
    if(type === 'kick'){
        data = '해당 회원을 추방 '
    }
    if(type === 'black'){
        data = '해당 회원을 블랙 '
    }
    if(type === 'approve'){
        data = '해당 회원을 승인 '
    }
    if(type === 'reject'){
        data = '해당 회원을 거절 '
    }
    return data + "하시겠습니까?";
}

function disband(partyName){
    const partyname = partyName.value.trim();
    if(partyname === ''){
        setMsg("파티이름을 입력해주세요.")
        return;
    }
    if(isDisbandBtn)return;
    isDisbandBtn=true;
    confirm("파티를 해체 하시겠습니까?")
    fetch(`/party/disbandParty/${partyId}/partyname/${partyname}`,{
        method : "DELETE"
    })
        .then(response => response.text())
        .then(result=>{
            isDisbandBtn=false;
            if(result==='ok'){
                alert("파티가 해체되었습니다.")
                location.reload();
                return;
            }
            if(result==='no party'){
                alert('파티가 존재하지 않습니다.')
                location.reload();
                return;
            }
            if(result === 'no host'){
                alert('권한이 없습니다.')
                location.reload();
                return;
            }
            if(result === 'no title'){
                setMsg('파티 이름을 정확하게 입력해주세요.')
            }
        })
        .catch(error=>console.log('Error =',error));
}
function setMsg(massage){
    msgSection.style.display="block";
    $msg.innerText = massage;
}

let isDelegateBtn = false;
function delegate() {
    const selectedRadio = document.querySelector('input[name="member"]:checked');
    if(isDelegateBtn)return;
    if(selectedRadio.value.trim() === '')return;
    const memberId = selectedRadio.value;
    isDelegateBtn = true;
    if(!confirm("정말로 위임하시겠습니까?"))return;

    fetch(`/party/delegate/${partyId}/userId/${memberId}`,{
        method : "PUT"
    })
        .then(response => response.text())
        .then(result=>{
            isDelegateBtn = false;
            if(result==='no party'){
                alert("파티가 존재하지 않습니다.");
                location.reload();
                return;
            }
            if(result==='no host'){
                alert('권한이 없습니다.');
                location.reload();
                return;
            }
            if(result==='error'){
                alert('처리중에 오류가 발생하였습니다. 다시시도해주세요')
                location.reload();
                return;
            }
            if(result==='no user'){
                alert('존재하지 않는 유저입니다.')
                location.reload();
                return;
            }
            alert( result +"님 에게 파티가 위임되었습니다.")
            location.reload();
        })
        .catch(error => console.log("Error = ",error))
}