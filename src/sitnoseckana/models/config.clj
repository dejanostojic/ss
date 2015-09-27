(ns sitnoseckana.models.config
  (:require  [sitnoseckana.models.base-daf :as base]
             [sitnoseckana.models.shop-product]
             [sitnoseckana.models.query-utils])
  (:import (java.sql Types)
           (java.time.temporal WeekFields)
           (java.time LocalDate DateTimeException)
           (java.util Locale)))

(def dao-settings {:tbl-name "config"
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
                            {:name "value"
                             :sql-type Types/VARCHAR
                             :java-type String
                             }
                            {:name "description"
                             :sql-type Types/VARCHAR
                             :java-type String
                             }
                            ]
                   } )

(def daf (base/base-daf dao-settings))

(defn load-config-by-name [name]
  "Loads config map entry from database by name"
  (first ((daf :load-list) :where (str "name=" (sitnoseckana.models.query-utils/mysql-str name)) :limit 1)))

(defn load-config-val-by-name
  "Loads (string) value of requested config"
  [name]
  (:value (load-config-by-name name)))

(defn active-week-number
  "Returns active week number from database as long.
  If there is no value than returns current week number."
  []
  (try (Long/parseLong (load-config-val-by-name "active_calendar_week"))
       (catch Exception e (-> (LocalDate/now) (.get (.weekOfWeekBasedYear (WeekFields/of (Locale. "sr"))))))))

(defn change-active-week-number [number]
  (sitnoseckana.models.query-utils/exec (str "update config set value=" (sitnoseckana.models.query-utils/mysql-str number) " where name='active_calendar_week'")))

(defn date-from-week-number
  "Returns LocalDate object for week number.
  If no week number is provided uses active-week-number"
  ([] (date-from-week-number (active-week-number)))
  ([week-number]
   (try
     (.with (LocalDate/now)
            (.weekOfWeekBasedYear (WeekFields/of (Locale. "sr")))
            (if (integer? week-number)
              (long week-number)
              week-number))
     (catch DateTimeException dte (do (.printStackTrace dte)
                                      (LocalDate/now))))))
