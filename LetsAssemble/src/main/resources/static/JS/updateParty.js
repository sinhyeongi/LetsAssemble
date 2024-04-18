function DaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = data.address; // 최종 주소 변수

            // 주소 정보를 해당 필드에 넣는다.
            document.getElementById("address").readOnly=true;
            document.getElementById("address").value = addr;
            // 주소로 상세 정보를 검색
            // 정상적으로 검색이 완료됐으면
        }
    }).open();
}
/* 인원수 조절 */
var slider = document.getElementById("capacity");
var output = document.getElementById("output");
output.innerHTML = slider.value + '명';

slider.oninput = function() {
    output.innerHTML = this.value + '명';
}


//커밋

function submit(form){
    const formData = new FormData(form);
    fetch("/party/update",{
        method: "POST",
        headers:{
            "Content-Type":"application/json",
        },
        body: JSON.stringify(Object.fromEntries(formData))
    })
        .then(response => response.text())
        .then(data => {
            if(data === 'ok'){
                location.href="/manage/myParty"
            }else{
                location.href="/error";
            }
        })
        .catch(error => {
            alert(error);
            location.href="/error";
        })
}