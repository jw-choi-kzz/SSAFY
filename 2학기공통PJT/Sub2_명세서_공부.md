WebRTC 기술 학습과 주요 기능 구현  
# 1. 주요 개념 정리
# REST API 방식  
## REST
Representation State Transfer: 웹 장점을 최대한 활용할 수 있는 아키텍처  
REST 구성: 자원 Resource(URI), 행위 Verb(HTTP Method), 표현 Representations  
### REST 특징
**유니폼 인터페이스 Uniform**: URI로 지정한 리소스에 대한 조작을 통일되고 한정적인 인터페이스로 수행하는 아키텍쳐  
**무상태성 Stateless**: 세션이나 쿠키 등의 상태 정보를 따로 저장하여 관리하지 않고, API 요청에 대한 처리만 수행  
**캐시 가능 Cacheable**: Last-Modefied나 E-Tag를 이용한 캐싱 등 기본적으로 웹 HTTP 프로토콜이 가지는 캐싱 기능 활용  
**자체 표현 구조 Self-descriptiveness**: REST API의 규격과 메시지만으로 API의 동작을 쉽게 이해할 수 있는 자체 표현 구조  
**Client-Server 구조**: REST API 서버는 API 제공, 클라이언트는 사용자 인증, 컨텍스트(세션, 로그인 정보) 등을 직접 관리하는 구조로 역할 분리 => Client와 Server 간 상호 의존성을 줄임  
**계층형 구조**: REST API 서버는 보안, 로드 밸런싱, 암호화, Proxy, API Gateway 등 추가적 계층을 구축하여 구조상의 유연함을 높임  
### REST API 설계
REST는 **자원 Resource**의 사용을 위해 URI 사용  
자원에 대한 **행위 Verb**는 HTTP Method(GET, POST, PUT, DELETE)로 표현  
#### URI 설계
URI는 자원을 표현 / 소문자로 / 긴 URI는 하이픈-사용  
#### HTTP Method
HTTP Method 별 용도  
POST (리소스 생성) / GET (조회) / PATCH (리소스 일부 속성 수정) / PUT (리소스 속성 덮어쓰기 또는 일부 속성 삭제) / DELETE (삭제)  
#### User Resource 예시
User  
|URI|HTTP Method|뜻|
|--|--|--|
|/users|GET|유저 목록 조회|
|/users|POST|유저 정보 추가|
|/users/{userId}|GET|userId = 1 인 유저의 상세 정보 조회|
|/users/{userId}|**PATCH**|userId = 1 인 유저의 상세 정보 **수정**|
|/users/{userId}|DELETE|userId = 1 인 유저 정보 삭제|
|/users/{userId}/apartments|GET|userId = 1 인 유저가 가지는 아파트 정보|  
#### Query Parameter 설계
URI에 ```?```나 ```&```를 붙여서 key-value 쌍으로 Query Parameter 추가  
|Query Parameter|HTTP Method|뜻|
|--|--|--|
|/users?name=최지원|GET|유저 이름이 최지원인 유저 목록 조회|
|/users?role=Admin,Role|GET|유저의 역할이 Admin 또는 Role인 유저 목록 조회|
|/users?ids=1,2,4|GET|userId = 1 or 2 or 4 인 유저 목록 조회|
|/users?name=최지원&sort=+id,-name|GET|유저의 이름이 최지원이고 userId를 오름차순 정렬, 유저 이름 내림차순 정렬한 목록 조회|  
  
# JPA에서의 테이블 매핑
## N:1 관계
7
## N:N 관계
8
  
# WebSocket
## SockJS
FE, BE 간 연결 및 메시지 송수신 구현을 지원해주는 WebSocket Polyfill  
WebSocket 객체 제공하는 브라우저 JS 라이브러리  
브라우저와 웹 서버 사이에 낮은 지연시간, 전체 이중/교차 도메인 통신 채널 생성  
Native WebSocket 사용, 실패 시 브라우저별 전송 프로토콜 사용해 추상화 통해 보여줌. 
```var sockjs = new SockJS(url, _reserved, options);``` option: server(string), transports(string, array), sessionId, timeout  
```js
const sock = new SockJS('api/v1/ws');
sock.onopen = function() {
  console.log('open');
  sock.send('test');
};
sock.onmessage = function(e) {
  console.log('message', e.data);
  sock.close();
}
sock.onclose = function() {
  console.log('close');
}
```
## Stomp
단순 텍스트 기반의 메시지 프로토콜(양방향 통신 프로토콜)  
통신 연결, 해제, 메시지 브로커 => 클라이언트는 특정 Topic에 대한 Subscribe 사용 가능  
해당 Topic으로 다른 클라이언트가 메시지 Send => 그 Topic을 Subscribe 하고 있는 클라이언트 모두 메시지를 받음  
```js
const socket = new SockJS
```
