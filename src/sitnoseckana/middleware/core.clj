(ns sitnoseckana.middleware.core
  (:import (java.util ResourceBundle Locale)))

(defn wrap-reload-bundles [app]
  (fn [req]
    (println "RELOAD BUDNLES MIDDLEWARE!!!")
    (app req)))
