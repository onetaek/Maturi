//댓글을 입력하는 textarea의 값이 변경될때 실행
function commentKeyUp(obj){
    //textarea 동적 크기조절
    $(obj).height("22px");
    let scHeight = $(obj).prop('scrollHeight')
    $(obj).height(`${scHeight}px`);

    //textarea에 글을 작성하면 댓글 버튼 활성화
    let commentFormRightWrap = $(obj).closest('.comment-form-right-wrap');
    let commentBtn = commentFormRightWrap.find('.comment-btn')[0];
    if($(obj).val().trim()!==""){
        commentBtn.style.background = "var(--hot-pink-color)";
        commentBtn.style.color = "var(--comment-btn-active-color)";
        commentBtn.style.cursor = "pointer";
    }else{
        commentBtn.style.background = "var(--comment-btn-background)";
        commentBtn.style.color = "var(--comment-btn-color)";
        commentBtn.style.cursor = "none";
    }
}

//댓글 수정하는 폼의 textarea의 값이 변경될 때 실행
function commentUpdateKeyUp(obj){
    //textarea 동적 크기조절
    $(obj).height("22px");
    let scHeight = $(obj).prop('scrollHeight')
    $(obj).height(`${scHeight}px`);

    //textarea에 글을 작성하면 댓글 버튼 활성화
    let commentRightWrap = $(obj).closest('.comment-right-wrap');
    let updateBtn = commentRightWrap.find('.update-btn')[0];
    if($(obj).val().trim()!==""){
        updateBtn.style.background = "var(--hot-pink-color)";
        updateBtn.style.color = "var(--comment-btn-active-color)";
        updateBtn.style.cursor = "pointer";
    }else{
        updateBtn.style.background = "var(--comment-btn-background)";
        updateBtn.style.color = "var(--comment-btn-color)";
        updateBtn.style.cursor = "inherit";
    }
}

//textarea에 click 될 때 취소/댓글 버튼 출력
function showCommentBtn(obj){
    let commentFormRightWrap = $(obj).closest('.comment-form-right-wrap');
    let commentBtnWrap = commentFormRightWrap.find('.comment-btn-wrap');
    commentBtnWrap.css('display','flex');
}

//취소 버튼 눌렀을 때 취소/댓글 버튼 사라짐
function cancelCommentBtn(obj){
    let commentFormRightWrap = $(obj).closest('.comment-form-right-wrap');
    let commentBtnWrap = commentFormRightWrap.find('.comment-btn-wrap');
    let textarea = commentFormRightWrap.find('.textarea-wrap textarea');
    commentBtnWrap.css('display','none');
    textarea.val('');
}

//---------대댓글(ref: 2)------------
//답글 버튼을 누르면 대댓글을 입력할 수 있는 form이 출력
function replyRef2FormShow(obj){
    let commentReplysWrap = $(obj).closest('.comment-replys-wrap');
    let replyFormRef2 = commentReplysWrap.find('.comment-form-container.ref-2');
    let replyFormRef2Textarea = commentReplysWrap.find('.comment-form-container.ref-2 textarea');
    replyFormRef2.css('display','flex')
    console.log("textarea",replyFormRef2Textarea);
    replyFormRef2Textarea.focus();
}
//취소버튼을 누르면 대댓글을 입력할 수 있는 form을 사라지도록
function cancelRef2ReplyBtn(obj){
    let commentReplysWrap = $(obj).closest('.comment-form-container.ref-2');
    commentReplysWrap.css('display','none');
}
//답글 더보기 버튼을 누르면 해당 댓글의 답글들을 볼 수 있는 버튼
function replyListToggle(obj){
    let replysContainer = $(obj).closest('.replys-container');
    let replyList = replysContainer.find('.reply-list');
    let ionIcon = replysContainer.find('.reply-toggle-btn ion-icon');
    if(replyList.hasClass('show')){//show클래스를 가지고있을 때 -> 삭제
        replyList.removeClass('show');
        replyList.css('display','none');
        ionIcon.css('transform','rotate(0)');
    }else {//show클래스가 없을 때 -> 추가
        replyList.addClass('show');
        replyList.css('display', 'flex');
        ionIcon.css('transform','rotate(180deg)');
    }
}

//---------대대댓글(ref: 3)------------
//답글 버튼을 누르면 대댓글을 입력할 수 있는 form이 출력
function replyRef3FormShow(obj){
    console.log("답글 클릭!");
    let replyItem = $(obj).closest('.reply-item');
    let replyFormRef3 = replyItem.find('.comment-form-container.ref-3');
    let replyFormRef3Textarea = replyItem.find('.textarea-wrap textarea');
    replyFormRef3.css('display','flex')
    replyFormRef3Textarea.focus();
}

//취소버튼을 누르면 대댓글을 입력할 수 있는 form을 사라지도록
function cancelRef3ReplyBtn(obj){
    let commentReplysWrap = $(obj).closest('.comment-form-container.ref-3');
    commentReplysWrap.css('display','none');

}

//refStep3단계의 textarea의 focus under-line 이벤트 처리하는 함수
function textareaFocus(obj){
    let textareaWrap = $(obj).closest('.textarea-wrap');
    if(!textareaWrap.hasClass('readonly')){
        let underLine = textareaWrap.find('.textarea-under-line');
        underLine.css('width','100%');
    }
}
//refStep3단계의 textarea의 focusout under-line 이벤트 처리하는 함수
function textareaFocusOut(obj){
    let textareaWrap = $(obj).closest('.textarea-wrap');
    let underLine = textareaWrap.find('.textarea-under-line');
    underLine.css('width','0%');
}

//---------refStep2,3 공통------------
//댓글 수정을 누르면 수정 폼으로 변경되도록
function updateFormShow(obj){
    let commentReplysWrap = $(obj).closest('.comment-wrap');
    let textareaWrap = commentReplysWrap.find('.comment-right-wrap .textarea-wrap');
    let textarea = textareaWrap.find('textarea');
    let updateBtnWrap = commentReplysWrap.find('.update-btn-wrap');

    //더보기 버튼을 닫아준다.
    $(".ellipsis-content").removeClass("active"); // .active 클래스 제거
    $(".ellipsis-content").css("height","0");
    $(".ellipsis-content").css("border","0");

    //textarea css적용
    textareaWrap.removeClass("readonly");
    textarea.prop("readonly",false);
    textareaWrap.find(".textarea-under-line").css("width","100%");

    //버튼 보여지도록 변경
    updateBtnWrap.css('display','flex');
}
function updateFormHidden(obj){
    let commentReplysWrap = $(obj).closest('.comment-wrap');
    let textareaWrap = commentReplysWrap.find('.comment-right-wrap .textarea-wrap');
    let textarea = textareaWrap.find('textarea');
    let updateBtnWrap = $(obj).closest('.update-btn-wrap');

    //textarea css적용
    textareaWrap.addClass("readonly");
    textarea.prop("readonly",true);
    textareaWrap.find(".textarea-under-line").css("width","0");

    //버튼을 사라지도록 변경
    updateBtnWrap.css('display','none');
}



