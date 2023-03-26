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
  const url = "/api/members/" + myPageMain.dataset.memberid
    +'?lastArticleId='+_fnToNull(obj['lastArticleId'])
    +'&size='+_fnToNull(obj['size'])
    +'&event='+_fnToNull(obj['event']);
  console.log(url);

  fetch(url)
    .then((response) => response.json())
    .then((data)=>{
      console.log(data);
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