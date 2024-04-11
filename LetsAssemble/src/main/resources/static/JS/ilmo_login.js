function checkPassword() {
    var inInput = document.getElementById('idInput');
    var passwordInput = document.getElementById('passwordInput');
    var loginButton = document.getElementById('loginButton');

    if (idInput.value.trim() === '' || passwordInput.value.trim() === '') {
        loginButton.classList.add('disabled');
        loginButton.disabled = true;
    } else {
        loginButton.classList.remove('disabled');
        loginButton.disabled = false;
    }
}

function validateForm() {
    var idInput = document.getElementById('idInput');
    var passwordInput = document.getElementById('passwordInput');

    if(idInput.value.trim() === ''){
        alert('아이디를 입력해주세요.');
        return false;
    }
    if (passwordInput.value.trim() === '') {
        alert('비밀번호를 입력해주세요.');
        return false; // 폼 제출을 중지합니다.
    }

    return true; // 폼을 제출합니다.
}