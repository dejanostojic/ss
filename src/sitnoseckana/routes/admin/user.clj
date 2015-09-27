(ns sitnoseckana.routes.admin.user
  (:require [sitnoseckana.util.web :as web]
            [compojure.core :refer [defroutes ANY GET]]
            [liberator.core :refer [defresource resource request-method-in]]
            [sitnoseckana.routes.admin :as admin]
            [selmer.parser :as parser]
            [sitnoseckana.models.user :as user]
            [sitnoseckana.models.user-role :as user-role]
            [buddy.hashers.bcrypt]
            [sitnoseckana.routes.platform-metadata :refer [user-roles]]))

(defresource user-form [user-id]
             :available-media-types ["text/html"]
             :allowed-methods [:get :put :delete]
             :exists? (fn [_]
                        (println "exists: " user-id )
                        (let [u ((user/daf :load-by-pk) (if (number? user-id) user-id (Long/parseLong user-id)) )]
                          (if-not (nil? u)
                            {:user u})))

             :put! (fn [ctx]
                     (println "req: " )
                     (clojure.pprint/pprint (web/keywordize-request (:form-params (:request ctx)) user/dao-settings))
                     ((:update user/daf) (update-in (web/keywordize-request (:form-params (:request ctx)) user/dao-settings) [:password] (fn [pass] (buddy.hashers.bcrypt/make-password pass 12)) ) "email,activated,activation_key" )
                     )
             :new? false

             :handle-no-content (fn [_] "no content")

             :delete! #((:delete user/daf) (:user %))

             :handle-ok (fn [ctx]
                          #_(println "ctx: " (as-> (update ctx :user (fn [u] (assoc u :user-roles ((user-role/daf :load-list) :where (str "user_id=" (:id u)))))) ctx2

                                                 (assoc ctx2 :page-title (str "User: " (get-in ctx2 [:user :user_name]))
                                                                    :user-roles user-roles
                                                                    :roles (map #(:role_name %) (get-in ctx2 [:user :user-roles]))
                                                                    :is-admin (some #{"admin"} (map #(:role_name %) (get-in ctx2 [:user :user-roles])))
                                                                    :is-member (some #{"member"} (map #(:role_name %) (get-in ctx2 [:user :user-roles])))
                                                                    :user-user-roles (get-in ctx2 [:user :user-roles])
                                                                    :role-maped (map #(:role_name %) (get-in ctx2 [:user :user-roles]))
                                                                    ;(some #{"admin"} (map (fn [x] (:role_name x) (get-in ctx [:user :user-roles]))))
                                                                    ;:is-member (some #{"member"} (map (fn [x] (:role_name x) (get-in ctx [:user :user-roles]))))
                                                                    ))

                                       )
                          #_(as-> (update ctx :user (fn [u] (assoc u :user-roles ((user-role/daf :load-list) :where (str "user_id=" (:id u)))))) ctx2

                                (assoc ctx2 :page-title (str "User: " (get-in ctx2 [:user :user_name]))
                                            :user-roles user-roles
                                            :roles (map #(:role_name %) (get-in ctx2 [:user :user-roles]))
                                            :is-admin (some #{"admin"} (map #(:role_name %) (get-in ctx2 [:user :user-roles])))
                                            :is-member (some #{"member"} (map #(:role_name %) (get-in ctx2 [:user :user-roles])))
                                            :user-user-roles (get-in ctx2 [:user :user-roles])
                                            :role-maped (map #(:role_name %) (get-in ctx2 [:user :user-roles]))
                                            ;(some #{"admin"} (map (fn [x] (:role_name x) (get-in ctx [:user :user-roles]))))
                                            ;:is-member (some #{"member"} (map (fn [x] (:role_name x) (get-in ctx [:user :user-roles]))))
                                            ))

                          (parser/render-file (str admin/site "/" admin/theme "/user/form" admin/page-suffix)
                                              (let [id (println "id: " (:id (:user ctx)))
                                                    roles ((user-role/daf :load-list) :where (str "user_id=" (:id (:user ctx))))]
                                                (-> ctx
                                                    web/assoc-bundle
                                                    web/assoc-anti-forgery
                                                    (update :user (fn [u] (assoc u :user-roles roles)))
                                                    (assoc :page-title (str "User: " (get-in ctx [:user :user_name]))
                                                           :user-roles user-roles
                                                           :is-admin (some #{"admin"} (map #(:role_name %) roles))
                                                           :is-member (some #{"member"} (map #(:role_name %) roles))
                                                           ;(some #{"admin"} (map (fn [x] (:role_name x) (get-in ctx [:user :user-roles]))))
                                                           ;:is-member (some #{"member"} (map (fn [x] (:role_name x) (get-in ctx [:user :user-roles]))))
                                                           )

                                                    ))

                                              ))

             :handle-not-found (fn [ctx]
                                 (parser/render-file (str admin/site "/" admin/theme "/user/form" admin/page-suffix)
                                                     (-> ctx
                                                         web/assoc-bundle
                                                         web/assoc-anti-forgery
                                                         (assoc :page-title "New user"))
                                                     ))
             )

(defresource user-list [ajax]
             :available-media-types ["text/html"]
             :allowed-methods [:get :post]

             :post! (fn [ctx]
                      (println "req: " )
                      (clojure.pprint/pprint (web/keywordize-request (:form-params (:request ctx)) user/dao-settings))
                      (let [user-id (:id ((:insert user/daf) (update-in (web/keywordize-request (:form-params (:request ctx)) user/dao-settings) [:password] (fn [pass] (buddy.hashers.bcrypt/make-password pass 12)) ) ))
                            is-admin (web/vec-to-bool (get-in ctx [:request :form-params "admin"]))
                            is-member (web/vec-to-bool (get-in ctx [:request :form-params "member"]))]
                        (if is-admin
                          ((user-role/daf :insert) {:id 0 :user_id user-id :role_name "admin"}))
                        (if is-member
                          ((user-role/daf :insert) {:id 0 :user_id user-id :role_name "member"}))
                          )


                      )

             :handle-ok (fn [ctx]
                          (parser/render-file (str admin/site "/" admin/theme "/user/list" (if ajax "-ajax") admin/page-suffix)
                                              (-> ctx
                                                  web/assoc-bundle
                                                  web/assoc-anti-forgery
                                                  (assoc :page-title (str "Users" )
                                                         :user-list ((:load-list user/daf))))
                                              ))
             )

(defroutes user-routes
           (ANY ["/admin/user"] [] (user-list false))
           (ANY ["/admin/user/new"] [] (user-form 0))
           (ANY ["/admin/user/:id/:username"] [id username] (user-form id))
           )