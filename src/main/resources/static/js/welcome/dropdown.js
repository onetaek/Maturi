let dropdownBtn = document.getElementById("drop-text");
let list = document.getElementById("list");
let icon = document.getElementById("icon");
let searchCond = document.getElementById("search-cond");
let searchInput = document.querySelector(".search-input");
let listItems = document.querySelectorAll(".dropdown-list-item");

//show dropdown list on click on dropdown btn
dropdownBtn.onclick = function(){
    if(list.classList.contains('show')){
        icon.style.rotate="0deg"
    }else{
        icon.style.rotate="180deg"
    }
    list.classList.toggle("show");

};

//바깥을 클릭했을 때 dropdown 올리기
window.onclick = function(e){
    if(
        e.target.id != "drop-text" &&
        e.target.id != "span" &&
        e.target.id != "icon"
    ){
        list.classList.remove("show");

        icon.style.rotate="0deg"
    }
}

for(item of listItems){
    item.onclick=function(e){
        searchCond.innerText = e.target.innerText;
        if( e.target.innerText == "전체"){
            searchInput.placeholder = "아무 조건으로 검색하기...";
        }else{
            searchInput.placeholder = e.target.innerText+ " 조건으로 검색하기...";
        }

    }
}