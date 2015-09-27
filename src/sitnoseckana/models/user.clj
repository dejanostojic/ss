(ns sitnoseckana.models.user
  (:require  [sitnoseckana.models.base-daf :refer [base-daf]]
             [sitnoseckana.util.web :refer [vec-to-bool]])
  (:import (java.sql Types PreparedStatement ResultSet Date)))


(def dao-settings {:tbl-name       "user"
                   :auto-increment true
                   :fields         [{:name        "id"
                                     :primary-key true
                                     :convert     (fn [val]
                                                    (Long/parseLong val))
                                     :sql-type    Types/BIGINT
                                     :java-type   Long
                                     }
                                    {:name      "user_name"
                                     :sql-type  Types/VARCHAR
                                     :java-type String
                                     }
                                    {:name      "password"
                                     :sql-type  Types/VARCHAR
                                     :java-type String
                                     }
                                    {:name      "email"
                                     :sql-type  Types/VARCHAR
                                     :java-type String
                                     }
                                    {:name         "activated"
                                     :rs-to-map    (fn [^ResultSet rs user index]
                                                     (assoc user :activated (.getBoolean rs index)))
                                     :map-to-param (fn [user ^PreparedStatement statment index]
                                                     (.setBoolean statment index (or (:activated user) false)))
                                     :convert      (fn [val]
                                                     (vec-to-bool val))
                                     :sql-type     Types/TINYINT
                                     :java-type    Boolean
                                     }
                                    {:name      "activation_key"
                                     :sql-type  Types/VARCHAR
                                     :java-type String
                                     }
                                    {:name      "first_name"
                                     :sql-type  Types/VARCHAR
                                     :java-type String
                                     }
                                    {:name      "last_name"
                                     :sql-type  Types/VARCHAR
                                     :java-type String
                                     }
                                    {:name      "phone"
                                     :sql-type  Types/VARCHAR
                                     :java-type String
                                     }
                                    {:name      "shipping_address"
                                     :sql-type  Types/VARCHAR
                                     :java-type String
                                     }
                                    {:name      "registration_date"
                                     :convert   (fn [val]
                                                  (new Date (Long/parseLong val)))
                                     :sql-type  Types/DATE
                                     :java-type Date
                                     }
                                    ]
                   } )

(def daf (base-daf dao-settings))