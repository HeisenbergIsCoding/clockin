# ClockIn 系統

## 專案概述

ClockIn 是一個現代化的企業員工打卡管理系統，提供完善的上下班打卡、請假管理、考勤統計以及異常檢測功能。系統採用模組化設計，基於 Spring Boot 3.x 開發，提供高可靠性、可擴展性的解決方案。

## 技術棧

- **JDK**: 17
- **框架**: Spring Boot 3.2.3, Spring MVC, Spring Data JPA
- **資料庫**: MySQL, Redis
- **API 文件**: SpringDoc OpenAPI 2.3.0
- **資料遷移**: Flyway 9.22.3
- **安全認證**: JWT (JSON Web Token) 0.12.3
- **工具類庫**: Apache Commons
- **辦公處理**: Apache POI 5.2.5
- **外部整合**: Google API Client 2.2.0

## 系統架構

ClockIn 系統採用模組化設計，主要包含以下模組：

### 核心模組

1. **clockin-api**: 系統主要入口點，整合所有模組功能並提供統一的 API 接口
2. **clockin-common**: 公共組件，提供系統共用的實體類、工具類和常量定義
3. **clockin-auth**: 用戶認證與授權模組，處理用戶登錄、權限驗證等功能
4. **clockin-record**: 打卡記錄管理模組，處理員工打卡、異常處理等核心業務邏輯
5. **clockin-admin**: 後台管理模組，提供系統管理員相關功能

### 系統功能

- 員工打卡（上/下班、外出/返回）
- 考勤記錄管理和匯出
- 打卡異常檢測和處理
- 考勤統計和報表生成
- 管理員後台系統
- 基於 JWT 的安全認證
- 支援 Google 賬號登錄

## 項目結構

```
clockin
├── clockin-admin               // 管理員模組
│   ├── src/main/java           // Java 源碼
│   └── src/main/resources      // 配置文件
├── clockin-api                 // 系統主入口模組
│   ├── src/main/java           // Java 源碼
│   └── src/main/resources      // 配置文件
├── clockin-auth                // 認證模組
│   ├── src/main/java           // Java 源碼
│   └── src/main/resources      // 配置文件
├── clockin-common              // 共用組件模組
│   ├── src/main/java           // Java 源碼
│   └── src/main/resources      // 配置文件
├── clockin-record              // 打卡記錄模組
│   ├── src/main/java           // Java 源碼
│   └── src/main/resources      // 配置文件
└── pom.xml                     // Maven 主配置文件
```

## 系統特性

1. **標準化架構**: 採用標準的 Spring 應用架構，包括 Entity、Repository、Service 和 Controller 層
2. **模組化設計**: 功能分為多個獨立模組，便於開發和維護
3. **高效資料存取**: 使用 Spring Data JPA 進行 ORM 映射，提供高效率的資料庫存取
4. **全面安全防護**: 實現 JWT 令牌認證，保障系統安全性
5. **API 文件自動生成**: 整合 SpringDoc OpenAPI，自動生成 API 文件
6. **資料庫遷移管理**: 使用 Flyway 進行資料庫版本控制
7. **定時任務支援**: 內建排程任務系統，支援各類定時作業
8. **快取機制**: 整合 Redis 提供高效能的快取支援

## 快速開始

### 系統需求

- JDK 17 或以上
- Maven 3.6 或以上
- MySQL 8.0 或以上
- Redis 6.0 或以上

### 配置數據庫

1. 建立 MySQL 資料庫:
```sql
CREATE DATABASE clockin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改 `clockin-api/src/main/resources/application.yml` 中的資料庫配置:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/clockin?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Taipei
    username: your_username
    password: your_password
```

### 編譯與運行

1. 編譯專案:
```bash
mvn clean package
```

2. 運行應用:
```bash
java -jar clockin-api/target/clockin-api.jar
```

3. 訪問 Swagger 文件:
```
http://localhost:8080/clockin/swagger-ui.html
```

## 開發指南

### 模組說明

- **clockin-common**: 包含共用的實體類、異常處理、響應模型和工具類
- **clockin-auth**: 處理用戶認證、授權及相關權限管理
- **clockin-record**: 管理打卡記錄、考勤統計等核心業務邏輯
- **clockin-admin**: 提供管理員後台功能，包括系統設置和用戶管理
- **clockin-api**: 系統主入口，整合各模組功能並提供統一的 API

### 新增功能流程

1. 在相應模組中建立實體類 (Entity)
2. 建立資料庫存取層 (Repository)
3. 實現業務邏輯層 (Service)
4. 建立控制器 (Controller) 提供 API 接口
5. 在 SpringDoc 中添加 API 文件註解

## 系統維護

### 日誌管理

系統日誌預設保存在 `logs/` 目錄下，可通過 `application.yml` 進行配置。

### 資料庫遷移

系統使用 Flyway 進行資料庫版本控制，遷移腳本位於 `clockin-api/src/main/resources/db/migration` 目錄。

### 性能監控

可透過 Spring Boot Actuator 進行系統性能監控。

## 授權協議

 2025 ClockIn System. All Rights Reserved.

## API 文檔範例

以下提供系統主要 API 端點的使用範例，完整 API 文檔可通過訪問 Swagger UI (`http://localhost:8080/clockin/swagger-ui.html`) 獲得。

### 認證相關 API

#### 使用者登入

```http
POST /clockin/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password"
}
```

回應:

```json
{
  "code": 200,
  "message": "登入成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "username": "admin",
    "fullName": "系統管理員",
    "roles": ["ROLE_ADMIN"]
  }
}
```

#### Google 帳號登入

```http
GET /clockin/auth/google/login
```

回應: 重定向至 Google 登入頁面

### 打卡記錄 API

#### 員工上班打卡

```http
POST /clockin/record/clock-in
Content-Type: application/json
Authorization: Bearer {token}

{
  "location": "公司",
  "deviceInfo": "Web Browser",
  "remark": "正常上班"
}
```

回應:

```json
{
  "code": 200,
  "message": "打卡成功",
  "data": {
    "recordId": 1001,
    "userId": 10,
    "username": "employee1",
    "clockInTime": "2025-03-01T09:00:15",
    "location": "公司",
    "type": "CLOCK_IN",
    "status": "NORMAL"
  }
}
```

#### 員工下班打卡

```http
POST /clockin/record/clock-out
Content-Type: application/json
Authorization: Bearer {token}

{
  "location": "公司",
  "deviceInfo": "Web Browser",
  "remark": "正常下班"
}
```

回應:

```json
{
  "code": 200,
  "message": "打卡成功",
  "data": {
    "recordId": 1002,
    "userId": 10,
    "username": "employee1",
    "clockOutTime": "2025-03-01T18:05:20",
    "location": "公司",
    "type": "CLOCK_OUT",
    "status": "NORMAL",
    "workHours": 9.08
  }
}
```

#### 查詢個人打卡記錄

```http
GET /clockin/record/personal?startDate=2025-03-01&endDate=2025-03-31
Authorization: Bearer {token}
```

回應:

```json
{
  "code": 200,
  "message": "查詢成功",
  "data": {
    "total": 20,
    "records": [
      {
        "recordId": 1001,
        "date": "2025-03-01",
        "clockInTime": "2025-03-01T09:00:15",
        "clockOutTime": "2025-03-01T18:05:20",
        "workHours": 9.08,
        "status": "NORMAL"
      },
      {
        "recordId": 1003,
        "date": "2025-03-02",
        "clockInTime": "2025-03-02T08:55:05",
        "clockOutTime": "2025-03-02T18:10:30",
        "workHours": 9.25,
        "status": "NORMAL"
      }
      // 更多記錄...
    ]
  }
}
```

### 管理員 API

#### 查詢部門所有員工打卡記錄

```http
GET /clockin/admin/department/{departmentId}/records?startDate=2025-03-01&endDate=2025-03-31&page=0&size=10
Authorization: Bearer {token}
```

回應:

```json
{
  "code": 200,
  "message": "查詢成功",
  "data": {
    "content": [
      {
        "recordId": 1001,
        "userId": 10,
        "username": "employee1",
        "fullName": "王小明",
        "date": "2025-03-01",
        "clockInTime": "2025-03-01T09:00:15",
        "clockOutTime": "2025-03-01T18:05:20",
        "workHours": 9.08,
        "status": "NORMAL"
      }
      // 更多記錄...
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "totalPages": 2,
      "totalElements": 20
    }
  }
}
```

#### 匯出考勤報表

```http
GET /clockin/admin/export?departmentId=1&startDate=2025-03-01&endDate=2025-03-31
Authorization: Bearer {token}
Accept: application/vnd.ms-excel
```

回應: Excel 檔案下載

系統將返回一個二進制流，瀏覽器會自動將其作為 Excel 檔案下載。檔案默認名稱格式為 `考勤報表_部門名稱_YYYY-MM-DD至YYYY-MM-DD.xlsx`

Excel 檔案內容包含：
1. **基本資訊頁**：
   - 報表生成時間
   - 部門名稱
   - 統計時間範圍
   - 總人數和考勤摘要

2. **考勤詳情頁**：
   - 員工編號
   - 員工姓名
   - 部門
   - 日期
   - 上班時間
   - 下班時間
   - 工作時數
   - 出勤狀態 (正常、遲到、早退、缺勤等)
   - 備註

3. **異常統計頁**：
   - 按員工統計的異常記錄匯總
   - 遲到次數和時長
   - 早退次數和時長
   - 缺勤天數
   - 加班時數

### API 錯誤回應處理

所有 API 在遇到錯誤時，將返回統一格式的錯誤訊息：

```json
{
  "code": 400,  // HTTP 狀態碼
  "message": "錯誤訊息",
  "errors": [   // 驗證錯誤時會出現此欄位
    {
      "field": "欄位名稱",
      "message": "驗證錯誤描述"
    }
  ]
}
```

常見錯誤碼：
- 400 Bad Request: 請求參數錯誤
- 401 Unauthorized: 未登入或 Token 已過期
- 403 Forbidden: 無權限訪問
- 404 Not Found: 資源不存在
- 500 Internal Server Error: 服務器內部錯誤
