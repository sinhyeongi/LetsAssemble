
const userContents = document.querySelectorAll('.user_content');
userContents.forEach(userContent => {
    userContent.addEventListener('click', function() {
        const partyId = userContent.querySelector('input[name="party_id"]').value;
        if (confirm('이 파티를 해체 시키시겠습니까?')) {
            deleteParty(partyId);
        }
    });
});

function deleteParty(partyId) {
    const url = '/admin/deleteParty';
    fetch(url + '?partyId=' + partyId, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            alert('파티를 해체시키는데 성공했습니다');
            // location.href="/admin/manage_user";
            location.reload();
        })
        .catch(error => {
            console.error('Error deleting user:', error);
            alert('파티를 해체시키는데 실패했습니다');
        });
}