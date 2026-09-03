# 📚 덕후감 (Deokhugam)

> 도서 이미지 OCR·ISBN 매칭과 리뷰·댓글을 제공하는 독서 커뮤니티 서비스 \
> 팀 프로젝트 종료 후 PostgreSQL 동시성 검증을 통해 충돌 처리 정책을 재검토하고 개선한 개인 포크

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-green)](https://spring.io/projects/spring-boot)

🌐 [서비스 시연 영상](https://drive.google.com/file/d/1AnXWbv5S4cD82CHmEw06vByZOhNqHBoC/view)  | 
📄 [개발리포트](https://app.notion.com/p/cf9203c86c59824b9d7d01f1f2a74229?source=copy_link)  | 
🧭 [프로젝트 보드](https://github.com/orgs/codeit-team2-intermediate-project/projects/4/views/1)

> 원본 프로젝트: [codeit-team2-intermediate-project/sb06-deokhugam-team2](https://github.com/codeit-team2-intermediate-project/sb06-deokhugam-team2) \
> 팀 프로젝트: 백엔드 6인, 2025.11.21 ~ 12.12 \
> 구현 이후: PostgreSQL 동시성 테스트로 낙관적 락과 Retry 동작을 검증하고, 충돌 처리 정책을 재검토해 자동 Retry 제거와 `409 Conflict` 응답으로 개선

---

## 🏗️ 시스템 아키텍처

> <img width="1800" height="1125" alt="deokhugam-architecture" src="https://github.com/user-attachments/assets/41720487-2889-442f-96b7-7813510e32f3" />


---

## 🙋 담당 기능 요약

| 담당 영역 | 핵심 기술·구현 |
|---|---|
| 도서 동시성 제어 | JPA `@Version` · Spring Retry · 수정·논리 삭제 충돌 처리 |
| 인기 도서 | Spring Batch · QueryDSL · 기간별 순위 생성·커서 조회 |
| 도서 삭제 | `@SQLRestriction` · bulk UPDATE · PostgreSQL FK cascade |
| OCR 기반 ISBN 인식 | OCR SPACE · OkHttp · ISBN 후보 추출 |
| AWS 배포 | ECS · RDS · S3 · ECR · GitHub Actions |

---

## 🔍 핵심 구현과 검증

> 상세한 기술 선택 이유, 구현 흐름, 동시성 검증 결과와 개선 방향은 개발리포트에 정리했습니다.
> 📄 [덕후감 개발리포트](https://app.notion.com/p/cf9203c86c59824b9d7d01f1f2a74229)

### 1. 낙관적 락과 동시성 제어

도서 수정·논리 삭제 충돌은 JPA `@Version`으로 감지합니다. PostgreSQL 동시성 검증에서 자동 Retry가 앞선 사용자 변경을 덮을 수 있음을 확인해 Retry를 제거하고, 충돌은 `409 Conflict`로 반환하도록 개선했습니다.

### 2. 인기 도서 배치와 커서 조회

공통 `dashboard` 테이블을 설계하고 Spring Batch로 기간별 인기 도서 순위를 미리 생성·저장한 뒤, QueryDSL 커서 방식으로 조회했습니다.

### 3. 도서 논리 삭제와 전파 방식

도서 논리 삭제를 구현했으며, 팀 회의를 통해 댓글 → 리뷰 → 도서 순서의 Repository bulk UPDATE 전파와 PostgreSQL FK cascade 기반 물리 삭제 방식을 적용했습니다.

---

## 🧩 기타 담당 구현

### AWS 배포

RDS·S3·ECR 자원을 구성하고 Docker 멀티 스테이지 빌드와 GitHub Actions를 이용한 ECR–ECS 배포 흐름을 적용했습니다. ECS–RDS 연결 문제는 보안 그룹 규칙을 조정해 해결했습니다.

---

## 🛠 기술 스택

| 분류                   | 기술                                                     |
| -------------------- | ------------------------------------------------------ |
| Language             | Java 17                                                |
| Framework            | Spring Boot 3.5.8, Spring Batch                        |
| Data Access          | Spring Data JPA, QueryDSL                              |
| Database / Migration | PostgreSQL, H2, Flyway                                 |
| External Integration | OCR SPACE API, OkHttp                                  |
| Infrastructure       | Docker, AWS ECS, RDS, S3, ECR                          |
| CI/CD                | GitHub Actions                                         |
| Other                | MapStruct, Lombok                                       |

---

## 🚀 로컬 실행 방법

### 사전 요구사항

- Docker & Docker Compose
- 외부 연동 사용 시 OCR SPACE·AWS S3·Naver API 인증 정보

### 실행

```bash
git clone https://github.com/HOGUN00/sb06-deokhugam-team2.git
cd sb06-deokhugam-team2

# 환경변수 설정
cp .env.template .env

# .env.template을 참고해 필요한 환경변수 입력

# PostgreSQL과 애플리케이션 빌드·실행
docker-compose up -d --build
```

---

## 👤 Author

**이호건**  |  [GitHub](https://github.com/HOGUN00)
