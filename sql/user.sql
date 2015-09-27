CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(150) DEFAULT NULL,
  `password` varchar(512) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `first_name` varchar(255),
  `last_name` varchar(255),
  `phone` varchar(50),
  `shipping_address` varchar(255),
  `activated` tinyint(1) DEFAULT 0,
  `activation_key` varchar(255) DEFAULT '',
  `registration_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8

CREATE TABLE `user_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned DEFAULT NULL,
  `role_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=453 DEFAULT CHARSET=utf8

CREATE TABLE user_profile (
  id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  user_id bigint(20) unsigned,
  first_name varchar(80) DEFAULT NULL,
  last_name varchar(80) DEFAULT NULL,
  nickname varchar(30) DEFAULT NULL,
  email varchar(150) DEFAULT NULL,
  user_name varchar(150) DEFAULT NULL,
  kind varchar(45) DEFAULT NULL,
  registration_date date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `up_user_name` (`user_name`),
  KEY `up_user_id` (`user_id`)

);