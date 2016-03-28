CREATE DATABASE  IF NOT EXISTS `cursus2` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `cursus2`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: cursus2
-- ------------------------------------------------------
-- Server version	5.6.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `topics`
--

DROP TABLE IF EXISTS `topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `topics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) DEFAULT NULL,
  `dtype` enum('course','initiative','forum','mediatek') NOT NULL,
  `title` varchar(1000) DEFAULT NULL,
  `is_delete` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `course_id` (`course_id`),
  KEY `dtype` (`dtype`),
  CONSTRAINT `topics_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topics`
--

LOCK TABLES `topics` WRITE;
/*!40000 ALTER TABLE `topics` DISABLE KEYS */;
INSERT INTO `topics` VALUES (4,5,'course','',0,'2016-01-24 02:42:08','2016-01-24 02:42:08'),(5,9,'course','',0,'2016-01-24 02:42:08','2016-01-24 02:42:08'),(6,10,'course','',0,'2016-01-24 02:42:08','2016-01-24 02:42:08'),(7,11,'course','',0,'2016-01-24 02:42:08','2016-01-24 02:42:08'),(8,4,'course','',0,'2016-01-24 02:42:08','2016-01-24 02:42:08'),(9,12,'course','',0,'2016-01-24 02:42:08','2016-01-24 02:42:08'),(10,6,'course','',0,'2016-01-24 02:42:08','2016-01-24 02:42:08'),(11,8,'course','',0,'2016-01-24 02:42:08','2016-01-24 02:42:08'),(12,13,'course','',0,'2016-01-24 02:42:08','2016-01-24 02:42:08'),(13,7,'course','',0,'2016-01-24 02:42:08','2016-01-24 02:42:08'),(16,NULL,'initiative','',0,'2016-01-24 02:45:11','2016-01-24 02:45:11'),(17,NULL,'forum','',0,'2016-01-24 02:45:11','2016-01-24 02:45:11'),(18,NULL,'mediatek','',0,'2016-01-24 02:45:11','2016-01-24 02:45:11');
/*!40000 ALTER TABLE `topics` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-01  1:18:58
