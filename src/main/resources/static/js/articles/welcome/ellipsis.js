function deleteArticle(articleId){

    if(confirm("게시글을 삭제하시겠습니까?")){
        console.log("게시글 삭제");

        let articleDeleteForm = document.articleDeleteForm;
        console.log(articleDeleteForm);
        articleDeleteForm.action = `/articles/${articleId}`;
        articleDeleteForm.submit();
    }
}


