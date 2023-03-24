if (hasArticle){
    window.onscroll = function () {
        if (getScrollTop() < getDocumentHeight() - window.innerHeight){
            return;
        }
        // 스크롤이 페이지 하단에 도달할 경우 ajax요청을 통해 게시글 데이터를 받아옴
        addPage("scroll");
    };
}



function getDocumentHeight() {
    const body = document.body;
    const html = document.documentElement;

    return Math.max(
        body.scrollHeight,
        body.offsetHeight,
        html.clientHeight,
        html.scrollHeight,
        html.offsetHeight
    );
}

function getScrollTop() {
    return window.pageYOffset !== undefined
        ? window.pageYOffset
        : (
            document.documentElement ||
            document.body.parentNode ||
            document.body
        ).scrollTop;
}

