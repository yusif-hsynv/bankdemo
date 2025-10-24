# 🏦 Bank Demo System (Spring Boot)

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen?logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue?logo=mysql)
![Maven](https://img.shields.io/badge/Maven-Build-blueviolet?logo=apachemaven)
![Lombok](https://img.shields.io/badge/Lombok-Automation-red?logo=lombok)

A simple **Bank Management and Transaction Application** built with **Spring Boot** and **MySQL**.  
It provides functionality for managing customers, accounts, transactions, users, and file uploads,  
with layered architecture, token-based authentication, and unified response handling.

---

## 🚀 Features
- CRUD operations for **Customer** and **Account**
- Internal and external **transaction** management with real-time balance updates
- Token-based user authentication (`User` & `UserToken`)
- Scheduled task for **automatic token expiration** (`MyScheduler`)
- File upload & download with `AttachmentController`
- Currency conversion for international transfers (via **ExchangeRate API**)
- Exception handling with `BankException` and `ExceptionConstants`
- Enum-based entity status control (`EnumAvailableStatus`)
- Clean layered architecture (Controller → Service → Repository)
- Unified response model using `Response<T>` and `RespStatus`

---

## 🧠 Transaction Logic Overview
When creating a transaction:
1. The system validates the user token.  
2. It verifies both **debit** and **credit** accounts exist and are active.  
3. For internal transfers — it checks balances and updates both accounts.  
4. For external transfers — it converts currency using **ExchangeRate API**.  
5. Each successful transaction is recorded in the `transaction` table.

---

## ⚙️ Tech Stack
- Java 17  
- Spring Boot (Web, Data JPA, Scheduler)  
- MySQL  
- Maven  
- Lombok  
- REST Template (for currency conversion API)

---

## 🧰 How to Run
```bash
git clone https://github.com/yusif-hsynv/bankdemo-spring.git
cd bankdemo-spring
mvn spring-boot:run
```
---
## 👨‍💻 Author  
**Yusif Hüseynov**  
*Java Developer | Spring Boot | REST APIs*  
[GitHub Profile](https://github.com/yusif-hsynv)
