(ns sitnoseckana.models.shop-order-item
  (:require  [sitnoseckana.models.base-daf :as base]
             [sitnoseckana.models.user])
  (:import (java.sql Types Date)))

(def dao-settings {:tbl-name "shop_order_item"
                   :auto-increment true
                   :fields [{:name "id"
                             :primary-key true
                             :convert (fn [val]
                                        (Long/parseLong val))
                             :sql-type Types/BIGINT
                             :java-type Long
                             }
                            {:name "order_id"
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
                            {:name "variation_id"
                             :convert (fn [val]
                                        (Long/parseLong val))
                             :sql-type Types/BIGINT
                             :java-type Long
                             }
                            {:name "price"
                             :convert (fn [val] (Double/parseDouble val))
                             :sql-type Types/DECIMAL
                             :java-type Double
                             }
                            {:name "discount"
                             :convert (fn [val] (Double/parseDouble val))
                             :sql-type Types/DECIMAL
                             :java-type Double
                             }
                            {:name "quantity"
                             :convert (fn [val] (Integer/parseInt val))
                             :sql-type Types/INTEGER
                             :java-type Integer
                             }
                            {:name "date"
                             :convert (fn [val]
                                        (new Date (Long/parseLong val)))
                             :sql-type Types/DATE
                             :java-type Date
                             }
                            ]
                   } )

(def daf (base/base-daf dao-settings))
