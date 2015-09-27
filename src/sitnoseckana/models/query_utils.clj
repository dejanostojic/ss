(ns sitnoseckana.models.query-utils
  (:import (java.sql Connection PreparedStatement ResultSet SQLException Statement))
  (:require [sitnoseckana.models.db :as db]))

(defn exec [sql]
  (let [^Statement st (.createStatement db/conn)]
    (try 
      (.executeUpdate st sql)
      (finally (.close st)))))

(defn for-first [sql closure]
  (let [^Statement st (.createStatement db/conn)
        ^ResultSet rs (.executeQuery st sql)]
    (try 
      (if (.next rs)
        (closure rs))
      (finally 
        (if (not (nil? rs))
          (.close rs))
        (.close st)))))

(defn for-each [sql closure]
  (let [^Statement st (.createStatement db/conn)
        ^ResultSet rs (.executeQuery st sql)]
    (try 
      (loop [r-vec []]
        (if (.next rs)
          (recur (conj r-vec (closure rs)))
          r-vec))
      (finally 
        (if (not (nil? rs))
          (.close rs))
        (.close st)))))


(defn mysql-str [val]
  (str "'" val "'"))

(defn like-literal [val]
  (if (nil? val )
    "NULL"
     (mysql-str (str "%" val "%"))))