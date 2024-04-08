//옵션 선택 시 추가 되어야 할 곳
const option_view = $('.option_payment_aside_div');
const total_price = $('#option_payment_total_price_text');

const datepickr =$(".option_payment_flatpickr");

const flatinstance = flatpickr(datepickr,{
   dateFormat: 'Y-m-d',
   enabledTime: false,
   minDate: 'today',
   locale: 'ko',
   disable : disabledDates
});
$('.option_payment_content_title').click(function(e){
   const t = $(this).parent($('.option_payment_content'));
   const content = $(t).find($('.option_payment_content_box'));
   if(content.css("display") === 'none'){
      content.css("display","flex");
      $(t).css("max-height","300px");
      return;
   }
   content.css("display","none");
   $(t).css("max-height","100px");
});

function createoption_view(_name,view_name,_date,_price){
   const top_div = $("<div>");
   top_div.addClass("option_payment_aside_div_info");
   let span = $("<span>");
   let sub_span = $("<span>");
   //옵션 이름
   span.text(_name);
   top_div.append(span);
   // 옵션 실행 날 + 날짜
   span = $("<span>")
   sub_span.text(view_name);
   span.append(sub_span);
   sub_span = $("<span>");
   sub_span.text(_date);
   span.append(sub_span);
   top_div.append(span);
   //옵션 가격
   span = $("<span>");
   sub_span = $("<span>");
   sub_span.text("옵션가격");
   span.append(sub_span);
   sub_span = $("<span>");
   sub_span.text(_price+"원");
   sub_span.addClass("price");
   span.append(sub_span);
   top_div.append(span);
   span = $("<span>");
   const btn = $('<button>');
   btn.addClass('option_payment_option_cancel');
   btn.text("취소하기");
   span.append(btn);
   top_div.append(span);
   option_view.append(top_div);
   btn.click(function(){
      const t = top_div.find(".price");
      let price = t.text().replace("원","");
      price *= -1;
      ChangePrice(price);
      top_div.remove();
   })
   ChangePrice(_price)
}


function ChangePrice(price){
   let _total_price = parseInt(total_price.text().replace("원",""));
   _total_price += parseInt(price);
   total_price.text(_total_price+" 원");
}
$(".option_payment_add_btn").click(function(){
   const t = $(this).closest($('.option_payment_content'));
   const option_name = t.find($('.option_payment_content_title')).text();
   const date = t.find($('.option_payment_flatpickr')).val();
   const view_name = t.find($('.option_payment_view_name')).text();
   const price = t.find($('.option_price')).val();
   if(!date){
      alert(view_name+"를 선택해 주세요");
      t.find($('.option_payment_flatpickr')).focus();
      return;
   }
   createoption_view(option_name,view_name,date,price);
   disabledDates.push(t.find($('.option_payment_flatpickr')).val());
   flatinstance.set('disable',disabledDates);
   t.find($('.option_payment_flatpickr')).val("");
})
$('#option_payment_btn').hover(function(){
   if($('.option_payment_content_section').css('display') !=='flex'){
      return;
   }
   $('.option_payment_content_section').css('display','none');
   $('.option_payment_aside').stop().animate({right : "25%",width : "50%",height: "80%"},1300);
   $(this).focus();
   },
    function(){

    }
);

$('html').click(function(e){

   if($(e.target).parents('.option_payment_aside').length < 1 &&
       $('.option_payment_content_section').css("display") !== "flex"&&
       !(e.target.id === 'option_payment_btn')){
      $('.option_payment_content_section').css('display','flex');
      $('.option_payment_aside').stop().animate({width:"24%",right : "3%",height: "auto"},700);
   }
})
$('#option_payment_btn').click(function(){
   $(this).attr('disabled',true);
   requestPay()

});
function requestPay(){
   const date = Today();
   const item_count = $('.option_payment_aside_div_info').length;
   const uid = date + (item_count > 0 ? ' 외'+(item_count-1):'');
   const _name = $('.option_payment_aside_div_info:eq(0)').find('span:eq(0)').text()+(item_count > 0 ? ' 외'+(item_count-1):'');
   const price = $('#option_payment_total_price_text').text().replace("원","").replaceAll(" ","");
   const buy_name = $('input[name=username]').val();
   const buy_tel = $('input[name=tel]').val();
   const partyId = $('input[name=partyId]').val();
   const isOnline = $('input[name=online]').val();
   IMP.request_pay({
      pg : 'html5_inicis.INIpayTest',
      pay_method : 'card',
      merchant_uid : uid,
      name : _name,
      amount : parseInt(price),
      buyer_name : buy_name,
      buyer_tel : buy_tel

   },function(rsp){
      if(rsp.success){
         InsertData(uid,isOnline,partyId);
         return;
      }
      $('#option_payment_btn').removeAttr("disabled")
   });

}
function InsertData(imp_uid,isOnline,partyId){
   const email = $('input[name=email]').val();
   let array_data = [];
   $('.option_payment_aside_div_info').each(function(){
      const data = {
         partyId : partyId,
         even_day : $(this).find('span:eq(1) span:eq(1)').text(),
         price : $(this).find('.price').text().replace('원',''),
         name : $(this).find('span:eq(0)').text(),
         email :email,
         imp_uid : imp_uid,
         isOnline : isOnline
      }
      array_data.push(data);
   })
   $.ajax({
      type:"post",
      url : "/pay/add",
      contentType : "application/json; charset=utf-8",
      data : JSON.stringify(array_data),
      async:false,
      success: function(data,status,xhr){
         if(status === 'success'){
            alert('이벤트 신청이 완료 되었습니다.')
            location.href="/";
         }
      },
      error : function(err){
         alert('에러 발생 : '+err);
      }
   })
}
function Today(){
   const today = new Date();
   return today.getFullYear()+'-'+("0"+(today.getMonth()+1)).slice(-2)+ '-'+("0"+today.getDate()).slice(-2) + " "+today.getHours()+':'+('0'+today.getMinutes()).slice(-2)+":"+('0'+today.getSeconds()).slice(-2);
}
createoption_view("파티글 상단에 고정하기","선택날짜",'2024-04-04',1000);
createoption_view("파티글 상단에 고정하기","선택날짜",'2024-04-04',1000);
createoption_view("파티글 상단에 고정하기","선택날짜",'2024-04-04',1000);
