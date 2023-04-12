//-----------댓글, 대댓글 목록을 불러오는 작업(READ)------------
function getComments(){
    fetch(`/api/articles/${articleId}/comments`)
        .then((response)=>response.json())
        .then((data)=>{
            console.log(data);
            const commentsReplysContainer = document.querySelector('#comments-replys-container');
            let ref = 0;
            data.forEach((comments,i)=>{
                let commentTemple;
                let replyTemple;
                console.log("------------------------");
                console.log(i,"번째 comments",comments);

                let firstCommentId;

                comments.forEach((comment,index)=>{
                    console.log(index,"번째 comment또는 reply",comment);

                    if(index === 0){
                        firstCommentId = comment.id;
                        let commentHtml = `
                    <!--댓글 하나당 대댓글 여러개 출력-->
                    <div class="comment-replys-wrap comment${comment.id}">
                        <!--댓글 내용을 출력-->
                        <div class="comment-wrap">
                            <div class="comment-left-wrap" onclick="location.href='/members/${comment.memberId}'">`
                        if(comment.profileImg == null || comment.profileImg === ""){
                            commentHtml += `<img src="/img/profileImg/default_profile.png" alt="프로필 이미지">`
                        }else if(article.profileImg.includes("http")){
                            commentHtml += `<img src="${comment.profileImg}" alt="프로필 이미지">`
                        }else{
                            commentHtml += `<img src="/test/file/${comment.profileImg}" alt="프로필 이미지">`
                        }
                        commentHtml+=`</div>
                            <div class="comment-right-wrap">
                                <div class="user-info">
                                <span>
                                    <span class="nick-name">${comment.nickName}</span>
                                    <span class="name">(${comment.name})</span>
                                </span>
                                    <span class="created-date">${comment.duration}</span>`
                        if(comment.modified === true){
                            commentHtml += `<span class="is-modified">(수정됨)</span>`
                        }else{
                            commentHtml += `<span class="is-modified"></span>`
                        }
                        commentHtml+=`</div>      
                                <div class="textarea-wrap readonly" data-commentid="${comment.id}">
                                    <textarea
                                    placeholder="내용을 입력해 주십시오." 
                                    oninput="commentUpdateKeyUp(this)" 
                                    readonly>${comment.content}</textarea>
                                    <span class="textarea-under-line"></span>
                                </div>
                                <div class="update-btn-wrap">
                                    <button onclick="updateFormHidden(this)" class="cancel-btn">취소</button>
                                    <button onclick="updateReply(this)" class="update-btn">수정</button>
                                </div>
                                <div class="like-reply-wrap">
                                    <span class="likeWrap">`
                            if(comment.liked){
                                commentHtml += `<span onclick="likeOrUnLike(this,${comment.id})" class="likeBtn isLikedArticle">좋아요</span>`;
                            }else{
                                commentHtml += `<span onclick="likeOrUnLike(this,${comment.id})" class="likeBtn">좋아요</span>`
                            }
                            commentHtml+=`<span class="likeNum">${comment.likeCount}</span>
                                    </span>
                                    <span class="reply-btn-wrap">
                                        <button onclick="replyRef2FormShow(this)" class="reply-btn">답글</button>
                                    </span>
                                </div>
                            </div>
                            <div class="ellipsis-btn-wrap">
                                <span class="ellipsis-btn" onclick="ellipsisToggle(this)">
                                    <ion-icon name="ellipsis-vertical"></ion-icon>
                                </span>
                                <ul class="ellipsis-content">`
                        if(comment.memberId === memberId){//댓글의 memberId와 세션의 memberId가 같을경우
                            commentHtml += `<li>
                                            <div>
                                                <ion-icon name="git-compare-outline"></ion-icon>
                                                <a onclick="updateFormShow(this)">댓글 수정</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div>
                                                <ion-icon name="trash-outline"></ion-icon>
                                                <a href="#"
                                                   onclick="deleteComment(${comment.id},${comment.ref})">댓글
                                                    삭제</a>
                                            </div>
                                        </li>`
                        }else{//댓글의 memberId와 세션의 membeRId가 다를 경우
                            commentHtml += `
                                        <!--신고하기-->
                                        <li>
                                            <div th:articleId="${article.id}"
                                                 th:onclick="reposrtComment(${comment.id})">
                                                <ion-icon name="warning-outline"></ion-icon>
                                                <a onclick="reportComment(${comment.id})">신고하기</a>
                                            </div>
                                        </li>`
                        }
                        commentHtml +=`</ul>      
                            </div>
                        </div>
                        <!--새로운 댓글을 작성할 form역할을 하는 곳-->
                        <div class="comment-form-container ref-2">
                            <div class="comment-form-left-wrap" onclick="location.href='/members/${memberId}'">`
                        if(member.profileImg == null || member.profileImg === ""){
                            commentHtml += `<img src="/img/profileImg/default_profile.png" alt="프로필 이미지">`
                        }else if(member.profileImg.includes("http")){
                            commentHtml += `<img src="${member.profileImg}" alt="프로필 이미지">`
                        }else{
                            commentHtml += `<img src="/test/file/${member.profileImg}" alt="프로필 이미지">`
                        }
            commentHtml += `</div>
                            <div class="comment-form-right-wrap">
                                <div class="textarea-wrap">
                                <textarea
                                        oninput="commentKeyUp(this)"
                                        placeholder="댓글추가..."></textarea>
                                    <span class="textarea-under-line"></span>
                                </div>
                                <div class="comment-btn-wrap">
                                    <button onclick="cancelRef2ReplyBtn(this)" class="cancel-btn">취소</button>
                                    <button onclick="commentCreate(this,${comment.ref},${comment.refStep})" class="comment-btn">답글</button>
                                </div>
                            </div>
                        </div>`
                        if(comments.length > 1){//대댓글이 있다는 의미이다.
                            commentHtml +=
                                `<div class="replys-container">
                            <!--대댓글을 show/hidden toggle버튼-->
                            <div onclick="replyListToggle(this)" class="reply-toggle-btn">
                                <ion-icon name="caret-down-outline"></ion-icon>
                                <span>답글${comments.length-1}개</span>
                            </div>
                            <!--대댓글을 출력할 공간-->
                            <ul class="reply-list">
                                
                            </ul>
                        </div>`
                        }
                        `</div>`;
                        commentTemple = myCreateElement(commentHtml);
                        if(i===0){
                            commentsReplysContainer.innerHTML = commentHtml;
                        }else{
                            commentsReplysContainer.appendChild(commentTemple);
                        }

                    }else{//comment의 인덱스가 0 보다 클경우(대댓글일 경우 => refStep이 2이상일 경우)
                        console.log("comment의 인덱스가 0보다 큼 -> reply입니다.")

                        let firstCommentWrap = document.querySelector(`.comment-replys-wrap.comment${firstCommentId}`);
                        let replyList = firstCommentWrap.querySelector('.reply-list');
                        console.log("firstCommentWrap",firstCommentWrap);
                        console.log("replyList",replyList);

                        let replyHtml = `<li class="reply-item">
                                    <div class="comment-wrap">
                                        <div class="comment-left-wrap" onclick="location.href='/members/${comment.memberId}'">`
                        if(comment.profileImg == null || comment.profileImg === ""){
                            replyHtml += `<img src="/img/profileImg/default_profile.png" alt="프로필 이미지">`
                        }else if(comment.profileImg.includes("http")){
                            replyHtml += `<img src="${comment.profileImg}" alt="프로필 이미지">`
                        }else{
                            replyHtml += `<img src="/test/file/${comment.profileImg}" alt="프로필 이미지">`
                        }
                            replyHtml+=`</div>
                                        <div class="comment-right-wrap">
                                            <div class="user-info">
                                                <span>
                                                    <span class="nick-name">${comment.nickName}</span>
                                                    <span class="name">(${comment.name})</span>
                                                </span>
                                                <span class="created-date">${comment.duration}</span>`
                                if(comment.modified === true){
                                    replyHtml+=`<span class="is-modified">(수정됨)</span>`
                                }else{
                                    replyHtml+=`<span class="is-modified"></span>`
                                }
                                replyHtml+=`</div>
                                            <div class="textarea-wrap readonly" data-commentid="${comment.id}">`
                                    if(comment.refMemberNickName !== null){
                                        replyHtml+=`<span class="ref-member-nick-name" 
                                                        onclick="location.href='/members/${comment.refMemberId}'">${comment.refMemberNickName}</span>`
                                    }
                                        replyHtml+=`<textarea
                                                    placeholder="내용을 입력해 주십시오." 
                                                    oninput="commentUpdateKeyUp(this)" 
                                                    onfocus="textareaFocus(this)"
                                                    onfocusout="textareaFocusOut(this)"
                                                    readonly>${comment.content}</textarea>
                                                    <span class="textarea-under-line"></span>
                                            </div>
                                            <div class="update-btn-wrap">
                                                <button onclick="updateFormHidden(this)" class="cancel-btn">취소</button>
                                                <button onclick="updateReply(this)" class="update-btn">수정</button>
                                            </div>
                                            
                                            <div class="like-reply-wrap">
                                                <span class="likeWrap">`
                                    if(comment.liked){
                                        replyHtml += `<span onclick="likeOrUnLike(this,${comment.id})" class="likeBtn isLikedArticle">좋아요</span>`;
                                    }else{
                                        replyHtml += `<span onclick="likeOrUnLike(this,${comment.id})" class="likeBtn">좋아요</span>`
                                    }
                                         replyHtml+=`<span class="likeNum">${comment.likeCount}</span>
                                                </span>
                                                <span class="reply-btn-wrap">
                                                    <button onclick="replyRef3FormShow(this)" class="reply-btn">답글</button>
                                                </span>
                                            </div>
                                        </div>
                                        <div class="ellipsis-btn-wrap">
                                            <span class="ellipsis-btn" onclick="ellipsisToggle(this)">
                                                <ion-icon name="ellipsis-vertical"></ion-icon>
                                            </span>
                                            <ul class="ellipsis-content">`
                        if(comment.memberId === memberId){//댓글의 memberId와 세션의 memberId가 같을경우
                            replyHtml+=`<li>
                                            <div>
                                                <ion-icon name="git-compare-outline"></ion-icon>
                                                <a onclick="updateFormShow(this)">댓글 수정</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div>
                                                <ion-icon name="trash-outline"></ion-icon>
                                                <a href="#"
                                                   onclick="deleteComment(${comment.id},${comment.ref})">댓글
                                                    삭제</a>
                                            </div>
                                        </li>`
                        }else{//댓글의 memberId와 세션의 membeRId가 다를 경우
                            replyHtml+=`
                                                    <!--신고하기-->
                                        <li>
                                            <div th:articleId="${article.id}"
                                                 th:onclick="reposrtComment(${comment.id})">
                                                <ion-icon name="warning-outline"></ion-icon>
                                                <a onclick="reportComment(${comment.id})">신고하기</a>
                                            </div>
                                        </li>`
                        }
                                replyHtml +=`</ul>    
                                        </div><!--.ellipsis-btn-wrap끝-->
                                    </div>
                                    <!--새로운 댓글을 작성할 form역할을 하는 곳-->
                                    <div class="comment-form-container ref-3">
                                        <div class="comment-left-wrap" onclick="location.href='/members/${memberId}'">`
                        if(member.profileImg == null || member.profileImg === ""){
                            replyHtml += `<img src="/img/profileImg/default_profile.png" alt="프로필 이미지">`
                        }else if(member.profileImg.includes("http")){
                            replyHtml += `<img src="${member.profileImg}" alt="프로필 이미지">`
                        }else{
                            replyHtml += `<img src="/test/file/${member.profileImg}" alt="프로필 이미지">`
                        }
                        replyHtml+=`</div>
                                        <div class="comment-form-right-wrap">
                                            <div class="textarea-wrap readonly">
                                                <div class="textarea-ref3">
                                                    <span onclick="location.href='/members/${memberId}'">${comment.nickName}</span>
                                                    <textarea
                                                            onfocus="textareaFocus(this)"
                                                            onfocusout="textareaFocusOut(this)"
                                                            oninput="commentKeyUp(this)"
                                                            onclick="showCommentBtn(this)"
                                                            placeholder="내용을 입력해 주십시오"></textarea>
                                                </div>
                                                <span class="textarea-under-line"></span>
                                            </div>
                                            <div class="comment-btn-wrap">
                                                <button onclick="cancelRef3ReplyBtn(this)" class="cancel-btn">취소
                                                </button>
                                                <button onclick="commentCreate(
                                                this,
                                                ${comment.ref},
                                                ${comment.refStep},
                                                ${comment.memberId},
                                                '${comment.nickName}')" class="comment-btn">답글
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </li>`
                        replyList.appendChild(myCreateElement(replyHtml));
                    }
                })//comments forEach문 끝
            })//data forEach문 끝
        })
}

//댓글 버튼 눌렀을 때 댓글 등록(Create)
function commentCreate(obj, ref, refStep,refMemberId,refMemberNickName){
    if(ref === null || ref === undefined) ref = null;
    if(refStep === null || refStep === undefined) refStep = null;
    if(refMemberId === null || refMemberId === undefined) refMemberId = null;
    if(refMemberNickName === null || refMemberNickName === undefined) refMemberNickName = null;

    console.log(ref,refStep,refMemberId,refMemberNickName);

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
                ref:ref,
                refStep:refStep,
                content:textarea.val(),
                refMemberId:refMemberId,
                refMemberNickName:refMemberNickName
            })
        }).then((response)=>{
            if(response.ok){

                Swal.fire({
                    title: "댓글을 등록하는데 성공하셨습니다!",
                    icon: 'success',
                    confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                }).then(function () {
                    getComments();//댓글들을 다시 가져옴
                });
            }
        })

    }
}

//댓글 삭제 버튼 클릭시 이벤트 처리(Delete)
function deleteComment(commentId,ref){
    Swal.fire({
        title: `댓글 삭제`,
        text: '댓글을 완전히 삭제할까요?',
        icon: "question",
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        cancelButtonColor: '#6e7881', // cancel 버튼 색깔 지정
        confirmButtonText: '댓글삭제', // confirm 버튼 텍스트 지정
        cancelButtonText: '취소', // cancel 버튼 텍스트 지정
        reverseButtons: false, // 버튼 순서 거꾸로
    }).then((result)=> {
        if (result.isConfirmed) {
            const url = `/api/comments/${commentId}`;
            fetch(url, {
                method: "delete",
                body:JSON.stringify({
                    articleId:articleId,
                    ref:ref
                }),
                headers: {
                    "Content-type": "application/json"
                }
            }).then(response => {
                if(response.ok){
                    Swal.fire({
                        title: "댓글 삭제 성공",
                        text:"댓글 삭제하는데 성공했습니다!",
                        icon: 'success',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                }else if(response.status===404) {
                    Swal.fire({
                        title: "댓글 삭제 실패",
                        text:"해당 댓글을 찾을 수 없습니다.",
                        icon: 'error',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                }else if(response.status===403) {
                    Swal.fire({
                        title: "댓글 삭제 실패",
                        text:"댓글을 삭제하려는 유저와 댓글을 생성한 유저가 일치하지 않습니다.",
                        icon: 'error',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                }else if(response.status===409) {
                    Swal.fire({
                        title: "댓글 삭제 실패",
                        text:"알 수 없는 이유로 인해 삭제된 댓글이 없습니다.",
                        icon: 'error',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                }else{
                    Swal.fire({
                        title: "댓글 삭제 실패",
                        text:"알 수 없는 이유로 인해 댓글 삭제에 실패했습니다.",
                        icon: 'error',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                }
                getComments();//댓글들을 다시 가져옴
            })
        }
    })
}

//댓글 대댓글 수정 (Update)
function updateReply(obj){
    let commentWrap = $(obj).closest('.comment-wrap');
    let textareaWrap = commentWrap.find('.textarea-wrap');
    let textarea = textareaWrap.find('textarea');
    let commentId = textareaWrap.data('commentid');
    let content = textarea.val()

    fetch(`/api/comments/${commentId}`,{
        method:"PATCH",
        headers:{
            "Content-Type":"application/json",
        },
        body:JSON.stringify({
            content : content
        })
    }).then((response)=>{
        if(response.ok){
            Swal.fire({
                title: "댓글 수정 완료",
                text:"댓글을 성공적으로 수정하였습니다",
                icon: 'success',
                confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
            }).then(function(){
                //textarea css 적용
                textareaWrap.addClass('readonly');
                textarea.prop("readonly",true);
                textareaWrap.find(".textarea-under-line").css("width","0");

                //버튼을 사라지도록 변경
                commentWrap.find(".update-btn-wrap").css('display','none');
            })
        }else if(response.status === 404){
            Swal.fire({
                title: "댓글 수정 실패",
                text:"해당 댓글을 찾을 수 없습니다",
                icon: 'error',
                confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
            }).then(function(){
                getComments();
            })
        }else if(response.status === 403){
            Swal.fire({
                title: "댓글 수정 실패",
                text:"댓글을 수정하려는 유저와 댓글을 생성한 유저가 일치하지 않습니다",
                icon: 'error',
                confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
            }).then(function(){
                getComments();
            })
        }else if(response.status === 400){
            Swal.fire({
                title: "댓글 수정 실패",
                text:"예상치 못한 결과로 인해 실패했습니다.",
                icon: 'error',
                confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
            }).then(function(){
                getComments();
            })
        }else{
            Swal.fire({
                title: "댓글 수정 실패",
                text:"알 수 없는 이유로 댓글 수정에 실패했습니다.",
                icon: 'error',
                confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
            }).then(function(){
                getComments();
            })
        }
    })
}

//좋아요 클릭시 이벤트 처리(Create, Delete)
function likeOrUnLike(obj,commentId){
    const url = `/api/comments/${commentId}/like`;
    fetch(url, {
        method: "post",
        body: JSON.stringify({
            commentId: commentId
        }),
        headers: {
            "Content-type": "application/json"
        }
    }).then(response => response.json())
        .then(data =>{
            console.log(data);

            // this 요소의 형제 요소 중 class명이 'likeNum'인 요소를 찾습니다.
            const likeNumElement = obj.nextElementSibling;

            if (data.isLiked == 1) { // 좋아요 !!
                obj.classList.add("isLikedArticle");
            } else { // 좋아요 취소!
                obj.classList.remove("isLikedArticle");
            }
            likeNumElement.innerText = data.likeNum;
        })
}

//댓글 신고 기능
function reportComment(commentId){
    Swal.fire({
        icon: 'warning',
        title: '신고사유를 작성해주세요',
        input: 'text',
        text:'한번 신고를 하고나서 취소할 수 없습니다.',
        inputAttributes: {
            autocapitalize: 'off'
        },
        showCancelButton: true,
        confirmButtonText: '신고하기',
        cancelButtonText:'취소',
        confirmButtonColor: '#d33',
        showLoaderOnConfirm: true, // 데이터 결과를 받을때까지, 버튼에다가 로딩바를 표현
        preConfirm: (text) => { // 확인 버튼 누르면 실행되는 콜백함수
            const url = "/api/comments/"+ commentId +"/report";

            return fetch(url, {
                method: "POST",
                headers:{
                    "Content-Type":"application/json",
                },
                body:JSON.stringify({
                    "reportReason":text
                })
            }).then((response)=>{
                if(response.status == 200){ // 신고 성공
                    Swal.fire({
                        title: "해당 댓글을 신고했습니다.",
                        icon: 'success',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                } else if(response.status == 226) {
                    Swal.fire({
                        title: "이미 신고한 글입니다.",
                        icon: 'error',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                } else if(response.status == 404){
                    Swal.fire({
                        title: "댓글을 찾을 수 없습니다.",
                        icon: 'error',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    }).then(function () {
                        // getComments();
                    })
                }
            })
        },
        //알림창 외부를 눌러도 닫히지 않도록하는 작업
        allowOutsideClick: () => !Swal.isLoading()
    })
}


//페이지가 로드될 때 댓글, 대댓글을 가져옴
getComments();