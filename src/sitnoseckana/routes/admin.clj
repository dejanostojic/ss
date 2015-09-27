(ns sitnoseckana.routes.admin
  (:require [compojure.core :refer :all]
            [liberator.core :refer [defresource resource request-method-in]]
            [liberator.representation :refer [ring-response]]
            [selmer.parser :as parser]
            [sitnoseckana.models.page :as page-daf]
            [sitnoseckana.app.langs :as langs]
            [ring.util.response :refer [redirect]]
            [sitnoseckana.util.web :as web]
            [sitnoseckana.models.shop-order :as order]
            [sitnoseckana.util.date :as date-util])
  (:import (java.time LocalDate DayOfWeek)))

(def template-folder "sitnoseckana/views/templates")
(def site "admin")
(def theme "devoops")
(def page-suffix ".html")
(def langs ["en" "sr"])

(defn handle-admin-ok
  [ctx]
  "shows requested admin page"
  (ring-response {:body (parser/render-file (str site "/" theme "/" (:path ctx) page-suffix) (-> ctx
                                                                                                 web/assoc-bundle
                                                                                                 web/assoc-anti-forgery
                                                                                                 ))})
  )


(defresource admin [path]
         :exists? (fn [ctx]
                   (println "admin exists: " (get-in ctx [:request]))
                    (cond
                     (clojure.java.io/resource (str template-folder "/" site "/" theme "/" path "/index" page-suffix))
                       {:path (str path "/index")} ;; pages -> pages/index.html
                     (clojure.java.io/resource (str template-folder "/" site "/" theme "/" path page-suffix))
                       [true {:path path}] ;; pages/index -> pages/index.html
                     :else false))
  
         :handle-ok (fn [ctx]
                      (handle-admin-ok ctx))
         
         :handle-not-found  (fn [ctx]
                              (str "url: " (get-in ctx [:request :uri]) " not found."))
                      
         ;:etag "fixed etag" ;; used for caching by browsers...
         :available-media-types ["text/html"]
         )

;        (defresource logout
;          :available-media-types ["text/html"]
;          :method-allowed? (request-method-in :get)
;          :handle-ok (fn [context]
;               (ring-response
;                (response/redirect
;                 (str (get-in context [:request :context]) "/")))))

(defresource admin-home []
         :handle-ok (fn [ctx]
                      (println "admin-home: " (get-in ctx [:request :session]))
                      (ring-response
                        (ring.util.response/redirect "/admin/index"))
                      ;(handle-admin-ok (assoc ctx :path "index"))
                      )
         ;:etag "fixed etag" ;; used for caching by browsers...
         :available-media-types ["text/html"])


(defn get-order-items []

  ;in Clojure solution
    #_(->> (sitnoseckana.models.base-daf/with-join sitnoseckana.models.shop-order-item/dao-settings
                                                :join [{
                                                        :from sitnoseckana.models.shop-product/dao-settings
                                                        :fs ["product_type_id" "price"]
                                                        :on ["product_id" "id"]
                                                        :join {
                                                               :from sitnoseckana.models.shop-product-type/dao-settings
                                                               :fs ["name"]
                                                               :on ["product_type_id" "id"]
                                                               }
                                                        }]
                                                )
         (group-by #(get-in % [:shop_product :shop_product_type :name]))
         (map (fn [map] {(key map) (reduce (fn [x1 x2]
                                            (+ x1 (* (x2 :price) (x2 :quantity)) )) 0 (val map) )})))

  ;sql solution
    (sitnoseckana.models.query-utils/for-each "select sum(shop_order_item.price * shop_order_item.quantity) as income,shop_product_type.name from shop_order_item left join shop_product on shop_order_item.product_id=shop_product.id left join shop_product_type on shop_product.product_type_id=shop_product_type.id where true group by shop_product_type.name order by income"
                                              (fn [^java.sql.ResultSet rs] [(.getString rs 2) (.getDouble rs "income")]))

    )



(defroutes admin-routes
  (ANY ["/admin/*"] {{path :*} :route-params} (admin path))
           ;(ANY ["/admin"] [] (admin-home))
   (GET ["/admin"] [] (parser/render-file (str site "/" theme "/index"  page-suffix)
                                                    (-> {}
                                                        web/assoc-bundle
                                                        web/assoc-anti-forgery
                                                        (assoc :page-title (str "Dashboard " )
                                                               :order-items (cheshire.core/generate-string (get-order-items))
                                                                 ))
                                                    )))  ;; /admin/pages/index

