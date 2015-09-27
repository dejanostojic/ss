(ns sitnoseckana.views.templates.defaultsite.app.newsletter.handler
  (:require [postal.core]
            [sitnoseckana.models.shop-menu :as shop-menu]
            [sitnoseckana.models.shop-product-type-variation :as shop-product-type-variation]
            [clj-pdf.core :refer [pdf]]
            )
  (:use clj-pdf.core))

(defn send-email []
  "sends dumb email to ostdejan@gmail.com"
  (postal.core/send-message {:host "smtp.gmail.com"
                             :user "sitnoseckana@gmail.com"
                             :pass "k3l3r@b3suzdr@v3"
                             :tls true
                             :port 25}
                            {:from "sitnoseckana@gmail.com"
                             :to "ostdejan@gmail.com"
                             :subject "Hi from sitnoseckana!"
                             :body [{:type "text/html"
                                     :content (slurp "http://localhost:8080/newsletter.html")}] }))

(def jela [{:ingridiants-presan "presni sastojci 1", :ingridiants-kom "kombin sastojci 1", :ingridiants-voc "voc sastojci 1", :day "P"}
           {:ingridiants-presan "presni sastojci 2", :ingridiants-kom "kombin sastojci 2", :ingridiants-voc "voc sastojci 2", :day "U"}
           {:ingridiants-presan "presni sastojci 3", :ingridiants-kom "kombin sastojci 3", :ingridiants-voc "voc sastojci 3", :day "S"}
           {:ingridiants-presan "presni sastojci 4", :ingridiants-kom "kombin sastojci 4", :ingridiants-voc "voc sastojci 4", :day "4"}
           {:ingridiants-presan "presni sastojci 4", :ingridiants-kom "\u042f \u043b\u044e\u0431\u043b\u044e \u0442\u0435\u0431\u044f", :ingridiants-voc "voc sastojci 5", :day "P"}
           ])

(def table-template
  (template
    [$ingridiants-presan
     $ingridiants-kom
     $ingridiants-voc
     $day ]))

(comment
  (pdf
    [{:font {:encoding :unicode
             :ttf-name "fonts/arialuni.ttf"}}
     [:table {
              :border true
              :widths [5 5 5 1]
              :header [{:backdrop-color [100 100 100] :encoding :unicode} "Meni za nedelju bla bla хахахахах 00"]}
      ["пресни оброци 290 дин / 160 дин" "КОМБИНОВАНИ ОБРОЦИ 310 дин /170 дин" "Воћни оброци 290 дин / 160 дин" "дан"]

      (nth (table-template jela) 0)
      (nth (table-template jela) 1)
      (nth (table-template jela) 2)
      (nth (table-template jela) 3)
      (nth (table-template jela) 4)
      ]]
    "/home/dostojic/Documents/clojure/doc.pdf")

  (clj-pdf.core/pdf
    [{:font {:encoding :unicode
             :ttf-name "fonts/arialuni.ttf"}}               ; :ttf-name is path to ttf.file
     [:phrase "тест 123"]
     [:phrase "нека ћирилица мало"]]
    "/home/dostojic/Documents/clojure/doc123.pdf")

  )




(defn a []
  (pdf
    [{}
     [:table {
              :border true
              :widths [5 5 5 1]
              :header [{:backdrop-color [100 100 100]} "Meni za nedelju bla bla"]}
      ["пресни оброци 290 дин / 160 дин" "КОМБИНОВАНИ ОБРОЦИ 310 дин /170 дин" "Воћни оброци 290 дин / 160 дин" "дан"]
      ["***
    marinirana sirova cvekla,
šargarepa, kupus, rukola, sušena
crna ribizla, indijski orah, seme
bundeve, zrno ovsa, dresing od
senfa, jabukovog sirćeta,
maslinovog ulja"
       "***
  crveno sočivo, zelena salata,
  crveni kupus, koren peršuna,
  keleraba, zrno ječma, seme
  suncokreta, laneno seme, tahini
  dresing"
       "***
  integralni pirinač, pirinčano
  mleko, badem, brusnica, rogač,
  cimet, med, rum" "P"]

      ["***
    marinirana sirova cvekla,
šargarepa, kupus, rukola, sušena
crna ribizla, indijski orah, seme
bundeve, zrno ovsa, dresing od
senfa, jabukovog sirćeta,
maslinovog ulja"
       "***
  crveno sočivo, zelena salata,
  crveni kupus, koren peršuna,
  keleraba, zrno ječma, seme
  suncokreta, laneno seme, tahini
  dresing"
       "***
  integralni pirinač, pirinčano
  mleko, badem, brusnica, rogač,
  cimet, med, rum" "U"]
      ["***
    marinirana sirova cvekla,
šargarepa, kupus, rukola, sušena
crna ribizla, indijski orah, seme
bundeve, zrno ovsa, dresing od
senfa, jabukovog sirćeta,
maslinovog ulja"
       "***
  crveno sočivo, zelena salata,
  crveni kupus, koren peršuna,
  keleraba, zrno ječma, seme
  suncokreta, laneno seme, tahini
  dresing"
       "***
  integralni pirinač, pirinčano
  mleko, badem, brusnica, rogač,
  cimet, med, rum" "S"]
      ["***
    marinirana sirova cvekla,
šargarepa, kupus, rukola, sušena
crna ribizla, indijski orah, seme
bundeve, zrno ovsa, dresing od
senfa, jabukovog sirćeta,
maslinovog ulja"
       "***
  crveno sočivo, zelena salata,
  crveni kupus, koren peršuna,
  keleraba, zrno ječma, seme
  suncokreta, laneno seme, tahini
  dresing"
       "***
  integralni pirinač, pirinčano
  mleko, badem, brusnica, rogač,
  cimet, med, rum" "Č"]
      ["***
    marinirana sirova cvekla,
šargarepa, kupus, rukola, sušena
crna ribizla, indijski orah, seme
bundeve, zrno ovsa, dresing od
senfa, jabukovog sirćeta,
maslinovog ulja"
       "***
  crveno sočivo, zelena salata,
  crveni kupus, koren peršuna,
  keleraba, zrno ječma, seme
  suncokreta, laneno seme, tahini
  dresing"
       "***
  integralni pirinač, pirinčano
  mleko, badem, brusnica, rogač,
  cimet, med, rum" "P"]]]
    "/home/dostojic/Documents/clojure/doc.pdf"))

(defn handle-index [ctx]
  (assoc ctx :page-attrs {:author     "Pera Peric" :author-role "admin"
                          :menu-items (let [variations ((shop-product-type-variation/daf :load-list))
                                            monday (java.sql.Date. (-> (.getTime (doto (java.util.Calendar/getInstance) (.set java.util.Calendar/DAY_OF_WEEK java.util.Calendar/MONDAY))) (.getTime)))
                                            friday (java.sql.Date. (-> (.getTime (doto (java.util.Calendar/getInstance) (.set java.util.Calendar/DAY_OF_WEEK java.util.Calendar/FRIDAY))) (.getTime)))]
                                        (map (fn [prod] (assoc-in prod [:shop_product :shop_product_type :variations] (filter #(if (= (:type_id %) (get-in prod [:shop_product :shop_product_type :id])) true) variations)) )
                                             (sitnoseckana.models.base-daf/with-join shop-menu/dao-settings
                                                                                     :join [{
                                                                                             :from sitnoseckana.models.shop-product/dao-settings
                                                                                             :fs ["name" "id" "ingredients"]
                                                                                             :on ["product_id" "id"]
                                                                                             :join {
                                                                                                    :from sitnoseckana.models.shop-product-type/dao-settings
                                                                                                    :fs ["name" "id"]
                                                                                                    :on ["product_type_id" "id"]
                                                                                                    }
                                                                                             }]
                                                                                     :where (str "date>={d '" monday "'} and date<={d '" friday "'}") :ord "date asc")) )}
             :juices (sitnoseckana.models.base-daf/with-join sitnoseckana.models.shop-product/dao-settings
                                                             :join [{
                                                                     :from sitnoseckana.models.shop-product-type/dao-settings
                                                                     :fs ["name" "id"]
                                                                     :on ["product_type_id" "id"]
                                                                     }]
                                                             :where "product_type_id=16" :ord "product_type_id asc")
             :shakes (sitnoseckana.models.base-daf/with-join sitnoseckana.models.shop-product/dao-settings
                                                             :join [{
                                                                     :from sitnoseckana.models.shop-product-type/dao-settings
                                                                     :fs ["name" "id"]
                                                                     :on ["product_type_id" "id"]
                                                                     }]
                                                             :where "product_type_id=17" :ord "product_type_id asc")
             :asdf {1 "jedan" 2 "two"}))

(defn handle-email [ctx]
  (assoc ctx :page-attrs {:author     "Pera Peric" :author-role "admin"
                          :menu-items (let [variations ((shop-product-type-variation/daf :load-list))
                                            monday (java.sql.Date. (-> (.getTime (doto (java.util.Calendar/getInstance) (.set java.util.Calendar/DAY_OF_WEEK java.util.Calendar/MONDAY))) (.getTime)))
                                            friday (java.sql.Date. (-> (.getTime (doto (java.util.Calendar/getInstance) (.set java.util.Calendar/DAY_OF_WEEK java.util.Calendar/FRIDAY))) (.getTime)))]
                                        (map (fn [prod] (assoc-in prod [:shop_product :shop_product_type :variations] (filter #(if (= (:type_id %) (get-in prod [:shop_product :shop_product_type :id])) true) variations)) )
                                             (sitnoseckana.models.base-daf/with-join shop-menu/dao-settings
                                                                                     :join [{
                                                                                             :from sitnoseckana.models.shop-product/dao-settings
                                                                                             :fs ["name" "id" "ingredients"]
                                                                                             :on ["product_id" "id"]
                                                                                             :join {
                                                                                                    :from sitnoseckana.models.shop-product-type/dao-settings
                                                                                                    :fs ["name" "id"]
                                                                                                    :on ["product_type_id" "id"]
                                                                                                    }
                                                                                             }]
                                                                                     :where (str "date>={d '" monday "'} and date<={d '" friday "'}") :ord "date asc")) )}
             :juices (sitnoseckana.models.base-daf/with-join sitnoseckana.models.shop-product/dao-settings
                                                             :join [{
                                                                     :from sitnoseckana.models.shop-product-type/dao-settings
                                                                     :fs ["name" "id"]
                                                                     :on ["product_type_id" "id"]
                                                                     }]
                                                             :where "product_type_id=16" :ord "product_type_id asc")
             :shakes (sitnoseckana.models.base-daf/with-join sitnoseckana.models.shop-product/dao-settings
                                                             :join [{
                                                                     :from sitnoseckana.models.shop-product-type/dao-settings
                                                                     :fs ["name" "id"]
                                                                     :on ["product_type_id" "id"]
                                                                     }]
                                                             :where "product_type_id=17" :ord "product_type_id asc")
             :asdf {1 "jedan" 2 "two"}))