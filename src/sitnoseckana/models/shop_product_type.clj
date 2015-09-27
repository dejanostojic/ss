(ns sitnoseckana.models.shop-product-type
  (:require  [sitnoseckana.models.base-daf :as base]
             [sitnoseckana.util.web :refer [vec-to-bool]])
  (:import (java.sql Types)))

(def dao-settings {:tbl-name "shop_product_type"
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
                          }
                         {:name "weekly"
                          :convert (fn [val]
                                     (vec-to-bool val))
                          :sql-type Types/TINYINT
                          :java-type Boolean
                          }
                         {:name "color"
                          :sql-type Types/VARCHAR
                          :java-type String
                          }
                         ] } )


(def daf (base/base-daf dao-settings))
