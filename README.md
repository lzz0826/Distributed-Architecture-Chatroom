# Distributed-Architecture-Chatroom

# 這是一個基於Netty實現的分布式架構聊天室，使用WebSocket作為通信協議。

![image](https://github.com/lzz0826/Distributed-Architecture-Chatroom/blob/main/images/003.png)


## 實現的功能如下：
- 支持多人同時在線<br />
- 實現公告.私聊.創建聊天室(群聊)<br />
- 好友.黑名單列表,黑名單可以封鎖對方訊息<br />
- 禁言功能<br />
- 使用者後台管理功能<br />
- 與服務器保持長連接,心跳檢測<br />
- 上傳大頭照<br />
- 使用貼圖聊天<br />

## 後端技術概覽:

-  Netty : 基於Java的高性能網絡編程框架,Netty的核心是異步、事件驅動和高效的網絡通信<br />
-  Websocket : 基於HTTP協議，它支持持久連接，可以實現實時的、雙向的通信。<br />
-  Redis : 高性能的內存數據庫，由於其快速的讀寫速度和豐富的功能，Redis被廣泛用於緩存、會話存儲、實時數據分析等場景。<br />
-存登入JWT Token,作為後續API認證身份與控制登入時過<br />
-禁言功能,可以在多台服務確實同步禁言,可設定過期時間<br />
-  RabbitMQ : 充當了應用程序之間的中間件，負責接收、存儲和轉發消息，使得不同的應用程序可以通過它進行異步通信。<br />
-  Nacos : 動態服務發現、配置管理和服務治理平台,它提供了服務註冊與發現、配置管理、動態DNS、流量管理等功能。<br />
-服務發現,有UI介面可以調整已啟動的服務權重<br />
-上新代碼可使用權重分配控制流量監控<br />
-服務的健康狀況監控<br />
-  Swagger: 用於生成和展示API文檔，方便開發者查看和調試API接口。<br />
http://localhost:8080/user/swagger-ui.html

## 運行項目:
* 需要先安裝 docker-compose<br />

### 運行前需要加上虛擬內部網域
docker network create --subnet=192.168.200.0/24 redis-cluster-net<br />

1.clone docker<br />
  https://github.com/lzz0826/Distributed-Architecture-Chatroom/tree/main/docker <br />
2.依序啟動服務 mysql -> redis -> nacos -> user-java <br />
使用cmd在各服務 docker-compose.yml 路徑中執行指令 dokcer comopose up -d <br />

### 成功啟動後可以訪問:

#### Swagger UI介面: 檔案上傳相關請使用 PostMam
http://localhost:8080/user/swagger-ui.html <br />
![image](https://github.com/lzz0826/Distributed-Architecture-Chatroom/blob/main/images/002.png)

#### Nacos UI介面:
http://127.0.0.1:8848/nacos/#/ <br />
![image](https://github.com/lzz0826/Distributed-Architecture-Chatroom/blob/main/images/001.png)

可以從上方的Nacos UI介面看到已啟動的 user-service

# API:
# 檔案上傳相關請使用 PostMam
## PostMan匯入檔: <br />
https://github.com/lzz0826/Distributed-Architecture-Chatroom/blob/main/postman/chat-room-API.json <br />

## ws: <br />
### 公告: <br />
https://docs.google.com/document/d/1Ce4mefuPYsalm8sMI--DB0Jb11LFiNYsoy8lY3WKw9M/edit <br />
### 私聊: <br />
https://docs.google.com/document/d/10lmVULQ4bxjI7kkBuwZuEzUyCnacE5Kp8OmvFhTfaMw/edit <br />
### 聊天室: <br />
https://docs.google.com/document/d/1pMlKB_5m_ScCNB3_e2EXjmlnAPqYRlig3KzMBZrxrjs/edit <br />



 









