const today = Today();
let check = false;
$('.option_view_cancel').click(function(e){
    $(this).attr('disabled',true);
    const view = $(this).closest(".view_card");
    const no = view.find('input[name=id]').val();
    const uid = view.find('.option_uid').attr('name');
    const uidlist = document.querySelectorAll('.view_card input[name="'+uid+'"]');
    let dateString = '';
    let viewList = [];
    uidlist.forEach((n,idx)=>{
        viewList.push($(n).closest('.view_card'));
        viewList[idx].find('.option_view_cancel').attr('disabled',true);
        if($(n).closest('.view_card').find('.option_even_day').text() <= today){
            b = true;
        }
        dateString += $(n).closest('.view_card').find('.option_even_day').text()+'\n';
    })
    if(b){
        alert('사용한 옵션이 있어 환불이 불가능합니다.');
        b = false;
        $(this).removeAttr('disabled');
        uidlist.forEach((n,idx)=>{
            viewList[idx].find('.option_view_cancel').removeAttr('disabled');
        })
        return;
    }
    if(!confirm(dateString+'에 대한 이벤트 요청이 취소 됩니다.\n결제를 취소하시겠습니까?')){
        uidlist.forEach((n,idx)=>{
            viewList[idx].find('.option_view_cancel').removeAttr('disabled');
        });
        return;
    }
    viewList.forEach(n=>{
        n.remove();
    })
    $.ajax({
        url : "/pay/cancel/"+no,
        type : "DELETE",
        async : false,
        success : function(data){
            if(data&&data.includes('개의 결제가 취소되었습니다.')){
                alert(data);
                viewList.forEach(n=>{
                    n.remove();
                })
            }else if(data === 'NFU'){
                alert('유저 정보를 찾지 못해 취소에 실패하였습니다.\n페이지를 새로 고칩니다.');

            }else if(data === 'Payment already canceled'){
                alert('이미 취소된 예약 입니다.');

            }else if(data === 'Time Over'){
                alert('잠시 후 다시 시도해주세요');
                $(this).removeAttr('disabled');
            }
            else{
                alert('오류로 인해 취소에 실패하였습니다.');

            }

        },
        error : function(error){
            alert('err = '+error.toString());
        }
    })

});
function Today(){
    const today = new Date();
    return today.getFullYear()+'-'+("0"+(today.getMonth()+1)).slice(-2)+ '-'+("0"+today.getDate()).slice(-2) + " "+today.getHours()+':'+('0'+today.getMinutes()).slice(-2)+":"+('0'+today.getSeconds()).slice(-2);
}