//-----------댓글, 대댓글 목록을 불러오는 작업------------
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
                            <div class="comment-left-wrap">`
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
                                <div class="textarea-wrap readonly">
                                    <textarea readonly>${comment.content}</textarea>
                                    <span class="textarea-under-line"></span>
                                </div>
                                <div class="like-reply-wrap">
                                    <span class="likeWrap">`
                            if(comment.liked){
                                commentHtml += `<span class="likeBtn isLikedArticle">좋아요</span>`;
                            }else{
                                commentHtml += `<span class="likeBtn">좋아요</span>`
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
                                                <a href="/articles/${article.id}/edit">댓글 수정</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div>
                                                <ion-icon name="trash-outline"></ion-icon>
                                                <a href="#"
                                                   th:onclick="deleteComment(${comment.id})">댓글
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
                                                <a>신고하기</a>
                                            </div>
                                        </li>`
                        }
                        commentHtml +=`</ul>      
                            </div>
                        </div>
                        <!--새로운 댓글을 작성할 form역할을 하는 곳-->
                        <div class="comment-form-container ref-2">
                            <div class="comment-form-left-wrap">`
                        if(comment.profileImg == null || comment.profileImg === ""){
                            commentHtml += `<img src="/img/profileImg/default_profile.png" alt="프로필 이미지">`
                        }else if(comment.profileImg.includes("http")){
                            commentHtml += `<img src="${comment.profileImg}" alt="프로필 이미지">`
                        }else{
                            commentHtml += `<img src="/test/file/${comment.profileImg}" alt="프로필 이미지">`
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
                                    <button onclick="commentRef2Btn(comment.ref,comment.refStep)" class="comment-btn">답글</button>
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
                        commentsReplysContainer.appendChild(commentTemple);
                    }else{//comment의 인덱스가 0 보다 클경우(대댓글일 경우 => refStep이 2이상일 경우)
                        console.log("comment의 인덱스가 0보다 큼 -> reply입니다.")

                        let firstCommentWrap = document.querySelector(`.comment-replys-wrap.comment${firstCommentId}`);
                        let replyList = firstCommentWrap.querySelector('.reply-list');
                        console.log("firstCommentWrap",firstCommentWrap);
                        console.log("replyList",replyList);

                        let replyHtml = `<li class="reply-item">
                                    <div class="comment-wrap">
                                        <div class="comment-left-wrap">`
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
                                            <div class="textarea-wrap readonly">`
                                    if(comment.refMemberNickName !== null){
                                        replyHtml+=`<span class="ref-member-nick-name">${comment.refMemberNickName}</span>`
                                    }
                                        replyHtml+=`<textarea readonly>${comment.content}</textarea>
                                                <span class="textarea-under-line"></span>
                                            </div>
                                            <div class="like-reply-wrap">
                                                <span class="likeWrap">`
                                    if(comment.liked){
                                        replyHtml += `<span class="likeBtn isLikedArticle">좋아요</span>`;
                                    }else{
                                        replyHtml += `<span class="likeBtn">좋아요</span>`
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
                                                <a href="/articles/${article.id}/edit">댓글 수정</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div>
                                                <ion-icon name="trash-outline"></ion-icon>
                                                <a href="#"
                                                   th:onclick="deleteComment(${comment.id})">댓글
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
                                                <a>신고하기</a>
                                            </div>
                                        </li>`
                        }
                                replyHtml +=`</ul>    
                                        </div><!--.ellipsis-btn-wrap끝-->
                                    </div>
                                    <!--새로운 댓글을 작성할 form역할을 하는 곳-->
                                    <div class="comment-form-container ref-3">
                                        <div class="comment-left-wrap">`
                        if(comment.profileImg == null || comment.profileImg === ""){
                            replyHtml += `<img src="/img/profileImg/default_profile.png" alt="프로필 이미지">`
                        }else if(comment.profileImg.includes("http")){
                            replyHtml += `<img src="${comment.profileImg}" alt="프로필 이미지">`
                        }else{
                            replyHtml += `<img src="/test/file/${comment.profileImg}" alt="프로필 이미지">`
                        }
                        replyHtml+=`</div>
                                        <div class="comment-form-right-wrap">
                                            <div class="textarea-wrap">
                                                <div class="textarea-ref3">
                                                    <span>${comment.nickName}</span>
                                                    <textarea
                                                            oninput="commentKeyUp(this)"
                                                            onclick="showCommentBtn(this)"
                                                            placeholder="댓글추가..."></textarea>
                                                </div>
                                                <span class="textarea-under-line"></span>
                                            </div>
                                            <div class="comment-btn-wrap">
                                                <button onclick="cancelRef3ReplyBtn(this)" class="cancel-btn">취소
                                                </button>
                                                <button onclick="commentRef3Btn(${comment.ref},${comment.refStep},${comment.memberId},${comment.memberNickName})" class="comment-btn">답글
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

//페이지가 로드될 때 댓글, 대댓글을 가져옴
getComments();