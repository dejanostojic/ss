(ns sitnoseckana.models.page
  (:import (java.sql PreparedStatement ResultSet Types))
  (:require [sitnoseckana.models.base-daf :as base]
            [sitnoseckana.util.web :refer [vec-to-bool]]
            [clojure.core.cache :as cache]))


(def page-sett {:tbl-name "page"
               :auto-increment true
               :fields [{:name "id" 
                         :primary-key true 
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :id (.getLong rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setLong statment index (:id page)))
                         :convert (fn [val]
                                    (Long/parseLong val))
                         :sql-type Types/BIGINT
                         :java-type Long
                         }
                        {:name "type" 
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :type (.getString rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setString statment index (:type page)))
                         :sql-type Types/VARCHAR
                         :java-type String
                         }
                        {:name "template" 
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :template (.getString rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setString statment index (:template page)))
                         :sql-type Types/VARCHAR
                         :java-type String
                         }
                        {:name "kind"
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :kind (.getString rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setString statment index (:kind page)))
                         :sql-type Types/VARCHAR
                         :java-type String
                         }
                        {:name "language_code" 
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :language_code (.getString rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setString statment index (:language_code page)))
                         :sql-type Types/VARCHAR
                         :java-type String
                         }
                        {:name "parent_page_id" 
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :parent_page_id (.getString rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setLong statment index (:parent_page_id page)))
                         :convert (fn [val]
                                    (Long/parseLong val))
                         :sql-type Types/BIGINT
                         :java-type Long
                         }
                        {:name "name" 
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :name (.getString rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setString statment index (:name page)))
                         :sql-type Types/VARCHAR
                         :java-type String
                         }
                        {:name "depth" 
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :depth (.getInt rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setInt statment index (:depth page)))
                         :convert (fn [val]
                                    (Integer/parseInt val))
                         :sql-type Types/INTEGER
                         :java-type Integer
                         }
                        {:name "ord" 
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :ord (.getInt rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setInt statment index (:ord page)))
                         :convert (fn [val]
                                    (Integer/parseInt val))
                         :sql-type Types/INTEGER
                         :java-type Integer
                         }
                        {:name "title" 
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :title (.getString rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setString statment index (:title page)))
                         :sql-type Types/VARCHAR
                         :java-type String
                         }
                        {:name "body" 
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :body (.getString rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setString statment index (:body page)))
                         :sql-type Types/VARCHAR
                         :java-type String
                         }
                        {:name "lead" 
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :lead (.getString rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setString statment index (:lead page)))
                         :sql-type Types/VARCHAR
                         :java-type String
                         }
                        {:name "published" 
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :published (.getBoolean rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setBoolean statment index (or (:published page) false)))
                         :convert (fn [val]
                                    (vec-to-bool val))
                         :sql-type Types/TINYINT
                         :java-type Boolean
                         }
                        {:name "primary_navigation" 
                         :rs-to-map (fn [^ResultSet rs page index]
                                      (assoc page :primary_navigation (.getBoolean rs index) ))
                         :map-to-param (fn [page ^PreparedStatement statment index]
                                         (.setBoolean statment index (or (:primary_navigation page) false)))
                         :convert (fn [val]
                                    (vec-to-bool val))
                         :sql-type Types/TINYINT
                         :java-type Boolean
                         }


        ;                {:name "member_only"}
       ;                 {:name "secondary_navigation"}
      ;                  {:name "mobile_navigation"}
     ;                   {:name "meta_description"}
    ;                    {:name "thumb_id1"}
   ;                     {:name "thumb_id2"}
  ;                      {:name "thumb_id3"}
 ;                       {:name "thumb_id4"}
 ;                       {:name "meta_title"}
;                        {:name "extras"}
;                        {:name "tag"}
 ;                       {:name "with_comments"}
                        ]})
  
(def tbl-settins (base/settings-maker page-sett))

(def page-cache (atom (cache/basic-cache-factory {})))

(def page-daf (base/base-daf page-sett page-cache))

;(def page-cache (atom ((:load-list page-daf) {}))) ;; TEMPORARY SOLUTION !!!!!!


(defn find-page-by-id [id]
  (first (filter #(do (println "id: " (:id %)) (= id (:id %))) (vals @page-cache))))


(defn load-root 
  "Finds root page in provided pages, for provided language code"
  ([lang] (load-root (vals @page-cache)  lang))
  ([pages lang]
    (first (filter #(and (zero? (:depth %)) (= lang (:language_code %))) pages))))

(comment
(defn load-root
  "Finds root page in provided pages, for provided language code"
  ([lang] (load-root @page-cache lang))
  ([pages lang]
  (loop [pgs pages]
    (let [page  (first pgs)]
      (if (or (empty? page) (and (== 0 (:depth page)) (= lang (:language_code page))))
        page
        (recur (rest pgs))))))))


(defn load-all []
  ((:load-list page-daf)))

(defn load-list [& params]
  (apply (:load-list page-daf) params))

;{:language_code sr, :ord 7, :name aa, :title aaa, :primary_navigation true, :id 0, :secondary_navigation true, :lead aa, :depth 1, :body <p>aa</p>, :parent_page_id 47, :published false}
