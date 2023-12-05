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
상속되는 
