# Distributed-Architecture-Chatroom

# 這是一個基於Netty實現的分布式架構聊天室，使用WebSocket作為通信協議。

## 實現的功能如下：
- 支持多人同時在線<br />
- 實現公告.私聊.創建聊天室(群聊)<br />
- 好友.黑名單列表,黑名單可以封鎖對方訊息<br />
- 禁言功能<br />
- 使用者後台管理功能<br />
- 與服務器保持長連接,心跳檢測<br />

## 後端技術概覽:

-  Netty : 基於Java的高性能網絡編程框架,Netty的核心是異步、事件驅動和高效的網絡通信<br />
-  Websocket : 基於HTTP協議，它支持持久連接，可以實現實時的、雙向的通信。<br />
-  Redis : 高性能的內存數據庫，由於其快速的讀寫速度和豐富的功能，Redis被廣泛用於緩存、會話存儲、實時數據分析等場景。<br />
-存登入JWT Token,作為後續API認證身份與控制登入時過<br />
-禁言功能,可以在多台服務確實同步禁言,可設定過期時間<br />
-  Nacos : 動態服務發現、配置管理和服務治理平台,它提供了服務註冊與發現、配置管理、動態DNS、流量管理等功能。<br />
-服務發現,有UI介面可以調整已啟動的服務權重<br />
-上新代碼可使用權重分配控制流量監控<br />
-服務的健康狀況監控<br />
-  Swagger: 用於生成和展示API文檔，方便開發者查看和調試API接口。<br />
http://localhost:8080/user/swagger-ui.html

## 運行項目:
* 需要先安裝 docker-compose<br />
1.clone docker<br />
  https://github.com/lzz0826/Distributed-Architecture-Chatroom/tree/main/docker <br />
2.依序啟動服務 mysql -> redis -> nacos -> user-java <br />
使用cmd在各服務 docker-compose.yml 路徑中執行指令 dokcer comopose -d <br />

### 成功啟動後可以訪問:

#### Swagger UI介面:
http://localhost:8080/user/swagger-ui.html <br />

#### Nacos UI介面:
http://127.0.0.1:8848/nacos/#/ <br />

 









