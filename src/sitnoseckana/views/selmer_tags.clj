(ns sitnoseckana.views.selmer-tags
  (:require [sitnoseckana.app.langs :as langs]
            [selmer.parser :as parser]
            [sitnoseckana.util.date]))

;; fmt tag repleaces finds key (first argument) in bundle bound in context-map. If there are aditional params than formats string.
(parser/add-tag! :fmt
  (fn [args context-map]
    (if (empty? (rest args))
      (langs/get-key (:bundle context-map) (first args))
      (langs/get-key (:bundle context-map) (first args) (rest args)))))


; auto generate this!!! NOT WORKING
;{% for p-name in plugin-names %}
;<option  value="{{p-name.val}}" {% ifequal p-name.val "platform" %}selected="selected"{% endifequal %}>{{p-name.label}}</option>
;{% endfor %}

;; example usage {% options some-opts selected-opt-val %}

;; first arg is name of collection of {:val value, :label label} maps. 
;; second arg is value of option should be preselected with selected attribute 
(parser/add-tag! :options
  (fn [args context-map]
    (let [opts (get-in context-map (into [] (map #(keyword %) (clojure.string/split (nth args 0) #"\."))))
          test-val (get-in context-map (into [] (map #(keyword %) (clojure.string/split (nth args 1) #"\."))))]
      (reduce  #(str %1 %2) "" (map  #(str  "<option value=\"" (:val %) "\" " (if (= (:val %) test-val) "selected=\"selected\"") " >" (:label %) "</option>") opts)))))


(parser/add-tag! :format-date
                 (fn [args ctx]
                   (let [date (get-in ctx (mapv #(keyword %) (clojure.string/split (first args) #"\.")))
                         pattern (second args)
                         lang (if (= 3 (count args))
                                (nth args 2)
                                ;(get-in ctx [:page :language_code])
                                "ci"
                                )]
                     (sitnoseckana.util.date/format-date date pattern lang)
                     )))

(parser/add-tag! :contains?
                 (fn [args ctx content]

                   (let [val (keyword (first args))
                         coll (get-in ctx (into [] (map #(keyword %) (clojure.string/split (nth args 1) #"\."))))]
                     (println val "; " coll)
                     (if (contains? coll val)
                       content)))
                 :endcontains?)

(parser/add-tag! :csrf-token
          (fn [args context-map]
            (get-in context-map [:request :session :ring.middleware.anti-forgery/anti-forgery-token])))


(selmer.filters/add-filter! :format-date  (fn [date format lang] (sitnoseckana.util.date/format-date date format lang)))
(selmer.filters/add-filter! :get-time (fn [date] (.getTime date)))
(selmer.filters/add-filter! :bool-not (fn [bool] (not (java.lang.Boolean/parseBoolean bool))))
