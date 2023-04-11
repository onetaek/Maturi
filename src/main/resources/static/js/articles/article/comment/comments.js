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

//textarea에 click 될 때 취소/댓글 버튼 출력
function showCommentBtn(obj){
    console.log("obj",obj);
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

//댓글 버튼 눌렀을 때 댓글 등록
function commentBtn(obj){
    let commentFormRightWrap = $(obj).closest('.comment-form-right-wrap');
    let textarea = commentFormRightWrap.find('.textarea-wrap textarea');
    console.log("textarea val",textarea.val());
    if(textarea.val().trim()!==""){
        //댓글 등록 ajax요청
        fetch(`/api/articles/${articleId}/comments`,{
            method: "POST",
            headers:{
                "Content-Type":"application/json",
            },
            body:JSON.stringify({
                ref:null,
                refStep:null,
                content:textarea.val(),
                refMemberId:null,
                refMemberNickName:null
            })
        }).then((response)=>{
            if(response.ok){
                alert("댓글 등록 성공!");
            }
        })

    }
}

//---------대댓글(ref: 2)------------
//답글 버튼을 누르면 대댓글을 입력할 수 있는 form이 출력
function replyRef2FormShow(obj){
    console.log("답글 클릭!");
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
function commentRef2Btn(){

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

//답글 버튼을 누를때 ajax요청을할 함수
function commentRef3Btn(){

}

