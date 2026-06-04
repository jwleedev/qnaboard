# 📋 QnA Board 프로젝트 

<br>

## 📝 프로젝트 소개
이 프로젝트는 **백엔드 아키텍처 설계와 비즈니스 로직 구현에 온전히 집중**하기 위해 개발된 렌더링(SSR) 방식의 QnA 게시판입니다. 
프론트엔드 UI(HTML, Bootstrap)는 템플릿을 기반으로 빠르게 구축하고, 그 기반 위에서 Thymeleaf를 활용한 동적 데이터 바인딩과 상태 유지 로직을 직접 설계 및 구현했습니다.

단순한 기능 구현을 넘어, **계층 간 결합도 완화**, **권한 탈취 방어를 위한 2차 검증(인가)**, **검증 및 예외 처리 최적화** 등 백엔드 개발자로서의 기술적 의사결정과 트러블슈팅 경험을 깊게 고민하며 담아냈습니다.

* **주요 기술 스택**: Spring Boot, MyBatis, MySQL, Thymeleaf, Spring Security

<br>

## ✨ 프로젝트 주요 기능

### [회원 및 인증]
* **회원가입 및 로그인**: Spring Security와 BCrypt 암호화를 활용한 안전한 회원 정보 관리 및 세션(HttpSession) 기반 인증 로직 처리
* **유효성 검사 및 예외 처리**: `@Valid`를 이용한 폼 데이터 검증 및 이메일 중복 가입 차단, 에러 메시지(Object/Field Error)의 UI 피드백 제공
* **접근 제어 및 흐름 개선**: Spring Interceptor를 활용한 미인증 사용자 접근 제어 및 로그인 후 기존에 보던 상세 페이지로 즉시 복귀하는 동적 리다이렉트 처리

### [게시판]
* **게시글 CRUD**: 게시글 작성, 상세 조회, 수정, 삭제 로직 전체 구현 (PRG 패턴 적용을 통한 중복 등록 방지)
* **다중 검색 및 페이징**: MyBatis 동적 쿼리(`<if>`)를 활용하여 다중 조건(제목, 작성자) 검색 기능 구현 및 페이징 이동 후에도 검색어 상태 유지 처리
* **조회수 최적화**: 매번 DB를 조회하는 서버 부하를 줄이기 위해, 쿠키(Cookie)를 1차 방어선으로 활용한 서버 사이드 조회수 중복 증가 방지 로직 적용
* **보안 및 권한 제어**: 악의적인 API 접근을 대비해 서버 단에서 세션 정보와 작성자 ID를 교차 검증하는 인가(Authorization) 제어 기능 구현
* **계층 분리 설계**: 웹 계층 폼 데이터 객체와 도메인 계층 DTO를 철저히 분리하여, 모듈 간 결합도를 낮추고 비즈니스 검증 로직의 유지보수성을 극대화

### [댓글]
* **댓글 작성 및 삭제**: 로그인 사용자 전용 기능으로, 본인이 작성한 댓글에만 삭제 권한 부여
* **예외 피드백**: 권한이 없는 비정상적인 삭제 요청 발생 시, 서버에서 예외를 캐치하여 알림창(alert)으로 피드백을 제공하고 안전한 페이지로 리다이렉트

<br>

## 개발 환경

###[Backend]
* **IDE(통합개발 환경)**: IntelliJ IDEA Community
* **개발 언어**: Java 25
* **프레임워크**: Spring Boot 4.0.6
* **Build**: Gradle
* **Security**:Spring Security

###[DataBase]
* **DB**: MySQL 8.0.45, MySQL Workbench 8.0 CE 
* **DB 접근 기술**: MyBatis

###[Frontend]
* **Template Engine**: Thymeleaf
* **UI Framework**: Bootstrap
* **Markup / Style**: HTML / CSS

<br><br>

## 🏗️ 구조 및 설계

### 1. API 설계

**메인 화면 API (HomeController)**

| Function | HTTP Method | URL | Return View/ Redirect |
|---|---|---|---|
| 메인 화면 조회 | `GET` | `/` | `/posts` (게시글 목록)으로 리다이렉트 |
<br>

**회원 관리 API (Member Controller)**

| Function | HTTP Method | URL | Return View/ Redirect |
|---|---|---|---|
| 회원가입 폼 조회 | `GET` | `/signup` | `member/signup` (회원가입 뷰) |
| 회원가입 | `POST` | `/signup` | **성공**: `redirect:/posts`<br>(가입 완료 후 게시글 목록으로 리다이렉트)<br><br>**실패**: `members/signup`<br>(검증 오류 또는 이메일 중복 시 폼 다시 반환) |
<br>

**로그인 API (LoginController)**

| Function | HTTP Method | URL | Return View/ Redirect |
|---|---|---|---|
| 로그인 폼 조회 | `GET` | `/login` | **미로그인**: `login/login` (로그인 뷰)<br><br>**기로그인**: `redirect:/`<br>(이미 로그인된 상태면 메인으로 리다이렉트) |
| 로그인 | `POST` | `/login` | **성공**: `redirect:{redirectURL}`<br>(이전 요청 경로로 리다이렉트)<br><br>**실패**: `login/login`<br>(검증 오류 또는 회원정보 불일치 시 폼 다시 반환) |
| 로그아웃 | `POST` | `/logout` | `redirect:/` (세션 만료 처리 후 메인으로 리다이렉트) |
<br>

**게시판 관련 API (PostController)**

| Function | HTTP Method | URL | Return View/ Redirect |
|---|---|---|---|
| 게시글 목록 조회 | `GET` | `/posts` | `posts/list` (게시글 페이징 목록 뷰) |
| 게시글 작성 폼 조회 | `GET` | `/posts/add` | `posts/write` (게시글 작성 뷰) |
| 게시글 등록 | `POST` | `/posts/add` | **성공**: `redirect:/posts/{postId}`<br>(게시글 상세로 리다이렉트)<br><br>**실패**: `posts/write`<br>(검증 오류 시 폼 다시 반환) |
| 게시글 상세 조회 | `GET` | `/posts/{postId}` | `posts/detail` (게시글 상세 뷰) |
| 게시글 수정 폼 조회 | `GET` | `/posts/{postId}/edit` | **성공**: `posts/write` (게시글 수정 뷰)<br><br>**실패**: `redirect:/posts/{postId}`<br>(수정 권한 없을 시 게시글 상세로 리다이렉트) |
| 게시글 수정 | `POST` | `/posts/{postId}/edit` | **성공**: `redirect:/posts/{postId}`<br>(게시글 상세로 리다이렉트)<br><br>**실패**: `posts/write`<br>(검증 오류 시 폼 다시 반환) |
| 게시글 삭제 | `POST` | `/posts/{postId}/delete` | **성공**: `redirect:/` (메인 화면으로 리다이렉트)<br><br>**실패**: `redirect:/posts/{postId}`<br>(게시글 상세로 리다이렉트) |
<br>

**댓글 API (CommentController)**

| Function | HTTP Method | URL | Return View/ Redirect |
|---|---|---|---|
| 댓글 등록 | `POST` | `comments/add` | **성공**: `redirect:/posts/{postId}`<br>(댓글 작성한 게시글 상세로 리다이렉트)<br><br>**실패**: `redirect:/posts/{postId}`<br>(검증 오류 시 리다이렉트) |
| 댓글 삭제 | `POST` | `comments/{commentId}/delete` | **성공**: `redirect:/posts/{postId}`<br>(해당 게시글 상세로 리다이렉트)<br><br>**실패**: `redirect:/posts/{postId}`<br>(댓글 삭제 권한이 없을 시 오류 메시지와 함께 해당 게시글 상세로 리다이렉트) |
<br>

### 2. DB 설계

**Post**

| 컬럼명 | 데이터 타입 | 조건 | 설명 |
|---|---|---|---|
| `post_id` | `INT` | AI, PK | 게시글 고유 식별자 |
| `member_id` | `INT` | FK, NOT NULL | 게시글 작성자 회원 고유 식별자 |
| `title` | `VARCHAR(255)` | NOT NULL | 게시글 제목 |
| `content` | `TEXT` | NOT NULL | 게시글 본문 내용 |
| `created_at` | `DATETIME` | DEFAULT CURRENT_TIMESTAMP<br>NOT NULL | 게시글 최초 작성일 |
| `updated_at` | `DATETIME` | DEFAULT NOW() ON UPDATE NOW()<br>NOT NULL | 게시글 최종 수정일 |
| `view_count` | `INT` | DEFAULT 0, NOT NULL | 게시글 조회수 |
<br>

**Member**

| 컬럼명 | 데이터 타입 | 조건 | 설명 |
|---|---|---|---|
| `member_id` | `INT` | AI, PK | 회원 고유 식별자 |
| `name` | `VARCHAR(50)` | NOT NULL | 회원 이름 |
| `email` | `VARCHAR(100)` | NOT NULL, UNIQUE | 회원 이메일 |
| `password` | `VARCHAR(255)` | NOT NULL | 비밀번호 |
| `joind_date` | `DATETIME` | DEFAULT CURRENT_TIMESTAMP<br>NOT NULL | 회원가입 일시 |
<br>

**Comment**

| 컬럼명 | 데이터 타입 | 조건 | 설명 |
|---|---|---|---|
| `comment_id` | `INT` | AI, PK | 댓글 고유 식별자 |
| `post_id` | `INT` | FK, NOT NULL | 댓글이 달린 게시글 고유 식별자 |
| `member_id` | `INT` | FK, NOT NULL | 댓글 작성자 회원 고유 식별자 |
| `content` | `TEXT` | NOT NULL | 댓글 내용 |
| `created_at` | `DATETIME` | DEFAULT CURRENT_TIMESTAMP<br>NOT NULL | 댓글 최초 작성일 |
<br>

## 실행 화면 (구현)

<details>
<summary><b>메인화면 사진 GIF</b></summary>
</details>

<details>
<summary><b>로그인 로그아웃 GIF</b></summary>

* Spring Security와 BCrypt 암호화를 적용한 안전한 로그인 처리
* 로그인 실패 시 에러 메시지(Object Error)를 화면에 표시하여 사용자 피드백 제공
</details>

<details>
<summary><b>회원가입 GIF</b></summary>

* `@Valid`를 이용한 회원가입 폼(Form) 검증
* 입력 규칙 미준수 시 필드 에러(Field Error)를 화면에 표시하여 사용자 피드백 제공
* 이메일 중복 가입 차단 및 에러 메시지(Object Error) 표시
</details>

<details>
<summary><b>게시판 검색 및 페이징 GIF</b></summary>

* MyBatis 동적 쿼리(`<if>`)를 활용한 다중 조건(제목, 작성자) 검색 및 최신 글(글 번호) 기준 정렬
* 게시글 리스트 페이징 처리
* 현재 페이지 번호 하이라이트 및 한계 페이지(처음/끝) 이동 버튼 비활성화
</details>

<details>
<summary><b>게시글 작성 GIF</b></summary>

* Validator를 이용한 게시글저장(PostSaveForm) 폼 객체 검증
* 입력 규칙 미준수 시 필드 에러(Field Error)를 화면에 표시하여 사용자 피드백 제공
* 게시글 저장 성공 시 새로 등록된 글의 상세 페이지로 즉시 이동하는 PRG(Post-Redirect-Get) 패턴 적용으로 중복 등록 방지
* 취소 버튼 클릭 시 게시글 리스트 페이지로 이동
</details>

<details>
<summary><b>게시글 상세 페이지 GIF</b></summary>

* 쿠키(Cookie)를 활용한 조회수 중복 증가 방지
* **게시글 제어 및 화면 흐름**
  * 수정 버튼 클릭 시 수정 권한을 검증하여 작성자 본인만 수정 페이지로 이동
  * 삭제 버튼 클릭 시 삭제 권한을 검증하여 작성자 본인만 게시글 삭제 후 게시글 리스트 페이지로 이동
</details>

<details>
<summary><b>게시글 수정 GIF</b></summary>

* Validator를 이용한 게시글수정폼 객체(PostUpdateForm)검증
* 입력 규칙 미준수 시 필드 에러(Field Error)를 화면에 표시하여 사용자 피드백 제공
* 게시글 수정 성공 시 수정된 글의 상세 페이지로 즉시 이동하는 PRG(Post-Redirect-Get) 패턴 적용으로 중복 등록 방지
* 글 작성과 수정이 하나의 HTML 템플릿을 공유하지만 수정 중 '취소' 버튼을 누를 경우 게시글의 상세 페이지로 이동
</details>

<details>
<summary><b>게시글 수정 실패 GIF</b></summary>

* 세션 스토리지의 사용자 객체와 DB의 작성자 식별자(ID)를 비교하는 인가(Authorization) 로직을 통해 안전한 수정 권한 체크
* 게시글 수정 실패 시 에러 메시지를 띄운 후 해당 게시글의 상세 페이지로 이동
</details>

<details>
<summary><b>게시글 삭제 GIF</b></summary>

* 세션 스토리지의 사용자 객체와 DB의 작성자 식별자(ID)를 비교하는 인가(Authorization) 로직을 통해 안전한 삭제 권한 체크
* 게시글 삭제 실패 시 에러 메시지를 띄운 후 해당 게시글의 상세 페이지로 이동
</details>

<details>
<summary><b>댓글 작성/삭제 GIF</b></summary>

* 로그인한 사용자만 댓글을 작성할 수 있도록 제한
* 세션 스토리지의 사용자 객체와 DB의 작성자 식별자(ID)를 비교하는 인가(Authorization) 로직을 통해 안전한 삭제 권한 체크
* 댓글 삭제 실패 시 에러 메시지를 띄운 후 해당 게시글의 상세 페이지로 이동
</details>
