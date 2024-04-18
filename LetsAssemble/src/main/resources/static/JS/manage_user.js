
const userContents = document.querySelectorAll('.user_content');
userContents.forEach(userContent => {
    userContent.addEventListener('click', function() {
        const userId = userContent.querySelector('input[name="user_id"]').value;
        console.log("userId = "+userId);
        if (confirm('이 회원을 탈퇴 시키시겠습니까?')) {
            deleteUser(userId);
        }
    });
});

function deleteUser(userId) {
    const url = '/admin/delete';
    fetch(url + '?userId=' + userId, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            console.log('회원 삭제 요청이 성공적으로 전송되었습니다.');
            alert('회원을 탈퇴시키는데 성공했습니다');
            // location.href="/admin/manage_user";
            location.reload();
        })
        .catch(error => {
            console.error('Error deleting user:', error);
            alert('회원을 탈퇴시키는데 실패했습니다');
        });
}