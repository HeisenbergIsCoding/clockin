# ClockIn 系統 Docker 部署指南

## 系統架構

ClockIn 系統由以下幾個主要服務組成：

1. **MySQL 資料庫** - 儲存所有系統數據
2. **Redis 緩存** - 提供緩存和session存儲
3. **ClockIn-Auth** - 認證服務 (port: 8081)
4. **ClockIn-Record** - 打卡記錄服務 (port: 8082)
5. **ClockIn-Admin** - 後台管理服務 (port: 8083)

## 前置需求

- Docker 20.10+ 
- Docker Compose v2.x+
- 約 2GB 可用硬碟空間

## 如何啟動系統

### 1. 初次設置

在項目根目錄執行以下命令構建並啟動所有服務：

```bash
docker-compose up -d
```

首次啟動將自動執行：
- 下載所需的 Docker 映像
- 構建各個服務模塊
- 創建必要的容器
- 設置網絡和數據卷
- 啟動所有服務

### 2. 查看服務運行狀態

```bash
docker-compose ps
```

### 3. 查看服務日誌

查看所有服務的日誌：
```bash
docker-compose logs -f
```

或查看特定服務的日誌：
```bash
docker-compose logs -f clockin-auth
docker-compose logs -f clockin-record
docker-compose logs -f clockin-admin
```

### 4. 停止系統

```bash
docker-compose down
```

如需同時刪除資料（慎用！），可執行：
```bash
docker-compose down -v
```

## 服務存取資訊

服務啟動後，可通過以下地址訪問：

- **ClockIn-Auth 認證服務**: http://localhost:8081/
  - Swagger UI: http://localhost:8081/swagger-ui.html

- **ClockIn-Record 打卡記錄服務**: http://localhost:8082/
  - Swagger UI: http://localhost:8082/swagger-ui.html

- **ClockIn-Admin 後台管理服務**: http://localhost:8083/admin
  - Swagger UI: http://localhost:8083/admin/swagger-ui.html

## 數據持久化

系統數據存儲在以下 Docker 數據卷中：

- `mysql_data`: MySQL 資料庫數據
- `redis_data`: Redis 緩存數據

即使容器被刪除，這些數據卷會保留數據。

## 環境配置

可在 `docker-compose.yml` 文件中調整各服務的環境變數，例如：

- 資料庫連接信息
- Redis 連接信息
- 服務間通信 URL
- 系統設定值

## 常見問題排解

1. **服務啟動失敗**
   - 檢查日誌: `docker-compose logs <服務名稱>`
   - 確保相關端口未被占用

2. **資料庫連接問題**
   - 確保 MySQL 服務健康運行
   - 檢查連接憑證是否正確

3. **服務間通信問題**
   - 確保服務間的 baseUrl 設置正確
   - 檢查網絡連接

## 重建服務

如需重建特定服務：

```bash
docker-compose build clockin-auth
docker-compose up -d clockin-auth
```

## 系統擴展

若需擴展系統（例如增加更多實例），可以修改 docker-compose.yml 增加服務複製數量並配置負載均衡器。
