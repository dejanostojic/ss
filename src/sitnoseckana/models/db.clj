(ns sitnoseckana.models.db
  (:require [clojure.java.jdbc :as sql])
  (:import java.sql.DriverManager))

; TODO INSERT THREAD POOLING!!!

(def db 
  {:classname "com.mysql.jdbc.Driver"
   :subprotocol "mysql"
   :subname "//localhost/sitnoseckana"
   :user "root"
   :password "dostojic123"
   :host "//localhost"
   :db-name "sitnoseckana"
   :autoReconnect true
   :port 3306})

(defmacro with-db [f & body]
  `(sql/with-connection ~db (~f ~@body)))

(. Class (forName (db :classname)))
(def ^:dynamic conn (DriverManager/getConnection (str "jdbc:" (db :subprotocol) ":" (db :host) ":" (db :port) "/" (db :db-name) "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8") (db :user) (db :password)))

