(ns sitnoseckana.models.base-daf
  (:import (java.sql Connection PreparedStatement ResultSet SQLException Statement))
  (:require [sitnoseckana.models.db :as db]
             [sitnoseckana.models.query-utils :as query-utils]
             [clojure.core.cache :as cache]))

(defn- filter-pk [fields]
  (filterv #(% :primary-key) fields))

(defn- filter-non-pk [fields]
  (filterv #(not (% :primary-key)) fields))

(defn col-separate 
  ([fields tbl-name] (col-separate fields tbl-name ""))
  ([fields tbl-name aggr]
    (if (empty? fields)
      (if (zero? (.length aggr))                        ;; escape if all fields are primary keys
        ""
        (subs aggr 0 (dec (.length aggr))))
      (recur (rest fields) tbl-name (str aggr tbl-name "." (:name (first fields)) ",")))))

(defn col-setters
  ([fields tbl-name] (col-setters fields tbl-name ""))
  ([fields tbl-name aggr]
    (if (empty? fields) 
      (if (zero? (.length aggr))                        ;; escape if all fields are primary keys
        ""
        (subs aggr 0 (dec (.length aggr))))
      (recur (rest fields) tbl-name (str aggr tbl-name "." (:name (first fields)) "=?,")))))

(defn pk-closure-builder
  ([fields tbl-name] (pk-closure-builder fields tbl-name ""))
  ([fields tbl-name aggr]
    (if (empty? fields) 
      (subs aggr 0 (- (.length aggr) 4))
      (recur (rest fields) tbl-name (str aggr tbl-name "." (:name (first fields)) "=? and ")))))

(defn- make-comma-sep-list [fields tbl-name]
  (col-separate fields tbl-name))

(defn- comma-sep-pk-list [fields tbl-name]
  (->  fields filter-pk (as-> x (col-separate x tbl-name))))

(defn- non-pk-comma-sep-list-builder [fields tbl-name]
  (->  fields filter-non-pk (as-> x (col-separate x tbl-name))))

(defn- non-pk-setters-fun [fields tbl-name]
  (->  fields filter-non-pk (as-> x (col-setters x tbl-name))))

(defn- pk-closure [fields tbl-name]
  (->  fields filter-pk (as-> x (pk-closure-builder x tbl-name))))

(defn loop-st 
  [fields ^PreparedStatement st map start]
    (loop [fs fields
           i start]
      (if (not (empty? fs))
        (let [field (first fs)]
          ;(println "loop rs: i:" i ", field: " (first fs)  ", value: " (get map (keyword (:name field)) ))
          ;((:map-to-param field) map st i)
          (.setObject st i (get map (keyword (:name field)) ) (:sql-type field))
          ; (println "statment: " st)
;          (.setObject st i (or (get map (:name (first fs))) (get map (first fs))))
          (recur (rest fs) (inc i))))))

(defn loop-rs [fields ^ResultSet rs]
  (loop [fs fields
         i 1
         ret-map {}]
    (if (empty? fs)
      ret-map  
      (recur (rest fs) (inc i) (assoc ret-map (keyword (:name (first fs))) (.getObject rs i))))))
;(recur (rest fs) (inc i)  ((:rs-to-map (first fs)) rs ret-map i)))))

(defn load-map [map comma-sep-list tbl-name pk-closure pk-fields fields]
   "loads row from db"
             (let [sql (str "select " comma-sep-list 
                            " from " tbl-name
                            " where " pk-closure)
                   ^PreparedStatement st (.prepareStatement db/conn sql)]
               (try
                 (do 
                  (loop-st pk-fields st map 1)
                 (let [^ResultSet rs (.executeQuery st)]
                     (if (.next rs)
                         (loop-rs fields rs)
                       ))))))

(defn settings-maker [dao-settings]
  "makes settings for specific table"
  (let [tbl-name (:tbl-name dao-settings )
        fields (:fields dao-settings)]
    (assoc dao-settings
      :pk-fields (filter-pk fields)
      :non-pk-fields (filter-non-pk fields)
      :pk-closure (pk-closure fields tbl-name)
      :non-pk-setters (non-pk-setters-fun fields tbl-name )
      :comma-sep-list (make-comma-sep-list fields tbl-name))))

(defn load-by-pk [dao-sett id]
  "loads from db row with matching id"
  (load-map {(:name (first (:pk-fields dao-sett))) id} (:comma-sep-list dao-sett) (:tbl-name dao-sett) (:pk-closure dao-sett) (:pk-fields dao-sett) (:fields dao-sett)))


(defn load-list [dao-sett {cols :cols
                           where :where
                           offset :offset
                           count :limit
                           ord :ord}]
  (let [sql (str "select " (:comma-sep-list dao-sett)
                 " from " (:tbl-name dao-sett)
                 (if (not (empty? where))
                   (str " where " where))
                 (if (not (empty? ord))
                   (str " order by " ord))
                 (if (and (not (nil? count)) (> count 0))
                   (str " limit " (if offset offset 0) "," count)))
        rs-closure (fn [^ResultSet rs]
                     (loop [fs (:fields dao-sett)
                            m {}
                            i 1]
                       (if (empty? fs)
                         m
                         (recur (rest fs) (assoc m (keyword (:name (first fs))) (.getObject rs i)) (inc i))))) ]
    (query-utils/for-each sql rs-closure)))

(defn load-map-cache
  "Loads map from cache if available, otherwise fetch from db"
  [cache-impl map comma-sep-list tbl-name pk-closure pk-fields fields]
  (let [pk-val (get map (-> pk-fields first :name keyword))]
      (get (if (cache/has? @cache-impl pk-val)
             (swap! cache-impl #(cache/hit % pk-val))
             (swap! cache-impl #(cache/miss % pk-val (load-map map comma-sep-list tbl-name pk-closure pk-fields fields)))) pk-val) ))

(defn base-daf
  "Base functions for db manipulation. Maps entity from hashmap to db table.
  dao-settings - Dao settings if mandatory argument that holds data for maping entity.
  cache-impl - external cache that actually stores the data (eg. atom). Ommiting disables cache."
  [dao-settings & [cache-impl]]
  (let [tbl-name (:tbl-name dao-settings)
        auto-increment (:auto-increment dao-settings)
        fields (:fields dao-settings)
        pk-fields (filter-pk fields)
        non-pk-fields (filter-non-pk fields)
        pk-closure (pk-closure fields tbl-name)
        non-pk-setters (non-pk-setters-fun fields tbl-name)
        comma-sep-list (make-comma-sep-list fields tbl-name)
        non-pk-comma-sep-list (make-comma-sep-list non-pk-fields tbl-name)
        dao {:load       (fn [map]
                           (if cache-impl
                             (load-map-cache cache-impl map comma-sep-list tbl-name pk-closure pk-fields fields)
                             (load-map map comma-sep-list tbl-name pk-closure pk-fields fields)
                             ))
             ;; loads from db row with matching id. If already in cache than returns from cache.
             :load-by-pk (fn [id]
                           (when (and id (number? id) (pos? id))
                             (if cache-impl
                               (load-map-cache cache-impl {(keyword (:name (first pk-fields))) id} comma-sep-list tbl-name pk-closure pk-fields fields)
                               (load-map {(keyword (:name (first pk-fields))) id} comma-sep-list tbl-name pk-closure pk-fields fields)))
                           )
             ;; loads list from database. Accepts conditional params
             ;; :cols - string -comma separated list of fields to select. Default all.
             ;; :where - string - where clause
             ;; :ord - string - order by clause - col. names and ASC DESC
             ;; :limit - number of results to fetch
             ;; :offset - number of rows to skip before loading, default 0
             :load-list  (fn [& {cols   :cols
                                 where  :where
                                 offset :offset
                                 count  :limit
                                 ord    :ord
                                 :or    {offset 0}}]
                           (let [fields (if cols
                                          (filter #(contains? (into #{} (clojure.string/split cols #",")) (:name %)) fields)
                                          fields)
                                 comma-sep-list (if cols
                                                  (make-comma-sep-list fields tbl-name)
                                                  comma-sep-list)
                                 sql (str "select " comma-sep-list
                                          " from " tbl-name
                                          (if (not (clojure.string/blank? where))
                                            (str " where " where))
                                          (if (not (clojure.string/blank? ord))
                                            (str " order by " ord))
                                          (if (and (not (nil? count)) (> count 0))
                                            (str " limit " offset "," count))
                                          )
                                 rs-closure (fn [^ResultSet rs]
                                              (loop [fs fields
                                                     m {}
                                                     i 1]
                                                (if (empty? fs)
                                                  m
                                                  (recur (rest fs) (assoc m (keyword (:name (first fs))) (.getObject rs i)) (inc i)))))]
                             (query-utils/for-each sql rs-closure)))

;             Deletes from database.
;             Accepts string condition or actual map. Removes item from cache if dao made with caching.
             :delete (fn  [param]
                         (cond
                           (string? param) (if (not (empty? param))
                                             (let [sql (str "delete from " tbl-name
                                                            (str " where " param))]
                                               (query-utils/exec sql)))
                           (map? param) (let [sql (str "delete from " tbl-name
                                                       (str " where " pk-closure))
                                              ^PreparedStatement st (.prepareStatement db/conn sql)]
                                          (try
                                            (do
                                              (println "sql: " sql)
                                              (if cache-impl
                                                (let [pk-val (get param (keyword (:name (first pk-fields))))]
                                                  (swap! cache-impl cache/evict pk-val)))

                                              (loop-st pk-fields st param 1) ;; change id columns from :keywords to "strings". From db we get keywords, but we have declared column names as strings - (reduce-kv #(assoc %1 (name %2) %3) {} param)
                                              (let [n (.executeUpdate st)]
                                                (if (pos? n)
                                                  n
                                                  false)))
                                            (finally (.close st))))))

             :insert (fn [map]
                       (let [sql (str "insert into " tbl-name
                                      " (" (if auto-increment
                                             non-pk-comma-sep-list
                                             comma-sep-list)
                                      ") values ("
                                      (reduce str (interpose "," (take (count
                                                                         (if auto-increment
                                                                           non-pk-fields
                                                                           fields) ) (repeat "?")))) ")")
                             ^PreparedStatement st (if auto-increment (.prepareStatement db/conn sql Statement/RETURN_GENERATED_KEYS) (.prepareStatement db/conn sql))]
                         (try
                           (do
                             (println "sql: " sql)
                             (loop-st (if auto-increment non-pk-fields fields)  st map 1) ;; change id columns from :keywords to "strings". From db we get keywords, but we have declared column names as strings (reduce-kv #(assoc %1 (name %2) %3) {} map)
                             (.executeUpdate st)

                             (let [ret (if auto-increment
                                         (let [rs (.getGeneratedKeys st)]
                                           (.next rs)
                                           (assoc map (keyword (:name (first pk-fields))) (.getObject rs 1)))
                                         map)]
                               (if cache-impl
                                 (swap! cache-impl #(cache/miss % (get ret (keyword (:name (first pk-fields)))) ret)))
                               ret
                               )
                             )
                           (finally (.close st)))))
             ;; updates entity in database. Also updates entry in cache if used.
             :update (fn [map & [cols]]                  ;; change to return boolean! IMPLEMENT UPDATING JUST SOME COLUMNS!
                       (let [fields (if cols
                                       (filter #(contains? (into #{} (clojure.string/split cols #",")) (:name %)) fields)
                                       fields)
                              non-pk-fields (if cols
                                              (filter-non-pk fields)
                                              non-pk-fields)
                              non-pk-setters (if cols
                                               (non-pk-setters-fun fields tbl-name)
                                               non-pk-setters)
                             sql (str "update " tbl-name
                                      " set " non-pk-setters " where " pk-closure)
                             ^PreparedStatement st (if auto-increment (.prepareStatement db/conn sql Statement/RETURN_GENERATED_KEYS) (.prepareStatement db/conn sql))]
                         (try
                           (do
                             (println "sql: " sql)
                             (println "good map: " map)

                             (loop-st non-pk-fields st map 1) ;; statment starts with 1 ;; (reduce-kv #(assoc %1 (name %2) %3) {} map)
                             (loop-st pk-fields st map (inc (count non-pk-fields))) ;; change id columns from :keywords to "strings". From db we get keywords, but we have declared column names as strings
                             (.executeUpdate st)

                             (if cache-impl
                               (swap! cache-impl #(cache/miss % (get map (keyword (:name (first pk-fields)))) map)))
                             map
                             )
                           (finally (.close st)))))

             }]
    ; if there is cache, prepopulate with load - list
    (if cache-impl
     (swap! cache-impl (fn [cache] (cache/seed cache (apply assoc {} (->> ((:load-list dao)) (map #(list ((keyword (:name (first pk-fields))) %) %)) flatten ))) ) ))
    dao
    ))

(defn- get-join-fields [dao-sett]
  (reduce #(str %1 "," %2) (flatten (map (fn [x]
                                           (map (fn [y] (:name y)) (:fields x))
                                           )
                                         (get-in dao-sett [:join]))))  )

(defn- get-join-definitions [dao-sett]
  (let [j (:j dao-sett)
        main-table (:tbl-name dao-sett)]
    (map (fn [join-tbl]
           (let [tbl-name (get-in join-tbl [:from :tbl-name])
                 fields (map #(assoc % :tbl-name tbl-name) (filter #(contains? (into #{} (:fs join-tbl)) (:name %)) (get-in join-tbl [:from :fields])))
                 join-clause (str "left join " tbl-name " on " main-table "." (first (:on join-tbl)) "=" tbl-name "." (second (:on join-tbl)))]
             {:tbl-name tbl-name, :fields fields :join-clause join-clause}))
         j)))

(comment
  (with-join sitnoseckana.models.shop-menu/dao-settings :join [{
                                                                :from sitnoseckana.models.shop-product/dao-settings
                                                                :fs ["name" "id" "ingredients"]
                                                                :on ["product_id" "id"]
                                                                } {
                                                                   :from sitnoseckana.models.shop-product-type/dao-settings
                                                                   :fs ["name" "id"]
                                                                   :on ["product_type_id" "id"]
                                                                   :left-table (:tbl-name sitnoseckana.models.shop-product/dao-settings)
                                                                   }]))
(defn get-join-definitions-2 [main-table join]

    (map (fn [join-tbl]
           (let [tbl-name (get-in join-tbl [:from :tbl-name])
                 main-table (or (join-tbl :left-table) main-table)
                 fields (map #(assoc % :tbl-name tbl-name)
                             (if (:fs join-tbl)
                               (filter #(contains? (into #{} (:fs join-tbl)) (:name %)) (get-in join-tbl [:from :fields]))
                               (get-in join-tbl [:from :fields])))
                 join-clause (str "left join " tbl-name " on " main-table "." (first (:on join-tbl)) "=" tbl-name "." (second (:on join-tbl)))]
             {:tbl-name tbl-name, :fields fields :join-clause join-clause}))
         join))

(comment
  ;get-join-definitions-3
  (with-join sitnoseckana.models.shop-menu/dao-settings :join [{
                                                                       :from sitnoseckana.models.shop-product/dao-settings
                                                                       :fs ["name" "id" "ingredients"]
                                                                       :on ["product_id" "id"]

                                                                       :join [{
                                                                               :from sitnoseckana.models.shop-product-type/dao-settings
                                                                               :fs ["name" "id"]
                                                                               :on ["product_type_id" "id"]
                                                                               :assoc-vec [(keyword (:tbl-name sitnoseckana.models.shop-product-type/dao-settings))]
                                                                               ;:left-table (:tbl-name sitnoseckana.models.shop-product/dao-settings)
                                                                               }]
                                                                       } ]))
(defn- prepare-join
  ([main-table join-tbl] (prepare-join main-table join-tbl []))
  ([main-table join-tbl aggr]
   (let [tbl-name (get-in join-tbl [:from :tbl-name])
         assoc-vec (or (-> aggr last :assoc-vec) [])
         fields (map #(assoc % :tbl-name tbl-name :assoc-vec (conj assoc-vec (keyword tbl-name)))
                     (if (:fs join-tbl)
                       (filter #(contains? (into #{} (:fs join-tbl)) (:name %)) (get-in join-tbl [:from :fields]))
                       (get-in join-tbl [:from :fields])))
         join-clause (str "left join " tbl-name " on " main-table "." (first (:on join-tbl)) "=" tbl-name "." (second (:on join-tbl)))

         next-aggr (conj aggr {:tbl-name tbl-name, :fields fields :join-clause join-clause :assoc-vec (conj assoc-vec (keyword tbl-name))})]
     (if-let [nested (:join join-tbl)]
       (prepare-join tbl-name nested next-aggr)
       next-aggr)
     )))

(defn get-join-definitions-3 [main-table join]

  (flatten (map (fn [ join-tbl]

                  (prepare-join main-table join-tbl))
                join)))

(defn with-join
  "Loads list of items joining multipe tables"
  [dao-settings & {join :join
                   cols   :cols
                   where  :where
                   offset :offset
                   count  :limit
                   ord    :ord
                   :or    {offset 0}}]

  (let [jds (if join
              (get-join-definitions-3 (:tbl-name dao-settings) join)
              (get-join-definitions dao-settings))
        comma-sep-list (str (make-comma-sep-list (:fields dao-settings) (:tbl-name dao-settings)) ;; base table cols
                            (reduce #(str %1 "," (make-comma-sep-list (:fields %2) (:tbl-name %2))) "" jds)) ;; add cols from join tables
        from (reduce #(str %1 " " (:join-clause %2)) (:tbl-name dao-settings) jds)
        sql (str "select " comma-sep-list
                 " from " from
                 (if (not (clojure.string/blank? where))
                   (str " where " where))
                 (if (not (clojure.string/blank? ord))
                   (str " order by " ord))
                 (if (and (not (nil? count)) (> count 0))
                   (str " limit " offset "," count))
                 )
        rs-closure (fn [^ResultSet rs]
                     ; (clojure.pprint/pprint (concat (:fields dao-settings) (flatten (map #(:fields %) jds))))
                     (loop [fs (concat (:fields dao-settings) (flatten (map #(:fields %) jds)))
                            m {}
                            i 1]

                       (if (empty? fs)
                         m
                         (recur (rest fs)
                                (if-let [assoc-vec (:assoc-vec (first fs))]
                                  (assoc-in m (conj assoc-vec (keyword (:name (first fs)))) (.getObject rs i))
                                  (assoc m (keyword (:name (first fs))) (.getObject rs i)))
                                (inc i)))))]
    ;jds
    ; (println "jds: " jds)
    ;(println "with join sql: " sql)
    (query-utils/for-each sql rs-closure)
    ))

(defn load-list-with-join
  "Loads list of items joining multipe tables"
  [dao-settings & {cols   :cols
                    where  :where
                    offset :offset
                    count  :limit
                    ord    :ord
                    :or    {offset 0}}]
  (let [jds (get-join-definitions dao-settings)
        comma-sep-list (str (make-comma-sep-list (:fields dao-settings) (:tbl-name dao-settings)) ;; base table cols
                            (reduce #(str %1 "," (make-comma-sep-list (:fields %2) (:tbl-name %2))) "" jds) ) ;; add cols from join tables
        from (reduce #(str %1 " " (:join-clause %2)) (:tbl-name dao-settings) jds)
        sql (str "select " comma-sep-list
                 " from " from
                 (if (not (clojure.string/blank? where))
                   (str " where " where))
                 (if (not (clojure.string/blank? ord))
                   (str " order by " ord))
                 (if (and (not (nil? count)) (> count 0))
                   (str " limit " offset "," count))
                 )
        rs-closure (fn [^ResultSet rs]

                     (loop [fs (concat (:fields dao-settings) (flatten (map #(:fields %) jds)))
                            m {}
                            i 1]

                       (if (empty? fs)
                         m
                         (recur (rest fs)
                                (if-let [tbl (keyword (:tbl-name (first fs)))]
                                  (assoc-in m [tbl (keyword (:name (first fs)))] (.getObject rs i))
                                  (assoc m (keyword (:name (first fs))) (.getObject rs i)))
                                 (inc i)))))]
    (clojure.pprint/pprint (concat (:fields dao-settings) (flatten (map #(:fields %) jds))))
    (println sql)
    (query-utils/for-each sql rs-closure)

    ))