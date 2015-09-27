(ns sitnoseckana.routes.login
  (:require [compojure.core :refer [defroutes GET POST]]
            [sitnoseckana.routes.auth :as auth]
            ))

(defroutes front-login-routes
           (GET ["/dost/test/login"] [_]
             (fn [request]
                                       (let [session (request :session)]
                                         ;(println "SESSION: " session)
                                         {:status 200
                                          :headers {"content-type" "text/html"}
                                          :body (str "You've visited test page " request "\n\n\n"
                                                     "session: " (get-in request [:session ]))})))
           (GET ["/dost/logout"] [_]  auth/logout  )
           (POST "/dost/login" [] #(auth/login-authenticate %1 true)))