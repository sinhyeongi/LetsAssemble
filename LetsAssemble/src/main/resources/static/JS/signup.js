let isCertification = false;
let isSendMail = false;
let isPwCheck = false;
let isNickname = false;
let isName = false;
let isPhone = false;
let isAge = false;
let isSignup = false;

let intervalId = null;
let form ;
let inputBoxs = {
    emailInputBox: null,
    certificationInputBox: null,
    nameInputBox: null,
    nickNameInputBox: null,
    pw1InputBox: null,
    pw2InputBox: null,
    phoneInputBox: null,
    genderInputBox: null,
    ageInputBox:null,
};
let ageOptions = [];

window.onload = function() {
    form = document.querySelector(".signupForm");
    init();

}
function init(){
    console.log('email = '+ user['email'])
    console.log('name = '+ user['name'])
    console.log('nickname = '+ user['nickname'])
    console.log('phone = '+ user['phone'])
    createInputBox("이메일","이메일을 입력해주세요","email",user !== null?cheackEmail():'',"email","email",false,"이메일 인증",onEmailChangeHandler,undefined,undefined,onEmailButtonClickHandler,"이메일을 입력해주세요",'emailInputBox');
    createInputBox("인증번호", "인증번호를 입력해주세요", "text", "", "certify", "certify", false, "번호인증", onCertificationChangeHandler, undefined, undefined, onCertificationButtonClickHandler, "인증번호를 확인해주세요", 'certificationInputBox');
    createInputBox("이름","이름을 입력해주세요","text",user?.name,"name","name",false,undefined,onNameChangeHandler,undefined,undefined,undefined,"이름을 입력해주세요",'nameInputBox');
    createInputBox("닉네임","닉네임을 입력해주세요","text",user?.nickname || '',"nickname","nickname",false,"중복 확인",onNickNameChangeHandler,undefined,undefined,onNickNameButtonClickHandler,"사용가능한 닉네임 입니다.",'nickNameInputBox');
    if(user === null){
        createInputBox("비밀번호","비밀번호를 입력해주세요","password","","pw1","password",false,undefined,onPwChangeHandler,undefined,undefined,undefined,undefined,'pw1InputBox');
        createInputBox("비밀번호 확인","비밀번호를 입력해주세요","password","","pw2","password2",false,undefined,onPwChangeHandler,undefined,undefined,undefined,"비밀번호가 일치하지 않습니다.",'pw2InputBox');
    }else{
        createInputTypeHidden('hidden',user['provider'],'provider');
        createInputTypeHidden('hidden',user['providerId'],'providerId');
        isPwCheck = true;
    }
    createInputBox("휴대폰번호","휴대폰번호를 입력해주세요","tel",user?.tel,"phone","phone",false,undefined,onPhoneChangeHandler,undefined,onPhoneKeyup,undefined,"형식에 맞게 입력해주세요",'phoneInputBox');
    createInputBox("나이","나이를 입력해주세요","number",user?.age ||"","age","age",false,undefined,onAgeChangeHandler,undefined,onAgeKeyup,undefined,"나이를 입력해주세요",'ageInputBox');

    createRadioButton(
        '성별',
        'gender',
        [
            { id: 'male', value: 'male', label: '남성' ,selected: true},
            { id: 'female', value: 'female', label: '여성' },
            { id: 'other', value: 'other', label: '기타' }
        ],
        'genderInputBox',
        undefined
    );
    createButton('submit-button','회원가입',submit);
    inputBoxs.certificationInputBox.classList.add("display-none");
    if(inputBoxs.nameInputBox.querySelector('.input-box-input').value.trim().length > 1) isName = true;
}
/****************이메일*******************/
//Change Event
const onEmailChangeHandler = (event)=>{
    buttonClassChange(event);
    isCertification = false;
    deleteLine(form,'.email');
}
function cheackEmail(){
    const email = user['email'];
    const pattern =  /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$/;
    if(pattern.test(email)){
        return email;
    }
    return '';

}
const onEmailButtonClickHandler = async (event) => {
    if (isSendMail) return;
    if (isCertification)return;
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
        inputBoxs.emailInputBox.querySelector('.input-box-content').classList.remove('redLine');
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
            if(isCertification){
                countDiv.innerHTML = '인증이 완료되었습니다.';
                clearInterval(intervalId);
                return;
            }
            if (countdown <= 0) {
                countDiv.classList.add('display-none');
                clearInterval(intervalId);
                certificationInputBox.classList.add('display-none');
                inputBoxs.emailInputBox.querySelector('.input-box-input').readOnly = false;
                inputBoxs.emailInputBox.querySelector('.input-box-button').innerText = "이메일 인증"
                isCertification = false;
            }
        }, 1000); // 1초마다 실행
        countDiv.innerHTML = '남은 시간: ' + countdown + '초';
        countDiv.classList.remove('display-none');
        countDiv.classList.remove('error');

    }else{//이메일 전송 실패
        isSendMail=false;
        isCertification = false;
        emailButton.innerText = "이메일 인증"
        emailButton.classList.remove('disabled');
        emailMessage.classList.add('error');
        emailMessage.classList.remove('display-none');
        emailMessage.innerText = "이메일을 형식에 맞게 작성해주세요.";
    }
}

/****************이메일 인증 번호*******************/

const onCertificationChangeHandler= (event)=>{
    buttonClassChange(event);
}
const onCertificationButtonClickHandler = (event)=> {
    if(isCertification)return;
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
    })
    .then(response =>response.text())
    .then(result => {
        console.log(result);
        //인증 완료
        if(result ==='ok'){
            clearInterval(intervalId);
            isCertification = true;
            setPossibleMessage(inputBoxs.certificationInputBox.querySelector('.input-box-message'),'인증이 완료되었습니다.');
            deleteErrorMessage(inputBoxs.emailInputBox.querySelector('.input-box-message'));
            //이메일,인증번호 수정 막기
            inputBoxs.emailInputBox.querySelector('.input-box-input').readOnly = true;
            inputBoxs.certificationInputBox.querySelector('.input-box-input').readOnly = true;
            //버튼 잠그기
            disabledButton(inputBoxs.certificationInputBox);
            disabledButton(inputBoxs.emailInputBox,'인증완료');
        }else{
            //인증실패
            setErrorMessage(inputBoxs.certificationInputBox.querySelector('.input-box-message'),'인증 번호가 맞지 않습니다.');
            isCertification=false;
        }
    })
}
/****************이름*******************/
const onNameChangeHandler = (event) =>{
    const value = event.target.value.trim();
    const messageBox = inputBoxs.nameInputBox.querySelector('.input-box-message')
    if(value ===''){
        setErrorMessage(messageBox,'이름을 입력해주세요');
        isName=false;
        return;
    }
    deleteLine(form,'.name');
    deleteErrorMessage(messageBox);
    isName=true;
}

/****************닉네임*******************/
const onNickNameChangeHandler= (event)=>{
    buttonClassChange(event);
    deleteLine(form,'.nickname');
    deleteErrorMessage(inputBoxs.nickNameInputBox.querySelector('.input-box-message'));
    isNickname=false;
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
            console.log('닉네임=' + result);
            if(result === 'true'){
                //닉네임 중복
                setErrorMessage(message,"이미 사용중인 닉네임 입니다.");
            }else{
                //사용 가능
                setPossibleMessage(message,"사용 가능한 닉네임입니다.");
                deleteLine(form,'.nickname')
                isNickname=true;
            }
        })
}

/****************비밀번호*******************/
function pwCheck(password,message){
    if(password.length < 4){
        setErrorMessage(message,'4글자 이상 입력하세요.');
        return false;
    }else{
        deleteErrorMessage(message);
    }
    if(password.indexOf(" ") !== -1){
        setErrorMessage(message,'공백문자는 사용할 수 없습니다.');
        return false;
    }
    deleteLine(form,'.pw1');
    deleteLine(form,'.pw2');
    return true;
}
const onPwChangeHandler = (event) =>{
    const message = inputBoxs.pw2InputBox.querySelector('.input-box-message');
    const pw1 = inputBoxs.pw1InputBox.querySelector('.input-box-input').value;
    const pw2 = inputBoxs.pw2InputBox.querySelector('.input-box-input').value;
    isPwCheck = false;
    if(!pwCheck(pw2,message)){
        return;
    }
    if(pw1 !== pw2){
        setErrorMessage(message,'비밀번호가 일치하지 않습니다.')
        return;
    }
    if(pw2.indexOf(" ") !== -1){
        setErrorMessage(message,"공백문자 사용불가");
        return;
    }
    deleteErrorMessage(message);
    isPwCheck =true;
}
/****************연락처*******************/

const onPhoneChangeHandler = (event) => {
    const value = event.target.value.trim();
    const messageBox = inputBoxs.phoneInputBox.querySelector('.input-box-message')
    isPhone=false;
    const phoneNumberPattern = /^\d{3}-\d{4}-\d{4}$/;
    if(value ===''){
        setErrorMessage(messageBox,"연락처를 입력해주세요.");
        return;
    }
    if(!phoneNumberPattern.test(value)){
        setErrorMessage(messageBox,"연락처를 형식에 맞게 입력해주세요");
        return;
    }
    deleteLine(form,'.phone');
    deleteErrorMessage(messageBox);
    isPhone =true;
}
const onPhoneKeyup = (event)=>{
    const val = event.target.value.trim();
    event.target.value = autoHyphen(val);
}

function autoHyphen(str) {
    str = str.replace(/[^0-9]/g, '');
    var tmp = '';

    // 핸드폰 및 다른 지역 전화번호 일 경우
    if (str.length < 4) {
        return str;
    } else if (str.length < 7) {
        tmp += str.substring(0, 3);
        tmp += '-';
        tmp += str.substring(3);
        return tmp;
    } else if (str.length < 11) {
        tmp += str.substring(0, 3);
        tmp += '-';
        tmp += str.substring(3, 7);
        tmp += '-';
        tmp += str.substring(7);
        return tmp;
    } else {
        tmp += str.substring(0, 3);
        tmp += '-';
        tmp += str.substring(3, 7);
        tmp += '-';
        tmp += str.substring(7,11);
        return tmp;
    }
}
/****************나이*******************/

const onAgeChangeHandler = (event) => {
    const value = event.target.value;
    const messageBox = inputBoxs.ageInputBox.querySelector('.input-box-message')
    isAge = false;
    if(value.trim() ===''){
        setErrorMessage(messageBox,"나이를 입력해주세요.");
        isAge=false;
        return;
    }
    if(value < 10 || value > 100){
        setErrorMessage(messageBox,"나이를 제대로 입력해주세요");
        return;
    }
    deleteLine(form,'.age');
    deleteErrorMessage(messageBox);
    isAge =true;
}
const onAgeKeyup = (event)=>{
    let value = event.target.value.trim();
    value = value.replace(/[^0-9]/g, '');
    event.target.value = value;
}

/******************서밋********************/
function submit(){
    if(isSignup)return;
    const email = inputBoxs.emailInputBox.querySelector('.input-box-input').value.trim();
    const signupForm = document.querySelector(".signupForm");
    let result;
    emailValidate(email).then(data => {result = data});
    //중복검사
    if(result === "true"){
        const emailMessage = inputBoxs.emailInputBox.querySelector('.input-box-message');
        emailMessage.classList.add("error");
        emailMessage.innerText = "이미 가입된 이메일 입니다.";
        return;
    }
    //이메일인증
    if(!isCertification) {
        redLine(signupForm, '.email');
        setErrorMessage(inputBoxs.emailInputBox.querySelector('.input-box-message'),"이메일 인증을 해주세요");
        return;
    }
    //이름
    if(!isName){
        redLine(signupForm,'.name');
        setErrorMessage(inputBoxs.nameInputBox.querySelector('.input-box-message'),"이름을 입력해주세요");
        return;
    }
    //비밀번호체크
    if(!isPwCheck){
        redLine(signupForm, '.pw2');
        redLine(signupForm, '.pw1');
        setErrorMessage(inputBoxs.pw2InputBox.querySelector('.input-box-message'),"비밀번호를 확인해 주세요");
        return;
    }
    //닉네임중복
    if(!isNickname){
        redLine(signupForm, '.nickname');
        setErrorMessage(inputBoxs.nickNameInputBox.querySelector('.input-box-message'),"중복체크를 해주세요");
        return;
    }
    //연락처
    if(!isPhone){
        redLine(signupForm,'.phone');
        setErrorMessage(inputBoxs.phoneInputBox.querySelector('.input-box-message'),"형식에 맞게 입력 해주세요");
        return;

    }
    //나이
    if(!isAge){
        redLine(signupForm,'.age');
        setErrorMessage(inputBoxs.ageInputBox.querySelector('.input-box-message'),"나이를 제대로 입력 해주세요");
        return;
    }
    isSignup=true;
    const formData = new FormData(signupForm);
    fetch("/user", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(Object.fromEntries(formData)),
    })
        .then(response=> response.text())
        .then(result => {
            if(result === 'ok'){
                let msg = '회원가입이 완료 되었습니다.'
                alert(msg);
                location.href="/";
            }else{
                let msg = '회원가입이 정상적으로 처리되지 못했습니다. 다시 시도 해주세요';
                alert(msg);
                isSignup=false;
            }
        })
}

//사용가능메세지(블루메세지)
function setPossibleMessage(element,text){
    element.classList.remove('error');
    element.classList.remove('display-none');
    element.innerText=text;
}
//에러메세지 함수(레드메세지)
function setErrorMessage(element,text){
    element.classList.add('error');
    element.classList.remove('display-none');
    element.innerText=text;
}
//버튼 잠그기
function disabledButton(element,text){
    element.querySelector('.input-box-button').classList.add('disabled');
    element.querySelector('.input-box-button').classList.remove('btn-active');
    if(text !== undefined){
        element.querySelector('.input-box-button').innerText=text;
    }
}

//메세지 없애기
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

//회원가입 버튼 생성
function createButton(id,value,onclick){
    const Button = document.createElement('input');
    Button.type = 'button';
    Button.id = id;
    Button.value= value;
    Button.onclick = onclick;
    const buttons = document.querySelector(".buttons");
    form.append(Button);
}

//빨간 테두리 생성
function redLine(form, target){
    target = form.querySelector(target);
    target.classList.add('redLine');
    target.firstChild.firstChild.focus();
}
//테두리 없애기
function deleteLine(form,target){
    target = form.querySelector(target);
    target.classList.remove('redLine');
}

//radio 생성 함수
function createRadioButton(title, name, options, querySelector, onChange) {
    // 라디오 버튼 그룹의 타이틀 설정
    const inputBox= document.createElement("div");
    inputBoxs[querySelector] = inputBox;
    inputBox.classList.add("input-box");
    form.append(inputBox);

    const radioGroupTitle = document.createElement("div");
    radioGroupTitle.classList.add("input-box-title");
    radioGroupTitle.innerText = title;
    inputBox.append(radioGroupTitle);

    const inputBoxContent = document.createElement("div");
    inputBoxContent.classList.add("radio-box-content");
    inputBox.append(inputBoxContent);

    // 라디오 버튼과 레이블을 생성하는 반복문
    options.forEach(option => {
        const inputWrapper = document.createElement("div");
        inputWrapper.classList.add("radio-input-wrapper");

        const radioInput = document.createElement("input");
        radioInput.type = "radio";
        radioInput.id = option.id;
        radioInput.name = name;
        radioInput.value = option.value;
        radioInput.onchange = onChange;
        inputWrapper.append(radioInput);

        const radioLabel = document.createElement("label");
        radioLabel.setAttribute("for", option.id);
        radioLabel.innerText = option.label;
        inputWrapper.append(radioLabel);

        if (option.selected) {
            radioInput.checked = true;
        }
        inputBoxContent.append(inputWrapper);
    });
}

function createInputTypeHidden(type,value,inputName){
    const inputBoxInput = document.createElement("input");
    inputBoxInput.type = type;
    inputBoxInput.value = value === undefined?'':value;
    inputBoxInput.name = inputName;
    form.append(inputBoxInput);

}
//inputBox 생성 함수
function createInputBox(title, placeholder, type, value, className, inputName, isErrorMessage, buttonTitle, onChange, onKeyDown,onKeyUp, onButtonClick , message , querySelector){

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
    inputBoxContent.classList.add(className)
    inputBox.append(inputBoxContent);

    //인풋박스 바디 설정
    const inputBoxBody = document.createElement("div");
    inputBoxBody.classList.add("input-box-body");
    inputBoxContent.append(inputBoxBody);

    //인풋박스 인풋 설정
    const inputBoxInput = document.createElement("input");
    inputBoxInput.classList.add("input-box-input");
    inputBoxInput.type = type;
    inputBoxInput.value = value === undefined || value === null || value === 'null' ?'':value;
    inputBoxInput.name = inputName;
    inputBoxInput.placeholder = placeholder;
    inputBoxInput.onchange = onChange;
    inputBoxInput.onkeydown = onKeyDown;
    inputBoxInput.addEventListener('keyup',onKeyUp);
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
