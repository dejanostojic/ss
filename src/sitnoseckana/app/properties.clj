(ns sitnoseckana.app.properties
  (:import (java.util Properties)))

(def prop-folder "properties/")
(def default-prop-name "platform")


(defn load-properties
  "Loads property in clojure map."
  ([prop-name] (load-properties prop-name prop-folder))
  ([prop-name location]
   (try
     (with-open [r (clojure.java.io/reader  (clojure.java.io/resource (str location prop-name ".properties"))  )]
       (let [props (Properties.)]
         (.load props r)
         (into {} (for [[k v] props] [(keyword k) v ]))))
     (catch java.lang.IllegalArgumentException e (do
                                                   (println (str "error loading properties file:"
                                                                 location prop-name ".properties" ":"
                                                                 (.getMessage e)))
                                                   {}))) ))


(def platform (load-properties default-prop-name))

#_(defn get-key
    "loads key from platform properties"
    ([key] (get (load-properties default-prop-name) key))
    ([key default] (if-let [val (get (load-properties default-prop-name) (if (keyword? key)
                                                                           key
                                                                           (keyword key)) )]
                     val
                     default)))

(defn get-key
  "loads key from platform properties"
  [key  & {:keys [prop folder default]
           :or {folder prop-folder
                prop default-prop-name}} ]
  (if-let [val (get (load-properties prop) (if (keyword? key)
                                             key
                                             (keyword key)))]
    val
    default))

