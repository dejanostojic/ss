(ns sitnoseckana.models.db
  (:require [sitnoseckana.app.properties :as props])
  (:import java.sql.DriverManager))

; TODO INSERT THREAD POOLING!!!

(def db 
  {:classname "com.mysql.jdbc.Driver"
   :subprotocol "mysql"
   :subname (str "//" (props/get-key "db_name") (props/get-key "db_user"))
   :user (props/get-key "db_user")
   :password (props/get-key "db_pass")
   :host (props/get-key "db_host")
   :db-name (props/get-key "db_name")
   :autoReconnect true
   :port (props/get-key "db_port")})

(. Class (forName (db :classname)))
(def ^:dynamic conn (DriverManager/getConnection (str "jdbc:" (db :subprotocol) "://" (db :host) ":" (db :port) "/" (db :db-name) "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8") (db :user) (db :password)))

