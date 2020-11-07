(ns adventofcode.day14
  (:require
    [adventofcode.utils :as utils]
    [clojure.string :as str]))

(defn split-input-output
  [string]
  (str/split string #"=>"))

(def d14-sample1
  (->> "9 ORE => 2 A
8 ORE => 3 B
7 ORE => 5 C
3 A, 4 B => 1 AB
5 B, 7 C => 1 BC
4 C, 1 A => 1 CA
2 AB, 3 BC, 4 CA => 1 FUEL"
       (str/split-lines)
       (map #(str/split % #"=>"))
       ;(mapv #(count %))
       ;(map #(conj (str/split (first  %) #",") (last %)))
       ;#(mapv #(str/trim %))
       ))
