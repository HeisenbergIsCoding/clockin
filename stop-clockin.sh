#!/bin/bash

# 打卡系統停止腳本
# 作者：ClockIn 系統團隊
# 日期：2025-03-03

# 顯示腳本標題
echo "========================================="
echo "      ClockIn 系統容器停止腳本           "
echo "========================================="

# 顯示當前目錄
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
echo "腳本執行目錄: $SCRIPT_DIR"
cd "$SCRIPT_DIR"

# 檢查 docker-compose 是否已安裝
if ! command -v docker-compose &> /dev/null; then
    echo "錯誤: Docker Compose 未安裝，請先安裝 Docker Compose。"
    exit 1
fi

echo "開始停止 ClockIn 系統容器..."

# 執行 docker-compose down 命令
docker-compose down

# 檢查停止結果
if [ $? -eq 0 ]; then
    echo "ClockIn 系統容器已成功停止！"
else
    echo "錯誤: ClockIn 系統容器停止失敗，請檢查錯誤日誌。"
fi

# 顯示容器狀態
echo -e "\n當前運行的容器:"
docker-compose ps
