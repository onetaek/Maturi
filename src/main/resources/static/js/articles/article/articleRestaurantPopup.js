//팝업 클릭, 닫기 버튼
function restaurantMapPopupToggle(){
    console.log("지도로 보기 클릭!");
    let blur = document.querySelector('#wrap');
    blur.classList.toggle('active');
    let popup = document.getElementById('restaurantMapPopup');
    popup.classList.toggle('active');
    if (isModal === 0){
        scrollStop();
    }else{
        scrollPlay();
    }
}