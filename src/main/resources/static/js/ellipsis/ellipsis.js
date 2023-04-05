function ellipsisToggle(obj){
    console.log("ellipsis 클릭");
    let ellipsisWrap = obj.closest('.ellipsis-btn-wrap');
    let ellipsisContent = ellipsisWrap.getElementsByClassName('ellipsis-content')[0];
    console.log("ul",ellipsisContent);
    let listCount = ellipsisContent.childElementCount;
    if(ellipsisContent.classList.contains('active')){
        ellipsisContent.style.height="0px";
        ellipsisContent.style.border="0";
    }else{
        ellipsisContent.style.height=`${50 * listCount + 10}px`;
        ellipsisContent.style.border="1px solid var(--ellipsis-color)";
    }
    ellipsisContent.classList.toggle('active');
}