# ClockIn 系統腳本使用指南

本文檔說明如何使用 ClockIn 系統提供的自動化腳本來啟動和停止系統容器。

## 可用腳本

系統提供了以下兩個腳本：

1. **start-clockin.sh** - 啟動所有 ClockIn 系統容器
2. **stop-clockin.sh** - 停止所有 ClockIn 系統容器

## 使用方法

### 啟動系統

在終端中執行以下命令啟動 ClockIn 系統的所有容器：

```bash
./start-clockin.sh
```

此腳本將：
- 檢查 Docker 和 Docker Compose 是否已安裝
- 檢查 Docker 服務是否運行
- 執行 `docker-compose up -d` 命令在後台啟動所有容器
- 顯示各服務的訪問地址
- 顯示當前運行的容器列表

### 停止系統

在終端中執行以下命令停止 ClockIn 系統的所有容器：

```bash
./stop-clockin.sh
```

此腳本將：
- 執行 `docker-compose down` 命令停止並移除所有容器
- 顯示停止結果
- 顯示當前運行的容器列表

## 系統服務

啟動後，可通過以下地址訪問各服務：

- **API 服務**: http://localhost:8080/clockin
- **認證服務**: http://localhost:8081
- **打卡記錄服務**: http://localhost:8082
- **管理後台服務**: http://localhost:8083
- **MySQL**: localhost:3306
- **Redis**: localhost:6379
- **Swagger API 文檔**: http://localhost:8080/clockin/swagger-ui.html

## 注意事項

1. 腳本需要有執行權限，如果無法執行，請先運行：
   ```bash
   chmod +x start-clockin.sh stop-clockin.sh
   ```

2. 腳本需要在項目根目錄（包含 docker-compose.yml 文件的目錄）中執行

3. 首次啟動系統可能需要較長時間，因為需要下載 Docker 映像並構建服務

4. 如果遇到問題，請查看 Docker 日誌：
   ```bash
   docker-compose logs -f
   ```

## 系統數據

系統數據存儲在 Docker 數據卷中，即使停止容器，數據也會保留。如果需要完全清除數據，請使用：

```bash
docker-compose down -v
```

請謹慎使用此命令，它將刪除所有系統數據！
