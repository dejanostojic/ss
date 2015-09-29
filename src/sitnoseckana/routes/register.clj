(ns sitnoseckana.routes.register
      (:require [compojure.core :refer [defroutes GET POST ANY DELETE]]
                [cheshire.core]
                [compojure.response]
                [sitnoseckana.models.user :as user]
                [sitnoseckana.models.shop-order :as order]
                [sitnoseckana.models.query-utils :as query-utils]
                [sitnoseckana.util.date]
                [sitnoseckana.models.user-role]
                [buddy.hashers.bcrypt]
                [ring.util.response :as resp]))


(defn send-confirmation-email
  "sends confirmation email to user"
  [req user]
  (when user
    (sitnoseckana.util.email/send-email {:from "sitnoseckana@gmail.com"
                                         :to (:email user)
                                         :subject "potvrdite SitnoSeckana registraciju"
                                         :body [{:type "text/html"
                                                 :content (str "<p>Potvrdite svoj nalog klikom na <a href=\"http://" (:server-name req) ":" (:server-port req) "/dost/register/confirmation?email=" (user :email) "&activation_key=" (user :activation_key) "\">sledeći link!</a></p>"
                                                               "<p>kopirajte link u browser ako imate problema:" "http://" (:server-name req) ":" (:server-port req) "/dost/register/confirmation?email=" (user :email) "&activation_key=" (user :activation_key) "</p>" ) }] })) )

(defn register-after-order[req]
  (let [pass (get-in req [:params :password])
        user (get-in req [:session :user-ordered])
        uuid (str (java.util.UUID/randomUUID))]
    (if-not (clojure.string/blank? pass)
      (when (-> user nil? not)
        (do
          (send-confirmation-email req ((user/daf :update) (assoc  user :password (buddy.hashers.bcrypt/make-password pass 12)
                                                                        :activation_key uuid
                                                                        :registration_date (java.sql.Date. (System/currentTimeMillis))) "password,registration_date,activation_key"))
          {:status 200
           :headers {"Content-Type" "application/json;  charset=utf-8"},
           :body (cheshire.core/generate-string {:resp "ok"})}))
      {:status 422
       :headers {"Content-Type" "application/json;  charset=utf-8"},
       :body (cheshire.core/generate-string {:resp "obavezna sifra"})})))

(defn email-confirmation-post [req]
  (let [email (get-in req [:params :email])
        activation_key (get-in req [:params :activation_key])
        user (if (or (clojure.string/blank? email) (clojure.string/blank? activation_key))
               nil
               (first ((user/daf :load-list) :where (str "email=" (query-utils/mysql-str email ) " and activation_key=" (query-utils/mysql-str activation_key )) :limit 1  )))]

    (if user
      (do
        ((user/daf :update) (assoc  user :activated true :activation_key "") "activated,activation_key")
        (when (query-utils/for-first
                (str "select count(*) as cnt from user_role where user_id=" (:id user))
                #(when (= 0 (.getInt %1 1))
                  ((sitnoseckana.models.user-role/daf :insert) {:user_id (:id user)
                                                                :role_name "member"}))))))
    (println "TODO SHOW MESSAGE when activating acount!")          ; TODO show message
    (resp/redirect (str (sitnoseckana.routes.page-url/order-view-url (:id req))))))

    (defroutes register-routes
           (POST ["/dost/register/after-order"] [] (fn [req] (register-after-order req)))

           (GET ["/dost/register/confirmation"] []      ;; TODO load page based on template and type eq: type=user, template=registration, kind=potvrdi-nalog
             (fn [req]
               (resp/redirect (str "/potvrdi-nalog.html?" (:query-string req)) )))

           (POST ["/dost/register/confirmation"] [] (fn [req] (email-confirmation-post req)))

           (POST ["/dost/register/resend-conf-email"] [] (fn [req]
                                                            (send-confirmation-email req (first ((user/daf :load-list) :where (str "email=" (query-utils/mysql-str (get-in req [:params :email]) )))))
                                                           {:status 200
                                                            :body "email sent"}))

           )
