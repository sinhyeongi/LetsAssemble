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

// 카테고리 한글로 변경
const cate_name = document.querySelector('.cate_name');
document.addEventListener("DOMContentLoaded", function () {


        // 카테고리에 따라 클래스를 추가하여 스타일을 적용
        switch (cate_name) {
            case "sports":
                cate_name.classList.add("category-sports");
                cate_name.innerHTML = '스포츠';
                break;
            case "travel":
                cate_name.classList.add("category-travel");
                cate_name.innerHTML = '여행';
                break;
            case "game":
                cate_name.classList.add("category-game");
                cate_name.innerHTML = '게임';
                break;
            case "study":
                cate_name.classList.add("category-study");
                cate_name.innerHTML = '스터디';
                break;
            case "boardGame":
                cate_name.classList.add("category-boardGame");
                cate_name.innerHTML ='보드게임';
                break;
            default:
                break;
        }
});
