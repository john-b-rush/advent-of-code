(ns advent-of-code.day1
  (:require
    [clojure.math.combinatorics :as combo]
    [clojure.string :as str]))


(defn- combos
  [number items]
  (combo/combinations items number))

;; Part 1:
#_(->> "inputs/day1.txt"
     (slurp)
     (str/split-lines)
     (map read-string)
     (combos 2)
     (filter #(= 2020 (reduce + %)))
     (first)
     (reduce *))


;; Part 2
#_(->> "inputs/day1.txt"
     (slurp)
     (str/split-lines)
     (map read-string)
     (combos 3)
     (filter #(= 2020 (reduce + %)))
     (first)
     (reduce *))

