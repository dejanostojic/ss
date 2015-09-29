(ns sitnoseckana.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [compojure.route :as route]
            [sitnoseckana.routes.page :refer [page-routes]]
            [sitnoseckana.routes.admin :refer [admin-routes]]
            [sitnoseckana.routes.admin.pages :refer [admin-pages-routes]]
            [sitnoseckana.routes.admin.login :refer [admin-login-routes]]
            [sitnoseckana.routes.admin.shop :refer [shop-routes]]
            [sitnoseckana.routes.cart :refer [cart-routes]]
            [sitnoseckana.routes.platform-metadata :as platform-metadata]
            [ring.middleware.session.memory :refer [memory-store]]
            [selmer.parser :as parser]
            [liberator.dev :refer [wrap-trace]]
            [ring.middleware.defaults :refer :all]
            [sitnoseckana.middleware.core]
            [sitnoseckana.app.properties :as props]
            [sitnoseckana.routes.auth :refer [options]]
            [sitnoseckana.routes.admin.user :refer [user-routes]]
            [sitnoseckana.routes.order :refer [order-routes]]
            [sitnoseckana.routes.register :refer [register-routes]]
            [sitnoseckana.routes.login :refer [front-login-routes]]
            [buddy.auth.accessrules :refer [wrap-access-rules] ]
            [ring.middleware.stacktrace]
            [ring.adapter.jetty :refer [run-jetty]]))

(defn init []
  (println "sitnoseckana is starting in " (get props/platform "project_stage") " mode")
  (parser/set-resource-path! (clojure.java.io/resource  "sitnoseckana/views/templates"))
  (parser/cache-off!)
  (use 'sitnoseckana.views.selmer-tags) ;; evaluate all my custom selmer tags on server startup
  (platform-metadata/init-plugins) ;; initialize all plugins for application
  (println "selmer set-resource-path, cache turned off."))

(defn destroy []
  (println "sitnoseckana is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))



(def app
  (-> (routes front-login-routes register-routes order-routes shop-routes user-routes admin-login-routes admin-pages-routes admin-routes cart-routes page-routes app-routes)
                         (wrap-trace :header :ui)
                         (ring.middleware.stacktrace/wrap-stacktrace)
                         #_(wrap-defaults (-> site-defaults (assoc-in [:security :anti-forgery] true) (assoc-in [:session]  true)) ) ;; I can get token with (use 'ring.middleware.anti-forgery) anti-forgery/*anti-forgery-token*
      (wrap-access-rules options)
      (wrap-defaults site-defaults )))

(defn -main [& args]
  (let [ip (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_IP" "0.0.0.0")
        port (Integer/parseInt (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_PORT" "8080"))]
    (init)
    (run-jetty app {:host ip :port port})))
