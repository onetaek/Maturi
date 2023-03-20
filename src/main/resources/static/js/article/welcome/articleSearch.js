let articleSearchForm = document.articleSearchForm;
let searchKeyWordInput = document.querySelector(".search-input");//검색 keyword input
let searchCategoryValue = document.querySelector(".search-category-value");//hidden input
let searchDropdownList = document.querySelectorAll(".dropdown-list-item");//dropdown li들

let searchRadioList = document.querySelectorAll('.input-container');
let myLatitude;
let myLongitude;

//keyword검색 조건(dropdown버튼)
for(let i = 0 ; i < searchDropdownList.length; i++){
    searchDropdownList[i].addEventListener("click",()=>{
       searchCategoryValue.setAttribute("name",
           searchDropdownList[i].getAttribute("data-keyword-name"));
       searchCategoryValue.setAttribute("value",searchKeyWordInput.value);
    });
}

function searchKeyWord(){
    articleSearchForm.action = "/articleList";
    articleSearchForm.method = "GET";
    articleSearchForm.submit();
}

//위도 경도 값 가져오기
function onGeoOk(position){
    myLatitude = position.coords.latitude; // 위도
    myLongitude = position.coords.longitude; // 경도
    console.log({"latitude":myLatitude,"longitude":myLongitude});
    document.querySelector("input[name=latitude]").value = myLatitude;
    document.querySelector("input[name=longitude]").value = myLongitude;
}
function onGeoError(){
    alert("현재위치를 찾을 수 없습니다.");
}
navigator.geolocation.getCurrentPosition(onGeoOk,onGeoError);
