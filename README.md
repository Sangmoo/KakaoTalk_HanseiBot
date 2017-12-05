# KakaoTalk 자동 API - JAVA + AWS

 * 개발 툴로는 이클립스(Neon Ver.) 사용
 * 이클립스 AWS Dev ToolKit 사용
 * JSON 이용


### 학교 정보를 얻을 수 있는 편의용으로 키워드형식 챗봇 개발

[카카오톡 플러스친구 REST API](https://github.com/plusfriend/auto_reply) 이용

 * 카카오톡 플러스친구 API
 * AWS Lambda
 * AWS API Gateway
 * JSON 이용
 * OpenWeatherMap API로 날씨 이용

### AWS Service 이용

 * 코드 작성 완료 후, 두 프로젝트 다 각각 AWS Lambda에 Upload
 * 대화 시작시로 만들었던 프로젝트는 keyboard 리소스로 이용, API Gateway에서 리소스 이름을 keyboard로 생성, 메소드는 Get타입으로 생성
 * 대화 도중시로 만들었던 프로젝트는 message 리소스로 이용, API Gateway에서 리소스 이름을 message로 생성, 메소드 타입은 POST로 생성
 * message 리소스의 메소드 생성 완료 후 API 배포, 배포된 웹 URL을 이용하여 플러스친구 스마트채팅 API에서 이용
 

##### *완료 모습*
 
 ![완료모습](http://img1.daumcdn.net/thumb/R1920x0/?fname=http%3A%2F%2Fcfile28.uf.tistory.com%2Fimage%2F9955083359FB3F822F0054)
