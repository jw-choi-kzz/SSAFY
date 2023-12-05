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


## GSON
