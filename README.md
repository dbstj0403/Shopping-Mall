### 🖤 디지털 하나로 쇼핑몰 과제
- 로그인, 상품 등록 및 삭제, 장바구니 추가, 주문 등의 작업을 할 수 있는 쇼핑몰 프로젝트입니다.

### 👩🏻‍💻 Commit Convention

- [유다시티 컨벤션](https://udacity.github.io/git-styleguide/)

```
feat: 새로운 기능 구현
add: 기능구현까지는 아니지만 새로운 파일이 추가된 경우
del: 기존 코드를 삭제한 경우
fix: 버그, 오류 해결
docs: README나 WIKI 등의 문서 작업
style: 코드가 아닌 스타일 변경을 하는 경우
refactor: 리팩토링 작업
test: 테스트 코드 추가, 테스트 코드 리팩토링
chore: 코드 수정, 내부 파일 수정
```

### ⭐️ 실행 가이드
- 로컬에 hanaro 데이터베이스를 만들어 주세요.
    - `create database hanaro;`
- 데이터베이스에 접속하기 위한 유저를 만들고, 권한을 부여해 주세요.
  ```
  -- 유저 생성
  CREATE USER IF NOT EXISTS 'hanaro'@'%' IDENTIFIED BY '12345678';

  -- 권한 부여
  GRANT ALL PRIVILEGES ON hanarodb.* TO 'hanaro'@'%';

  -- 권한 적용
  FLUSH PRIVILEGES;
  ```
- DB 연결을 위해 앱을 한번 실행하고, test 폴더의 SqlImportTest를 실행해 주세요.
    - 정상적으로 데이터 삽입이 되지 않을 경우 IntelliJ에 hanarodb를 직접 연결하고 hanaro 유저로 접속해 주세요.
- 이후 필요할 경우 Spring Boot 앱을 작동시키기 위해 다시 HanaroApplication을 실행해 주세요.

### ⚙️ 테스트 계정
- `관리자`
    - email : admin@email.com
    - password : 12345678
- `사용자`
    - email : hanaro@email.com
    - password : 12345678
    - hanaro2, hanaro3, hanaro4... 여러 사용자가 존재하며, 비밀번호는 모두 12345678로 같습니다.

### 🗂️ Swagger
- 해당 프로젝트의 다양한 API를 직접 테스트해 볼 수 있습니다.
    - http://localhost:8080/swagger-ui/index.html
- `사용자 / 관리자 API 구분`
    - Swagger 문서 상단 탭을 눌러 사용자 / 관리자 / Actuator API를 각각 확인할 수 있습니다.

      <img width="1167" height="237" alt="Image" src="https://github.com/user-attachments/assets/39a6ed83-4362-499b-8602-9e790e34f850" />


### 🚀 기능 명세
- `인증 / 인가`
    - Spring Security, jwt 토큰을 이용한 회원가입 및 로그인 기능을 제공합니다. 토큰 검증을 통해 권한이 확인되지 않을 경우 401, 403 에러를 띄웁니다.

      <img width="1123" height="432" alt="Image" src="https://github.com/user-attachments/assets/e4a208bb-b590-4d61-80b2-30430171a502" />

- `파일 업로드`
    - 관리자가 상품 등록 시 사진을 함께 등록할 경우 /resources/static/upload 파일에 날짜별 폴더링과 함께 업로드됩니다. 상품 수정 시 아무 이미지도 보내지 않을 경우 기존 이미지 경로가 유지되고, 새로운 이미지를 첨부할 경우 해당 이미지들로 모두 수정됩니다.

      <img width="504" height="325" alt="Image" src="https://github.com/user-attachments/assets/32dacd49-0665-401e-88db-d0bdb3444ec9" />

      <img width="868" height="313" alt="Image" src="https://github.com/user-attachments/assets/cc5ac536-1be7-45aa-a824-a37ec18bfd94" />

- `스케쥴링`
    - 결제 완료, 배송 준비, 배송 중, 배송 완료 상태를 enum으로 관리하고, 생성된 주문을 스케쥴러를 통해 각각의 상태를 순차적으로 5분, 15분, 1시간 간격으로 변환합니다.
      ```
      public enum OrderStatus {
      PAYMENT_COMPLETED,   // 결제 완료
      PREPARING_SHIPMENT,  // 배송 준비
      IN_TRANSIT,          // 배송 중
      DELIVERED           // 배송 완료
      }
      ```

      <img width="904" height="152" alt="Image" src="https://github.com/user-attachments/assets/753ea84a-c4b4-4998-891e-9bf83a9f7dc6" />

- `배치 Job`
    - 매일 자정에 일별 매출 통계를 집계하며, 전날 주문들의 매출 통계를 작성해 daily_sales_summary 테이블에 저장합니다. Column들은 날짜, 총 매출, 총 주문 수, 집계 날짜로 이루어져 있습니다. (사진은 테스트용으로 임의의 시간에 매출을 집계)

      <img width="995" height="45" alt="Image" src="https://github.com/user-attachments/assets/ada337d7-1c97-40a1-b1db-620d717e6831" />

      <img width="243" height="53" alt="Image" src="https://github.com/user-attachments/assets/42dcbc78-b1c0-4bda-a28b-1d780f41dce7" />

    - 매일 자정 일별 매출 통계를 집계함과 동시에, 상품별 매출 통계 또한 product_daily_sales 테이블에 저장합니다. Column은 날짜, 상품 아이디, 총 매출 금액, 집계 날짜로 이루어져 있습니다.

      <img width="552" height="164" alt="Image" src="https://github.com/user-attachments/assets/12a2e557-219e-4a18-a0cb-0bcd493aca02" />


- `로깅`
    - 상품 관리, 주문 관련 이벤트가 발생할 때마다 로그를 발생시킵니다.
    - 주문 관련 로그는 business_order 파일에, 상품 관련 로그는 business_product 파일에 기록됩니다. 당일의 로그는 business_order, business_product 파일에, 하루가 지나면 전날의 로그는 롤링되어 business_order.2025-08-10 양식으로 로그 파일이 분리됩니다.

      <img width="368" height="105" alt="Image" src="https://github.com/user-attachments/assets/5a766572-0c29-432b-9c32-5145d2e55206" />
    - business_order 로그 파일 예시
    - 로그 발생 플로우
      ```
      주문 시작 → INFO
      장바구니 비었으면 → WARN + 주문 중단
      재고 부족이면 → WARN + 주문 중단
      정상 완료 시 → INFO (주문 성공)
      이어서 장바구니 비움 → INFO
      ```

      <img width="856" height="128" alt="Image" src="https://github.com/user-attachments/assets/4e9ef5de-1e64-4617-8aa0-1c6ac9288898" />

    - business_product 로그 파일 예시
    - 로그 발생 플로우
      ```
      상품 생성 완료 → INFO
      상품 삭제 완료 → INFO
      상품 정보 수정 완료 → INFO (변경 전/후 값 + 이미지 교체 여부 포함)
      상품 이미지 모두 삭제 완료 -> INFO
      재고 수정 완료 → INFO
      ```

      <img width="959" height="191" alt="Image" src="https://github.com/user-attachments/assets/360563ad-5a16-46ef-87e0-91ff150a9a6e" />

- `Validation`
    - 회원가입, 상품 등록 시 입력 데이터의 유효성을 검증합니다.

      ```
          @NotBlank(message = "이메일은 필수입니다.")
          @Email(message = "올바른 이메일 형식이 아닙니다.")
          @Schema(description = "이메일", example = "hanaro@email.com")
          private String email;

          @NotBlank(message = "비밀번호는 필수입니다.")
          @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
          @Schema(description = "비밀번호", example = "12345678")
          private String password;

          @NotBlank(message = "닉네임은 필수입니다.")
          @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야 합니다.")
          @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "닉네임은 한글, 영문, 숫자만 사용할 수 있습니다.")
          @Schema(description = "이름", example = "별돌이")
          private String name;
      ```
    - DTO 레벨에서 @Valid 어노테이션을 통해 요청 본문이 DTO로 바인딩될 때 각 필드에 선언된 검증 어노테이션이 자동으로 실행됩니다. 서비스 로직 중 옳지 못한 요청일 경우 CustomException을 발생시켜 예외 처리를 진행합니다. 또한, 모든 예외는 GlobalExceptionHandler에서 상태 코드와 메시지를 통일된 형식으로 응답합니다.

- `Actuator 엔드 포인트`
    - 시스템 health, bean, env, metric 정보를 확인하기 위한 actuator 엔드 포인트를 Swagger 문서에서 확인할 수 있습니다.

      <img width="1154" height="616" alt="Image" src="https://github.com/user-attachments/assets/fda69919-05ca-4744-9c40-b1498cc14ba6" />


### ✍🏻 ERD
<img width="858" height="686" alt="Image" src="https://github.com/user-attachments/assets/48ba2f76-61c4-4958-8668-05c16fee5f01" />
