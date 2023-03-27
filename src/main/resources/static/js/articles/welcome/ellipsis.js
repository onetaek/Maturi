function deleteArticle(articleId){

    if(confirm("게시글을 삭제하시겠습니까?")){
        console.log("게시글 삭제");

        let articleUpdateForm = document.articleUpdateForm;
        articleUpdateForm._method.value = "delete";
        articleUpdateForm.action = `/articles/${articleId}`;
        articleUpdateForm.submit();
    }
}



