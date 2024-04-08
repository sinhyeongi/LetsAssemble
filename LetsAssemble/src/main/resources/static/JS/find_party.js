function adMyParty(event){
    const isValidClass = event.querySelector('.ad_my_party');
    if(isValidClass){
    //     내 파티 홍보하기 클래스가 있으면 파티 홍보 페이지로 이동
        console.log('홈으로 이동해야함')
        location.href = "/";
    } else{
        // 파티 인포 페이지로 이동
        const hiddenInput = event.querySelector('input[type="hidden"]');
        const party_id = hiddenInput.value;
        console.log("party_id = " + party_id);
        location.href=`/party_info?id=${party_id}`;
    }
}

function go_partyInfo(event){
    const hiddenInput = event.querySelector('input[type="hidden"]');
    const party_id = hiddenInput.value;
    console.log("party_id = " + party_id);
    location.href=`/party_info?id=${party_id}`;
}

const spans = document.querySelectorAll('.find_category span');

spans.forEach(function (span) {
    span.addEventListener('click', function (event) {

        // 기존에 생성된 밑줄 요소가 있는지 확인
        const existingUnderlines = document.querySelectorAll('.underline');
        existingUnderlines.forEach(function (underline) {
            underline.parentNode.removeChild(underline); // 모든 밑줄 요소 삭제
        });
        spans.forEach(function (s) {
            s.classList.remove('clicked');
        });
        // 클릭된 span 요소에 밑줄 추가
        const underline = document.createElement('div');
        underline.classList.add('underline');
        span.appendChild(underline);
        span.classList.add('clicked');
    });
});