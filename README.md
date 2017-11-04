# KakaoTalk 자동 API - JAVA + AWS

구현 준비:

 * [카카오톡 플러스친구 관리자센터](https://center-pf.kakao.com/login)에서 플러스 친구 생성
 * [AWS](https://aws.amazon.com/ko/)에서 계정 생성
 * 개발 툴로는 이클립스(Neon Ver.) 사용
 * 이클립스내 추가 툴킷으로 AWS Dev ToolKit 사용
 * simple-JSON 이용


### 학교 정보를 얻을 수 있는 편의용으로 키워드형식 챗봇 개발

[카카오톡 플러스친구 REST API](https://github.com/plusfriend/auto_reply) 이용

 * 카카오톡 플러스친구 API Object
 * AWS Lambda
 * AWS API Gateway 
 * JSON 이용

#### 플러스친구 대화 시작시 Button 객체 활용 코드 일부

```javascript
        //JSON 객체 생성
        JSONObject js = new JSONObject();
        JSONArray jsArr = new JSONArray();
        
        
        //버튼에 들어갈 텍스트 넣어주기
        jsArr.add("공지사항");
        jsArr.add("e-class");
        jsArr.add("도서관");
        jsArr.add("수강신청");
        
        //home keyboard 설정해주기
        js.put("type", "buttons");
        js.put("buttons", jsArr);
        
        
        //완성된 JSON 내보내기
        return js;
        
    }
}
```
keyboard 필드의 예제를 활용 [keyboard - button 예제](https://github.com/plusfriend/auto_reply#specification)
* 버튼 3개 단위로 다음 칸으로 자동 이동
* type을 text로 바꿔서 버튼이 아닌 텍스트로 표현 가능
<hr/>

#### 플러스친구 대화 중 message객체 활용 코드 일부
```javascript
		//입력된 메시지에서 content만 뽑아내기
        JSONObject js = new JSONObject();
        JSONArray jsArr = new JSONArray();
        String answer = input.get("content").toString();
        
        
        //내보내야 하는 JSON이 2단계여서 객체도 2개가 필요
        JSONObject jsMes = new JSONObject();
        JSONObject jsMes2 = new JSONObject();
        
        JSONObject jsAns = new JSONObject();
        
        
        //say hello가 입력됐을 때 
        if(answer.contains("say hello")){
            
            jsAns.put("text", "hello");
            jsMes.put("message", jsAns);
            }
        else if(answer.contains("공지사항")){
        	jsMes2.put("text", "공지사항 바로가기입니다.");
            jsAns.put("label", "공지사항 바로가기");
        	jsAns.put("url", "http://portal.hansei.ac.kr/portal/default/gnb/hanseiTidings/notice.page");
        	// message_button
        	jsMes2.put("message_button", jsAns);
        	jsMes.put("message", jsMes2);
        }
        // 이 외 모두
        else {
        	jsAns.put("text", "알아들을수 없는 말입니다.");
            jsMes.put("message", jsAns);
        }
        //내보낼 JSON인 jsMes에 if절로 만든 상황에 따른 답변을 넣어줍니다
        return jsMes;
        }
```
message 필드의 예제를 활용 [message 예제](https://github.com/plusfriend/auto_reply#specification-1)
* 사용자의 답변 키워드를 받아서 짜여진 코드에 맞춰 답변
* message가 아닌 keyboard로 답을 보내어 버튼으로도 답변가능

<hr/>

### AWS Service 이용

 * 코드 작성 완료 후, 두 프로젝트 다 각각 AWS Lambda에 Upload
 * 대화 시작시로 만들었던 프로젝트는 keyboard 리소스로 이용, API Gateway에서 리소스 이름을 keyboard로 생성, 메소드는 Get타입으로 생성
 * 대화 도중시로 만들었던 프로젝트는 message 리소스로 이용, API Gateway에서 리소스 이름을 message로 생성, 메소드 타입은 POST로 생성
 * message 리소스의 메소드 생성 완료 후 API 배포, 배포된 웹 URL을 이용하여 플러스친구 스마트채팅 API에서 이용

*완료 모습*
 
 ![완료모습](http://img1.daumcdn.net/thumb/R1920x0/?fname=http%3A%2F%2Fcfile28.uf.tistory.com%2Fimage%2F9955083359FB3F822F0054)
