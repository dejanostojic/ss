(ns sitnoseckana.routes.cart
  (:require [compojure.core :refer [defroutes GET POST ANY DELETE]]
            [liberator.core :refer [defresource resource request-method-in]]
            [liberator.representation :refer [ring-response as-response]]
            [cheshire.core]
            [compojure.response]
            ))


(defroutes cart-routes
           (POST ["/dost/shop/cart"] [] (fn [req]
                                           (let [form-params (get-in req [:form-params])
                                                 session (:session req)
                                                 cart (get-in req [:session :cart])
                                                 product-id (try
                                                              (Long/parseLong (form-params "productId"))
                                                              (catch NumberFormatException nfe 0))
                                                 quantity (try
                                                            (Integer/parseInt (form-params "quantity"))
                                                            (catch NumberFormatException nfe 0))
                                                 type-id (try
                                                           (Long/parseLong (form-params "typeId"))
                                                           (catch NumberFormatException nfe 0))
                                                 variation-id (try
                                                                (Long/parseLong (form-params "variationId"))
                                                                (catch NumberFormatException nfe 0))
                                                 formatted-date (form-params "formattedDate")
                                                 for-friend (form-params "forFriend")
                                                 product (first (sitnoseckana.models.base-daf/with-join sitnoseckana.models.shop-product/dao-settings
                                                                                                        :join [{
                                                                                                                :from sitnoseckana.models.shop-product-type/dao-settings
                                                                                                                :fs   ["name" "id"]
                                                                                                                :on   ["product_type_id" "id"]
                                                                                                                }]
                                                                                                        :where (str "shop_product.id=" product-id) ))
                                                 variation (first (sitnoseckana.models.base-daf/with-join sitnoseckana.models.shop-product-type-variation/dao-settings
                                                                                                          :join [{
                                                                                                                  :from sitnoseckana.models.shop-variation/dao-settings
                                                                                                                  :on   ["variation_id" "id"]
                                                                                                                  }]
                                                                                                          :where (str "type_id=" type-id " and variation_id=" variation-id ) ))
                                                 ]

                                             (clojure.pprint/pprint (assoc session :cart (if (zero? quantity)
                                                                                         (update-in cart [:products] dissoc {product-id variation-id} )
                                                                                         (assoc-in cart [:products {product-id variation-id}] {:quantity quantity
                                                                                                                                               :type_id type-id
                                                                                                                                               :variation_id variation-id
                                                                                                                                               :formatted_date formatted-date
                                                                                                                                               :product product
                                                                                                                                               :variation variation}))  ))





                                              {:status 200
                                               :headers {"Content-Type" "application/json;  charset=utf-8"},
                                               :body (cheshire.core/generate-string {:quantity quantity
                                                                                     :type_id type-id
                                                                                     :variation_id variation-id
                                                                                     :formatted_date formatted-date
                                                                                     :product product
                                                                                     :variation variation})
                                              :session (assoc session :cart (if (zero? quantity)
                                                                              (update-in cart [:products] dissoc {product-id variation-id} )
                                                                              (assoc-in cart [:products {product-id variation-id}] {:quantity quantity
                                                                                                                                    :type_id type-id
                                                                                                                                    :variation_id variation-id
                                                                                                                                    :formatted_date formatted-date
                                                                                                                                    :product product
                                                                                                                                    :variation variation}))  )})
                                          ) )

           (DELETE ["/dost/shop/cart"] [] (fn [req]
                                         {:status 200
                                          :headers {"Content-Type" "application/json;  charset=utf-8"},
                                          :body (cheshire.core/generate-string {:action "deleted"})
                                          :session (assoc (:session req) :cart {:products nil}  )}))

           (GET ["/dost/shop/cart"] [] (fn [req]
                                         {:status 200
                                          :headers {"Content-Type" "application/json;  charset=utf-8"},
                                          :body (cheshire.core/generate-string (get-in req [:session :cart]))}))
           )