(ns adventofcode.day5
  (:require
    [adventofcode.intcode :as ic]
    [adventofcode.utils :as utils]))


(defn part1
  []
  (last (ic/run-program (utils/day5-input) 1)))

(defn part2
  []
  (last (ic/run-program (utils/day5-input) 5)))

