# Distributed-Architecture-Chatroom

# 基于Netty實現的WebSocket聊天室,分布式架構

## 實現的功能如下：
1.支持多人同時在線<br />
2.實現公告.私聊.創建聊天室(群聊)<br />
3.好友.黑名單列表,黑名單可以封鎖對方訊息<br />
4.禁言功能<br />
5.使用者後台管理功能<br />
6.與服務器保持長連接,心跳檢測<br />

## 後端技術概覽:

### Netty : 基於Java的高性能網絡編程框架,Netty的核心是異步、事件驅動和高效的網絡通信

### Websocket : 基於HTTP協議，它支持持久連接，可以實現實時的、雙向的通信。

### Redis : 高性能的內存數據庫，由於其快速的讀寫速度和豐富的功能，Redis被廣泛用於緩存、會話存儲、實時數據分析等場景。
實現功能:<br />
存登入JWT Token,作為後續API認證身份與控制登入時過<br />
禁言功能,可以在多台服務確實同步禁言,可設定過期時間<br />

### Nacos : 動態服務發現、配置管理和服務治理平台,它提供了服務註冊與發現、配置管理、動態DNS、流量管理等功能。
實現功能:<br />
服務發現,有UI介面可以調整已啟動的服務權重<br />
上新代碼可使用權重分配控制流量監控<br />
服務的狀況監控<br />

### Swagger: 串接API資訊
http://localhost:8080/user/swagger-ui.html




