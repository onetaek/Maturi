/* 더보기 버튼 */


/* 댓글 업로드 */
const commentForm = document.commentFrm;
commentForm.commentSubmitBtn.addEventListener("click", ()=>{
  // commentForm.method = "post";
  // commentForm.action = `/article/${articleWrap.dataset.articleid}/comment`;
  // commentForm.submit();

  let lastCommentId = 0;
  if(document.querySelectorAll(".comment")[0] != null){
    lastCommentId = document.querySelectorAll(".comment")[0].dataset.commentid;
  }
  const url = `/api/article/${articleWrap.dataset.articleid}/comment`;
  fetch(url, {
    method: "post",
    body: JSON.stringify({
      commentBody: commentForm.commentBody.value,
      lastCommentId: lastCommentId
    }),
    headers: {
      "Content-type": "application/json"
    }
  }).then(response => response.json())
    .then(data => {
      console.log(data);
      window.location.reload(); // 일단은 리로드 -> 나중에 html구조 추가로 수정
    })
})

/* 댓글 영역별 이벤트 */
const commentWrap = document.getElementById("commentWrap");
const comment = commentWrap.querySelectorAll(".comment");
comment.forEach(e =>{
  /* 좋아요 클릭 */
  e.querySelector(".commentWrap .likeBtn").addEventListener("click", ()=>{
    const url = "/api/comment/" + e.dataset.commentid + "/likeOrUnlike";
    fetch(url, {
      method: "post",
      body: JSON.stringify({
        commentId: e.dataset.commentid
      }),
      headers: {
        "Content-type": "application/json"
      }
    }).then(response => response.json())
      .then(data =>{
        console.log(data);
        if(data.isLiked == 1){ // 좋아요 !!
          e.querySelector(".commentWrap .likeBtn").classList.add("isLikedArticle");
        } else { // 좋아요 취소!
          e.querySelector(".commentWrap .likeBtn").classList.remove("isLikedArticle");
        }
        e.querySelector(".commentWrap .likeNum").innerText = data.likeNum;
      })
  });

  /* 댓글 삭제 */
  e.querySelector(".commentWrap .commentDelete").addEventListener("click", ()=>{
    const url = "/api/comment/" + e.dataset.commentid;
    fetch(url, {
      method: "delete",
      headers: {
        "Content-type": "application/json"
      }
    }).then(response => {
      if(!response.ok){
        alert("댓글 삭제에 실패했습니다!");
        return;
      } else {
        e.remove(); // 해당 댓글 태그 삭제
      }
    })
  })

  /* 댓글 수정 */
    e.querySelector(".commentWrap .commentModify").addEventListener("click", (ee)=>{
      ee.preventDefault();
      let content = e.querySelector(".commentWrap .cContent").innerText;
      const commentBox = e.querySelector(".commentBox");
      commentBox.innerHTML =
        `<textarea type="text" class="commentModifyBody">${content}</textarea>
        <button type="button" class="commentModifyBtn">수정하기</button>
        <button type="button" class="commentModifyCancel">취소</button>`;

      // 취소 버튼 클릭
      commentBox.querySelector(".commentModifyCancel").addEventListener("click", ()=>{
        window.location.reload();
      });
      // 수정 버튼 클릭
      commentBox.querySelector(".commentModifyBtn").addEventListener("click",()=>{
        console.log("modify btn click");
        const url = "/api/comment/" + e.dataset.commentid;
        fetch(url, {
          method: "PATCH",
          body: JSON.stringify({
            commentBody: commentBox.querySelector(".commentModifyBody").value
          }),
          headers: {
            "Content-type": "application/json"
          }
        }).then(response => {
          if(!response.ok){
            alert("댓글 수정에 실패했습니다!");
            return;
          } else {
            window.location.reload();
          }
        })
      });
    });
});