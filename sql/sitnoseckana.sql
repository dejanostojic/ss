-- MySQL dump 10.15  Distrib 10.0.21-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: sitnoseckana
-- ------------------------------------------------------
-- Server version	10.0.21-MariaDB

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
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config`
--

LOCK TABLES `config` WRITE;
/*!40000 ALTER TABLE `config` DISABLE KEYS */;
INSERT INTO `config` VALUES (13,'active_calendar_week','40','Determines which week menus will be presented to front end users');
/*!40000 ALTER TABLE `config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `page`
--

DROP TABLE IF EXISTS `page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `page` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL,
  `template` varchar(45) DEFAULT NULL,
  `kind` varchar(30) DEFAULT NULL,
  `language_code` varchar(10) DEFAULT NULL,
  `parent_page_id` bigint(20) unsigned DEFAULT NULL,
  `name` varchar(120) DEFAULT NULL,
  `depth` int(11) DEFAULT NULL,
  `ord` int(11) DEFAULT NULL,
  `title` varchar(150) DEFAULT NULL,
  `body` mediumtext,
  `published` tinyint(1) DEFAULT '1',
  `member_only` tinyint(1) unsigned DEFAULT NULL,
  `primary_navigation` tinyint(1) unsigned DEFAULT NULL,
  `secondary_navigation` tinyint(1) unsigned DEFAULT NULL,
  `mobile_navigation` tinyint(1) unsigned DEFAULT NULL,
  `meta_description` text,
  `thumb_id1` bigint(20) DEFAULT NULL,
  `thumb_id2` bigint(20) DEFAULT NULL,
  `thumb_id3` bigint(20) DEFAULT NULL,
  `thumb_id4` bigint(20) DEFAULT NULL,
  `meta_title` varchar(150) DEFAULT NULL,
  `lead` text,
  `extras` text,
  `tag` varchar(512) DEFAULT NULL,
  `with_comments` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `parentitem_id` (`parent_page_id`),
  KEY `ci_parent_ord` (`parent_page_id`,`ord`),
  KEY `ci_pub_primnav_type_kind_ord` (`published`,`primary_navigation`,`type`,`kind`,`ord`)
) ENGINE=InnoDB AUTO_INCREMENT=562 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `page`
--

LOCK TABLES `page` WRITE;
/*!40000 ALTER TABLE `page` DISABLE KEYS */;
INSERT INTO `page` VALUES (47,'shop','products','index','sr',0,'home',0,1,'Naslovna','',1,0,1,0,1,'ovde ide meta opis za istu stvar',195908,0,NULL,NULL,'Ovde ide meta title za Optimizaciju','','','',1),(316,'platform','homeberg','index','sr',47,'pocetna',1,1,'Početna','<p>body eeeeeeeeee&nbsp;<em>italic&nbsp;<strong>bloc</strong></em></p>',1,0,0,0,0,'',0,0,NULL,NULL,'','primary','',NULL,1),(331,'platform','home','index','me',0,'home',0,1,'Počjetna','',1,0,1,0,0,'',0,NULL,NULL,NULL,'','','',NULL,1),(405,'app','page','index','sr',47,'o-nama',1,2,'O nama','<h1>Mi nismo kul</h1>',1,0,0,1,0,'',0,0,NULL,NULL,'','Llleda','','',1),(556,'platform','home','index','sp',0,'home',0,1,'Platform',NULL,1,0,1,0,0,NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,0),(557,'shop','products','index','sr',47,'menu',1,7,'Menu','',1,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,NULL),(559,'app','newsletter','index','sr',47,'newsletter',1,7,'Newsletter','',1,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,NULL),(560,'user','registration','potvrdi-nalog','sr',47,'potvrdi-nalog',1,7,'Potvrdite nalog','<p>Ovo će da bude sjajno!</p>',1,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Potvdite vaš nalog ovde',NULL,NULL,NULL),(561,'shop','orders','index','sr',47,'porudzbine',1,7,'Porudzbine','',1,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,NULL);
/*!40000 ALTER TABLE `page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop_menu`
--

DROP TABLE IF EXISTS `shop_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_menu` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) unsigned NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `shop_menu_product_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_menu`
--

LOCK TABLES `shop_menu` WRITE;
/*!40000 ALTER TABLE `shop_menu` DISABLE KEYS */;
INSERT INTO `shop_menu` VALUES (10,1,'2015-03-02'),(11,4,'2015-03-03'),(12,5,'2015-03-04'),(13,6,'2015-03-05'),(14,7,'2015-03-06'),(15,2,'2015-03-02'),(16,8,'2015-03-03'),(17,9,'2015-03-04'),(18,10,'2015-03-05'),(19,11,'2015-03-06'),(20,3,'2015-03-02'),(21,12,'2015-03-03'),(22,13,'2015-03-04'),(23,14,'2015-03-05'),(24,15,'2015-03-06'),(25,1,'2015-03-12'),(26,8,'2015-03-12'),(27,14,'2015-03-12'),(28,14,'2015-03-13'),(29,9,'2015-03-13'),(30,6,'2015-03-13'),(31,1,'2015-03-11'),(32,9,'2015-03-11'),(33,13,'2015-03-11'),(34,1,'2015-03-19'),(35,8,'2015-03-19'),(36,12,'2015-03-19'),(37,1,'2015-04-20'),(38,4,'2015-04-21'),(39,5,'2015-04-22'),(40,6,'2015-04-23'),(41,7,'2015-04-24'),(42,2,'2015-04-20'),(43,8,'2015-04-21'),(44,9,'2015-04-22'),(45,10,'2015-04-23'),(46,11,'2015-04-24'),(47,3,'2015-04-20'),(48,12,'2015-04-21'),(49,13,'2015-04-22'),(50,14,'2015-04-23'),(51,15,'2015-04-24'),(52,1,'2015-04-27'),(53,4,'2015-04-28'),(54,5,'2015-04-29'),(55,6,'2015-04-30'),(56,7,'2015-05-01'),(57,2,'2015-04-27'),(58,8,'2015-04-28'),(59,9,'2015-04-29'),(60,10,'2015-04-30'),(61,11,'2015-05-01'),(62,3,'2015-04-27'),(63,12,'2015-04-28'),(64,13,'2015-04-29'),(65,14,'2015-04-30'),(66,15,'2015-05-01'),(67,7,'2015-05-18'),(68,6,'2015-05-19'),(69,5,'2015-05-20'),(70,4,'2015-05-14'),(71,4,'2015-05-21'),(72,1,'2015-05-22'),(73,2,'2015-05-18'),(74,8,'2015-05-19'),(75,9,'2015-05-20'),(76,10,'2015-05-21'),(77,11,'2015-05-22'),(78,3,'2015-05-22'),(79,12,'2015-05-21'),(80,13,'2015-05-20'),(81,14,'2015-05-19'),(82,15,'2015-05-18'),(83,1,'2015-05-25'),(84,2,'2015-05-26'),(85,5,'2015-05-27'),(86,6,'2015-05-28'),(87,7,'2015-05-29'),(88,8,'2015-05-25'),(89,9,'2015-05-27'),(90,10,'2015-05-28'),(91,11,'2015-05-29'),(92,5,'2015-05-26'),(93,3,'2015-05-25'),(94,12,'2015-05-26'),(95,13,'2015-05-27'),(96,14,'2015-05-28'),(97,15,'2015-05-29'),(98,1,'2015-07-10'),(99,2,'2015-07-10'),(100,3,'2015-07-10'),(101,8,'2015-07-09'),(102,12,'2015-07-09'),(103,4,'2015-07-09'),(104,1,'2015-07-13'),(105,4,'2015-07-14'),(106,5,'2015-07-15'),(107,6,'2015-07-16'),(108,7,'2015-07-17'),(109,2,'2015-07-13'),(110,8,'2015-07-14'),(111,9,'2015-07-15'),(112,10,'2015-07-16'),(113,11,'2015-07-17'),(114,3,'2015-07-13'),(115,12,'2015-07-14'),(116,13,'2015-07-15'),(117,14,'2015-07-16'),(118,15,'2015-07-17'),(119,1,'2015-07-20'),(120,8,'2015-07-20'),(121,12,'2015-07-20'),(122,14,'2015-07-21'),(123,1,'2015-08-13'),(124,2,'2015-08-13'),(125,3,'2015-08-13'),(126,4,'2015-08-14'),(127,8,'2015-08-14'),(128,12,'2015-08-14'),(129,1,'2015-08-17'),(130,4,'2015-08-18'),(131,5,'2015-08-19'),(132,6,'2015-08-20'),(133,7,'2015-08-21'),(134,2,'2015-08-17'),(135,8,'2015-08-18'),(136,9,'2015-08-19'),(137,10,'2015-08-20'),(138,11,'2015-08-21'),(139,3,'2015-08-17'),(140,12,'2015-08-18'),(141,13,'2015-08-19'),(142,14,'2015-08-20'),(143,15,'2015-08-21'),(144,1,'2015-09-21'),(146,4,'2015-09-22'),(147,5,'2015-09-23'),(148,6,'2015-09-24'),(149,7,'2015-09-25'),(150,2,'2015-09-21'),(151,8,'2015-09-22'),(152,9,'2015-09-23'),(153,10,'2015-09-24'),(154,11,'2015-09-25'),(155,3,'2015-09-21'),(156,12,'2015-09-22'),(157,13,'2015-09-23'),(158,14,'2015-09-24'),(159,15,'2015-09-25'),(161,7,'2015-09-28'),(162,6,'2015-09-29'),(163,5,'2015-09-30'),(164,4,'2015-10-01'),(165,1,'2015-10-02'),(166,2,'2015-09-28'),(167,8,'2015-09-29'),(168,9,'2015-09-30'),(169,10,'2015-10-01'),(170,11,'2015-10-02'),(171,3,'2015-10-02'),(172,12,'2015-10-01'),(173,13,'2015-09-30'),(174,14,'2015-09-29'),(175,15,'2015-09-28');
/*!40000 ALTER TABLE `shop_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop_order`
--

DROP TABLE IF EXISTS `shop_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL,
  `date` date NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `shipping_address` varchar(255) DEFAULT NULL,
  `extra_info` text,
  `total_price` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shop_order_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_order`
--

LOCK TABLES `shop_order` WRITE;
/*!40000 ALTER TABLE `shop_order` DISABLE KEYS */;
INSERT INTO `shop_order` VALUES (1,101,'2015-09-24','Pera','Peric','pera@peric.com','011123123','Pere Velimirovica 8, Vidikovac','Nadam se da može klopa.',780.00),(2,101,'2015-09-24','Petar','Peric','pera@peric.com','0118408055','Lorem ipsum 4, bla bla','Dodatne napomene kažu sledeće:....',730.00),(3,102,'2015-09-24','Dejan','Dejanovic','dejan@dejanovic.com','01142421234','Lorem Ipsum, 5 Dolor Sit Amet','Lorem ipsum dolor sit amet. Gaudeaums igitur, juvenus dus sumus.',670.00),(4,103,'2015-09-24','Marko','Marković','marko@markovic.com','0112345542','Marka Markovica 4, Lipsum','Lorem ipsum dolor sit amet',750.00),(5,101,'2015-09-25','Petar','Petrovic','pera@peric.com','879788878','ADresa perina,','Bez napomena',NULL),(6,104,'2015-09-25','Neko','Gerila','1vdsf1+1ojr4f66nqjqk@sharklasers.com','2134','Lipsum. asdf 3','Napomena',1050.00),(7,104,'2015-09-25','Neki','Tamo','1vdsf1+1ojr4f66nqjqk@sharklasers.com','142352435','LIpoasdkf asd, 4 542','Sve kul. fladksgflaskfdhg',710.00),(8,105,'2015-09-25','Gorila','Mejl','1veg3s+23xqjuytelhik@sharklasers.com','3124234','sadfalksdjf kajl','Test asfdasd',870.00),(9,105,'2015-09-25','Guerilla','Email','1veg3s+23xqjuytelhik@sharklasers.com','24325345','sdfakjsdfl','aslkdjflakje ale ',620.00),(10,105,'2015-09-25','Guerilla','Email','1veg3s+23xqjuytelhik@sharklasers.com','091235234','kasdfasd , 234 2','kul 1veg3s+23xqjuytelhik@sharklasers.com ',530.00),(11,105,'2015-09-25','Guerilla','Email','1veg3s+23xqjuytelhik@sharklasers.com','123432','Ejkasdf 3, bgd','Ej hej hej',970.00),(12,105,'2015-09-25','Gerila','Email','1veg3s+23xqjuytelhik@sharklasers.com','234453','asdfer 32 ','napomasd f1veg3s+23xqjuytelhik@sharklasers.com',310.00),(13,105,'2015-09-25','Guerilla','Email','1veg3s+23xqjuytelhik@sharklasers.com','011233424','kasjdflkasjdf 3, bgh','Emam',600.00),(14,105,'2015-09-25','Guerilla','Email','1veg3s+23xqjuytelhik@sharklasers.com','011/789-987','ADres','Dodatno nista',290.00),(15,105,'2015-09-25','Guerrilla','Email','1veg3s+23xqjuytelhik@sharklasers.com','011/7987-98','adfe 3, bgd','Nema',310.00),(16,105,'2015-09-25','Guerrilla','Email','1veg3s+23xqjuytelhik@sharklasers.com','123/234-789','ADresa neka','lipusadf ',290.00),(17,106,'2015-09-25','Guerilla','Mail','psawobam@sharklasers.com','011/987-985','Adresa Ulica 5, mesto','psawobam@sharklasers.com',310.00),(18,107,'2015-09-25','Aleksandar','Aljehin','vghyuxnj@sharklasers.com','011/789-987','Neka Ruska 8, Sibir','',720.00),(19,108,'2015-09-26','Tester','ss','ss@sharklasers.com','011/987-987','Ulica i broj 8, Beograd','Samo da klopam',600.00),(20,109,'2015-09-26','Member1','Test','member1@sharklasers.com','011/789-7778','Member Adress 2, Cloud','',430.00),(21,109,'2015-09-26','member1','Bez prez','member1@sharklasers.com','011/879-654','Neka Adresa 2, Grad','',720.00),(22,109,'2015-09-27','Member1','Test','member1@sharklasers.com','011/789-7778','Member Adress 2, Cloud','',1070.00);
/*!40000 ALTER TABLE `shop_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop_order_item`
--

DROP TABLE IF EXISTS `shop_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_order_item` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) unsigned NOT NULL,
  `product_id` bigint(20) unsigned NOT NULL,
  `variation_id` bigint(20) unsigned NOT NULL,
  `price` decimal(20,2) DEFAULT NULL,
  `discount` decimal(20,2) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `shop_order_item_order_id` (`order_id`),
  KEY `shop_order_item_product_id` (`product_id`),
  KEY `shop_order_item_variation_id` (`variation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_order_item`
--

LOCK TABLES `shop_order_item` WRITE;
/*!40000 ALTER TABLE `shop_order_item` DISABLE KEYS */;
INSERT INTO `shop_order_item` VALUES (1,1,7,1,290.00,0.00,1,'2015-09-25'),(2,1,17,0,150.00,0.00,1,'1970-01-01'),(3,1,25,0,170.00,0.00,2,'1970-01-01'),(4,2,11,1,310.00,0.00,1,'2015-09-25'),(5,2,18,0,120.00,0.00,1,'1970-01-01'),(6,2,23,0,150.00,0.00,2,'1970-01-01'),(7,3,7,1,290.00,0.00,1,'2015-09-25'),(8,3,16,0,140.00,0.00,1,'1970-01-01'),(9,3,24,0,120.00,0.00,2,'1970-01-01'),(10,4,11,1,310.00,0.00,1,'2015-09-25'),(11,4,23,0,150.00,0.00,2,'1970-01-01'),(12,4,21,0,140.00,0.00,1,'1970-01-01'),(13,5,18,0,120.00,0.00,1,'2015-09-25'),(14,5,24,0,120.00,0.00,2,'2015-09-25'),(15,5,21,0,140.00,0.00,1,'2015-09-25'),(16,5,20,0,250.00,0.00,1,'2015-09-25'),(17,5,2,2,170.00,0.00,1,'2015-09-21'),(18,6,9,1,310.00,0.00,1,'2015-09-23'),(19,6,15,1,290.00,0.00,1,'2015-09-25'),(20,7,1,1,290.00,0.00,1,'2015-09-21'),(21,7,17,0,150.00,0.00,2,'2015-09-25'),(22,7,24,0,120.00,0.00,1,'2015-09-25'),(23,8,17,0,150.00,0.00,2,'2015-09-25'),(24,8,21,0,140.00,0.00,2,'2015-09-25'),(25,8,3,1,290.00,0.00,1,'2015-09-21'),(26,9,2,1,310.00,0.00,1,'2015-09-21'),(27,9,3,2,160.00,0.00,1,'2015-09-21'),(28,9,17,0,150.00,0.00,1,'2015-09-25'),(29,10,18,0,120.00,0.00,2,'2015-09-25'),(30,10,3,1,290.00,0.00,1,'2015-09-21'),(31,11,2,1,310.00,0.00,1,'2015-09-21'),(32,11,3,2,160.00,0.00,1,'2015-09-21'),(33,11,20,0,250.00,0.00,2,'2015-09-25'),(34,12,2,1,310.00,0.00,1,'2015-09-21'),(35,13,2,1,310.00,0.00,1,'2015-09-21'),(36,13,3,1,290.00,0.00,1,'2015-09-21'),(37,14,3,1,290.00,0.00,1,'2015-09-21'),(38,15,11,1,310.00,0.00,1,'2015-09-25'),(39,16,12,1,290.00,0.00,1,'2015-09-22'),(40,17,2,1,310.00,0.00,1,'2015-09-21'),(41,18,7,1,290.00,0.00,1,'2015-09-25'),(42,18,11,1,310.00,0.00,1,'2015-09-25'),(43,18,18,0,120.00,0.00,1,'2015-09-25'),(44,19,2,1,310.00,0.00,1,'2015-09-21'),(45,19,3,1,290.00,0.00,1,'2015-09-21'),(46,20,11,1,310.00,0.00,1,'2015-09-25'),(47,20,18,0,120.00,0.00,1,'2015-09-26'),(48,21,11,1,310.00,0.00,1,'2015-09-25'),(49,21,15,1,290.00,0.00,1,'2015-09-25'),(50,21,18,0,120.00,0.00,1,'2015-09-25'),(51,22,7,1,290.00,0.00,1,'2015-09-28'),(52,22,2,2,170.00,0.00,2,'2015-09-28'),(53,22,15,2,160.00,0.00,2,'2015-09-28'),(54,22,24,0,120.00,0.00,1,'2015-09-27');
/*!40000 ALTER TABLE `shop_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop_product`
--

DROP TABLE IF EXISTS `shop_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_product` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `ingredients` varchar(512) DEFAULT NULL,
  `product_type_id` bigint(20) unsigned NOT NULL,
  `price` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`id`),
  KEY `product_type_id` (`product_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_product`
--

LOCK TABLES `shop_product` WRITE;
/*!40000 ALTER TABLE `shop_product` DISABLE KEYS */;
INSERT INTO `shop_product` VALUES (1,'ŠARENI MIKS','(spanać, crveni kupus, praziluk, masline, rotkvice, koren peršuna, ječam, seme bundeve, maslinovo ulje, balzamiko od višnje, himalajska so)',7,0.00),(2,'LEBLEBIJA SA KREM SALATOM OD PATLIDžANA','(leblebija, kukuruz, šargarepa, susam, heljda; plavi patlidžan, beli luk, orasi, peršun, vlašac, majonez od prepeličijih jaja ili senf)',14,0.00),(3,'PROSO SA SUVIM VOĆEM','(proso, cimet, med ili agave sirup, suvo grožđe, smokve i brusnica, lešnik)',15,0.00),(4,'PROBIOTSKA SALATA','(kiseli kupus, šargarepa, rukola, ljubičasti luk, zrno ovsa, semenke bundeve, badem, kim, dresing od maslinovog ulja, senfa i himalajske soli)',7,0.00),(5,'CELE - KELE SALATA','(celer, keleraba, šargarepa, raž, suncokret seme, dresing od domaćeg kikiriki butera, limuna i himalajske soli)',7,0.00),(6,'SICILIJANSKA SALATA','(slatki kupus, komorač, pomorandža, orasi, seme suncokreta, zrno spelte, crna ribizla, mešavina začina, maslinovo ulje, sok limuna)',7,0.00),(7,'ŠARENA SALATA SA ČIČOKOM','(čičoka, cvekla, rukola, šargarepa, seme bundeve, zrno ovsa, indijski orah, dresing od balzamika, maslinovog ulja i himalajske soli)',7,0.00),(8,'ZELENIŠ SA AZUKI PASULjEM','(azuki pasulj, zelena salata, kukuruz, zrno ječma, seme bundeve, orah, dresing od nerafinisanog suncokretovog ulja, alzamika, himalajske soli i kumina)',14,0.00),(9,'MEDITERANSKA BORANIJA','(boranija, rukola, leblebija, šargarepa, beli luk, masline, seme bundeve, proso, kari začin, kurkuma, origano, maslinovo ulje, balzamiko)',14,0.00),(10,'ZELENA SALATA I CRVENO SOČIVO','(zelena salata, radič, celer, crveno sočivo, zrno ječma, laneno seme, sok limuna, klice grčkog semena, krem dresing od indijskog oraha)',14,0.00),(11,'VEGE BURGER I SALATA','(burger: tikvice, krompir, bundeva, list peršuna i mirođije, beli luk, prezla, maslinovo ulje, him. so; salata: zelena salata, kupus, šargarepa, mladi luk, ječam, seme suncokreta, dresing)',14,0.00),(12,'BANANA SPLIT','(banana, mandarina, kivi, čokoladni dresing od indijskog oraha, kakaoa, ruma i meda ili agave sirupa)',15,0.00),(13,'KRUŠKE SA MRVICAMA','(kruške i urme sa zrnom amaranta posute mrvicama od oraha, meda i rogača)',15,0.00),(14,'BAŠTENSKA ČAROLIJA','(šargarepa, jabuka, cvekla, zrno heljde, badem, preliv od malina, banane, meda i soka limuna)',15,0.00),(15,'MULTIVTAMINSKI MIKS','(mandarine, kivi, ananas, heljda zrno, indijski orah, med ili agave sirup)',15,0.00),(16,'Jabuka','',16,140.00),(17,'Šargarepa i jabuka','',16,150.00),(18,'Koktel 1','Jabuka, cvekla, peršun',16,120.00),(19,'Koktel 2','Jabuka, cvekla, celer',16,150.00),(20,'Ceđena pomorandža','ereqđđđđđšššččććć',16,250.00),(21,'Kupus','',16,140.00),(22,'Kupus sa šargarepom','',16,150.00),(23,'Malina smuti','Banana, maline, puno zrno ovsa, sok limuna',17,150.00),(24,'Zeleni smuti','Banana, spanać, kivi, pomorandža',17,120.00),(25,'Šumski smuti','banana, urme, laneno seme, šumsko voće',17,170.00);
/*!40000 ALTER TABLE `shop_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop_product_type`
--

DROP TABLE IF EXISTS `shop_product_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_product_type` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `weekly` tinyint(1) unsigned DEFAULT NULL,
  `color` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_product_type`
--

LOCK TABLES `shop_product_type` WRITE;
/*!40000 ALTER TABLE `shop_product_type` DISABLE KEYS */;
INSERT INTO `shop_product_type` VALUES (7,'Presan obrok',1,'red'),(14,'Kombinovan obrok',1,'blue'),(15,'Voćni obrok',1,'brown'),(16,'Sok',0,NULL),(17,'Smuti',0,NULL);
/*!40000 ALTER TABLE `shop_product_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop_product_type_variation`
--

DROP TABLE IF EXISTS `shop_product_type_variation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_product_type_variation` (
  `type_id` bigint(20) unsigned NOT NULL,
  `variation_id` bigint(20) unsigned NOT NULL,
  `price` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`type_id`,`variation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_product_type_variation`
--

LOCK TABLES `shop_product_type_variation` WRITE;
/*!40000 ALTER TABLE `shop_product_type_variation` DISABLE KEYS */;
INSERT INTO `shop_product_type_variation` VALUES (7,1,290.00),(7,2,160.00),(14,1,310.00),(14,2,170.00),(15,1,290.00),(15,2,160.00);
/*!40000 ALTER TABLE `shop_product_type_variation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop_product_variation`
--

DROP TABLE IF EXISTS `shop_product_variation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_product_variation` (
  `product_id` bigint(20) unsigned NOT NULL,
  `variation_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`product_id`,`variation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_product_variation`
--

LOCK TABLES `shop_product_variation` WRITE;
/*!40000 ALTER TABLE `shop_product_variation` DISABLE KEYS */;
/*!40000 ALTER TABLE `shop_product_variation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop_variation`
--

DROP TABLE IF EXISTS `shop_variation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_variation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_variation`
--

LOCK TABLES `shop_variation` WRITE;
/*!40000 ALTER TABLE `shop_variation` DISABLE KEYS */;
INSERT INTO `shop_variation` VALUES (1,'Velika kutija'),(2,'Mala kutija'),(3,'Poslastica nedelje');
/*!40000 ALTER TABLE `shop_variation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(150) DEFAULT NULL,
  `password` varchar(512) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `activated` tinyint(1) DEFAULT '0',
  `activation_key` varchar(255) DEFAULT '',
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `shipping_address` varchar(255) DEFAULT NULL,
  `registration_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (97,'dostadmin','bcrypt+sha512$b938f6ab81b2d3047c69aa17$243261243132246d36355666574f6747563031693548364f496a442e2e353857586e356a7a596957624a3254382f5855415161684875655874362e71','ostdejan@gmail.com',1,NULL,NULL,NULL,NULL,NULL,NULL),(101,'pera@peric.com',NULL,'pera@peric.com',0,NULL,NULL,NULL,NULL,NULL,NULL),(102,'dejan@dejanovic.com',NULL,'dejan@dejanovic.com',0,NULL,'Dejan','Dejanovic','01142421234','Lorem Ipsum, 5 Dolor Sit Amet',NULL),(103,'marko@markovic.com',NULL,'marko@markovic.com',0,NULL,'Marko','Marković','0112345542','Marka Markovica 4, Lipsum',NULL),(104,'1vdsf1+1ojr4f66nqjqk@sharklasers.com',NULL,'1vdsf1+1ojr4f66nqjqk@sharklasers.com',0,NULL,'Neko','Gerila','2134','Lipsum. asdf 3',NULL),(105,'1veg3s+23xqjuytelhik@sharklasers.com','bcrypt+sha512$3803dbc58730253787d46176$24326124313224642e31622e716c454161416c58507068675a47634875337851534743673864723778413072732e336d57764c3142325a6c30524171','1veg3s+23xqjuytelhik@sharklasers.com',0,'c050a8d9-781f-4ede-a7ae-c18b61092188','Gorila','Mejl','3124234','sadfalksdjf kajl','2015-09-25'),(106,'psawobam@sharklasers.com','bcrypt+sha512$391000bf8ab27f79efdb02cc$2432612431322470414739465a64477a36377730476f4c6f2e746c4c653858706a564c54346b507835487862644937366a684135386337614754662e','psawobam@sharklasers.com',0,'16ac13b2-fe33-477a-a94d-35fedbc01698','Guerilla','Mail','011/987-985','Adresa Ulica 5, mesto','2015-09-25'),(107,'vghyuxnj@sharklasers.com','bcrypt+sha512$1a43688fa76621e016effc6c$24326124313224776167566d784231637a53524b34496c6274564c632e41796b39526f41684f4a3130316f416c675747355438727746487344304136','vghyuxnj@sharklasers.com',1,'','Aleksandar','Aljehin','011/789-987','Neka Ruska 8, Sibir','2015-09-25'),(108,'ss@sharklasers.com','bcrypt+sha512$6c1997309c297071638ca82c$243261243132244c4b4173364830534666324c71576d574e594639472e6f503141753566552f306f727064366356512f346f506a564c6e2e684e6b2e','ss@sharklasers.com',1,'','Tester','ss','011/987-987','Ulica i broj 8, Beograd','2015-09-26'),(109,'member1@sharklasers.com','bcrypt+sha512$010324970eabbde4fc332bbd$2432612431322438575770636a395642534c73714357335a696f46754f6f58396d767a624b33506177635258695a2e5a446e75464a79384577324779','member1@sharklasers.com',1,'','Member1','Test','011/789-7778','Member Adress 2, Cloud','2015-09-26');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned DEFAULT NULL,
  `role_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=458 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (453,97,'admin'),(457,109,'member'),(456,108,'member');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-09-27 19:09:26
