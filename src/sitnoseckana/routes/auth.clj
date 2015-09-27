(ns sitnoseckana.routes.auth
  (:require [buddy.auth :refer [authenticated?]]
            [buddy.auth.accessrules :refer [success error]]
            [ring.util.response :refer [response redirect content-type]]
            [sitnoseckana.models.user :as user]
            [buddy.hashers.bcrypt :refer [check-password make-password]]
            [sitnoseckana.models.query-utils :refer [mysql-str]]
            [sitnoseckana.models.user-role :as user-role]))


(defn authenticated-access
  [request]
  (if (authenticated? request)
    true
    (error "Only authenticated users allowed")))

(def user-roles
  {:admin :member})

(defn get-user-roles
  [request]
  (get-in request [:session :user-data :roles]))

(defn is-admin?
  [request]
  (contains? (get-user-roles request) :admin))

(defn admin-access
  [request]
  (if (is-admin? request)
    true
    (error "Only admin users allowed")))

(defn any-access
  [request]
  true)

(def rules [{:pattern #"^/admin/login$"
             :handler any-access}
            {:pattern #"^/admin/.*"
             :handler {:or [admin-access]}}
            {:pattern #"^/.*"
             :handler any-access}])

;; Logout handler
;; Responsible for clearing the session.

(defn logout
  [request]
  (-> (redirect "/admin/login")
      (assoc :session {})))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Authentication                                   ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def roles
  {:admin #{:admin}
   :test #{:member}})

(def authdata
  "Global var that stores valid users with their
   respective passwords."
  {:admin "secret"
   :test "secret"})

;; Authentication Handler
;; Used to respond to POST requests to /login.
;; TODO REFACTOR! To many stuff for this function!!!
(defn login-authenticate
  "Check request username and password against authdata username and passwords.
  On successful authentication, set appropriate user into the session and
  redirect to the value of (:query-params (:next request)).
  On failed authentication, renders the login page."
  [request & [ajax-login] ]
  (let [username (get-in request [:form-params "username"])
        password-guess (get-in request [:form-params "password"])
        session (:session request)
        us (first ((user/daf :load-list) :where (str "user_name=" (mysql-str username) " and activated=1" )  :limit 1))]
    (println "login authenticate called" username password-guess)
    ;(println "session: " session)
    (when us
      (println "--------DA LI VALJA SIFRA:" (check-password password-guess (or (:password us) "no pass") )))
    (if  (and us (check-password password-guess (:password us) ))
      (let [next-url (get-in request [:query-params :next] (if ajax-login
                                                             false
                                                             "/admin") )
            roles (if us
                    (into #{} (map #(keyword (:role_name  %)) ((user-role/daf :load-list) :where (str "user_id=" (:id us)) ))) )
            updated-session (assoc session :user-data {:username username :roles  roles :user (dissoc us :password)}  )]

        (if ajax-login
          {:status 200
           :headers {"Content-Type" "application/json;  charset=utf-8"},
           :body (cheshire.core/generate-string {:resp "logged-in" :user-data {:username username :roles  roles :user (dissoc us :password)}}) ;; TODO duplication. Same above! Fix later
           :session updated-session}
          (-> (redirect next-url)
              (assoc :session updated-session))))
      (if ajax-login
        {:status 401
         :headers {"Content-Type" "application/json;  charset=utf-8"},
         :body (cheshire.core/generate-string {:resp "wrong-guess"})}
        (redirect "/admin/login"))
      )))

(defn on-error
  [request value]
  (redirect "/admin/login"))

(def options {:rules rules :on-error on-error})