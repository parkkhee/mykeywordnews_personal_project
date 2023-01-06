![image](https://user-images.githubusercontent.com/76894305/210966861-e8419c16-4c64-4e30-9ba4-a27b23e484ba.png)

# mykeyword_personal_project

## 작품 선정 이유
- 다양한 포털 사이트, 다양한 뉴스 기사 사이트들이 존재
- 여러 포털 사이트에 일일이 접속하고, 일일이 검색하는 데 시간 소요
- 일일이 포털 사이트에 접속하지 말고, 단순한 로그인만으로 자신의 관심사 키워드를 등록하고,뉴스 기사를 제공받으면 좋을 것 같다는 아이디어

## 작품 설명
- 유저가 회원가입시 키워드 2개를 입력
- 4개의 포털사이트(NAVER, NATE, ZUM, MSN(Microsoft Network))에서 유저가 입력한 키워드에 해당하는 최신기사 4개씩을 크롤링
  (단어수 = 4(포털사이트) * 4(뉴스기사)* 2(빈도수높은단어) = 32개) (뉴스 기사 수 = 16개)
- 유저 로그인시 워드클라우드 모습으로 화면에 출력. 클릭 -> 단어를 추출해온 기사로 이동
- 기사로 이동과 동시에 뉴스 요약문 출력
- 정해진 주기마다 뉴스를 크롤링, DB 업데이트
- 실시간 인기 검색어 top10 볼 수 있다.

## 작품 핵심 기술, 개발 환경
![image](https://user-images.githubusercontent.com/76894305/210967435-3933fb39-4901-48a0-979a-1fb8bdc67517.png)

## 시스템 구성도
![image](https://user-images.githubusercontent.com/76894305/210967480-360e465e-ee76-4c02-a3a6-bbf131233a9b.png)

## 컴포넌트별 기능들
### SignIn & SignUp
![image](https://user-images.githubusercontent.com/76894305/210967588-f7cc135f-5595-43a9-91d3-bd536e46ed2c.png)
아이디, 비밀번호 입력시 로그인
(Create Account 클릭  회원가입 폼)
회원가입시 아이디, 비밀번호, 키워드1, 키워드2 입력
로그인시 JWT생성

### WordcloudFront
![image](https://user-images.githubusercontent.com/76894305/210967821-3ab6e23d-d825-4018-b362-4efd59263853.png)
로그인 시 해당 유저의 키워드를 이용해 추출한 각 기사내 빈도수 높은 단어들 워드 클라우드 형식으로 출력
관심있는 단어 클릭 -> 해당 단어를 가져온 기사로 이동

### RealTime Front
실시간 인기 검색어 top10 워드클라우드 형식으로 출력
"시그널" 이라는 네이버기반 검색량 조회 서비스 -> 실시간 인기 검색어 크롤링
주기적으로 DB 업데이트
단어클릭 -> 네이버웹 뉴스 자동 검색(자바스크립트)

### SummarySystem
![image](https://user-images.githubusercontent.com/76894305/210968409-c49358e3-af74-4d49-9289-0c55a1f2320d.png)
뉴스 본문 요약
단어 클릭시
뉴스 사이트로 이동과 동시에 뉴스 본문을 요약한 내용이 출력됨

### MyPage
![image](https://user-images.githubusercontent.com/76894305/210968600-68faa6f3-22a9-437d-bb37-d07af5a62bbe.png)
마이페이지에서 본인의 아이디, 설정된 키워드 확인 가능
키워드 수정과 비밀번호 수정이 가능

### DataBase
DB속 저장된 회원 정보(비밀 번호는 spring security로 인해 암호화)
사용자 입력 키워드
크롤링 데이터(url, 기사내 단어)



