// 검색 조건을 세팅해주고 Ajax요청을 실행하는 함수
function addPage(event) {
    let obj = SearchCondSetting(event);
    let orderCond = orderCondSetting(event);
    searchArticleAjax(obj,orderCond);
    isLoading = false;
}

//enter버튼을 눌렀을 때 Ajax요청으로 게시판 정보를 가져옴
document.querySelector('.search-input').addEventListener("keyup", ()=>{
    if(window.event.keyCode === 13){
        addPage("click");
    }
})

//검색 버튼을 눌렀을 때 Ajax요청으로 게시판 정보를 가져옴
document.querySelector('#main-search-btn').addEventListener('click',()=>{
    console.log("검색 아이콘 클릭!");
    let obj = SearchCondSetting("click");
    let orderCond = orderCondSetting("click");
    console.log("name",searchCategoryValue.name);
    console.log("value",searchCategoryValue.value);
    searchArticleAjax(obj,orderCond);
});


//scrollEvent.js에 스크롤로 하단에 도달했을 때 Ajax요청코드 있음

//Ajax요청으로 데이터를 받고 게시글을 화면에 추가해주는 코드
function searchArticleAjax(obj,orderCond){
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
        +'&orderBy='+_fnToNull(orderCond['orderBy'])
        +'&views='+_fnToNull(orderCond['views'])
        +'&commentCount='+_fnToNull(orderCond['commentCount'])
        +'&likeCount='+_fnToNull(orderCond['likeCount']);

    fetch(url)
    .then((response) => {
        if(response.status === 404){
            hasArticle = false;
            document.querySelector('input[name="lastArticleId"]').value = null;
            let articleList = document.querySelector('#article-list');
            let html = ``;
            html += `<li class="no-search-message"><p>조건에 맞는 게시글이 없습니다</p></li>`;
            articleList.innerHTML = html;
            html = ``;
            console.log("조건에 맞는 게시글이 없습니다.")
            return;
        }
        return response.json();
    })
    .then((data) => {
        console.log("data",data);
        document.querySelector('input[name="lastArticleId"]').value = data.lastArticleId;//마지막 게시글의 id를 저장

        if(data.hasNext === false){
            hasArticle = false;
            document.querySelector('input[name="lastArticleId"]').value = null;
        }else{
            hasArticle = true;
        }

        let articleList = document.querySelector('#article-list');
        let html = ``;
        let articles = data['content'];

        articles.forEach((article) => {

            if(article.id === data.lastArticleId){
                document.querySelector('input[name="views"]').value = article.views;
                document.querySelector('input[name="commentCount"]').value = article.commentCount;
                document.querySelector('input[name="likeCount"]').value = article.like;
            }

            let articleHtml = ``;
            articleHtml += `<li class="article-wrap article-wrap${article.id}" data-articleid=${article.id}>
                <!--    글쓴이 정보 -->
                <div class="writerInfo">
                    <a class="writerProfileImg" href="/members/${article.memberId}">`
                if(article.profileImg == null || article.profileImg === ""){
                    articleHtml += `<img src="/img/profileImg/default_profile.png" alt="프로필 이미지">`
                }else if(article.profileImg.includes("http")){
                    articleHtml += `<img src="${article.profileImg}" alt="프로필 이미지">`
                }else{
                    articleHtml += `<img src="${article.profileImg}" alt="프로필 이미지">`
                }
            articleHtml +=`</a>
                    <div class="user-info">
                        <a class="writer-name" href="/members/${article.memberId}">
                            <span class="writerNickName">${article.nickName}</span>
                            (<span class="writerName">${article.name}</span>)
                        </a>
                        <span class="writtenAt">${article.date}</span>
                    </div>
                </div>
                <!--    글 본문 -->
                <div class="contentWrap contentWrap${article.id}" onclick="location.href='/articles/${article.id}'">`
            if(article.image.length ===1){
                articleHtml+= `<ul class="bImg img-length-1">`
            }else if(article.image.length ===2){
                articleHtml+= `<ul class="bImg img-length-2">`
            }else if(article.image.length ===3){
                articleHtml+= `<ul class="bImg img-length-3">`
            }else {
                articleHtml+= `<ul class="bImg img-length-4">`
            }
            // 이미지 처리 ..
            for(let i=0; i<article.image.length; i++) {
                if(i >= 3){
            articleHtml+=`<div class="overflow-img-wrap">
                            <span>+ ${article.image.length - 3}</span>
                        </div>`;
                    break; // i가 3보다 크거나 같을 때 종료
                }
                const img = article.image[i];
                articleHtml+=`<li>
                  <div style="background-image:url('${img}')"></div>
                </li>`;
            }
            articleHtml += `
                        <div class="restaurant-info-wrap">
                            <div><ion-icon name="location"></ion-icon>${article.address}</div>
                            <div>${article.restaurantName}</div>
                        </div>
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
                    </span>
                   
                    <div class="tagWrap">`
        article.tags.forEach((tag) => {
            articleHtml += `<span class="tag-box"><span>${tag}</span></span>`
        });
            articleHtml += `
                   </div>
                   <span class="count-info">
                        <span class="count-wrap">
                            <ion-icon name="eye-outline"></ion-icon>
                            <span>${article.views}</span>
                        </span>
                        <span class="count-wrap">
                            <ion-icon name="chatbubbles-outline"></ion-icon>
                            <span>${article.commentCount}</span>
                        </span>
                   </span>        
                </div>
                <!--     리뷰글 더보기 버튼 -->
                <div class="ellipsis-btn-wrap">
                    <span class="ellipsis-btn ellipsis-btn${article.id}" onclick="ellipsisToggle(this)">
                        <ion-icon name="ellipsis-vertical-outline"></ion-icon>
                    </span>
                    <ul class="ellipsis-content ellipsis-content${article.id}">`
                    if(article.memberId === memberId){
                        articleHtml += `<li>
                                            <div>
                                                <ion-icon name="git-compare-outline"></ion-icon>
                                                <a href="/articles/${article.id}/edit">게시글 수정</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div>
                                                <ion-icon name="trash-outline"></ion-icon>
                                                <a href="#" onclick="deleteArticle(${article.id})">게시글 삭제</a>
                                            </div>
                                        </li>`;
                    }else{
                        if(article.followingMember){//팔로잉하고 있는 유저 -> 팔로우 취소 버튼 출력
                            articleHtml += `<li class="followingBtnWrap">
                                                <div onclick="followCancel(${article.id},${article.memberId},'${article.nickName}')">
                                                    <ion-icon name="person-remove-outline"></ion-icon>
                                                    <span>팔로잉 취소</span>
                                                </div>
                                            </li>`
                        }else{//팔로잉을 하지 않고있는 유저 -> 팔로잉 버튼 출력
                            articleHtml += `<li class="followingBtnWrap">
                                                <div onclick="following(${article.id},${article.memberId},'${article.nickName}')">
                                                    <ion-icon name="person-add-outline"></ion-icon>
                                                    <span>팔로잉</span>
                                                </div>
                                            </li>`
                        }
                        articleHtml += `<li>
                                            <div onclick="reportArticle(${article.id})">
                                                <ion-icon name="warning-outline"></ion-icon>
                                                <a>신고하기</a>
                                            </div>
                                        </li>`;
                        articleHtml += `<li>
                                            <div onclick="blockMember(${article.memberId},'${article.nickName}')">
                                                <ion-icon name="ban-outline"></ion-icon>
                                                <a>차단하기</a>
                                            </div>
                                        </li>`;
                    }
               articleHtml+=`</ul>
                </div>
            </li>`;

            if (data.event === "scroll"){
                //위의 코드를 통해 만들어준 html을 append해준다.
                articleList.appendChild(myCreateElement(articleHtml));
            } else if (data.event === "click" || data.event === "load" ) {
                html += articleHtml;
            }
        });//forEach문끝 요소들 넣어줌

        if (data.hasNext === false){
            if(data.event === "click" || data.event === "load"){
                if(articles.length === 0){
                    articleList.innerHTML = `<li class="no-search-message"><p>조건에 맞는 게시글이 없습니다</p></li>`;
                    console.log("조건에 맞는 게시글이 없습니다. return false");
                }else{
                    html += `<li class="no-more-article-message"><p>더 이상 게시글이 없습니다</p></li>`;
                    articleList.innerHTML = html
                }
            }else if(data.event ==="scroll"){
                articleList.appendChild(myCreateElement(`<li class="no-more-article-message"><p>더 이상 게시글이 없습니다</p></li>`));
            }
        } else {
            if(data.event === "click" || data.event === "load"){
                articleList.innerHTML = html
            }
        }


        articles.forEach((article) => {
            //좋아요 클릭 이벤트를 적용해준다.
            const likeBtn = document.querySelector(`.likeBtn${article.id}`);
            if(article.liked){
                likeBtn.classList.add("isLikedArticle");
            }
            const likeNum = document.querySelector(`.likeNum${article.id}`);
            const articleLi = document.querySelector(`.article-wrap${article.id}`)
            likeBtn.addEventListener("click", ()=>{
                const url = "/api/articles/"+articleLi.dataset.articleid+"/like";
                fetch(url, {
                    method: "post",
                    headers: {
                        "Content-type": "application/json"
                    }
                }).then((response) => response.json())
                .then((data) => {
                    console.log(data);
                    if(data.isLiked == 1){ // 좋아요!
                        likeBtn.classList.add("isLikedArticle");
                    } else { // 좋아요 취소!
                        likeBtn.classList.remove("isLikedArticle");
                    }
                    likeNum.innerText = data.likeNum;
                })
            });
        });//articles.forEach끝(이벤트걸어줌)

        // articleListImgStyle();
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
addPage("load");

