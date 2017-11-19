package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;
// 날씨 URL 받아오기
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;



import org.json.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class HomeKeyboard2 implements RequestHandler<JSONObject, JSONObject> {

    @Override
    public JSONObject handleRequest(JSONObject input, Context context) {
    	//입력된 메시지에서 content만 뽑아내기
    	JSONObject js = new JSONObject();
        JSONArray jsArr = new JSONArray();
        String answer = input.get("content").toString();
        
        
        //내보내야 하는 JSON이 2단계여서 객체도 2개가 필요
        JSONObject jsMes = new JSONObject();
        JSONObject jsMes2 = new JSONObject();
        
        JSONObject jsAns = new JSONObject();
        
        
        
        // 
        if(answer.contains("기타 정보")){
            
            jsAns.put("text", "원하는 정보를 눌러주세요");
            
            jsArr.add("교과과정");
            jsArr.add("공지사항");
            jsArr.add("도서관");
            ///////////////////
            jsArr.add("이클래스");
            jsArr.add("날씨");
            jsArr.add("수강신청");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
            
            // 내보내기
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        // 교과과정
        else if(answer.contains("교과과정")){
        	jsAns.put("text", "교과 과정 목록 안내입니다.\n");
        	
        	jsMes2.put("label", "교과 과정 페이지 입니다.");
        	jsMes2.put("url", "http://www.hansei.ac.kr/portal/default/homepage/schoolaffairs/schoolaffairs_54000.page");
        	jsAns.put("message_button", jsMes2);
        	
            jsMes.put("message", jsAns);
        }
        // 공지사항
        else if(answer.contains("공지사항")){
        	jsMes2.put("text", "공지사항 바로가기입니다.");
        	
        	jsAns.put("label", "공지사항 바로가기");
        	jsAns.put("url", "http://portal.hansei.ac.kr/portal/default/gnb/hanseiTidings/notice.page");
        	// message_button
        	jsMes2.put("message_button", jsAns);
        	jsMes.put("message", jsMes2);
        }
        // 도서관
        else if(answer.contains("도서관")){
        	jsMes2.put("text", "도서관 바로가기입니다.");
        	
        	jsAns.put("label", "도서관 바로가기");
        	jsAns.put("url", "http://lib.hansei.ac.kr");
        	// message_button
        	jsMes2.put("message_button", jsAns);
        	jsMes.put("message", jsMes2);
        }
        // 이클래스
        else if(answer.contains("e-class") || answer.contains("이클래스")){
        	jsMes2.put("text", "이클래스 바로가기입니다.");
        	
        	jsAns.put("label", "이클래스 바로가기");
        	jsAns.put("url", "http://eclass.hansei.ac.kr/ilos/main/main_form.acl");
        	// message_button
        	jsMes2.put("message_button", jsAns);
        	jsMes.put("message", jsMes2);
        }
        // 날씨 OpenWeatherMap API 사용
        else if(answer.contains("날씨")){
        	try {
            	// 한세대의 위도와 경도
            	String lon = "126.953333";  //경도
            	String lat = "37.345649";   //위도
            	
                //OpenAPI call하는 URL
                String urlstr = "http://api.openweathermap.org/data/2.5/weather?"
                            + "lat="+lat+"&lon="+lon
                            +"&appid=36c93a43609f3a714b4d043c5fce7340";
                URL url = new URL(urlstr);
                BufferedReader bf;
                String line;
                String result="";

                //날씨 정보를 받아온다.
                bf = new BufferedReader(new InputStreamReader(url.openStream()));

                //버퍼에 있는 정보를 문자열로 변환.
                while((line=bf.readLine())!=null){
                    result=result.concat(line);
                    //System.out.println(line);
                }
                

                // 문자열을 JSON으로 파싱
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObj = (JSONObject) jsonParser.parse(result);
                
//                // 지역 출력
//                if(jsonObj.get("name").equals("Tang-ni")) {
//                	System.out.println("경기도 날씨");
//                }
                
                // 온도 출력(절대온도라서 변환 필요)
                JSONObject mainArray = (JSONObject) jsonObj.get("main");
                double ktemp = Double.parseDouble(mainArray.get("temp").toString());
                double temp = ktemp-273.15; // 화씨를 섭씨로 맞추기위해 -273.15
                
                // 날씨 출력
                JSONArray weatherArray = (JSONArray) jsonObj.get("weather");
                JSONObject obj = (JSONObject) weatherArray.get(0);
                
                if(obj.get("main").equals("Haze")) {
                	jsAns.put("text", "안개가 많이 끼었습니다.\n"+"현재 기온 : "+temp);
                	jsMes.put("message", jsAns);
                }
                else if(obj.get("main").equals("Clouds")) {
                	jsAns.put("text", "구름이 많습니다.\n"+"현재 기온 : "+temp);
                	jsMes.put("message", jsAns);
                }
                else if(obj.get("main").equals("Clear")) {
                	jsAns.put("text", "맑습니다.\n"+"현재 기온 : "+temp);
                	jsMes.put("message", jsAns);
                }
                else if(obj.get("main").equals("Snow")) {
                	jsAns.put("text", "눈이 내립니다.\n"+"현재 기온 : "+temp);
                	jsMes.put("message", jsAns);
                }
                else if(obj.get("main").equals("Rain")) {
                	jsAns.put("text", "비가 내립니다.\n"+"현재 기온 : "+temp);
                	jsMes.put("message", jsAns);
                }
                else if(obj.get("main").equals("Mist")) {
                	jsAns.put("text", "구름이 많이 끼었습니다.\n"+"현재 기온 : "+temp);
                	jsMes.put("message", jsAns);
                }
                else if(obj.get("main").equals("Thunderstorm")) {
                	jsAns.put("text", "천둥이 칩니다.\n"+"현재 기온 : "+temp);
                	jsMes.put("message", jsAns);
                }
                else if(obj.get("main").equals("Drizzle")) {
                	jsAns.put("text", "구름이 좀 끼었습니다.\n"+"현재 기온 : "+temp);
                	jsMes.put("message", jsAns);
                }

                bf.close();
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        // 수강신청
        else if(answer.contains("수강신청")){
        	jsMes2.put("text", "수강신청 바로가기입니다.");
        	
        	jsAns.put("label", "수강신청 바로가기");
        	jsAns.put("url", "http://sugangnew.hansei.ac.kr:8808/main.html?fake=Fri%20Oct%2006%202017%2022:02:03%20GMT+0900%20(%B4%EB%C7%D1%B9%CE%B1%B9%20%C7%A5%C1%D8%BD%C3");
        	// message_button
        	jsMes2.put("message_button", jsAns);
        	jsMes.put("message", jsMes2);
        }
        else if(answer.contains("날씨")){
        	jsAns.put("text", "교과 과정 목록 안내입니다.\n");
        	
            jsMes.put("message", jsAns);
        }
        /////////////////////////////////////////////
        // IT학부
        else if(answer.contains("IT학부")){
        	jsAns.put("text", "IT학부 안내 입니다.\n원하는 과를 선택해주세요");
        	
        	jsMes2.put("label", "IT학부 소개 페이지 입니다.");
        	jsMes2.put("url", "http://www.hansei.ac.kr/portal/default/homepage/faculty/faculty_36010.page");
        	jsAns.put("message_button", jsMes2);
        	
        	// 버튼 구현
        	jsArr.add("ICT디바이스학과");
            jsArr.add("전자소프트웨어학과");
            jsArr.add("산업보안학과");
            jsArr.add("IT행정실");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
            
            // 내보내기
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        // IT 행정실
        else if(answer.contains("IT행정실")){
        	jsAns.put("text", "이공관 1층에 위치하고 있습니다.\n"+"Tel)031-450-5170");
            jsMes.put("message", jsAns);
        }
        // ICT디바이스학과
        else if(answer.contains("ICT디바이스학과")){
        	jsAns.put("text", "ICT디바이스학과 소개 페이지입니다.\n"+"원하는 정보를 선택해주세요.");
        	
        	// 버튼 구현
        	jsArr.add("ICT 교수진");
            jsArr.add("ICT 교육목표");
            jsArr.add("ICT 졸업 후 활동 분야");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
        	
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        // 교수진 소개 버튼
        else if(answer.contains("ICT 교수진")){
        	jsAns.put("text", "ICT디바이스학과 교수님 소개 페이지입니다.\n"+"원하는 교수님 정보를 선택해주세요.");
        	
        	// 버튼 구현
        	jsArr.add("김정곤 교수님");
            jsArr.add("김선집 교수님");
            jsArr.add("윤수영 교수님");
            ///////////////////////
            jsArr.add("신승중 교수님");
            jsArr.add("홍완표 교수님");
            jsArr.add("김석우 교수님");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
        	
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        // 교수님 소개
        else if(answer.contains("김정곤 교수님")){
        	jsAns.put("text", "김정곤 교수님은 이공관 3층에 위치합니다.\n"+"Tel)010-9100-5174");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("김선집 교수님")){
        	jsAns.put("text", "김선집 교수님은 이공관 3층에 위치합니다.\n"+"Tel)010-2055-0156");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("김석우 교수님")){
        	jsAns.put("text", "김석우 교수님은 이공관 3층에 위치합니다.\n"+"Tel)010-2409-1122");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("윤수영 교수님")){
        	jsAns.put("text", "김석우 교수님은 이공관 3층에 위치합니다.\n"+"Tel)031-450-9885");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("신승중 교수님")){
        	jsAns.put("text", "김석우 교수님은 이공관 3층에 위치합니다.\n"+"Tel)010-3664-0666");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("홍완표 교수님")){
        	jsAns.put("text", "김석우 교수님은 이공관 3층에 위치합니다.\n"+"Tel)031-450-5340");
            jsMes.put("message", jsAns);
        }
        // ICT 교육목표
        else if(answer.contains("ICT 교육목표")){
        	jsAns.put("text", "ICT 디바이스 전공은 ‘제4차 산업혁명’이 미래에 미칠 영향이 세계적 관심사로 떠오른 가운데, 국내에서 10년 안에 1800만개 일자리가 인공지능이나 로봇으로 대체될 수 있다는 연구 결과가 나왔습니다. ‘정부 보고서’를 보면,\n"+
        			"인공지능과 로봇 기술의 발전으로 2025년 취업자 2561만명 중 1807만명(71%)이 ‘일자리 대체 위험’에 직면할 수 있다고 합니다."+ 
        			"ICT 디바이스학과는 사물인터넷, 빅 데이터, 클라우드, 모바일, 그리고 인공지능을 분야를 기반으로 정보, 통신, 전자 분야가 융합된 학문으로서 정보통신 분야뿐만 아니라 로봇, 자동차, 조선, 건설, 교통 등 사회 및 산업 전 분야에 적용할 수 있어 다양한 가능성과 기회를 학생들에게 제공해 줄 것입니다."+
        			"ICT에 관한 이론적 교육과 풍부한 실습 및 프로젝트 수업을 통하여 산업체에서 요구하는 지식을 갖추고 현장에서 바로 활용할 수 있는 수준의 인력을 양성하고 있습니다."+
        			"\n\n\n1) ICT에 관한 교육을 통해 정보사회와 미래 지식사회에 대한 비전과 소양을 갖추도록 함."
        			+"2) 하드웨어 및 소프트웨어를 바탕으로 하는 정보통신 시스템의 설계 및 구현 능력을 갖추도록 함."
        			+"3) 실습과 프로젝트 수업을 통하여 산업체에서 요구하는 지식을 갖추고 현장에서 바로 활용할 수 있는 수준의 인력을 양성."
        			+"4) 의, 진리, 사랑을 바탕으로 하는 인성 교육에 중점 둠.");
            jsMes.put("message", jsAns);
        }
        // ICT 졸업 후 활동 분야
        else if(answer.contains("ICT 졸업 후 뢀동 분야")){
        	jsAns.put("text", "1) 관련분야\nICT 및 IT융합 산업 관련 회사에서 연구, 관리, 기획, 교육, 영업 등의 업무를 담당.\n"+
        			"국가, 지방자치 단체, 기업체의 연구소등에 취업.\n"+
        			"공공기관, 공사, 방송사 등으로 진출.\n"+
        			"국가, 지방 자치단체의 공무원으로 진출.\n"+
        			"국내외 대학원으로 진학.\n"+
        			"개인의 아이디어나 기술력을 바탕으로 창업.\n\n"+
        			"2) 진로소개\n"+
        			"제 4차 산업혁명을 선도하는 주역으로서 IoT, 빅 데이터, 모바일 관련 산업체와 공공기관에 창의력 있고 경쟁력 있는 고급인력으로 취업할 수 있을 뿐 아니라 아이디어와 기술력을 바탕으로 창업을 할 수도 있습니다.");
            jsMes.put("message", jsAns);
        }
        
        // 전자소프트웨어학과
        else if(answer.contains("전자소프트웨어학과")){
        	jsAns.put("text", "ICT디바이스학과 소개 페이지입니다.\n"+"원하는 정보를 선택해주세요.");
        	
        	// 버튼 구현
        	jsArr.add("전자SW 교수진");
            jsArr.add("전자SW 교육목표");
            jsArr.add("전자SW 졸업 후 활동 분야");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
        	
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        // 교수진 소개 버튼
        else if(answer.contains("전자SW 교수진")){
        	jsAns.put("text", "전자소프트웨어학과 교수님 소개 페이지입니다.\n"+"원하는 교수님 정보를 선택해주세요.");
        	
        	// 버튼 구현
        	jsArr.add("김형준 교수님");
            jsArr.add("류대현 교수님");
            jsArr.add("이왕헌 교수님");
            ///////////////////////
            jsArr.add("엄기홍 교수님");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
        	
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        // 교수님 소개
        else if(answer.contains("김형준 교수님")){
        	jsAns.put("text", "김형준 교수님은 이공관 3층에 위치합니다.\n"+"Tel)031-450-5158");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("류대현 교수님")){
        	jsAns.put("text", "류대현 교수님은 이공관 3층에 위치합니다.\n"+"Tel)010-5450-0132");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("이왕헌 교수님")){
        	jsAns.put("text", "이왕헌 교수님은 이공관 3층에 위치합니다.\n"+"Tel)031-450-5146");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("엄기홍 교수님")){
        	jsAns.put("text", "엄기홍 교수님은 이공관 3층에 위치합니다.\n"+"Tel)031-450-5308");
            jsMes.put("message", jsAns);
        }
        // 전자SW 교육목표
        else if(answer.contains("전자SW 교육목표")){
        	jsAns.put("text", "컴퓨터 기술을 기반으로 실무형 맞춤 S/W 기술인력을 양성하여 국가적 인재를 양성 하는데 목표가 있습니다.\n"+
        						"1) 컴퓨터 기술을 기반으로 하는 실무형 맞춤 소프트웨어 기술인력 양성.\n"+
        						"2) 체계적인 분석과 설계과정을 통하여 이론과 실험·실습을 통한 시스템적인 접근 방법을 구사할 수 있는 전문 소프트웨어 지식인 양성\n"+
        						"3) 팀 프로젝트를 통해 특성화된 전자소프트웨어공학 교육 지향 4. 정보화,지식화 사회를 올바르게 인식하고 선도할 수 있는 인재 양성");
            jsMes.put("message", jsAns);
        }
        // 전자SW 졸업 후 활동 분야
        else if(answer.contains("전자SW 졸업 후 뢀동 분야")){
        	jsAns.put("text", "1) 관련분야\n"+
        			"인터넷 프로그래머, 시스템 프로그래머, 온라인/오프라인 응용 소프트웨어 프로그래머, 데이터베이스/서버/네트워크/전산실 관리자, 인터넷 전문가, 정보 보안 전문가,\n"+
        			"인터넷 방송 및 게임 제작 전문 인력, 하드웨어 개발자, ASIC 개발자, 그리고 System-on-chip(SOC) 개발자 등이 될 수 있으며, 수많은 컴퓨터, 정보, 통신 산업 관련 회사에서 "+ 
        			"관리/기획/교육/영업 등의 업무를 담당하거나 연구소나 대학원에 진학하여 컴퓨터 및 정보기술 이론에 대해 한층 더 심도 있는 연구를 수행할 수 있습니다.\n\n"+
        			"2) 진로소개\n"+
        			"전자소프트웨어공학 전공을 졸업한 학생은 정보화 사회로의 진입과 더불어 미래사회를 선도하는 중추적인 역할을 수행할 수 있습니다.\n"+ 
        			"온/오프라인 개발자 및 DB, 네트워크 등의 인프라 개발자 등이 될 수 있습니다. 개발 이외의 컴퓨터에 의존하는 개발 이외의 업무분야는 모든 산업이"+ 
        			"컴퓨터에 의존하기 때문에 더욱 광범위합니다. 컴퓨터에 의존하는 경향은 정보화가 더 진행될수록 더 깊어질 것으로 모든 사람들이 예측하고 있습니다.");
            jsMes.put("message", jsAns);
        }
        
        ////////////////////////////////
        else if(answer.contains("Hi")){
        	jsAns.put("text", "Hi!");
            jsMes.put("message", jsAns);
        }
        
        // 이 외 모두
        else{
        	jsAns.put("text", "알아들을수 없는 말입니다.");
            jsMes.put("message", jsAns);
        }
        //내보낼 JSON인 jsMes에 if절로 만든 상황에 따른 답변을 넣어줍니다
        return jsMes;
    }

}
