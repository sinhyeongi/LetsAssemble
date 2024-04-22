/* 질문 클릭 시*/
const qnaItems = document.querySelectorAll('.boarder-title');

qnaItems.forEach((qnaItem)=>{
    qnaItem.addEventListener('click',(event)=>{
        const target = event.target.parentElement.parentElement;
        const content = target.querySelector('.boarder-content');
        if(content.style.display === 'none'){
            content.style.display = 'block';
        }else{
            console.log(content.style.display);
            content.style.display = 'none';
        }
    });
})

function loadList(keyword) {

}