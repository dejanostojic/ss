(ns sitnoseckana.util.web
  (:require [ring.middleware.anti-forgery :as anti-forgery]
            [sitnoseckana.app.langs :as langs]))


(defn add-anti-forgery-bundle
          "Associates resource bundle and anti forgery tocken in contex map"
          ([ctx] (add-anti-forgery-bundle ctx "en"))
          ([ctx lang]
            (assoc ctx :bundle (get-in langs/bundles [:admin lang])
                       :x-csrf-token anti-forgery/*anti-forgery-token*)))

(defn assoc-bundle
  "Adds :bundle key in contex map"
  ([ctx] (assoc-bundle ctx "en"))
  ([ctx lang]                                               ;(assoc ctx :bundle (get-in langs/bundles [:admin lang]))
    (assoc ctx :bundle (langs/get-bundle :admin lang))
    ))

(defn assoc-anti-forgery
  "Adds anti forgery tocken :x-csrf-token in contex map"
  [ctx] (assoc ctx :x-csrf-token anti-forgery/*anti-forgery-token*))

(defn assoc-admin-keys
  "Adds resource bundle, anti forgery tocken and user data"
  ([ctx] (assoc-admin-keys ctx "en"))
  ([ctx lang]
   (println "assoc admin data: " (get-in ctx [:request :session :user-data]))
   (-> ctx
       (assoc-bundle lang)
       (assoc-anti-forgery)
       (assoc :user-data (get-in ctx [:request :session :user-data])))))

(defn parse-bool
  "Parses string and returns boolean value. Returns true if string is \"true\", else returns false."
  [string]
  (if string
    (try (java.lang.Boolean/parseBoolean string)
         (catch java.lang.Exception _ false))
    false))

(defn vec-to-bool
  "Takes input from form checkbox (hidden input with false val, and true value from checbox if checked) and returns bool"
  [value]
  (if (vector? value)
    (reduce #(or %1 %2) (map #(parse-bool %) value))
    (if (string? value)
      (parse-bool value)
      false))
  )

(defn keywordize-request
  "Takes entity (eg. from request map) and dao-settings.
  Converts keys from strings to keywords, and also converts values from strings to correct type with :convert function"
  [req-map dao-settings]
  (reduce-kv #(assoc %1 (keyword %2) ((fn [name val]
                                        (let [conv (:convert (first (filter (fn [fst] (= name (:name fst))) (:fields dao-settings))) )]
                                          (if (fn? conv)
                                            (do (println "keywordize: " name ": before conv:" (type val) ", after: " (type (conv val))) (conv val) )
                                            (do (println "else!!!!") val)
                                            ))  ) %2 %3)) {} req-map))

