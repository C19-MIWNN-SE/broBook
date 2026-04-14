DROP DATABASE IF EXISTS brobook;
CREATE DATABASE brobook;
CREATE USER IF NOT EXISTS 'broBook'@'localhost'
IDENTIFIED BY 'broBookPW';
GRANT ALL ON brobook.*
TO 'broBook'@'localhost';
FLUSH PRIVILEGES;