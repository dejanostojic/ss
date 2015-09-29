(defproject sitnoseckana "0.1.0-SNAPSHOT"
  :description "Simple shop for food catering"
  :url "http://sitnoseckana.rs"
  :dependencies [ [org.clojure/clojure "1.7.0"]
                 [compojure "1.3.4" :exclusions [org.clojure/tools.reader clj-time]]
                 [selmer "0.8.0"]
                 [ring-server "0.4.0"]
                 [ring/ring-anti-forgery "1.0.0"]
                 [ring/ring-defaults "0.1.5"]
                 [mysql/mysql-connector-java "5.1.34"]
                 [j18n "1.0.2"]
                 [liberator "0.13"]
                 [cheshire "5.4.0"]
                 [org.clojure/core.cache "0.6.4"]
                 [buddy "0.5.4"]
                 [com.draines/postal "1.11.3"]
                 ;[clj-pdf "2.0.6"]
                 ]
  :plugins [[lein-ring "0.9.1"]]
  :ring {:handler sitnoseckana.handler/app
         :init sitnoseckana.handler/init
         :destroy sitnoseckana.handler/destroy
         :war-resources-path "templates"}
  :jvm-opts ["-Dfile.encoding=utf-8"]
  ;:aot :all
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.3.2"]]}})
