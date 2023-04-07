function reportArticle(articleId){
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
      const url = "/api/articles/"+ articleId +"/report";

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
            alert("해당 게시글을 신고했습니다.");
          } else if(response.status == 226) {
            alert("이미 신고한 글입니다.");
          } else if(response.status == 404){
            alert("해당 게시글을 찾을 수 없습니다.");
            location.href = "/articles";
          }
        })
    },
    allowOutsideClick: () => !Swal.isLoading()
  })
  // if(confirm("정말 해당 게시글을 신고하시겠습니까?")){
  //   const url = "/api/articles/"+ articleId +"/report";
  //   fetch(url, {
  //     method: "post"
  //   }).then((response)=>{
  //     if(response.status == 200){ // 신고 성공
  //       alert("해당 게시글을 신고했습니다.");
  //     } else if(response.status == 226) {
  //       alert("이미 신고한 글입니다.");
  //     } else if(response.status == 404){
  //       alert("해당 게시글을 찾을 수 없습니다.");
  //       location.href = "/articles";
  //     }
  //   })
  // }
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