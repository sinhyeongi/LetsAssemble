
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

document.addEventListener("DOMContentLoaded", function () {
    const cate_names = document.querySelectorAll('.cate_name');

    cate_names.forEach(cate_name => {
        const text = cate_name.textContent.trim();

        switch (text) {
            case "sports":
                cate_name.classList.add("category-sports");
                cate_name.textContent = '스포츠';
                break;
            case "travel":
                cate_name.classList.add("category-travel");
                cate_name.textContent = '여행';
                break;
            case "game":
                cate_name.classList.add("category-game");
                cate_name.textContent = '게임';
                break;
            case "study":
                cate_name.classList.add("category-study");
                cate_name.textContent = '스터디';
                break;
            case "boardGame":
                cate_name.classList.add("category-boardGame");
                cate_name.textContent = '보드게임';
                break;
            default:
                break;
        }
    });
});