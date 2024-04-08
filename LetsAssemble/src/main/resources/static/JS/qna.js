/* 카테고리 클릭 시*/
const buttonItems = document.querySelector('.button-items');
const $qna_wrap = document.querySelector(".qna-wrap");

buttonItems.addEventListener('click',(event)=>{
    if(event.target.className !== 'item'){
        return;
    }
    const keyword = event.target.dataset.id;
    loadList(keyword)
});

function loadList(keyword) {
    fetch(`qna?keyword=${keyword}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(data => makeView(data, memID))
        .catch(error => {
            console.error("Error occurred while processing the request:", error);
        });
}