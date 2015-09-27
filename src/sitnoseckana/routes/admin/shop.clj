(ns sitnoseckana.routes.admin.shop
  (:require [compojure.core :refer [defroutes ANY GET PUT]]
            [liberator.core :refer [defresource resource request-method-in]]
            [liberator.representation :refer [ring-response]]
            [selmer.parser :as parser]
            [sitnoseckana.models.shop-product-type-variation :as shop-product-type-variation]
            [sitnoseckana.models.shop-product-type :as shop-product-type]
            [sitnoseckana.models.shop-variation :as shop-variation]
            [sitnoseckana.models.shop-product :as shop-product]
            [sitnoseckana.models.shop-menu :as shop-menu]
            [sitnoseckana.models.shop-order-item :as shop-order-item]
            [sitnoseckana.models.shop-order :as shop-order]
            [sitnoseckana.routes.admin :as admin]
            [ring.util.response :refer [redirect]]
            [sitnoseckana.util.web :as web]
            [sitnoseckana.util.date :as date-util])
  (:import (java.time DayOfWeek LocalDate)))



(defresource type-form [id ajax]
             :available-media-types ["application/json" "text/html"]
             :allowed-methods [:get :put :delete]
             :exists? (fn [_]
                        (let [shop-product-type ((:load-by-pk shop-product-type/daf) (Long/parseLong id))]
                          (if-not (nil? shop-product-type)
                            {:shop-product-type shop-product-type})))

             :post! (fn [ctx]
                      ((:insert shop-product-type/daf) (web/keywordize-request (:form-params (:request ctx)) shop-product-type/dao-settings)))
             :put! (fn [ctx]
                     ((:update shop-product-type/daf) (web/keywordize-request (:form-params (:request ctx)) shop-product-type/dao-settings)))
             :new? false

             :handle-no-content (fn [_] "no content")

             :delete! #((:delete shop-product-type/daf) (:shop-product-type %))

             :handle-ok (fn [ctx]
                          (parser/render-file (str admin/site "/" admin/theme "/shop/productType/form" (if ajax "-ajax") admin/page-suffix)
                                              (-> ctx
                                                  web/assoc-bundle
                                                  web/assoc-anti-forgery
                                                  (assoc :page-title (str "Shop: " )))
                                              ))

             :handle-not-found (fn [_] (str "not found page with id: " id))
             )

(defresource type-list [ajax]
             :available-media-types ["application/json" "text/html"]
             :allowed-methods [:get :post]

             :post! (fn [ctx]
                      (println "req: " )
                      (clojure.pprint/pprint (web/keywordize-request (:form-params (:request ctx)) shop-product-type/dao-settings))
                      ((:insert shop-product-type/daf) (web/keywordize-request (:form-params (:request ctx)) shop-product-type/dao-settings))
                      )

             :handle-ok (fn [ctx]
                          (parser/render-file (str admin/site "/" admin/theme "/shop/productType/list" (if ajax "-ajax") admin/page-suffix)
                                              (-> ctx
                                                  web/assoc-bundle
                                                  web/assoc-anti-forgery
                                                  (assoc :page-title (str "Shop: " )
                                                         :type-list ((:load-list shop-product-type/daf))))
                                              ))
             )


(defresource product-form [id ajax]
             :available-media-types ["application/json" "text/html"]
             :allowed-methods [:get :put :delete]
             :exists? (fn [_]
                        (let [product ((:load-by-pk shop-product/daf) (Long/parseLong id))]
                          (if-not (nil? product)
                            {:product product})))

             :post! (fn [ctx]
                      ((:insert shop-product/daf) (web/keywordize-request (:form-params (:request ctx)) shop-product/dao-settings)))
             :put! (fn [ctx]
                     ((:update shop-product/daf) (web/keywordize-request (:form-params (:request ctx)) shop-product/dao-settings)))
             :new? false

             :handle-no-content (fn [_] "no content")

             :delete! #((:delete shop-product/daf) (:shop-product-type %))

             :handle-ok (fn [ctx]
                          (parser/render-file (str admin/site "/" admin/theme "/shop/product/form" (if ajax "-ajax") admin/page-suffix)
                                              (-> ctx
                                                  web/assoc-bundle
                                                  web/assoc-anti-forgery

                                                  (assoc :page-title (str "Product: " )
                                                         :product-types-json (cheshire.core/generate-string ((:load-list shop-product-type/daf)))))
                                              ))

             :handle-not-found (fn [_] (str "not found page with id: " id))
             )

(defresource product-list [ajax]
             :available-media-types ["application/json" "text/html"]
             :allowed-methods [:get :post]

             :post! (fn [ctx]
                      (println "req: " )
                      (clojure.pprint/pprint (web/keywordize-request (:form-params (:request ctx)) shop-product/dao-settings))
                      ((:insert shop-product/daf) (web/keywordize-request (:form-params (:request ctx)) shop-product/dao-settings))
                      )

             :handle-ok #(let [media-type (get-in % [:representation :media-type])]
                          (println "request: " (get-in % [:request :params :name]))
                          (condp = media-type
                            "application/json" (let [params (get-in % [:request :params])
                                                     name (:name params)]
                                                 (cheshire.core/generate-string (sitnoseckana.models.base-daf/load-list-with-join shop-product/dao-settings :where (str (:tbl-name shop-product/dao-settings) ".name like " (sitnoseckana.models.query-utils/like-literal name)) )))
                            "text/html" (parser/render-file (str admin/site "/" admin/theme "/shop/product/list" (if ajax "-ajax") admin/page-suffix)
                                                            (-> %
                                                                web/assoc-bundle
                                                                web/assoc-anti-forgery
                                                                (assoc :page-title (str "Shop: " )
                                                                       :product-types-json (cheshire.core/generate-string ((:load-list shop-product-type/daf)))
                                                                       :product-list (sitnoseckana.models.base-daf/load-list-with-join shop-product/dao-settings
                                                                                                                                       :where (let [params (get-in % [:request :params])
                                                                                                                                                    name-sql (if-let [name (:name params)]
                                                                                                                                                               (str " and " (:tbl-name shop-product/dao-settings) ".name like " (sitnoseckana.models.query-utils/like-literal name)))
                                                                                                                                                    type-sql (if-let [type-sql (if-let [type (:product_type_id params)]
                                                                                                                                                                                 (if (string? type)
                                                                                                                                                                                   type
                                                                                                                                                                                   (clojure.string/join "," type)))]
                                                                                                                                                               (str " and " (:tbl-name shop-product/dao-settings) ".product_type_id in (" type-sql ")")) ]
                                                                                                                                                (str "true" name-sql type-sql)))))
                                                            )))
             )

(defresource type-variation-form [type-id variation-id ajax]
             :available-media-types ["text/html"]
             :allowed-methods [:get :put :delete]
             :exists? (fn [_]
                        (println "exists: " type-id " - " variation-id)
                        (let [product ((shop-product-type-variation/daf :load) {:type_id (Long/parseLong type-id)
                                                                                    :variation_id (Long/parseLong variation-id)})]
                          (if-not (nil? product)
                            {:type-var product})))

             :post! (fn [ctx]
                      ((:insert shop-product-type-variation/daf) (web/keywordize-request (:form-params (:request ctx)) shop-product-type-variation/dao-settings)))
             :put! (fn [ctx]
                     ((:update shop-product-type-variation/daf) (web/keywordize-request (:form-params (:request ctx)) shop-product-type-variation/dao-settings)))
             :new? false

             :handle-no-content (fn [_] "no content")

             :delete! #((:delete shop-product/daf) (:type-var %))

             :handle-ok (fn [ctx]
                          (parser/render-file (str admin/site "/" admin/theme "/shop/typeVariation/form" (if ajax "-ajax") admin/page-suffix)
                                              (-> ctx
                                                  web/assoc-bundle
                                                  web/assoc-anti-forgery

                                                  (assoc :page-title (str "Product: " )
                                                         :shop-product-types ((shop-product-type/daf :load-list))
                                                         :shop-variation ((shop-variation/daf :load-list))))
                                              ))

             :handle-not-found (fn [_] (str "not found type!"))
             )



(defresource type-variation-list [ajax]
             :available-media-types ["text/html"]
             :allowed-methods [:get :post]

             :post! (fn [ctx]
                      (println "req: " )
                      (clojure.pprint/pprint (web/keywordize-request (:form-params (:request ctx)) shop-product-type-variation/dao-settings))
                      ((shop-product-type-variation/daf :insert) (web/keywordize-request (:form-params (:request ctx)) shop-product-type-variation/dao-settings))
                      )

             :handle-ok (fn [ctx]
                          (parser/render-file (str admin/site "/" admin/theme "/shop/typeVariation/list" (if ajax "-ajax") admin/page-suffix)
                                              (-> ctx
                                                  web/assoc-bundle
                                                  web/assoc-anti-forgery
                                                  (assoc :page-title (str "Shop: " )
                                                         :type-variation-list (sitnoseckana.models.base-daf/load-list-with-join shop-product-type-variation/dao-settings)))
                                              ))
             )

(defresource menu-list []
             :available-media-types ["application/json"]
             :allowed-methods [:post :get]

             :post! (fn [ctx]
                      (if-let [menu-entry ((:insert shop-menu/daf) (web/keywordize-request (:form-params (:request ctx)) shop-menu/dao-settings))]
                        {:menu-entry menu-entry}))

             :new? true

             :handle-created (fn [ctx]
                               (cheshire.core/generate-string (ctx :menu-entry)))

             :handle-ok (fn [ctx] (let [params (:params (:request ctx))]
                                    (cheshire.core/generate-string ;(sitnoseckana.models.base-daf/load-list-with-join shop-menu/dao-settings :where (str "date>='" (params :start) "' and date<='" (params :end) "'" ))
                                      (sitnoseckana.models.base-daf/with-join shop-menu/dao-settings
                                                                              :join [{
                                                                                      :from sitnoseckana.models.shop-product/dao-settings
                                                                                      :fs   ["name" "id" "ingredients"]
                                                                                      :on   ["product_id" "id"]
                                                                                      :join {
                                                                                             :from sitnoseckana.models.shop-product-type/dao-settings
                                                                                             :fs   ["name" "id" "color"]
                                                                                             :on   ["product_type_id" "id"]
                                                                                             }
                                                                                      }]
                                                                              :where (str "date>='" (params :start) "' and date<='" (params :end) "'" ) :ord "date asc, product_type_id asc")
                                      )) )

             :handle-no-content (fn [_] "no content"))


(defresource menu-form [id]
             :available-media-types ["application/json"]
             :allowed-methods [:get :put :delete]

             :exists? (fn [_]
                        (let [menu ((shop-menu/daf :load) {:id (Long/parseLong id)})]
                          (if-not (nil? menu)
                            {:menu-entry menu})))

             :put! (fn [ctx]
                     {:menu-entry ((:update shop-menu/daf) (web/keywordize-request (:form-params (:request ctx)) shop-menu/dao-settings))})

             :new? false

             :delete! #((:delete shop-menu/daf) (:menu-entry %))

             :handle-ok (fn [ctx] (let [params (:params (:request ctx))]
                                    (cheshire.core/generate-string (sitnoseckana.models.base-daf/load-list-with-join shop-menu/dao-settings :where (str "date>='" (params :start) "' and date<='" (params :end) "'" )))) )

             :handle-no-content (fn [_] "no content"))

(defroutes shop-routes

           (GET ["/admin/shop/product/new"] [] (parser/render-file (str admin/site "/" admin/theme "/shop/product/form"  admin/page-suffix)
                                                                       (-> {}
                                                                           web/assoc-bundle
                                                                           web/assoc-anti-forgery
                                                                           (assoc :page-title (str "Shop: " )
                                                                                  :product-types-json (cheshire.core/generate-string ((:load-list shop-product-type/daf)))))
                                                                       ))

           (ANY ["/admin/shop/product/:id"] [id] (product-form id false))
           (ANY ["/admin/shop/product"] [] (product-list false))
           (ANY ["/admin/shop/product-ajax"] [] (product-list true))

           (GET ["/admin/shop/productType/new"] [] (parser/render-file (str admin/site "/" admin/theme "/shop/productType/form"  admin/page-suffix)
                                                                            (-> {}
                                                                                web/assoc-bundle
                                                                                web/assoc-anti-forgery
                                                                                (assoc :page-title (str "Shop: " )))
                                                                            ))
           (ANY ["/admin/shop/productType"] [] (type-list false ))
           (ANY ["/admin/shop/productType/:id"] [id] (type-form id false))
           (ANY ["/admin/shop/productType-ajax/:id"] [id] (type-form id true))

           (ANY ["/admin/shop/productTypeVariation"] [] (type-variation-list false))
           (GET ["/admin/shop/productTypeVariation/new"] [] (fn [ctx]
                                                              (parser/render-file (str admin/site "/" admin/theme "/shop/typeVariation/form" admin/page-suffix)
                                                                                  (-> ctx
                                                                                      web/assoc-bundle
                                                                                      web/assoc-anti-forgery

                                                                                      (assoc :page-title (str "Product: " )
                                                                                             :shop-product-types ((shop-product-type/daf :load-list))
                                                                                             :shop-variation ((shop-variation/daf :load-list))))
                                                                                  )))
           (ANY "/admin/shop/productTypeVariation/:par1/:par2" [par1 par2] (type-variation-form par1 par2 false))

           (GET ["/admin/shop/menu"] [] (parser/render-file (str admin/site "/" admin/theme "/shop/menu/index"  admin/page-suffix)
                                                                       (-> {}
                                                                           web/assoc-bundle
                                                                           web/assoc-anti-forgery
                                                                           (assoc :page-title (str "Shop: " )
                                                                                  :product-list ((shop-product/daf :load-list))
                                                                                  :product-types-json (cheshire.core/generate-string ((shop-product-type/daf :load-list)))
                                                                                  :product-types ((shop-product-type/daf :load-list) )
                                                                                  :selected-week (sitnoseckana.models.config/active-week-number)
                                                                                  :product-list-json (cheshire.core/generate-string (sitnoseckana.models.base-daf/load-list-with-join shop-product/dao-settings))  ))
                                                                       ))

           (ANY ["/admin/shop/menu/calendar"] [] (menu-list))
           (ANY ["/admin/shop/menu/calendar/:id"] [id] (menu-form id))
           (GET ["/admin/shop/menu/activeWeek"] [] (fn [req]
                                                     {:status  200
                                                     :headers {"content-type" "application/json"}
                                                     :body    (cheshire.core/generate-string (let [day-in-week (sitnoseckana.models.config/date-from-week-number)
                                                                                                   monday (date-util/get-day day-in-week DayOfWeek/MONDAY)
                                                                                                   friday (date-util/get-day day-in-week DayOfWeek/FRIDAY)
                                                                                                   work-week (date-util/get-days monday friday)]
                                                                                               (map (fn [day]
                                                                                                      {:start-date (date-util/format-date day "yyyy-MM-dd" "sr")}) work-week))) } ))
           (PUT ["/admin/shop/menu/activeWeek/:numb"] [numb] (fn [req]
                                                     (sitnoseckana.models.config/change-active-week-number numb)
                                                     {:status  200
                                                     :headers {"content-type" "application/json"}
                                                     :body (cheshire.core/generate-string (let [day-in-week (sitnoseckana.models.config/date-from-week-number)
                                                                                                   monday (date-util/get-day day-in-week DayOfWeek/MONDAY)
                                                                                                   friday (date-util/get-day day-in-week DayOfWeek/FRIDAY)
                                                                                                   work-week (date-util/get-days monday friday)]
                                                                                               (map (fn [day]
                                                                                                      {:start-date (date-util/format-date day "yyyy-MM-dd" "sr")}) work-week))) } ))
           (GET ["/admin/shop/order"] [] (parser/render-file (str admin/site "/" admin/theme "/shop/order/index"  admin/page-suffix)
                                                            (-> {}
                                                                web/assoc-bundle
                                                                web/assoc-anti-forgery
                                                                (assoc :page-title (str "Orders" )
                                                                       :order-list ((shop-order/daf :load-list) :order-by "id desc")))
                                                            ))
           (GET ["/admin/shop/order/:id"] [id] (parser/render-file (str admin/site "/" admin/theme "/shop/order/form"  admin/page-suffix)
                                                            (-> {}
                                                                web/assoc-bundle
                                                                web/assoc-anti-forgery
                                                                (assoc :page-title (str "Orders" )
                                                                       :order ((shop-order/daf :load-by-pk) id)
                                                                       :order-item-list (map #(assoc % :total (* (:quantity %) (:price %)) ) (sitnoseckana.models.base-daf/with-join shop-order-item/dao-settings
                                                                                                                                               :join [{
                                                                                                                                                       :from sitnoseckana.models.shop-product/dao-settings
                                                                                                                                                       ;:fs   ["name" "id"]
                                                                                                                                                       :on   ["product_id" "id"]
                                                                                                                                                       }
                                                                                                                                                      {
                                                                                                                                                       :from sitnoseckana.models.shop-variation/dao-settings
                                                                                                                                                       :on ["variation_id" "id"]
                                                                                                                                                       }]
                                                                                                                                               :where (str "order_id=" id) ))

                                                                       ) )
                                                            ))
           )