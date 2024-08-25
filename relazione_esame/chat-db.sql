CREATE DATABASE  IF NOT EXISTS `jpachatdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `jpachatdb`;
-- MySQL dump 10.13  Distrib 8.0.39, for Linux (x86_64)
--
-- Host: localhost    Database: jpachatdb
-- ------------------------------------------------------
-- Server version	8.0.39-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creation_date` date DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
INSERT INTO `chat` VALUES (3,'2024-05-23',_binary '\0','Summer Fest','group'),(4,'2024-05-23',_binary '\0','Charity Run','group'),(11,'2024-08-20',_binary '\0','Food Truck Festival','group'),(12,'2024-08-20',_binary '\0','Comedy Night','group'),(14,NULL,_binary '\0','jd','private'),(15,'2024-08-21',_binary '\0','Malua','group'),(17,'2024-08-23',_binary '\0','user3','private'),(21,'2024-08-25',_binary '\0','aw','private'),(22,'2024-08-25',_binary '\0','Art Exhibition','group'),(23,NULL,_binary '\0','user5','private');
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_messages`
--

DROP TABLE IF EXISTS `chat_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_messages` (
  `chat_id` bigint NOT NULL,
  `messages_id` bigint NOT NULL,
  UNIQUE KEY `UK_mrq0rmc439okhdws2rxsjjhdn` (`messages_id`),
  KEY `FKb27mi3082eolv7k6tavhgq3wc` (`chat_id`),
  CONSTRAINT `FKb27mi3082eolv7k6tavhgq3wc` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`id`),
  CONSTRAINT `FKjtlh6un2reea4nsgktq7qtao0` FOREIGN KEY (`messages_id`) REFERENCES `message` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_messages`
--

LOCK TABLES `chat_messages` WRITE;
/*!40000 ALTER TABLE `chat_messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `username` varchar(255) NOT NULL,
  `admin` bit(1) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `chat_id` bigint NOT NULL,
  PRIMARY KEY (`chat_id`,`username`),
  CONSTRAINT `FK1uggdurk7qndy8jj0vj51i0al` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES ('jd',_binary '\0','1990-01-01',_binary '','John','Doe',3),('js',NULL,'1992-02-02',_binary '','Jane','Smith',3),('lt',NULL,'1996-10-10',_binary '','Lily','Taylor',3),('selfuser',_binary '\0','2002-07-31',_binary '\0','Tu','Tu',3),('user1',_binary '','2002-07-31',_binary '\0','Nicola','Rossi',3),('user2',_binary '','2002-07-31',_binary '\0','Margherita','Verdi',3),('user3',_binary '\0','2002-07-31',_binary '\0','Anna','Bianchi',3),('jd',_binary '','1990-01-01',_binary '\0','John','Doe',4),('mb',_binary '','1980-05-05',_binary '','Mike','Brown',4),('selfuser',_binary '','2002-07-31',_binary '\0','Tu','Tu',4),('user2',_binary '\0','2002-07-31',_binary '\0','Margherita','Verdi',4),('user4',_binary '','2002-07-31',_binary '\0','Matteo','Rossi',4),('user5',_binary '\0','2002-07-31',_binary '\0','Alessandro','Bianchi',4),('jd',_binary '\0','1990-01-01',_binary '\0','John','Doe',11),('js',_binary '\0','1992-02-02',_binary '\0','Jane','Smith',11),('selfuser',_binary '',NULL,_binary '\0','Tu','Tu',11),('aw',_binary '','1995-04-04',_binary '\0','Alice','Williams',12),('bj',_binary '\0','1985-03-03',_binary '\0','Bob','Johnson',12),('mb',_binary '\0','1980-05-05',_binary '\0','Mike','Brown',12),('selfuser',_binary '\0',NULL,_binary '\0','Tu','Tu',12),('jd',_binary '\0','1990-01-01',_binary '\0','John','Doe',14),('selfuser',_binary '\0','2002-08-29',_binary '\0','Tu','Tu',14),('selfuser',_binary '\0','2002-08-29',_binary '','Tu','Tu',15),('user1',_binary '','1990-01-01',_binary '\0','Nicola','Rossi',15),('user2',_binary '\0','1990-01-01',_binary '\0','Margherita','Verdi',15),('selfuser',_binary '\0','2002-08-29',_binary '\0','Tu','Tu',17),('user3',_binary '\0','1990-01-01',_binary '\0','Anna','Bianchi',17),('aw',_binary '\0','1995-04-04',_binary '\0','Alice','Williams',21),('selfuser',_binary '\0','2002-08-29',_binary '\0','Tu','Tu',21),('ed',_binary '\0','1998-06-06',_binary '\0','Emma','Davis',22),('js',_binary '','1992-02-02',_binary '\0','Jane','Smith',22),('mb',_binary '','1980-05-05',NULL,'Mike','Brown',22),('selfuser',_binary '',NULL,_binary '\0','Tu','Tu',22),('sw',_binary '\0','1994-08-08',_binary '','Sophia','Wilson',22),('user3',_binary '\0','1990-01-01',_binary '\0','Anna','Bianchi',22),('selfuser',_binary '\0','2002-08-29',_binary '\0','Tu','Tu',23),('user5',_binary '\0','1990-01-01',_binary '\0','Alessandro','Bianchi',23);
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `chat_id` bigint DEFAULT NULL,
  `sender_chat_id` bigint DEFAULT NULL,
  `sender_username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg2yhptren6nxuyqtnfa8qahgf` (`sender_chat_id`,`sender_username`),
  KEY `FKmejd0ykokrbuekwwgd5a5xt8a` (`chat_id`),
  CONSTRAINT `FKg2yhptren6nxuyqtnfa8qahgf` FOREIGN KEY (`sender_chat_id`, `sender_username`) REFERENCES `member` (`chat_id`, `username`),
  CONSTRAINT `FKmejd0ykokrbuekwwgd5a5xt8a` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (1,'ciao a tutti!','2024-08-13 13:45:00.000000',3,3,'user1'),(8,'come state?','2024-08-13 13:45:00.000000',3,3,'selfuser'),(10,'io tutto bene!','2024-08-17 22:06:25.041000',3,3,'selfuser'),(11,'Ciao a tutti. Questo è il gruppo per la maratona di beneficenza!','2024-08-17 22:21:37.429000',4,4,'selfuser'),(15,'ci vediamo!','2024-08-20 22:34:09.696000',3,3,'selfuser'),(21,'Ciao a tutti! ci vediamo domani!','2024-08-20 23:10:23.233000',11,11,'selfuser'),(24,'ciao john!','2024-08-20 23:50:50.328000',14,14,'selfuser'),(25,'come stai?','2024-08-20 23:51:54.595000',14,14,'selfuser'),(26,'Come state?','2024-08-21 13:05:36.289000',11,11,'selfuser'),(27,'Ciao a tutti! Siete invitati alla serata comedy!','2024-08-24 14:00:00.000000',12,12,'aw'),(28,'Bellissima idea! Io ci sono, chi altro?','2024-08-24 14:05:00.000000',12,12,'bj'),(29,'Benissimo! Magari prima facciamo un piccolo aperitivo, che dite?','2024-08-24 14:06:00.000000',12,12,'aw'),(30,'Ottima idea, io ci sono!','2024-08-24 14:30:00.000000',12,12,'selfuser'),(31,'Io purtroppo non riesco perché lavoro!','2024-08-24 14:40:00.000000',12,12,'mb'),(51,'grazie a tutti è stato belissimo','2024-08-25 16:55:22.000000',15,15,'user1'),(52,'Anche per me!','2024-08-25 15:00:09.735000',15,15,'selfuser'),(53,'Mi dispiace molto!','2024-08-25 15:00:28.556000',12,12,'selfuser'),(54,'Ciao a tutti!','2024-08-25 15:01:21.277000',22,22,'selfuser'),(55,'Ciao, sei libero stasera?','2024-08-25 15:02:15.827000',23,23,'selfuser');
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-26  1:19:39
