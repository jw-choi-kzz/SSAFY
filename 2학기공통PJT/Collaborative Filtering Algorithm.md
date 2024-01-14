
# 위키
협업 필터링(collaborative filtering)
## 1
기존의 어느 정도 예측이 가능한 고객들과 비슷한 패턴을 가진 고객들을 찾는다.  
기존 고객들의 행동을 예측하기 위해 첫 번째 단계에서 찾은 비슷하다고 생각된 고객들의 행동을 수치화하여 사용한다.  
## 2 
아이템 간의 상관관계를 결정하는 아이템 매트릭스(item-item matrix)를 만든다.  
매트릭스를 사용하여 최신 사용자의 데이터를 기반으로 그 사용자의 기호를 유추한다.  
https://ko.wikipedia.org/wiki/%ED%98%91%EC%97%85_%ED%95%84%ED%84%B0%EB%A7%81


# CF 추천 알고리즘
(**CBF**: content based filtering: 아이템 고유 정보 기반)  
사용자와 아이템의 선호도 기반. 비슷한 선호도를 가진 사용자 식별.  
나와 선호도가 유사한 사람의 사례를 통한 추천.  
## 1. User/item based CF(Memory-based algorithm)
유사도 기반으로 평가 점수 계산  
User-item matrix (행 item, 열 user)   
user-based: 평가가 유사한 **user**(취향이 비슷한 사람)를 찾은 후 접하지 않은 item 선호도 추론.  
### 유사도 평가
**코사인 유사도**: 공통평가 항목에 대해 벡터화 시킨 후 코사인 유사도 공식에 대입, 모든 사용자에 적용, 사용자 유사도 행렬(*사용자 수 by 사용자 수*) 구하기, 유사도와 다른 사람 평가 점수 가중합으로 평가 안한 항목의 점수 구하기.  
- 계산방법: 가장 유사도 큰 사람을 통해 계산 / 유사도가 큰 상위 n명의 유사도를 통해 계산 / 전체 사람의 유사도를 통해 계산 등  
item-based: 평가가 유사한 **item**(비슷한 아이템)을 찾은 후 item에 대한 user 선호도 구하기.  
아이템 유사도 행렬(*아이템 수 by 아이템 수*) 구하기.
### 조건
유저가 아이템을 평가한 점수(explicit feedback(rating))/ 클릭(시청) 여부, 접속 시간 등 간접적으로 선호도를 내포한 데이터(implicit feedback) 
평가 점수가 어느 정도 있는 상태(점수 부족한 경우 신뢰도 문제 발생) => n명 이상일 경우에만 유사도를 계산으로 해결..  
  
실제 추천 서비스에서는 item based CF 주로 사용  
User-item matrix는 빈 값이 많음 => sklearn 라이브러리는 연산량 초과 에러 => **CSR, COO** 방식으로 계산  
평가 성향에 대한 반영 필요: 평균을 빼는 등 정규화 과정을 통해 추천 시스템의 정확도 올릴 수 있음.  

+) 2. MF,FM  
행렬 연상 기반 + 선형대수로 평가하지 않은 선호도 예측    
*+) Deep learning CF
딥러닝 모델을 통해 선호도 예측*  

=> 사용자가 평가하지 않은 / 선호도를 알 수 없는 / 접하지 않은 아이템에 대한 평가(선호도) 예측  
선호도 기반 TOP n개 제공.  
https://oper0815.github.io/recommendation/CF/
  
# 추천 알고리즘 구현하기

https://proinlab.com/archives/2103
  
# 추천 알고리즘 CF 이해

https://bitcodic.tistory.com/105

# 
https://www.baeldung.com/java-collaborative-filtering-recommendations
