const content = document.querySelector('.content');
const atag = document.querySelectorAll('.link');

atag.forEach(tag => {
    tag.addEventListener("click",event => {
        if(event.target.tagName ==='button'){
            event.preventDefault();
            console.log(event.target);
        }
    })
})
function memberList(id){
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
    let htmlList=`<div class="memberList">
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
        if(a.state==='Y' && b.state==='W'){
            return 1;
        }
        if(a.black && !b.black){
            return 1;
        }
        return 0;
    })
    data.forEach(obj=>{
        console.log(obj)
        if(obj.state === 'N')return;
        //연락처
        const nickname = obj.user.nickname;
        const phone = obj.user.phone;
        //가입신청날짜
        const registDay = obj.aplicant_day;
        const gender = obj.user.gender;
        htmlList += `
            <div class="member-info">
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
                <button class="kick" onclick="ARKB(${obj.id},'kick')">추방</button>
                <button class="black" onclick="ARKB(${obj.id},'black')">블랙</button>
                `
        }
        if(obj.state === 'W'){
            htmlList +=`
                <button class="approve" onclick="ARKB(${obj.id},'approve')">승인</button>
                <button class="reject" onclick="ARKB(${obj.id},'reject')">거절</button>
                `
        }
        htmlList += `</div>`;
    });
    htmlList +=`</div>`
    content.innerHTML = htmlList;
}

function ARKB(idx,type){
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

function CAndE(partyId){
    console.log(partyId);
}