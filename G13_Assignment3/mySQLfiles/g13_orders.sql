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
  `VisitorId` varchar(45) NOT NULL,
  `Date` varchar(45) NOT NULL,
  `Time` varchar(45) NOT NULL,
  `NumberOfVisitors` varchar(45) NOT NULL,
  `PhoneNumber` varchar(45) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `VisitorType` varchar(45) NOT NULL,
  `ExitTime` varchar(45) NOT NULL,
  `OrderStatus` varchar(45) NOT NULL,
  `PayStatus` varchar(45) NOT NULL,
  `TotalCost` varchar(45) NOT NULL,
  PRIMARY KEY (`OrderNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES ('01234567','Hyde Park','198765432','02-06-24','10:55','6','0539012345','example9@gmail.com','individual','13:55','processing','',''),('11223344','Hyde Park','207717109','25-03-24','21:54','52','0539876543','test@example.com','individual','12:45','cancelled automatically','unpaid','300'),('11378290','Hyde Park','207717109','21-03-24','08:00','20','0535283455','waseem21saleem@gmail.com','small group','16:37','completed','',''),('12114452','Yellowstone Park','207717109','24-03-24','22:58','4','0543216547','visitor2@example.com','group','23:04','completed','paid','400'),('12319429','Hyde Park','207717109','27-03-24','19:05','50','0123456789','ahmad.aj.20@hotmail.com','organized group','21:03','processing','not paid','425.0'),('12345678','Central Park','123456789','25-03-24','21:54','48','0501234567','example1@gmail.com','individual','13:30','cancelled automatically','',''),('12365478','Central Park','207717109','21-03-30','10:30','2','0509876543','user1@example.com','individual','12:30','cancelled manually','unpaid','200'),('14523636','Central Park','207717109','21-04-02','09:00','5','0505554444','example1@example.com','group','13:00','processing','paid','500'),('14785236','Central Park','207717109','21-03-24','10:00','2','0501234567','visitor@example.com','individual','12:00','cancelled manually','paid','200'),('14861720','Hyde Park','207717109','27-03-24','15:30','4','0123123123','was@was.was','organized group','17:30','processing','paid','340.0'),('14963258','Yellowstone Park','207717109','21-03-28','16:00','2','0541234567','visitor1@example.com','individual','18:30','cancelled manually','unpaid','150'),('15963246','Hyde Park','207717109','21-03-29','13:45','6','0531111222','test1@example.com','group','17:30','confirmed','paid','600'),('17712165','Hyde Park','207717109','01-04-24','15:30','5','1234567890','was@was.was','organized group','17:30','processing','not paid','425.0'),('23456789','Hyde Park','345678912','27-03-24','23:45','2','0543456789','waseem21saleem@gmail.com','individual','17:15','processing','',''),('25433589','Hyde Park','207717109','29-03-24','15:00','5','0123456789','kjk@lk.sd','small group','16:00','cancelled manually','not paid','425.0'),('25982314','Yellowstone National Park','207717109','28-03-24','13:29','50','0123123123','Mohamaddukhi6@gmail.com','organized group','16:29','cancelled manually','paid','425.0'),('31686393','Hyde Park','207717109','26-03-24','20:14','10',NULL,NULL,'unplanned group','20:38','completed','paid','900.0'),('32665458','Hyde Park','207717109','27-03-24','16:14','15',NULL,NULL,'unplanned group','17:03','completed','paid','1350.0'),('35946834','Hyde Park','207717109','28-03-24','12:30','5','0123123123','was@was.was','organized group','14:30','processing','paid','425.0'),('39189654','Hyde Park','207717109','14-03-24','11:20','4','0123456789','was.was@was.was','small group','12:20','cancelled manually','not paid','340.0'),('45678901','Central Park','987654321','03-06-24','14:40','10','0500123456','example10@gmail.com','group','17:40','processing','paid',''),('47207590','Yellowstone National Park','207717109','28-03-24','10:30','5','0123123123','waseem21saleem@gmail.com','organized group','13:30','processing','paid','425.0'),('47969569','Hyde Park','207717109','21-03-24','11:19','25','0559550692','noooooooooor@nor.nor','small group','12:19','cancelled manually','paid',''),('55753103','Hyde Park','207717109','20-03-24','08:00','30','0535283455','aa@aa.aa','small group','12:00','cancelled manually','paid',''),('56012437','Hyde Park','207717109','01-04-24','15:30','4','0123456789','was@was.was','organized group','17:30','processing','not paid','340.0'),('56789012','Hyde Park','432198765','30-05-24','13:45','7','0536789012','example6@gmail.com','group','16:45','processing','paid',''),('67890123','Yellowstone National Park','207717109','29-03-24','08:30','50','0528901234','example8@gmail.com','group','11:30','cancelled manually','paid',''),('68523698','Yellowstone Park','207717109','21-03-25','11:30','5','0547654321','user@example.com','group','15:00','cancelled manually','unpaid','500'),('74521458','Central Park','207717109','21-03-27','14:20','4','0502468135','example@example.com','group','18:00','cancelled manually','paid','400'),('76782750','Central Park','207405911','25-03-24','14:03','5','0123456789','adham@gmail.com','small group','19:03','cancelled manually','not paid','425.0'),('77875035','Central Park','207717109','25-05-24','13:31','5','1234567899','was@was.com','small group','15:31','cancelled manually','paid',''),('79629955','Hyde Park','207717109','22-03-24','11:20','5','0123456789','was.was@was.was','small group','12:20','cancelled manually','paid','425.0'),('80218680','Hyde Park','207717109','26-03-24','22:02','10',NULL,NULL,'organized group','22:04','completed','paid','900.0'),('82085338','Hyde Park','207717109','29-03-24','12:30','5','0112233221','was@was.was','organized group','14:30','processing','paid','425.0'),('87019887','Hyde Park','207717109','29-03-24','11:00','5','0512345678','lo@lo.lo','small group','12:00','cancelled manually','not paid','425.0'),('87654321','Yellowstone National Park','987654321','29-03-24','12:30','50','0532345678','example2@gmail.com','group','15:30','processing','',''),('89012345','Central Park','321987654','31-05-24','15:20','10','0547890123','example7@gmail.com','individual','18:20','processing','',''),('90123456','Hyde Park','207717109','21-04-01','14:45','3','0539871234','test2@example.com','individual','17:15','cancelled manually','unpaid','300'),('93551337','Hyde Park','207717109','21-03-24','11:20','3','0123456789','was@was.was','small group','12:20','cancelled manually','not paid','255.0'),('98765432','Central Park','543216789','28-03-24','22:12','10','0524567890','waseem21saleem@gmail.com','group','12:00','confirmed','','');
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

-- Dump completed on 2024-03-28 17:44:52
