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
   
--------------------------------------------
  
# Spring Boot에서 WebSocket 구축 및 사용하기
**[Browser > Server]**: HTTP 사용자 요청 - 서버 응답하면 연결 끊김. 페이지 이동 있음.  
**[XMLHttpRequest > Server]**: XMLHttpRequest 객체가 서버에 요청하는 방식으로 서버와 연결. Json이나 xml 형태로 필요한 데이터만 주고 받을 수 있고, 변경이 필요하면 해당 페이지 내에서 변경 가능.  
**WebSocket**: Stateful Protocol. 서버와 클라이언트 간의 연결을 항상 유지. 비정상적으로 연결이 끊어졌을 깨 대응 필요.
  
**어노테이션 ```@ServerEndpoint``` 선언 클래스**와 **WebSocket 환경 설정 파일**  
## 1. websocket 의존성 추가
```implementation 'org.springframework.boot:spring-boot-starter-websocket'```  
## 2. WebSocketConfig > Bean 생성
```java
@Component
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        /*
            2022.10.26[프뚜]:
                Spring에서 Bean은 싱글톤으로 관리되지만,
                @ServerEndpoint 클래스는 WebSocket이 생성될 때마다 인스턴스가 생성되고
                JWA에 의해 관리되기 때문에 Spring의 @Autowired가 설정된 멤버들이 초기화 되지 않습니다.
                연결해주고 초기화해주는 클래스가 필요합니다.
         */
        return new ServerEndpointExporter();
    }
}
```
## 3. WebSocket 구현하기
WebSocketChatting 클래스에 ```@ServerEndpoint(value = "/chatt"), @Service``` 어노테이션 붙여서 서비스 등록  
```@OnOpen```: 사용자가 접속하면 session 추가 / ```@OnClose```: 사용자가 종료되면 session 제거    
```@OnMessage```: 사용자가 입력한 메세지를 받고, 접속되어 있는 사용자에게 메세지 전송  
  
```java
@ServerEndpoint(value = "/chatt")
@Service
public class WebSocketChatting {

    private static Set<Session> CLIENTS = Collections.synchronizedSet(new HashSet<>()); //사용자를 관리할 수 있는 Set 전역변수 선언

    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.toString());

        if (CLIENTS.contains(session)) {
            System.out.println("[프뚜] 이미 연결된 세션입니다. > " + session);
        } else {
            CLIENTS.add(session);
            System.out.println("[프뚜] 새로운 세션입니다. > " + session);
        }
    }

    @OnClose
    public void onClose(Session session) throws Exception {
        CLIENTS.remove(session);
        System.out.println("[프뚜] 세션을 닫습니다. : " + session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        System.out.println("[프뚜] 입력된 메세지입니다. > " + message);

        for (Session client : CLIENTS) {
            System.out.println("[프뚜] 메세지를 전달합니다. > " + message);
            client.getBasicRemote().sendText(message);
        }
    }
}
```
## 4. WebSocket과 연동되는 js 구현하기
Server에서 sendText를 받을 수 있는 function 선언, 사용자가 Server에 메세지를 보내는 함수 선언.  
```js
let ws;
const mid = getId('mid');
const btnLogin = getId('btnLogin');
const btnSend = getId('btnSend');
const talk = getId('talk');
const msg = getId('msg');

// 2022.10.26[프뚜]: 전송 데이터(JSON)
const data = {};

function getId(id) {
    return document.getElementById(id);
}

btnLogin.onclick = function() {
    // 2022.10.26[프뚜]: 서버와 webSocket 연결 
    ws = new WebSocket("ws://" + location.host + "/chatt"); //@ServerEndpoint의 value값으로 연결. 

    // 2022.10.26[프뚜]: 서버에서 받은 메세지 처리
    ws.onmessage = function(msg) { //Server에서 sendText를 받을 수 있는 function 선언
        const data = JSON.parse(msg.data);
        let css;

        if (data.mid === mid.value) {
            css = 'class=me';
        } else {
            css = 'class=other';
        }

        const item = `<div ${css} >
		                <span><b>${data.mid}</b></span> [ ${data.date} ]<br/>
                      <span>${data.msg}</span>
						</div>`;

        talk.innerHTML += item;

        // 2022.10.26[프뚜]: 스크롤바 하단으로 이동
        talk.scrollTop=talk.scrollHeight;
    }
}

msg.onkeyup = function(ev) {
    if (ev.keyCode === 13) {
        send();
    }
}

btnSend.onclick = function() {
    send();
}

function send() { //사용자가 Server에 메세지를 보내는 함수 선언
    if (msg.value.trim() !== '') {
        data.mid = getId('mid').value;
        data.msg = msg.value;
        data.date = new Date().toLocaleString();
        const temp = JSON.stringify(data);
        ws.send(temp);
    } 
    msg.value = '';
}
```
  
+) https://github.com/JeongSeongSoo/spring-tistory  
  
https://ssjeong.tistory.com/entry/JAVA-Spring-Boot%EC%97%90%EC%84%9C-WebSocket-%EA%B5%AC%EC%B6%95-%EB%B0%8F-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
