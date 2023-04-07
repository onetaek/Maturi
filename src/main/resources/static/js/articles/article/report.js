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
            Swal.fire({
              title: "해당 게시글을 신고했습니다.",
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
              title: "해당 게시글을 찾을 수 없습니다.",
              icon: 'error',
              confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
            }).then(function () {
              location.href = "/articles";
            })
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
  Swal.fire({
    title: "정말 해당 댓글을 신고하시겠습니까?",
    icon: "question",
    showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
    confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
    cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
    confirmButtonText: '신고하기', // confirm 버튼 텍스트 지정
    cancelButtonText: '취소', // cancel 버튼 텍스트 지정
    reverseButtons: false, // 버튼 순서 거꾸로
    // background-color: #6e7881
  }).then((result) => {
    if (result.isConfirmed) {
      const url = "/api/comment/"+ commentId +"/report";
      fetch(url, {
        method: "post"
      }).then((response)=>{
        if(response.status == 200){ // 신고 성공
          Swal.fire({
            title: "해당 댓글을 신고했습니다.",
            icon: 'success',
            confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
          })
        } else if(response.status == 226) {
          Swal.fire({
            title: "이미 신고한 댓글입니다.",
            icon: 'info',
            confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
          })
        } else if(response.status == 404){
          Swal.fire({
            title: "해당 댓글을 찾을 수 없습니다.",
            icon: 'error',
            confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
          }).then(function () {
            location.href = "/articles";

          })
        }
      })
    }
  });

}