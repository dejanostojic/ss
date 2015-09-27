(ns sitnoseckana.models.user-role
  (:require  [sitnoseckana.models.base-daf :refer [base-daf]]
             [sitnoseckana.util.web :refer [vec-to-bool]])
  (:import (java.sql Types PreparedStatement ResultSet)))

(def dao-settings {:tbl-name       "user_role"
                   :auto-increment true
                   :fields         [{:name        "id"
                                     :primary-key true
                                     :convert     (fn [val]
                                                    (Long/parseLong val))
                                     :sql-type    Types/BIGINT
                                     :java-type   Long
                                     }
                                    {:name      "user_id"
                                     :convert     (fn [val]
                                                    (Long/parseLong val))
                                     :sql-type    Types/BIGINT
                                     :java-type   Long
                                     }
                                    {:name      "role_name"
                                     :sql-type  Types/VARCHAR
                                     :java-type String
                                     }]
                   })

(def daf (base-daf dao-settings))

