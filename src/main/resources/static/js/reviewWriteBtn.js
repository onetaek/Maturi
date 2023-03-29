const goWriteBtn = document.querySelector(".goWriteBtn");
const restaurantInfoForm = document.restaurantInfoForm;

// 카테고리 유효성 검사
goWriteBtn.addEventListener("click", ()=>{
  console.log("글작성하기 버튼 클릭!");
  console.log("category = ",rInfo_category);
  rInfo_category.trim();


  if(rInfo_category.includes("음식점 >")){ // 카테고리에 음식점 포함이면!
    // submit!!
    window.location.href = "/articles/new?name="+rInfo_name+
        "&category="+rInfo_category+
        "&oldAddress="+rInfo_oldAddress+
        "&address="+rInfo_address+
        "&latitude="+rInfo_latPosition+
        "&longitude="+rInfo_lngPosition;


    // 글쓰기 : article
    // 글하나 보기 : article/1 get
    // 글삭제 : article/1 delete
    // 글수정 : article/1 patch
    // 글전체보기 : /articles
  }
  else { // 포함 x
    alert("음식점으로 등록된 가게만 선택 가능합니다. 카테고리를 다시 확인해주세요.");
  }
});