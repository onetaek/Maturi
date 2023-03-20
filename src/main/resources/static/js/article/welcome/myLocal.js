let myLatitude;
let mylongitude;
function myLocalSearch(){
  if (navigator.geolocation) {

    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
    navigator.geolocation.getCurrentPosition(function(position) {

      myLatitude = position.coords.latitude, // 위도
      mylongitude = position.coords.longitude; // 경도
    });

    console.log("myLatitude = " + myLatitude + ", mylongitude = " + mylongitude);
  } else { // HTML5의 GeoLocation을 사용할 수 없을때
      alert("위치 엑세스를 허용해주세요!");
  }
}
document.getElementById("my-local").addEventListener("click", ()=>{
  myLocalSearch();
  console.log("myLatitude = " + myLatitude + ", mylongitude = " + mylongitude);
})