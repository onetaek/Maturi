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
           searchDropdownList[i].dataset.name);
       document.querySelector('input[name="keywordName"]').value
        =searchDropdownList[i].dataset.name;
    });
}

console.log("??",document.querySelector('#main-search-btn'));

document.querySelector('#main-search-btn').addEventListener('click',()=>{
    console.log("검색 아이콘 클릭!");
    if(!$('#my-local').is(':checked')){//관심지역이 선택이 되지 않았다면
        $('input[name="latitude"]').removeAttr("value");
        $('input[name="longitude"]').removeAttr("value");
    }
    if($('#category').is(':checked')){//카테고리가 선택되었다면
        document.querySelector('input[name="category"]')
            .setAttribute("value",
                document.querySelector('label[for="category"]').innerText );
    }
    if(!$('#all').is(':checked')&&
        !$('#follow').is(':checked')&&
        !$('#local').is(':checked')&&
        !$('#my-local').is(':checked')&&
        !$('#category').is(':checked')){//혹시 아무것도 체크가 안되어있다면
        $('input[name="latitude"]').removeAttr("value");
        $('input[name="longitude"]').removeAttr("value");
    }
    //동적 hidden input에 value속성에 값추가
    searchCategoryValue.setAttribute("value",searchKeyWordInput.value);
    articleSearchForm.action = "/articleList";
    articleSearchForm.method = "GET";

    console.log("name",searchCategoryValue.name);
    console.log("value",searchCategoryValue.value);

    articleSearchForm.submit();
});
