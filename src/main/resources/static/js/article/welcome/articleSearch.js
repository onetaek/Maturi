let articleSearchForm = document.articleSearchForm;
// let searchRadioBtn = document.querySelectorAll(".input-container > input");
// let searchClickBtn = document.querySelector(".search-button");
// let searchDropDownCond = document.querySelector("#search-cond");

let searchCategoryValue = document.querySelector(".search-category-value");
let searchDropdownList = document.querySelectorAll(".dropdown-list-item");


searchCategoryValue.setAttribute("name","all");
searchCategoryValue.setAttribute("value","");


// for(let i = 0 ; searchRadioBtn.length ; i++ ){
//     searchRadioBtn[i].addEventListener("click",(e)=>{
//         console.log("검색조건 클릭!");
//         let input1 = document.createElement('input');
//         input1.setAttribute("type","hidden");
//         input1.setAttribute("name",searchRadioBtn[i].value);
//         input1.setAttribute("value","true");
//         articleSearchForm.appendChild(input1);
//         // articleSearchForm.submit();
//     });
// }
// function searchKeyWord(){
//     console.log("클릭!");
//     let input1 = document.createElement('input');
//     input1.setAttribute("name",searchDropDownCond.dataset.keywordName);
//     let searchKeyWord = document.querySelector(".search-input");
//     input1.setAttribute("value",searchKeyWord.value);
//     console.log("name",searchDropDownCond.dataset.keywordName);
//     console.log("value",searchKeyWord.value);
//     articleSearchForm.appendChild(input1);
//     // articleSearchForm.submit();
// }

//SearchResponse때 할것들
//1. 검색창에 value넣기
//2. dropdown버튼 값 넣어주기
//3. radtio버튼 checked해주기



