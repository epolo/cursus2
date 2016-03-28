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
-- Table structure for table `courses_students`
--

DROP TABLE IF EXISTS `courses_students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `courses_students` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `answers` text,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `courses_students_course_id_student_id_index` (`course_id`,`student_id`),
  KEY `student_id` (`student_id`),
  CONSTRAINT `courses_students_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `courses_students_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses_students`
--

LOCK TABLES `courses_students` WRITE;
/*!40000 ALTER TABLE `courses_students` DISABLE KEYS */;
INSERT INTO `courses_students` VALUES (8,5,6,'[{\"question\":{\"body\":\"Умеете ли Вы играть в какие-нибудь карточные игры со взятками?\",\"answer1\":\"Нет\",\"answer2\":\"Да, в преферанс\",\"answer3\":\"Да, в покер\",\"answer4\":\"Да\"},\"answer\":\"Нет\"}]','2015-12-13 15:27:15','2015-12-13 16:09:52'),(9,6,6,'[{\"question\":{\"body\":\"Умеете ли Вы играть в какие-либо карточные игры? Если да, то в какие?\",\"answer1\":\"Нет, не умею\",\"answer2\":\"Умею в преферанс\",\"answer3\":\"Умею в покер\",\"answer4\":\"Умею в другие игры\"},\"answer\":\"Умею в покер\"},{\"question\":{\"body\":\"Учились ли Вы играть в бридж раньше?\",\"answer1\":\"Да, учился\",\"answer2\":\"Нет, не учился\",\"answer3\":\"-\",\"answer4\":\"-\"},\"answer\":\"Да, учился\"}]','2015-12-13 16:08:15','2015-12-13 16:08:16'),(10,11,6,'[{\"question\":{\"body\":\"Сколько языков ты знаешь?\",\"answer1\":\"1\",\"answer2\":\"2\",\"answer3\":\"3\",\"answer4\":\"4\"},\"answer\":\"1\"},{\"question\":{\"body\":\"Любишь ли ты играть в логические игры?\",\"answer1\":\"да\",\"answer2\":\"нет\",\"answer3\":\"время от времени\",\"answer4\":\"\"},\"answer\":\"да\"}]','2015-11-20 11:40:45','2015-11-28 15:52:47'),(11,12,6,'[{\"question\":{\"body\":\"Удавалось ли вам получить удовольствие от урока русского языка хоть раз в жизни?\",\"answer1\":\"Да, это мой любимый предмет\",\"answer2\":\"Да, но редко\",\"answer3\":\"Как правило, мне скучно на этих уроках\",\"answer4\":\"Даже представить не могу, что это может кому-то нравиться\"},\"answer\":\"Да, но редко\"}]','2015-12-13 16:03:27','0000-00-00 00:00:00'),(28,10,12,'[{\"question\":{\"body\":\"Есть ли у тебя в школе уроки русского языка?\",\"answer1\":\"Да\",\"answer2\":\"Нет\",\"answer3\":\"Есть, но не в школе\",\"answer4\":\"Были\"},\"answer\":\"Да\"},{\"question\":{\"body\":\"Занимался ли ты когда-нибудь проектной деятельностью?\",\"answer1\":\"Да\",\"answer2\":\"Нет\",\"answer3\":\"Не уверен(а)\",\"answer4\":\"Не помню\"},\"answer\":\"Нет\"},{\"question\":{\"body\":\"Что тебе интересно делать в курсе русского языка?\",\"answer1\":\"Учиться писать грамотно\",\"answer2\":\"Сочинять тексты\",\"answer3\":\"Решать олимпиадные задачи\",\"answer4\":\"Разбираться в устройстве грамматики\"},\"answer\":\"Сочинять тексты\"},{\"question\":{\"body\":\"Как ты оцениваешь свой уровень знаний по русскому языку?\",\"answer1\":\"Высокий\",\"answer2\":\"Низкий\",\"answer3\":\"Средний\",\"answer4\":\"Не знаю\"},\"answer\":\"Не знаю\"}]','2015-11-01 21:11:43','0000-00-00 00:00:00'),(31,4,13,'[{\"question\":{\"body\":\"Вопрос №1\",\"answer1\":\"А\",\"answer2\":\"Б\",\"answer3\":\"В\",\"answer4\":\"Г\"},\"answer\":\"А\"}]','2015-11-01 23:37:20','0000-00-00 00:00:00'),(32,6,13,'[{\"question\":{\"body\":\"Умеете ли Вы играть в какие-либо карточные игры? Если да, то в какие?\",\"answer1\":\"Нет, не умею\",\"answer2\":\"Умею в преферанс\",\"answer3\":\"Умею в покер\",\"answer4\":\"Умею в другие игры\"},\"answer\":\"Нет, не умею\"},{\"question\":{\"body\":\"Учились ли Вы играть в бридж раньше?\",\"answer1\":\"Да, учился\",\"answer2\":\"Нет, не учился\",\"answer3\":\"-\",\"answer4\":\"-\"},\"answer\":\"Нет, не учился\"}]','2015-11-21 15:41:06','2015-12-08 15:08:26'),(48,11,20,'[{\"question\":{\"body\":\"Сколько языков ты знаешь?\",\"answer1\":\"1\",\"answer2\":\"2\",\"answer3\":\"3\",\"answer4\":\"4\"},\"answer\":\"4\"},{\"question\":{\"body\":\"Любишь ли ты играть в логические игры?\",\"answer1\":\"да\",\"answer2\":\"нет\",\"answer3\":\"время от времени\",\"answer4\":\"\"},\"answer\":\"да\"}]','2015-11-28 20:54:12','2015-11-28 20:54:13'),(51,5,21,'[{\"question\":{\"body\":\"Умеете ли Вы играть в какие-нибудь карточные игры со взятками?\",\"answer1\":\"Нет\",\"answer2\":\"Да, в преферанс\",\"answer3\":\"Да, в покер\",\"answer4\":\"Да\"},\"answer\":\"Нет\"}]','2015-12-13 15:26:38','2015-12-24 11:56:54'),(54,5,22,'[{\"question\":{\"body\":\"Умеете ли Вы играть в какие-нибудь карточные игры со взятками?\",\"answer1\":\"Нет\",\"answer2\":\"Да, в преферанс\",\"answer3\":\"Да, в покер\",\"answer4\":\"Да\"},\"answer\":\"Да\"}]','2015-12-13 21:22:13','2015-12-24 16:26:37'),(67,12,26,'[{\"question\":{\"body\":\"Удавалось ли вам получить удовольствие от урока русского языка хоть раз в жизни?\",\"answer1\":\"Да, это мой любимый предмет\",\"answer2\":\"Да, но редко\",\"answer3\":\"Как правило, мне скучно на этих уроках\",\"answer4\":\"Даже представить не могу, что это может кому-то нравиться\"},\"answer\":\"Да, это мой любимый предмет\"}]','2015-12-22 10:36:48','0000-00-00 00:00:00');
/*!40000 ALTER TABLE `courses_students` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-01  1:18:59
