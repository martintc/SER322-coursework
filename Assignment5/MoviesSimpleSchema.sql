CREATE DATABASE IF NOT EXISTS `movies_simple`;
USE `movies_simple`;

CREATE TABLE `language` (
  `language_id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `name` char(20) NOT NULL,
  PRIMARY KEY (`language_id`)
);

CREATE TABLE `actor` (
  `actor_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  PRIMARY KEY (`actor_id`)
);

CREATE TABLE `film` (
  `film_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` text,
  `release_year` year(4) DEFAULT NULL,
  `language_id` tinyint(3) unsigned NOT NULL,
  `length` smallint(5) unsigned DEFAULT NULL,
  PRIMARY KEY (`film_id`),
  CONSTRAINT `fk_film_language` FOREIGN KEY (`language_id`) REFERENCES `language` (`language_id`) ON UPDATE CASCADE);
  
CREATE TABLE `film_actor` (
  `actor_id` smallint(5) unsigned NOT NULL,
  `film_id` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`actor_id`,`film_id`),
  CONSTRAINT `fk_film_actor_actor` FOREIGN KEY (`actor_id`) REFERENCES `actor` (`actor_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_film_actor_film` FOREIGN KEY (`film_id`) REFERENCES `film` (`film_id`) ON UPDATE CASCADE
);


INSERT INTO `language` VALUES 
(1,'English'),
(2,'Spanish'),
(3,'Japanese');

INSERT INTO `actor` VALUES 
(1,'PENELOPE','GUINESS'),
(2,'NICK','WAHLBERG'),
(3,'ED','CHASE'),
(4,'JENNIFER','DAVIS'),
(5,'JOHNNY','LOLLOBRIGIDA'),
(6,'BETTE','NICHOLSON'),
(7,'GRACE','MOSTEL'),
(8,'MATTHEW','JOHANSSON'),
(9,'JOE','SWANK'),
(10,'CHRISTIAN','GABLE');


INSERT INTO `film` VALUES 
(1,'ACADEMY DINOSAUR','A Epic Drama of a Feminist And a Mad Scientist',2006,1,86),
(2,'ACE GOLDFINGER','A Astounding Epistle of a Database Administrator And a Explorer',2006,2,48),
(3,'ADAPTATION HOLES','A Astounding Reflection of a Lumberjack And a Car',2006,1,50),
(4,'AFFAIR PREJUDICE','A Fanciful Documentary of a Frisbee And a Lumberjack',2006,1,117),
(5,'AFRICAN EGG','A Fast-Paced Documentary of a Pastry Chef And a Dentist',2006,2,130),
(6,'AGENT TRUMAN','A Intrepid Panorama of a Robot And a Boy',2006,1,169),
(7,'AIRPLANE SIERRA','A Touching Saga of a Hunter And a Butler',2006,3,62),
(8,'AIRPORT POLLOCK','A Epic Tale of a Moose And a Girl',2006,1,54),
(9,'ALABAMA DEVIL','A Thoughtful Panorama of a Database Administrator And a Mad Scientist',2006,3,114),
(10,'ALADDIN CALENDAR','A Action-Packed Tale of a Man And a Lumberjack',2006,2,63);

INSERT INTO `film_actor` VALUES 
(1,1),
(1,3),
(1,4),
(1,6),
(2,1),
(2,2),
(2,8),
(3,4),
(3,6),
(3,7),
(4,6),
(5,1),
(5,3),
(5,4),
(5,9),
(5,10),
(6,7),
(6,8),
(6,9),
(7,7),
(7,4),
(7,1),
(8,3),
(8,8),
(8,9),
(9,1),
(9,2),
(9,3),
(9,4),
(9,6),
(9,7),
(9,8),
(9,9),
(9,10),
(10,1),
(10,4),
(10,10);



