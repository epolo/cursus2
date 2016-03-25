-- 
-- Database and accounts initialization
--
-- !!! Must be started by admin !!!
--

CREATE DATABASE  IF NOT EXISTS cursus2 /*!40100 DEFAULT CHARACTER SET utf8 */;

-- create user 'tomcat'@'172.31.25.212' identified by '172.31.25.212';
-- grant all on cursus2.* to 'tomcat'@'172.31.25.212';

create user 'tomcat'@'localhost' identified by 'tomcat';

grant all on cursus2.* to 'tomcat'@'localhost';
