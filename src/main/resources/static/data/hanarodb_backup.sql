-- MySQL dump 10.13  Distrib 8.0.42, for macos13.7 (arm64)
--
-- Host: localhost    Database: hanarodb
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `cart_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cart_product` (`cart_id`,`product_id`),
  KEY `FKl7je3auqyq1raj52qmwrgih8x` (`product_id`),
  CONSTRAINT `FKl7je3auqyq1raj52qmwrgih8x` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKpcttvuq4mxppo8sxggjtn5i2c` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cart_user` (`user_id`),
  CONSTRAINT `FKb5o626f86h46m4s7ms6ginnop` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
INSERT INTO `carts` VALUES (5,1),(6,7),(7,8),(8,9);
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `daily_sales_summary`
--

DROP TABLE IF EXISTS `daily_sales_summary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `daily_sales_summary` (
  `stat_date` date NOT NULL,
  `total_sales` bigint NOT NULL,
  `total_orders` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daily_sales_summary`
--

LOCK TABLES `daily_sales_summary` WRITE;
/*!40000 ALTER TABLE `daily_sales_summary` DISABLE KEYS */;
INSERT INTO `daily_sales_summary` VALUES ('2025-08-10',1343666,9,'2025-08-12 05:30:41');
/*!40000 ALTER TABLE `daily_sales_summary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `line_total` int NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` int NOT NULL,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FKlf6f9q956mt144wiv6p1yko16` (`product_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKlf6f9q956mt144wiv6p1yko16` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (26,90000,'포메라니안',2,45000,14,18),(27,78000,'자바스크립트 딥다이브',2,39000,14,19),(28,400000,'에어팟',2,200000,14,20),(29,1780000,'말티쥬 귀여워',2,890000,15,17),(30,46666,'웰시코기',2,23333,15,21),(31,1000000,'에어팟',5,200000,16,20),(32,1780000,'말티쥬 귀여워',2,890000,17,17),(33,90000,'포메라니안',2,45000,27,18),(34,200000,'에어팟',1,200000,27,20),(35,117000,'자바스크립트 딥다이브',3,39000,28,19),(36,890000,'말티쥬 귀여워',1,890000,29,17),(37,46666,'웰시코기',2,23333,29,21),(38,90000,'포메라니안',2,45000,29,18),(39,400000,'에어팟',2,200000,29,20),(40,1780000,'말티쥬 귀여워',2,890000,31,17),(41,46666,'웰시코기',2,23333,31,21);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `status` enum('PAYMENT_COMPLETED','PREPARING_SHIPMENT','IN_TRANSIT','DELIVERED') NOT NULL,
  `status_changed_at` datetime(6) NOT NULL,
  `total_amount` int NOT NULL,
  `total_quantity` int NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (14,'2025-08-12 14:00:12.273844','IN_TRANSIT','2025-08-12 14:32:10.035431',568000,6,1),(15,'2025-08-12 14:01:20.507479','IN_TRANSIT','2025-08-12 14:32:10.035431',1826666,4,7),(16,'2025-08-12 14:02:43.681083','IN_TRANSIT','2025-08-12 14:32:10.035431',1000000,5,8),(17,'2025-08-12 14:12:18.235521','IN_TRANSIT','2025-08-12 14:33:10.031373',1780000,2,9),(24,'2025-08-10 10:15:00.000000','DELIVERED','2025-08-10 10:15:00.000000',90000,2,1),(25,'2025-08-10 14:40:00.000000','DELIVERED','2025-08-10 14:40:00.000000',1780000,3,7),(26,'2025-08-10 19:05:00.000000','DELIVERED','2025-08-10 19:05:00.000000',239000,4,8),(27,'2025-08-10 10:15:00.000000','DELIVERED','2025-08-10 10:15:00.000000',290000,3,1),(28,'2025-08-10 11:05:00.000000','DELIVERED','2025-08-10 11:05:00.000000',117000,3,7),(29,'2025-08-10 14:40:00.000000','DELIVERED','2025-08-10 14:40:00.000000',936666,3,8),(30,'2025-08-11 10:10:00.000000','DELIVERED','2025-08-11 10:10:00.000000',290000,4,1),(31,'2025-08-11 11:15:00.000000','DELIVERED','2025-08-11 11:15:00.000000',929333,4,7);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `price` int DEFAULT NULL,
  `stock` int NOT NULL DEFAULT '0',
  `version` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (17,'말티 기여워','말티쥬 귀여워',890000,21,1),(18,'망망','포메라니안',45000,997,0),(19,'개발자용 책','자바스크립트 딥다이브',39000,964,0),(20,'Apple','에어팟',200000,26,0),(21,'귀엽','웰시코기',23333,5664,0),(22,'일본 강아지','시바견',4444,55555,0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_daily_sales`
--

DROP TABLE IF EXISTS `product_daily_sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_daily_sales` (
  `stat_date` date NOT NULL,
  `product_id` bigint NOT NULL,
  `total_qty` int NOT NULL,
  `total_sales` bigint NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`stat_date`,`product_id`),
  KEY `idx_pds_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_daily_sales`
--

LOCK TABLES `product_daily_sales` WRITE;
/*!40000 ALTER TABLE `product_daily_sales` DISABLE KEYS */;
INSERT INTO `product_daily_sales` VALUES ('2025-08-10',17,1,890000,'2025-08-12 05:30:41'),('2025-08-10',18,2,90000,'2025-08-12 05:30:41'),('2025-08-10',19,3,117000,'2025-08-12 05:30:41'),('2025-08-10',20,1,200000,'2025-08-12 05:30:41'),('2025-08-10',21,2,46666,'2025-08-12 05:30:41');
/*!40000 ALTER TABLE `product_daily_sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_image`
--

DROP TABLE IF EXISTS `product_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_image` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_primary` bit(1) NOT NULL,
  `order_no` int NOT NULL,
  `url` varchar(500) NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_product_order` (`product_id`,`order_no`),
  CONSTRAINT `FK6oo0cvcdtb6qmwsga468uuukk` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_image`
--

LOCK TABLES `product_image` WRITE;
/*!40000 ALTER TABLE `product_image` DISABLE KEYS */;
INSERT INTO `product_image` VALUES (5,_binary '',0,'/static/upload/2025/08/12/6b195aded3cd493baa795915284fc1b3.jpg',18),(6,_binary '',0,'/static/upload/2025/08/12/ff0eb3e8d2d04914bb06a261dc9ccf07.jpeg',17),(7,_binary '',0,'/static/upload/2025/08/12/67eb8ca54428409abca3aeaa6f2fe7c7.jpeg',20),(8,_binary '',0,'/static/upload/2025/08/12/c275691ca2dc451eb4025a854ef0067e.jpeg',21),(9,_binary '',0,'/static/upload/2025/08/12/f8d89ee04d4f4a5591e780e6e20b70e1.jpeg',22);
/*!40000 ALTER TABLE `product_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ROLE_ADMIN','ROLE_USER') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'hanaro@email.com','별돌이','$2a$10$knkxafjX4hqZ0NpsNPUiT.CZnmbyFoNugDNnSHeelCl.31.mU90XK','ROLE_USER'),(6,'admin@email.com','별송이','$2a$10$L2ZvLAlCjXhq4b253VCXAObyyCfDCq4cdvzeUyqiPDRx2PUhDKBha','ROLE_ADMIN'),(7,'hanaro2@email.com','별벗','$2a$10$p2lew9J/QQGIjS34nr3ia.1HcRyxgmfqb7bvjExNykZdasdFukkkG','ROLE_USER'),(8,'hanaro3@email.com','별봄이','$2a$10$XFsHDxzYaZfH1Sur0kKGLuei1vNOIY4silFkxJj50gNwHR6aQQrHS','ROLE_USER'),(9,'hanaro4@email.com','별프로','$2a$10$6b4hAjYeMXFH3wwZn2fFkOwcjQfc6SSiHt0AUTzQiouJNcRQOfpU.','ROLE_USER'),(11,'hanaro6@email.com','별누리','$2a$10$cncm31X49a5x4wTSAQp8I.8nHJpUpESV8Vbmvbd4NGW7BjtsPvS0C','ROLE_USER'),(12,'hanaro7@email.com','별별이','$2a$10$l3BmipCYxxC0B.MYQaj0qeWIR2HuboGxfZpl1/XlCtdQoQ8xXQhjy','ROLE_USER'),(13,'hana@email.com','hana','$2a$10$xR.sRN0w1pOw5yLwRjlNJutzVioR63TqC5gylA1aUbpz0rQ0hlERa','ROLE_USER');
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

-- Dump completed on 2025-08-12 15:11:00
