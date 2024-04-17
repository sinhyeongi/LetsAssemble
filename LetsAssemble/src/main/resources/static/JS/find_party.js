function adMyParty(event){
    const isValidClass = event.querySelector('.ad_my_party');
    if(isValidClass){
    //     내 파티 홍보하기 클래스가 있으면 파티 홍보 페이지로 이동
        console.log('홈으로 이동해야함')
        location.href = "/";
    } else{
        // 파티 인포 페이지로 이동
        const hiddenInput = event.querySelector('input[type="hidden"]');
        const party_id = hiddenInput.value;
        console.log("party_id = " + party_id);
        location.href=`/party/party_info?id=${party_id}`;
    }
}

function go_partyInfo(event){
    const hiddenInput = event.querySelector('input[type="hidden"]');
    const party_id = hiddenInput.value;
    console.log("party_id = " + party_id);
    location.href=`/party/party_info?id=${party_id}`;
}

const spans = document.querySelectorAll('.division span');
const categories = document.querySelectorAll('.category-filter');
const locations = document.querySelectorAll('.location-filter');
const parties = document.querySelectorAll('.small_content');
const big_parties = document.querySelectorAll('.big_content');

let division = '전체';

function fetchPartiesByType(type) {
    let url = '/party/getParties';

    // type에 따라 요청 URL을 변경
    if (type === '온라인') {
        url += '?type=online';
    } else if (type === '오프라인') {
        url += '?type=offline';
    } else if (type === '전체'){
        location.href = '/party/find_party';
        return;
    }

    fetch(url)
        .then(response => response.json())
        .then(partyList => {
            const big_container = document.querySelector('.big_container');

            // 기존의 big_content 요소 모두 삭제
            big_container.innerHTML = '';

            partyList.forEach(party => {
                const div = document.createElement('div');
                div.classList.add('big_content');

                // 파티 정보를 표시
                div.innerHTML = `
                    <input type="hidden" name="party_id" value="${party.id}">
                    <div class="big_content_sub">
                        <span>${party.title}</span>
                    </div>
                    <div class="big_content_category">
                        카테고리: <span class="big_cate_name">${party.interest}</span>
                    </div>
                    <div class="big_content_division">
                        모임 방식: <span class="big_division_name">${party.isOnline === 0 ? '오프라인' : '온라인'}</span>
                    </div>
                    <div class="big_content_intro">
                        파티 소개: <span>${party.intro}</span>
                    </div>
                    <div class="big_content_loc">
                        <span>
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512" style="width:20px; height:17px;">
                                <path d="M215.7 499.2C267 435 384 279.4 384 192C384 86 298 0 192 0S0 86 0 192c0 87.4 117 243 168.3 307.2c12.3 15.3 35.1 15.3 47.4 0zM192 128a64 64 0 1 1 0 128 64 64 0 1 1 0-128z" />
                            </svg>
                        </span>
                        <span class="big_loc_name">${party.area}</span>
                    </div>
                    <div class="big_content_personnel">
                        모집 인원 : (<span>${party.personnel}</span> / 5) 명
                    </div>
                `;

                // big_content 요소에 클릭 이벤트 리스너 등록
                div.addEventListener('click', () => {
                    adMyParty(div); // 클릭 시 adMyParty 함수 호출
                });

                big_container.appendChild(div);
            });
        })
        .catch(error => console.error('Error fetching parties:', error));
}


spans.forEach(function (span) {
    span.addEventListener('click', function (event) {

        // 관심사랑 지역 초기화
        locations.forEach(item => {
            item.classList.remove('selected');
        });

        categories.forEach(item => {
            item.classList.remove('selected');
        });

        // 선택 안한 상태일때는 전체 자동선택
        locations[0].classList.add('selected');
        categories[0].classList.add('selected');

        console.log(this.innerHTML.trim());
        division = this.innerHTML.trim();
        // 기존에 생성된 밑줄 요소가 있는지 확인
        const existingUnderlines = document.querySelectorAll('.underline');
        existingUnderlines.forEach(function (underline) {
            underline.parentNode.removeChild(underline); // 모든 밑줄 요소 삭제
        });
        spans.forEach(function (s) {
            s.classList.remove('clicked');
        });
        // 클릭된 span 요소에 밑줄 추가
        const underline = document.createElement('div');
        underline.classList.add('underline');
        span.appendChild(underline);
        span.classList.add('clicked');

        // 클릭한 곳 전체, 온라인, 오프라인 인지
        // let division = this.innerHTML.trim();
        console.log('여기서 divison은 ' + division);


        // 유료 파티들
        big_parties.forEach(party => {
            const div = party.querySelector('.big_division_name');
            if (div) {
                if (division.includes('전체')) {
                    party.style.display = 'block';
                } else if (division.includes(div.innerHTML)) {
                    party.style.display = 'block';
                } else {
                    party.style.display = 'none';
                }
            }
        });

        // 비동기 요청
        if(division.includes('온라인')){
            fetchPartiesByType('온라인');
        } else if(division.includes('오프라인')){
            fetchPartiesByType('오프라인');
        } else {
            fetchPartiesByType('전체');
        }

        // 일반 파티들
        parties.forEach(party => {
            const div = party.querySelector('.small_division_name');
            if (div) {
                console.log("division = " + division);
                console.log("div.inner = " + div.innerHTML);
                if (division.includes('전체')) {
                    party.style.display = 'block';
                } else if (division.includes(div.innerHTML)) {
                    party.style.display = 'block';
                } else {
                    party.style.display = 'none';
                }
            }
        })

    });
});


// 클릭시

// const spans = document.querySelectorAll('.division span');
// const categories = document.querySelectorAll('.category-filter');
// const locations = document.querySelectorAll('.location-filter');
// const parties = document.querySelectorAll('.small_content');

let category_name = '';
categories.forEach(cate => {
    cate.addEventListener('click', function () {
        locations.forEach(item => {
            item.classList.remove('selected');
        });
        // 선택 안한 상태일때는 전체 자동선택
        locations[0].classList.add('selected');

        categories.forEach(item => {
            item.classList.remove('selected');
        });
        this.classList.add('selected');
        category_name = this.innerHTML;
        // console.log("여긴 들어오나 " + category_name);
        console.log("여기서 division 은 ? " + division);

        // 지역 카테고리 상관 없이 관심사만 고를때

        parties.forEach(party => {
            console.log('여기서 category_name = ' + category_name);
            const cate = party.querySelector('.small_cate_name');
            // console.log('여기서 cate = ' + cate.innerHTML);
            const div = party.querySelector('.small_division_name'); //온,오프라인
            if (cate) {
                if(category_name === "전체" && division.includes("전체")){
                    party.style.display ='block';
                } else if (category_name === "전체" && division.includes(div.innerHTML)) {
                    party.style.display = 'block';
                } else if (category_name === cate.innerHTML && division.includes("전체")) {
                    party.style.display = 'block';
                } else if (category_name === cate.innerHTML && division.includes(div.innerHTML)) {
                    party.style.display = 'block';
                } else {
                    party.style.display = 'none';
                }
            }
        })


        // 지역 카테고리 고를때
        locations.forEach(loc => {
            loc.addEventListener('click', function () {
                locations.forEach(item => {
                    item.classList.remove('selected');
                });

                this.classList.add('selected');
                // console.log("test = " + this.innerHTML);
                const selectedCategory = this.innerHTML;
                parties.forEach(party => {
                    const locName = party.querySelector('.small_loc_name');
                    const cate = party.querySelector('.small_cate_name');
                    const div = party.querySelector('.small_division_name');
                    if (locName) {
                        console.log("여기서 카테고리는 ? " + category_name);
                        // console.log("박스 안 cate = " + cate.innerHTML);
                        const locNameText = locName.innerHTML;
                        console.log(locNameText);
                        console.log("selectedCategory = " + selectedCategory);
                        console.log("여기서 division은 ??? " + division);
                        if(category_name === '전체' && selectedCategory === '전체' && division.includes('전체')){
                            party.style.display = 'block';
                        } else if(category_name === '전체' && selectedCategory === '전체' && division.includes(div.innerHTML)){
                            party.style.display = 'block';
                        } else if(category_name === '전체' && selectedCategory === locNameText && division.includes('전체')){
                            party.style.display = 'block';
                        }  else if (category_name === cate.innerHTML && selectedCategory === '전체' && division.includes('전체')) {
                            party.style.display = 'block';
                        }
                        else if (category_name === cate.innerHTML && selectedCategory === '전체' && division.includes(div.innerHTML)) {
                            party.style.display = 'block';
                        } else if (category_name === cate.innerHTML && locNameText === selectedCategory && division.includes(div.innerHTML)) {
                            party.style.display = 'block';
                        } else if (category_name === cate.innerHTML && locNameText === selectedCategory && division.includes('전체')) {
                            party.style.display = 'block';
                        }  else {
                            party.style.display = 'none';
                        }
                    }
                });
            });
        }); // 지역 카테고리

    }); // cate 클릭
}); // categorise.foreach문




