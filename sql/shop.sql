create table `shop_variation` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `shop_product_type` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(100) DEFAULT NULL,
    `weekly` tinyint(1) unsigned,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table `shop_product_type_variation` (
    `type_id` bigint(20) unsigned NOT NULL,
    `variation_id` bigint(20) unsigned NOT NULL,
    `price` decimal(20,2),
    PRIMARY KEY (`type_id`, `variation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `shop_product` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL,
    `ingredients` varchar(512) DEFAULT NULL,
    `product_type_id` bigint(20) unsigned NOT NULL,
    PRIMARY KEY (`id`),
    KEY `product_type_id` (`product_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `shop_product_variation` (
    `product_id` bigint(20) unsigned NOT NULL,
    `variation_id` bigint(20) unsigned NOT NULL ,
    PRIMARY KEY (`product_id`, `variation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `shop_menu` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `product_id` bigint(20) unsigned NOT NULL,
    `date` DATE NOT NULL,
    PRIMARY KEY (`id`),
    KEY `shop_menu_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;