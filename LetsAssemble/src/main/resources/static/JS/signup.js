/*
function init(){
    inputBox("아이디","아이디를 입력해주세요","text","",undefined,)
}

function onIdChangeHandler(){

}

function inputBox(title, placeholder, type, value, isErrorMessage, buttonTitle, onChange, onKeyDown, onButtonClick , message){
    const buttonClass = value === '' ? 'input-box-button-disable' : 'input-box-button';
    const messageClass = isErrorMessage ? 'input-box-message-error' : 'input-box-message';

    //인풋박스 div 설정
    const inputBox= document.createElement("div");
    inputBox.classList.add("input-box");

    //인풋박스 타이틀 설정
    const inputBoxTitle = document.createElement("div");
    inputBoxTitle.classList.add("input-box-title");
    inputBoxTitle.innerText = title;
    //인풋박스 컨텐츠 설정

    const inputBoxContent = document.createElement("div");
    inputBoxContent.classList.add("input-box-content");

    //인풋박스 바디 설정
    const inputBoxBody = document.createElement("div");
    inputBoxBody.classList.add("input-box-body");
    const inputBoxInput = document.createElement("input");
    inputBoxInput.classList.add("input-box-input");
    inputBoxInput.type = type;
    inputBoxInput.value = value;
    inputBoxInput.placeholder = placeholder;
    inputBoxInput.onchange = onChange;
    inputBoxInput.onkeydown = onKeyDown;
    if(buttonTitle !== undefined && onButtonClick !== undefined){
        const inputBoxButton = document.createElement("div");
        inputBoxButton.classList.add(buttonClass)
        inputBoxButton.onclick(onButtonClick);
        inputBoxButton.innerText = buttonTitle;
        inputBoxBody.append(inputBoxButton);
    }
    if(message !== undefined){
        const inputBoxMessage = document.createElement("div");
        inputBoxMessage.classList.add(messageClass);

    }


    inputBoxBody.append(inputBoxInput);
    inputBoxContent.append(inputBoxBody);
    inputBox.append(inputBoxTitle);
    inputBox.append(inputBoxContent);

}*/
