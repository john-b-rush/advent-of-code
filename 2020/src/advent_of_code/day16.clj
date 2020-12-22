(ns advent-of-code.day16
  (:require
    [clojure.set :refer [difference intersection]]
    [clojure.string :as str]))


(defn all-ranges-pred
  "Reads in all the ranges and creates a predicate which satifies them all."
  [ranges]
  (->> ranges
       (str/split-lines)
       (map (fn [line]
              (let [[_ field rangea-start rangea-end rangeb-start rangeb-end] (re-find #"(.*): (\d+)-(\d+) or (\d+)-(\d+)" line)]
                #(or (<= (read-string rangea-start)
                         %
                         (read-string rangea-end))
                     (<= (read-string rangeb-start)
                         %
                         (read-string rangeb-end))))))
       (apply some-fn)))


(defn process-other-tix
  "Extracts the other tix info"
  [other-tix]
  (->>
    other-tix
    (str/split-lines)
    (rest)
    (map #(str/split % #","))
    (map #(map read-string %))))


;; Part 1
#_(let [[ranges my-tix other-tx] (->> "inputs/day16.txt"
                                    (slurp)
                                    (#(str/split % #"\n\n")))
      all-range-pred (all-ranges-pred ranges)
      other-tix (process-other-tix other-tx)]
  (->> other-tix
       (flatten)
       (remove all-range-pred)
       (reduce +)))


(defn determine-field-ranges
  "Creates a series of functions that will return the name of the field if it's inside the range"
  [ranges]
  (->> ranges
       (str/split-lines)
       (map (fn [line]
              (let [[_ field rangea-start rangea-end rangeb-start rangeb-end] (re-find #"(.*): (\d+)-(\d+) or (\d+)-(\d+)" line)]
                #(when (or (<= (read-string rangea-start)
                               %
                               (read-string rangea-end))
                           (<= (read-string rangeb-start)
                               %
                               (read-string rangeb-end)))
                   field))))))


;; Sometimes hardcoding is fastest
(def fields
  #{"departure location"
    "departure station"
    "departure platform"
    "departure track"
    "departure date"
    "departure time"
    "arrival location"
    "arrival station"
    "arrival platform"
    "arrival track"
    "class"
    "duration"
    "price"
    "route"
    "row"
    "seat"
    "train"
    "type"
    "wagon"
    "zone"
    nil})


(defn which-field
  "Takes a set of ranges for a field and a vector of numbers and
  returns the set of fields that could match the numbers"
  [field-ranges values]
  (->> (reduce
         (fn [acc value]
           (let [possible-fields (set (map #(% value) field-ranges))
                 remaining-fields (intersection acc possible-fields)]
             remaining-fields))
         fields
         values)))


(let [[ranges my-tix other-tx] (->> "inputs/day16.txt"
                                    (slurp)
                                    (#(str/split % #"\n\n")))
      my-tix (->> my-tix
                  (str/split-lines)
                  (second)
                  (#(str/split % #","))
                  (mapv read-string))
      all-range-pred (all-ranges-pred ranges)
      other-tix (->> (process-other-tix other-tx)
                     ;; Remove the invalid tixs
                     (remove #(some (complement all-range-pred) %)))
      field-ranges (determine-field-ranges ranges)]
  (->> other-tix
       ;; 'rotate the matrix' aka group by columns
       (apply map list)
       (map #(which-field field-ranges %))
       ;; We now columnes with multiple fields
       (map-indexed (fn [idx itm] [idx itm]))
       (sort-by #(count (second %)))
       ;; Figure out the unique field per column
       (reduce
         (fn [[seen-fields field-map] [pos possible-fields]]
           (let [field (first (difference possible-fields seen-fields))]
             [(conj seen-fields field)
              (assoc field-map field pos)]))
         [#{} {}])
       (second)
       (seq)
       (filter #(str/starts-with? (first %) "departure"))
       ;; Get the values from my tix
       (map #(nth my-tix (second %)))
       (reduce *)))

