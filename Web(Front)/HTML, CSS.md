## HTML
Hyper Text Markup Language  
Hyper Text : 참조를 통해 한 문서에서 다른 문서로 즉시 접근할 수 없는 텍스트  
Markup : 태그 등을 이용하여 문서나 데이터의 구조를 명기하는 언어  
웹페이지 작성을 위한 언어. 확장자 html, 태그 대소문자 구분 없음, 엔터 스페이스바, 탭 적용되지 않음  
구성요소) 태그, 내용  
기본구조) html, **head**, meta, **body**  
+) 특수문자 ```&nbsp;``` 공백, ```&lt;``` <, ```&gt;``` >, ```&amp;``` &, ```&quot;``` ", ```&copy;``` copyright 마크, ```&reg;``` registered trade마크  
  
semantic tag : 의미론적 요소 담은 태그
```<header> <nav> <aside> <section> <article> <footer> <h1>~<h6>```  
  
text content : ```<blockquote>``` 긴 인용문, ```<hr>``` 구분선, ```<pre>``` 공백, 줄바꿈 입력대로 화면에 표시, ```<p>``` 하나의 문단, ```<ul>``` 번호 없는 목록, ```<ol>``` 번호 있는 목록, ```<div>``` 구문 컨텐츠를 위한 블록 컨테이너  
= display의 block  
  
inline text semantics : ```<a>``` 하이퍼링크 생성, ```<b> <strong>``` 굵은 글씨, ```<br>``` 텍스트 내 줄바꿈, ```<i> <em>``` 기울게 표시, ```<q>``` 짧은 인라인 인용문, ```<s>``` 취소선, ```<u>``` 밑줄, ```<sup> <sub>``` 위 첨자/아래 첨자, ```<span>``` 구문 콘텐츠를 위한 인라인 컨테이너  
= display의 inline   
  
image & multimedia : ```<audio>``` 소리 콘텐츠를 포함할 때 사용, ```<img>``` 문서에 이미지 삽입, ```<video>``` 미디어 플레이어를 문서에 삽입  
  
table content : ```<table>```, ```<thead> <tbody> <tfoot>```, ```<tr> <th>``` 테이블 행, 머리글, ```<td>``` 데이터, ```<col>``` 표의 열, ```<colgroup>``` 표의 열을 묶는 그룹 정의, ```<caption>``` 표의 설명 또는 제목  
  
Forms : 사용자로부터 데이터 입력 받아 서버에서 처리하기 위한 용도( **submit**)  
```method="GET" 또는 "POST", name="form 이름", action="안의 내용 처리할 프로그램(url)", target="action 태그에서 지정한 스크립트 파일을 self 또는 blank(새 창) 어디서 열지 지정"```  
```<form>``` 정보 제출하기 위한 대화형 컨트롤을 포함하는 문서 구획  
```<button>``` 클릭 가능한 버튼, ```<input>``` 웹 기반 양식에서 사용자 데이터 받는 대화형 컨트롤  
```<label>``` 사용자 인터페이스 항목 설명, ```<select> <option>``` 옵션 메뉴 제공하는 컨트롤, 항목  
```<textarea>``` 멀티라인 일반 텍스트 편집 컨트롤  
```<fieldset>``` 웹 양식의 여러 컨트롤과 레이블을 묶을 때 사용, ```<legend>``` fieldset 콘텐츠 설명   
    
--------------
  
## CSS
Cascading Style Sheets  
HTML 문서를 화면에 표시하는 방식을 정의한 언어  

적용 방법) 
Internal style sheet : 파일 내 스타일 적용, head 태그 안의 style 태그 안에 css 규칙 작성. 외부 스타일 시트보다 우선 적용   
Inline style : tag 내에서 style 속성 사용 ```<h1 style="color: red; background-color: blue;">```  
**스타일 적용 운선순위 : 인라인 -> 내부 스타일 시트 -> 외부 스타일 시트 순**  

CSS 선택자 : HTML 문서에서 CSS 규칙을 적용할 요소 정의  
기본 선택자(전체/유형/클래스/id/특성 선택자), 그룹 선택자, 결합자(자손/자식 결합자, 일반 형제/인접 형제/열 결합자), 의사 클래스/의사 요소  
#### 기본 선택자
##### 전체 선택자 universal selector
HTML 문서 내 모든 element 선택.  ``` * { 스타일 속성 }```  
##### 유형 선택자 type selector
태그 명을 이용해 스타일 적용 태그 선택, HTML 내에서 주어진 유형의 모든 요소 선택  
``` element { 스타일 속성 }```  
##### 클래스 선택자 class selector
class가 적용된 모든 태그 선택,HTML 내에서 동일한 클래스 명 중복 사용 가능  
``` classname { 스타일 속성 }```  
##### id 선택자
동일한 id를 가진 태그 선택, HTML 내에서 주어진 ID를 가진 요소가 하나만 존재  
```#id값 {스타일 속성}```  
  
#### 선택자 목록, 그룹 선택자
선택자, 선택자로 그룹 생성하여 모든 일치하는 노드 선택  
```element, element { 스타일 속성 }```  

##### 자손 결합자 descendant combinator
첫 번째 요소의 자손인 노드 선택 ```selector1 selector2 { 스타일 속성 }```  
##### 자식 결합자 chlid combinator
첫 번째 요소의 바로 아래 자식인 노드 선택 ```selector1 > selector2 { 스타일 속성 }```  
##### 일반 형제 결합자 general sibling combinator
첫 번째 요소를 뒤따르면서 같은 부모를 공유하는 두 번째 요소 모두 선택 ```former-element ~ target-element { 스타일 속성 }```  
##### 인접 형제 결합자 adjacent sibling combinator
첫 번째 요소의 바로 뒤에 위치하면서 같은 부모 공유하는 두 번째 요소 선택 ```former-element + target-element { 스타일 속성 }```  
   
우선순위 = 같은 요소에 두 개 이상의 css 규칙 적용 시 **마지막 규칙, 구체적인 규칙, !important 가 우선 적용**  
  
#### 상속 inheritance
부모 요소에 적용된 스타일이 자식 요소에게 상속 가능  
요소의 상속되는 속성에 값이 지정되지 않은 경우 = 요소는 부모 요소의 해당 속성 계산 값을 상속받음  
부모에 어떤 속성 값이 지정되지 않은 경우 = 속성 초기값을 얻음  
  
css **크기** 단위 = length, %, auto(width = 100%, height = 0%)  
css **색상** 단위 = 색상 키워드 대소문자 구분 x, RGB(16진수 표기법, 함수형 표기법)/ HSL(색상, 채도, 명도)  
**css font** = ```font-family``` 글꼴 지정, ```font-size```, ```font-style```, ```font-variant``` 소문자를 작은 대문자로 변형, ```font-weight``` 글자 굵기 지정  
**css text**  
```text-align``` text 정렬 방식, ```text-decoration``` text 장식, ```text-indent``` text-block 안 첫 라인의 들여쓰기, ```text-transform``` text 대문자화, ```white-space``` 엘리먼트 안의 공백, ```vertical-align``` 수직 정렬, ```letter-spacing``` 문자 간 space 간격, ```word-spacing``` 단어 간 간격, ```line-height``` 줄(행) 간격, ```color``` text 색상  
**background**  
```background-color```, ```background-image``` 배경 이미지, ```background-attrachment``` 배경 이미지 고정하거나 scroll 여부, ```background-repeat``` 배경 그림 반복 여부, ```background-position``` 배경 그림 위치, ```background-size``` 배경 이미지 크기, ```background-clip``` 배경 적용 범위 조절  
  
#### box model
margin(테두리 밖 외부 여백) - border(테두리) - padding(테두리 안쪽 내부 여백) - 실제 내용  
**margin** = box의 마진 영역 너비 지정 : 값 1개(모든 면), 2개(상하/좌우), 3개(상/좌우/하), 4개(상,우,하,좌)  
*margin 0 auto 를 통해 가운데 정렬이 되도록 설정 가능* *마진상쇄 현상*  
  
#### display
**display : block** = 화면 크기 전체의 가로 폭 차지(줄바꿈), 블록 레벨 요소 안에 인라인 레벨 요소 들어갈 수 있음 (ex. div, ui, oi, li, p, hr, form)  
**display : inline** = content 너비만큼 가로폭 차지(줄 바꿈 x), width/height/margin-top/margin-bottom 지정 불가, line-height로 상하 여백 지정 (ex. span, a, img, input, label, b, e, i, strong)  
**display : inline-block** = inline처럼 한줄에 표시 가능, block 처럼 width/height/margin 지정 가능  
**display : none** = 화면에 표시 x, 공간 차지 x ***(visibility : hidden 은 화면 표시 x, 공간 차지 o)***
  
#### position
**position : static** = 일반적인 내용물의 흐름, 상단/좌측에서의 거리 지정 불가  
**position : relative** = HTML 문서에서의 일반적 내용물 흐름(top, left)   
**position : absolute** = 자신의 상위 box 속에서의 top, left, right, bottom 절대적 위치 지정  
**position : fixed** = 스크롤해도 화면 상 지정된 위치에 있음  
*z-index 위 아래 배치*   
  
#### float
박스를 어느 위치에 배치할 것인지 결정  
**float : none** = 기본값, **left** = 요소를 왼쪽으로, **right** = 요소를 오른쪽으로  
  
#### clear 
float 속성이 가지고 있는 값을 초기화하기 위해 사용  
**none**, **left, right**, **both**  
  
### flexbox
인터페이스 내의 아이템 간 공간 배분, 정렬 기능 제공하기 위한 1차원 레이아웃 모델 설계  
**Main Axios**(주축), **Cross Axios**(교차축), **start**(시작선), **end**(끝선),  **item**들을 감싸고 있는 **Container**    

