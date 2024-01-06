> inflearn 강의명 : JPA & Spring Data JPA 기초   
> 섹션 0 JPA 기본 매핑의 01~07번까지 정리    

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
  
## 접근 타입
**필드 접근**: 필드 값을 사용해서 매핑  
**프로퍼티 접근**: getter/setter 메서드를 사용해서 매핑  
### 설정 방법
* ```@Id``` 어노테이션을 어디에 붙였는가?
  * **field**에 붙이면 필드 접근, **getter** method에 붙이면 프로퍼티 접근    
* ```@Access``` 어노테이션을 사용해서 명시적으로 지정: ```@Access(AccessType.PROPERTY)```, ```@Access(AccessType.FIELD)```
  * Class, 개별 field에 적용 가능 
  
# 05 엔티티 식별자 생성 방식
직접 할당/ 식별 칼럼 방식/ 시퀀스 사용 방식/ 테이블 사용 방식  
## 직접 생성 방식
* ```@Id``` 설정 대상에 직접 값 설정: 사용자가 입력한 값, 규칙에 따라 생성한 값 등  
* 저장하기 전에 생성자 할당, 보통 생성 시점에 전달  
## 식별 칼럼 방식
* DB의 식별 칼럼에 매핑 *ex) MySQL 자동 증가 컬럼*   
  * *DB가 식별자를 생성하기 때문에 객체 생성시에 식별값을 설정하지 않음*  
* ```@GeneratedValue(strategy= GenerationType.IDENTITY)``` 설정   
* INSERT 쿼리 실행해야 식별자 알 수 있음  
  * EntityManager #persist() 호출 시점에 INSERT 쿼리 실행. persist() 실행 시 객체에 식별자 값 할당됨  
```java
@Entity
public class Review {
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;
~~
}

Review review - new Review(5, "H-01", ~); //객체 생성 시점에는 식별자를 따로 지정하지 않고
entityManager.persist(review); //저장(persist 메서드) 시점에 INSERT 쿼리 바로 실행
Long genId = review.getId(); //persist() 이후 식별자 사용 가능  
```  
## 시퀀스 사용 방식
* 시퀀스를 사용해서 식별자 생성: JPA가 식별자 생성 처리 -> 객체 생성 시에 식별값을 설정하지 않음  
* ```@SequenceGenerator```로 시퀀스 생성기 설정, ```@GeneratedValue의 generator로 시퀀스 생성기 지움
* EntityManager #persist() 호출 시점에 시퀀스 사용
  * ```persist()``` 실행할 때 객체에 식별자 값 할당됨
  * INSERT 쿼리는 실행하지 않음
```java
@Entity
@Table(schema ="crm", name= "activity_log")
public class ActivityLog {
  @Id
  @SequenceGenerator(
    name= "log_seq_gen",//이것과
    sequenceName= "activity_seq",
    schema= "crm",
    allocationSize= 1 //1로 지정해야 한다!!! 높게 하면 중복 시퀀스 생성 문제 발생
)
@GeneratedValue(generator= "log_seq_gen")//이것이 같은 이름 사용
private Long id;
~
}
```  
## 테이블 사용 방식
* 테이블을 시퀀스처럼 사용  
  * 테이블에 엔티티를 위한 키 보관. 해당 테이블을 이용해서 다음 식별자 생성  
* ```@TableGenerator```로 테이블 생성기 설정, ```@GeneratedValue```의 generator로 테이블 생성기 지정
* EntityManager #persist() 호출 시점에 테이블 사용: ```persist()``` 할 때 테이블을 이용해서 식별자 구하고 엔티티에 할당. INSERT 쿼리는 실행하지 않음  
```MySQL
create table id_seq (
  entity varchar(100) not null primary key,
  nextvalbigint
)
```
```java
@TableGenerator(
table= "id_seq",
pkColumnName= "entity",
valueColumnName= "nextval",
~~
)

@GeneratedValue(generator= "accessIdGen")
private Long id;
```  
   
# 06 @Embeddable
엔티티가 아닌 타입을 한 개 이상의 필드와 매핑할 때 사용  
엔티티의 한 속성으로 ```@Embeddable``` 적용 타입 사용  
```java
@Embeddable
public class Address {
}

@Entity
@Table(name = "hotel_info")
public class Hotel {
  @Id
  @Column(name= "hotel_id")
  private String id;
  ~
  @Embedded
  private Address address;
}
```  
null을 넣으면 address에 매핑된 모든 것이 null  
같은 ```@Embeddable``` 타입 필드가 2개면 Repeated column Error 발생  
```@AttributeOverride```로 설정 재정의 : ```@AttributeOverrides```` 안에 ```@AttributeOverride(name="address1이라는 이름으로", column = @Column(name="waddr1라는 이름의 column에 넣겠다"))```   
**@Embeddable을 사용하면 모델을 더 잘 표현할 수 있음 *(개별 속성을 모으는 것보다 타입으로 더 쉽게 이해하는 것)***  
  
# 07 @Embeddable 다른 테이블에 매핑하기
*엔티티가 아닌 값(개념적으로 value일 때)을 다른 테이블에 저장할 때 사용. 1-1 관계인 두 테이블 매핑 시 종종 등장*   
## 방법 1 @SecondaryTable(name="테이블명")
```java
//@SecondaryTable 사용과 @Embeddable에 테이블명을 직접 명시하는 것
@Embeddable
public class Intro {
  @Column(table= "writer_intro", name= "content_type") //writer_intro 테이블에 content_type 이름의 Column 사용
  private String contentType;

  @Column(table= "writer_intro")
  private String content;
  ...
}

@Entity
@SecondaryTable(name= "writer_intro",
  pkJoinColumns= @PrimaryKeyColumn(
    name= "writer_id", //writer_intro 테이블의 column이고
    referencedColumnName= "id" //writer 테이블의 column 지정
    //join 대상이 되는 column을 지정 해주는 것
  )
)
public class Writer {
  ...
  @Embedded
  private Intro intro;
}
```
## 방법 2 @SecondaryTable + @AttributeOverride 
*여러 곳에서 사용하게 될 Address는 Table에 이름을 박기가 어렵다*  
```java
@Embedded
@AttributeOverrides({
  @AttributeOverride(name= "address1",
                     column= @Column(table= "writer_address", name= "add1")), //column 어노테이션에서 name을 지정해준다
...
})
private Address address;
```
객체 생성 시 ```Writer w = new Writer("name", new Address("주소1", "주소2", "12345"), new Intro("text/plane", "소개글"));```  
**Embedded 된 테이블에 대해서는 left join을 사용해야 조회가 가능하다**  
