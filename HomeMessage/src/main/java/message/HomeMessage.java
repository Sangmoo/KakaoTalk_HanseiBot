package message;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;

import org.json.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class HomeMessage implements RequestHandler<JSONObject, JSONObject> {

    @Override
    public JSONObject handleRequest(JSONObject input, Context context) {
    	//입력된 메시지에서 content만 뽑아내기
        // JSONObject js = new JSONObject();
        // JSONArray jsArr = new JSONArray();
        String answer = input.get("content").toString();
        
        
        //내보내야 하는 JSON이 2단계여서 객체도 2개가 필요
        JSONObject jsMes = new JSONObject();
        JSONObject jsMes2 = new JSONObject();
        
        JSONObject jsAns = new JSONObject();
        
        if(answer.contains("안녕"))
        {
        		jsAns.put("text", "Hi!");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("it행정실") || answer.contains("아이티행정"))
        {
        		jsAns.put("text", "이공관 1층에 위치하고 있습니다. \n TEL) 0314505170");
        		jsMes.put("message", jsAns);
        }
        else if(answer.contains("주간식단표") || answer.contains("식단"))
        {
        		jsMes2.put("text", "주간식단표 안내입니다.");
        		jsAns.put("label", "주간 식단표 바로가기");
        		jsAns.put("url", "http://portal.hansei.ac.kr/portal/default/gnb/hanseiTidings/weekMenuTable.page");
        		
        		jsMes2.put("message_button", jsAns);
        		jsMes.put("message", jsMes2);
        }
        else if(answer.contains("교내전화번호") || answer.contains("교내번호"))
        {
        		jsMes2.put("text", "교내전화번호 안내입니다.");
        		jsAns.put("label", "교내 번호 바로가기");
        		jsAns.put("url", "http://www.hansei.ac.kr/portal/default/homepage/collegeLife/facilitiesGuide/introduction_17300.page");
        		
        		jsMes2.put("message_button", jsAns);
        		jsMes.put("message", jsMes2);
        }
        else if(answer.contains("김정곤교수"))
        {
        		jsAns.put("text", "김정곤교수님은 이공관 3층에 위치합니다. \n TEL)010-9100-5174" + "\n");
        		jsMes.put("message", jsAns);
        }
        else if(answer.contains("류대현교수"))
        {
        		jsAns.put("text", "류대현교수님은 이공관 3층에 위치합니다. \n TEL)010-5450-0132" + "\n");
        		jsMes.put("message", jsAns);
        }
        else if(answer.contains("교무처"))
        {
        		jsAns.put("text", "본관 4층에 위치합니다. \n TEL)교무입학처 교무팀 (031-450-5347, 5162)");
        		jsMes.put("message", jsAns);
        }
        else if(answer.contains("날씨"))
        {
	        	jsMes2.put("text", "날씨 바로가기입니다.");
	        	
	        	jsAns.put("label", "날씨 바로가기");
	        	jsAns.put("url", "http://weather.naver.com/rgn/townWetr.nhn?naverRgnCd=02410102");
	        	// message_button
	        	jsMes2.put("message_button", jsAns);
	        	jsMes.put("message", jsMes2);
        }
        else if(answer.contains("공지"))
        {
	        	jsMes2.put("text", "공지사항 바로가기입니다.");
	        	
	        	jsAns.put("label", "공지사항 바로가기");
	        	jsAns.put("url", "http://portal.hansei.ac.kr/portal/default/gnb/hanseiTidings/notice.page");
	        	// message_button
	        	jsMes2.put("message_button", jsAns);
	        	jsMes.put("message", jsMes2);
        }
        else if(answer.contains("캠퍼스안내") || answer.contains("학교지도"))
        {
	        	jsMes2.put("text", "캠퍼스안내 바로가기입니다.");
	        	
	        	jsAns.put("label", "캠퍼스안내 바로가기");
	        	jsAns.put("url", "http://www.hansei.ac.kr/portal/default/homepage/collegeLif/introduction_17100/introduction_17200.page");
	        	// message_button
	        	jsMes2.put("message_button", jsAns);
	        	jsMes.put("message", jsMes2);
        }
        else if(answer.contains("도서관"))
        {
	        	jsMes2.put("text", "도서관 바로가기입니다\n TEL) 0314509829");
	        	
	        	jsAns.put("label", "도서관 바로가기");
	        	jsAns.put("url", "http://lib.hansei.ac.kr");
	        	// message_button
	        	jsMes2.put("message_button", jsAns);
	        	jsMes.put("message", jsMes2);
        }
        else if(answer.contains("수강신청"))
        {
	        	jsMes2.put("text", "수강신청 바로가기입니다.");
	        	
	        	jsAns.put("label", "수강신청 바로가기");
	        	jsAns.put("url", "http://sugangnew.hansei.ac.kr:8808/main.html?fake=Fri%20Oct%2006%202017%2022:02:03%20GMT+0900%20(%B4%EB%C7%D1%B9%CE%B1%B9%20%C7%A5%C1%D8%BD%C3");
	        	// message_button
	        	jsMes2.put("message_button", jsAns);
	        	jsMes.put("message", jsMes2);
        }
        else if(answer.contains("교과과정"))
        {
	        	jsMes2.put("text", "교과과정 바로가기입니다.");
	        	
	        	jsAns.put("label", "교과과정 바로가기");
	        	jsAns.put("url", "http://www.hansei.ac.kr/portal/default/homepage/schoolaffairs/schoolaffairs_54000.page");
	        	// message_button
	        	jsMes2.put("message_button", jsAns);
	        	jsMes.put("message", jsMes2);
        }
        else if(answer.contains("e-class") || answer.contains("이클래스") || answer.contains("eclass"))
        {
	        	jsMes2.put("text", "이클래스 바로가기입니다.");
	        	
	        	jsAns.put("label", "이클래스 바로가기");
	        	jsAns.put("url", "http://eclass.hansei.ac.kr/ilos/main/main_form.acl");
	        	// message_button
	        	jsMes2.put("message_button", jsAns);
	        	jsMes.put("message", jsMes2);
        }
        else if(answer.contains("전자출결"))
        {
	        	jsMes2.put("text", "전자출결 바로가기입니다.");
	        	
	        	jsAns.put("label", "전자출결 바로가기");
	        	jsAns.put("url", "https://attend.hansei.ac.kr/hansei/student/index.html?suser_id=0848dccdb8ec44dc8120244e215be632&suser_name=79C5A445B5A62623DEB4E53FFFD4EBA3344EEAC2EBA64921918EB893A56EA784");
	        	// message_button
	        	jsMes2.put("message_button", jsAns);
	        	jsMes.put("message", jsMes2);
        }
        // 이 외 모두
        else{
        		jsAns.put("text", "알아들을수 없는 말입니다. \n"
        				+ "ex) 공지사항, 이클래스, 교무처");
            jsMes.put("message", jsAns);
        }
        //내보낼 JSON인 jsMes에 if절로 만든 상황에 따른 답변을 넣어줍니다
        return jsMes;
    }

}
