(ns sitnoseckana.views.templates.defaultsite.shop.products.handler
  (:require [sitnoseckana.models.shop-menu :as shop-menu]
            [sitnoseckana.models.shop-product-type-variation :as shop-product-type-variation]
            [sitnoseckana.models.shop-variation :as shop-variation]
            [sitnoseckana.util.date]
            [sitnoseckana.routes.order]
            [sitnoseckana.routes.page-url]
            [sitnoseckana.models.config])
  (:import (java.time LocalDate DayOfWeek)
           (java.util Locale)
           (java.time.format DateTimeFormatter)))


(defn handle-index [ctx]
  (assoc ctx :page-attrs {:order-item-link (str            (sitnoseckana.routes.page-url/order-page-url) ".html"
                                             )
                          :menu-items      (let [vars-with-name (sitnoseckana.models.base-daf/with-join shop-product-type-variation/dao-settings
                                                                                                        :join [{
                                                                                                                :from shop-variation/dao-settings
                                                                                                                :fs   ["id" "name"]
                                                                                                                :on   ["variation_id" "id"]}])
                                                 date (sitnoseckana.models.config/date-from-week-number)

                                                 monday (java.sql.Date/valueOf (sitnoseckana.util.date/get-day date DayOfWeek/MONDAY))
                                                 friday (java.sql.Date/valueOf (sitnoseckana.util.date/get-day date DayOfWeek/FRIDAY))]

                                             (map (fn [prod] (assoc-in prod [:shop_product :shop_product_type :variations] (filter #(if (= (:type_id %) (get-in prod [:shop_product :shop_product_type :id])) true) vars-with-name)))
                                                  (map (fn [menu] (assoc menu :time-in-milis (.getTime (:date menu)))) (sitnoseckana.models.base-daf/with-join shop-menu/dao-settings
                                                                                                                                                               :join [{
                                                                                                                                                                       :from sitnoseckana.models.shop-product/dao-settings
                                                                                                                                                                       :fs   ["name" "id" "ingredients"]
                                                                                                                                                                       :on   ["product_id" "id"]
                                                                                                                                                                       :join {
                                                                                                                                                                              :from sitnoseckana.models.shop-product-type/dao-settings
                                                                                                                                                                              :fs   ["name" "id"]
                                                                                                                                                                              :on   ["product_type_id" "id"]
                                                                                                                                                                              }
                                                                                                                                                                       }]
                                                                                                                                                               :where (str "date>={d '" monday "'} and date<={d '" friday "'}") :ord "date asc, product_type_id asc"))))}
             :juices (sitnoseckana.models.base-daf/with-join sitnoseckana.models.shop-product/dao-settings
                                                             :join [{
                                                                     :from sitnoseckana.models.shop-product-type/dao-settings
                                                                     :fs ["name" "id"]
                                                                     :on ["product_type_id" "id"]
                                                                     }]
                                                             :where "product_type_id=16" :ord "product_type_id asc")
             :shakes (sitnoseckana.models.base-daf/with-join sitnoseckana.models.shop-product/dao-settings
                                                             :join [{
                                                                     :from sitnoseckana.models.shop-product-type/dao-settings
                                                                     :fs ["name" "id"]
                                                                     :on ["product_type_id" "id"]
                                                                     }]
                                                             :where "product_type_id=17" :ord "product_type_id asc")
             :days-till-friday  (let [v (sitnoseckana.util.date/get-days (LocalDate/now) (sitnoseckana.util.date/get-day (LocalDate/now) DayOfWeek/FRIDAY))]
                                  (map (fn [^LocalDate day] {:val (.format day (DateTimeFormatter/ofPattern "yyyy-MM-dd" (Locale/forLanguageTag "sr-Latn-RS"))) :label (.format day (DateTimeFormatter/ofPattern "EEEE" (Locale/forLanguageTag "sr-Latn-RS")))}) v))
             :current-date  (.format (LocalDate/now) (DateTimeFormatter/ofPattern "yyyy-MM-dd" (Locale/forLanguageTag "sr-Latn-RS")))
             ))

(defn handle-view [ctx]
  (assoc ctx :author {:author "Pera Peric" :author-role "admin"}
             ))
