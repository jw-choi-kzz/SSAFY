# WebSocket
서버와 클라이언트 간에 양방향 통신 프로토콜. 계속 연결 유지하면서 양방향으로 데이터 주고받기 가능.  
*(HTTP 프로토콜: 클라이언트에서 요처 보내면 서버에서 응답, 연결 끝)*  
**클라이언트)** WebSocket 객체를 만들고 서버와의 연결을 설정, 데이터 전송  
**서버)** 클라이언트와의 연결 수신, 연결 유지하며 데이터 전송  
*실시간 채팅, 게임, 주식 시세 등에서 활용*  
    
## SocketHandler 
구현체에 등록할 SocketHandler 정의.  
WebSocket 프로토콜은 Text, Binary 타입 지원하기 때문에 필요에 따라 **TextWebSocketHandler, BinaryWebSocketHandler** 상속하여 구현.  
**WebSocketSession** 파라미터는 WebSocket이 연결될 때 생기는 연결정보 담은 객체.  
```afterConnectionEstablished```: 소켓 연결 시 동작  
```acafterConnectionClosed```: 소켓이 종료되면 동작  
```handleTextMessage```: 메시지 수신 시 동작(**TextWebSocketHandler** 상속 시)  
```handleBinaryMessage```: 메시지 수신 시 동작(**BinaryWebSocketHandler** 상속 시)  
  
### ex. DevLogWebSocketHandler.java
```java
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DevLogWebSocketHandler extends TextWebSocketHandler{
	
	Map<String, WebSocketSession> sessionMap = new HashMap<>(); //웹소켓 세션을 담아둘 맵
	Map<String, String> userMap = new HashMap<>();	//사용자
	
	/* 클라이언트로부터 메시지 수신시 동작 */
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String msg = message.getPayload();
		log.info("===============Message=================");
		log.info("{}", msg);
		log.info("===============Message=================");
		
		JSONObject obj = jsonToObjectParser(msg);
		//로그인된 Member (afterConnectionEstablished 메소드에서 session을 저장함)
		for(String key : sessionMap.keySet()) {
			WebSocketSession wss = sessionMap.get(key);
			
			if(userMap.get(wss.getId()) == null) {
				userMap.put(wss.getId(), (String)obj.get("userName"));
			}
			
			//클라이언트에게 메시지 전달
			wss.sendMessage(new TextMessage(obj.toJSONString()));
		}
	}
	
	/* 클라이언트가 소켓 연결시 동작 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("{} 연결되었습니다.", session.getId());
		super.afterConnectionEstablished(session);
		sessionMap.put(session.getId(), session);
		
		JSONObject obj = new JSONObject();
		obj.put("type", "getId");
		obj.put("sessionId", session.getId());
        
        //클라이언트에게 메세지 전달
		session.sendMessage(new TextMessage(obj.toJSONString()));
	}
	
	/* 클라이언트가 소켓 종료시 동작 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("{} 연결이 종료되었습니다.", session.getId());
		super.afterConnectionClosed(session, status);
		sessionMap.remove(session.getId());
		
		String userName = userMap.get(session.getId());
		for(String key : sessionMap.keySet()) {
			WebSocketSession wss = sessionMap.get(key);
			
			if(wss == session) continue;

			JSONObject obj = new JSONObject();
			obj.put("type", "close");
			obj.put("userName", userName);
			
			wss.sendMessage(new TextMessage(obj.toJSONString()));
		}
		userMap.remove(session.getId());
	}
	
	/**
	 * JSON 형태의 문자열을 JSONObejct로 파싱
	 */
	private static JSONObject jsonToObjectParser(String jsonStr) throws Exception{
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		obj = (JSONObject) parser.parse(jsonStr);
		return obj;
	}
}
//출처: https://fvor001.tistory.com/123 [Dev Log:티스토리]
```
  
## WebSocketConfig.java
Spring에서 WebSocket을 사용하기 위해 클라이언트가 보내는 통신을 처리할 Handler 필요.  
DevLogWebSocketHandler를 Handshake할 주소와 함께 추가  
주소는 PORT 뒤에 endpoint 입력  
```ws://127.0.0.1:80/chating```  
  
```java
@Configuration
@EnableWebSocket// 웹소켓 활성화
public class WebSocketConfig implements WebSocketConfigurer{

	@Autowired
	private DevLogWebSocketHandler devLogWebSocketHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// WebSocketHandler를 추가
		registry.addHandler(devLogWebSocketHandler, "/chating");
	}
}
//출처: https://fvor001.tistory.com/123 [Dev Log:티스토리]
```
  
## ChatController.java
```
@Controller
public class ChatController {
	@RequestMapping("/chat")
	public ModelAndView chat() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("chat");
		return mv;
	}
}
//출처: https://fvor001.tistory.com/123 [Dev Log:티스토리]
```
  
## application.properties
```
#Tomcat Server Setting
server.port=80

#ModelAndView Path Setting
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp

#JSP to Modify Not Restart Server
server.servlet.jsp.init-parameters.development=true
//출처: https://fvor001.tistory.com/123 [Dev Log:티스토리]
```
  
## Chat.jsp
```
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<meta charset="UTF-8">
	<title>DevLog Chating</title>
	<style>
		*{
			margin:0;
			padding:0;
		}
		.container{
			width: 500px;
			margin: 0 auto;
			padding: 25px
		}
		.container h1{
			text-align: left;
			padding: 5px 5px 5px 15px;
			color: #FFBB00;
			border-left: 3px solid #FFBB00;
			margin-bottom: 20px;
		}
		.chating{
			background-color: #000;
			width: 500px;
			height: 500px;
			overflow: auto;
		}
		.chating .me{
			color: #F6F6F6;
			text-align: right;
		}
		.chating .others{
			color: #FFE400;
			text-align: left;
		}
		.chating .start{
			color: #AAAAAA;
			text-align: center;
		}
		.chating .exit{
			color: red;
			text-align: center;
		}
		input{
			width: 330px;
			height: 25px;
		}
		#yourMsg{
			display: none;
		}
	</style>
</head>

<script type="text/javascript">
	var ws;

	function wsOpen(){
		//websocket을 지정한 URL로 연결
		ws = new WebSocket("ws://" + location.host + "/chating");
		wsEvt();
	}
		
	function wsEvt() {
		//소켓이 열리면 동작
		ws.onopen = function(e){
			
		}
		
		//서버로부터 데이터 수신 (메세지를 전달 받음)
		ws.onmessage = function(e) {
			//e 파라미터는 websocket이 보내준 데이터
			var msg = e.data; // 전달 받은 데이터
			if(msg != null && msg.trim() != ''){
				var d = JSON.parse(msg);
				
				//socket 연결시 sessionId 셋팅
				if(d.type == "getId"){
					var si = d.sessionId != null ? d.sessionId : "";
					if(si != ''){
						$("#sessionId").val(si); 
						
						var obj ={
							type: "open",
							sessionId : $("#sessionId").val(),
							userName : $("#userName").val()
						}
						//서버에 데이터 전송
						ws.send(JSON.stringify(obj))
					}
				}
				//채팅 메시지를 전달받은 경우
				else if(d.type == "message"){
					if(d.sessionId == $("#sessionId").val()){
						$("#chating").append("<p class='me'>" + d.msg + "</p>");	
					}else{
						$("#chating").append("<p class='others'>" + d.userName + " : " + d.msg + "</p>");
					}
						
				}
				//새로운 유저가 입장하였을 경우
				else if(d.type == "open"){
					if(d.sessionId == $("#sessionId").val()){
						$("#chating").append("<p class='start'>[채팅에 참가하였습니다.]</p>");
					}else{
						$("#chating").append("<p class='start'>[" + d.userName + "]님이 입장하였습니다." + "</p>");
					}
				}
				//유저가 퇴장하였을 경우
				else if(d.type == "close"){
					$("#chating").append("<p class='exit'>[" + d.userName + "]님이 퇴장하였습니다." + "</p>");
					
				}
				else{
					console.warn("unknown type!")
				}
			}
		}

		document.addEventListener("keypress", function(e){
			if(e.keyCode == 13){ //enter press
				send();
			}
		});
	}

	function chatName(){
		var userName = $("#userName").val();
		if(userName == null || userName.trim() == ""){
			alert("사용자 이름을 입력해주세요.");
			$("#userName").focus();
		}else{
			wsOpen();
			$("#yourName").hide();
			$("#yourMsg").show();
		}
	}

	function send() {
		var obj ={
			type: "message",
			sessionId : $("#sessionId").val(),
			userName : $("#userName").val(),
			msg : $("#chatting").val()
		}
		//서버에 데이터 전송
		ws.send(JSON.stringify(obj))
		$('#chatting').val("");
	}
</script>
<body>
	<div id="container" class="container">
		<h1>DevLog Chat</h1>
		<input type="hidden" id="sessionId" value="">
		
		<div id="chating" class="chating">
		</div>
		
		<div id="yourName">
			<table class="inputTable">
				<tr>
					<th>닉네임</th>
					<th><input type="text" name="userName" id="userName"></th>
					<th><button onclick="chatName()" id="startBtn">채팅 참가</button></th>
				</tr>
			</table>
		</div>
		<div id="yourMsg">
			<table class="inputTable">
				<tr>
					<th>메시지</th>
					<th><input id="chatting" placeholder="보내실 메시지를 입력하세요."></th>
					<th><button onclick="send()" id="sendBtn">보내기</button></th>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
//출처: https://fvor001.tistory.com/123 [Dev Log:티스토리]
```
  
## Dependency
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-websocket</artifactId>
</dependency>

<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
</dependency>

<!-- View JSP -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
    <scope>provided</scope>
</dependency>
<!-- View JSP -->

<!-- json simple  -->
<dependency>
  <groupId>com.googlecode.json-simple</groupId>
  <artifactId>json-simple</artifactId>
  <version>1.1.1</version>
</dependency>
<!-- json simple  -->
///출처: https://fvor001.tistory.com/123 [Dev Log:티스토리]
```
