console.log("popup.js 파일 실행")
let isModal = 0;
function popupToggle(){
    console.log("popup 버튼 클릭!");
    let blur = document.querySelector('#wrap');
    blur.classList.toggle('active');
    let popup = document.getElementById('popup');
    popup.classList.toggle('active');
    if (isModal === 0){
        scrollStop();
    }else{
        scrollPlay();
    }
}
function scrollStop(){
    document.querySelector('body').style.height='100vh';
    document.querySelector('body').style.overflow='hidden';
    isModal=1;
}
function scrollPlay(){
    document.querySelector('body').style.height='inherit';
    document.querySelector('body').style.overflow='inherit';
    isModal=0;
}