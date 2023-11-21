///////////////////////////////////////// 주소 select 관련 스크립트 //////////////////////////////////
$.support.cors = true;
const sido = document.getElementById("sido_code");
const sigoon = document.getElementById("sigoon_code");
const dong = document.getElementById("dong_code");

//시도 관련변수
let sido_popup_dropdownBtn;
let sido_popup_list;
let sido_popup_icon;
let sido_popup_searchCond;
let sido_popup_dropdown_listItem;

//시군 관련 변수
let sigoon_popup_dropdownBtn;
let sigoon_popup_list;
let sigoon_popup_icon;
let sigoon_popup_searchCond;
let sigoon_popup_dropdown_listItem;

//동 관련 변수
let dong_popup_dropdownBtn;
let dong_popup_list;
let dong_popup_icon;
let dong_popup_searchCond;
let dong_popup_dropdown_listItem;

let sidoName = "";
let sigoonName = "";
let dongName = "";

//드롭다운 되서나오는 시,도,군이 너무 많을 때를 대비한 변수들
let mapPopupContent = document.querySelector('#popupContent');
let maxDropdownHeight = mapPopupContent.clientHeight - 35 -10;//select버튼의 높이 - padding값
let popup_list_count;

$(function(){
  $.ajax({
    type: "get",
    url: "https://api.vworld.kr/req/data?key=CEB52025-E065-364C-9DBA-44880E3B02B8&domain=http://localhost:8080&service=data&version=2.0&request=getfeature&format=json&size=1000&page=1&geometry=false&attribute=true&crs=EPSG:3857&geomfilter=BOX(13663271.680031825,3894007.9689600193,14817776.555251127,4688953.0631258525)&data=LT_C_ADSIDO_INFO",
    async: false,
    dataType: 'jsonp',
    success: function(data) {
      let html =  `<div id="sido-popup-drop-text" class="popup-dropdown-text">`
          +         `<span id="sido-popup-search-cond">전체</span>`
          +         `<i id="sido-popup-icon" class="fa-solid fa-chevron-down"></i>`
          +       `</div>`
          +       `<ul id="sido-popup-list" class="popup-dropdown-list">`;

      data.response.result.featureCollection.features.forEach(function(f){
        let 행정구역코드 = f.properties.ctprvn_cd;
        let 행정구역명 = f.properties.ctp_kor_nm;
        html += `<li class="popup-dropdown-list-item" data-location="${행정구역코드}">${행정구역명}</li>`;
      })
      html += `</ul>`;
      $('#sido_code').html(html);
      //전체 버튼 추가
      $('#sido-popup-list').prepend('<li class="popup-dropdown-list-item" data-location="all">전체</li>')

      //sido관련 변수
      sido_popup_dropdownBtn = document.getElementById("sido-popup-drop-text");
      sido_popup_list = document.getElementById("sido-popup-list");
      sido_popup_icon = document.getElementById("sido-popup-icon");
      sido_popup_searchCond = document.getElementById("sido-popup-search-cond");
      sido_popup_dropdown_listItem = document.querySelectorAll("#sido-popup-list>.popup-dropdown-list-item")

      /**
       * sido관련 이벤트 처리
       */
      sido_popup_dropdownBtn.onclick = function(){
        console.log("sido_popup_dropdownBtn 클릭!");
        sigoonName = "";
        dongName = "";
        if(sido_popup_list.classList.contains('show')){
          sido_popup_icon.style.rotate="0deg"
          $('#sido-popup-list').animate({height:`0px`},500);
        }else{
          sido_popup_icon.style.rotate="180deg"
          popup_list_count = sido_popup_dropdown_listItem.length / 2;
          console.log(popup_list_count);
          popup_list_count = Number.isInteger(popup_list_count) ? popup_list_count : popup_list_count + 0.5;
          $('#sido-popup-list').animate({height:`${sido_popup_dropdown_listItem[0].clientHeight * popup_list_count + 20 }px`},500);
        }
        sido_popup_list.classList.toggle("show");

      };



      for(item of sido_popup_dropdown_listItem){

        item.onclick=function(e){
          console.log("시도 선택");
          sido_popup_searchCond.innerText = e.target.innerText;
          sido_popup_list.classList.toggle("show");
          console.log("시도 코드 선택!");
          let thisVal = $(this).data('location');//data-location속성의 값을 가져옴
          // 주소값 얻는 코드 추가
          const sidoOpt = sido.querySelectorAll("#sido-popup-list > .popup-dropdown-list-item");
          sidoOpt.forEach(element =>{
            let optValue = element.getAttribute("data-location");
            if(optValue != null && optValue == "all"){//전체를 선택했을 경우 입력 값 없음 + 시군,동 요소 없에고 전체로 변경
              sidoName = "";
              console.log(sidoName + " " + sigoonName + " " + dongName);
              let sigoonItems = document.querySelectorAll('#sigoon-popup-list > .popup-dropdown-list-item');
              let goonItems = document.querySelectorAll('#dong-popup-list > .popup-dropdown-list-item');
              if(sigoonItems.length > 0){
                document.querySelector('#sigoon-popup-list').remove();
                document.querySelector('#sigoon-popup-drop-text').innerText = "전체";
              }
              if(goonItems.length > 0){
                document.querySelector('#dong-popup-list').remove();
                document.querySelector('#dong-popup-drop-text').innerText = "전체";
              }
              return false;
            }else if(optValue != null && optValue == thisVal) {
              sidoName = element.innerText;
              console.log(sidoName + " " + sigoonName + " " + dongName);
            }
          })
          $('#sido-popup-list').animate({height:`0px`},500);

          let html =`<div id="dong-popup-drop-text" class="popup-dropdown-text">`;
          html +=      `<span id="dong-popup-search-cond">전체</span>`;
          html +=      `<i id="dong-popup-icon" class="fa-solid fa-chevron-down"></i>`;
          html +=   `</div>`;
          $('#dong_code').html(html);

          console.log();
          $.ajax({
            type: "get",
            url: "https://api.vworld.kr/req/data?key=CEB52025-E065-364C-9DBA-44880E3B02B8&domain=http://localhost:8080&service=data&version=2.0&request=getfeature&format=json&size=1000&page=1&geometry=false&attribute=true&crs=EPSG:3857&geomfilter=BOX(13663271.680031825,3894007.9689600193,14817776.555251127,4688953.0631258525)&data=LT_C_ADSIGG_INFO",
            data : {attrfilter : 'sig_cd:like:'+thisVal},
            async: false,
            dataType: 'jsonp',
            success: function(data) {
              let html =`<div id="sigoon-popup-drop-text" class="popup-dropdown-text">`;
              html +=      `<span id="sigoon-popup-search-cond">전체</span>`;
              html +=      `<i id="sigoon-popup-icon" class="fa-solid fa-chevron-down"></i>`;
              html +=   `</div>`;
              html +=   `<ul id="sigoon-popup-list" class="popup-dropdown-list">`;

              data.response.result.featureCollection.features.forEach(function(f){
                let 행정구역코드 = f.properties.sig_cd;
                let 행정구역명 = f.properties.sig_kor_nm;
                // html +=`<option value="${행정구역코드}">${행정구역명}(${행정구역코드})</option>`
                html += `<li class="popup-dropdown-list-item" data-location="${행정구역코드}">${행정구역명}</li>`;
              })
              html += `</ul>`;
              $('#sigoon_code').html(html);
              //전체 버튼 추가
              $('#sigoon-popup-list').prepend('<li class="popup-dropdown-list-item" data-location="all">전체</li>')

              //sigoon관련 변수
              sigoon_popup_dropdownBtn = document.getElementById("sigoon-popup-drop-text");
              sigoon_popup_list = document.getElementById("sigoon-popup-list");
              sigoon_popup_icon = document.getElementById("sigoon-popup-icon");
              sigoon_popup_searchCond = document.getElementById("sigoon-popup-search-cond");
              sigoon_popup_dropdown_listItem = document.querySelectorAll("#sigoon-popup-list>.popup-dropdown-list-item");

              /**
               * sigoon css처이(넘칠경우 overflow scroll처리)
               */
              popup_list_count = sigoon_popup_dropdown_listItem.length / 2;
              if((sigoon_popup_dropdown_listItem[0].clientHeight * popup_list_count + 20) > maxDropdownHeight){
                sigoon_popup_list.style.overflowY = "scroll";
              }

              /**
               * sigoon 관련 이벤트 처리
               */
              sigoon_popup_dropdownBtn.onclick = function(){
                dongName = "";
                if(sigoon_popup_list.classList.contains('show')){
                  sigoon_popup_icon.style.rotate="0deg"
                  $('#sigoon-popup-list').animate({height:`0px`},500);
                }else{
                  sigoon_popup_icon.style.rotate="180deg"
                  popup_list_count = sigoon_popup_dropdown_listItem.length / 2;
                  popup_list_count = Number.isInteger(popup_list_count) ? popup_list_count : popup_list_count + 0.5;
                  if((sigoon_popup_dropdown_listItem[0].clientHeight * popup_list_count + 20) > maxDropdownHeight){
                    console.log("동 넘었다");
                    dong_popup_list.style.overflowY = "scroll";
                    $('#sigoon-popup-list').animate({height:`${maxDropdownHeight + 20}px`},500);
                  }else{
                    $('#sigoon-popup-list').animate({height:`${sigoon_popup_dropdown_listItem[0].clientHeight * popup_list_count + 20 }px`},500);

                  }
                }
                sigoon_popup_list.classList.toggle("show");
              };

              for(item of sigoon_popup_dropdown_listItem){

                // 시군 코드 선택
                item.onclick=function(e){
                  console.log("시군 코드선택!");
                  sigoon_popup_searchCond.innerText = e.target.innerText;
                  sigoon_popup_list.classList.toggle("show");

                  let thisVal = $(this).data('location');//data-location속성의 값을 가져옴
                  // 주소값 얻는 코드 추가
                  const sigoonOpt = sigoon.querySelectorAll("#sigoon-popup-list > .popup-dropdown-list-item");
                  sigoonOpt.forEach(element =>{
                    let optValue = element.getAttribute("data-location");
                    if(optValue != null && optValue == "all"){
                      sigoonName = "";
                      console.log(sidoName + " " + sigoonName + " " + dongName);
                      let goonItems = document.querySelectorAll('#dong-popup-list > .popup-dropdown-list-item');
                      if(goonItems.length > 0){
                        document.querySelector('#dong-popup-list').remove();
                        document.querySelector('#dong-popup-drop-text').innerText = "전체";
                      }
                      return false;
                    }else if(optValue != null && optValue == thisVal) {
                      sigoonName = element.innerText;
                      console.log(sidoName + " " + sigoonName + " " + dongName);
                    }
                  })
                  $('#sigoon-popup-list').animate({height:`0px`},500);
                  $.ajax({
                    type: "get",
                    url: "https://api.vworld.kr/req/data?key=CEB52025-E065-364C-9DBA-44880E3B02B8&domain=http://localhost:8080&service=data&version=2.0&request=getfeature&format=json&size=1000&page=1&geometry=false&attribute=true&crs=EPSG:3857&geomfilter=BOX(13663271.680031825,3894007.9689600193,14817776.555251127,4688953.0631258525)&data=LT_C_ADEMD_INFO",
                    data : {attrfilter : 'emd_cd:like:'+thisVal},
                    async: false,
                    dataType: 'jsonp',
                    success: function(data) {
                      let html =`<div id="dong-popup-drop-text" class="popup-dropdown-text">`;
                      html +=      `<span id="dong-popup-search-cond">전체</span>`;
                      html +=      `<i id="dong-popup-icon" class="fa-solid fa-chevron-down"></i>`;
                      html +=   `</div>`;
                      html +=   `<ul id="dong-popup-list" class="popup-dropdown-list">`;

                      data.response.result.featureCollection.features.forEach(function(f){
                        let 행정구역코드 = f.properties.emd_cd;
                        let 행정구역명 = f.properties.emd_kor_nm;
                        // html +=`<option value="${행정구역코드}">${행정구역명}(${행정구역코드})</option>`
                        html += `<li class="popup-dropdown-list-item" data-location="${행정구역코드}">${행정구역명}</li>`;
                      })
                      html += `</ul>`;
                      $('#dong_code').html(html);
                      //전체 버튼 추가
                      $('#dong-popup-list').prepend('<li class="popup-dropdown-list-item" data-location="all">전체</li>')

                      //dong관련 변수
                      dong_popup_dropdownBtn = document.getElementById("dong-popup-drop-text");
                      dong_popup_list = document.getElementById("dong-popup-list");
                      dong_popup_icon = document.getElementById("dong-popup-icon");
                      dong_popup_searchCond = document.getElementById("dong-popup-search-cond");
                      dong_popup_dropdown_listItem = document.querySelectorAll("#dong-popup-list>.popup-dropdown-list-item")


                      /**
                       * dong css처리(넘칠경우 overflow scroll처리)
                       */
                      // let dongHeight;

                      console.log("동의 높이",(dong_popup_dropdown_listItem[0].clientHeight * popup_list_count + 20));
                      console.log("최대치 높이",maxDropdownHeight);
                      /**
                       * dong과련 이벤트 처리
                       */
                      dong_popup_dropdownBtn.onclick = function(){
                        if(dong_popup_list.classList.contains('show')){
                          dong_popup_icon.style.rotate="0deg"
                          $('#dong-popup-list').animate({height:`0px`},500);
                        }else{
                          dong_popup_icon.style.rotate="180deg"
                          popup_list_count = dong_popup_dropdown_listItem.length / 2;
                          popup_list_count = Number.isInteger(popup_list_count) ? popup_list_count : popup_list_count + 0.5;
                          if((dong_popup_dropdown_listItem[0].clientHeight * popup_list_count + 20) > maxDropdownHeight){
                            console.log("동 넘었다");
                            dong_popup_list.style.overflowY = "scroll";
                            $('#dong-popup-list').animate({height:`${maxDropdownHeight + 20}px`},500);
                          }else{
                            $('#dong-popup-list').animate({height:`${dong_popup_dropdown_listItem[0].clientHeight * popup_list_count + 20}px`},500);
                          }
                        }
                        dong_popup_list.classList.toggle("show");
                      };
                      for(item of dong_popup_dropdown_listItem){

                        // 동 코드 선택
                        item.onclick=function(e){
                          dong_popup_searchCond.innerText = e.target.innerText;
                          dong_popup_list.classList.toggle("show");
                          let thisVal = $(this).data('location');//data-location속성의 값을 가져옴
                          // 주소값 얻는 코드 추가
                          const dongOpt = dong.querySelectorAll("#dong-popup-list > .popup-dropdown-list-item");
                          dongOpt.forEach(element =>{
                            let optValue = element.getAttribute("data-location");
                            if(optValue != null && optValue == "all"){
                              dongName = "";
                              console.log(sidoName + " " + sigoonName + " " + dongName);
                            }else if(optValue != null && optValue == thisVal) {
                              dongName = element.innerText;
                              console.log(sidoName + " " + sigoonName + " " + dongName);
                            }
                          })
                          $('#dong-popup-list').animate({height:`0px`},500);
                        }
                      }
                    },
                    error: function(xhr, stat, err) {}
                  });
                }
              }
            },
            error: function(xhr, stat, err) {}
          });
        }
      }
    },
    error: function(xhr, stat, err) {}
  });




//바깥을 클릭했을 때 dropdown 올리기
  window.onclick = function(e){
    // console.log("클릭한 대상의 class명",e.target.className);
    if(
        e.target.id != "sido-popup-drop-text" &&
        e.target.id != "sido-popup-icon" &&
        e.target.id != "sido-popup-search-cond" &&
        e.target.id != "sigoon-popup-drop-text" &&
        e.target.id != "sigoon-popup-icon" &&
        e.target.id != "sigoon-popup-search-cond" &&
        e.target.id != "dong-popup-drop-text" &&
        e.target.id != "dong-popup-icon" &&
        e.target.id != "dong-popup-search-cond"&&
        e.target.id != "drop-text" &&
        e.target.id != "search-cond" &&
        e.target.id != "icon"

    ){
      if(document.getElementById("list")!= null){
        document.getElementById("list").classList.remove("show");
      }
      if(document.getElementById("icon")!= null){
        document.getElementById("icon").style.rotate="0deg"
      }
      sido_popup_list.classList.remove("show");
      sido_popup_icon.style.rotate="0deg"
      if(sigoon_popup_list != null) sigoon_popup_list.classList.remove("show");
      if(sigoon_popup_icon != null){
        sigoon_popup_icon.style.rotate="0deg"
      }
      if(dong_popup_list != null) dong_popup_list.classList.remove("show");
      if(dong_popup_icon!=null){
        dong_popup_icon.style.rotate="0deg"
      }
      $('#sido-popup-list').animate({height:`0px`},500);
      $('#sigoon-popup-list').animate({height:`0px`},500);
      $('#dong-popup-list').animate({height:`0px`},500);


    }
  }
})