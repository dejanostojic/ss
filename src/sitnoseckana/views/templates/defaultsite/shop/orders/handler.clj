(ns sitnoseckana.views.templates.defaultsite.shop.orders.handler
  (:require [sitnoseckana.models.shop-product-type-variation :as shop-product-type-variation]
            [sitnoseckana.models.shop-variation :as shop-variation]
            [sitnoseckana.models.shop-product]
            [sitnoseckana.models.shop-order-item :as shop-order-item]
            [sitnoseckana.models.shop-order :as shop-order]
            [sitnoseckana.routes.order]
            [sitnoseckana.routes.page-url]
            ))

(defn handle-index [ctx]
  (assoc ctx
    :order-list (map #(assoc % :order-view-link (sitnoseckana.routes.page-url/order-view-url (:id %))) ((shop-order/daf :load-list) :where (str "user_id=" (get-in ctx [:request :session :user-data :user :id] 0)) :order-by "id desc"))))

(defn handle-view [ctx]
  (assoc ctx
  :order ((shop-order/daf :load-by-pk) (ctx :order-id 0))
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
                                                                                                                :where (str "order_id=" (ctx :order-id 0)) ))))