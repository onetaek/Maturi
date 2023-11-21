// let isFetching = true;
//
// if(isFetching){
//     window.onscroll = function () {
//
//         isFetching = false;
//         if (hasArticle){
//             if (getScrollTop() < getDocumentHeight() - window.innerHeight -1){
//                 return;
//             }else{
//                 // 스크롤이 페이지 하단에 도달할 경우 ajax요청을 통해 게시글 데이터를 받아옴
//                 console.log("스크롤 이벤트 동작!!!");
//                 addPage("scroll");
//             }
//         }
//
//     };
//     isFetching = true;
// }

let throttleTimer;
let isLoading = false;

function handleScroll() {
    if (isLoading) {
        return;
    }

    clearTimeout(throttleTimer);

    throttleTimer = setTimeout(function() {
        const scrollBottom = $(document).height() - $(window).height() - $(window).scrollTop() - 1;

        if (scrollBottom < 100 && hasArticle) { // 스크롤바가 아래쪽 100px 이내로 내려오면 추가로 게시글을 로드합니다.
            isLoading = true;
            addPage("scroll");
        }
    }, 500); // 500ms(0.5초) 동안 스크롤 이벤트를 무시합니다.
}

$(window).on('scroll', handleScroll);


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

