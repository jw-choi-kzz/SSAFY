# JDBC
Java Database Connectivity  
Java 프로그램에서 DB에 일관된 방식으로 접근 할 수 있도록 API를 제공하는 클래스의 집합  
## <방법>  
JDBC 드라이버 로드(JVM 메모리) - DB 연결 - SQL문 실행 or 준비 - DB 연결 해제   
### (1) JDBC 드라이버 로드
JDBC 드라이버를 프로그램 시작 시 로딩; jar 추가 또는 Maven pom.xml에서 추가  
```java
//생성자 만들고
Class.forName("com.mysql.cj.jdbc.Driver");
//예외는 try~catch로 처리

//main 메서드에서
JDBCTest db = new JDBCTest();
```  
### (2) DB 연결
```DriverManager``` class의 static method ```getConnection(URL, UserId, UserPassword)```로 요청  
```java
Connection conn = DriverManager.getConnection("URL","ID","PW");
//Connection은 인터페이스라서 new 연산자 사용 안함
//URL, ID, PW를 담은 객체를 만들어서 Connection 타입으로 반환  
```  
### (3) SQL 실행
Statement 객체 필요. Connection 객체 이용하여 ```createStatement()``` method로 생성  
결과 값이 여러 레코드로 나올 때는 ```executeQuery(String sql)```, 테이블 변경만 되는 경우(반환은 int)```executeUpdate(String sql)```  
### (4) DB 연결 해제
**DBUtil**: MySQL DB 연결 객체 제공, 사용 자원 해제 기능 제공  
Singleton pattern 적용  
```public static void close(Connection conn, PreparedStatement pstmt)```, ```close(Connection conn, PreparedStatement pstmt, ResultSet rs)```  
또는 전개연산자 사용 ```public void close(AutoCloseable...closeables)```: if (c != null) 이면 c.close()  
  
* *BoardDaoImpl 에서 DBUtil 사용 필요*. ```private DBUtil util = DBUtil.getInstance();```  
* *try ~ catch ~ finally에서 종료는 항상 finally 부분에 써줄 것*
* *BoardDaoImpl에서 insertBoard는 VALUES (?,?,?) 처럼 '?'기호 사용 가능. 미완성 SQL문과 Connection 속 prepareStatement method로 가져오고, setString에서 get뭐시기를 통해 ? 부분을 채운다*
* 
