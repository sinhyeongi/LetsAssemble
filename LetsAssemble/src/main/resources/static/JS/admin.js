const sirenTab = document.querySelector('.manage_siren');
const partyTab = document.querySelector('.manage_party');
const userTab = document.querySelector('.manage_user');

const manage_header = document.querySelector('.manage_header');
const main = document.querySelector('.detail-content_main');

sirenTab.addEventListener('click', function () {
    manage_header.innerHTML =
        `신고 관리 <img width="40" height="40"
                      src="https://img.icons8.com/color/48/siren.png" alt="siren" /> `;
    goManage('신고관리');
});

partyTab.addEventListener('click', function () {
    manage_header.innerHTML =
        `파티 관리 <img width="50" height="50"
                      src="https://img.icons8.com/bubbles/50/meeting.png" alt="meeting" /> `;
    goManage('파티관리');
});

userTab.addEventListener('click', function () {
    manage_header.innerHTML =
        `회원 관리 <img width="50" height="50" src="https://img.icons8.com/bubbles/50/user.png" alt="user" />`;
    goManage('회원관리');
});

function goManage(type) {
    let url = '/admin';

    if(type === '신고관리'){
        url += '/manage_siren';
    }  else if(type === '파티관리'){
        url += '/manage_party';
    } else if(type === '회원관리'){
        url += '/manage_user';
    }


    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            return response.text();
        })
        .then(html => {

            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const main = document.querySelector('.detail-content_main');

            // HTML 요소를 main 요소에 삽입
            main.innerHTML = doc.documentElement.innerHTML;

            // 삽입된 HTML 내에서 스크립트 태그 실행
            const scripts = main.getElementsByTagName('script');
            for (let i = 0; i < scripts.length; i++) {
                const script = document.createElement('script');
                script.src = scripts[i].src; // 스크립트 소스 설정
                document.body.appendChild(script); // 스크립트 추가하여 실행
            }
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        });
}


