(ns sitnoseckana.routes.admin.login
  (:require [compojure.core :refer [defroutes GET POST]]
            [sitnoseckana.routes.auth :as auth]
            ))


(defn handler [request]
  (let [session (request :session)
        counter (inc (session :counter 0)) ]
    (println "SESSION: " session)
    {:status 200
     :headers {"content-type" "text/plain"}
     :session (assoc session :counter counter)
     :body (str "You've visited this page " counter  " times!\nUser: " (:user-data session "not logged in") )}))

(defn handler-show [request]
  (let [session (request :session)
        counter (session :counter 0)]
    (println "SESSION: " session)
    {:status 200
     :headers {"content-type" "text/html"}
     :body (str "You've visited test page " counter " times!")}))



(defroutes admin-login-routes
           (GET ["/admin/logout"] [_]  auth/logout  )
           (POST "/admin/login" [] auth/login-authenticate))

