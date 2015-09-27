(ns sitnoseckana.models.shop-order
  (:require  [sitnoseckana.models.base-daf :as base]
             [sitnoseckana.models.user])
  (:import (java.sql Types Date)))

(def dao-settings {:tbl-name "shop_order"
                   :auto-increment true
                   :fields [{:name "id"
                             :primary-key true
                             :convert (fn [val]
                                        (Long/parseLong val))
                             :sql-type Types/BIGINT
                             :java-type Long
                             }
                            {:name "user_id"
                             :convert (fn [val]
                                        (Long/parseLong val))
                             :sql-type Types/BIGINT
                             :java-type Long
                             }
                            {:name "date"
                             :convert (fn [val]
                                        (new Date (Long/parseLong val)))
                             :sql-type Types/DATE
                             :java-type Date
                             }
                            {:name "first_name"
                             :sql-type Types/VARCHAR
                             :java-type String
                             }
                            {:name "last_name"
                             :sql-type Types/VARCHAR
                             :java-type String
                             }
                            {:name "email"
                             :sql-type Types/VARCHAR
                             :java-type String
                             }
                            {:name "phone"
                             :sql-type Types/VARCHAR
                             :java-type String
                             }
                            {:name "shipping_address"
                             :sql-type Types/VARCHAR
                             :java-type String
                             }
                            {:name "extra_info"
                             :sql-type Types/VARCHAR
                             :java-type String
                             }
                            {:name "total_price"
                             :convert (fn [val] (Double/parseDouble val))
                             :sql-type Types/DECIMAL
                             :java-type Double
                             }

                            ]
                   } )

(def daf (base/base-daf dao-settings))