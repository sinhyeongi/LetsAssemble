let isCertification = false;
let isSendMail = false;
let form ;
let intervalId = null;
let inputBoxs = {
    emailInputBox: null,
    certificationInputBox: null,
    pw1InputBox: null,
    pw2InputBox: null,
    nickNameInputBox: null
};
window.onload = function() {
    init();
    form = document.querySelector(".signupForm");
    inputBoxs.certificationInputBox.classList.add("display-none");
}
function init(){
    createInputBox("이메일","이메일을 입력해주세요","email","",false,"이메일 인증",onEmailChangeHandler,undefined,onEmailButtonClickHandler,"이메일을 입력해주세요",'emailInputBox');
    createInputBox("인증번호","인증번호를 입력해주세요","text","",false,"번호인증",onCertificationChangeHandler,undefined,onCertificationButtonClickHandler,"인증번호를 확인해주세요",'certificationInputBox');
    createInputBox("비밀번호","비밀번호를 입력해주세요","password","",false,undefined,undefined,undefined,undefined,undefined,'pw1InputBox');
    createInputBox("비밀번호 확인","비밀번호를 입력해주세요","password","",true,undefined,onPwChangeHandler,undefined,undefined,"비밀번호가 일치하지 않습니다.",'pw2InputBox');
    createInputBox("닉네임","닉네임을 입력해주세요","text","",true,"중복 확인",undefined,undefined,undefined,"사용가능한 닉네임 입니다.",'nickNameInputBox');
}
//이메일
const onEmailChangeHandler = (event)=>{
    buttonClassChange(event);
}
const onEmailButtonClickHandler = async (event) => {
    if (isSendMail) return;
    const email = event.target.parentElement.firstChild.value;
    const emailMessage=inputBoxs.emailInputBox.getElementsByClassName("input-box-message");
    const emailButton = inputBoxs.emailInputBox.getElementsByClassName("input-box-button");
    const emailInput = inputBoxs.emailInputBox.getElementsByClassName('input-box-input');
    emailInput.setAttribute("readOnly",true);

    const button = event.target.parentElement.parentElement.getElementsByClassName("input-box-button");
    let result;
    await emailValidate(email).then(data => {result = data});
    //중복검사
    if(result === "true"){
        emailMessage.className = "input-box-message-error";
        emailMessage.innerText = "이미 가입된 이메일 입니다.";
        return;
    }
    //발송된 메일 존재
    console.log("button = " +  emailButton);
    isSendMail = true;
    emailButton.innerText = "이메일 발송중"
    emailButton.className = "input-box-button-disabled"
    //emailCertification(email, emailMessage, emailButton, inputBoxs.certificationInputBox);
}

async function emailValidate(email) {
    let validate;
    await fetch(`user/validate?email=${email}`)
        .then(result => {validate = result.text()})
    return validate;
}
//await
//async 비동기 먼저 처리 후 실행하고싶을 때
function emailCertification(email, emailMessage, emailButton, certificationInputBox) {
     fetch("/mailSend", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            email: email
        }),
    })
        .then((response) => response.text())
        .then((result) => {
            success(result, emailMessage, emailButton, certificationInputBox)
        });
}

//인증메일 성공적으로 발송 시
function success(result  , emailMessage, emailButton, certificationInputBox){
    if(result === "success"){
        viewInput(certificationInputBox);
        inputBoxs.emailInputBox.getElementsByClassName('input-box-input').readOnly = true;
        emailButton.innerText = "이메일 발송됨"

        inputBoxs.emailInputBox.setAttribute("disabled","disabled");
        // div 태그를 선택합니다.
        const countDiv = emailMessage;
        // 카운트다운 시간을 설정합니다. (3분 = 180초)
        let countdown = 180;
        // 카운트다운을 시작합니다.
        intervalId = setInterval(function() {
            // 카운트다운 시간을 감소시킵니다.
            countdown--;
            // div 태그에 남은 시간을 표시합니다.
            countDiv.innerHTML = '남은 시간: ' + countdown + '초';
            // 카운트다운이 0이 되면 div 태그를 숨기고 카운트다운을 중지합니다.
            if (isCertification || countdown <= 0) {
                countDiv.style.display = 'none';
                clearInterval(intervalId);
            }
        }, 1000); // 1초마다 실행
    }else{
        isSendMail=false;
        emailButton.innerText = "이메일 인증"
        emailButton.className = 'input-box-button';
        emailMessage.className = "input-box-message-error";
        emailMessage.innerText = "이메일을 확인해주세요.";
    }
}

//이메일 인증
const onCertificationChangeHandler= (event)=>{
    buttonClassChange(event);
}
const CertificationKeyDownHandler= (event)=> {}
const onCertificationButtonClickHandler = (event)=> {}

const onPwChangeHandler = (event) =>{
}

function viewInput(input){
    input.classList.remove("display-none");
}



//버튼 변경
function buttonClassChange(event){
    const inputValue = event.target.value;
    const button = event.target.parentElement.lastChild;
    button.className = inputValue.trim() ? 'input-box-button' : 'input-box-button-disabled';
}

//inputBox 생성 함수
function createInputBox(title, placeholder, type, value, isErrorMessage, buttonTitle, onChange, onKeyDown, onButtonClick , message , querySelector){

    const signupForm = document.querySelector(".signupForm");
    const buttonClass= value ? 'input-box-button' : 'input-box-button-disabled';
    const messageClass = isErrorMessage ? 'input-box-message-error' : 'input-box-message';

    //인풋박스 div 설정
    const inputBox= document.createElement("div");
    inputBoxs[querySelector] = inputBox;
    inputBox.classList.add("input-box");
    signupForm.append(inputBox);

    //인풋박스 타이틀 설정
    const inputBoxTitle = document.createElement("div");
    inputBoxTitle.classList.add("input-box-title");
    inputBoxTitle.innerText = title;
    inputBox.append(inputBoxTitle);

    //인풋박스 컨텐츠 설정
    const inputBoxContent = document.createElement("div");
    inputBoxContent.classList.add("input-box-content");
    inputBox.append(inputBoxContent);

    //인풋박스 바디 설정
    const inputBoxBody = document.createElement("div");
    inputBoxBody.classList.add("input-box-body");
    inputBoxContent.append(inputBoxBody);

    const inputBoxInput = document.createElement("input");
    inputBoxInput.classList.add("input-box-input");
    inputBoxInput.type = type;
    inputBoxInput.value = value;
    inputBoxInput.placeholder = placeholder;
    inputBoxInput.onchange = onChange;
    inputBoxInput.onkeydown = onKeyDown;
    inputBoxBody.append(inputBoxInput);

    if(buttonTitle !== undefined && onButtonClick !== undefined) {
        const inputBoxButton = document.createElement("div");
        inputBoxButton.classList.add(buttonClass);
        inputBoxButton.classList.add("disabled");
        inputBoxButton.innerText = buttonTitle;
        inputBoxButton.onclick = onButtonClick;

        inputBoxBody.append(inputBoxButton);
    }

    if(message !== undefined){
        const inputBoxMessage = document.createElement("div");
        inputBoxMessage.classList.add(messageClass);
        inputBoxMessage.innerText = message;
        inputBoxContent.append(inputBoxMessage);
    }


}
