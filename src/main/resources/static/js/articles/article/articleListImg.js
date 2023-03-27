console.log("articleListImg");

function articleListImgStyle(){
  const bImgUl_list = document.querySelectorAll(".bImg");
  bImgUl_list.forEach(ul => {
    const bImgLis = ul.querySelectorAll("li");
    console.log("size : " + bImgLis.length);
    if(bImgLis.length > 1){ // 이미지 2개 이상
      ul.style.backgroundColor = "unset";
      if(bImgLis.length > 4){ // 이미지 4개 이상
        let temp = `<li class="ionImg"><ion-icon name="add-circle-outline"></ion-icon></li>`;
        ul.insertAdjacentHTML("beforeend", temp);
        let ionImg = ul.querySelector(".ionImg");
        ionImg.style.position = "absolute";
        ionImg.style.bottom = "0";
        ionImg.style.right = "0";
      } else if(bImgLis.length > 2){ // 이미지 3개 이상
        let temp = `<li class="ionImg">상세 보기...</li>`;
        ul.insertAdjacentHTML("beforeend", temp);
      }

      ul.querySelectorAll("li").forEach(bImgLi => {
        bImgLi.style.width = "50%";
        bImgLi.style.height = "50%";
      })
    }
    imgRatio(ul);
  });
}
