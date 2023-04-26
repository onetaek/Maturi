let myLatitude;
let myLongitude;

//위도 경도 값 가져오기
function onGeoOk(position){
    myLatitude = position.coords.latitude; // 위도
    myLongitude = position.coords.longitude; // 경도
    console.log({"latitude":myLatitude,"longitude":myLongitude});
    document.querySelector("input[name=latitude]").value = myLatitude;
    document.querySelector("input[name=longitude]").value = myLongitude;console.log()
}
function onGeoError(){
    // Swal.fire({
    //     title: "현재위치를 찾을 수 없습니다.",
    //     icon: 'error',
    //     confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
    // })
    console.log("현재위치를 찾을 수 없습니다.");
}
navigator.geolocation.getCurrentPosition(onGeoOk,onGeoError);
