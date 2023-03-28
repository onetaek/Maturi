function reportArticle(articleId){
  if(confirm("정말 해당 게시글을 신고하시겠습니까?")){
    const url = "/api/articles/"+ articleId +"/report";
    fetch(url, {
      method: "post"
    }).then((response)=>{
      if(response.status == 200){ // 신고 성공
        alert("해당 게시글을 신고했습니다.");
      } else if(response.status == 226) {
        alert("이미 신고한 글입니다.");
      } else if(response.status == 404){
        alert("해당 게시글을 찾을 수 없습니다.");
        location.href = "/articles";
      }
    })
  }
}

function reportComment(commentId){
  if(confirm("정말 해당 댓글을 신고하시겠습니까?")){
    const url = "/api/comment/"+ commentId +"/report";
    fetch(url, {
      method: "post"
    }).then((response)=>{
      if(response.status == 200){ // 신고 성공
        alert("해당 댓글을 신고했습니다.");
      } else if(response.status == 226) {
        alert("이미 신고한 댓글입니다.");
      } else if(response.status == 404){
        alert("해당 댓글을 찾을 수 없습니다.");
        location.href = "/articles";
      }
    })
  }
}