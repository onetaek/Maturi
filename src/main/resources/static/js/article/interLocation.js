let interLocationBtn = document.querySelector('.inter-location-btn');
console.log("inter",interLocationBtn);

interLocationBtn.addEventListener('click',()=>{
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

            fetch("/api/member/area",{
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
                    alert(`${sidoText}${checkSigoonText}${checkDongText}을(를) 관심지역으로 성공적으로 등록 하였습니다`);
                    $("#local").prop("checked",true);
                    popupToggle();
                }else{
                    alert(`관심지역으로 등록하는데 실패했습니다`);
                    $("#all").prop("checked",true);
                    popupToggle();
                }
                selectInterLocation();
            });
        }
    }
});

selectInterLocation();
function selectInterLocation(){
    fetch("/api/member/area")
    .then((response) => response.json())
    .then((data) => {
        console.log(data);
        let interLocationRadioBtn = document.querySelector('label[for=local]');
        const sido = data['sido'] === null ? "" : data['sido'];
        const sigoon = data['sigoon'] === null ? "" : `>${data['sigoon']}` ;
        const dong = data['dong'] === null ? "" : `>${data['dong']}` ;

        if(sido === "" && sigoon ==="" && dong ===""){
            interLocationRadioBtn.innerText = `관심지역`;
        }else{
            interLocationRadioBtn.innerText = `${sido}${sigoon}${dong}`;
        }
    })

}