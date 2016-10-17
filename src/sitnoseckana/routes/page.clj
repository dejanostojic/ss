(ns sitnoseckana.routes.page
  (:require [compojure.core :refer :all]
            [liberator.core :refer [defresource resource request-method-in]]
            [selmer.parser :as parser]
            [sitnoseckana.models.page :as page-daf]
            [sitnoseckana.routes.page-url :as page-url]
            [sitnoseckana.routes.platform-metadata])
  ;(:use [sitnoseckana.routes.page-url])
  (:import (java.util Locale)))


  #_(require 'sitnoseckana.views.templates.defaultsite.shop.products.handler
         'sitnoseckana.views.templates.defaultsite.shop.cart.handler
         'sitnoseckana.views.templates.defaultsite.app.page.handler
         'sitnoseckana.views.templates.defaultsite.platform.home.handler
         'sitnoseckana.views.templates.defaultsite.platform.homeberg.handler
         'sitnoseckana.views.templates.defaultsite.app.newsletter.handler) ;; DONE: this can be required programmatically
  



(def template-folder "sitnoseckana/views/templates")
(def site "defaultsite")
(def page-suffix ".html")
(def langs ["sr" "en"]) ;; language list for test. Default lang is first! 
;; example sr url: /vesti/sport.html
;; example en url: /en/stories/sprot.html


(comment
  ;plugin map structure
{"app" {:templates {"page" {:views {"index" :fn-handle-index "view" :fn-handle-view} ;"** just names no extensions!!! **"
                                } 
                    "no content" {}} 
           :config "config.xml file" 
           :dir "java.util.File"}
 "shop" {}}
)

;; this implementation currently has hardcoded paths for template files and and clj-handlers ;; can be exported to external config file
(def plugins 
  (into {} 
    (map (fn [plugin] 
           (hash-map (.getName plugin) ;; shop
             (hash-map :templates (into {} 
              (map (fn [file]
                (let [fname (.getName file)] ;; config.xml, products, cart
                  (hash-map fname
                      (let [f-list (.listFiles file)]
                       (hash-map :views
                        (into {} (map (fn [tmpl-file]
                                        (let [v-name (subs (.getName tmpl-file) 0 (- (.length (.getName tmpl-file)) (.length "html") 1))
                                              handler (str "sitnoseckana.views.templates.defaultsite." (.getName plugin) "." fname ".handler")]
                                          (require (symbol handler))

                                          (hash-map v-name (ns-resolve *ns* (symbol (str handler "/handle-" v-name))))))
                                     (filter #(.endsWith (.getName %) ".html") f-list)))))))) (filter #(.isDirectory %) (.listFiles plugin)))))))
                         (filter #(.isDirectory %) (.listFiles (java.io.File. (.getFile (clojure.java.io/resource  (str template-folder "/" site)))))))))

;; maybe items are better stored as trees, e.g. {id 123, name "asdf" ... parent {}, children [{}..{}]} extended map?
;(def pages (page-daf/load-all))


(defn resolve-page
  "finds template for provided vector of names and returns view to render"

  ([names lang] (resolve-page names lang (page-daf/load-all)))
  ([names lang pages]
   (loop [l-names names
          cur-page (page-daf/load-root lang)
          br-pt 0]
     (if (empty? l-names)                                    ;; end of url, all names found
       {:page cur-page
        :view (:kind cur-page)}
       (let [name (first l-names)
             pa   (page-url/find-page pages  cur-page name)]
         (if (nil? pa)                                       ; if no child we stop
           {:page  cur-page                                  ; have found page
            :br-pt br-pt                                     ;found breaking point
            :view  (cond
                     (and (zero? (try (Integer/parseInt name) (catch NumberFormatException nfe 0)))
                          (get-in plugins [(:type cur-page) :templates (:template cur-page) :views name] false) ) name           ; can't find pages and last name is not number and there is view with that name
                     (not (zero? (try (Integer/parseInt name) (catch NumberFormatException nfe 0)))) "view" ; we came to number so it is a view
                     :else "index"
                     )
            }
           (recur (rest l-names) pa (inc br-pt)))))))
  )

(defn handle-page-url-ok
  "Handles requests (e.g. /somePage/someSection.html).
  Splits the path on separator char '/', determines the language, finds the page and renders appropriate view"
  [ctx path]
    (let [splited-path (clojure.string/split path #"/")
          num-lang  (if-some [lang (some #{(first splited-path)} langs)]
                             {:lang lang
                              :names (rest splited-path)}
                             {:lang (first langs) ;; default lang is first in list
                              :names splited-path})
          page-data (resolve-page (:names num-lang) (:lang num-lang))
          locale (Locale/forLanguageTag "sr-Latn")
          page (:page page-data)
          view (:view page-data)
          rw-params (page-url/get-rw-params page-data splited-path)
          lang-code (:lang num-lang)
          root (page-daf/load-root lang-code)
          pages (page-url/mark-on-path (filter :published (page-daf/load-list :where (str "language_code='" lang-code "'") :ord "depth, ord")) page)
          pages (map (fn [page] (assoc page :url (page-url/create-url pages page)))
                     pages)
          navitems (map #(assoc % :url (page-url/create-url pages %)
                                  :title (:title %)) pages)
          is-home-page? (= (:id page) (:id root))
          handler-in {
                      :pages         pages
                      :is-home-page? is-home-page?
                      :nav-prim      (filter :primary_navigation navitems)
                      :page page
                      }
          ]
;(println "getin: " [(:type page) :templates (:template page) :views view])
;(println "handle page ok: " (get-in plugins [(:type page) :templates (:template page) :views view]) )
;(println "parser/render-file: " (str site "/" (:type page) "/" (:template page) "/" view page-suffix))
          (parser/render-file (str site "/" (:type page) "/" (:template page) "/" view page-suffix)
            ((get-in plugins [(:type page) :templates (:template page) :views view]) (merge ctx handler-in rw-params) ))))

(defresource page [path]
         :handle-ok (fn [ctx]
                      (handle-page-url-ok ctx path))
         :available-media-types ["text/html"])
(defroutes page-routes
           (ANY "/" [] (page ""))
           (ANY ["/*.html"] {{path :*} :route-params} (page path)))

