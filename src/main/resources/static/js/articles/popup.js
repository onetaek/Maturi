let isModal = 0;
let isInterBtnClick = false;

function popupToggle(){

    let blur = document.querySelector('#wrap');
    blur.classList.toggle('active');
    let popup = document.getElementById('popup');
    popup.classList.toggle('active');
    if (isModal === 0){
        scrollStop();
    }else{
        scrollPlay();
    }

    if(isInterBtnClick){
        $("#all").prop("checked",true);
        isInterBtnClick = false;
    }
}
// document.querySelector('.popup-close-btn-wrap').addEventListener('click',()=>{
//     isInterBtnClick = true;
//     $("#all").prop("checked",true);
// });

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
