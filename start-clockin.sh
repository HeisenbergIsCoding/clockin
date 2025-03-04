#!/bin/bash

# 打卡系統啟動腳本
# 作者：ClockIn 系統團隊
# 日期：2025-03-03

# 顯示腳本標題
echo "========================================="
echo "      ClockIn 系統容器啟動腳本           "
echo "========================================="

# 顯示當前目錄
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
echo "腳本執行目錄: $SCRIPT_DIR"
cd "$SCRIPT_DIR"

# 檢查 docker 是否已安裝
if ! command -v docker &> /dev/null; then
    echo "錯誤: Docker 未安裝，請先安裝 Docker。"
    exit 1
fi

# 檢查 docker-compose 是否已安裝
if ! command -v docker-compose &> /dev/null; then
    echo "錯誤: Docker Compose 未安裝，請先安裝 Docker Compose。"
    exit 1
fi

# 檢查 docker 服務是否運行
if ! docker info &> /dev/null; then
    echo "錯誤: Docker 服務未運行，請先啟動 Docker 服務。"
    exit 1
fi

echo "開始啟動 ClockIn 系統容器..."

# 執行 docker-compose up -d 命令
docker-compose up -d

# 檢查啟動結果
if [ $? -eq 0 ]; then
    echo "ClockIn 系統容器已成功在後台啟動！"
    echo "服務訪問地址:"
    echo "- API 服務: http://localhost:8080/clockin"
    echo "- 認證服務: http://localhost:8081"
    echo "- 打卡記錄服務: http://localhost:8082"
    echo "- 管理後台服務: http://localhost:8083"
    echo "- MySQL: localhost:3306"
    echo "- Redis: localhost:6379"
    echo "- Swagger API 文檔: http://localhost:8080/clockin/swagger-ui.html"
else
    echo "錯誤: ClockIn 系統容器啟動失敗，請檢查錯誤日誌。"
fi

# 顯示容器狀態
echo -e "\n當前運行的容器:"
docker-compose ps
