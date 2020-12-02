(ns advent-of-code.day3
  (:require
    [clojure.math.combinatorics :as combo]
    [clojure.string :as str]))


(defn- new-loc
  [[x y] inst]
  (case inst
    "<"
    [(dec x) y]

    ">"
    [(inc x) y]

    "^"
    [x (inc y)]

    "v"
    [x (dec y)]

    [x y]))


(defn houses
  [instructions]
  (loop [locations [[0 0]]
         current_loc [0 0]
         instructions instructions]
    (if (empty? instructions)
      locations
      (let [new-location (new-loc current_loc (first instructions))]
        (recur
          (conj locations new-location)
          new-location
          (rest instructions))))))

;; Part 1
#_(->>
  "inputs/day3.txt"
  (slurp)
  (seq)
  (map str)
  (houses)
  (distinct)
  (count))


;; Part 2
#_(->>
  "inputs/day3.txt"
  (slurp)
  (seq)
  (map str)
  (partition 2)  ;; this and next split into 2 paritions for every other one
  (apply map list)
  (map houses)
  (reduce concat)
  (distinct)
  (count))
