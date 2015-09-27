(ns sitnoseckana.models.shop-menu
  (:require  [sitnoseckana.models.base-daf :as base]
             [sitnoseckana.models.shop-product])
  (:import (java.sql Types)))

(def dao-settings {:tbl-name "shop_menu"
                   :auto-increment true
                   :fields [{:name "id"
                             :primary-key true
                             :convert (fn [val]
                                        (Long/parseLong val))
                             :sql-type Types/BIGINT
                             :java-type Long
                             }
                            {:name "product_id"
                             :convert (fn [val]
                                        (Long/parseLong val))
                             :sql-type Types/BIGINT
                             :java-type Long
                             }
                            {:name "date"
                             :convert (fn [val]
                                        (new java.sql.Date (Long/parseLong val)))
                             :sql-type Types/DATE
                             :java-type java.sql.Date
                             }]
                   :j [{
                           :from sitnoseckana.models.shop-product/dao-settings
                           :fs ["name" "ingredients" "product_type_id"]
                           :on ["product_id" "id"]
                           }]} )

(def daf (base/base-daf dao-settings))

