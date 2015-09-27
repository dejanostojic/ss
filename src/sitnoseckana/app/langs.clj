(ns sitnoseckana.app.langs
  (:require ;[taoensso.tower :as tower :refer (with-tscope)]
            [j18n.core :as res]
            [sitnoseckana.app.properties :as props])
  (:import (java.util ResourceBundle Locale)
           java.text.MessageFormat))

(defn get-bundle
  "Site param is :fp or :admin depending of witch route were hit."
  [site lang]
  (if (= site :admin)
    (ResourceBundle/getBundle "langs.admin.AdminResources" (Locale. lang))
    (ResourceBundle/getBundle "langs.front.FrontPageResources" (Locale. lang))))

(def bundles {:fp {"en" (ResourceBundle/getBundle "langs.front.FrontPageResources" (Locale. "en"))
                   "sr" (ResourceBundle/getBundle "langs.front.FrontPageResources" (Locale. "sr"))
                   "rs" (ResourceBundle/getBundle "langs.front.FrontPageResources" (Locale. "rs"))}
              :admin {"en" (ResourceBundle/getBundle "langs.admin.AdminResources" (Locale. "en"))}})


(defn get-key
  "Takes ResourceBundle and search key as arguments and returns value of provided key in bundle. If key is absent than returns ???key???"
  ([^ResourceBundle bundle key]
  (let [val (res/resource key bundle)]
    (if val
      val
      (str "???" key "???"))))
  ([^ResourceBundle bundle key args]
  (let [val (get-key bundle key)]
    (MessageFormat/format val (to-array args)))))

