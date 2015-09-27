(ns sitnoseckana.models.shop-product-type-variation
  (:require  [sitnoseckana.models.base-daf :refer [base-daf]]
             [sitnoseckana.models.shop-variation]
             [sitnoseckana.models.shop-product-type])
  (:import (java.sql Types)))

(def dao-settings {:tbl-name "shop_product_type_variation"
                   :auto-increment false
                   :fields [{:name "type_id"
                             :primary-key true
                             :convert (fn [val] (Long/parseLong val))
                             :sql-type Types/BIGINT
                             :java-type Long
                             ;    :fk {:table "shop_product_type", :col "id", :sett sitnoseckana.models.shop-product-type/dao-settings }

                             }
                            {:name "variation_id"
                             :primary-key true
                             :convert (fn [val] (Long/parseLong val))
                             :sql-type Types/BIGINT
                             :java-type Long
                             }
                            {:name "price"
                             :convert (fn [val] (Double/parseDouble val))
                             :sql-type Types/DECIMAL
                             :java-type Double
                             }
                            ]
                   :join [{:from "left join shop_product_type on shop_product_type_variation.type_id = shop_product_type.id"
                           :fields [
                                    {:name "shop_product_type.name"}
                                    {:name "shop_product_type.id"}
                                    ]
                           :submap "product_type"
                           }
                          {:from "left join shop_variation on shop_product_type_variation.variation_id = shop_variation.id"
                           :fields [
                                    {:name "shop_variation.name"}
                                    ]
                           :submap "variation"
                           }

                          ]
                   :j [{
                        :from sitnoseckana.models.shop-product-type/dao-settings
                        :fs ["name" "id"]
                        :on ["type_id" "id"]
                        :submap "product_type"
                        }
                       {
                        :from sitnoseckana.models.shop-variation/dao-settings
                        :fs ["name" "id"]
                        :on ["variation_id" "id"]
                        :submap "variation"
                        }
                       ]
                   } )


(def daf (base-daf dao-settings))
