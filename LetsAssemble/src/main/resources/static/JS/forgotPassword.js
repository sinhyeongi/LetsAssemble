const input = document.getElementById('emailInput');

const form = document.getElementById('form');

let isSubmitBtn = false;
let pwChangeBtn = false;

form.addEventListener('submit', function(event) {
    event.preventDefault();
});
input.addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        const form = document.getElementById('form');
        submitForm(form);
    }
});

function submitForm(form){
    if(isSubmitBtn)return;
    const email = form.email.value.trim();
    changeDisabledBtn(form.btn,"전송");
    fetch("/forgotEmail",{
        method : "POST",
        headers:{
            "Content-type":"application/json"
        },
        body: JSON.stringify({
            email:email
        })
    })
        .then(response => response.text())
        .then(result => {
            isSubmitBtn = false;
            changeDisabledBtn(form.btn,"전송");
            if(result === 'ok'){
                alert("인증번호가 정상적으로 발송 되었습니다")
                form.email.readOnly = true;
            }else if(result ==='buttonError'){
                alert("전송되었습니다. 이메일을 확인해주세요")
            }else if(result ==='emailError') {
                alert("이메일을 형식에 맞게 입력해주세요.")
            }else if(result ==='userError') {
                alert("존재하지않는 이메일 입니다.")
            }else{
                alert("알 수 없는 오류,다시시도해주세요")
            }
        })
        .catch(error => console.log(error));
}


function changeDisabledBtn(btn, msg){
    btn.disabled === true? btn.disabled=false:btn.disabled=true;
    if(btn.disabled){
        btn.classList.add("disabled");
    }else{
        btn.classList.remove("disabled");
    }
    btn.innerText = msg;
}


/* 새 비밀번호 입력 */
function pwSubmit(form){
    if(pwChangeBtn)return;
    pwChangeBtn=true;
    fetch("/user/resetPassword",{
        method :"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify({
            email : form.email.value.trim(),
            password1 : form.password1.value.trim(),
            password2 : form.password2.value.trim()
        })
    })
     .then(response => response.text())
        .then(result => {
            pwChangeBtn=false;
            if(result === 'ok'){
                alert("비밀번호가 정상적으로 변경되었습니다.\n" +
                    "변경된 비밀번호로 로그인해주세요.")
                location.href="/";
            }
            if(result === 'not user error'){
                location.href="error";
            }
            if(result === '"empty error"'){
                alert("비밀번호를 입력해주세요.");
            }
            if(result === 'not equals password'){
                alert("비밀번호가 맞지 않습니다.\n" +
                    " 비밀번호를 확인해주세요.");
            }
            if(result === 'not 8 characters'){
                alert("비밀번호를 8글자 이상 입력해주세요.");
            }
        })
}
