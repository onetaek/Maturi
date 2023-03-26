let interLocationRegisterBtn = document.querySelector('.inter-location-btn');
let interLocationRadioBtn = document.querySelector('#local');

interLocationRadioBtn.addEventListener('click',()=>{

   let localLabel = document.querySelector('label[for="local"]');
    console.log("관심지역을 눌렀습니다!!!!!!!!!");
    console.log("라벨 안에있는 값이 관심지역입니까?",localLabel.innerText === "관심지역");

    if(localLabel.innerText === "관심지역"){
        if(confirm("관심지역을 등록하시겠습니까?")){
            if($('#local').is(':checked')){
                isInterBtnClick = true;
            }
            popupToggle();
        }else{
            $("#all").prop("checked",true);
        }
    }
});

//관심지역 DB에서 INSERT : 관심지역으로 등록 버튼 클릭
interLocationRegisterBtn.addEventListener('click',()=>{
    let sidoText = document.querySelector('#sido-popup-search-cond').innerText;
    let sigoonText = document.querySelector('#sigoon-popup-search-cond').innerText;
    let dongText = document.querySelector('#dong-popup-search-cond').innerText;
    console.log("관심지역으로 등록 클릭!");
    console.log(sidoText);
    console.log(sigoonText);
    console.log(dongText);
    if(sidoText === "전체"){
        alert("관심지역을 등록하기 위해서는 적어도 시도는 선택하셔야합니다.");
    }else{
        let checkSigoonText = sigoonText === "전체" ? "" : `>${sigoonText}`;
        let checkDongText = dongText === "전체" ? "" : `>${dongText}`;
        if(confirm(`${sidoText}${checkSigoonText}${checkDongText}을(를) 관심지역으로 설정하시겠습니까?`)){

            let sidoValue = sidoText === "전체" ? null : `${sidoText}`;
            let sigoonValue = sigoonText === "전체" ? null : `${sigoonText}`;
            let dongValue = dongText === "전체" ? null : `${dongText}`;

            fetch("/api/members/area",{
                method: "PATCH",
                headers:{
                    "Content-Type":"application/json",
                },
                body:JSON.stringify({
                    sido : sidoValue,
                    sigoon : sigoonValue,
                    dong : dongValue
                }),
            })
            .then((response) => {
                if(response.ok){
                    $("#all").prop("checked",true);//#local로 바꿨는데 안되네ㅠ
                    alert(`${sidoText}${checkSigoonText}${checkDongText}을(를) 관심지역으로 성공적으로 등록 하였습니다`);
                    popupToggle();
                }else{
                    alert(`관심지역으로 등록하는데 실패했습니다`);
                    $("#all").prop("checked",true);
                    popupToggle();
                }
                selectInterLocation();
            });
            isInterBtnClick = false;
        }
    }
});

selectInterLocation();
function addDeleteBtn(){
    document.querySelector('.input-container-interLocal-wrap').innerHTML
        += `<ion-icon class="inter-delete-btn" onclick="deleteInterLocation()" name="close-circle-outline"></ion-icon>`;

}


//관심지역 DB에서 SELECT : 페이지 로드, 수정, 삭제 하고나서
function selectInterLocation(){
    fetch("/api/members/area")
    .then((response) => response.json())
    .then((data) => {
        console.log(data);
        let interLocationRadioBtn = document.querySelector('label[for=local]');
        let radioTitleLocal = document.querySelector(".radio-title-local");
        const sido = isEmpty(data['sido']) ? "" : data['sido'];
        const sigoon = isEmpty(data['sigoon']) ? "" : `>${data['sigoon']}` ;
        const dong = isEmpty(data['dong']) ? "" : `>${data['dong']}` ;

        if(isEmpty(data['sido']) && isEmpty(data['sigoon']) && isEmpty(data['dong'])){
            interLocationRadioBtn.innerText = `관심지역`;
        }else{
            interLocationRadioBtn.innerHTML = `${sido}${sigoon}${dong}`;
            if(document.querySelector('.inter-delete-btn') === null ){
                addDeleteBtn();
            }
        }
    })
}

//관심지역 DB에서 DELETE : 관심지역 옆의 X버튼 눌렀을 경우
function deleteInterLocation(){
    console.log("관심지역 삭제 버튼 클릭");
    if(confirm("관심지역을 삭제 하시겠습니까?")){
        console.log("관심지역 삭제 동의");
        fetch("/api/members/area",{
            method: "DELETE",
        })
        .then((response) => {
            if(response.ok){
                alert("삭제 성공!");
                selectInterLocation();
                let element = document.querySelector('.input-container-interLocal-wrap');
                element.innerHTML =
                    `<div class="input-container-interLocal-wrap">
                        <div class="input-container input-container-interLocal">
                            <input id="local" type="radio" name="radioCond" value="interLocal">
                            <div class="radio-tile radio-title-local">
                                <ion-icon name="compass"></ion-icon>
                                <label for="local">관심지역</label>
                            </div>
                        </div>
                    </div>`;
                //새로운 요소이므로 다시 이벤트를 걸어준다.
                interLocationRadioBtn = document.querySelector('#local');
                interLocationRadioBtn.addEventListener('click',()=>{

                    let localLabel = document.querySelector('label[for="local"]');
                    console.log("관심지역을 눌렀습니다!!!!!!!!!");
                    console.log("라벨 안에있는 값이 관심지역입니까?",localLabel.innerText === "관심지역");

                    if(localLabel.innerText === "관심지역"){
                        if(confirm("관심지역을 등록하시겠습니까?")){
                            isInterBtnClick = true;
                            popupToggle();
                        }else{
                            $("#all").prop("checked",true);
                        }
                    }
                });
                $("#all").prop("checked",true);
            }else{
                alert("삭제 실패ㅠ");
                selectInterLocation();
                $("#local").prop("checked",true);
            }
        });
    }
}


function isEmpty(str){
    if(typeof str == "undefined" || str == null || str == "")
        return true;
    else
        return false ;
}