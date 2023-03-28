// const searchParams = new URLSearchParams(location.search);
// searchParams.forEach(param => {
//   // console.log(param);
// })

function paramSearch(){
  const urlParams = new URL(location.href).searchParams;

  console.log("tag : " + urlParams.get("tag"));

  let radioCond = urlParams.get("radioCond") != null?
                urlParams.get("radioCond") : null;
  let latitude = urlParams.get("latitude") != null?
    urlParams.get("latitude") : null;
  let longitude = urlParams.get("longitude") != null?
    urlParams.get("longitude") : null;
  let category = urlParams.get("category") != null?
    urlParams.get("category") : null;


  let writer = urlParams.get("writer") != null?
    urlParams.get("writer") : null;
  let restaurantName = urlParams.get("restaurantName") != null?
    urlParams.get("restaurantName") : null;
  let tag = urlParams.get("tag") != null?
    urlParams.get("tag") : null;
  let all = urlParams.get("all") != null?
                urlParams.get("all") : null;
  let keyword = urlParams.get("keyword") != null?
    urlParams.get("keyword") : null;
  let lastArticleId = null;

  let obj = {
    'radioCond': radioCond,//검색에 필요한 값
    'latitude': latitude,//검색에 필요한 값
    'longitude': longitude,//검색에 필요한 값
    'category': category,//검색에 필요한 값
    'content': null,//검색에 필요한 값
    'writer': writer,//검색에 필요한 값
    'tag': tag,//검색에 필요한 값
    'restaurantName': restaurantName,//검색에 필요한 값
    'all': all,//검색에 필요한 값
    'keyword': keyword,//검색에 필요한 값
    'lastArticleId': null,//페이징에 필요한 값
    'size': 5, // 페이징에 필요한 값
    'event' : "click"//페이징에 필요한 값
  }

  if(restaurantName != null){ // 파라미터에 레스토랑
    searchKeyWordInput.value = restaurantName;
    document.getElementById("search-cond").innerText = "가게명";
    searchCategoryValue.setAttribute("name", "restaurantName");
    document.querySelector('input[name="keywordName"]').value = "restaurantName";
  } else if(tag != null){ // 파라미터에 태그
    searchKeyWordInput.value = tag;
    searchCategoryValue.setAttribute("name", "tag");
    document.querySelector('input[name="keywordName"]').value = "tag";
  }
  hasArticle = true;
  searchCondSetting("click");
  searchArticleAjax(obj);
  document.querySelector('#main-search-btn').click();
}

paramSearch();
