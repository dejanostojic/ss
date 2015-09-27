(ns sitnoseckana.util.date
  (:import (java.util Locale)
           (java.text SimpleDateFormat)
           (java.time.format DateTimeFormatter)))


(defn format-date [date format lang]
  (let [loc (Locale/forLanguageTag (cond
                                     (= lang "sr") "sr-Latn-RS"
                                     (= lang "ci") "sr"
                                     :else lang))]
    (try (.format (SimpleDateFormat. format loc) date)
         (catch Exception e
           (try (.format date (DateTimeFormatter/ofPattern format loc))
                (catch Exception e (println "ERROR ::: formating date :::" (.getMessage e))))))))

(defn parse-date [str-date format]
  (.parse (SimpleDateFormat. format) str-date))

(defn get-day
  "Returns nearest LocalDate object for day-of-week"
  [date day-of-week]
  (let [date-int (-> date (.getDayOfWeek) (.getValue))
        day-of-week-int (.getValue day-of-week)]
    (if (=  date-int day-of-week-int)
      date
      (if (> date-int day-of-week-int)
        (recur (.minusDays date 1) day-of-week)
        (recur (.plusDays date 1) day-of-week))))
  )

(defn get-days
  "Returns list of LocalDate objects til first nex day-of-the-week.
   If start-date is WEDNESDAY and end-date is FRIDAY, result will be [Wen Thu Fri]"
  ([start-date end-date] (get-days start-date end-date []))

  ([start-date end-date dates]
   (let [start-date-int (-> start-date (.getDayOfWeek) (.getValue))
         end-date-int  (-> end-date (.getDayOfWeek) (.getValue))]
     (if (=  start-date-int end-date-int)
       (conj dates start-date)
       (if (> end-date-int start-date-int)
         (recur (.plusDays start-date 1) end-date (conj dates start-date))
         (recur (.minusDays start-date 1) end-date (conj dates start-date))))
     )))


(defn mysql-date-literal [date]
  (str "{d '" date "}"))