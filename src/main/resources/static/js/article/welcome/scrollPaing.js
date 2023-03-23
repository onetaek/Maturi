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
function addPage(page) {
    $.ajax({
        type: 'GET',
        url: '/api/articles/',
        data: {
            lastArticleId : 10,
            size: articlesPerPageSize, // max page size
        },
        dataType: 'json',
    }).done(function (result) {
        if (result.length == 0) {
            return;
        }

        // add articleList data
        const pageElement = document.createElement('div');
        pageElement.id = getPageId(page);
        pageElement.className = 'article-list__page';

        for (var i = 0; i < result.length; i++) {
            pageElement.appendChild(getArticle(result[i]));
        }

        articleList.appendChild(pageElement);

        // add articleListPagination data
        addPaginationPage(page);
    });
}

const articleList = document.getElementById('article-list');
const articleListPagination = document.getElementById(
    'article-list-pagination'
);
let page = 0;

// 초기 페이지
addPage(++page);

window.onscroll = function () {
    if (getScrollTop() < getDocumentHeight() - window.innerHeight) return;
    // 스크롤이 페이지 하단에 도달할 경우 새 페이지 로드
    addPage(++page);
};