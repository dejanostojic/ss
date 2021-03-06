# sitnoseckana

Web application for food catering implemented in Clojure

Live preview: http://clj-dejanostojic.rhcloud.com/ 
(*note* sending emails won't work in live preview because of gmail asuming my account is stolen, server is located in USA and i created it from Belgrade)

## Prerequisites

You will need [Leiningen][1] 2.3.4 or above installed.

Java 1.8.0

Mysql 5.5.45 or MariaDB 10.0.21

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run following commands:

### init database
sql dump file is located in sql/sitnoseckana.sql

db settings for application are in resources/properties/platform.properties

You can change .properties file or import db to localhost with same parameters

1) log in to mysql and create schema

- CREATE DATABASE `sitnoseckana` /*!40100 DEFAULT CHARACTER SET utf8 */

2) go to root of web application and import db

- mysql -u MYSQL-USER -p sitnoseckana < sql/sitnoseckana.sql

3) login to mysql and create user and privileges

- CREATE USER 'sitnoseckana'@'localhost' IDENTIFIED BY 'sitnoseckana123';

- GRANT ALL PRIVILEGES ON sitnoseckana.* TO 'sitnoseckana'@'localhost';

- FLUSH PRIVILEGES;

### change admin email
    in resources/properties/platform.properties key is admin_email


After db created you can start server with from root of application

### start aplication
lein ring server

## Testing
Admin panel url is /admin

- Admin user:
  - username: dostadmin
  - password: a

- Member user:
  - username: member1@sharklasers.com
  - password: member1

### Privileges

* Anonymous access actions
  * Can view meals and make orders
  
* Member access actions
  * register after purchase
  * speed up orderin process with auto filling form from his account
  * review list of his orders and details for each one.

* Admin access actions
  * Access admin panel
  * Add/delete pages (can choose templates and chagne content)
  * View all products, create new or change or delete existing
  * View all product types, create new or change or delete existing, define price for existing product types
  * Add products to weekly menu, and change active week (usualy on saturday when menu for next week is completed)
  * Mannage users
  * View orders (making new orders is done in front end. It is enough to fill existing user email and order will be grouped in his account)
  * View dashboard at the moment just income per product groups

## License

Copyright © 2014 FIXME
