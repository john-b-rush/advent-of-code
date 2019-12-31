(ns adventofcode.day7
  (:require
    [adventofcode.intcode :as ic]
    [adventofcode.utils :as utils]
    [clojure.math.combinatorics :as combo]))


(defn prep-signal
  [phase-setting input feedback-loop?]
  (if feedback-loop?
    (cons phase-setting (cons input (lazy-seq (repeat phase-setting))))
    [phase-setting input]))


(defn calc-signal
  [program phase-sequence feedback-loop?]
  (loop [program program
         phase-sequence phase-sequence
         prev-outputs 0]
    (if (empty? phase-sequence)
      prev-outputs
      (recur program
             (rest phase-sequence)
             (ic/run-program
               program
               (prep-signal
                 (first phase-sequence)
                 prev-outputs
                 feedback-loop?))))))


(defn part1
  []
  (reduce max (map #(calc-signal (utils/day7-input) % false) (combo/permutations (range 5)))))


(defn part2
  []
  (reduce max (map #(calc-signal (utils/day7-input) % true) (combo/permutations (range 5)))))
