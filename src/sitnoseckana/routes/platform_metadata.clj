(ns sitnoseckana.routes.platform-metadata)

(def plugin-list (atom []))
(def plugin-map (atom  {}))

;; plugin example:

;{
; :type "type-n"
; :menu-name "name-n"
; :menu-ord "n"
; :rw-rules {"template" {"view" "paramName"}} ;; e.g. {"*" {"*" "productId"}} 
; :admin-privileges {}
; :member-privileges {}
; }

(def user-roles [:admin :member])

(defn add-plugin
  "adds new plugin in plugin map"
  [plugin]
  (swap! plugin-list conj plugin)
  (swap! plugin-map assoc (:type plugin) plugin))

(defn init-plugins
  "initialize application plugins"
  []
  (add-plugin {
               :type "platform"
               :templates {"home" ["index"], "homeberg" ["index"]}
               :menu-name nil
               :menu-ord 0
               :rw-rules nil
               :admin-privileges {}
               :member-privileges {}
               })
  (add-plugin {
               :type "app"
               :menu-name "Pages"
               :templates {"page" ["index"], "newsletter" ["index"]}
               :menu-ord 1
               :rw-rules nil 
               :admin-privileges {}
               :member-privileges {}
               })
    (add-plugin {
               :type "shop"
               :menu-name "Shop"
               :templates {"cart" ["index"], "products" ["index" "view"] "orders" ["index" "view"]}
               :menu-ord 2
               :rw-rules {"orders" {"view" :order-id}} ;; e.g. {"*" {"*" "productId"}}
               :admin-privileges {}
               :member-privileges {}
               })
  (add-plugin {
               :type "user"
               :menu-name "User"
               :templates {"registration" ["index" "potvrdi-nalog"]}
               :menu-ord 2
               :rw-rules nil
               :admin-privileges {}
               :member-privileges {}
               })
  )
;(init-plugins)
(defn make-html-options
  "helper function that formates option-list in {:val value, :label label} format"
  ([option-list]
  (map #(hash-map :val %, :label %) option-list))
  ([option-list select]
    (into (list select) (make-html-options option-list))))

(defn plugin-names
  "returns list of plugin names"
  []
  (make-html-options (keys @plugin-map)))

(defn template-names
  "returns list of template names for plugin"
  [plugin-name]
  (make-html-options (distinct (concat (keys (:templates (get @plugin-map plugin-name))) (keys (:templates (get @plugin-map "app")))))))

(defn views
  "returns views for provided template name"
  [plugin-name template-name]
  (let [res (-> @plugin-map (get plugin-name) :templates (get template-name))]
    (if res
      (make-html-options res)
      (-> @plugin-map (get "app") :templates (get template-name) make-html-options)))) ;; to do: Return index if choosen combo like shop page


