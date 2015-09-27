create table shop_order (
    id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    user_id bigint(20) unsigned NOT NULL,
    date DATE not null,
    first_name varchar(255),
    last_name varchar(255),
    email varchar(255),
    phone varchar(50),
    shipping_address varchar(255),
    extra_info text,
    total_price decimal(20,2)
    PRIMARY KEY (`id`),
    KEY `shop_order_user_id` (`user_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table shop_order_item (
    id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    order_id bigint(20) unsigned NOT NULL,
    product_id bigint(20) unsigned NOT NULL,
    variation_id bigint(20) unsigned NOT NULL,
    price decimal(20,2),
    discount decimal(20,2),
    quantity int(11),
    date DATE NOT NULL,
    PRIMARY KEY (`id`),
    KEY `shop_order_item_order_id` (`order_id`),
    KEY `shop_order_item_product_id` (`product_id`),
    KEY `shop_order_item_variation_id` (`variation_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;