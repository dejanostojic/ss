(ns sitnoseckana.models.db
  (:require [sitnoseckana.app.properties :as props])
  (:import java.sql.DriverManager))

; TODO INSERT THREAD POOLING!!!

(def db
  {:classname "com.mysql.jdbc.Driver"
   :subprotocol "mysql"
   :subname (str "//" (props/get-key "db_name") (props/get-key "db_user"))
   :user (get (System/getenv) "OPENSHIFT_MYSQL_DB_USERNAME" (props/get-key "db_user"))
   :password (get (System/getenv) "OPENSHIFT_MYSQL_DB_PASSWORD" (props/get-key "db_pass"))
   :host  (get (System/getenv) "OPENSHIFT_MYSQL_DB_HOST" (props/get-key "db_host"))
   :db-name (props/get-key "db_name")
   :autoReconnect true
   :port (get (System/getenv) "OPENSHIFT_MYSQL_DB_PORT" (props/get-key "db_port"))})

(. Class (forName (db :classname)))
(def ^:dynamic conn (DriverManager/getConnection (str "jdbc:" (db :subprotocol) "://" (db :host) ":" (db :port) "/" (db :db-name) "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8") (db :user) (db :password)))
