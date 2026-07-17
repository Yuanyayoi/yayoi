-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: gchc_db
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Table structure for table `articles`
--

DROP TABLE IF EXISTS `articles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `author` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `content` text COLLATE utf8mb4_unicode_ci,
  `cover_image` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `published` bit(1) NOT NULL,
  `summary` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `views` int NOT NULL,
  `show_on_home` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articles`
--

LOCK TABLES `articles` WRITE;
/*!40000 ALTER TABLE `articles` DISABLE KEYS */;
INSERT INTO `articles` VALUES (1,'Admin',1,'<p>GCHC 发布新一代快充解决方案，引领行业创新！</p><p>欢迎关注我们的最新动态，获取更多产品资讯。</p>','https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=800&q=80',NULL,_binary '','GCHC 新产品发布，引领行业技术创新','GCHC 新产品发布','2026-06-26 10:21:54.025513',4,1),(2,'Admin',2,'<p>新能源行业快速发展，技术创新持续推进。</p><p>GCHC 持续加大研发投入，保持行业领先地位。</p>','https://images.unsplash.com/photo-1517649763962-0c623066013b?w=800&q=80',NULL,_binary '','新能源行业快速发展，技术创新持续推进','行业发展趋势','2026-06-26 10:02:33.231320',3,1);
/*!40000 ALTER TABLE `articles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `awards`
--

DROP TABLE IF EXISTS `awards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `awards` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `organization` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `year` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `image` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sort_order` int NOT NULL DEFAULT '0',
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `awards`
--

LOCK TABLES `awards` WRITE;
/*!40000 ALTER TABLE `awards` DISABLE KEYS */;
INSERT INTO `awards` VALUES (2,'常州市龙城英才计划大湾区邀请赛一等奖','中共常州市武进区委人才工作领导小组办公室','2024年4月','','/uploads/e3750c30-8e6e-4a8e-abac-3c8ad206f158.png',0,1,'2026-06-26 16:02:17.394014','2026-06-26 16:31:12.619408'),(3,'商务部全球数贸大赛铜奖','全球数字贸易创新大赛组委会','2024年9月','','/uploads/b612f19d-4df1-463f-ad17-083c20294114.png',0,1,'2026-06-26 17:11:13.811186','2026-06-26 17:18:52.266249'),(4,'江西省首届高层次人才创新创业大赛三等奖','中共江西省委人才工作领导小组办公室','2025年9月','','/uploads/d7fc68e1-30dd-4fb1-8694-fc5fbc8c05f9.png',0,1,'2026-06-26 17:13:06.012667','2026-06-26 17:13:06.012667');
/*!40000 ALTER TABLE `awards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carousels`
--

DROP TABLE IF EXISTS `carousels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carousels` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `subtitle` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carousels`
--

LOCK TABLES `carousels` WRITE;
/*!40000 ALTER TABLE `carousels` DISABLE KEYS */;
INSERT INTO `carousels` VALUES (8,'2026-06-10 02:57:13.037272','依托10C级高倍率电池技术，连接绿电接入、分布式储能、极速超充与数字化调度，打造充储一体能源生态。','/uploads/1d12a6d5-75e2-492c-b398-2aaa33000063.png','构建10C+充储一体生态网络','','2026-06-29 15:45:22.490308'),(15,'2026-06-17 17:57:26.119487','面向低空经济、机器人、电动重卡、特种车辆、移动储能等场景，满足爆发功率、快速补能与长周期运行需求。','/uploads/4bcc8a1e-353f-45de-bc00-2578f2f94285.png','面向高功率场景的高倍率电池系统','','2026-06-29 15:45:11.268694'),(16,'2026-06-25 10:43:59.611030','围绕10C高倍率充放电，构建全域热管理、智能均衡与高耐久循环体系，在提升补能效率的同时兼顾寿命与安全。','/uploads/76747752-f5c3-4ec7-8812-626cce4a8b00.png','10C超充，突破快充、寿命与安全悖论','','2026-06-29 15:44:57.225357'),(17,'2026-06-25 11:14:33.729358','融合高速激光成型、3D打印增材制造与AI驱动BMS，覆盖电芯、Pack与系统集成，为新能源场景提供完整快充解决方案。','/uploads/20fe0c16-8f5b-48ec-96d3-ff74ea41cf1c.png','领先的电池快充技术解决方案服务商','','2026-06-29 15:44:46.647411');
/*!40000 ALTER TABLE `carousels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  `sort_order` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_t8o6pivur7nn124jehx7cygw5` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,NULL,'高性能快充解决方案',_binary '','快充系统',NULL,0,NULL),(2,NULL,'智能电池管理系统',_binary '','电池管理',NULL,0,NULL),(3,NULL,'新能源充电桩设备',_binary '','充电桩',NULL,0,NULL),(4,'2026-06-25 09:54:30.062291','储能\n',_binary '','储能',NULL,0,'2026-06-25 09:54:30.062291');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `core_values`
--

DROP TABLE IF EXISTS `core_values`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `core_values` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `icon` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sort_order` int NOT NULL DEFAULT '0',
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `core_values`
--

LOCK TABLES `core_values` WRITE;
/*!40000 ALTER TABLE `core_values` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_values` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `features`
--

DROP TABLE IF EXISTS `features`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `features` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `style` int DEFAULT '1',
  `sort_order` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `features`
--

LOCK TABLES `features` WRITE;
/*!40000 ALTER TABLE `features` DISABLE KEYS */;
INSERT INTO `features` VALUES (6,'2026-06-11 02:55:47.028708','10C超级快充技术：5分钟充电80%，极化阻抗降低70%；\n智能动态充放电策略：全主动均衡电芯电压，安全接纳超大电流，避免活性物质损耗。','极速超充技术','2026-06-29 17:39:01.635343',5,1),(7,'2026-06-11 02:56:14.741605','团队汇聚全球顶尖院校专家和5名+博士，在电池技术研发、生产管理、市场营销、商务谈判等行业领域都具有深厚的背景、资深的影响和丰富的经验，曾主导国内外50+重大项目。\n','优秀技术团队','2026-06-29 11:50:54.753600',6,0),(11,'2026-06-26 17:39:40.830864','新电池系统结构设计：提升散热能力，完善温控体系，磷酸铁锂电池在10C倍率循环10000次（三元锂电池2600次循环），容量衰减率≤20%。','高耐久循环体系','2026-06-29 17:39:02.757911',3,2),(12,'2026-06-26 17:41:22.636134','1、三维立体温控系统：换热面积增加20倍，10C快充温差＜2℃，最高温度≤36℃。\n2、精准温域自控技术：自研温控板，-30℃分钟级预热启动，10C大倍率放电温差≤6℃。\n','全域热管理方案','2026-06-29 17:39:03.897317',2,3),(13,'2026-06-29 11:54:28.429584','1、优秀质量能量密度：采用全新薄片电芯和Pack结构，体积利用率提升72.3%；\n2、军工级环境适应性：高效降温（纳米级渗透、30秒阻断热蔓延）、化学抑制（靶向分解、复燃风险＜5%），热失控风险降低90%。\n','高能量密度与可靠性','2026-06-29 17:39:05.313732',4,4),(14,'2026-06-29 11:54:45.882927','        材料创新、激光3D制造工艺、BMS智能管理及独有温度控制体系降低了生产成本，量产后成本较同类产品相对低20%。\n','量产低成本优势','2026-06-29 17:39:07.347878',1,5);
/*!40000 ALTER TABLE `features` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `icons`
--

DROP TABLE IF EXISTS `icons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `icons` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `image_url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `sort_order` int NOT NULL DEFAULT '0',
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_icons_image_url` (`image_url`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `icons`
--

LOCK TABLES `icons` WRITE;
/*!40000 ALTER TABLE `icons` DISABLE KEYS */;
INSERT INTO `icons` VALUES (1,'/uploads/icon_d8fe4d71.svg','低成本','',1,0,'2026-06-29 11:49:24.521511','2026-06-29 11:49:24.521511'),(2,'/uploads/icon_4feab4e7.svg','热管理','',1,0,'2026-06-29 11:49:40.012546','2026-06-29 11:49:40.012546'),(3,'/uploads/icon_6017efa0.svg','循环','',1,0,'2026-06-29 11:49:50.415259','2026-06-29 11:49:50.415259'),(4,'/uploads/icon_4862f769.svg','可靠','',1,0,'2026-06-29 11:50:21.333561','2026-06-29 11:50:21.333561'),(5,'/uploads/icon_c74e7222.svg','快充','',1,0,'2026-06-29 11:50:36.922686','2026-06-29 11:50:36.922686'),(6,'/uploads/icon_8cfa29d6.svg','团队','',1,0,'2026-06-29 11:50:46.881057','2026-06-29 11:50:46.881057');
/*!40000 ALTER TABLE `icons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `message` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `replied` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (2,'2026-06-10 09:02:13.049270','2953964506@qq.com','111','111','',_binary '','2026-06-10 09:02:30.073661'),(3,'2026-06-24 15:51:03.523785','','11','详细','18942332950',_binary '','2026-06-24 15:51:15.535510'),(4,NULL,'','21','微信','18927321897',_binary '\0',NULL);
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `milestones`
--

DROP TABLE IF EXISTS `milestones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `milestones` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `year` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sort_order` int NOT NULL DEFAULT '0',
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `milestones`
--

LOCK TABLES `milestones` WRITE;
/*!40000 ALTER TABLE `milestones` DISABLE KEYS */;
INSERT INTO `milestones` VALUES (1,'2026年','完善量产线','建立产线，完善从原材料到成品工序，规模化生产，实现年供货500MWh的产量。',0,1),(2,'2027年','建立灯塔工厂','批量供应特种高端行业（低空经济、机器人、机器狗等）/市场规模大的行业。实现年供货2GWh的产量。\n',1,1),(3,'2028年','赋能产业','与电池生产等厂家进行技术合作，建立“超充”联盟。实现年供货4GWh的产量。\n',2,1),(4,'2029年','构建“10C+”生态','建立开放的低成本柔性“10C+”充储一体生态网络。\n\n',3,1);
/*!40000 ALTER TABLE `milestones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint DEFAULT NULL,  
  `created_at` datetime(6) DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `enabled` bit(1) NOT NULL,
  `image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `stock` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `show_on_home` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_settings`
--

DROP TABLE IF EXISTS `site_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `site_settings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `group_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `setting_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `setting_value` text COLLATE utf8mb4_unicode_ci,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKiw2h5rke4aufwrd6ksp3w8ffo` (`group_name`,`setting_key`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_settings`
--

LOCK TABLES `site_settings` WRITE;
/*!40000 ALTER TABLE `site_settings` DISABLE KEYS */;
INSERT INTO `site_settings` VALUES (1,'2026-06-10 08:04:55.369561','contact','address','11','2026-06-17 17:35:41.149885'),(2,'2026-06-10 08:04:55.432278','contact','phone','18942332950','2026-06-17 17:35:41.155777'),(3,'2026-06-10 08:04:55.439850','contact','email','2953964506@qq.com','2026-06-17 17:35:41.161056'),(4,'2026-06-10 08:04:55.444544','contact','businessHours','工作日 9:00-18:00','2026-06-17 17:35:41.166232'),(5,'2026-06-10 08:04:55.449199','contact','description','有任何问题，欢迎随时与我们联系。','2026-06-17 17:35:41.173135'),(6,'2026-06-10 08:19:19.937292','about','companyName','四川国驰恒创科技有限公司','2026-06-26 15:11:37.686152'),(7,'2026-06-10 08:19:19.943847','about','companyDesc','       四川国驰恒创科技有限公司，核心技术源自美国辛辛那提大学博士团队，专注于提供新能源电池快充技术解决方案。\n       突破传统电池“快充-寿命-安全”三元悖论，将“高速激光成型工艺、3D打印增材制造技术及AI驱动BMS”深度融合进电池生产制造全过程，为客户提供完整的电池快充技术解决方案。','2026-06-26 15:11:37.698110'),(8,'2026-06-10 08:19:19.949102','about','mission','将颠覆性技术融入电池生产制造 提供价值显著的产品和服务','2026-06-26 15:11:37.706095'),(9,'2026-06-10 08:19:19.954108','about','vision','成为全球 超充电池技术领导者','2026-06-26 15:11:37.714953'),(10,'2026-06-10 08:19:19.959855','about','values','创新、专业、诚信、共赢\n','2026-06-10 08:19:40.082238'),(11,'2026-06-11 01:44:45.304617','contentModules','homeSpotlight','{\"subtitle\":\"XRE 高能极速超充\",\"title\":\"以 10C+ 快充技术服务新能源产业\",\"description\":\"围绕动力电池、储能电池与移动电力场景，融合高速激光成型、3D 打印增材制造和 AI 驱动 BMS，提供从电芯开发、热管理、Pack 集成到量产导入的完整解决方案。\",\"buttonText\":\"了解产品服务\",\"buttonLink\":\"/products\",\"metrics\":[{\"value\":\"5分钟\",\"label\":\"10%-80% 极速补能\"},{\"value\":\"10C\",\"label\":\"高倍率充放电能力\"},{\"value\":\"52项\",\"label\":\"已申请发明专利\"},{\"value\":\"全链条\",\"label\":\"研发验证到量产导入\"}]}','2026-06-29 16:02:41.366770'),(12,'2026-06-11 02:04:00.228465','contentModules','aboutHero','{\"enabled\":true,\"eyebrow\":\"\",\"title\":\"领先的电池快充技术解决方案服务商\",\"description\":\"四川国驰恒创科技有限公司专注新能源电池快充技术解决方案，融合高速激光成型工艺、3D 打印增材制造与 AI 驱动 BMS，为动力电池、储能电池及多场景移动电力提供从研发验证到产业化导入的一体化能力。\",\"stats\":[{\"value\":\"10C\",\"label\":\"高倍率快充技术\"},{\"value\":\"52项\",\"label\":\"已申请发明专利\"},{\"value\":\"350+\",\"label\":\"团队论文及专利成果\"},{\"value\":\"50+\",\"label\":\"重大项目经验\"}]}','2026-06-29 16:02:41.348206'),(13,'2026-06-11 02:04:00.250837','contentModules','aboutPosition','{\"enabled\":true,\"subtitle\":\"公司定位\",\"title\":\"突破“快充、寿命、安全”三元悖论\",\"paragraphs\":[\"公司核心技术源自美国辛辛那提大学博士团队，围绕 10C 级高倍率充放电需求，在材料、电芯结构、热管理、系统集成和 BMS 控制策略上协同创新，有效提升电池在多种气候环境下的快速充放电效率与稳定性。\",\"方案适用于锂电池、半固态及固态电池等多种技术路线，已形成从基础理论、材料研发、电芯开发、系统集成、测试验证到量产导入的研发闭环。\"]}','2026-06-29 16:02:41.359654'),(14,'2026-06-11 02:04:00.257096','contentModules','aboutMission','{\"enabled\":true,\"items\":[{\"title\":\"愿景\",\"text\":\"成为全球超充电池技术领导者\"},{\"title\":\"使命\",\"text\":\"将颠覆性技术融入电池生产制造，提供价值显著的产品和服务\"},{\"title\":\"价值观\",\"text\":\"务实、创新、价值驱动，以技术服务行业客户\"}]}','2026-06-29 16:02:41.355841'),(15,'2026-06-11 02:04:00.262996','contentModules','aboutCapabilities','{\"enabled\":true,\"subtitle\":\"核心能力\",\"title\":\"从电芯到 Pack 的全链条快充技术体系\",\"items\":[{\"index\":\"01\",\"title\":\"高速激光成型工艺\",\"text\":\"围绕电极结构、离子传输路径和热管理结构进行工艺创新，降低极化阻抗并提升高倍率充放电效率。\"},{\"index\":\"02\",\"title\":\"3D 打印增材制造\",\"text\":\"通过多尺度 3D 成型与材料改性能力，支撑电芯结构、导热路径和定制化 Pack 的快速迭代。\"},{\"index\":\"03\",\"title\":\"AI 驱动 BMS\",\"text\":\"实时监测电池健康状态，动态匹配充放电曲线，进行主动均衡与安全控制，提升系统全生命周期表现。\"},{\"index\":\"04\",\"title\":\"全域热管理\",\"text\":\"构建风冷、液冷及可移动式液冷方案，兼顾高倍率散热效率、系统重量、成本和维护便利性。\"}]}','2026-06-29 16:02:41.342214'),(16,'2026-06-11 02:04:00.269882','contentModules','aboutTeam','{\"enabled\":true,\"subtitle\":\"核心团队\",\"title\":\"博士团队牵头，跨越新能源、AI 与数字制造\",\"description\":\"团队汇聚 5 名以上博士及产业化专家，成员覆盖电池技术研发、材料研究、增材制造、生产管理、质量体系、市场营销和商务合作等领域，曾主导或参与国内外 50+ 重大项目。\",\"proof\":[{\"value\":\"5名+\",\"text\":\"博士与高层次专家\"},{\"value\":\"30余项\",\"text\":\"国家级、省部级及横向科研项目经验\"},{\"value\":\"数万次\",\"text\":\"论文引用与行业影响力积累\"},{\"value\":\"全链条\",\"text\":\"研发、制造、质量与市场协同能力\"}]}','2026-06-29 16:02:41.363019'),(17,'2026-06-11 02:04:00.276436','contentModules','aboutMilestones','{\"enabled\":true,\"subtitle\":\"荣誉与进展\",\"title\":\"持续推进技术验证与产业合作\",\"items\":[{\"time\":\"2024.04\",\"title\":\"常州市龙城英才计划大湾区邀请赛一等奖\",\"text\":\"团队以超充电池技术方案获得创新赛事认可，持续推进产业资源对接。\"},{\"time\":\"2024.06\",\"title\":\"中国科技大学先进技术研究院入园大赛第一名\",\"text\":\"项目在技术先进性、产业化路径和团队能力方面获得评审认可。\"},{\"time\":\"2025.09\",\"title\":\"江西省首届高层次人才创新创业大赛三等奖\",\"text\":\"XRE 高能极速超充电池系统持续参与技术验证、项目路演与产业合作。\"},{\"time\":\"产业验证\",\"title\":\"警用巡逻车、无人机、机器狗等场景试点\",\"text\":\"围绕特种车辆与无人化巡逻装备开展动力电池系统升级验证，推动成果落地。\"}]}','2026-06-29 16:02:41.351882'),(18,'2026-06-11 02:04:00.282836','contentModules','productsHero','{\"enabled\":true,\"eyebrow\":\"\",\"title\":\"XRE 高能极速超充电池系统\",\"description\":\"面向低空经济、特种设备、移动储能、机器人与高频商用车辆等场景，提供高倍率快充、热管理、BMS 与系统集成解决方案。\",\"metricValue\":\"5分钟\",\"metricLabel\":\"完成 10%-80% 电量补给\"}','2026-06-29 16:02:41.371862'),(19,'2026-06-11 02:04:00.289607','contentModules','productsOverview','{\"enabled\":true,\"subtitle\":\"产品定位\",\"title\":\"以 10C 快充能力重构移动电力体验\",\"description\":\"XRE 高能极速超充电池系统以高速激光成型工艺、3D 打印增材制造与 AI 驱动 BMS 为核心，在不牺牲安全与寿命的前提下提升补能效率，兼容多材料体系与多规格 Pack 形态。\",\"highlights\":[{\"title\":\"10C 超级快充\",\"text\":\"实现 5 分钟 10%-80% SOC 的极速补能体验，降低高频作业等待时间。\"},{\"title\":\"长循环寿命\",\"text\":\"磷酸铁锂体系支持 10C 倍率下长循环应用，降低更换与维护频率。\"},{\"title\":\"全域热管理\",\"text\":\"通过风冷、液冷与可移动式液冷方案，平衡散热效率、成本、重量和维护。\"},{\"title\":\"AI-BMS 安全控制\",\"text\":\"动态监测健康状态，主动均衡电芯容量和电压，提升快充安全性。\"}]}','2026-06-29 16:02:41.374976'),(20,'2026-06-11 02:04:00.296947','contentModules','productsTechnology','{\"enabled\":true,\"subtitle\":\"技术亮点\",\"title\":\"围绕快充、寿命、安全的系统级创新\",\"items\":[{\"index\":\"01\",\"title\":\"全域热管理方案\",\"text\":\"三维立体温控、精准温域自控与模块化液冷能力协同，支撑高倍率充放电下的温差控制。\"},{\"index\":\"02\",\"title\":\"高耐久循环体系\",\"text\":\"通过电池系统结构优化与温控体系完善，提升散热能力和高倍率循环可靠性。\"},{\"index\":\"03\",\"title\":\"锂离子传输路径优化\",\"text\":\"采用梯度孔道与碳纳米管网络构建高速离子通道，改善极化阻抗与倍率性能。\"},{\"index\":\"04\",\"title\":\"量产低成本优势\",\"text\":\"材料创新、激光 3D 制造工艺、BMS 智能管理与温控体系共同降低系统量产成本。\"}]}','2026-06-29 16:02:41.387304'),(21,'2026-06-11 02:04:00.303195','contentModules','productsSeries','{\"enabled\":true,\"subtitle\":\"产品系列\",\"title\":\"覆盖高压车规级与低压多规格应用\",\"items\":[{\"title\":\"高压车规级 10C 系统\",\"text\":\"适配乘用车高压平台，支撑极速快充与大功率瞬时放电。\",\"tag\":\"高压平台\"},{\"title\":\"低压 2C-10C 系列 Pack\",\"text\":\"覆盖 48V、60V、72V 等多规格布局，灵活匹配多类设备。\",\"tag\":\"多规格\"},{\"title\":\"移动储充与调频储能\",\"text\":\"兼具储能、充电和调度能力，面向分布式能源与应急保障场景。\",\"tag\":\"充储一体\"},{\"title\":\"特种装备动力系统\",\"text\":\"面向警用巡逻车、无人机、机器狗等装备升级，提升出勤与补能效率。\",\"tag\":\"场景验证\"}]}','2026-06-29 16:02:41.383583'),(22,'2026-06-11 02:04:00.311111','contentModules','productsScenarios','{\"enabled\":true,\"subtitle\":\"应用场景\",\"title\":\"高倍率快充与移动电力的多场景落地\",\"items\":[{\"title\":\"低空经济\",\"text\":\"面向 eVTOL、无人机物流与应急救援等场景，满足长续航、极端温度耐受与超充放需求。\",\"image\":\"/assets/electric-motor-pxwlTZMK.jpg\"},{\"title\":\"特种设备与特种车辆\",\"text\":\"适配警用巡逻车、工程机械、矿用设备、机场和港口设备等高频作业场景。\",\"image\":\"/assets/patrol-car-10I1VfTX.jpg\"},{\"title\":\"机器人与机器狗\",\"text\":\"满足机器人设备对高能量密度、高倍率充放电、过充过放与过热保护的综合要求。\",\"image\":\"/assets/robot-dog-Dk4S8Lce.jpg\"},{\"title\":\"移动储能与商用车辆\",\"text\":\"支持户外储能、电动摩托、低速商用车辆等场景，提升补能便利性与运营效率。\",\"image\":\"/assets/outdoor-power-BdHwpPzA.jpg\"}]}','2026-06-29 16:02:41.378429'),(23,'2026-06-24 11:23:22.797198','footer','service1','数字化规划','2026-06-24 11:25:40.521776'),(24,'2026-06-24 11:23:22.846561','footer','service2','系统建设','2026-06-24 11:25:40.527333'),(25,'2026-06-24 11:23:22.853911','footer','service3','数据运营','2026-06-24 11:25:40.532160'),(26,'2026-06-24 11:23:22.860516','footer','service4','   ','2026-06-24 11:25:40.535845'),(27,'2026-06-24 11:23:22.868537','footer','service5','  ','2026-06-24 11:25:40.540178'),(28,'2026-06-24 11:23:22.874862','footer','copyright','© 2024 四川国驰恒创科技有限公司. 保留所有权利.','2026-06-24 11:25:40.545040'),(29,'2026-06-24 11:23:22.879395','footer','privacyText','隐私政策','2026-06-24 11:25:40.549217'),(30,'2026-06-24 11:23:22.883993','footer','sitemapText','网站地图','2026-06-24 11:25:40.553440'),(31,'2026-06-24 11:23:22.888880','footer','termsText','使用条款','2026-06-24 11:25:40.557805'),(32,'2026-06-24 11:23:22.894224','footer','brandDescription','四川国驰恒创科技有限公司专注新能源电池快充技术解决方案，为动力电池、储能电池及多场景移动电力提供从研发验证到产业化导入的一体化能力。','2026-06-24 11:25:40.563367'),(33,'2026-06-25 14:58:33.829392','features','sectionSubtitle','为什么选择我们？','2026-06-25 16:26:57.765178'),(34,'2026-06-25 14:58:33.874801','features','sectionTitle','核心优势','2026-06-25 16:26:57.769462'),(35,'2026-06-25 17:15:55.436933','homeAboutIntro','subtitle','国驰恒创','2026-06-25 17:24:16.903700'),(36,'2026-06-25 17:15:55.464263','homeAboutIntro','buttonText','了解更多','2026-06-25 17:24:16.919245'),(37,'2026-06-25 17:15:55.471439','homeAboutIntro','buttonLink','/about','2026-06-25 17:24:16.925127'),(38,'2026-06-25 17:15:55.479773','homeAboutIntro','imageUrl','http://localhost:8080/uploads/91b18663-3039-4528-89de-97d314c3a6c9.png','2026-06-25 17:24:16.932213'),(39,'2026-06-26 08:50:08.164776','homeNews','sectionSubtitle','最新动态','2026-06-26 10:01:11.187453'),(40,'2026-06-26 08:50:08.242551','homeNews','sectionTitle','公司新闻','2026-06-26 10:01:11.199035'),(41,'2026-06-26 08:50:08.248610','homeNews','buttonText','查看全部文章','2026-06-26 10:01:11.204235'),(42,'2026-06-26 08:50:08.253060','homeNews','buttonLink','/news','2026-06-26 10:01:11.208062'),(51,'2026-06-26 11:38:20.790561','homeHero','companyName','????????????','2026-06-29 17:20:43.529028'),(52,'2026-06-26 11:38:20.816650','homeHero','companyDesc','??????????????????,???????????????????','2026-06-29 17:20:43.534093'),(53,'2026-06-26 14:56:55.117133','about','image','http://localhost:8080/uploads/b98a82c2-5949-495c-b570-d1146acc0c16.png','2026-06-26 15:11:37.717957'),(54,'2026-06-26 15:46:54.233426','milestones','milestone1Year','','2026-06-26 16:21:42.532408'),(55,'2026-06-26 15:46:54.262598','milestones','milestone1Desc','','2026-06-26 16:21:42.536627'),(56,'2026-06-26 15:46:54.268824','milestones','milestone2Year','','2026-06-26 16:21:42.539981'),(57,'2026-06-26 15:46:54.272708','milestones','milestone2Desc','','2026-06-26 16:21:42.543381'),(58,'2026-06-26 15:46:54.277867','milestones','milestone3Year','','2026-06-26 16:21:42.547025'),(59,'2026-06-26 15:46:54.282116','milestones','milestone3Desc','','2026-06-26 16:21:42.550892'),(60,'2026-06-26 15:46:54.286706','milestones','milestone4Year','','2026-06-26 16:21:42.553938'),(61,'2026-06-26 15:46:54.290623','milestones','milestone4Desc','','2026-06-26 16:21:42.556779'),(62,'2026-06-26 16:16:00.344058','values','value1Title','','2026-06-26 16:27:15.814941'),(63,'2026-06-26 16:16:00.349506','values','value1Icon','','2026-06-26 16:27:15.819041'),(64,'2026-06-26 16:16:00.354387','values','value1Desc','','2026-06-26 16:27:15.822779'),(65,'2026-06-26 16:16:00.358634','values','value2Title','','2026-06-26 16:27:15.826994'),(66,'2026-06-26 16:16:00.362915','values','value2Icon','','2026-06-26 16:27:15.830404'),(67,'2026-06-26 16:16:00.369513','values','value2Desc','','2026-06-26 16:27:15.833869'),(68,'2026-06-26 16:16:00.374139','values','value3Title','','2026-06-26 16:27:15.837269'),(69,'2026-06-26 16:16:00.378171','values','value3Icon','','2026-06-26 16:27:15.841266'),(70,'2026-06-26 16:16:00.382326','values','value3Desc','','2026-06-26 16:27:15.844738'),(71,'2026-06-26 16:16:00.388682','values','value4Title','','2026-06-26 16:27:15.847957'),(72,'2026-06-26 16:16:00.393182','values','value4Icon','','2026-06-26 16:27:15.851884'),(73,'2026-06-26 16:16:00.398062','values','value4Desc','','2026-06-26 16:27:15.855662'),(74,'2026-06-26 17:33:56.992437','homeCta','eyebrow','立即行动','2026-06-26 17:33:56.992437'),(75,'2026-06-26 17:33:57.005215','homeCta','title','准备好开启新能源技术合作了吗？','2026-06-26 17:33:57.005215'),(76,'2026-06-26 17:33:57.008133','homeCta','description','无论是电池快充技术、储能系统还是智能装备，我们的专家团队为您提供从研发到量产的一站式解决方案','2026-06-26 17:33:57.008133'),(77,'2026-06-26 17:33:57.012006','homeCta','button1Text','立即咨询','2026-06-26 17:33:57.012006'),(78,'2026-06-26 17:33:57.015476','homeCta','button1Link','/contact','2026-06-26 17:33:57.015476'),(79,'2026-06-26 17:33:57.019326','homeCta','button2Text','了解产品','2026-06-26 17:33:57.019326'),(80,'2026-06-26 17:33:57.022202','homeCta','button2Link','/products','2026-06-26 17:33:57.022202'),(81,'2026-06-29 15:38:15.700645','productHero','heroTitle','产品','2026-06-29 15:38:15.700645'),(82,'2026-06-29 15:38:15.709237','productHero','heroSubtitle','产品服务\n','2026-06-29 15:38:15.709237'),(84,'2026-06-29 17:11:07.531921','aboutHero','companyName','四川国驰恒创科技有限公司','2026-06-29 17:11:07.531921'),(85,'2026-06-29 17:11:07.544463','aboutHero','companyDesc','核心技术源自美国辛辛那提大学博士团队，专注于提供新能源电池快充技术解决方案。','2026-06-29 17:11:07.544463'),(86,'2026-06-29 17:11:07.549605','aboutHero','backgroundImage','/uploads/3a53a2d5-40e1-45db-9b93-fee514f5940e.png','2026-06-29 17:11:07.549605');
/*!40000 ALTER TABLE `site_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_members`
--

DROP TABLE IF EXISTS `team_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_members` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bio` text COLLATE utf8mb4_unicode_ci,
  `created_at` datetime(6) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `position` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sort_order` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_members`
--

LOCK TABLES `team_members` WRITE;
/*!40000 ALTER TABLE `team_members` DISABLE KEYS */;
INSERT INTO `team_members` VALUES (3,'/uploads/e39ae3ef-9aeb-4201-913f-eb550fd959d9.png','美国辛辛那提大学人工智能专业博士，国内新能源领域跨界技术权威专家，长期聚焦新能源智能装备、数字能源、智能智造融合方向核心技术攻关。凭借前瞻性科研成果与行业突出贡献，多次受邀出席香山科学会议，参与国家前沿科技顶层议题研讨与战略技术论证，跻身国内新能源高端科研圈层。\n','2026-06-26 16:14:54.986248',_binary '','朱小宝','团队带头人',0,'2026-06-26 17:15:09.031045'),(4,'/uploads/6a749655-69fa-41f5-a23d-f0662f4dab3a.png','美国辛辛那提大学机械工程专业博士，主持参与国家科学基金、MGGFB(DOD)、州政府研究项目多项，长期为机械制造类顶级期刊审稿；主持美国机械工程学会、增材制造分会并受邀做学术报告十余次。\n','2026-06-26 16:17:31.864305',_binary '','王亚超','技术总监',1,'2026-06-26 16:17:37.157086'),(5,'/uploads/53bf5949-65e9-4157-bc47-34db459bcfc4.png','计算机专业背景，深耕技术研发与生产管理领域多年，具备项目全周期统筹经验。主导技术研发攻坚、生产全流程优化与产线工艺自动化升级，搭建完善全链条质量管理体系，高效推进技术成果落地转化，兼具硬核技术功底与综合生产运营管理的复合型能力。\n','2026-06-26 16:18:08.286159',_binary '','佟辉','项目总监',2,'2026-06-26 16:18:08.286159'),(6,'/uploads/c7078520-8906-4e4a-9fad-eb53bb796f36.png','美国辛辛那提大学机械工程专业博士，美国 NSF 智能维护系统中心 (IMS Center) /工业人工智能中心 (IAI Center) 研究员，发表论文30余篇。头部企业授予企业顾问、高级数据科学家、讲师、项目合作伙伴等多个头衔。\n','2026-06-26 16:18:47.524108',_binary '','冯建设','产品总监',3,'2026-06-26 16:18:47.524108'),(7,'/uploads/4d8f96b4-e7f1-4934-8750-8f8fd623f87f.png','清华大学博士毕业，美国辛辛那提大学博士后，博士生导师，主要从事纳米二维材料改性、3D打印等方面的应用基础研究工作。在项目中负责3D打印材料和电池材料研究。\n','2026-06-26 16:19:21.385436',_binary '','吴鑫','化学专家',4,'2026-06-26 16:19:21.385436'),(8,'/uploads/5cdc6940-e863-452f-8e1d-ecca7200bc19.png','具备15年+制造企业总经理管理经验，具有23年粉末冶金制造企业管理经验。精通粉末冶金设计、生产和销售，熟练运用IATF16949质量管理体系；为多个企业培养了多层次实用型技术人才。\n','2026-06-26 16:19:57.040613',_binary '','付玉军','生产总监',5,'2026-06-26 16:19:57.040613'),(9,'/uploads/a3f6934a-c5f3-470b-8f6e-20591b7a751e.png','深耕新能源、人工智能及化工领域30余年，具备市场实战与创业操盘经验。凭借对产业的敏锐洞察，精准锁定销售突破口，助力业务快速破局并实现增长。负责销售工作，涵盖渠道管理与维护，成功开拓行业标杆客户。\n\n','2026-06-26 16:20:32.931072',_binary '','魏晚庚','销售总监',6,'2026-06-26 16:20:32.931072');
/*!40000 ALTER TABLE `team_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `enabled` bit(1) NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2026-06-09 06:45:38.077420','admin@gchc.com',_binary '','$2a$10$l81u4vsXUl/QnJSbqzkcVuYO19Y/xZ1E6PCQowtnhezy2KKM5suZa','13800138000','ADMIN','2026-06-09 06:45:38.077420','admin'),(2,'2026-06-09 06:45:38.200726','user@gchc.com',_binary '','$2a$10$OF5iD8uTts4E0LINvlJ.iers1ZyWkfLkXl9NqcjPQmXvRXl97wIOu','13900139000','USER','2026-06-09 06:45:38.200726','user');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'gchc_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-30 11:38:24
