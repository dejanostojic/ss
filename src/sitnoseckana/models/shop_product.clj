(ns sitnoseckana.models.shop-product
  (:require  [sitnoseckana.models.base-daf :refer [base-daf]]
             [sitnoseckana.models.shop-product-type])
  (:import (java.sql Types)))

(def dao-settings {:tbl-name "shop_product"
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
                            {:name "ingredients"
                             :sql-type Types/VARCHAR
                             :java-type String
                             }
                            {:name "product_type_id"
                             :sql-type Types/BIGINT
                             :java-type Long
                             }
                            {:name "price"
                             :convert (fn [val] (Double/parseDouble val))
                             :sql-type Types/DECIMAL
                             :java-type Double
                             }
                            ]
                   :j [{
                        :from sitnoseckana.models.shop-product-type/dao-settings
                        :fs ["name" "id"]
                        :on ["product_type_id" "id"]
                        }
                       ]} )


(def daf (base-daf dao-settings))
