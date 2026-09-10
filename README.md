# 📚 덕후감 (Deokhugam)

> 도서 이미지 OCR·ISBN 매칭과 리뷰·댓글·인기 순위를 제공하는 독서 커뮤니티 서비스<br>
> 6인 백엔드 팀에서 **도서 동시성 제어, 인기 도서 Batch·조회, AWS 배포**를 담당했고, 프로젝트 종료 후 **PostgreSQL 동시성 테스트로 기존 충돌 처리 정책을 재검증해 개선**했습니다.

🌐 [서비스 시연 영상](https://drive.google.com/file/d/1AnXWbv5S4cD82CHmEw06vByZOhNqHBoC/view) |
📄 [기술 문서](https://app.notion.com/p/cf9203c86c59824b9d7d01f1f2a74229?source=copy_link) |
🧭 [프로젝트 보드](https://github.com/orgs/codeit-team2-intermediate-project/projects/4/views/1)

> 팀 프로젝트: 백엔드 6인, 2025.11.21 ~ 12.12<br>
> 원본 프로젝트: [codeit-team2-intermediate-project/sb06-deokhugam-team2](https://github.com/codeit-team2-intermediate-project/sb06-deokhugam-team2)

---

## 🙋 담당 영역

| 담당 영역 | 주요 경험 |
| --- | --- |
| 도서 수정·삭제 | JPA `@Version` 기반 충돌 감지 · 논리 삭제 |
| 인기 도서 | Spring Batch · 기간별 순위 스냅샷 · 커서 페이지네이션 |
| OCR | 도서 이미지에서 ISBN 추출 |
| AWS 배포 | RDS · S3 · ECR · ECS · GitHub Actions |

---

## 🔍 핵심 구현 및 개선

### 1. 낙관적 락 Retry 정책 재검토

**기존 정책**<br>
JPA `@Version`으로 수정·삭제 충돌 감지<br>
→ 충돌 발생 시 Spring Retry로 자동 재시도

**검증**<br>
PostgreSQL 동시성 테스트<br>
→ 두 요청이 동일 version 조회<br>
→ 한 요청 Commit<br>
→ 다른 요청에서 낙관적 락 충돌<br>
→ Retry가 최신 version 재조회<br>
→ 기존 수정 요청 전체 재실행

**문제**<br>
전체 수정 요청 재적용<br>
→ 먼저 반영된 사용자의 변경을 덮을 가능성

**개선**<br>
자동 Retry 제거<br>
→ `409 Conflict` / `BOOK_STATE_CONFLICT`

**결과**<br>
동시 수정 · 수정 후 삭제 · 삭제 후 수정<br>
→ **늦게 반영되는 요청을 충돌 처리**

**판단**<br>
기술의 정상 동작 ≠ 서비스 정책의 적절성<br>
→ 충돌을 자동 재시도로 숨기지 않고 `409 Conflict`로 명시

### 2. 인기 도서 Batch + 커서 기반 조회

**요구사항**<br>
기간별 인기 도서 순위 제공

**설계**<br>
요청마다 전체 리뷰 재집계·정렬 방지<br>
→ Spring Batch에서 기간별 순위 사전 계산<br>
→ `dashboard` 테이블에 스냅샷 저장

**조회**<br>
QueryDSL로 기간·정렬·커서 조건 조합<br>
→ 마지막 조회 지점 이후 데이터 조회

**확인**<br>
Batch Job 완료<br>
→ 인기 도서 스냅샷 생성<br>
→ 커서 기반 페이지 조회 흐름 확인

**판단**<br>
실시간 집계 요구 없음<br>
→ **집계 시점과 조회 시점 분리**

---

## ☁️ AWS 배포 경험

RDS · S3 · ECR 자원 구성<br>
→ ECS EC2 시작 유형으로 애플리케이션 배포<br>
→ GitHub Actions 기반 Docker Build · ECR Push · ECS 갱신

ECS → RDS 연결 문제<br>
→ RDS Security Group에서 ECS Task Security Group 접근 허용<br>
→ 연결 정상화

---

## 🏗️ 시스템 아키텍처

<img width="1800" height="1125" alt="deokhugam-architecture" src="https://github.com/user-attachments/assets/41720487-2889-442f-96b7-7813510e32f3" />

도서 삭제 전파 방식, 동시성 테스트 세부 시나리오와 구현 근거는 [기술 문서](https://app.notion.com/p/cf9203c86c59824b9d7d01f1f2a74229?source=copy_link)에 정리했습니다.

---

## 🛠 기술 스택

| 분류 | 기술 |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 3.5.8, Spring Batch |
| Data Access | Spring Data JPA, QueryDSL |
| Database | PostgreSQL |
| External Integration | OCR SPACE API |
| Infrastructure | Docker, AWS ECS, RDS, S3, ECR |
| CI/CD | GitHub Actions |

---

## 🚀 로컬 실행

```bash
git clone https://github.com/HOGUN00/sb06-deokhugam-team2.git
cd sb06-deokhugam-team2

cp .env.template .env
docker-compose up -d --build
```

---

## 👤 Author

**이호건** | [GitHub](https://github.com/HOGUN00)
