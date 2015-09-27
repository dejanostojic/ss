(ns sitnoseckana.app.properties
  (:import (java.util Properties)))

(def prop-folder "resources/properties/")

(defn load-properties
  "Loads property in clojure map."
  ([prop-name] (load-properties prop-name prop-folder))
  ([prop-name location]
   (with-open [r (clojure.java.io/reader  (str prop-folder "platform.properties") )]
     (let [props (Properties.)]
       (.load props r)
       props))))

(def platform (load-properties "platform"))
