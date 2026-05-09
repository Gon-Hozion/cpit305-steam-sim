CREATE DATABASE IF NOT EXISTS steam_lite_db;
USE steam_lite_db;

CREATE TABLE IF NOT EXISTS accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'user'
);

CREATE TABLE IF NOT EXISTS games (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    developer VARCHAR(100),
    genre VARCHAR(50),
    price DOUBLE,
    file_path VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS downloads (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT,
    game_id INT,
    download_count INT DEFAULT 1,
    last_downloaded TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ratings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT,
    game_id INT,
    rating INT CHECK (rating >= 1 AND rating <= 5)
);

INSERT IGNORE INTO accounts(id, username, password, role)
VALUES
(1, 'admin', 'admin123', 'admin');

INSERT IGNORE INTO games(id, title, developer, genre, price, file_path)
VALUES
(1, 'Cyber Arena', 'BMBO Studios', 'Shooter', 19.99, 'resources/games/cyber_arena.zip'),
(2, 'Java Quest', 'KAU Games', 'Adventure', 9.99, 'resources/games/java_quest.zip'),
(3, 'Racing Kings', 'FastSoft', 'Racing', 14.99, 'resources/games/racing_kings.zip');
