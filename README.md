# 📚 덕후감 (Deokhugam)

> 도서 이미지 OCR·ISBN 매칭과 리뷰·댓글을 제공하는 독서 커뮤니티 서비스 \
> 팀 프로젝트에서 도서 논리 삭제, OCR 기반 ISBN 인식, 인기 도서 배치, 낙관적 락과 AWS 배포를 담당하고 구현 이후 PostgreSQL 동시성 검증을 통해 동작과 한계를 분석한 개인 포크

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-green)](https://spring.io/projects/spring-boot)

🌐 [서비스 시연 영상](https://drive.google.com/file/d/1AnXWbv5S4cD82CHmEw06vByZOhNqHBoC/view)  | 
📄 [개발리포트](https://app.notion.com/p/cf9203c86c59824b9d7d01f1f2a74229?source=copy_link)  | 
🧭 [프로젝트 보드](https://github.com/orgs/codeit-team2-intermediate-project/projects/4/views/1)

> 원본 프로젝트: [codeit-team2-intermediate-project/sb06-deokhugam-team2](https://github.com/codeit-team2-intermediate-project/sb06-deokhugam-team2) \
> 팀 프로젝트: 백엔드 6인, 2025.11.21 ~ 12.12 \
> 담당: 도서 논리 삭제·OCR·인기 도서·도서 동시성 제어·AWS 배포 \
> 구현 이후: PostgreSQL 동시성 테스트 결과를 검토해 낙관적 락과 Retry의 동작 및 갱신 유실 한계를 분석

---

## 📌 목차

1. [담당 기능 요약](#-담당-기능-요약)
2. [핵심 구현과 검증](#-핵심-구현과-검증)
3. [기타 담당 구현](#-기타-담당-구현)
4. [기술 스택](#-기술-스택)
5. [로컬 실행 방법](#-로컬-실행-방법)

---

## 🙋 담당 기능 요약

| 영역             | 핵심 기술                                      | 담당·경험                                      |
| -------------- | ------------------------------------------ | ------------------------------------------ |
| 도서 동시성         | JPA `@Version`, Spring Retry               | 수정·논리 삭제 충돌 감지 구현, PostgreSQL 동시성 검증 결과 분석 |
| 인기 도서          | Spring Batch, QueryDSL                     | 공통 대시보드 테이블 설계, 기간별 순위 생성과 커서 조회           |
| 도서 삭제          | bulk UPDATE, `@SQLRestriction`, FK cascade | 도서 논리 삭제 구현, 연관 데이터 전파 방식은 팀 회의로 결정        |
| OCR 기반 ISBN 인식 | OCR SPACE, OkHttp                          | 이미지 OCR 결과 파싱과 ISBN 후보 추출                  |
| AWS 배포         | ECS, RDS, S3, ECR, GitHub Actions          | AWS 자원 구성과 Docker 이미지 배포 흐름 적용             |

---

## 🔍 핵심 구현과 검증

### 1. 낙관적 락 적용과 PostgreSQL 검증 결과

> `@Version`과 Spring Retry로 도서 수정·논리 삭제 충돌을 감지하도록 구현하고, PostgreSQL 검증 결과를 통해 자동 Retry가 사용자 변경까지 보존하지는 않는다는 한계를 확인했습니다.

도서 수정과 논리 삭제가 빈번하지 않을 것으로 예상해, 항상 행 잠금을 점유하는 비관적 락보다 충돌 시점에 이를 감지하는 낙관적 락을 선택했습니다. `Book.version`에 `@Version`을 적용하고, 충돌 시 `ObjectOptimisticLockingFailureException`만 재시도하도록 구성했습니다.

구현 이후 Codex가 구성·실행한 로컬 Docker PostgreSQL 17.6 동시성 테스트의 코드와 결과를 검토했습니다. 테스트에서는 여러 요청이 같은 version을 읽은 뒤 동시에 갱신하도록 실행 시점을 맞추는 `CyclicBarrier`를 사용했습니다.

* Retry 없는 두 트랜잭션은 5회 모두 1건 성공, 1건 낙관적 락 예외가 발생했습니다.
* 10개 동시 수정 요청은 Retry 후 3회 모두 10/10 성공했고 최종 version은 10이었습니다.
* 서로 다른 필드를 수정해도 재시도된 요청이 오래된 전체 상태를 다시 적용해 앞선 변경을 덮을 수 있었습니다.

현재 구현은 기술적인 UPDATE 충돌은 감지하지만 사용자 관점의 갱신 유실까지 막지는 못합니다. 향후에는 전체 수정의 자동 Retry를 제거하거나 제한하고, 클라이언트 version이 다르면 `409 Conflict`를 반환하는 방향이 적합하다고 판단했습니다.

---

### 2. 인기 도서 배치 생성과 커서 조회

> 요청마다 전체 리뷰를 집계하는 대신 Spring Batch로 기간별 순위를 미리 계산해 저장하고, QueryDSL 커서 방식으로 조회했습니다.

인기 도서·인기 리뷰·파워 유저 결과를 함께 관리할 수 있도록 `ranking_type`, `period_type`, `entity_id`, `rank`, `score`, 생성 시각을 가진 공통 `dashboard` 테이블을 직접 설계했습니다.

```text
기간별 리뷰 SQL 집계
  → JdbcCursorItemReader로 순차 조회
  → Processor에서 순위 부여
  → JpaItemWriter로 dashboard 스냅샷 저장
```

* 100개 단위 chunk로 데이터를 처리했습니다.
* DAILY·WEEKLY·MONTHLY·ALL_TIME Job을 자정부터 1분 간격으로 실행했습니다.
* 조회에서는 `limit+1`개를 가져와 다음 페이지 존재 여부를 판단했습니다.
* 동적으로 조합되는 기간·정렬·커서 조건은 QueryDSL로 구현했습니다.

다만 배치 SQL에 논리 삭제된 리뷰를 제외하는 조건이 없고, 같은 날 여러 번 실행한 스냅샷을 구분하지 못합니다. 커서 조건이 rank만 사용되는 점과 응답 생성 과정의 N+1 가능성도 후속 개선 대상으로 확인했습니다.

---

### 3. 도서 논리 삭제와 전파 방식

> 도서 논리 삭제를 담당했으며, 연관 데이터 전파와 물리 삭제 방식은 팀 회의에서 결정해 프로젝트에 적용했습니다.

도서 서비스가 리뷰·댓글 서비스의 내부 로직에 직접 의존하지 않도록, 팀 회의에서 Repository bulk UPDATE를 사용하는 방식으로 결정했습니다. 전파 순서는 하위 데이터부터 처리하도록 댓글 → 리뷰 → 도서 순서로 구성했습니다.

팀에서 정한 방식에 따라 리뷰와 댓글에는 JPQL bulk UPDATE를 적용했고, 제가 담당한 도서는 managed entity의 dirty checking으로 `deleted` 값을 변경했습니다. 이를 통해 도서 UPDATE에는 `@Version` 조건이 포함되어 수정과 삭제가 경합할 때 충돌을 감지할 수 있습니다.

팀에서 결정한 물리 삭제 방식은 논리 삭제된 도서만 native query로 제거하고, 연관 데이터는 PostgreSQL FK `ON DELETE CASCADE`로 삭제하는 구조입니다. 다만 bulk UPDATE는 연관 도메인의 서비스 로직과 이벤트를 우회하며, 리뷰·댓글 전파와 실제 FK cascade는 통합 테스트로 검증하지 못했습니다.

---

## 🧩 기타 담당 구현

### OCR 기반 ISBN 인식

이미지를 OCR SPACE API로 전달하고 `ParsedText`에서 하이픈으로 구분된 ISBN 후보를 추출했습니다. OCR·도서 정보 조회·도서 등록은 독립 API이며, 사용자는 인식 결과를 화면에서 확인하고 수정할 수 있습니다.

```text
Multipart 이미지 수신 → 1 MiB 제한 확인 → OCR SPACE 호출
→ ParsedText 추출 → ISBN 후보 검색 → 하이픈 제거 후 반환
```

현재는 정규식 형태만 확인하므로 ISBN-10·ISBN-13 길이와 check digit 검증이 필요합니다. OCR 오류 응답과 미검출 상황을 고정하는 외부 API mock 테스트도 후속 과제로 남았습니다.

### AWS 배포

RDS·S3·ECR 자원을 직접 생성하고 Amazon ECS의 EC2 시작 유형으로 애플리케이션 배포 환경을 구성했습니다. 제공된 GitHub Actions 예제를 프로젝트 환경에 맞게 수정했습니다.

```text
main push 또는 수동 실행
  → Docker 멀티 스테이지 빌드
  → commit SHA 태그로 ECR push
  → 기존 ECS task definition 조회
  → 새 revision 등록
  → ECS service 갱신
```

ECS에서 RDS로 연결되지 않는 문제는 RDS 보안 그룹에서 ECS task 보안 그룹의 접근을 허용해 해결했습니다. 비용과 자원 제약으로 기존 task의 desired count를 `0`으로 낮춘 뒤 새 task definition으로 `1`을 올렸으며, 현재 방식은 배포 중 일시적인 중단이 발생할 수 있습니다.

---

## 🛠 기술 스택

| 분류                   | 기술                                                     |
| -------------------- | ------------------------------------------------------ |
| Language             | Java 17                                                |
| Framework            | Spring Boot 3.5.8, Spring Batch, Spring Retry          |
| Data Access          | Spring Data JPA, QueryDSL                              |
| Database / Migration | PostgreSQL, H2, Flyway                                 |
| External Integration | OCR SPACE API, OkHttp                                  |
| Infrastructure       | Docker, AWS ECS, RDS, S3, ECR                          |
| CI/CD                | GitHub Actions                                         |
| Other                | MapStruct, Lombok, Springdoc OpenAPI, Datasource Proxy |

---

## 🚀 로컬 실행 방법

### 사전 요구사항

* Java 17
* 외부 연동 사용 시 OCR SPACE·AWS S3·Naver API 인증 정보

### 실행

실행 환경에 필요한 값을 설정한 뒤 애플리케이션을 시작합니다. 환경변수 이름은 `.env.template`과 `application.yaml`에서 확인할 수 있습니다.

```bash
./gradlew bootRun
```

기본 프로필은 `local`이며 H2를 사용합니다. PostgreSQL 환경에서는 datasource URL·사용자·비밀번호와 사용할 프로필을 별도로 설정해야 합니다.

---

## 👤 Author

**이호건**  |  [GitHub](https://github.com/HOGUN00)
