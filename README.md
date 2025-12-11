# 2팀
(팀 협업 문서 링크 게시)

## 팀원 구성
- 이진우 ([Github 링크](https://github.com/jionu102))
- 김승빈 ([Github 링크](https://github.com/mainlib990))
- 김태현 ([Github 링크](https://github.com/kimtaehyun80))
- 박종건 ([Github 링크](https://github.com/3Park))
- 이호건 ([Github 링크](https://github.com/HOGUN00))
- 조동현 ([Github 링크](https://github.com/donghyun9898))

---

## 프로젝트 소개

덕후감 : 도서 이미지 OCR 및 ISBN 매칭 서비스  
프로젝트 기간: 2025.11.21 ~ 2025.12.12

---

## 기술 스택

- **Backend:** Spring Boot, Lombok, Spring Data JPA, MapStruct, springdoc-openapi, Spring scheduler, Spring batch, Flyway, QueryDSL
- **Database:** Postgresql
- **공통 Tool:** Github, Discord

---

## 팀원별 구현 기능 상세

### 이진우
- (자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)
- **소셜 로그인 API**
    - Google OAuth 2.0을 활용한 소셜 로그인 기능 구현
    - 로그인 후 추가 정보 입력을 위한 RESTful API 엔드포인트 개발
- **회원 추가 정보 입력 API**
    - 회원 유형(관리자, 학생)에 따른 조건부 입력 처리 API 구현

---

### 김승빈
- (자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)
- **회원별 권한 관리**
    - Spring Security를 활용하여 사용자 역할에 따른 권한 설정
    - 관리자 페이지와 일반 사용자 페이지를 위한 조건부 라우팅 처리
- **반응형 레이아웃 API**
    - 클라이언트에서 요청된 반응형 레이아웃을 위한 RESTful API 엔드포인트 구현

---

### 김태현
- (자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)
- **수강생 정보 관리 API**
    - GET 요청을 사용하여 학생의 수강 정보를 조회하는 API 엔드포인트 개발
    - 학생 정보의 CRUD 처리 (Spring Data JPA 사용)
- **공용 Button API**
    - 공통으로 사용할 버튼 기능을 처리하는 API 엔드포인트 구현

---

### 박종건
- (자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)
- **관리자 API**
    - `@PathVariable`을 사용한 동적 라우팅 기능 구현
    - PATCH, DELETE 요청을 사용하여 학생 정보를 수정하고 탈퇴하는 API 엔드포인트 개발
- **학생 정보의 CRUD 기능**
    - Spring Data JPA를 활용한 학생 정보 CRUD API 구현
- **회원관리 슬라이더**
    - 학생별 정보 목록을 Carousel 형식으로 조회하는 API 구현

---

### 이호건
- (자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)
- **학생 시간 정보 관리 API**
    - 학생별 시간 정보를 GET 요청으로 조회하는 API 구현
    - 실시간 접속 현황을 관리하는 API 엔드포인트 개발
- **수정 및 탈퇴 API**
    - PATCH, DELETE 요청을 사용하여 수강생의 개인정보 수정 및 탈퇴 처리
- **공용 Modal API**
    - 공통 Modal 컴포넌트를 처리하는 API 구현
 
### 조동현
- (자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)
- **학생 시간 정보 관리 API**
    - 학생별 시간 정보를 GET 요청으로 조회하는 API 구현
    - 실시간 접속 현황을 관리하는 API 엔드포인트 개발
- **수정 및 탈퇴 API**
    - PATCH, DELETE 요청을 사용하여 수강생의 개인정보 수정 및 탈퇴 처리
- **공용 Modal API**
    - 공통 Modal 컴포넌트를 처리하는 API 구현

---

## 파일 구조


---

## 구현 홈페이지  
(개발한 홈페이지에 대한 링크 게시)  
[서비스 배포 링크](http://deokhugam-lb-635555306.ap-northeast-2.elb.amazonaws.com/index.html#/login)

---

## 프로젝트 회고록  
(제작한 발표자료 링크 혹은 첨부파일 첨부)
