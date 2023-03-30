let searchKeyWordInput = document.querySelector(".search-input");//검색 keyword input
let searchCategoryValue = document.querySelector(".search-category-value");//hidden input
let searchDropdownList = document.querySelectorAll(".dropdown-list-item");//dropdown li들
let hasArticle = true;

// keyword검색 조건(dropdown버튼)
for(let i = 0 ; i < searchDropdownList.length; i++){
    searchDropdownList[i].addEventListener("click",()=>{
       searchCategoryValue.setAttribute("name",
           searchDropdownList[i].dataset.name);
       document.querySelector('input[name="keywordName"]').value
        =searchDropdownList[i].dataset.name;
    });
}

function searchCondSetting(event){
    console.log("무슨 동작이니?",event);

    if($('#category').is(':checked')){//카테고리가 선택되었다면
        document.querySelector('input[name="category"]')
            .setAttribute("value",
                document.querySelector('label[for="category"]').innerText );
    }
    if(!$('#all').is(':checked')&&
        !$('#follow').is(':checked')&&
        !$('#local').is(':checked')&&
        !$('#my-local').is(':checked')&&
        !$('#like').is(':checked')&&
        !$('#category').is(':checked')){//혹시 아무것도 체크가 안되어있다면
        $('input[name="latitude"]').removeAttr("value");
        $('input[name="longitude"]').removeAttr("value");
        alert("아무 조건도 체크되어있지 않습니다.")
        $("#all").prop("checked",true);
        return false;
    }
    //동적 hidden input에 value속성에 값추가
    searchCategoryValue.setAttribute("value",searchKeyWordInput.value);

    //------search조건에 맞는 json데이터 만드는 과정--------
    let radioCond = "everything";
    if($('#all').is(':checked')){//전체 선택
        radioCond = "everything"
    }else if($('#follow').is(':checked')){//팔로우 선택
        radioCond = "follow"
    }else if($('#local').is(':checked')){//관심지역 선택
        radioCond = "interLocal"
    }else if($('#my-local').is(':checked')){//현재위치 선택
        radioCond = "myLocal"
    }else if($('#category').is(':checked')){//카테고리 선택
        radioCond = "category"
    }else if($('#like').is(':checked')){//좋아요 선택
        radioCond = "like"
    }

    console.log("현재위치가 체크되었니?",$('#my-local').is(":checked"));
    let latitude = $('#my-local').is(':checked') ? $('input[name="latitude"]').val() : null;
    let longitude = $('#my-local').is(':checked') ? $('input[name="longitude"]').val() : null;
    console.log("위도",latitude,"경도",longitude);
    let category = $('#category').is(':checked') ? $('input[name="category"]').val() : null;

    let content = searchCategoryValue.name === "content" ? searchKeyWordInput.value : null;
    let writer = searchCategoryValue.name === "writer" ? searchKeyWordInput.value : null;
    let tag = searchCategoryValue.name === "tag" ? searchKeyWordInput.value : null;
    let restaurantName = searchCategoryValue.name === "restaurantName" ? searchKeyWordInput.value : null;
    let all = searchCategoryValue.name === "all" ? searchKeyWordInput.value : null;

    if($('#local').is(':checked')){//관심지역이 선택이 되지 않았다면
        console.log("관심지역이 선택되었으므로 위도 경도를 null로 변경");
        latitude = null;
        longitude = null;
    }

    let lastArticleInput = document.querySelector('input[name="lastArticleId"]');
    let lastArticle;
    if(lastArticleInput.value === "" || lastArticleInput.value === null){
        lastArticle == null;
    }else{
        lastArticle = lastArticleInput.value;
    }
    if(event === "click" || event ==='load'){
        lastArticle = null;
    }
    let obj = {
        'radioCond': radioCond,//검색에 필요한 값
        'latitude': latitude,//검색에 필요한 값
        'longitude': longitude,//검색에 필요한 값
        'category': category,//검색에 필요한 값
        'content': content,//검색에 필요한 값
        'writer': writer,//검색에 필요한 값
        'tag': tag,//검색에 필요한 값
        'restaurantName': restaurantName,//검색에 필요한 값
        'all': all,//검색에 필요한 값
        'keyword': searchKeyWordInput.value,//검색에 필요한 값
        'lastArticleId': lastArticle,//페이징에 필요한 값
        'size': 5, // 페이징에 필요한 값
        'event' : event//페이징에 필요한 값
    };
    console.log("javascript에서 setting한 검색, 페이징 조건",obj);
    return obj;
}

