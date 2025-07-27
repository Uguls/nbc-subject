# NBC Subject - Spring Security JWT 인증 시스템

Spring Boot 3.4와 Spring Security를 활용한 JWT 기반 사용자 인증 및 권한 관리 시스템입니다.

## 📋 프로젝트 개요

이 프로젝트는 JWT 토큰을 사용한 Stateless 인증 시스템과 역할 기반 접근 제어를 구현한 REST API 서버입니다.

### 주요 기능
- 🔐 **사용자 회원가입/로그인** - JWT 토큰 기반 인증
- 👥 **사용자 권한 관리** - USER/ADMIN 역할 기반 접근 제어  
- 🛡️ **Spring Security 통합** - @PreAuthorize를 활용한 메서드 레벨 보안
- 📚 **API 문서화** - Swagger UI를 통한 상세한 API 명세

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| **Framework** | Spring Boot 3.4.4 |
| **Language** | Java 17 |
| **Build Tool** | Gradle |
| **Database** | H2 (개발), MySQL (운영) |
| **Security** | Spring Security 6.4.4 |
| **Authentication** | JWT (jjwt 0.12.6) |
| **Password Encoding** | BCrypt |
| **ORM** | Spring Data JPA |
| **Documentation** | SpringDoc OpenAPI 3 |
| **Testing** | JUnit 5, Mockito |

## 🏗 아키텍처

```
src/main/java/nbc/nbcsubject/
├── common/
│   ├── config/                 # Spring 설정
│   │   └── SecurityConfig.java
│   ├── security/               # JWT 보안 관련
│   │   ├── jwt/
│   │   └── filter/
│   ├── exception/              # 전역 예외 처리
│   └── response/               # 공통 응답 형식
├── domain/
│   ├── user/                   # 사용자 도메인
│   │   ├── entity/
│   │   ├── repository/
│   │   ├── service/
│   │   ├── controller/
│   │   └── dto/
│   └── admin/                  # 관리자 도메인
│       ├── service/
│       ├── controller/
│       └── dto/
└── NbcSubjectApplication.java
```

## 🚀 실행 방법

### 사전 요구사항
- Java 17 이상
- Gradle 8.0 이상

### 로컬 실행
```bash
# 1. 프로젝트 클론
git clone [GitHub Repository URL]
cd nbc-subject

# 2. 의존성 설치 및 빌드
./gradlew build

# 3. 애플리케이션 실행
./gradlew bootRun
```

### 설정 파일
`src/main/resources/application.yaml`에서 데이터베이스 및 JWT 설정을 확인할 수 있습니다.

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create
  datasource:
    url: jdbc:h2:mem:nbc_subject
    username: sa
    password:

jwt:
  secret: nbcSubjectApllication123...
  accessToken-expiration-time: 3600000  # 1시간
  refreshToken-expiration-time: 1209600000  # 14일
```

## 📱 API 엔드포인트

### 🌐 실행 중인 서비스
- **API Base URL**: `http://3.27.205.11:8080`
- **Swagger UI**: http://3.27.205.11:8080/swagger-ui/index.html

### 👤 사용자 인증 API

#### 회원가입
```http
POST /users/signup
Content-Type: application/json

{
  "username": "testuser",
  "nickname": "테스트유저", 
  "password": "password123"
}
```

**응답 예시:**
```json
{
  "userId": 1,
  "username": "testuser",
  "nickname": "테스트유저",
  "roles": ["ROLE_USER"]
}
```

#### 로그인
```http
POST /users/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

**응답 예시:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciI..."
}
```

### 🔐 관리자 API (ADMIN 권한 필요)

#### 사용자 권한 승격
```http
PATCH /admin/users/{userId}/roles
Authorization: Bearer <JWT_TOKEN>
```

**응답 예시:**
```json
{
  "username": "testuser",
  "nickname": "테스트유저",
  "roles": ["ROLE_ADMIN"]
}
```

## 🛡️ 보안 설계

### JWT 토큰 구조
- **Header**: 토큰 타입 및 알고리즘 (HS512)
- **Payload**: 사용자 정보 (username, roles, 만료시간)
- **Signature**: 서버 시크릿 키로 생성된 서명

### 권한 체계
- **ROLE_USER**: 일반 사용자 (기본 권한)
- **ROLE_ADMIN**: 관리자 (사용자 권한 승격 가능)

### 보안 기능
- 🔒 **Stateless 인증**: 세션 사용 안함
- 🛡️ **CSRF 비활성화**: REST API 특성상 불필요
- 🔐 **BCrypt 패스워드 암호화**
- ⏰ **토큰 만료 관리**: Access Token 1시간

## 🧪 테스트

### 테스트 실행
```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests UserServiceTest

# 특정 테스트 메서드 실행  
./gradlew test --tests UserServiceTest.signUp_Success
```

### 테스트 커버리지
- **단위 테스트**: Service Layer Mock 기반 테스트
- **통합 테스트**: Controller + Security 통합 테스트
- **Repository 테스트**: JPA Repository 동작 검증

## 📖 API 문서

### Swagger UI
프로젝트 실행 후 다음 URL에서 상세한 API 문서를 확인할 수 있습니다:

**🔗 http://3.27.205.11:8080/swagger-ui/index.html**

Swagger UI에서는 다음 기능을 제공합니다:
- 📋 전체 API 엔드포인트 목록
- 🔍 요청/응답 스키마 상세 정보
- 🧪 API 직접 테스트 기능
- 🔐 JWT 토큰 인증 테스트

## 🌟 주요 특징

### 1. Spring Security 통합
```java
@PreAuthorize("hasRole('ADMIN')")
@PatchMapping("/admin/users/{userId}/roles")
public ResponseEntity<?> updateUserRole(@PathVariable Long userId) {
    // 관리자만 접근 가능한 엔드포인트
}
```

### 2. JWT 기반 인증
- 토큰 생성/검증 로직 구현
- Custom JWT Authentication Filter
- 토큰 만료 처리

### 3. 예외 처리
- Global Exception Handler
- 커스텀 예외 클래스
- 일관된 에러 응답 형식

### 4. 도메인 중심 설계
- 각 도메인별 패키지 분리
- Repository, Service, Controller 레이어 구분
- DTO를 통한 데이터 전송

## 🔧 개발 도구

### 데이터베이스 접근
H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:nbc_subject`
- Username: `sa`
- Password: (빈 값)

### 로그 설정
```yaml
logging.level:
  org.hibernate.SQL: debug  # SQL 쿼리 로그
  org.springframework.security: info  # Security 로그
```

**🚀 개발 환경**: Spring Boot 3.4.4 + Java 17
