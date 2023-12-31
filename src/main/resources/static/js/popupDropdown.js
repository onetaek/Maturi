//sido관련 변수
let sido_popup_dropdownBtn = document.querySelector("#sido-popup-drop-text");
let sido_popup_list = document.getElementById("sido-popup-list");
let sido_popup_icon = document.getElementById("sido-popup-icon");
let sido_popup_searchCond = document.getElementById("sido-popup-search-cond");
let sido_popup_dropdown_listItem = document.querySelectorAll("#sido-popup-list>.popup-dropdown-list-item")


// sigoon관련 변수
let sigoon_popup_dropdownBtn = document.getElementById("sigoon-popup-drop-text");
let sigoon_popup_list = document.getElementById("sigoon-popup-list");
let sigoon_popup_icon = document.getElementById("sigoon-popup-icon");
let sigoon_popup_searchCond = document.getElementById("sigoon-popup-search-cond");
let sigoon_popup_dropdown_listItem = document.querySelectorAll("#sigoon-popup-list>.popup-dropdown-list-item")

// dong관련 변수
let dong_popup_dropdownBtn = document.getElementById("dong-popup-drop-text");
let dong_popup_list = document.getElementById("dong-popup-list");
let dong_popup_icon = document.getElementById("dong-popup-icon");
let dong_popup_searchCond = document.getElementById("dong-popup-search-cond");
let dong_popup_dropdown_listItem = document.querySelectorAll("#dong-popup-list>.popup-dropdown-list-item")


/**
 * sido관련 이벤트 처리
 */
sido_popup_dropdownBtn.addEventListener("click",function(){
    if(sido_popup_list.classList.contains('show')){
        sido_popup_icon.style.rotate="0deg"
    }else{
        sido_popup_icon.style.rotate="180deg"
    }
    sido_popup_list.classList.toggle("show");
});


for(item of sido_popup_dropdown_listItem){
    item.onclick=function(e){
        sido_popup_searchCond.innerText = e.target.innerText;
    }
}

/**
 * sigoon 관련 이벤트 처리
 */
sigoon_popup_dropdownBtn.onclick = function(){
    if(sigoon_popup_list.classList.contains('show')){
        sigoon_popup_icon.style.rotate="0deg"
    }else{
        sigoon_popup_icon.style.rotate="180deg"
    }
    sigoon_popup_list.classList.toggle("show");
};
for(item of sigoon_popup_dropdown_listItem){
    item.onclick=function(e){
        sigoon_popup_searchCond.innerText = e.target.innerText;
    }
}

/**
 * dong과련 이벤트 처리
 */
dong_popup_dropdownBtn.onclick = function(){
    if(dong_popup_list.classList.contains('show')){
        dong_popup_icon.style.rotate="0deg"
    }else{
        dong_popup_icon.style.rotate="180deg"
    }
    dong_popup_list.classList.toggle("show");
};
for(item of dong_popup_dropdown_listItem){
    item.onclick=function(e){
        dong_popup_searchCond.innerText = e.target.innerText;
    }
}

//바깥을 클릭했을 때 dropdown 올리기
window.onclick = function(e){
    if(
        e.target.id != "sido-popup-drop-text" &&
        e.target.id != "sido-popup-icon" &&
        e.target.id != "sido-popup-search-cond" &&
        e.target.id != "sigoon-popup-drop-text" &&
        e.target.id != "sigoon-popup-icon" &&
        e.target.id != "sigoon-popup-search-cond" &&
        e.target.id != "dong-popup-drop-text" &&
        e.target.id != "dong-popup-icon" &&
        e.target.id != "dong-popup-search-cond"
    ){
        sido_popup_list.classList.remove("show");
        sido_popup_icon.style.rotate="0deg"
        sigoon_popup_list.classList.remove("show");
        sigoon_popup_icon.style.rotate="0deg"
        dong_popup_list.classList.remove("show");
        dong_popup_icon.style.rotate="0deg"
    }
}
