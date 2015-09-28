(ns sitnoseckana.routes.order
  (:require [compojure.core :refer [defroutes GET POST ANY DELETE]]
            [cheshire.core]
            [compojure.response]
            [sitnoseckana.models.user :as user]
            [sitnoseckana.models.shop-order :as order]
            [sitnoseckana.models.query-utils :as query-utils]
            [sitnoseckana.util.date]
            [sitnoseckana.util.email]
            [postal.core]
            [sitnoseckana.models.page :as page-daf]
            [sitnoseckana.app.properties :as props]))

(defn calc-total-price
  ([product-list] (calc-total-price product-list 0))
  ([product-list init]
   (if (empty? product-list)
     init
     (recur (rest product-list) (+ init (let [val (first product-list)]  (* (val :quantity) (get-in val [:variation :price] (get-in val [:product :price] 0))) )  )))) )




(defroutes order-routes
           (POST ["/dost/shop/order"] []
             (fn [req]
               (let [form-params (get-in req [:form-params])
                     session (:session req)
                     cart (get-in req [:session :cart])
                     first-name (form-params "first_name")
                     last-name (form-params "last_name")
                     email (form-params "email")
                     phone (form-params "phone")
                     shipping-address (form-params "shipping_address")
                     extra-info (form-params "extra_info")
                     user (or (first ((user/daf :load-list) :where (str "email=" (query-utils/mysql-str email) )  :limit 1))
                              ((user/daf :insert) {:first_name first-name
                                                   :last_name last-name
                                                   :email email
                                                   :user_name email
                                                   :phone phone
                                                   :activated false
                                                   :shipping_address shipping-address}))
                     order ((order/daf :insert) {:id 0
                                                 :user_id (:id user)
                                                 :date (java.sql.Date. (.getTime (java.util.Date.)) )
                                                 :first_name first-name
                                                 :last_name last-name
                                                 :email email
                                                 :user_name email
                                                 :phone phone
                                                 :shipping_address shipping-address
                                                 :extra_info extra-info
                                                 :total_price (calc-total-price (vals (:products cart)))
                                                 })
                     ]
                 (doseq [[k val] (:products cart)]
                   ((sitnoseckana.models.shop-order-item/daf :insert) {:id 0
                                                                       :order_id (:id order)
                                                                       :product_id (get-in val [:product :id])
                                                                       :variation_id (:variation_id val)
                                                                       :price (get-in val [:variation :price] (get-in val [:product :price]))
                                                                       :discount 0
                                                                       :quantity (:quantity val)
                                                                       :date (java.sql.Date. (.getTime (sitnoseckana.util.date/parse-date (:formatted_date val) "yyyy-MM-dd" )))
                                                                       }))

                 (sitnoseckana.util.email/send-email {:from "sitnoseckana@gmail.com"
                                                      :to (props/get-key "admin_email")
                                                      :subject (str "Porudzbina od " email)
                                                      :body [{:type "text/plain"
                                                              :content (str "Nova porudzbina je napravljena u iznosu od: " (calc-total-price (vals (:products cart))))}] })

                 {:status 200
                  :headers {"Content-Type" "application/json;  charset=utf-8"},
                  :body (cheshire.core/generate-string {:action "Uspešno naručeno"})
                  :session (assoc (:session req) :cart {:products nil} :user-ordered user)}


                 ))))