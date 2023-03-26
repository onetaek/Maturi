//팝업 클릭, 닫기 버튼
function followPopupToggle(){
    console.log("follow/following 클릭!");
    let blur = document.querySelector('#wrap');
    blur.classList.toggle('active');
    let popup = document.getElementById('followPopup');
    popup.classList.toggle('active');
    if (isModal === 0){
        scrollStop();
    }else{
        scrollPlay();
    }
}
//팔로우 팔로워 버튼 horizontalUnderLine 이벤트
let horizontalUnderLine = document.querySelector("#horizontal-under-line");
let followMenu = document.querySelectorAll(".follow-container");
followMenu.forEach((menu) =>
    menu.addEventListener("click",(e) =>
    horizontalIndicator(e))
);

function horizontalIndicator(e){
    horizontalUnderLine.style.left = e.currentTarget.offsetLeft + "px";
    horizontalUnderLine.style.width = e.currentTarget.offsetWidth + "px";
    horizontalUnderLine.style.top =
        e.currentTarget.offsetTop + e.currentTarget.offsetHeight - 4 + "px";
}

//