# 🎮 SteamLiteSimulator

A lightweight multiplayer Steam-inspired game distribution platform built using Java, Socket Programming, Multithreading, JDBC, MySQL, and File I/O.

---

# 📌 Project Overview

SteamLiteSimulator is a client-server desktop application developed for **CPIT-305 (Advanced Java Programming)**.

The system simulates a simplified digital game platform where users can:

* Register and login
* Browse available games
* Download games from the server
* Rate games
* View download history
* Use an Admin Panel to manage the platform

The project demonstrates multiple core Java concepts combined into one complete application.

---

# 🚀 Features

## 👤 User Features

* User Registration
* User Login Authentication
* View Available Games
* Download Games
* Download Progress Bar
* Rate Games (1–5 stars)
* View Personal Download History

---

## 🛠️ Admin Features

* Add New Games
* Delete Games
* View Registered Users
* Manage Game Library

---

## ⚡ Technical Features

* Multithreaded Server
* Socket-Based Client/Server Communication
* JDBC + MySQL Database Integration
* File Transfer System
* Server Logging System
* Exception Handling
* Progress Tracking During Downloads
* GitHub Version Control Collaboration

---

# 🧠 Technologies Used

| Technology         | Purpose                     |
| ------------------ | --------------------------- |
| Java               | Main Programming Language   |
| Java Sockets       | Client-Server Communication |
| Multithreading     | Handle Multiple Clients     |
| JDBC               | Database Connectivity       |
| MySQL              | Database Management         |
| File I/O Streams   | File Transfer & Logging     |
| NetBeans / VS Code | Development Environment     |
| Git & GitHub       | Team Collaboration          |

---

# 🏗️ System Architecture

```text
Client
   ↓
Socket Connection
   ↓
Multithreaded Server
   ↓
DatabaseManager (JDBC)
   ↓
MySQL Database
```

---

# 📂 Project Structure

```text
SteamLiteSimulator/
│
├── client/
│   ├── Client.java
│   └── ClientMenu.java
│
├── server/
│   ├── Server.java
│   ├── ClientHandler.java
│   └── FileTransferManager.java
│
├── database/
│   └── DatabaseManager.java
│
├── utils/
│   └── ServerLogger.java
│
├── resources/
│   └── games/
│
├── database_setup.sql
└── README.md
```

---

# 🗄️ Database Tables

The project uses MySQL with the following tables:

| Table     | Purpose                 |
| --------- | ----------------------- |
| accounts  | Stores users/admins     |
| games     | Stores game information |
| ratings   | Stores user ratings     |
| downloads | Stores download history |

---

# 🔌 Setup Instructions

## 1️⃣ Clone Repository

```bash
git clone https://github.com/Gon-Hozion/cpit305-steam-sim.git
```

---

## 2️⃣ Install MySQL

Install:

* MySQL Server
* MySQL Workbench

---

## 3️⃣ Create Database

Open MySQL Workbench and run:

```sql
SOURCE database_setup.sql;
```

Or manually execute the contents of:

```text
database_setup.sql
```

---

## 4️⃣ Configure Database Password

Inside:

```text
database/DatabaseManager.java
```

Update:

```java
private static final String PASSWORD = "root123";
```

To match your local MySQL password.

---

## 5️⃣ Add MySQL Connector/J

Download:

```text
mysql-connector-j
```

Add the `.jar` file to project libraries.

---

## 6️⃣ Run Server

Run:

```text
Server.java
```

Expected:

```text
Server started on port 5000...
```

---

## 7️⃣ Run Client

Run:

```text
Client.java
```

---

# 🧪 Example Functionalities

## Register

```text
1. Register
Username: bandr
Password: bandr123
```

---

## Login

```text
2. Login
Username: bandr
Password: bandr123
```

---

## View Games

```text
=== GAME LIST ===
1 - Cyber Arena | Shooter | $19.99
2 - Java Quest | Adventure | $9.99
```

---

## Download Game

```text
=== DOWNLOAD GAME ===
Enter game ID: 1
Downloading game...
[████████████████████] 100%
Download completed!
```

---

## Rate Game

```text
=== RATE GAME ===
Enter game ID: 1
Enter rating: 5
Rating submitted successfully!
```

---

# 📜 Server Logging

The project includes a custom logging system.

Example:

```text
[2026-05-09T21:50] bandr logged in successfully.
[2026-05-09T21:52] Game ID 1 downloaded.
```

Logs are stored inside:

```text
server.log
```

---

# 👨‍💻 Team Contributions

| Member   | Responsibility               |
| -------- | ---------------------------- |
| Mohammed | Server + Multithreading      |
| Bandr    | Database + JDBC              |
| Bashar   | File Transfer + Progress Bar |
| Molham   | Client UI + Menus            |

---

# 📚 Course Concepts Covered

This project demonstrates:

* Java OOP
* Exception Handling
* File I/O Streams
* Serialization Concepts
* Multithreading
* Socket Programming
* JDBC
* MySQL Integration
* Client/Server Architecture
* GitHub Collaboration

---

# 🔐 Future Improvements

Potential future upgrades:

* Password Hashing
* GUI Interface (JavaFX/Swing)
* Real Game File Sizes
* Cloud Database Hosting
* Online Multiplayer Support
* User Profile System
* Download Resume Support

---

# 📸 Suggested Screenshots

Add screenshots here:

* Login Screen
* Game List
* Download Progress Bar
* Admin Panel
* MySQL Database
* Server Logs

---

# 🏁 Final Notes

SteamLiteSimulator was built as a practical demonstration of combining networking, databases, multithreading, and file systems into one integrated Java application.

The project focuses on clean modular structure, teamwork collaboration, and real-world software engineering concepts.

---

# ⭐ GitHub Repository

Repository:

```text
https://github.com/Gon-Hozion/cpit305-steam-sim
```

---

# 📄 License

This project was developed for educational purposes as part of CPIT-305 coursework.
