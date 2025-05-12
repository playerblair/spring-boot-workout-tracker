CREATE TABLE IF NOT EXISTS exercise (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    muscle_group VARCHAR(50)
);