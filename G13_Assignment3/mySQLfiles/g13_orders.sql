-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: g13
-- ------------------------------------------------------
-- Server version	8.3.0

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
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `OrderNumber` varchar(45) NOT NULL,
  `ParkName` varchar(45) NOT NULL,
  `Id` varchar(45) NOT NULL,
  `Day` varchar(45) NOT NULL,
  `Month` varchar(45) NOT NULL,
  `Time` varchar(45) NOT NULL,
  `NumberOfVisitors` varchar(45) NOT NULL,
  `PhoneNumber` varchar(45) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `OrderType` varchar(45) NOT NULL,
  PRIMARY KEY (`OrderNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES ('10587492','Yosemite National111','','22-02-24 13:45','','','3','04-321586711','visitor3@example.com',''),('25618943','Everglades National','','22-02-24 11:10','','','1','04-1987432','visitor5@example.com',''),('38296517','Glacier National222','','22-02-24 14:30','','','6','04-1987222111','visitor7@example.com',''),('48257934','Yellowstone National','','22-02-24 9:00','','','2','04-7564839','visitor1@example.com',''),('51482763','Banff National','','22-02-24 16:00','','','10','04-8756913','visitor8@example.com',''),('62974581','Great Smoky Mountains','','22-02-24 10:00','','','7','04-6372518','visitor9@example.com',''),('68932475','Grand Villa','','22-02-24 15:20','','','8','04-6372222','visitor4@example.com',''),('74321856','Zion National','','22-02-24 12:00','','','4','04-5689374','visitor6@example.com',''),('87354219','Acadia National','','22-02-24 13:00','','','9','04-0978613','visitor10@example.com',''),('93721658','Central Park','','22-02-24 10:30','','','5','04-4362951','visitor2@example.com','');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-16 21:30:34
