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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` enum('admin','teacher','student') NOT NULL DEFAULT 'student',
  `ggle_uid` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `status` enum('active','blocked') NOT NULL DEFAULT 'active',
  `avatar_url` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `personal_info` text,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `old_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ggle_uid` (`ggle_uid`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','1','default_user@local','blocked','/images/avatar-user.svg','default_user','default_user','default_user default_user',NULL,'2015-10-22 12:47:02','0000-00-00 00:00:00',1),(2,'admin',NULL,'frampolo@gmail.com','active','https://cursus-storage.s3.eu-central-1.amazonaws.com/uploads%2F363ffd5d34015631c4d64da9f0f7b24e-IMG_0693.JPG','Ольга','Фрамполь','Ольга Фрамполь',NULL,'2015-10-22 12:48:44','2015-11-28 21:16:35',2),(3,'admin',NULL,'mga.post@gmail.com','active','/upload/user_3/SAM_1598.JPG','Mikhail','Aivazov','Mikhail Aivazov','sdf sD sf s<div>asf sa sa fsfsa</div>','2015-10-22 12:49:19','2015-10-22 12:53:13',3),(4,'admin',NULL,'epolovnikov@gmail.com','active','/images/avatar-user.svg',NULL,NULL,NULL,NULL,'2015-10-22 12:49:34','0000-00-00 00:00:00',4),(5,'admin',NULL,'antynitr@gmail.com','active','/images/avatar-user.svg','Евгений','Степанов','Евгений Степанов',NULL,'2015-10-22 12:51:01','2015-10-22 12:57:07',5),(6,'student','106271342809350084935','sof5859@gmail.com','active','https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg','Соня','Кравцова','Соня Кравцова',NULL,'2015-10-26 15:53:47','0000-00-00 00:00:00',6),(7,'teacher',NULL,'verbmv@gmail.com','active','https://cursus-storage.s3.eu-central-1.amazonaws.com/uploads%2F32e94a982ccf2be5c729829e903cc6d7-DSC_4886.JPG','Мария Владимировна','Вербицкая','Мария Владимировна Вербицкая','Русский язык можно изучать азартно!','2015-10-31 19:21:23','2015-11-28 22:15:59',7),(8,'teacher',NULL,'larfeldman@gmail.com','active','https://cursus-storage.s3.eu-central-1.amazonaws.com/uploads%2F73aff95fbf5e616d4c7fddd42dfe1b52-DSCN0045.JPG','Лариса Михайловна','Фельдман','Лариса Михайловна Фельдман','Бридж - это высококлассный тренинг по психологии общения.','2015-10-31 19:24:50','2015-11-29 13:45:05',8),(9,'teacher',NULL,'kseniakvasha@gmail.com','active','https://cursus-storage.s3.eu-central-1.amazonaws.com/uploads%2F7e71ebb546da7d3324f2333f79120d34-AS1C6778.JPG','Ксения Григорьевна','Кваша','Ксения Григорьевна Кваша','Математические  бои - это одновременно и весело, и серьёзно:)','2015-10-31 19:27:46','2015-12-23 10:34:16',9),(10,'teacher',NULL,'s.i.oleynikova@gmail.com','active','images/avatar-user.svg','Станислава Игоревна','Олейникова','Станислава Игоревна Олейникова','Сколько всего интересного!','2015-10-31 19:33:49','0000-00-00 00:00:00',10),(11,'teacher',NULL,'natalikorovina@gmail.com','active','https://cursus-storage.s3.eu-central-1.amazonaws.com/uploads%2Fffbf88b30cb54286aae5b5125efeef62-image.jpg','Наталия Владимировна','Коровина','Наталия Владимировна Коровина','Русский язык.','2015-11-01 19:30:55','2015-12-03 12:58:56',11),(12,'student','103969403315321340996','curators.ngs@gmail.com','active','https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg','Кураторы','8 класса','Кураторы 8 класса',NULL,'2015-11-01 21:11:14','0000-00-00 00:00:00',12),(13,'student','111515299369351362845','lavandaria.laverde@gmail.com','active','https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg','Lavandaria','Laverde','Lavandaria Laverde',NULL,'2015-11-01 23:35:41','0000-00-00 00:00:00',13),(14,'admin',NULL,'exprofesso3@gmail.com','active','/images/avatar-user.svg','','Администратор',' Администратор',NULL,'2015-11-03 01:32:29','2015-12-14 11:12:05',14),(15,'admin',NULL,'exprofesso4@gmail.com','active','/images/avatar-user.svg',NULL,NULL,NULL,NULL,'2015-11-03 01:32:40','0000-00-00 00:00:00',15),(16,'student','108342433308132757476','cursutest7@gmail.com','active','https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg','7 test','фамилио','7 test фамилио',NULL,'2015-11-20 12:22:18','0000-00-00 00:00:00',16),(17,'teacher',NULL,'egorovmaxim1974@gmail.com','active','/images/avatar-user.svg','Максим Михайлович','Егоров','Максим Михайлович Егоров','ЧГК, брейн-ринг','2015-11-21 12:47:15','0000-00-00 00:00:00',17),(18,'student','109574517221043690181','miklevershkopf@gmail.com','active','https://lh6.googleusercontent.com/-epkqYZiSsK0/AAAAAAAAAAI/AAAAAAAAAEI/tuAYRq2sbEs/photo.jpg','Mikle','Vershkopf','Mikle Vershkopf',NULL,'2015-11-22 18:13:10','0000-00-00 00:00:00',18),(19,'student','118227977524851389378','alexey.aykin@gmail.com','active','https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg','Alexey','Aykin','Alexey Aykin',NULL,'2015-11-27 14:11:24','0000-00-00 00:00:00',19),(20,'student','101242705846484436338','glightvaz@gmail.com','active','https://lh3.googleusercontent.com/-W2Eh2WBBWuU/AAAAAAAAAAI/AAAAAAAAEmM/DiSLiRgbpq0/photo.jpg','Gregory','Ayvazov','Gregory Ayvazov',NULL,'2015-11-28 20:13:48','0000-00-00 00:00:00',20),(21,'student','109237005454963249004','evavasilevs@gmail.com','active','https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg','Ева','В','Ева В',NULL,'2015-12-11 22:04:33','0000-00-00 00:00:00',21),(22,'student','117192498285203694493','epolo.student@gmail.com','active','https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg','Евгений','Половников','Евгений Половников',NULL,'2015-12-13 21:21:30','2015-12-13 21:22:53',22),(23,'student','108441931627639319019','family.buder@gmail.com','active','https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg','Fam.','Buder','Fam. Buder',NULL,'2015-12-14 19:33:23','0000-00-00 00:00:00',23),(24,'student','104884772842312922048','kyrill.buder.wolf@gmail.com','active','https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg','','',' ',NULL,'2015-12-15 11:51:44','0000-00-00 00:00:00',24),(25,'student','105084863413594909255','ayvazovasv@gmail.com','active','https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg','Светлана','Айвазова','Светлана Айвазова',NULL,'2015-12-16 18:54:18','2015-12-16 19:06:33',25),(26,'student','103771038961993411132','akivenson@gmail.com','active','https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg','Elena','Akivenson','Elena Akivenson',NULL,'2015-12-22 10:31:04','0000-00-00 00:00:00',26);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
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
