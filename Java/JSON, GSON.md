## JSON
제이슨 **JavaScript Object Notation** : key, value의 쌍으로 표현  
자바스크립트 토대, 여러 프로그래밍 언어에서 사용 가능한 독립형 언어. 웹 클라이언트와 웹 서버 간 데이터 교환에 사용  
웹 브라우저 비동기 처리에 사용되는 AJAX의 데이터 교환 형식으로 널리 알려짐  
*최근에는 XML보다 JSON 많이 사용*  
(장점) 주요 FE 프레임워크에 의해 지원됨, 공식 포맷이라 개발자 간 데이터 통신 가능, 텍스트로 이루어져 있어 읽고 쓰기 쉬움, xml에 비해 용량이 적고 이해하기 쉬움, 언어와 플랫폼에 독립적이라 서로 다른 시스템 간 데이터 교환에 좋음  
```{"key" : value, ...}``` = key는 " "로 묶어서 표현, Value는 String일때 " "로 묶어서 표현  
: value는 object, array, number, string, true, false, null로 사용  
**Object** 표현 시 { "id" : "ssafy"**,** "name" : "싸피" }  
**Array** 표현 [ "hong", "kim", "park", "lee" ]  
*Array 메서드는 보통 화살표 함수랑 같이 씀* ```((파라미터)=>{함수 내용})```  
map : 새로운 배열 만들 때 ```arr.map(function(num){ return num*2; })```      
filter : 원하는 값만 골라 새로운 배열 만들 때 ```arr.filter((num) => num%2 === 1);```  
  
클래스 객체와 JSON 변환 : ```class <-> {}```  ```Array, List <-> []```  
  
## GSON
구글 GSON = Java 객체를 JSON 표현으로 변환하는 데 사용할 수 있는 라이브러리  
JSON 문자열을 Java 객체로, Java 객체를 JSON 문자열로 변환하는 간단한 방법 제공 ```toJson(), fromJson()```  
GSON을 사용하기 위해 라이브러리를 다운 받고 프로젝트에 인식시켜야 함  
mvn레포지토리 사이트에서 gson jar 파일 다운 = Build Path - Configure Build Path - Library 에서 Add Jars  
```java
String str = "{\"name\":\"이상혁\",\"age\":27,\"hobby\":\"게임\"}"; // 큰 따옴표 안에서 기호 "를 사용하기 위해서는 앞에 \붙이고 \" 써야 함  
Gson gson = new Gson(); //GSON 객체 생성
Person p = gson.fromJson(str, Person.class); //str
```   
