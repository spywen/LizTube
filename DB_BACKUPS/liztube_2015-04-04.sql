# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Hôte: 127.0.0.1 (MySQL 5.6.21)
# Base de données: liztube
# Temps de génération: 2015-04-04 17:20:10 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Affichage de la table role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;

INSERT INTO `role` (`id`, `name`)
VALUES
	(1,'AUTHENTICATED'),
	(2,'ADMIN'),
	(3,'USER');

/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;


# Affichage de la table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `birthdate` datetime NOT NULL,
  `email` varchar(100) NOT NULL,
  `firstname` varchar(100) NOT NULL,
  `isactive` bit(1) NOT NULL,
  `isfemale` bit(1) NOT NULL,
  `lastname` varchar(100) NOT NULL,
  `modificationdate` datetime DEFAULT NULL,
  `password` varchar(200) NOT NULL,
  `pseudo` varchar(50) NOT NULL,
  `registerdate` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `birthdate`, `email`, `firstname`, `isactive`, `isfemale`, `lastname`, `modificationdate`, `password`, `pseudo`, `registerdate`)
VALUES
	(1,'2013-10-05 10:15:26','spywen@hotmail.fr','Laurent',00000001,00000000,'Babin','2013-10-05 10:15:26','e73b79a0b10f8cdb6ac7dbe4c0a5e25776e1148784b86cf98f7d6719d472af69','spywen','2013-10-05 10:15:26'),
	(2,'2013-10-05 10:15:26','kmille@hotmail.fr','Camille',00000001,00000001,'Fontaine','2013-10-05 10:15:26','e73b79a0b10f8cdb6ac7dbe4c0a5e25776e1148784b86cf98f7d6719d472af69','kmille','2013-10-05 10:15:26');

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Affichage de la table user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK_it77eq964jhfqtu54081ebtio` (`role_id`),
  CONSTRAINT `FK_apcc8lxk2xnug8377fatvbn04` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_it77eq964jhfqtu54081ebtio` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;

INSERT INTO `user_role` (`user_id`, `role_id`)
VALUES
	(1,1),
	(1,2);

/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;


# Affichage de la table video
# ------------------------------------------------------------

DROP TABLE IF EXISTS `video`;

CREATE TABLE `video` (
  `keyid` varchar(255) NOT NULL,
  `description` longtext,
  `ispublic` bit(1) NOT NULL,
  `ispubliclink` bit(1) NOT NULL,
  `title` longtext NOT NULL,
  `user` bigint(20) NOT NULL,
  `creationdate` datetime NOT NULL,
  `videorankaslatest` bigint(20) NOT NULL,
  `videorankasmostviewed` bigint(20) NOT NULL,
  PRIMARY KEY (`keyid`),
  KEY `FK_3ecjgxlg1psty2h4bknwt59rf` (`user`),
  CONSTRAINT `FK_3ecjgxlg1psty2h4bknwt59rf` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;

INSERT INTO `video` (`keyid`, `description`, `ispublic`, `ispubliclink`, `title`, `user`, `creationdate`, `videorankaslatest`, `videorankasmostviewed`)
VALUES
	('3ef39e77-8c2c-4afb-8ad7-d96958536c0f','description',00000000,00000000,'title',1,'2015-04-04 18:45:33',0,0),
	('dbc3f06e-1fa2-4d00-ad6a-6cf2b50e3581','description',00000000,00000000,'title',1,'2015-04-04 18:45:45',0,0);

/*!40000 ALTER TABLE `video` ENABLE KEYS */;
UNLOCK TABLES;


# Affichage de la table view
# ------------------------------------------------------------

DROP TABLE IF EXISTS `view`;

CREATE TABLE `view` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `viewdate` datetime NOT NULL,
  `user` bigint(20) NOT NULL,
  `video` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9oc5iu4cho9dkq158wb37bb17` (`user`),
  KEY `FK_fg5depbv6kb3sqysvqeo1gcrw` (`video`),
  CONSTRAINT `FK_9oc5iu4cho9dkq158wb37bb17` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_fg5depbv6kb3sqysvqeo1gcrw` FOREIGN KEY (`video`) REFERENCES `video` (`keyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
