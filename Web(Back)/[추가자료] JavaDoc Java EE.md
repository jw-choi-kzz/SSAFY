https://javaee.github.io/javaee-spec/javadocs/   
## Interface Servlet
Servlets receive and respond to requests from Web clients, usually across HTTP(HyperText Transfer Protocol)  
### Servlet 생명 주기 메서드
init (생성, 초기화. 파라미터 ServletConfig) - service (요청 처리. 파라미터 ServletRequest, ServletResponse) - destroy (서블릿 종료 및 자원 정리)  
```init``` 메서드 : 서블릿 인스턴스 생성 후 딱 한번만 호출, 서블릿이 요청 받기 전에 성공적으로 완료되어야 함. 파라미터 = ServletConfig 객체(서블릿 구성, 초기화 파라미터 포함)  

getServletConfig(return: ServletConfig), getServletInfo(return: String)  
인터페이스 구현 => javax.servlet.GenericServlet, javax.servlet.http.HttpServlet  
