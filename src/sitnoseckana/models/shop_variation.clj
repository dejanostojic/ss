(ns sitnoseckana.models.shop-variation
  (:require  [sitnoseckana.models.base-daf :refer [base-daf]])
  (:import (java.sql Types)))

(def dao-settings {:tbl-name "shop_variation"
                   :auto-increment true
                   :fields [{:name "id"
                             :primary-key true
                             :convert (fn [val]
                                        (Long/parseLong val))
                             :sql-type Types/BIGINT
                             :java-type Long
                             }
                            {:name "name"
                             :sql-type Types/VARCHAR
                             :java-type String
                             }] } )


(def daf (base-daf dao-settings))
