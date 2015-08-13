(ns schedules.core
  (:require [clj-time.core :refer [date-time days plus before?]]
            [clj-time.predicates :refer [saturday? sunday?]]
            [clj-time.periodic :refer [periodic-seq]]
            [clj-time.format :refer [unparse formatter]]
            [clojure.java.io :refer [writer]]))

(def p 0.0139)

(def sponsor-ids ["268"])

(def strategies [1 2 3 4])

(def holidays [(date-time 2015 9 7)
               (date-time 2015 10 12)
               (date-time 2015 11 11)
               (date-time 2015 11 26)
               (date-time 2015 11 27)
               (date-time 2015 12 24)
               (date-time 2015 12 25)
               (date-time 2015 12 28) 
               (date-time 2015 12 29) 
               (date-time 2015 12 30) 
               (date-time 2015 12 31) 
               (date-time 2016 1 1) 
               (date-time 2016 1 18) 
               (date-time 2016 2 15) 
               (date-time 2016 3 25) 
               (date-time 2016 5 30) 
               (date-time 2016 5 31)])

(def date-range
  {:start (date-time 2015 8 17)
   :end (date-time 2016 5 31)})

(defn weekend? [date]
  (or (saturday? date)
      (sunday? date)))

(defn- holiday? [date]
  (boolean (some #{date} holidays)))

(defn- qualifies? [date]
  (and (not (weekend? date))
       (not (holiday? date))))

(defn- to-str [date]
  (unparse (formatter "YYYY-MM-dd") date))

(def dates
  (let [{:keys [start end]} date-range]
    (loop [nd start
           dates []]
      (let [dates (if (qualifies? nd)
                    (conj dates (to-str nd))
                    dates)]
        (if (before? nd end)
          (recur
            (plus nd (days 1))
            dates)
          dates)))))

(def schedules
  (->> (for [id sponsor-ids]
         (for [date dates]
           (for [strategy strategies]
             (format "insert into sponsor_schedules (date, sponsor_id, strategy_id, p, created_at, updated_at) values ('%s', %s, %s, %s, current_timestamp, current_timestamp);\n" date id strategy p))))
       (flatten)))

(defn write-sql [sponsor-ids schedules dates strategies]
  (with-open [wrtr (writer "/tmp/schedules.sql")]
    (doseq [line schedules]
      (.write wrtr line))))

;; Run this
;; (write-sql sponsor-ids schedules dates strategies)
