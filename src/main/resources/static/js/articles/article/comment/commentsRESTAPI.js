//-----------댓글, 대댓글 목록을 불러오는 작업------------
function getComments(){
    fetch(`/api/articles/${articleId}/comments`)
        .then((response)=>response.json())
        .then((data)=>{
            console.log(data);
            data.forEach((comment)=>{
                console.log("comment",comment);
            })
        })
}

//페이지가 로드될 때 댓글, 대댓글을 가져옴
getComments();