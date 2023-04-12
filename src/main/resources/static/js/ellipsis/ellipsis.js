function ellipsisToggle(obj){
    let ellipsisWrap = obj.closest('.ellipsis-btn-wrap');
    let ellipsisContent = ellipsisWrap.getElementsByClassName('ellipsis-content')[0];
    let listCount = ellipsisContent.childElementCount;
    if(ellipsisContent.classList.contains('active')){
        ellipsisContent.style.height="0px";
        ellipsisContent.style.border="0";
    }else{
        ellipsisContent.style.height=`${50 * listCount + 10}px`;
        ellipsisContent.style.border="1px solid var(--ellipsis-color)";
    }
    ellipsisContent.classList.toggle('active');
}

//토글 버튼을 클릭해서 요소가 보여진 상태에서 다른부분을 클릭하면 요소가 사라지도록하는 이벤트
$(document).click(function(event){//문서 클릭 이벤트
    let target = $(event.target);
    // 클릭된 요소가 토글 버튼인 경우에는 요소를 숨기지 않음
    if(target.is(".ellipsis-btn") || target.parents(".ellipsis-btn").length > 0) {
        return;
    }
    if(!target.is(".ellipsis-btn")&&
        !target.is(".ellipsis-content")&&
        !target.is(".ellipsis-btn-wrap")){
        // $(".ellipsis-content").hide();
        $(".ellipsis-content").removeClass("active"); // .active 클래스 제거
        $(".ellipsis-content").css("height","0");
        $(".ellipsis-content").css("border","0");
    }
})