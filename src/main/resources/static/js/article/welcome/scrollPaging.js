const articlesPerPageSize = 5;

function getPageId(n) {
    return 'article-page-' + n;
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

// result data 로 image tag 생성
function getArticle(data) {
    const image = new Image();
    image.className =
        'article-list__item__image article-list__item__image--loading';
    image.src = '/gallery/display?id=' + data.mainImageId;
    image.onclick = function () {
        location.href = '/gallery/' + data.id;
    };

    image.onload = function () {
        image.classList.remove('article-list__item__image--loading');
    };

    const article = document.createElement('article');
    article.className = 'article-list__item';
    article.appendChild(image);

    return article;
}

// 해당 page 정보를 pagination 리스트에 추가
function addPaginationPage(page) {
    const pageLink = document.createElement('a');
    pageLink.href = '#' + getPageId(page);
    pageLink.innerHTML = page;

    const listItem = document.createElement('li');
    listItem.className = 'article-list__pagination__item';
    listItem.appendChild(pageLink);

    articleListPagination.appendChild(listItem);

    if (page === 2) {
        articleListPagination.classList.remove(
            'article-list__pagination--inactive'
        );
    }
}

// ajax 로 해당 page 데이터 가져와서 뿌려주기

function addPage(event) {
    let obj = searchCondSetting(event);
    searchArticleAjax(obj);
}

function searchArticleAjax(obj){
    console.log("페이징 처리 ajax요청");
    const url = '/api/articles?radioCond='+_fnToNull(obj['radioCond'])
        +'&latitude='+_fnToNull(obj['latitude'])
        +'&longitude='+_fnToNull(obj['longitude'])
        +'&category='+_fnToNull(obj['category'])
        +'&content='+_fnToNull(obj['content'])
        +'&writer='+_fnToNull(obj['writer'])
        +'&tag='+_fnToNull(obj['tag'])
        +'&restaurantName='+_fnToNull(obj['restaurantName'])
        +'&all='+_fnToNull(obj['all'])
        +'&keyword='+_fnToNull(obj['keyword'])
        +'&lastArticleId='+_fnToNull(obj['lastArticleId'])
        +'&size='+_fnToNull(obj['size'])
        +'&event='+_fnToNull(obj['event'])
    console.log("ajax요청 url",url);
    fetch(url)
    .then((response) => response.json())
    .then((data) => {
        console.log(data);
        document.querySelector('input[name="lastArticleId"]').value = data.lastArticleId;
        console.log("어떤 동작인가요?",data.event);
        let articleList = document.querySelector('#article-list');
        let html = ``;
        let articles = data['content'];
        console.log("articles",articles)
        if((data.event === "click" || data.event === "load") && articles.length === 0){
            html += `<li><p>조건에 맞는 게시글이 없습니다ㅠㅠ</p></li>`;
            articleList.innerHTML = html;
            return;
        }
        articles.forEach((article) => {
            html += `<li class="article-wrap" data-articleid=${article.id}>
                <!--    글쓴이 정보 -->
                <div class="writerInfo">
                    <p class="writerProfileImg">`
                if(article.profileImg == null || article.profileImg === ""){
                    html += `<img src="#" alt="프로필 이미지">`
                }else if(article.profileImg.includes("http")){
                    html += `<img src="${article.profileImg}" alt="프로필 이미지">`
                }else{
                    html += `<img src="/test/file/${article.profileImg}" alt="프로필 이미지">`
                }
            html +=`</p>
                    <p class="written">
                        <span class="writerName">${article.name}</span>
                        <span class="writerNickName">${article.nickName}</span>
                        <span class="writtenAt">${article.modifiedDate}</span>
                    </p>
                </div>
                <!--    글 본문 -->
                <div class="contentWrap">
                    <ul class="bImg">
                        <li><img src="/test/file/${article.image}" alt="사진1"></li>
                    </ul>
                    <p class="bContent">
                        ${article.content}
                    </p>
                </div>
                <!--    좋아요 & 태그 -->
                <div class="likeAndTag">
                    <span class="likeWrap">
                      <span class="likeBtn">좋아요</span>
                      <span class="likeNum">${article.like}</span>
                    </span>`
            article.tags.forEach((tag) => {
                html += `<span><a href="#">${tag}</a></span>`
            });
            html += `
                </div>
                <!--     리뷰글 더보기 버튼 -->
                <div class="ellipsis-btn-wrap">
                    <span class="ellipsis-btn">
                        <ion-icon name="ellipsis-vertical-outline"></ion-icon>
                    </span>
                    <ul class="ellipsis-content">
                        <li><a href="#">게시글 수정</a></li>
                        <li><a href="#">게시글 삭제</a></li>
                        <li><a href="#">신고하기</a></li>
                    </ul>
                </div>
            </li>`;
            if (data.event === "scroll"){
                const template = document.createElement('template');
                template.innerHTML = html.trim();
                console.log(template.innerHTML);
                console.log(template.content);
                articleList.appendChild(template.content.firstElementChild);
            }
        })
        if (data.hasNext === false ){
            html += `<li><p>더이상 게시글이 없습니다!</p></li>`;
        }
        if (data.event === "click" || data.event === "load"){
            console.log("click이벤트 또는 load이벤트입니다");
            articleList.innerHTML = html
        }

    })
}

const articleList = document.getElementById('article-list');
const articleListPagination = document.getElementById(
    'article-list-pagination'
);

// 초기 페이지
addPage("load");

window.onscroll = function () {
    if (getScrollTop() < getDocumentHeight() - window.innerHeight) return;
    // 스크롤이 페이지 하단에 도달할 경우 새 페이지 로드
    addPage("scroll");
};