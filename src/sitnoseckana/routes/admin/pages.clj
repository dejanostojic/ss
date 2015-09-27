(ns sitnoseckana.routes.admin.pages
  (:require [compojure.core :refer :all]
            [liberator.core :refer [defresource resource request-method-in]]
            [liberator.representation :refer [ring-response]]
            [selmer.parser :as parser]
            [sitnoseckana.models.page :as page-daf]
            [sitnoseckana.app.langs :as langs]
            [sitnoseckana.routes.admin :as admin]
            [sitnoseckana.routes.platform-metadata :as platform]
            [ring.util.response :refer [redirect]]
            [sitnoseckana.util.web :as web]))

;(defresource entry-resource [id]
;  :allowed-methods [:get :put :delete]
;  :known-content-type? #(check-content-type % ["application/json"])
;  :exists? (fn [_]
;             (let [e (get @entries id)]
;                    (if-not (nil? e)
;                      {::entry e})))
;  :existed? (fn [_] (nil? (get @entries id ::sentinel)))
;  :available-media-types ["application/json"]
;  :handle-ok ::entry
;  :delete! (fn [_] (dosync (alter entries assoc id nil)))
;  :malformed? #(parse-json % ::data)
;  :can-put-to-missing? false
;  :put! #(dosync (alter entries assoc id (::data %)))
;  :new? (fn [_] (nil? (get @entries id ::sentinel))))

(defresource page [id ajax]
             :available-media-types ["application/json" "text/html"]
             :allowed-methods [:get :put :delete :post]
             :exists? (fn [ctx]
                        (let [page ((:load-by-pk page-daf/page-daf) (Long/parseLong id))] ;(base-daf/load-by-pk page-daf/tbl-settins id)
                          (if-not (nil? page)
                            {:page page})))
             :post! (fn [ctx]
                      (println "post req:" (:request ctx))
                      ((:insert page-daf/page-daf) (web/keywordize-request (:form-params (:request ctx)) page-daf/page-sett))
                      ;((:insert page-daf/page-daf) (:form-params (:request ctx)))
                      )

             :put! (fn [ctx]

                     (println "put req:" (web/keywordize-request (:form-params (:request ctx)) page-daf/page-sett) "\n\n\n")

                     ((:update page-daf/page-daf) (web/keywordize-request (:form-params (:request ctx)) page-daf/page-sett))

                     )
             :new? false

             :handle-no-content (fn [_]
                                  "no content")

             :delete! #((:delete page-daf/page-daf) (:page %))

             :handle-ok (fn [ctx]
                          (parser/render-file (str admin/site "/" admin/theme "/" "pages/index" (if ajax "-ajax") admin/page-suffix)
                                              (-> ctx
                                                  web/assoc-admin-keys
                                                  (assoc :pages (if-not ajax
                                                                  (let [pages (page-daf/load-list :where (str "language_code='" (get-in ctx [:page :language_code]) "' and depth=1") :ord "depth, ord")]
                                                                    {:list pages
                                                                     :root-page (page-daf/load-root (get-in ctx [:page :language_code]))})
                                                                  nil)

                                                         :page-title (str "Pages: " (get-in ctx [:page :title]))
                                                         ;                                          :plugin-names (platform/plugin-names)
                                                         ;                                          :template-names (platform/template-names (get-in ctx [:page :type]))
                                                         ;                                          :views (platform/views (get-in ctx [:page :type]) (get-in ctx [:page :template]))
                                                         :platform-metadata-json (cheshire.core/generate-string @platform/plugin-map)))
                                              ))
             :handle-not-found (fn [_] (str "not found page with id: " id)))




(defresource test-res []
             :available-media-types ["application/json" "text/html"]
             :allowed-methods [:post :put]
             :post! (fn [ctx]
                      (println "put test req:" (clojure.pprint/pprint (:request ctx)) )
                      (println "keywordized req:" (web/keywordize-request (:form-params (:request ctx)) page-daf/page-sett))
                      ;{:page ((:insert page-daf/page-daf) (web/keywordize-request (:form-params (:request ctx)) page-daf/page-sett))}
                      )
             :put! (fn [ctx]
                     (println "put test req:" (clojure.pprint/pprint (:request ctx)))
                     (println "keywordized req:" (clojure.pprint/pprint (web/keywordize-request (:form-params (:request ctx)) page-daf/page-sett)))
                     ;{:page ((:insert page-daf/page-daf) (web/keywordize-request (:form-params (:request ctx)) page-daf/page-sett))}
                     ))
(defroutes admin-pages-routes
           (ANY ["/test/admin/pages/*"] [_]  (test-res)  )
           (ANY ["/admin/pages/:lang"] [lang] (page  (str (:id (page-daf/load-root lang))) false)) ; /admin/pages/sr
           (ANY ["/admin/pages/:id/*"] [id] (page id false))
           (ANY ["/admin/pages"] [] (page "0" false))
           (ANY ["/admin/page-ajax/:id/*"] [id] (page id true)))
;; /admin/page/450/title
