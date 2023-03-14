let a_latitude = 35.8661154915473; // 음식점 위도
let a_longitude = 128.593834750412; // 음식점 경도

var a_mapContainer = document.getElementById('articleMap'), // 지도를 표시할 div
  a_mapOption = {
    center: new kakao.maps.LatLng(a_latitude, a_longitude), // 지도의 중심좌표
    level: 3 // 지도의 확대 레벨
  };

var a_Map = new kakao.maps.Map(a_mapContainer, a_mapOption); // 지도 생성

// 마커가 표시될 위치
var a_markerPosition  = new kakao.maps.LatLng(a_latitude, a_longitude);

// 마커 생성
var a_Marker = new kakao.maps.Marker({
  position: a_markerPosition
});

// 마커가 지도 위에 표시되도록 설정
a_Marker.setMap(a_Map);

// 아래 코드는 지도 위의 마커를 제거하는 코드입니다
// marker.setMap(null);
