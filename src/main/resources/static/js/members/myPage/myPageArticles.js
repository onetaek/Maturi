const myPageMain = document.getElementById("content"); // 마이페이지 메인 컨텐츠
const reviewWrap = document.getElementById("reviewWrap"); // 리뷰글 공간
let hasArticle = true; // 로드할 글 존재하는지 여부

if(hasArticle === true){
  addPage("load"); // 페이지 로드시
}


// 검색 조건을 세팅해주고 Ajax요청을 실행하는 함수
function addPage(event) {
  let obj = condSetting(event);
  myPageArticleAjax(obj);
}

function condSetting(event){
  let lastArticleInput = reviewWrap.querySelector('input[name="lastArticleId"]');
  let lastArticle;
  if(lastArticleInput.value === "" || lastArticleInput.value === null){
    lastArticle == null;
  }else{
    lastArticle = lastArticleInput.value;
  }
  let obj = {
    'lastArticleId': lastArticle,//페이징에 필요한 값
    'size': 5, // 페이징에 필요한 값
    'event' : event//페이징에 필요한 값
  };
  return obj;
}

/* 글 리스트 불러오기 */
function myPageArticleAjax(obj){
  const url = "/api/articles/members/" + myPageMain.dataset.memberid
    +'?lastArticleId='+_fnToNull(obj['lastArticleId'])
    +'&size='+_fnToNull(obj['size'])
    +'&event='+_fnToNull(obj['event']);
  console.log(url);

  fetch(url)
    .then((response) => response.json())
    .then((data)=>{

      reviewWrap.querySelector('input[name="lastArticleId"]').value = data.lastArticleId;//마지막 게시글의 id를 저장

      if(data.hasNext === false){ // 다음 페이즈 없을 경우
        hasArticle = false;
        document.querySelector('input[name="lastArticleId"]').value = null;
      }
      let articleList = document.querySelector('#article-list');
      let html = ``;
      let articles = data['content'];

      // 게시글 없을 경우
      if((data.event === "click" || data.event === "load") && articles.length === 0){
        html += `<li class="no-search-message"><p>게시글이 없습니다</p></li>`;
        articleList.innerHTML = html;
        html = ``;
        console.log("게시글이 없습니다.")
        return;
      }

      articles.forEach((article) => {

        let articleHtml = ``;
        articleHtml += `<li id="articleWrap" class="article-wrap article-wrap${article.id}" data-articleid=${article.id}>
                <!--    글쓴이 정보 -->
                <div class="writerInfo">
                    <p class="writerProfileImg">`
        if(article.profileImg == null || article.profileImg === ""){
          articleHtml += `<img src="/img/profileImg/default_profile.png" alt="프로필 이미지">`
        }else if(article.profileImg.includes("://")){
          articleHtml += `<img src="${article.profileImg}" alt="프로필 이미지">`
        }else{
          articleHtml += `<img src="/test/file/${article.profileImg}" alt="프로필 이미지">`
        }
        articleHtml +=`</p>
                    <p class="written">
                        <span>
                          <span class="writerNickName">${article.nickName}</span>
                          (<span class="writerName">${article.name}</span>)
                        </span>
                        <span class="writtenAt">${article.modifiedDate}</span>
                    </p>
                </div>
                <!--    글 본문 -->
                <div class="contentWrap contentWrap${article.id}">
                    <ul class="bImg">`

        // 이미지 처리 ..
        article.image.forEach(img =>{
          articleHtml += `<li>
                              <img src="/test/file/${img}" alt="사진1">
                          </li>`
        })

        articleHtml += `</ul>
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
                    <ul class="ellipsis-content ellipsis-content${article.id}">`
        /* 작성자 = 로그인 회원 확인해서 버튼 매칭 */
        if(article.memberId == memberId){
          articleHtml +=
            `<form action="/articles/${article.id}" method="post" name="articleUpdateForm${article.id}" class="articleUpdateForm${article.id}">
              <input type="hidden" name="_method">
              </form>
            <li><a href="#" class="updateArticle${article.id}">게시글 수정</a></li>
            <li><a href="#" class="deleteArticle${article.id}">게시글 삭제</a></li>`
        } else {
          articleHtml += `<li><a href="#">신고하기</a></li>`
        }

        articleHtml += `</ul>
                </div>
            </li>`;

        if (data.event === "scroll"){
          //위의 코드를 통해 만들어준 html을 append해준다.
          articleList.appendChild(myCreateElement(articleHtml));
        } else if (data.event === "click" || data.event === "load" ) {
          html += articleHtml;
        }
      });//forEach문끝 요소들 넣어줌

      if (data.hasNext === false ){ // 게시글 모두 로드됐을 경우
        html += `<li class="no-more-article-message"><p>더 이상 게시글이 없습니다</p></li>`;
      }
      if (data.event === "click" || data.event === "load"){//클릭 or load면 기존 게시글을 덮어서 새로 입력
        articleList.innerHTML = html
      }

      /* 좋아요 이벤트 걸어주기 (db통신) */
      articles.forEach((article) => {
        console.log("좋아요 클릭 이벤트 걸어줄 하나의 article",article);
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

      /* 해당 게시글의 더보기 버튼 */
      articles.forEach((article) => {
        //좋아요 클릭 이벤트를 적용해준다.
        const ellipsisBtn = document.querySelector(`.ellipsis-btn${article.id}`);
        const ellipsisContent = document.querySelector(`.ellipsis-content${article.id}`);
        console.log(ellipsisBtn);
        console.log(ellipsisContent);
        ellipsisBtn.addEventListener("click",function(){
          ellipsisContent.classList.toggle("active");
        });

        //게시글 화면 클릭시 해당 게시글 상세 페이지로 이동
        let articleContent = document.querySelector(`.contentWrap${article.id}`);
        const articleId = document.querySelector(`.article-wrap${article.id}`).dataset.articleid;
        articleContent.addEventListener("click",()=>{
          window.location.href=`/articles/${articleId}`;
        });
      });//articles.forEach끝(이벤트걸어줌)

      /* 게시글 삭제 이벤트 */
      articles.forEach((article) => {
        const articleUpdateForm = document.querySelector(`.articleUpdateForm${article.id}`);
        const deleteBtn = document.querySelector(".deleteArticle${article.id}");
        const updateBtn = document.querySelector(".updateArticle${article.id}");
        deleteBtn.addEventListener("click", ()=>{
          articleUpdateForm._method.value = "delete";
          articleUpdateForm.action = `/articles/${article.id}`;
          articleUpdateForm.submit();
        })
      })
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