package home_keyboard;
 
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
        jsArr.add("신학부");
        jsArr.add("사회과학부");
        ///////////////////////
        jsArr.add("국제언어학부");
        jsArr.add("간호복지학부");
        jsArr.add("예술학부");
        //////////////////////
        jsArr.add("디자인학부");
        jsArr.add("행정처");
        jsArr.add("기타 정보");
        
        //home keyboard 설정해주기
        js.put("type", "buttons");
        js.put("buttons", jsArr);
        
        
        //완성된 JSON 내보내기
        return js;
        
    }

}
