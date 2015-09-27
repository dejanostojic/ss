(ns sitnoseckana.views.templates.defaultsite.shop.cart.handler)

(defn handle-index [ctx]
  (assoc ctx :page-attr {:id 123 :title "app page index"}))