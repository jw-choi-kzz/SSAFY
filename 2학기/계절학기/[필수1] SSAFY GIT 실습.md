요구사항 분석 - 설계 - 개발 - 빌드 - 배포 ...   
GitLab
Jira(issue tracking management) : 프로젝트 관리, 진행 상황 확인, 협업, 이력 관리  
Jenkins(continuous integration) : 소스코드 완결성 유지에 사용. 빌드 자동화로 문제 조기 파악    
  
요구사항 정의서에 작성 - jira에 이슈 등록(역할 분담) - GitLab Personal access token 발급(만료일 지정, 분실해도 해지&재발급 가능)  
project access token 발급  
git bash here
```
//깃으로 관리한 적 없다면
git init
git config user.name 사용자명
git config user.email 이메일
git add .
git commit -m "initial commit"
```
  
소스코드 업로드
```
git remote add origin 프로젝트URL
git branch
git push -u origin master//요즘은 main 쓰는 추세
```
   
브랜치 생성 ```git checkout -b develop```  
  
변경사항 반영
```
git add README.md
git commit -m "add readme"

git push -u origin master
```
  
빌드 관리 : GitLab token 입력 - Jenkins 생성 - 빌드현황 페이지 활성화  
웹 기반 프로젝트 개발물에 대해서는 실행 기능 활성화  
  
정리 : develop -> master merge 하고, Jira에 생성한 이슈 완료 상태로 변경  

Git 공식 웹사이트: https://git-scm.com/doc  
Jira 공식 가이드: https://www.atlassian.com/software/jira/guides/gettingstarted/introduction#what-is-jira-software  
Jira를 이용한 애자일 scrum 프로젝트 관리: https://bcho.tistory.com/826  

