let articleSearchForm = document.articleSearchForm;
let searchKeyWordInput = document.querySelector(".search-input");//검색 keyword input
let searchCategoryValue = document.querySelector(".search-category-value");//hidden input
let searchDropdownList = document.querySelectorAll(".dropdown-list-item");//dropdown li들

let searchRadioList = document.querySelectorAll('.input-container');
let myLatitude;
let myLongitude;


function myLatitudeMyLongitude(){//내위치 값을 가져오는 함수

    if (navigator.geolocation) {

        console.log("geolocation 시작");
        // GeoLocation을 이용해서 접속 위치를 얻어옵니다
        navigator.geolocation.getCurrentPosition(function(position) {
            myLatitude = position.coords.latitude; // 위도
            myLongitude = position.coords.longitude; // 경도
            console.log({"latitude":myLatitude,"longitude":myLongitude});
            return {"latitude":myLatitude,"longitude":myLongitude};
        });
    } else { // HTML5의 GeoLocation을 사용할 수 없을때
        alert("위치 엑세스를 허용해주세요!");
    }
}

searchRadioList.forEach(item =>{//item은 li요소 하나를 의미하는 것이다.
   item.addEventListener("click",(e)=>{
       let category = e.target.value;
       console.log(category);
       switch(category) {
           case "interLocal":
               console.log("관심지역 클릭.");
               break;
           case "myLocal":
               let obj = myLatitudeMyLongitude();
               console.log(myLatitude);
               console.log(myLongitude);
               console.log("현재위치 클릭.");
               break;
           case "category":
               console.log("카테고리 클릭");
               break;
           default:
               console.log("기본값");
               break;
       }


   });
});



//keyword검색 조건(dropdown버튼)
for(let i = 0 ; i < searchDropdownList.length; i++){
    searchDropdownList[i].addEventListener("click",()=>{
       searchCategoryValue.setAttribute("name",
           searchDropdownList[i].getAttribute("data-keyword-name"));
       searchCategoryValue.setAttribute("value",searchKeyWordInput.value);
    });
}

function searchKeyWord(){
    articleSearchForm.submit();
}



//radio버튼 검색 조건


//SearchResponse때 할것들
//1. 검색창에 value넣기
//2. dropdown버튼 값 넣어주기
//3. radtio버튼 checked해주기



