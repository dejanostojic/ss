(ns sitnoseckana.views.templates.defaultsite.platform.home.handler
  (:require [sitnoseckana.models.shop-menu :as shop-menu]
            [sitnoseckana.models.shop-product-type-variation]
            [sitnoseckana.models.base-daf]))

(defn handle-index [ctx]
  (println "HANDLE INDEX!!!!!!!!!!")
  (assoc ctx :page-attrs {:h1          "home page"
                          :author-role "admin"
                          :menu-items    (let [variations ((sitnoseckana.models.shop-product-type-variation/daf :load-list))
                                               monday (java.sql.Date. (-> (.getTime (doto (java.util.Calendar/getInstance) (.set java.util.Calendar/DAY_OF_WEEK java.util.Calendar/MONDAY))) (.getTime)))
                                             friday (java.sql.Date. (-> (.getTime (doto (java.util.Calendar/getInstance) (.set java.util.Calendar/DAY_OF_WEEK java.util.Calendar/FRIDAY))) (.getTime)))]
                                         (map (fn [prod] (assoc-in prod [:shop_product :shop_product_type :variations] (filter #(if (= (:type_id %) (get-in prod [:shop_product :shop_product_type :id])) true) variations)) )
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
                                                                                          :where (str "date>={d '" monday "'} and date<={d '" friday "'}") :ord "date asc")) )
                          :juices (sitnoseckana.models.base-daf/with-join sitnoseckana.models.shop-product/dao-settings
                                                                           :join [{
                                                                                   :from sitnoseckana.models.shop-product-type/dao-settings
                                                                                   :fs ["name" "id"]
                                                                                   :on ["product_type_id" "id"]
                                                                                   }]
                                                                           :where "product_type_id in (16,17)" :ord "product_type_id asc")
                          :variations (sitnoseckana.models.shop-product-type-variation/daf :load-list)
                                       }))

