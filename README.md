# sm_android

# 요구사항 정리 필요

매장 관리용

게임 클라이언트: http request post 로 query 로 전달

## 약어

C : client(dll)
SS : SocketServer
WS : WebServer
MG : ManagerClient(mobile app)



## 서버 
### node web server : db 관리 

### socketio : ping 관리

### db : mysql -> orm 사용 Sequelize

  table -> 
    
    manager : 관리자 
    - id(primary_key, autoincreament) : Int
    - user_id(Uniqueue) : String
    - password : String
    - type : manager, supermanager
    - created_at
    - modified_at

    store : 매장 정보
    - id(unique) : Int
    - managerId(manager.userId) : Int
    - name
    - created_at
    - modified_at
            
    device : 기기 정보
    - id(primary_key, autoincreament)
    - store_id(foregin)
    - mac_addr(constraint) : 맥주소
    - name(constraint) : 기기 번호
    - created_at
    - modified_at

    inout_tody : 입출금 표시(일 정산 이후에 삭제?)
    - store_id(constraint uniqueu)
    - device_id(constraint uniqueu)
    - is_input: bool
    - price : Int
    - created_at : DATETIME
   
    engineer : 설치 기사
    - id : (primary_key, autoincrement)
    - name : 
    
    game : 게임 정보 테이블
    - id : (primary_key, autoincrement)
    - name : 게임명(findwindow로 찾을수 있는?)
    - 
    
    game_event : 게임 이벤트 테이블 // 게임마다 정해지는거지 설정에 따라 바뀔수 없음
    - id : (primary_key, autoincrement)
    - game_id(foregin)
    - event_name : string
    - vk : Int // 가상키코드
    
## client(dll)

### 기능 

- 입출금 전송(매장, 기기번호, 입출금 여부, 금액)
- 핑(켜져 있는지 확인을 위함) 1분에 한번씩 전송: 매장, 기기번호

## manager client(mobile app)  

### 화면 

#### 로그인 (id, password)

#### 매장 목록
  - 매상 표시
#### 기기 목록
  - 기기 목록 표시
  - 기기 온오프 여부 표시
  - 입출금 표시

#### 입출금 기록






