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



      });//forEach문끝 요소들 넣어줌

      if (data.hasNext === false ){ // 게시글 모두 로드됐을 경우
        html += `<li class="no-more-article-message"><p>더 이상 게시글이 없습니다</p></li>`;
      }

      if (data.event === "click" || data.event === "load"){//클릭 or load면 기존 게시글을 덮어서 새로 입력
        articleList.innerHTML = html
      }
    })
}
