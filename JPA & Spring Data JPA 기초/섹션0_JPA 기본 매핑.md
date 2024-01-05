JPA Java(2.2)/Jakarta(3.0) Persistence API      
자바 객체와 관계형 DB 간의 매핑 처리를 위한 API  
  
# 01 일단 해보기
## JPA 특징
어노테이션을 이용한 매핑 설정(xml 파일을 이용한 매핑 설정도 가능)  
String, int, LocalData 등 기본적인 타입에 대한 매핑 지원  
커스텀 타입 변환기 지원: 내가 만든 Money 타입을 DB 컬럼에 매핑 가능  
밸류 타입 매핑 지원: 한 개 이상 칼럼을 한 개 타입으로 매핑 가능   
클래스 간 연관 지원: 1-1, 1-N, N-1, N-M  
상속에 대한 매핑 지원   
  
EntityManager를 이용하여 DB 연동 처리. **쿼리 작성 없이** 객체 변경만으로 DB 테이블 업데이트

# 02 코드 구조 & 영속 컨텍스트
## 영속 컨텍스트 Persistence context
|응용 프로그램||영속컨텍스트||DB|
|--|--|--|--|--|
|엔티티 객체| ➡️ |영속 객체| ➡️ |레코드|
|엔티티 객체| ⬅️ |영속 객체| ⬅️ |레코드|
* DB에서 읽어온 객체 or 응용프로그램 EntityManager를 통해 저장한 객체 or EntityManager를 통해 읽어온 객체를 저장하고 있는 일종의 메모리 공간  
* 영속 컨텍스트 영속 객체들을 **보관**하고 있다가 **commit하는 시점**에 영속 컨텍스트의 변경 내역이 있는지 확인하고(변경 추적), 변경 내역이 있으면 DB에 반영
*이런 과정은 spring이 대부분 해준다..*  

# 03 엔티티 CRUD 처리
## EntityManager가 제공하는 메서드
```persist()``` : 저장  
```find()``` : 조회  
```remove()``` : 삭제  
+) merge()
**변경은 transaction 안에서 해야 한다**

# 04 엔티티 매핑 설정
## 기본 어노테이션
```@Entity```: 엔티티 클래스에 설정 **<필수>**   
```@Table```: 매핑할 테이블 지정   
```@Id```: 식별자 속성에 설정 **<필수>**  
```@Column```: 매핑할 column 명 지정 *(지정하지 않으면 field/property 명 사용)*  
```@Enumerated```: enum 타입 매핑할 때 설정  
   
+) 거의 안씀  
```@Temporal```: java.util.Date, java.util.Calender 매핑  
```@Basic```: 기본 지원 타입 매핑  
  
### @Table 어노테이션
어노테이션 생략 시 class 이름과 동일한 이름에 mapping  
#### 속성
**name**: table 이름 *생략 시 class 이름과 동일한 이름*  
**catalog**: catalog 이름 *ex) MySQL DB 이름*   
**schema**: schema 이름 *ex) Oracle schema 이름*   
### @Enumerated 어노테이션
#### 설정 값
**EnumType.STRING**: enum 타입 값 이름을 저장 *문자열 타입 칼럼에 매핑*  
**EnumType.ORDINAL**: enum 타입의 값의 순서를 저장 *숫자 타입 칼럼에 매핑*
  
## 엔티티 클래스 제약 조건
```@Entity``` 적용해야 함. ```@Id``` 적용해야 함.  
인자 없는 기본 생성자 필요. 접근은 public 또는 protected.  
final이면 안 됨. 최상위 클래스여야 함.  

# 05 엔티티 식별자 생성 방식

# 06 @Embeddable

# 07 @Embeddable 다른 테이블에 매핑하기
