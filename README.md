## 맛집 리뷰 웹 사이트 프로젝트(Maturi)

### 아래 링크는 MATURI프로젝트를 정리한 블로그 링크입니다. 꼭 한번 봐주세요!
&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;

https://51-taek.tistory.com/36
### 아래링크는 cafe24에 배포한 MATURI프로젝트 사이트입니다!
&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;

http://maturi51.store

 * maturi사이트 이용시 회원가입 절차가 번거러우시다면 아래 정보로 로그인하셔서 이용하시면 됩니다.
 > 아이디 : demo5151@maturi.com   
 > 비밀번호 : demo5151!
 * 현재위치로 게시글 검색(필터링)은 geolocation을 사용시 SSL프로토콜을 사용하지 않으면 지원하지 않아서 정상적으로 작동하지 않습니다.
 * 네이버 로그인은 현재 검수를 통화받지 못한 상태여서 이용이 불가능합니다.

![포스터_640](https://user-images.githubusercontent.com/86419261/235067246-6271a4c7-7eed-4109-b15e-bf428c0f7128.png)



## 프로젝트 소개 및 주제
##### 프로젝트 명인 maturi는 맛집 + 집들이의 합성어로 집들이 하며 집을 소개하듯이 리뷰글을 통해 맛집을 소개하자는 의미입니다.
##### 전반적인 웹의 기본 소양이 되는 게시판, 회원 CRUD를 중심으로 한 커뮤니티 사이트입니다.
##### 카카오 map api, 카카오 로그인 api, 네이버 로그인 api 등등의 여러 api 통신을 적극 활용하였습니다.
##### 많은 경우의 수가 존재하는 검색 조건, 정렬을 하나의 동적 쿼리문으로 처리하여 차별화하였습니다.

## 프로젝트 개요
1. 프로젝트 명칭 : MATURI
2. 개발 인원 : 2명
3. 개발자 : 오원택(팀장), 김혜현
4. 개발 기간 : 2023.03.07 ~ 진행 중
5. 주요 기술 스택 : java, spring, spring boot, JPA, querydsl, thymeleaf, javsscript

## 메뉴 구조도 IA (InformationArchitecture)
##### 개발자나 시스템 관리자를 대상으로 작성한 메뉴 구조도로 애플리케이션의 전반적인 기능을 담고 있습니다. 마인드맵 형태로 구성하여 한눈에 볼 수 있도록 제작하였습니다.
![메뉴 구조도](https://user-images.githubusercontent.com/86419261/235066486-540121a8-ff96-4888-b9ef-2968aceec263.png)
## ERD (Entity Relationshop Diagram)
![ERD](https://user-images.githubusercontent.com/86419261/236677196-c66ce685-52dc-47e4-9a55-0f997662cd47.png)

## 기술 스택
 * 개발 언어 : JAVA(Oracle OpenJDB version 1.8), Html + Css, Javascript </br>
<img src="https://user-images.githubusercontent.com/86419261/235322478-a840a7f0-3271-4938-909d-85cea7892459.png" height="100" /> <img src="https://user-images.githubusercontent.com/86419261/235322830-6e468288-bc02-4c7a-be45-db8ee05ab25e.png" height="100" />
 * 데이터 베이스 : MariaDB(10.1.x), H2 Database(1.4.200) </br>
<img src="https://user-images.githubusercontent.com/86419261/235323040-e6200760-68b7-4174-81a9-dbae30734671.png" height="100"/> <img src="https://user-images.githubusercontent.com/86419261/235323071-b41d5298-e7fc-43c7-855f-f06c26000dae.png" height="100"/>
 * 라이브러리 : Jquery, Querydsl, Spring Data JPA ,Thymeleaf </br>
<img src="https://user-images.githubusercontent.com/86419261/235323112-7885fe90-7909-4542-8eef-7f29bc31618b.png" height="100"/> <img src="https://user-images.githubusercontent.com/86419261/235323135-e2d42a99-ee80-4395-bcf3-ef3d3bc26498.png" height="100"/> <img src="https://user-images.githubusercontent.com/86419261/235323147-b5f101be-a4c9-4a0d-b45e-692b9ca6b4e3.png" height="100"/> <img src="https://user-images.githubusercontent.com/86419261/235323161-b6356380-2416-4843-9944-4e11bbd3e78c.png" height="100"/>
 * 프레임워크 : Spring, Spring boot,Bootstrap </br>
<img src="https://user-images.githubusercontent.com/86419261/235323197-36c6cf2a-1c32-4b79-9b90-381579343c04.png" height="100"/> <img src="https://user-images.githubusercontent.com/86419261/235323203-c504fc8d-da2e-4984-8b77-2fb0f7c11e4a.png" height="100"/> <img src="https://user-images.githubusercontent.com/86419261/235323215-0c5efca2-e476-4ddb-922b-107250000340.png" height="100"/>
 * 웹 서버 및 WAS : Apache-Tomcat 8.0.9 </br>
 <img src="https://user-images.githubusercontent.com/86419261/235323237-d679fdf0-7997-4c6c-986a-d20c3cd5d858.png" height="100"/>
 
 * 개발 환경 : Intellij, Visual Studio Code, HeidiSQL </br>
<img src="https://user-images.githubusercontent.com/86419261/235323252-464b517c-a4f0-42e8-b853-d4a62ff81ed4.png" height="100"/> <img src="https://user-images.githubusercontent.com/86419261/235323313-a3a6a8f2-7356-475c-aa9a-afda35e9ddc2.png" height="100"/> <img src="https://user-images.githubusercontent.com/86419261/235323267-e822cf98-1b6d-4df6-b4b6-2271cc5a339c.png" height="100"/>
 * 테스트 도구 : Junit, Postman </br>
<img src="https://user-images.githubusercontent.com/86419261/235323364-33a6d7fd-d404-48a0-8cc5-b756cd6457d0.png" height="100"/> <img src="https://user-images.githubusercontent.com/86419261/235323377-ba9e9b87-f610-42c9-937e-f90dad578bec.png" height="100"/>
 * 형상관리 도구 및 기타 도구 : Git, Github, Notion , Miro </br>
<img src="https://user-images.githubusercontent.com/86419261/235323388-471a972f-a08c-4844-aab3-0a847d886db9.png" height="100"/> <img src="https://user-images.githubusercontent.com/86419261/235323404-fd217787-cc2d-4b75-9b8d-ca7a2e9d74c1.png" height="100"/> <img src="https://user-images.githubusercontent.com/86419261/235323411-c37cfed4-9e52-474f-bafb-e66b60b24ffd.png" height="100"/>

## 시스템 명세서
| 구분 | 개발도구 | 역할 |
| :--: | ------ | --- |
| 플랫폼 | cafe24 | JSP Tomcat 호스팅 플랫폼 |
| 개발환경 및 소프트웨어 | Intellij 2022-0203 | 프로그래밍 언어 통합 개발 IDE |
|  | Oracle OpenJDB version 1.8 | 자바 프로그래밍 언어 Open JDK |
|  | MariaDB 10.1.x UTF-8 | 오픈소스 RDBMS 소프트웨어 |
|  | H2 DB 1.4.200 | 테스트 코드를 위한 RDBMS 소프트웨어 |
|  | hibernate:5.1.0.Final | Java ORM 기술의 구현체 |
|  | maria-java-client 2.7.0 | Java와 MariaDB 연결 인터페이스(JDBC) |
|  | Visual Studio Code | 프로그래밍 언어 통합 개발 IDE |
|  | HeidiSQL | (DBMS) Database Management System |
|  | Diagrams | 소프트웨어 명세서 작성 도구 |
|  | apache-tomcat version 8.0 | 웹 개발을 위한 웹 서버 및 WAS |
|  | Git | 형상 관리를 위한 분산 저장소 |
|  | GitHub | 협업을 위한 원격 저장소 |
|  | Notion | 회의 및 각종 프로젝트 자료를 공유하기 위한 협업 도구 |
|  | Miro | 와이어 프레임 제작 도구 |
| Framework | Spring.5.2.2.RELEASE | 자바 기반의 오픈소스 애플리케이션 프레임워크 |
|  | Spring Boot.2.2.2.RELEASE | Spring 프레임워크의 다양한 기능을 지원하는 프레임워크 |
|  | Bootstrap | 프론트엔드 웹 개발 프레임워크 |
| Library | Querydsl.4.2.2 | 자바 기반의 타입 안전한 SQL 쿼리 작성을 지원하는 라이브러리 |
|  | Spring data JPA:2.2.3.RELEASE | Spring 프레임워크에서 제공하는 ORM 기술 |
|  | jQuery | 자바스크립트 기반의 라이브러리 |
|  | json-simple.1.1.1 | JSON 데이터를 생성하고 파싱하는 Java 라이브러리 |
|  | jakarta.mail:1.6.4 | 이메일 인증을 구현하기 위한 기술 |
|  | modelmapper:2.4.2 | 자바 객체 간의 매핑을 쉽게 처리하기 위한

## URL설계서
<img src="https://user-images.githubusercontent.com/86419261/235324318-fb2322f9-d9b8-4187-a577-f7ed429f9eee.jpg"/>

### 아래 링크는 MATURI프로젝트를 정리한 블로그 링크입니다. 꼭 한번 봐주세요!
&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;&darr;

https://51-taek.tistory.com/36
