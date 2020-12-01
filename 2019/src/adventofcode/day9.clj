(ns adventofcode.day9
  (:require
    [adventofcode.intcode :refer [run-intcode]]
    [adventofcode.utils :as utils]))

(defn part1
  []
  (run-intcode (utils/day9-input) [1]))

(defn part2
  []
  (run-intcode (utils/day9-input) [2]))

