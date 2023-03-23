package com.maturi.util.constfield;

public class LocationConst {
    //위도 1도 : 111.1412Km = ? : 7.1km
    //지하철역 8개 (성서산업단지역 ~ 청라언덕역 기준) = 7.1km(차로 이동했을 때 최단경로)
    // 7.1 ÷ 111.1412 ÷ 2 를 계산하면 +, - 해줘야할 값이 나옴
    public final static Double kmToLat = 0.03194135028234354;
}
