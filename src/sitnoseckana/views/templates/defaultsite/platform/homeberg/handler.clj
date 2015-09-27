(ns sitnoseckana.views.templates.defaultsite.platform.homeberg.handler
  (:require [sitnoseckana.models.shop-menu :as shop-menu]))

(defn handle-index [ctx]
  (println "HANDLE INDEX!!!!!!!!!!")
  (assoc ctx :page-attrs {:h1          "home page"
                          :author-role "admin"
                          :menu-items    (let [monday (java.sql.Date. (-> (.getTime (doto (java.util.Calendar/getInstance) (.set java.util.Calendar/DAY_OF_WEEK java.util.Calendar/MONDAY))) (.getTime)))
                                             friday (java.sql.Date. (-> (.getTime (doto (java.util.Calendar/getInstance) (.set java.util.Calendar/DAY_OF_WEEK java.util.Calendar/FRIDAY))) (.getTime)))]
                                         (sitnoseckana.models.base-daf/with-join shop-menu/dao-settings
                                                                                 :join [{
                                                                                         :from sitnoseckana.models.shop-product/dao-settings
                                                                                         :fs ["name" "id" "ingredients"]
                                                                                         :on ["product_id" "id"]
                                                                                         :join {
                                                                                                :from sitnoseckana.models.shop-product-type/dao-settings
                                                                                                :fs ["name" "id"]
                                                                                                :on ["product_type_id" "id"]
                                                                                                }
                                                                                         }]
                                                                                 :where (str "date>={d '" monday "'} and date<={d '" friday "'}") :ord "date asc"))
                          :juices (sitnoseckana.models.base-daf/with-join sitnoseckana.models.shop-product/dao-settings
                                                                           :join [{
                                                                                   :from sitnoseckana.models.shop-product-type/dao-settings
                                                                                   :fs ["name" "id"]
                                                                                   :on ["product_type_id" "id"]
                                                                                   }]
                                                                           :where "product_type_id in (16,17)" :ord "product_type_id asc")
                                       }))

