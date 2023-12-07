JavaScript : 웹 브라우저 UI 제어를 위한 프로그래밍 언어. Node.js 이용해 콘솔 환경에서 사용 *ES6*  
```<script> 문서 내 위치 제약 없음 </script>```  
외부스크립트 참조 ```<script src="외부파일위치.js"> .js 확장자 파일 생성</script>```  
#### 변수
변수 : 가리키는 값에 대한 타입 나타냄. **동적 타입**(대입되는 값에 따라 용도 변경)  
(var,) let, const 사용해서 선언. 문자, $, _로 시작  
데이터 타입 : 기본 데이터 타입(문자열 String, 숫자형 Number, Boolean, null, undefined)/ 객체 타입(Object : function, array 등)/ Symbol    
``` typeof (데이터)``` 하면 타입 문자열 반환. ```typeof null = object```  
  
**var** = **호이스팅**(밑에 정의해도 위에서 써먹을 수 있음) 특성 있음, 재 선언 가능, 재 할당 가능. 함수 스코프  
**let** = 재 선언 불가, 재 할당 가능. 블록 스코프  
**const** = 재 선언 불가, 재 할당 불가. 블록 스코프 SNAKE_CASE 사용, 선언 시 값 할당해야 함. 상수로 사용  
  
**undefined** : 변수에 값이 대입되지 않아서 타입을 알 수 없음  
```JavaScript
let name = "홍길동";
let msg = `나의 이름은 ${name}입니다.`; //`` 안에는 ${} 사용해서 변수 넣기 가능
```
문자열 연산 : 문자열+숫자 = 문자열 ``` 1+"20" = 120```, **+ 외의 연산 = 숫자** ```"100"-8 = 92```  
null, undefined, 0, '', NaN = **false**로 인식  
**일치 연산자** = 값과 타입 일치 체크 ```===```  
  
**배열** : [] 또는 Array() 로 생성, 크기 **동적** 변경, **여러 데이터 타입**을 하나의 배열에 입력 가능, **push**(맨 뒤에 추가), **unshift**(맨 앞에 추가), **shift**(맨 앞에서 제거해 반환), **includes**(값 있나 확인)   
  
**객체** Object : 문자열로 이름을 붙인 값들의 집합체 Key: Value. 객체에 저장하는 값 = **property**  
*객체는 prototype 프로퍼티를 가짐*  
객체 리터럴 ```{}```, ```new Object()```, 생성자 함수 function mem(){} 만들고 ```let mem1 = new mem();```  
```객체.프로퍼티``` 또는 ```객체 ["id"]``` 같이 property 조회 변경 가능  
*객체 변수에는 주소 저장*  
  
***함수 안에서 this는 함수를 호출한 객체***  
```JavaScript
var m1 = {name: "홍길동"};
function msg () {
  console.log(this);//함수를 호출한 객체 = m1(그 메서드를 가진 객체)
  console.log(this.name+"님이 입장");
}
m1.msg = msg; //m1에 msg라는 property를 추가함
//{name: "홍길동", msg: function (){ console.log(this); } } 상태
m1.msg();
```
함수 특징) 함수를 객체 타입 값처럼 사용(변수 대입, 매개변수 사용 가능, 배열 요소, 객체 프로퍼티로 설정) => **일급 객체** First-class citizen  
함수 **선언식** ```function 함수명() { 함수 내용 }```, 함수 **표현식** ```let 함수명 = function () { 함수 내용 }```  
함수 선언식은 호이스팅됨 (밑에서 선언해도 위에서 실행 됨)  
**익명 함수**로 정의 가능 ```let func = function () { 표현식 }```   
함수 리턴값 함수로도 가능  
함수 매개변수 : **arguments**라는 함수 내부 프로퍼티로 **매개변수** 처리 가능, 기본 인자 사용 가능  
함수 호출 시 매개변수의 영향을 받지 않음... **오버로딩이 안 되고** 덮어쓰기가 됨.   
  
-----------------
   
window(전역 객체, 최상위 객체) - 그 밑에 DOM(문서요소: document, ...), BOM(브라우저: navigator, screen, location, frames, history, XHR), JavaScript(Object, Array, Function, ...)  
  
#### window 제공 함수
```window.alert("window 대신 this, self, frames 사용 가능")```   
**alert** : 경고창  
**confirm** : 확인창  
**prompt** : 입력창  
open : 창을 연다    
**parseInt, parseFloat** : 문자열을 정수, 실수로 바꿀 때    
setTimeout, clearTimeout : 몇 초 후에 실행할지  
setInterval, clearInterval    
*globalThis : 전역 객체 식별자*  
   
## DOM
Document Object Model : XML, HTML 문서의 각 항목을 계층으로 표현하여 생성, 변형, 삭제할 수 있도록 돕는 인터페이스  
```document.getElementById(string)```, ```document.querySelector(css selector)```, ```document.querySelectorAll(css selector) //결과를 배열처럼 사용```   
createElement(tagName), createTextNode(text) = 만들고  
appendChild(node), append(string | node) = 추가하고  
removeChild(node),  
setAttribute(name, value) = 생성된 element에 속성 추가, 사용자 정의 속성    
innerHTML, innerTEXT  
  
## EVENT
웹 페이지에서 발생하는 상호작용에 대한 이벤트 발생 -> js를 사용해 DOM에 발생하는 이벤트 감지하고 대응하는 작업 수행  
키보드 : keyup, keydown, keypress  
마우스 : click, mousemove, mouseup, mousedown, mouseenter, mouseleave  
로딩 : load, unload  
폼 : input, change, blur, focus, submit  

**이벤트 전파** event propagation  
**캡쳐링** 단계 capturing : 이벤트가 상위 요소에서 하위 요소 방향으로 전파  
**타깃** 단계 target : 이벤트가 타깃에 도달  
**버블링** 단계 bubbling : 이벤트가 하위 요소에서 상위 요소 방향으로 전파  
  
**고전** 이벤트 처리 방식 = **attribute/ property** 방식으로 등록.  
on이벤트 종류 형식으로 엘리먼트에 직접 지정(인라인 또는 함수 생성하고 호출)/  
스크립트에서 이벤트 지정      
```js
var btn = document.querySelector("#btn");
btn.onclick = doAction;
function doAction(){
  alert("클릭");
}
```  
타깃 단계와 버블링 단계의 이벤트만 캐치 가능  
**표준** 이벤트 처리 방식 = ```이벤트요소.addEventListener(이벤트 타입, 이벤트 리스너, [option]);``` 이용  
타깃 단계, 버블링 단계, **(메서드 3번째 인자로 true 전달해주면) 캡쳐링 단계의 이벤트** 선별적 캐치 가능.  
