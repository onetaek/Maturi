// 검색 조건을 세팅해주고 Ajax요청을 실행하는 함수
function addPage(event) {
    let obj = searchCondSetting(event);
    searchArticleAjax(obj);
}

//검색 버튼을 눌렀을 때 Ajax요청으로 게시판 정보를 가져옴
if(hasArticle === true){
    document.querySelector('#main-search-btn').addEventListener('click',()=>{
        console.log("검색 아이콘 클릭!");
        let obj = searchCondSetting("click");
        console.log("name",searchCategoryValue.name);
        console.log("value",searchCategoryValue.value);
        searchArticleAjax(obj);
    });
}

//scrollEvent.js에 스크롤로 하단에 도달했을 때 Ajax요청코드 있음

//Ajax요청으로 데이터를 받고 게시글을 화면에 추가해주는 코드
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
        document.querySelector('input[name="lastArticleId"]').value = data.lastArticleId;//마지막 게시글의 id를 저장
        if(data.hasNext === false){
            hasArticle = false;
            document.querySelector('input[name="lastArticleId"]').value = null;
        }
        let articleList = document.querySelector('#article-list');
        let html = ``;
        let articles = data['content'];

        if((data.event === "click" || data.event === "load") && articles.length === 0){
            html += `<li class="no-search-message"><p>조건에 맞는 게시글이 없습니다</p></li>`;
            articleList.innerHTML = html;
            html = ``;
            console.log("조건에 맞는 게시글이 없습니다.")
            return;
        }

        articles.forEach((article) => {

            let articleHtml = ``;

            articleHtml += `<li class="article-wrap article-wrap${article.id}" data-articleid=${article.id}>
                <!--    글쓴이 정보 -->
                <div class="writerInfo">
                    <p class="writerProfileImg">`
                if(article.profileImg == null || article.profileImg === ""){
                    articleHtml += `<img src="#" alt="프로필 이미지">`
                }else if(article.profileImg.includes("http")){
                    articleHtml += `<img src="${article.profileImg}" alt="프로필 이미지">`
                }else{
                    articleHtml += `<img src="/test/file/${article.profileImg}" alt="프로필 이미지">`
                }
            articleHtml +=`</p>
                    <p class="written">
                        <span class="writerName">${article.name}</span>
                        <span class="writerNickName">${article.nickName}</span>
                        <span class="writtenAt">${article.modifiedDate}</span>
                    </p>
                </div>
                <!--    글 본문 -->
                <div class="contentWrap contentWrap${article.id}">
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
                      <span class="likeBtn likeBtn${article.id}">좋아요</span>
                      <span class="likeNum likeNum${article.id}">${article.like}</span>
                    </span>`
            article.tags.forEach((tag) => {
                articleHtml += `<span><a href="#">${tag}</a></span>`
            });
            articleHtml += `
                </div>
                <!--     리뷰글 더보기 버튼 -->
                <div class="ellipsis-btn-wrap">
                    <span class="ellipsis-btn ellipsis-btn${article.id}">
                        <ion-icon name="ellipsis-vertical-outline"></ion-icon>
                    </span>
                    <ul class="ellipsis-content ellipsis-content${article.id}">
                        <li><a href="#">게시글 수정</a></li>
                        <li><a href="#">게시글 삭제</a></li>
                        <li><a href="#">신고하기</a></li>
                    </ul>
                </div>
            </li>`;

            if (data.event === "scroll"){
                //위의 코드를 통해 만들어준 html을 append해준다.
                const template = document.createElement('template');
                template.innerHTML = articleHtml.trim();
                console.log("template.content.firstElementChild",template.content.firstElementChild);
                articleList.appendChild(template.content.firstElementChild);
            } else if (data.event === "click" || data.event === "load" ) {
                html += articleHtml;
            }
        });//forEach문끝 요소들 넣어줌

        if (data.hasNext === false ){
            html += `<li class="no-more-article-message"><p>더 이상 게시글이 없습니다</p></li>`;
        }
        if (data.event === "click" || data.event === "load"){//클릭 or load면 기존 게시글을 덮어서 새로 입력
            articleList.innerHTML = html
        }
        console.log("좋아요 클릭이벤트 걸어주기전의 articles ",articles);
        // articles.forEach((article) => {
        //     console.log("좋아요 클릭 이벤트 걸어줄 하나의 article",article);
        //     //좋아요 클릭 이벤트를 적용해준다.
        //     const likeBtn = document.querySelector(`.likeBtn${article.id}`);
        //     if(article.liked){
        //         likeBtn.classList.add("isLikedArticle");
        //     }
        //     const likeNum = document.querySelector(`.likeNum${article.id}`);
        //     const articleLi = document.querySelector(`.article-wrap${article.id}`)
        //     likeBtn.addEventListener("click", ()=>{
        //         const url = "/api/article/likeOrUnlike/" + articleLi.dataset.articleid;
        //         fetch(url, {
        //             method: "post",
        //             headers: {
        //                 "Content-type": "application/json"
        //             }
        //         }).then((response) => response.json())
        //         .then((data) => {
        //             console.log(data);
        //             if(data.isLiked == 1){ // 좋아요!
        //                 likeBtn.classList.add("isLikedArticle");
        //             } else { // 좋아요 취소!
        //                 likeBtn.classList.remove("isLikedArticle");
        //             }
        //             likeNum.innerText = data.likeNum;
        //         })
        //     });
        // });//articles.forEach끝(이벤트걸어줌)

        // articles.forEach((article) => {
        //     //좋아요 클릭 이벤트를 적용해준다.
        //     const ellipsisBtn = document.querySelector(`.ellipsis-btn${article.id}`);
        //     const ellipsisContent = document.querySelector(`.ellipsis-content${article.id}`);
        //
        //     for(let i = 0 ; i < ellipsisBtn.length; i++){
        //         ellipsisBtn[i].addEventListener("click",function(){
        //             ellipsisContent[i].classList.toggle("active");
        //         });
        //     }
        // });//articles.forEach끝(이벤트걸어줌)

        // articles.forEach((article,idx,arr) => {
        //
        //     let articleContent = document.querySelector(`.contentWrap${article.id}`);
        //     const articleId = document.querySelector(`.article-wrap${article.id}`).dataset.articleid;
        //     articleContent.addEventListener("click",()=>{
        //         window.location.href=`/article/${articleId}`;
        //     });
        //
        // });//articles.forEach끝(이벤트걸어줌)

    })
}

//json에 값을 전달할 때 unfined,null처리
function _fnToNull(data) {
    // undifined나 null을 null string으로 변환하는 함수.
    if (String(data) == 'undefined' || String(data) == 'null') {
        return ''
    } else {
        return data
    }
}

// 초기 페이지가 load될때 Ajax요청으로 게시판 정보를 가져옴
if(hasArticle === true){
    addPage("load");
}
