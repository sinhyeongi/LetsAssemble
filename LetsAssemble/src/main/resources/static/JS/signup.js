let isCertification = false;
let isSendMail = false;
let isPwCheck = false;
let isNickname = false;

let intervalId = null;
let form ;
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
    createInputBox("이메일","이메일을 입력해주세요","email","",false,"이메일 인증",onEmailChangeHandler,undefined,undefined,onEmailButtonClickHandler,"이메일을 입력해주세요",'emailInputBox');
    createInputBox("인증번호","인증번호를 입력해주세요","text","",false,"번호인증",onCertificationChangeHandler,undefined,undefined,onCertificationButtonClickHandler,"인증번호를 확인해주세요",'certificationInputBox');
    createInputBox("비밀번호","비밀번호를 입력해주세요","password","",false,undefined,onPwChangeHandler,undefined,undefined,undefined,undefined,'pw1InputBox');
    createInputBox("비밀번호 확인","비밀번호를 입력해주세요","password","",false,undefined,onPwChangeHandler,undefined,undefined,undefined,"비밀번호가 일치하지 않습니다.",'pw2InputBox');
    createInputBox("닉네임","닉네임을 입력해주세요","text","",false,"중복 확인",onNickNameChangeHandler,undefined,undefined,onNickNameButtonClickHandler,"사용가능한 닉네임 입니다.",'nickNameInputBox');
}
///////////////////////////////////////////이메일
const onEmailChangeHandler = (event)=>{
    buttonClassChange(event);
}
const onEmailButtonClickHandler = async (event) => {
    if (isSendMail) return;
    const email = event.target.parentElement.firstChild.value.trim();
    if(email === "") return;
    const emailMessage=inputBoxs.emailInputBox.querySelector(".input-box-message");
    const emailButton = inputBoxs.emailInputBox.querySelector(".input-box-button");
    const emailInput = inputBoxs.emailInputBox.querySelector('.input-box-input');
    const button = event.target.parentElement.parentElement.querySelector(".input-box-button");

    let result;
    await emailValidate(email).then(data => {result = data});
    //중복검사
    if(result === "true"){
        emailMessage.classList.add("error");
        emailMessage.innerText = "이미 가입된 이메일 입니다.";
        return;
    }
    //발송된 메일 존재
    isSendMail = true;

    emailButton.innerText = "이메일 발송중"
    emailButton.classList.add("disabled");
    emailButton.classList.remove("btn-active");
    emailCertification(email, emailMessage, emailButton, inputBoxs.certificationInputBox);
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
        clearInterval(intervalId);
        certificationInputBox.classList.remove("display-none");
        inputBoxs.emailInputBox.querySelector('.input-box-input').readOnly = true;
        emailButton.innerText = "재발송"
        emailButton.classList.remove("disabled");
        emailButton.classList.add("btn-active");
        isSendMail = false;

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
        countDiv.classList.remove('display-none')
    }else{//이메일 전송 실패
        isSendMail=false;
        emailButton.innerText = "이메일 인증"
        emailButton.classList.remove('disabled');
        emailMessage.classList.add('error');
        emailMessage.classList.remove('display-none');
        emailMessage.innerText = "이메일을 형식에 맞게 작성해주세요.";
    }
}

///////////////////////////////////////////////인증번호

const onCertificationChangeHandler= (event)=>{
    buttonClassChange(event);
}
const onCertificationButtonClickHandler = (event)=> {
    const authNumber = inputBoxs.certificationInputBox.querySelector('.input-box-input').value.trim();
    if(authNumber === '')return;
    CertificationCheck(authNumber);
}
function CertificationCheck(authNum){
    const email = inputBoxs.emailInputBox.querySelector('.input-box-input').value;
    fetch("/mailAuthCheck",{
        method : "POST",
        headers : {
            "Content-Type": "application/json",
        },
        body : JSON.stringify({
            email:email,
            authNum:authNum
        }),
    }).then(response =>response.text())
        .then(result => {
            //인증 완료
            if(result ==='ok'){
                clearInterval(intervalId);
                isCertification = true;
                inputBoxs.certificationInputBox.querySelector('.input-box-message').innerText="인증이 완료되었습니다."
                inputBoxs.certificationInputBox.querySelector('.input-box-message').classList.remove('error');
                inputBoxs.certificationInputBox.querySelector('.input-box-input').readOnly = true;
                inputBoxs.certificationInputBox.querySelector('.input-box-button').classList.add('disabled');
                inputBoxs.emailInputBox.querySelector('.input-box-message').classList.add('display-none');
            //인증실패
            }else{
                inputBoxs.certificationInputBox.querySelector('.input-box-message').innerText="인증 번호가 맞지않습니다."
                inputBoxs.certificationInputBox.querySelector('.input-box-message').classList.add('error');
            }
        })
}

////////////////////////////////////////////////비밀번호
function pwCheck(password,message){
    if(password.length < 4){
        setErrorMessage(message,'4글자 이상 입력하세요.');
        return false;
    }else{
        deleteErrorMessage(message);
    }
    if(password.indexOf(" ") !== -1){
        console.log(password);
        setErrorMessage(message,'공백문자는 사용할 수 없습니다.');
        return false;
    }
    return true;
}
const onPwChangeHandler = (event) =>{
    const message = inputBoxs.pw2InputBox.querySelector('.input-box-message');
    const pw1 = inputBoxs.pw1InputBox.querySelector('.input-box-input').value;
    const pw2 = inputBoxs.pw2InputBox.querySelector('.input-box-input').value;
    if(!pwCheck(pw2,message)){
        return;
    }
    if(pw1 !== pw2){
        setErrorMessage(message,'비밀번호가 일치하지 않습니다.')
        return;
    }else{
        deleteErrorMessage(message);
    }

    isPwCheck =true;
}
////////////////////////////////////닉네임
const onNickNameChangeHandler= (event)=>{
    buttonClassChange(event);
}
const onNickNameButtonClickHandler = (event) => {
    const nickname = inputBoxs.nickNameInputBox.querySelector('.input-box-input').value;
    const message = inputBoxs.nickNameInputBox.querySelector('.input-box-message');
    if(nickname.indexOf(" ") !== -1){
        setErrorMessage(message,"공백문자 사용불가")
        return;
    }
    if(nickname.length < 3){
        setErrorMessage(message,"3글자 이상 입력해주세요")
        return;
    }
    fetch(`/user/validate/nickname?nickname=${nickname}`)
        .then(response => response.text())
        .then(result=>{
            if(result === true){
                //닉네임 중복
                setErrorMessage(message,"이미 사용중인 닉네임 입니다.")

            }else{
                //사용 가능
                setPossibleMessage(message,"사용 가능한 닉네임입니다.")

            }
        })


}




function setPossibleMessage(element,text){
    element.classList.remove('error');
    element.classList.remove('display-none');
    element.innerText=text;
}
//에러메세지 함수
function setErrorMessage(element,text){
    element.classList.add('error');
    element.classList.remove('display-none');
    element.innerText=text;
}
//에러메세지 종료 함수
function  deleteErrorMessage(element){
    element.classList.add('display-none');
}
//버튼 변경
function buttonClassChange(event){
    const inputValue = event.target.value;
    const button = event.target.parentElement.lastChild;
    if(inputValue.trim()){
        button.classList.remove('disabled');
        button.classList.add('btn-active');
    }else{
        button.classList.remove('btn-active');
        button.classList.add('disabled');
    }
}

//inputBox 생성 함수
function createInputBox(title, placeholder, type, value, isErrorMessage, buttonTitle, onChange, onKeyDown,onKeyUp, onButtonClick , message , querySelector){

    const signupForm = document.querySelector(".signupForm");

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
    inputBoxInput.onKeyUp = onKeyUp;
    inputBoxBody.append(inputBoxInput);

    if(buttonTitle !== undefined && onButtonClick !== undefined) {
        const inputBoxButton = document.createElement("div");
        inputBoxButton.classList.add('input-box-button');
        if(!value){
            inputBoxButton.classList.add("disabled");
        }else{
            inputBoxButton.classList.add('btn-active');
        }
        inputBoxButton.innerText = buttonTitle;
        inputBoxButton.onclick = onButtonClick;

        inputBoxBody.append(inputBoxButton);
    }

    if(message !== undefined){
        const inputBoxMessage = document.createElement("div");
        inputBoxMessage.classList.add('input-box-message');
        if(!isErrorMessage){
            inputBoxMessage.classList.remove('error')
            inputBoxMessage.classList.add('display-none')
        }else{
            inputBoxMessage.classList.add('error')
        }
        inputBoxMessage.innerText = message;
        inputBoxContent.append(inputBoxMessage);
    }
}
