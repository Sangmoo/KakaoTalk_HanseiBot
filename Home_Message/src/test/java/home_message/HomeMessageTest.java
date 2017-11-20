package home_message;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import org.json.*;
import org.json.simple.*;
import org.json.simple.parser.*;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HomeMessageTest {

    private static JSONObject input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
    	/*        수정해야 할 부분 ↓        */
        // 공지사항 버튼이 입력된 상황 설정
        
        JSONObject js = new JSONObject();
        
        js.put("content", "날씨");
        
        input = js;
 
        /*        수정해야 할 부분 ↑        */
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testHomeKeyboard2() {
        HomeMessage handler = new HomeMessage();
        Context ctx = createContext();

        JSONObject output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println(output.toString());
        }
    }
}
