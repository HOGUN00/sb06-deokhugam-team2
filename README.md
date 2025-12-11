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
   - 리뷰 쓰기
       - CQRS 패턴 기반으로 리뷰 생성 로직 구현
       - 리뷰 작성 시 도서의 총 리뷰 수와 평균 평점을 실시간으로 재계산 및 반영

   - 리뷰 읽기
       - 유연한 리뷰 조회 기능 제공
       - 단일 리뷰 조회 및 커서 기반 페이지네이션을 통한 목록 조회 지원
       - 사용자, 도서, 키워드 등 다양한 조건에 따른 필터링 및 정렬 기능 제공

   - 리뷰 수정
       - 기존 리뷰 내용 및 평점 수정 기능 구현
       - 수정된 평점을 즉시 반영하여 도서의 전체 평점 통계 재계산 및 데이터 정합성 보장

   - 리뷰 삭제
       - 두 가지 삭제 전략 제공: '소프트 삭제' (숨김 처리) 및 '하드 삭제' (영구 제거)
       - 각 삭제 방식은 도서의 리뷰 통계 및 관련 데이터 (예: 댓글)에 즉시 반영

   - 리뷰 좋아요
       - 핵심 리뷰 도메인과 분리된 독립적인 '좋아요' 모듈 구현
       - 좋아요 상태 토글 (좋아요/취소) 시 이벤트 기반 알림 전송 기능 제공

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
    - 사용자 활동점수 기준으로 파워유저 순위 구현
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
- **도서 삭제**
    - 논리 삭제, 물리 삭제
- **OCR 기반 ISBN 인식**
    - OCR SPACE API를 통해 이미지 안에서 ISBN 인식
- **인기 도서 목록 조회**
    - 매일 배치 작업으로 대시보드에 인기 도서 저장
    - QueryDsl과 커서 페이지네이션으로 조회
- **도서 낙관적 락**
    - 도서 수정과 도서 논리 삭제가 동시에 이루어질 시 어느 한 작업이 무시되지 않도록 낙관적 락 구현
- **AWS 관리**

 ---
 
### 조동현
- **댓글 등록**
    - 리뷰에 댓글을 작성하는 기능 구현
    - ReviewStat과 연동하여 댓글 생성시 commentCount가 증가하도록 처리
    - Notification과 연동하여 댓글 작성시 해당 리뷰 작성자에게 알림이 가도록 처리
- **댓글 수정**
    - 본인이 작성한 댓글인지 검증하고 댓글을 수정하는 기능 구현
- **댓글 단건 조회**
    - 삭제되지 않은 댓글을 조회하는 기능 구현
- **댓글 목록 조회**
    - 커서 페이지네이션 기반으로 댓글 목록을 조회하는 기능 구현
    - createAt,commentId 두가지를 복합 정렬 조건으로 사용하여 정확한 순서를 보장하도록 하였음
- **댓글 삭제**
    - softDelete,hardDelete 두가지 기능을 모두 지원하도록 구현
    - softDelete 는 deleted 필드를 true처리하여 숨김
    - hardDelete 는 db사아에서 영구삭제 처리
    - 논리삭제 되지 않은 댓글을 물리삭제 하려 할때 논리삭제후 물리삭제 되도록 처리

---

## 파일 구조

```text
sb06-deokhugam-team2
├── build.gradle
├── docker-compose.yml
├── Dockerfile
├── gradle
│     └── wrapper
│         ├── gradle-wrapper.jar
│         └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── README.md
├── settings.gradle
└── src
    ├── main
    │     ├── java
    │     │     └── com
    │     │         └── codeit
    │     │             └── sb06deokhugamteam2
    │     │                 ├── book
    │     │                 │     ├── client
    │     │                 │     │     └── NaverSearchClient.java
    │     │                 │     ├── controller
    │     │                 │     │     └── BookController.java
    │     │                 │     ├── dto
    │     │                 │     │     ├── data
    │     │                 │     │     │     ├── BookDashboardDto.java
    │     │                 │     │     │     ├── BookDto.java
    │     │                 │     │     │     └── PopularBookDto.java
    │     │                 │     │     ├── request
    │     │                 │     │     │     ├── BookCreateRequest.java
    │     │                 │     │     │     ├── BookImageCreateRequest.java
    │     │                 │     │     │     └── BookUpdateRequest.java
    │     │                 │     │     └── response
    │     │                 │     │         ├── CursorPageResponseBookDto.java
    │     │                 │     │         ├── CursorPageResponsePopularBookDto.java
    │     │                 │     │         ├── NaverBookDto.java
    │     │                 │     │         └── NaverSearchResponse.java
    │     │                 │     ├── entity
    │     │                 │     │     ├── Book.java
    │     │                 │     │     └── BookStats.java
    │     │                 │     ├── mapper
    │     │                 │     │     ├── BookCursorMapper.java
    │     │                 │     │     └── BookMapper.java
    │     │                 │     ├── package-info.java
    │     │                 │     ├── repository
    │     │                 │     │     ├── BookRepository.java
    │     │                 │     │     ├── BookRepositoryCustom.java
    │     │                 │     │     └── BookRepositoryCustomImpl.java
    │     │                 │     ├── service
    │     │                 │     │     ├── BookService.java
    │     │                 │     │     └── OcrService.java
    │     │                 │     └── storage
    │     │                 │         └── S3Storage.java
    │     │                 ├── comment
    │     │                 │     ├── controller
    │     │                 │     │     └── CommentController.java
    │     │                 │     ├── dto
    │     │                 │     │     ├── CommentCreateRequest.java
    │     │                 │     │     ├── CommentDto.java
    │     │                 │     │     ├── CommentUpdateRequest.java
    │     │                 │     │     └── CursorPageResponseCommentDto.java
    │     │                 │     ├── entity
    │     │                 │     │     └── Comment.java
    │     │                 │     ├── mapper
    │     │                 │     │     └── CommentMapper.java
    │     │                 │     ├── package-info.java
    │     │                 │     ├── repository
    │     │                 │     │     ├── CommentQueryRepository.java
    │     │                 │     │     ├── CommentQueryRepositoryImpl.java
    │     │                 │     │     └── CommentRepository.java
    │     │                 │     └── service
    │     │                 │         └── CommentService.java
    │     │                 ├── common
    │     │                 │     ├── config
    │     │                 │     │     ├── DataSourceConfig.java
    │     │                 │     │     ├── LoggingInterceptor.java
    │     │                 │     │     ├── OkHttpConfig.java
    │     │                 │     │     ├── QueryCountInterceptor.java
    │     │                 │     │     ├── QuerydslConfig.java
    │     │                 │     │     ├── RestTemplateConfig.java
    │     │                 │     │     └── WebConfig.java
    │     │                 │     ├── enums
    │     │                 │     │     ├── PeriodType.java
    │     │                 │     │     └── RankingType.java
    │     │                 │     ├── exception
    │     │                 │     │     ├── ErrorCode.java
    │     │                 │     │     ├── ErrorResponse.java
    │     │                 │     │     ├── exceptions
    │     │                 │     │     │     ├── AWSException.java
    │     │                 │     │     │     ├── BasicException.java
    │     │                 │     │     │     ├── BookException.java
    │     │                 │     │     │     ├── CommentException.java
    │     │                 │     │     │     ├── MDCException.java
    │     │                 │     │     │     ├── NaverSearchException.java
    │     │                 │     │     │     ├── NotificationException.java
    │     │                 │     │     │     ├── OcrException.java
    │     │                 │     │     │     └── UserException.java
    │     │                 │     │     ├── GlobalExceptionHandler.java
    │     │                 │     │     └── package-info.java
    │     │                 │     └── interceptor
    │     │                 │         └── UserIdHeaderInterceptor.java
    │     │                 ├── dashboard
    │     │                 │     ├── batch
    │     │                 │     │     ├── book
    │     │                 │     │     │     ├── BookDashboardCreateBatchConfig.java
    │     │                 │     │     │     └── BookDashboardScheduler.java
    │     │                 │     │     ├── listener
    │     │                 │     │     │     └── RankingListener.java
    │     │                 │     │     ├── review
    │     │                 │     │     │     ├── ReviewDashboardCreateBatchConfig.java
    │     │                 │     │     │     └── ReviewDashboardScheduler.java
    │     │                 │     │     └── user
    │     │                 │     │         ├── UserDashboardJobConfig.java
    │     │                 │     │         └── UserDashboardScheduler.java
    │     │                 │     ├── controller
    │     │                 │     │     └── DashboardController.java
    │     │                 │     ├── dto
    │     │                 │     │     ├── data
    │     │                 │     │     │     ├── PopularReviewDto.java
    │     │                 │     │     │     └── ReviewReaderItemDto.java
    │     │                 │     │     └── response
    │     │                 │     │         └── CursorPageResponsePopularReviewDto.java
    │     │                 │     ├── entity
    │     │                 │     │     └── Dashboard.java
    │     │                 │     ├── repository
    │     │                 │     │     ├── DashboardRepository.java
    │     │                 │     │     ├── DashboardRepositoryCustom.java
    │     │                 │     │     ├── DashboardRepositoryImpl.java
    │     │                 │     │     ├── UserRepositoryExtension.java
    │     │                 │     │     └── UserRepositoryExtensionImpl.java
    │     │                 │     └── service
    │     │                 │         └── DashboardService.java
    │     │                 ├── like
    │     │                 │     ├── adapter
    │     │                 │     │     ├── in
    │     │                 │     │     │     └── event
    │     │                 │     │     │         └── LikeReviewEventHandler.java
    │     │                 │     │     └── out
    │     │                 │     │         ├── entity
    │     │                 │     │         │     ├── ReviewLike.java
    │     │                 │     │         │     └── ReviewLikeId.java
    │     │                 │     │         └── repository
    │     │                 │     │             └── LikeReviewJpaRepository.java
    │     │                 │     ├── application
    │     │                 │     │     ├── port
    │     │                 │     │     │     ├── in
    │     │                 │     │     │     │     ├── CancelReviewLikeUseCase.java
    │     │                 │     │     │     │     ├── DeleteReviewLikesUseCase.java
    │     │                 │     │     │     │     └── LikeReviewUseCase.java
    │     │                 │     │     │     └── out
    │     │                 │     │     │         └── SaveLikeReviewPort.java
    │     │                 │     │     └── service
    │     │                 │     │         └── LikeReviewCommandService.java
    │     │                 │     └── package-info.java
    │     │                 ├── notification
    │     │                 │     ├── batch
    │     │                 │     │     ├── NotificationDeleteBatchConfig.java
    │     │                 │     │     ├── NotificationReadAllBatchConfig.java
    │     │                 │     │     └── NotificationScheduler.java
    │     │                 │     ├── controller
    │     │                 │     │     └── NotificationController.java
    │     │                 │     ├── entity
    │     │                 │     │     ├── dto
    │     │                 │     │     │     ├── NotificaionCursorDto.java
    │     │                 │     │     │     ├── NotificationDto.java
    │     │                 │     │     │     ├── request
    │     │                 │     │     │     │     ├── NotificationCreateRequest.java
    │     │                 │     │     │     │     └── NotificationUpdateRequest.java
    │     │                 │     │     │     └── response
    │     │                 │     │     │         └── NotificationCursorResponse.java
    │     │                 │     │     └── Notification.java
    │     │                 │     ├── NotificationComponent.java
    │     │                 │     ├── package-info.java
    │     │                 │     ├── repository
    │     │                 │     │     ├── NotificationRepository.java
    │     │                 │     │     ├── NotificationRepositoryDsl.java
    │     │                 │     │     └── NotificationRepositoryDslImpl.java
    │     │                 │     └── service
    │     │                 │         ├── NotificationService.java
    │     │                 │         └── NotificationServiceImpl.java
    │     │                 ├── review
    │     │                 │     ├── adapter
    │     │                 │     │     ├── in
    │     │                 │     │     │     └── web
    │     │                 │     │     │         ├── ReviewApi.java
    │     │                 │     │     │         ├── ReviewController.java
    │     │                 │     │     │         └── ReviewControllerAdvice.java
    │     │                 │     │     └── out
    │     │                 │     │         ├── entity
    │     │                 │     │         │     ├── Review.java
    │     │                 │     │         │     └── ReviewStat.java
    │     │                 │     │         ├── event
    │     │                 │     │         │     └── ReviewEventPublisherAdapter.java
    │     │                 │     │         ├── mapper
    │     │                 │     │         │     ├── ReviewJpaMapper.java
    │     │                 │     │         │     └── ReviewStatJpaMapper.java
    │     │                 │     │         └── repository
    │     │                 │     │             ├── ReviewBookJpaRepository.java
    │     │                 │     │             ├── ReviewCommentJpaRepository.java
    │     │                 │     │             ├── ReviewJpaRepository.java
    │     │                 │     │             ├── ReviewLikeJpaRepository.java
    │     │                 │     │             ├── ReviewNotificationRepository.java
    │     │                 │     │             └── ReviewUserJpaRepository.java
    │     │                 │     ├── application
    │     │                 │     │     ├── dto
    │     │                 │     │     │     ├── request
    │     │                 │     │     │     │     ├── CursorPageRequestReviewDto.java
    │     │                 │     │     │     │     ├── ReviewCreateRequest.java
    │     │                 │     │     │     │     └── ReviewUpdateRequest.java
    │     │                 │     │     │     └── response
    │     │                 │     │     │         ├── CursorPageResponseReviewDto.java
    │     │                 │     │     │         ├── ReviewDto.java
    │     │                 │     │     │         └── ReviewLikeDto.java
    │     │                 │     │     ├── port
    │     │                 │     │     │     ├── in
    │     │                 │     │     │     │     ├── CreateReviewUseCase.java
    │     │                 │     │     │     │     ├── DeleteReviewUseCase.java
    │     │                 │     │     │     │     ├── GetReviewUseCase.java
    │     │                 │     │     │     │     ├── ToggleReviewLikeUseCase.java
    │     │                 │     │     │     │     └── UpdateReviewUseCase.java
    │     │                 │     │     │     └── out
    │     │                 │     │     │         ├── LoadReviewBookPort.java
    │     │                 │     │     │         ├── LoadReviewLikePort.java
    │     │                 │     │     │         ├── LoadReviewPort.java
    │     │                 │     │     │         ├── LoadReviewUserPort.java
    │     │                 │     │     │         ├── ReviewEventPublisher.java
    │     │                 │     │     │         ├── SaveReviewBookPort.java
    │     │                 │     │     │         ├── SaveReviewCommentPort.java
    │     │                 │     │     │         ├── SaveReviewNotificationPort.java
    │     │                 │     │     │         ├── SaveReviewPort.java
    │     │                 │     │     │         └── SaveReviewUserPort.java
    │     │                 │     │     └── service
    │     │                 │     │         ├── ReviewCommandService.java
    │     │                 │     │         └── ReviewQueryService.java
    │     │                 │     ├── domain
    │     │                 │     │     ├── event
    │     │                 │     │     │     ├── ReviewDeletedEvent.java
    │     │                 │     │     │     ├── ReviewLikeCanceledEvent.java
    │     │                 │     │     │     └── ReviewLikedEvent.java
    │     │                 │     │     ├── exception
    │     │                 │     │     │     ├── AlreadyExistsReviewException.java
    │     │                 │     │     │     ├── InvalidReviewContentException.java
    │     │                 │     │     │     ├── InvalidReviewCountException.java
    │     │                 │     │     │     ├── InvalidReviewRatingException.java
    │     │                 │     │     │     ├── ReviewBookNotFoundException.java
    │     │                 │     │     │     ├── ReviewException.java
    │     │                 │     │     │     ├── ReviewNotFoundException.java
    │     │                 │     │     │     ├── ReviewPermissionDeniedException.java
    │     │                 │     │     │     └── ReviewUserNotFoundException.java
    │     │                 │     │     ├── model
    │     │                 │     │     │     ├── ReviewBookDomain.java
    │     │                 │     │     │     ├── ReviewContentDomain.java
    │     │                 │     │     │     ├── ReviewCountDomain.java
    │     │                 │     │     │     ├── ReviewDomain.java
    │     │                 │     │     │     ├── ReviewLikeDomain.java
    │     │                 │     │     │     ├── ReviewLikeNotificationDomain.java
    │     │                 │     │     │     ├── ReviewRatingDomain.java
    │     │                 │     │     │     ├── ReviewStatDomain.java
    │     │                 │     │     │     └── ReviewUserDomain.java
    │     │                 │     │     └── service
    │     │                 │     │         └── ReviewService.java
    │     │                 │     └── package-info.java
    │     │                 ├── Sb06DeokhugamTeam2Application.java
    │     │                 └── user
    │     │                     ├── controller
    │     │                     │     └── UserController.java
    │     │                     ├── dto
    │     │                     │     ├── CursorPageResponse.java
    │     │                     │     ├── PowerUserDto.java
    │     │                     │     ├── UserDto.java
    │     │                     │     ├── UserLoginRequest.java
    │     │                     │     ├── UserRegisterRequest.java
    │     │                     │     └── UserUpdateRequest.java
    │     │                     ├── entity
    │     │                     │     └── User.java
    │     │                     ├── mapper
    │     │                     │     └── UserMapper.java
    │     │                     ├── package-info.java
    │     │                     ├── repository
    │     │                     │     ├── UserQueryRepository.java
    │     │                     │     └── UserRepository.java
    │     │                     ├── scheduler
    │     │                     │     └── UserBatchScheduler.java
    │     │                     └── service
    │     │                         └── UserService.java
    │     └── resources
    │         ├── application-local.yaml
    │         ├── application-prod.yaml
    │         ├── application.yaml
    │         ├── db
    │         │     └── migration
    │         │         ├── V1__init.sql
    │         │         ├── V2__change_table_names.sql
    │         │         ├── V3__remove_redundant_colums.sql
    │         │         ├── V4__add_cascade.sql
    │         │         └── V5__add_version_to_book.sql
    │         ├── logback-spring.xml
    └── test
        ├── java
        │     └── com
        │         └── codeit
        │             └── sb06deokhugamteam2
        │                 ├── book
        │                 │     ├── BookIntegrationTest.java
        │                 │     ├── fixture
        │                 │     │     └── BookFixture.java
        │                 │     └── repository
        │                 │         └── BookVersionWithSQLRestrictionRepositoryTest.java
        │                 ├── comment
        │                 │     └── CommentIntegrationTest.java
        │                 ├── dashboard
        │                 │     ├── DashboardIntegrationTest.java
        │                 │     └── fixture
        │                 │         └── DashboardFixture.java
        │                 ├── notification
        │                 │     └── NotificationTest.java
        │                 ├── review
        │                 │     └── ReviewTest.java
        │                 ├── Sb06DeokhugamTeam2ApplicationTests.java
        │                 └── user
        │                     └── UserControllerIntegrationTest.java
        └── resources
            └── application-test.yaml
```

---

## 구현 홈페이지  
(개발한 홈페이지에 대한 링크 게시)  
[서비스 배포 링크](http://deokhugam-lb-635555306.ap-northeast-2.elb.amazonaws.com/index.html#/login)

---

## 프로젝트 회고록  
(제작한 발표자료 링크 혹은 첨부파일 첨부)
