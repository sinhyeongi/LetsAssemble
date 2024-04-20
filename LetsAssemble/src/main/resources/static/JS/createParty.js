const onlineSection = document.querySelector('.isOnline_section');
const categoryList = [...document.getElementsByName('category')];
const capacitySection = document.querySelector('.capacity_section');
const isonlineList = [...document.getElementsByName('isOnline')];
let is1step = false;
let isAddress = false;
let isCreateBtn = false;

/* 카테고리 선택 시 하위 선택사항 생성*/
categoryList.forEach(category =>{
    category.addEventListener("change",(event)=>{
        onlineSection.classList.add("action");
    })
})
/* is온라인 선택 시 하위 선택사항 생성*/
isonlineList.forEach(category =>{
    category.addEventListener("change",(event)=>{
        capacitySection.classList.add("action");
    })
})
/* 인원수 조절 */
var slider = document.getElementById("capacity");
var output = document.getElementById("output");
output.innerHTML = slider.value + '명';

slider.oninput = function() {
    output.innerHTML = this.value + '명';
    is1step = true;
    const btn = document.getElementById('step1_btn');
    btn.classList.add("action");
}
function step1Btn(){
    if(!is1step)return;
    const step1 = document.getElementById("step1");
    step1.style.display = "none";
    step2.style.display = "block";
    document.getElementById("submitBtn").classList.add("action")

}

function submit(form){
    if(isCreateBtn)return;
    isCreateBtn = true;
    const formData = new FormData(form);
    fetch("/party/create",{
        method : "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(Object.fromEntries(formData)),
    })
        .then(response => response.text())
        .then(data => {
            isCreateBtn = false;
            const msgBox = document.querySelector(".msgBox");
            if(isNaN(data)){
                msgBox.style.display="block";
            }
            if(data === 'no phone'){
                location.href='/user/loginForm'
                return;
            }
            if(data === 'no category'){
                msgBox.innerHTML="관심사를 설정해주세요."
                return;
            }
            if(data === 'no isOnline'){
                msgBox.innerHTML="온라인여부를 설정해주세요."
                return;
            }
            if(data === 'no capacity'){
                msgBox.innerHTML="파티원 수를 설정해주세요."
                return;
            }
            if(data === 'no name'){
                msgBox.innerHTML="파티 이름을 설정해주세요."
                return;
            }
            if(data === 'no address'){
                msgBox.innerHTML="활동지역을 설정해주세요."
                return;
            }
            if( data === 'error') {
                location.href="/error";
                return;
            }
            location.href = "/party/update/"+data;
        });
}




/* 지역 검색 및 지도*/
var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new daum.maps.LatLng(37.4981646510326, 127.028307900881), // 지도의 중심좌표
        level: 1 // 지도의 확대 레벨
    };
//지도를 미리 생성
var map = new daum.maps.Map(mapContainer, mapOption);
//주소-좌표 변환 객체를 생성
var geocoder = new daum.maps.services.Geocoder();
//마커를 미리 생성
var marker = new daum.maps.Marker({
    position: new daum.maps.LatLng(37.4981646510326, 127.028307900881),
    map: map
});


function DaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = data.address; // 최종 주소 변수

            // 주소 정보를 해당 필드에 넣는다.
            document.getElementById("address").readOnly=true;
            document.getElementById("address").value = addr;
            mapContainer.style.display = "block";
            // 주소로 상세 정보를 검색
            geocoder.addressSearch(data.address, function(results, status) {
                // 정상적으로 검색이 완료됐으면
            console.log(status)
                if (status === daum.maps.services.Status.OK) {

                    var result = results[0]; //첫번째 결과의 값을 활용
                    // 해당 주소에 대한 좌표를 받아서
                    var coords = new daum.maps.LatLng(result.y, result.x);
                    // 지도를 보여준다.
                    map.relayout();
                    // 지도 중심을 변경한다.
                    map.setCenter(coords);
                    // 마커를 결과값으로 받은 위치로 옮긴다.
                    marker.setPosition(coords);
                    isAddress= true;
                    document.getElementById("submitBtn").classList.add("action")
                }
            });
        }
    }).open();
}