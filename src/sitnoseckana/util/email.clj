(ns sitnoseckana.util.email
  (:require [postal.core]))

(def smtp {:host "smtp.gmail.com"
           :user "sitnoseckana@gmail.com"
           :pass "k3l3r@b3suzdr@v3"
           :tls true
           :port 25})

(defn send-email
  "sends email  with or without attachment
  {:type :inline
  :content (java.io.File. \"/tmp/a.pdf\")
  :content-type \"application/pdf\"}"
  [{:keys [from to subject body]}]
  (-> (java.lang.Thread.
        (fn []
          (try (postal.core/send-message smtp
                                         {:from from
                                          :to to
                                          :subject subject
                                          :body body
                                          })
               (catch Exception e (println (.getMessage e)))))) .start))
