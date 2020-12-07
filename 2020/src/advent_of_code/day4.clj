(ns advent-of-code.day4
  (:require
    [clojure.string :as str]))


(def inputs
  (-> "inputs/day4.txt"
      (slurp)
      (str/split #"\n\n")))


(defn has-fields?
  [passport]
  (and
    (str/includes? passport "byr")
    (str/includes? passport "iyr")
    (str/includes? passport "eyr")
    (str/includes? passport "hgt")
    (str/includes? passport "hcl")
    (str/includes? passport "ecl")
    (str/includes? passport "pid")))


;; Part 1
(->> inputs
     (filter has-fields?)
     (count))


(defn- check-fields
  [[_ field value]]
  (try
    (case field
      "byr"
      (<= 1920 (read-string value) 2002)

      "iyr"
      (<= 2010 (read-string value) 2020)

      "eyr"
      (<= 2020 (read-string value) 2030)

      "hgt"
      (let [[_ amount system] (re-find #"(\d+)([a-z]+)" value)]
        (case system
          "cm"
          (<= 150 (read-string amount) 193)
          "in"
          (<= 59 (read-string amount) 76)

          false))

      "hcl"
      (not (nil? (re-find #"^#[0-9a-f]{6,}$" value)))

      "ecl"
      (contains? #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} value)

      "pid"
      (= 9 (count value))

      "cid"
      true)
    (catch Exception _
      ;; for when the read-string fails
      false)))


(defn fields-valid?
  [value]
  (->> value
       (re-seq  #"(\w+):(#?\w+)")
       (map check-fields)
       (every? true?)))

;; Part 2
(->> inputs
     (filter has-fields?)
     (filter fields-valid?)
     count)
