document.querySelectorAll('.login_btn').forEach(function(button) {
    button.addEventListener('click', function() {
        // 클릭된 버튼의 id 값을 추출
        var id = this.id;
        // URL에 id 값을 추가하여 이동
        window.location.href = '/oauth2/authorization/' + id;
    });
});