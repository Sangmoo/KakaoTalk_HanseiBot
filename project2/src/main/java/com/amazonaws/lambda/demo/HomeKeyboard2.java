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
            //////////////////
            jsArr.add("서점");
            jsArr.add("학식");
            
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
        else if(answer.contains("서점")){
        	jsMes2.put("text", "서점은 학생회관 1층에 있습니다.\n"
        			+ "Tel) 031-456-9754");
        	
        	jsMes.put("message", jsMes2);
        }
        else if(answer.contains("학식") || answer.contains("주간식단")){
        	jsMes2.put("text", "주간식단표 바로가기입니다.");
        	
        	jsAns.put("label", "주간식단표 메뉴 바로가기");
        	jsAns.put("url", "http://portal.hansei.ac.kr/portal/default/gnb/hanseiTidings/weekMenuTable.page");
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
        
        // 산업보안학과 
        else if(answer.contains("산업보안학과")){
        	jsAns.put("text", "산업보안학과 소개 페이지입니다.\n"+"원하는 정보를 선택해주세요.");
        	
        	// 버튼 구현
        	jsArr.add("산업보안 교수진");
            jsArr.add("산업보안 교육목표");
            jsArr.add("산업보안 졸업 후 활동 분야");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
        	
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        // 교수진 소개 버튼
        else if(answer.contains("산업보안 교수진")){
        	jsAns.put("text", "산업보안학과 교수님 소개 페이지입니다.\n"+"원하는 교수님 정보를 선택해주세요.");
        	
        	// 버튼 구현
        	jsArr.add("송봉규 교수님");
            jsArr.add("조용순 교수님");
            jsArr.add("권창희 교수님");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
        	
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        // 교수님 소개
        else if(answer.contains("송봉규 교수님")){
        	jsAns.put("text", "송봉규 교수님은 비전관 2층에 위치합니다.\n"+"Tel)031-450-9880");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("조용순 교수님")){
        	jsAns.put("text", "조용순 교수님은 비전관 2층에 위치합니다.\n"+"Tel)010-450-9877");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("권창희 교수님")){
        	jsAns.put("text", "권창희 교수님은 이공관 3층에 위치합니다.\n"+"Tel)031-450-5254");
            jsMes.put("message", jsAns);
        }
        // 산업보안 교육목표
        else if(answer.contains("산업보안 교육목표")){
        	jsAns.put("text", "우리나라의 기술발전에 따라 국내외의 산업스파이 등에 의한 국부유출이 증가하고 있어 산업보안의 중요성이 부각되고 있다.\n"
        			+ "본 학과는 산업보안과 관련된 체계적인 이론 교육과 실습을 통하여 법학, 범죄학, 경영학, 정보보안 등에 대하여 깊이 이해시켜, 정부기관, 민간기업, 정부출연연구소 등에서 즉시활용 가능한 통섭형·실무형 산업보안 고급 전문 인력을 양성하여\n"
        			+ "대한민국 경제안보에 이바지함을 함을 목적으로 하고 있다.");
            jsMes.put("message", jsAns);
        }
        // 산업보안 졸업 후 활동 분야
        else if(answer.contains("산업보안 졸업 후 뢀동 분야")){
        	jsAns.put("text", "최근 국가 핵심기술유출 및 대기업․중소기업의 기술유출사건이 증가하고 있음에 따라 관련분야의 산업보안전문가의 수요가 급증하고 있다.\n"
        			+ "졸업생들은 산업보안 전략 및 기획분야, 산업보안 교육훈련 분야, 산업보안시스템 구축 운영 분야, 산업보안 사고 대응분야에서 활동하게 된다.\n"
        			+ "산업보안학과 졸업생들의 주요 활동분야는 국가기관 공무원, 공공기관 종사자, 기업의 산업보안전문가, 산업보안 컨설턴트 창업 등으로 분류할 수 있다.");
            jsMes.put("message", jsAns);
        }
        ////////////////////////////////
        // 신학부
        else if(answer.contains("신학부")){
        	jsAns.put("text", "신학부 안내 입니다.\n원하는 과를 선택해주세요");
        	
        	jsMes2.put("label", "신학부 소개 페이지 입니다.");
        	jsMes2.put("url", "http://www.hansei.ac.kr/portal/default/homepage/faculty/faculty_31010.page");
        	jsAns.put("message_button", jsMes2);
        	
        	// 버튼 구현
        	jsArr.add("신학과");
            jsArr.add("기독교교육학과");
            jsArr.add("신학부행정실");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
            
            // 내보내기
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        // 신학부행정실
        else if(answer.contains("신학부행정실")){
        	jsAns.put("text", "신학관 1층에 위치하고 있습니다.\n"+"Tel)031-450-5023");
            jsMes.put("message", jsAns);
        }
        // 신학과
        else if(answer.contains("신학과")){
        	jsAns.put("text", "신학과 소개 페이지입니다.\n"+"원하는 정보를 선택해주세요.");
        	
        	// 버튼 구현
        	jsArr.add("신학과 교수진");
            jsArr.add("신학과 교육목표");
            jsArr.add("신학과 졸업 후 활동 분야");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
        	
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        // 교수진 소개 버튼
        else if(answer.contains("신학과 교수진")){
        	jsAns.put("text", "신학과 교수님 소개 페이지입니다.\n"+"원하는 교수님 정보를 선택해주세요.");
        	
        	// 버튼 구현
        	jsArr.add("신문철 교수님");
            jsArr.add("이영호 교수님");
            jsArr.add("차준희 교수님");
            //////////////////////
            jsArr.add("최문홍 교수님");
            jsArr.add("최상준 교수님");
            jsArr.add("임영섭 교수님");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
        	
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        // 교수님 소개
        else if(answer.contains("신문철 교수님")){
        	jsAns.put("text", "신문철 교수님은 신학관에 위치합니다.\n"+"Tel)031-450-5193");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("이영호 교수님")){
        	jsAns.put("text", "이영호 교수님은 신학관에 위치합니다.\n"+"Tel)031-450-5219");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("차준희 교수님")){
        	jsAns.put("text", "차준희 교수님은 신학관에 위치합니다.\n"+"Tel)031-450-5076");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("최문홍 교수님")){
        	jsAns.put("text", "최문홍 교수님은 신학관에 위치합니다.\n"+"Tel)031-450-5217");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("최상준 교수님")){
        	jsAns.put("text", "최상준 교수님은 신학관에 위치합니다.\n"+"Tel)031-450-9882");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("임영섭 교수님")){
        	jsAns.put("text", "임영섭 교수님은 신학관에 위치합니다.\n"+"Tel)031-450-5376");
            jsMes.put("message", jsAns);
        }
        // 신학과 교육목표
        else if(answer.contains("신학과 교육목표")){
        	jsAns.put("text", "본 학교의 신학은 기독교 신학의 기초 위에 오순절신학의 전통을 계승하고 더 나아가 순복음신학을 정립하고\n"
        			+ "확산하여 한국과 세계 교회와 신학계에 이바지할 수 있도록 하는 데 교육 목표를 둡니다.\n"
        			+ "이를 위해 신학과 분야에서는 성서신학을 바탕으로 이론신학적 체계를 가르치고 훈련시켜 오순절적 영성을 겸비한 순복음 지도자를 양성하는 데 주력합니다.\n"
        			+ "또한, 신학과 목회의 괴리감을 일소하기 위해 교회와 신학교 간의 상호협력적이며 보완적인 시스템을 구축하여한국교회의 획기적인 발전에 기여할 수 있도록 제반적인 노력을 다합니다.");
            jsMes.put("message", jsAns);
        }
        // 신학과 졸업 후 활동 분야
        else if(answer.contains("산업보안 졸업 후 뢀동 분야")){
        	jsAns.put("text", "본 전공을 취득한 학생은 명실공히 21세기 교회를 이끌어 갈 오순절적 영성을 소유한 지도자들입니다.\n"
        			+ "이들은 목회자, 선교사, 신학자 또는 각 분야의 유능한 일꾼들로서 순복음교단 뿐만 아니라 세계교회에서 활약하며 주의 오심을 땅끝까지 증거할 역할을 할 것입니다.\n"
        			+ "또한, 대학원에 진학하여 석사 및 박사학위를 취득할 수 있으며 목회자 ,선교사, 신학자,기관목회자,(군목, 교목, 원목), 특수사회 선교사, 사회복지사 등으로 활동할 수 있습니다.");
            jsMes.put("message", jsAns);
        }
        /////////////////////
        // 기독교교육학과
        else if(answer.contains("기독교교육학과")){
        	jsAns.put("text", "기독교교육학과 소개 페이지입니다.\n"+"원하는 정보를 선택해주세요.");
        	
        	// 버튼 구현
        	jsArr.add("기독교교육학과 교수진");
            jsArr.add("기독교교육학과 교육목표");
            jsArr.add("기독교교육학과 졸업 후 활동 분야");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
        	
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        // 교수진 소개 버튼
        else if(answer.contains("기독교교육학과 교수진")){
        	jsAns.put("text", "신학과 교수님 소개 페이지입니다.\n"+"원하는 교수님 정보를 선택해주세요.");
        	
        	// 버튼 구현
        	jsArr.add("최성훈 교수님");
            jsArr.add("이수길 교수님");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
        	
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        // 교수님 소개
        else if(answer.contains("최성훈 교수님")){
        	jsAns.put("text", "최성훈 교수님은 신학관에 위치합니다.\n"+"Tel)031-450-9825");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("이수길 교수님")){
        	jsAns.put("text", "이수길 교수님은 신학관에 위치합니다.\n"+"Tel)031-450-5236");
            jsMes.put("message", jsAns);
        }
        // 기독교교육학과 교육목표
        else if(answer.contains("기독교교육학과 교육목표")){
        	jsAns.put("text", "1) 기독교교육과 상담을 실천할 수 있는 전문적 지도자 육성\n"
        			+ "2) 기독교 문화를 세우고 이끌어가는 창의적인 전문인 양성\n"
        			+ "3) 기독교 상담과 교육분야에서 세계적인전문가 역할을 할 수 있는 글로벌 인재 육성\n"
        			+ "4) 영성과 전문성의 조화.");
            jsMes.put("message", jsAns);
        }
        // 기독교교육학과 졸업 후 활동 분야
        else if(answer.contains("기독교교육학과 졸업 후 뢀동 분야")){
        	jsAns.put("text", "기독교교육·상담학과의 취업은 크게 네 가지로 분류할 수 있습니다.\n"
        			+ "1) 교회사역을 목적으로 하는 대학원(M.Div)진학.\n"
        			+ "현재 한세대 대학원 과정에서 기독교교육학과가 있어 교회교육을 전문으로 하는 학생들의 진학이 늘고 있습니다.\n"
        			+ "이는 학부에서의 교육과 연계할 수 있어 많은 장점이 있습니다. \n"
        			+ "2) 일반대학원 또는 교육대학원 진학.\n"
        			+ "교육이나, 심리, 상담을 목적으로 대학원에 진학하는 경우입니다. 현재, 교육공학이나 심리학을 공부하기 원하는 학생들이 늘고 있습니다.\n"
        			+ "3) 교회로의 취업.\n"
        			+ "교회로의 취업은 교육전도사나 교회에서의 교육을 담당하는 과정입니다. 교육전도사로 시작해서 대학원을 마친 후 교육목회나 담임목회 사역을 준비하는 과정입니다.\n"
        			+ "4) 상담센터로의 취업.\n"
        			+ "기독교교육과 상담의 전공자는 기독교 세계관을 바탕으로 교육과 상담의 전문지식을 함양한 지도자를 양성 하며, 발달재활심리사 자격증을 획득하여 교회, 학교, 상담센터, Wee센터, 발달장애치료센터, 재활관련기관, 아동청소년기관 등에서 상담을 담당하는 과정입니다.");
            jsMes.put("message", jsAns);
        }
        ///////////////////////////////
        // 사회과학부
        else if(answer.contains("사회과학부")){
        	jsAns.put("text", "사회과학부 안내 입니다.\n원하는 과를 선택해주세요");
        	
        	jsMes2.put("label", "사회과학부 소개 페이지 입니다.");
        	jsMes2.put("url", "http://www.hansei.ac.kr/portal/default/homepage/faculty/faculty_32010.page");
        	jsAns.put("message_button", jsMes2);
        	
        	// 버튼 구현
        	jsArr.add("미디어광고학과");
            jsArr.add("국제경영학과");
            jsArr.add("경찰행정학과");
            jsArr.add("국제관광학과");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
            
            // 내보내기
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        else if(answer.contains("미디어광고학과")){
        	jsAns.put("text", "미디어광고학과는 스마트미디어시대와 세계화시대를 이끌어 갈 전문역량과 실무 능력을 갖춘 미디어콘텐츠 전문가 양성을 교육 목표로 실용적 전문성이 강화된 교육과정을 운영하고 있습니다.\n"
        			+ "기독교 세계관을 바탕으로 미디어 현상을 분석하고, 커뮤니케이션의 이론적 학습과 실무지식을 익혀 글로벌 시대에 부응하는 미디어영상 전문가 육성을 목표로 합니다.\n"
        			+ "재학 기간 동안 이론적 체계는 물론 언론현장, 광고홍보 업계 등 미디어 관련 산업에서의 현장실습과 인턴경험을 통해 스스로 미래를 개척하는 자주적인 인재로 성장해갈 수 있으며, 디지털 콘텐츠를 기획, 제작하고 직접 유통시킬 수 있는 마케팅 능력까지 갖춘 스마트미디어 시대의 전문인으로 성장할 수 있도록 국내 최고의 교수진이 모든 역량을 다하고 있습니다.");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("국제경영학과")){
        	jsAns.put("text", "국제경영학과는 글로벌 지식정보화시대를 맞이하여, 기업의 글로벌화(Globalization)와 시장의 개방화 등 급변하는 글로벌 비즈니스 환경에 부응하는 기독교적 인성과 전문인으로서의 역량을 갖춘 인재를 육성하는데 그 목표를 두고 있습니다.\n"
        			+ "재학 기간 동안 차별화된 교과과정을 통해 이론적 체계뿐만 아니라, 기업 현장실습 및 인턴제도 등을 활용하여 글로벌 비즈니스 환경을 둘러싼 다양한 제반문제에 적극적으로 대처할 수 있는 역량을 갖출 수 있도록 지원하고 있습니다.\n"
        			+ "특히 디지털화되고 있는 글로벌경영 환경 변화에 부응하는 전문적인 지식소양을 겸비한 글로벌 인재로 성장해 갈 수 있도록 국내 최고의 교수진이 모든 역량을 다하고 있습니다.\n"
        			+ "국제경영학과에서는 국제경영(International Business)and Management, Global Marketing and 디지털 창업, Accounting and Finance 등 심화전공 트랙을 운영하고 있습니다.");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("경찰행정학과")){
        	jsAns.put("text", "경찰행정학과에서는 국민의 생명과 신체 보호를 비롯하여 사회공공질서를 유지해나갈 전문적인 인력 양성을 목표로 하고 있습니다.\n"
        			+ "이를 위해 경찰의 조직, 인사는 물론 민사법과 형법, 형사소송법 등 법학 분야에 대한 체계적인 이해를 돕는 이론교육에서부터 재학생 전원이 태권도 유단자가 될 수 있도록 지원하는 교육과정을 운영하고 있습니다.");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("국제관광학과")){
        	jsAns.put("text", "관광 수요가 날로 증대되는 21세기, 국제관광전공에서는 관광산업이론과 실무실습교육을 병행하여 이론과 실무를 겸비한 관광전문인력을 양성하고 있습니다.\n"
        			+ "교육과정은 관광학 분야의 다양한 기초이론을 바탕으로 매학기 인턴십과 실습과목 이수과정으로 각종 관광산업계의 실습교육을 실행하고 있습니다.\n"
        			+ "아울러 국가공인자격증인 관광통역안내사, 국내여행안내사, 호텔서비스사/호텔관리사/호텔경영사 자격시험에서 전공관련 과목의 필기시험이 면제되는 혜택을 받을 수 있습니다.");
            jsMes.put("message", jsAns);
        }
        //////////////////////////
        // 국제 언어학부
        else if(answer.contains("국제언어학부")){
        	jsAns.put("text", "사회과학부 안내 입니다.\n원하는 과를 선택해주세요");
        	
        	jsMes2.put("label", "국제언어학부 소개 페이지 입니다.");
        	jsMes2.put("url", "http://www.hansei.ac.kr/portal/default/homepage/faculty/faculty_35010.page");
        	jsAns.put("message_button", jsMes2);
        	
        	// 버튼 구현
        	jsArr.add("영어통상통역학과");
            jsArr.add("중국어학과");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
            
            // 내보내기
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        else if(answer.contains("영어통상통역학과")){
        	jsAns.put("text", "글로벌 시대에 발맞춘 국제적 수준의 영어 전문인력을 양성하는 데 교육목적을 두고, 다양한 분야에서 높은 수준의 역할을 수행할 수 있는 영어 전문인력을 양성하기 위해 충분한 교육과 실습기회를 제공합니다.\n"
        			+ "최고의 집중적인 영어교육을 받게 되며 또한 국제적 시야와 감각을 갖추도록 하기 위해 세계 유수의 외국자매학교와 어학연수 및 교환학생의 기회를 부여합니다.");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("중국어학과")){
        	jsAns.put("text", "세계화, 정보화, 개방화 시대인 21세기에 국제 공용어로 부상하고 있는 중국어의 중요성은 아무리 강조해도 지나치지 않을 것입니다.\n"
        			+ "2014년도에 신설된 본 학과는 중국과의 국제교류가 활발해진 시대적 흐름에 부응하기 위해 중국어를 중심으로 한 중국문화 전반에 대한 체계적인 교육시스템을 통하여 사회 각 분야에 필요로 하는 중국 전문가를 양성하는 것을 목표로 하고 있습니다.\n"
        			+ "이를 위해 본 대학은 이미 중국, 대만 등의 중국어권 소재 대학과 교류협정을 맺고 다각도의 학술교류에 힘쓰고 있으며 매년 교환학생을 파견하여 자유로운 중국어구사능력을 지닌 국제적 감각의 전문가를 양성하고자 합니다.");
            jsMes.put("message", jsAns);
        }
        /////////////////////////
        // 간호복지학부
        else if(answer.contains("간호복지학부")){
        	jsAns.put("text", "간호복지학부 안내 입니다.\n원하는 과를 선택해주세요");
        	
        	jsMes2.put("label", "간호복지학부 소개 페이지 입니다.");
        	jsMes2.put("url", "http://www.hansei.ac.kr/portal/default/homepage/faculty/faculty_3A020.page");
        	jsAns.put("message_button", jsMes2);
        	
        	// 버튼 구현
        	jsArr.add("간호학과");
            jsArr.add("사회복지학과");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
            
            // 내보내기
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        else if(answer.contains("간호학과")){
        	jsAns.put("text", "한세대학교 간호학과는 기독교 정신을 토대로 전인간호를 수행할 간호 전문인을 양성합니다.\n"
        			+ "현장 실습을 중심으로 실제 간호 현장에서 빠르게 적응할 수 있도록 교과과정을 편재했습니다.\n"
        			+ "경찰병원, 한림대학교 통탄성심병원,강북삼성병원, 강남차병원 등과 실습협약을 체결해 있으며, 간호사로서의 핵심역량을 함양하기 위한 교육을 시행하고 있습니다.");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("사회복지학과")){
        	jsAns.put("text", "사회복지학과에서는 사회의 변화 추세에 따른 다양한 욕구를 충족시키기 위해 현대사회의 문제를 이해하고 해결하며, 전문적인 지식과 기술을 습득한 복지전문가를 양성하는 데 목표를 두고 있습니다.\n"
        			+ "이를 위해 사회복지사 1급에 필요한 10개의 필수과목을 포함해서 다양한 교육과정을 제공하고 있으며 또한 건강가정사를 위한 자격증 교과과정도 개설하고 있습니다.\n"
        			+ "졸업 후에는 사회복지기관이나 공무원 등으로 진출할 수 있습니다.");
            jsMes.put("message", jsAns);
        }
        /////////////////////////////////
        // 예술학부
        else if(answer.contains("예술학부")){
        	jsAns.put("text", "예술학부 안내 입니다.\n원하는 과를 선택해주세요");
        	
        	jsMes2.put("label", "간호복지학부 소개 페이지 입니다.");
        	jsMes2.put("url", "http://www.hansei.ac.kr/portal/default/homepage/faculty/faculty_37010.page");
        	jsAns.put("message_button", jsMes2);
        	
        	// 버튼 구현
        	jsArr.add("음악학과");
            jsArr.add("공연예술학과");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
            
            // 내보내기
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        else if(answer.contains("음악학과")){
        	jsAns.put("text", "피아노, 관현악, 성악의 다양한 전공을 포함하고 있으며 각 전공마다 실기 위주의 교육을 강화하고 있습니다.\n"
        			+ "또한 다양한 연주활동과 무대경험을 통하여 경쟁력 있는 음악인을 양성하고 있으며 국내외 우수 교수진을 통해 개개인의 음악적 소질과 적성을 중시합니다.\n"
        			+ "다양하고 특성화된 전문적인 실기지도를 통해, 음악적 능력을 최대한 개발할 수 있도록 하며, 더불어 기독교 정신과 신앙을 바탕으로 훌륭한 음악가로서의 교육과 폭넓은 교양인으로서의 자질 향상과\n"
        			+ "성숙한 인격을 갖춘, 사회가 요구하는 전문 예술인을 발굴하기 위해 노력하고 있습니다.");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("공연예술학과")){
        	jsAns.put("text", "다양한 형태의 공연예술에 적합한 인성과 기능을 훈련시켜 공연예술전문인으로서 문화예술계를 이끌어갈 우수한 인재 육성을 목표로 하고 있습니다.\n"
        			+ "연기, 댄스, 보컬 이외에도 각 분야에 필요한 능력을 집중적으로 훈련하여 현장에서 요구하는 기본기를 탄탄히 갖출 수 있도록 하며, 학계와 현장에서 활발하게 활동하고 있는 전문가를 교수로 초빙하여\n"
        			+ "이론과 실기를 겸비한 균형있는 교육을 통해 보다 전문적이고 창의적인 역량을 갖춘 만능엔터테이너를 양성하고자 합니다.");
            jsMes.put("message", jsAns);
        }
        ////////////////////////////
        // 디자인학부
        else if(answer.contains("디자인학부")){
        	jsAns.put("text", "디자인학부 안내 입니다.\n원하는 과를 선택해주세요");
        	
        	jsMes2.put("label", "디자인학부 소개 페이지 입니다.");
        	jsMes2.put("url", "http://www.hansei.ac.kr/portal/default/homepage/faculty/faculty_38010.page");
        	jsAns.put("message_button", jsMes2);
        	
        	// 버튼 구현
        	jsArr.add("시각정보디자인학과");
            jsArr.add("실내건축디자인학과");
            jsArr.add("섬유패션디자인학과");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
            
            // 내보내기
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        else if(answer.contains("시각정보디자인학과")){
        	jsAns.put("text", "급변하는 산업화 ∙ 정보화사회가 요구하는 보다 종합적인 사고를 기반으로 정보에 대한 시각화 과정을 통한 효과적 커뮤니케이션 교육으로 기업이 요구하는 창의적 실무형 인재 양성을 목표로 합니다.\n"
        			+ "또한 심화 전공별 효과적인 트랙 시스템과 융합형 랩의 운영을 통하여 차별화된 다학제적 마인드를 지닌 우수 인재를 배출합니다.");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("실내건축디자인학과")){
        	jsAns.put("text", "실내건축디자인학과에서는 미래의 이상적인 인간환경 구축과 새로운 공간문화 창출을 위한 연구와 교육이 이루어집니다.\n"
        			+ "이를 위해 다학제적인 접근, 최첨단 장비와 실습지원 등으로 최고의 교육환경을 구축하고 있으며 디자이너로서의 안목을 향상시킬 수 있는 다양한 해외 연수 및 공모전 지원 등을 통해 국내외적으로 그 경쟁력을 인정받고 있습니다.\n"
        			+ "또한 친환경 관련 전공을 통하여 실력 있고 의식 있는 디자인 리더로서의 역량을 키워나가고 있습니다.");
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("섬유패션디자인학과")){
        	jsAns.put("text", "폭넓고 다양한 학문적인 이론과 첨단기술을 습득하고 국제적인 감각과 지식을 익혀 글로벌 섬유패션 전문인재 육성을 목표로 합니다.\n"
        			+ "Creative, Unique, Wearable, Salable, Design을 목표로 창의력과 테크닉의 조화를 극대화하는 교육을 통해 미적 표현능력의 개발은 물론 패션산업의 다양한 마켓에 부응할 수 있는 전문인을 양성합니다.\n"
        			+ "글로벌적이며 통합적인 틀을 구축하는 교과목부터 실무밀착형 교과목까지 특성화된 커리큘럼을 제공하여 디자인 기획 및 개발부터 생산, 유통에 이르는 섬유패션산업의 리더로 거듭날 수 있도록 하고 있습니다.");
            jsMes.put("message", jsAns);
        }
        //////////////////////////
        // 행정처
        else if(answer.contains("행정처")){
        	jsAns.put("text", "행정처 안내 입니다.\n원하는 부서를 선택해주세요");
        	
        	jsMes2.put("label", "행정처 페이지 입니다.");
        	jsMes2.put("url", "http://www.hansei.ac.kr/portal/default/homepage/organization/administrativeAgency/organization_41500.page");
        	jsAns.put("message_button", jsMes2);
        	
        	// 버튼 구현
        	jsArr.add("교무처");
            jsArr.add("학적");
            jsArr.add("교목실");
            /////////////////////
            jsArr.add("시설");
            jsArr.add("생활관");
            jsArr.add("글로벌");
            
            js.put("buttons", jsArr);
            js.put("type", "buttons");
            
            // 내보내기
            jsMes.put("message", jsAns);
            jsMes.put("keyboard", js);
        }
        else if(answer.contains("교무처")){
        	jsAns.put("text", "교무처 안내입니다.\n"
        			+ "Tel) 031-450-5082");
        	
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("학적")){
        	jsAns.put("text", "학적 안내입니다.\n"
        			+ "Tel) 031-450-5155");
        	
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("교목실")){
        	jsAns.put("text", "교목실 안내입니다.\n"
        			+ "Tel) 031-450-5302");
        	
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("시설")){
        	jsAns.put("text", "교무처 안내입니다.\n"
        			+ "Tel) 031-450-5225");
        	
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("생활관")){
        	jsMes2.put("text", "생활관 안내입니다.\n"
        			+ "Tel) 031-450-5130");
        	
        	jsAns.put("label", "생활관 홈페이지 바로가기");
        	jsAns.put("url", "www.hanseitown.co.kr");
        	// message_button
        	jsMes2.put("message_button", jsAns);
        	jsMes.put("message", jsMes2);
        	
            jsMes.put("message", jsAns);
        }
        else if(answer.contains("글로벌")){
        	jsMes2.put("text", "글로벌교육센터입니다.\n"
        			+ "Tel) 031-450-5130");
        	
        	jsAns.put("label", "한세 글로벌교육센터 홈페이지 바로가기");
        	jsAns.put("url", "http://www.hanseienglish.org");
        	// message_button
        	jsMes2.put("message_button", jsAns);
        	jsMes.put("message", jsMes2);
        	
            jsMes.put("message", jsAns);
        }
        //내보낼 JSON인 jsMes에 if절로 만든 상황에 따른 답변을 넣어줍니다
        return jsMes;
    }

}
