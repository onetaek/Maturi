let searchKeyWordInput = document.querySelector(".search-input");//검색 keyword input
let searchCategoryValue = document.querySelector(".search-category-value");//hidden input
let searchDropdownList = document.querySelectorAll(".dropdown-list-item");//dropdown li들
let articleSearchForm = document.articleSearchForm;

// keyword검색 조건(dropdown버튼)
for(let i = 0 ; i < searchDropdownList.length; i++){
    searchDropdownList[i].addEventListener("click",()=>{

        console.log("li요소",searchDropdownList[i]);
        console.log('name에 넣을 값',searchDropdownList[i].getAttribute("data-keyword-name"));
        console.log('value에 넣을 값',searchKeyWordInput.value);

       searchCategoryValue.setAttribute("name",
           searchDropdownList[i].getAttribute("data-keyword-name"));
       searchCategoryValue.setAttribute("value",searchKeyWordInput.value);
        console.log("제발");
    });
}

console.log("??",document.querySelector('#main-search-btn'));

document.querySelector('#main-search-btn').addEventListener('click',()=>{
    console.log("검색 아이콘 클릭!");
    console.log("name",searchCategoryValue.name);

    console.log("value",searchCategoryValue.value);
    if($('#all').is(':checked')){
        $('input[name="latitude"]').removeAttr("value");
        $('input[name="longitude"]').removeAttr("value");
        $('input[name="category"]').removeAttr("value");
    }else if($('#local').is(':checked')){
        $('input[name="category"]').removeAttr("value");
    }else if($('#category').is(':checked')){
        $('input[name="latitude"]').removeAttr("value");
        $('input[name="longitude"]').removeAttr("value");
    }

    articleSearchForm.action = "/articleList";
    articleSearchForm.method = "GET";
    articleSearchForm.submit();
});
