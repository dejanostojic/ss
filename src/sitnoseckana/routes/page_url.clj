(ns sitnoseckana.routes.page-url
  (:require [sitnoseckana.models.page :as page-daf]))

(defn find-parent
  "return parent page by parent page id"
  [page]
  (when  (and page (:parent_page_id page) (> 0 (:parent_page_id page))) )
  ((page-daf/page-daf :load-by-pk) (:parent_page_id page)))

(defn create-page-url
  "goes till root page and creates url"
  ([page] (create-page-url page (:name page)))
  ([page url]
   (if (nil? page)
     url
     (recur (find-parent page) (str url (:name url))))))

(defn find-page
  "finds page specified by name in children of provided parent"
  [pages parent name]
  (first (filter #(and (== (:id parent) (:parent_page_id %)) (= name (:name %))) pages)))





(defn order-page []
  (first (page-daf/load-list :where (str "type='shop' and template='orders'") :limit 1)))

(defn order-page-url []
  (create-page-url (order-page)))

(defn order-view-url [id]
  (str (order-page-url) "/" id))

;(get-in @sitnoseckana.routes.platform-metadata/plugin-map ["shop" :rw-rules "orders"])
(defn get-rw-rule
  "returns rw rule from page"
  [page]
  (get-in @sitnoseckana.routes.platform-metadata/plugin-map [(:type page) :rw-rules (:template page) ]))

(defn get-rw-params
  "scans platform metadata for rw rules and associates if needed"
  [page-data splited-path]
  (let [param-name (get-in @sitnoseckana.routes.platform-metadata/plugin-map [(:type (:page page-data)) :rw-rules (:template (:page page-data)) (:view page-data)])
        break-number (:br-pt page-data )]
    (when (and param-name break-number)
      {param-name (try (Integer/parseInt (splited-path break-number)) (catch NumberFormatException nfe -1))})))
