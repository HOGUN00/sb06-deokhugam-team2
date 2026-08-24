# 📚 덕후감 (Deokhugam)

> 도서 이미지 OCR·ISBN 매칭과 리뷰·댓글을 제공하는 독서 커뮤니티 서비스 \
> 팀 프로젝트에서 도서 논리 삭제, OCR 기반 ISBN 인식, 인기 도서 배치, 낙관적 락과 AWS 배포를 담당하고 구현 이후 PostgreSQL 동시성 검증 결과를 검토해 동작과 한계를 분석한 개인 포크

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

## 🏗️ 시스템 아키텍처

> <img width="1800" height="1125" alt="deokhugam-architecture" src="https://github.com/user-attachments/assets/41720487-2889-442f-96b7-7813510e32f3" />


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

> 상세한 기술 선택 이유, 구현 흐름, 동시성 검증 결과와 개선 방향은 개발리포트에 정리했습니다.
> 📄 [덕후감 개발리포트](https://app.notion.com/p/cf9203c86c59824b9d7d01f1f2a74229)

### 1. 낙관적 락과 동시성 제어

도서 수정·논리 삭제 충돌을 감지하기 위해 JPA `@Version`과 Spring Retry를 적용했습니다. PostgreSQL 동시성 테스트 결과를 검토해 전체 상태 자동 재시도가 앞선 변경을 덮을 수 있는 한계를 확인하고, 클라이언트 version 비교와 `409 Conflict` 반환을 개선 방향으로 정리했습니다.

### 2. 인기 도서 배치와 커서 조회

공통 `dashboard` 테이블을 설계하고 Spring Batch로 기간별 인기 도서 순위를 미리 생성·저장한 뒤, QueryDSL 커서 방식으로 조회했습니다. 후속 검토에서 집계 정합성, 스냅샷·커서 안정성, 조회 효율을 개선 대상으로 확인했습니다.

### 3. 도서 논리 삭제와 전파 방식

도서 논리 삭제를 구현했으며, 팀 회의를 통해 댓글 → 리뷰 → 도서 순서의 Repository bulk UPDATE 전파와 PostgreSQL FK cascade 기반 물리 삭제 방식을 적용했습니다. bulk UPDATE가 서비스 로직과 이벤트를 우회하는 점과 전파 과정의 통합 검증 부족을 한계로 남겼습니다.

---

## 🧩 기타 담당 구현

### OCR 기반 ISBN 인식

이미지를 OCR SPACE API로 전달하고 `ParsedText`에서 ISBN 후보를 추출했으며, 사용자가 인식 결과를 확인·수정할 수 있도록 구성했습니다. 현재는 정규식 형태만 확인하므로 ISBN 길이·check digit 검증과 외부 API mock 테스트가 필요합니다.

### AWS 배포

RDS·S3·ECR 자원을 구성하고 Docker 멀티 스테이지 빌드와 GitHub Actions를 이용한 ECR–ECS 배포 흐름을 적용했습니다. ECS–RDS 연결 문제는 보안 그룹 규칙을 조정해 해결했으며, 자원 제약으로 기존 task를 내린 뒤 새 task를 실행해 배포 중 일시적인 중단이 발생할 수 있습니다.

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
