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
        document.querySelector('input[name="lastArticleId"]').value = data.lastArticleId;
        if(data.hasNext === false){
            hasArticle = false;
            document.querySelector('input[name="lastArticleId"]').value = null;
        }
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
