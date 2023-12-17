# Servlet
웹 프로그래밍 Web Architecture   
![image](https://github.com/jw-choi-kzz/SSAFY/assets/141205653/618771ed-49e2-4203-9aa5-11a61fda94b3)
*모든 페이지를 웹서버에서 정적으로 갖고있기는 힘들다* => 웹 서버와 어플리케이션 서버를 합친 **WAS**를 통해 페이지를 만들어서 전달  
WAS 종류가 많이 있지만 우리는 **tomcat**을 사용  
URL (Uniform Resource Locator) : 웹 상의 자원을 참조하기 위한 웹 주소  
웹 페이지 : 웹 브라우저를 통해서 보여지는 화면  
웹 서버 : 클라이언트 요청에 맞는 응답(웹 페이지)을 제공  
웹 어플리케이션 : 웹 서버를 기반으로 실행되는 응용 소프트웨어   
웹 어플리케이션 서버 **Web Application Server = WAS** : 요청이 오면 알맞은 프로그램을 실행하여 응답을 만들고 제공하는 서버  
*이클립스의 server에 Tomcat 9.0.80 를 등록*  
#### Dynamic Web Project 구조  
##### Java Resources > src
Web Application 실행에 필요한 **java** 관련 resource 포함  
##### WebContent > META-INF, WEB-INF
Web Application 실행에 필요한 **html, javascript, css, JSP, Image** 등 웹 콘텐츠를 포함  
web.xml (Web Application 설정파일)은 WebContent > WEB-INF에 위치  
### Servlet  
Server + Applet(Application + let 작은 프로그램?) 합성어  
자바를 이용해 웹에서 실행되는 프로그램을 작성, 웹 페이지를 동적으로 생성하는 것  
이미 만들어진 Servlet API를 활용 : javax.servlet > Interface Servlet, Class GenericServlet. javax.servlet.http > Class HttpServlet  

Servlet parameter  
