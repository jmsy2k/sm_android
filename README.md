# sm_android


## 약어

A : 앱

S : 소켓서버

H : 매장 호스트

C : 매장 클라이언트

G : 게임 클라이언트 embed dll

I : 설치 usb 프로그램

W : 웹서버


## 서버 
### node web server : db 관리 

### socketio : ping 관리

### db : mysql -> orm 사용 Sequelize

  table -> 
    
    manager : 관리자 
    - id(primary_key, autoincreament) : Int
    - user_id(Uniqueue) : String
    - user_name : String
    - password : String
    - manager_type_id(foregin) : Int
    - created_at
    - modified_at

    manager_type : 관리자 권한
    - id(primary_key, autoinrement) : Int
    - name_ko : string (관리자, 총괄, 기사, 어드민)
    - name : manager, supermanager, engineer, admin
    
    store_permission : 매장 권한(매핑 테이블)
    - id( primary_key, autoincrement) : Int
    - manager_id(foregin, index) : Int
    - store_id(foregin) : Int
    
    game_permission : 게임 권한(매핑 테이블)
    - id( primary_key, autoincrement) : Int
    - manager_id(foregin, index) : Int
    - game_id(foregin) : Int
    
    store : 매장 정보
    - id(unique) : Int
    - name : string
    - manager_id(foregin) : Int ( 등록한 기사 정보 )
    - host_local_ip : string
    - created_at
    - modified_at
            
    device : 기기 정보
    - id(primary_key, autoincreament)
    - store_id(foregin)
    - mac_addr(constraint) : 맥주소
    - name(constraint) : 기기 번호
    - game_id(foregin)
    - created_at
    - modified_at

    inout_tody : 입출금 표시(일 정산 이후에 삭제?)
    - store_id(constraint uniqueu)
    - device_id(constraint uniqueu)
    - is_input: bool
    - price : Int
    - created_at : DATETIME
   
    game : 게임 정보 테이블
    - id : (primary_key, autoincrement)
    - name : 게임명(findwindow로 찾을수 있는?)
    
    game_event : 게임 이벤트 테이블 // 게임마다 정해지는거지 설정에 따라 바뀔수 없음
    - id : (primary_key, autoincrement)
    - game_id(foregin)
    - event_name : string
    - vk : Int // 가상키코드
    
    call_log : 콜 로그
    - id(primary_key, autoincrement)
    - store_id(foregin)
    - device_id(foregin)
    - created_at: DATETIME
    - manager_id(foregin)
    - event_id(foregin)
    
    setting_log : 세팅 로그
    - id(primary_key, autoinrement)
    - manager_id(foregin)
    - store_id(foregin)
    - device_id(foregin)
    - type: host, device
    - created_at: DATETIME
    
## client(dll)

### 기능 

- 입출금 전송(매장, 기기번호, 입출금 여부, 금액)
- 핑(켜져 있는지 확인을 위함) 1분에 한번씩 전송: 매장, 기기번호


## 필요 프로그램 목록

1. client(dll)
    - 입출금 금액 전송(웹)
    - 확률 전달(레지스트리)
    - 게임 금액 전달(레지스트리)
2. call client(exe)  
    - 매장 호스트 조회(웹)
    - 매장 호스트 접속(소켓)
    - 콜 입력(소켓)
    - 접속 끊기면 게속 재시도
3. store host(exe or service) //
    - 로컬 소켓 서버(기기 관리)
    - 서버 접속(소켓)
    - 기기 목록 관리(소켓)
    - 콜 전달(소켓)
    - 접속 끊키면 계속 재시도
4. socket server(exe)
    - 매장 접속되어 있는 매장 목록 관리
    - 콜 전달
    - 매장 기기 목록 전달
5. web server(node js)
    - 사용자 인증
    - 호스트 인스톨
    - 게임 목록 관리
    - 기기 설치 관리
    - 호스 로컬 ip 관리
    - 매장 목록 관리(매장 등록을 어디서 할건지?)
    - 기기 목록 관리
    - 게임 목록 관리
    - 게임 이벤트 관리
    - 콜 히스토리 관리
    - 사용자 추가
    - 사용자 타입 관리
6. db(mysql)
7. app(android)
    - 인증 
    - 매장 목록 
    - 매장 정보 조회(이름, 정산(날짜별?), 기기 목록)
    - 기기 정보(번호, 맥주소, 입출금 기록, 입출금 토탈, 이번트 콜)
    - 매장 생성 
    - 게임 생성
    - 이벤트 추가/수정/삭제
    - 사용자 생성 <- 본인 가입이 아니고 하위 관리자만 생성 가능(기사는 생성 불가)
    - 기사에게 (매장/게임)권한 부여 창
8. installer(exe) // 별개 프로그램 제작
    - 인증(id/비번 저장)
    - call host 설치
    - call host 설치 내용 전송(localip,매장 id)
    - call client 설치
    - call client 설치 내용 전송(매장 id, 기기 번호, 게임 id, 맥 주소)
    - 확률/금액(레지스트리 등록)
    - 매장 목록 조회
    - 게임 목록 조회
 
## 패킷

https://docs.google.com/spreadsheets/d/1zxJGZiLXYNsahyMkz1Ka81bGi7yCZpOOnwxietSNJ0k/edit?usp=sharing

## 필요 서버
1. ec2 windows server(socket server)
2. ec2 linux  server(web server)
3. rds mysql 







