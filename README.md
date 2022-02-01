# sm_android
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
    
    user : 관리자 
    - id(primary_key, autoincreament) : Int
    - user_id(Uniqueue) : String
    - password : String
    - type : manager, supermanager

    store : 매장 정보
    - id(unique) : Int
    - managerId(manager.userId) : Int
    - name
            
    device : 기기 정보
    - id(primary_key, autoincreament)
    - store_id(foregin)
    - mac_addr

    inout_tody : 입출금 표시(일 정산 이후에 삭제?)
    - store_id(constraint uniqueu)
    - device_id(constraint uniqueu)
    - is_input: bool
    - price : Int
    - date : DATETIME
    
    
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






