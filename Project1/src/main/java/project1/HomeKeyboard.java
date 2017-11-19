package project1;
 
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.json.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class HomeKeyboard implements RequestHandler<JSONObject, JSONObject> {
 
	public JSONObject handleRequest(JSONObject input, Context context) {
        
        //JSON 객체 생성
        JSONObject js = new JSONObject();
        JSONArray jsArr = new JSONArray();
        
        
        //버튼에 들어갈 텍스트 넣어주기
        jsArr.add("IT학부");
        jsArr.add("e-class");
        jsArr.add("도서관");
        jsArr.add("기타 정보");
        
        //home keyboard 설정해주기
        js.put("type", "buttons");
        js.put("buttons", jsArr);
        
        
        //완성된 JSON 내보내기
        return js;
        
    }

}
