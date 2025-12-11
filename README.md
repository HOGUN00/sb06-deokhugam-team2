# 2팀
- [GitHub Issue](https://github.com/codeit-team2-intermediate-project/sb06-deokhugam-team2/issues)
- [Github Project](https://github.com/orgs/codeit-team2-intermediate-project/projects/4/views/4)

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
- **사용자 회원가입,로그인**
    - 탈퇴후 재가입,로그인 불가 추가 구현.(요구사항외 구현)
- **사용자 수정**
    - 닉네임 수정 구현(프론트에서 string으로 들어오는 요청에 맞게 구현)
- **사용자 논리,물리 삭제**
    - 사용자 논리삭제시 연관관계(리뷰,뎃글)도 함께 논리삭제로 전환되도록 구현
    - 논리삭제후 1일 경과시 물리삭제로 전환 되도록 배치처리
- **대시보드 파워유저 순위 및 매일배치**
    - 사용자 활동점수로 통계를 이용해서 파워유저 순위 구현
    - 대시보드상의 매일배치 가 이루어지도록 구현
  
---

### 박종건
- **알림 읽기**
  - 알림 일괄 읽기 기능 구현
  - 알림 단건 읽기 기능 구현
- **알림 일괄 읽기 및 삭제**
    - Spring batch 와 scheduler 를 이용해 읽은지 7일 이상 경과된 알림 삭제
    - Spring batch 와 scheduler 를 이용해 일괄 알림 읽기
- **알림 조회**
    - QueryDSL을 이용해 알림 cursor 페이지네이션 조회
- **알림 등록**
  - 리뷰, 댓글 작성 등 타 도메인에서 알림등록 가능하도록 등록 컴포넌트 구현
- **로깅**
    - HandlerInterceptor 와 logback 설정을 통한 로깅
    - MDC 적용
- **전역오류처리**
    - RestControllerAdvice 를 이용한 전역 오류 처리
    - 알림 관련 커스텀 Exception 구현
- **DDL 관리**
    - Flyway로 DDL 관리      

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

sb06-deokhugam-team2
├── build.gradle
├── docker-compose.yml
├── Dockerfile
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── README.md
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── codeit
    │   │           └── sb06deokhugamteam2
    │   │               ├── book
    │   │               │   ├── client
    │   │               │   │   └── NaverSearchClient.java
    │   │               │   ├── controller
    │   │               │   │   └── BookController.java
    │   │               │   ├── dto
    │   │               │   │   ├── data
    │   │               │   │   │   ├── BookDashboardDto.java
    │   │               │   │   │   ├── BookDto.java
    │   │               │   │   │   └── PopularBookDto.java
    │   │               │   │   ├── request
    │   │               │   │   │   ├── BookCreateRequest.java
    │   │               │   │   │   ├── BookImageCreateRequest.java
    │   │               │   │   │   └── BookUpdateRequest.java
    │   │               │   │   └── response
    │   │               │   │       ├── CursorPageResponseBookDto.java
    │   │               │   │       ├── CursorPageResponsePopularBookDto.java
    │   │               │   │       ├── NaverBookDto.java
    │   │               │   │       └── NaverSearchResponse.java
    │   │               │   ├── entity
    │   │               │   │   ├── Book.java
    │   │               │   │   └── BookStats.java
    │   │               │   ├── mapper
    │   │               │   │   ├── BookCursorMapper.java
    │   │               │   │   └── BookMapper.java
    │   │               │   ├── package-info.java
    │   │               │   ├── repository
    │   │               │   │   ├── BookRepository.java
    │   │               │   │   ├── BookRepositoryCustom.java
    │   │               │   │   └── BookRepositoryCustomImpl.java
    │   │               │   ├── service
    │   │               │   │   ├── BookService.java
    │   │               │   │   └── OcrService.java
    │   │               │   └── storage
    │   │               │       └── S3Storage.java
    │   │               ├── comment
    │   │               │   ├── controller
    │   │               │   │   └── CommentController.java
    │   │               │   ├── dto
    │   │               │   │   ├── CommentCreateRequest.java
    │   │               │   │   ├── CommentDto.java
    │   │               │   │   ├── CommentUpdateRequest.java
    │   │               │   │   └── CursorPageResponseCommentDto.java
    │   │               │   ├── entity
    │   │               │   │   └── Comment.java
    │   │               │   ├── mapper
    │   │               │   │   └── CommentMapper.java
    │   │               │   ├── package-info.java
    │   │               │   ├── repository
    │   │               │   │   ├── CommentQueryRepository.java
    │   │               │   │   ├── CommentQueryRepositoryImpl.java
    │   │               │   │   └── CommentRepository.java
    │   │               │   └── service
    │   │               │       └── CommentService.java
    │   │               ├── common
    │   │               │   ├── config
    │   │               │   │   ├── DataSourceConfig.java
    │   │               │   │   ├── LoggingInterceptor.java
    │   │               │   │   ├── OkHttpConfig.java
    │   │               │   │   ├── QueryCountInterceptor.java
    │   │               │   │   ├── QuerydslConfig.java
    │   │               │   │   ├── RestTemplateConfig.java
    │   │               │   │   └── WebConfig.java
    │   │               │   ├── enums
    │   │               │   │   ├── PeriodType.java
    │   │               │   │   └── RankingType.java
    │   │               │   ├── exception
    │   │               │   │   ├── ErrorCode.java
    │   │               │   │   ├── ErrorResponse.java
    │   │               │   │   ├── exceptions
    │   │               │   │   │   ├── AWSException.java
    │   │               │   │   │   ├── BasicException.java
    │   │               │   │   │   ├── BookException.java
    │   │               │   │   │   ├── CommentException.java
    │   │               │   │   │   ├── MDCException.java
    │   │               │   │   │   ├── NaverSearchException.java
    │   │               │   │   │   ├── NotificationException.java
    │   │               │   │   │   ├── OcrException.java
    │   │               │   │   │   └── UserException.java
    │   │               │   │   ├── GlobalExceptionHandler.java
    │   │               │   │   └── package-info.java
    │   │               │   └── interceptor
    │   │               │       └── UserIdHeaderInterceptor.java
    │   │               ├── dashboard
    │   │               │   ├── batch
    │   │               │   │   ├── book
    │   │               │   │   │   ├── BookDashboardCreateBatchConfig.java
    │   │               │   │   │   └── BookDashboardScheduler.java
    │   │               │   │   ├── listener
    │   │               │   │   │   └── RankingListener.java
    │   │               │   │   ├── review
    │   │               │   │   │   ├── ReviewDashboardCreateBatchConfig.java
    │   │               │   │   │   └── ReviewDashboardScheduler.java
    │   │               │   │   └── user
    │   │               │   │       ├── UserDashboardJobConfig.java
    │   │               │   │       └── UserDashboardScheduler.java
    │   │               │   ├── controller
    │   │               │   │   └── DashboardController.java
    │   │               │   ├── dto
    │   │               │   │   ├── data
    │   │               │   │   │   ├── PopularReviewDto.java
    │   │               │   │   │   └── ReviewReaderItemDto.java
    │   │               │   │   └── response
    │   │               │   │       └── CursorPageResponsePopularReviewDto.java
    │   │               │   ├── entity
    │   │               │   │   └── Dashboard.java
    │   │               │   ├── repository
    │   │               │   │   ├── DashboardRepository.java
    │   │               │   │   ├── DashboardRepositoryCustom.java
    │   │               │   │   ├── DashboardRepositoryImpl.java
    │   │               │   │   ├── UserRepositoryExtension.java
    │   │               │   │   └── UserRepositoryExtensionImpl.java
    │   │               │   └── service
    │   │               │       └── DashboardService.java
    │   │               ├── like
    │   │               │   ├── adapter
    │   │               │   │   ├── in
    │   │               │   │   │   └── event
    │   │               │   │   │       └── LikeReviewEventHandler.java
    │   │               │   │   └── out
    │   │               │   │       ├── entity
    │   │               │   │       │   ├── ReviewLike.java
    │   │               │   │       │   └── ReviewLikeId.java
    │   │               │   │       └── repository
    │   │               │   │           └── LikeReviewJpaRepository.java
    │   │               │   ├── application
    │   │               │   │   ├── port
    │   │               │   │   │   ├── in
    │   │               │   │   │   │   ├── CancelReviewLikeUseCase.java
    │   │               │   │   │   │   ├── DeleteReviewLikesUseCase.java
    │   │               │   │   │   │   └── LikeReviewUseCase.java
    │   │               │   │   │   └── out
    │   │               │   │   │       └── SaveLikeReviewPort.java
    │   │               │   │   └── service
    │   │               │   │       └── LikeReviewCommandService.java
    │   │               │   └── package-info.java
    │   │               ├── notification
    │   │               │   ├── batch
    │   │               │   │   ├── NotificationDeleteBatchConfig.java
    │   │               │   │   ├── NotificationReadAllBatchConfig.java
    │   │               │   │   └── NotificationScheduler.java
    │   │               │   ├── controller
    │   │               │   │   └── NotificationController.java
    │   │               │   ├── entity
    │   │               │   │   ├── dto
    │   │               │   │   │   ├── NotificaionCursorDto.java
    │   │               │   │   │   ├── NotificationDto.java
    │   │               │   │   │   ├── request
    │   │               │   │   │   │   ├── NotificationCreateRequest.java
    │   │               │   │   │   │   └── NotificationUpdateRequest.java
    │   │               │   │   │   └── response
    │   │               │   │   │       └── NotificationCursorResponse.java
    │   │               │   │   └── Notification.java
    │   │               │   ├── NotificationComponent.java
    │   │               │   ├── package-info.java
    │   │               │   ├── repository
    │   │               │   │   ├── NotificationRepository.java
    │   │               │   │   ├── NotificationRepositoryDsl.java
    │   │               │   │   └── NotificationRepositoryDslImpl.java
    │   │               │   └── service
    │   │               │       ├── NotificationService.java
    │   │               │       └── NotificationServiceImpl.java
    │   │               ├── review
    │   │               │   ├── adapter
    │   │               │   │   ├── in
    │   │               │   │   │   └── web
    │   │               │   │   │       ├── ReviewApi.java
    │   │               │   │   │       ├── ReviewController.java
    │   │               │   │   │       └── ReviewControllerAdvice.java
    │   │               │   │   └── out
    │   │               │   │       ├── entity
    │   │               │   │       │   ├── Review.java
    │   │               │   │       │   └── ReviewStat.java
    │   │               │   │       ├── event
    │   │               │   │       │   └── ReviewEventPublisherAdapter.java
    │   │               │   │       ├── mapper
    │   │               │   │       │   ├── ReviewJpaMapper.java
    │   │               │   │       │   └── ReviewStatJpaMapper.java
    │   │               │   │       └── repository
    │   │               │   │           ├── ReviewBookJpaRepository.java
    │   │               │   │           ├── ReviewCommentJpaRepository.java
    │   │               │   │           ├── ReviewJpaRepository.java
    │   │               │   │           ├── ReviewLikeJpaRepository.java
    │   │               │   │           ├── ReviewNotificationRepository.java
    │   │               │   │           └── ReviewUserJpaRepository.java
    │   │               │   ├── application
    │   │               │   │   ├── dto
    │   │               │   │   │   ├── request
    │   │               │   │   │   │   ├── CursorPageRequestReviewDto.java
    │   │               │   │   │   │   ├── ReviewCreateRequest.java
    │   │               │   │   │   │   └── ReviewUpdateRequest.java
    │   │               │   │   │   └── response
    │   │               │   │   │       ├── CursorPageResponseReviewDto.java
    │   │               │   │   │       ├── ReviewDto.java
    │   │               │   │   │       └── ReviewLikeDto.java
    │   │               │   │   ├── port
    │   │               │   │   │   ├── in
    │   │               │   │   │   │   ├── CreateReviewUseCase.java
    │   │               │   │   │   │   ├── DeleteReviewUseCase.java
    │   │               │   │   │   │   ├── GetReviewUseCase.java
    │   │               │   │   │   │   ├── ToggleReviewLikeUseCase.java
    │   │               │   │   │   │   └── UpdateReviewUseCase.java
    │   │               │   │   │   └── out
    │   │               │   │   │       ├── LoadReviewBookPort.java
    │   │               │   │   │       ├── LoadReviewLikePort.java
    │   │               │   │   │       ├── LoadReviewPort.java
    │   │               │   │   │       ├── LoadReviewUserPort.java
    │   │               │   │   │       ├── ReviewEventPublisher.java
    │   │               │   │   │       ├── SaveReviewBookPort.java
    │   │               │   │   │       ├── SaveReviewCommentPort.java
    │   │               │   │   │       ├── SaveReviewNotificationPort.java
    │   │               │   │   │       ├── SaveReviewPort.java
    │   │               │   │   │       └── SaveReviewUserPort.java
    │   │               │   │   └── service
    │   │               │   │       ├── ReviewCommandService.java
    │   │               │   │       └── ReviewQueryService.java
    │   │               │   ├── domain
    │   │               │   │   ├── event
    │   │               │   │   │   ├── ReviewDeletedEvent.java
    │   │               │   │   │   ├── ReviewLikeCanceledEvent.java
    │   │               │   │   │   └── ReviewLikedEvent.java
    │   │               │   │   ├── exception
    │   │               │   │   │   ├── AlreadyExistsReviewException.java
    │   │               │   │   │   ├── InvalidReviewContentException.java
    │   │               │   │   │   ├── InvalidReviewCountException.java
    │   │               │   │   │   ├── InvalidReviewRatingException.java
    │   │               │   │   │   ├── ReviewBookNotFoundException.java
    │   │               │   │   │   ├── ReviewException.java
    │   │               │   │   │   ├── ReviewNotFoundException.java
    │   │               │   │   │   ├── ReviewPermissionDeniedException.java
    │   │               │   │   │   └── ReviewUserNotFoundException.java
    │   │               │   │   ├── model
    │   │               │   │   │   ├── ReviewBookDomain.java
    │   │               │   │   │   ├── ReviewContentDomain.java
    │   │               │   │   │   ├── ReviewCountDomain.java
    │   │               │   │   │   ├── ReviewDomain.java
    │   │               │   │   │   ├── ReviewLikeDomain.java
    │   │               │   │   │   ├── ReviewLikeNotificationDomain.java
    │   │               │   │   │   ├── ReviewRatingDomain.java
    │   │               │   │   │   ├── ReviewStatDomain.java
    │   │               │   │   │   └── ReviewUserDomain.java
    │   │               │   │   └── service
    │   │               │   │       └── ReviewService.java
    │   │               │   └── package-info.java
    │   │               ├── Sb06DeokhugamTeam2Application.java
    │   │               └── user
    │   │                   ├── controller
    │   │                   │   └── UserController.java
    │   │                   ├── dto
    │   │                   │   ├── CursorPageResponse.java
    │   │                   │   ├── PowerUserDto.java
    │   │                   │   ├── UserDto.java
    │   │                   │   ├── UserLoginRequest.java
    │   │                   │   ├── UserRegisterRequest.java
    │   │                   │   └── UserUpdateRequest.java
    │   │                   ├── entity
    │   │                   │   └── User.java
    │   │                   ├── mapper
    │   │                   │   └── UserMapper.java
    │   │                   ├── package-info.java
    │   │                   ├── repository
    │   │                   │   ├── UserQueryRepository.java
    │   │                   │   └── UserRepository.java
    │   │                   ├── scheduler
    │   │                   │   └── UserBatchScheduler.java
    │   │                   └── service
    │   │                       └── UserService.java
    │   └── resources
    │       ├── application-local.yaml
    │       ├── application-prod.yaml
    │       ├── application.yaml
    │       ├── db
    │       │   └── migration
    │       │       ├── V1__init.sql
    │       │       ├── V2__change_table_names.sql
    │       │       ├── V3__remove_redundant_colums.sql
    │       │       ├── V4__add_cascade.sql
    │       │       └── V5__add_version_to_book.sql
    │       ├── logback-spring.xml
    └── test
        ├── java
        │   └── com
        │       └── codeit
        │           └── sb06deokhugamteam2
        │               ├── book
        │               │   ├── BookIntegrationTest.java
        │               │   ├── fixture
        │               │   │   └── BookFixture.java
        │               │   └── repository
        │               │       └── BookVersionWithSQLRestrictionRepositoryTest.java
        │               ├── comment
        │               │   └── CommentIntegrationTest.java
        │               ├── dashboard
        │               │   ├── DashboardIntegrationTest.java
        │               │   └── fixture
        │               │       └── DashboardFixture.java
        │               ├── notification
        │               │   └── NotificationTest.java
        │               ├── review
        │               │   └── ReviewTest.java
        │               ├── Sb06DeokhugamTeam2ApplicationTests.java
        │               └── user
        │                   └── UserControllerIntegrationTest.java
        └── resources
            └── application-test.yaml

---

## 구현 홈페이지  
(개발한 홈페이지에 대한 링크 게시)  
[서비스 배포 링크](http://deokhugam-lb-635555306.ap-northeast-2.elb.amazonaws.com/index.html#/login)

---

## 프로젝트 회고록  
(제작한 발표자료 링크 혹은 첨부파일 첨부)
