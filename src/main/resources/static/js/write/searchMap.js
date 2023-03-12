const goWriteBtn = document.querySelector(".goWriteBtn");

// 카테고리 유효성 검사
goWriteBtn.addEventListener("click", ()=>{
  if(rInfo_category.includes("음식점 >")){ // 카테고리에 음식점 포함이면!
    // submit!!
  }
  else { // 포함 x
    alert("음식점으로 등록된 가게만 선택 가능합니다. 카테고리를 다시 확인해주세요.");
  }
});