(ns adventofcode.day2
  (:require
    [adventofcode.intcode :as ic]
    [adventofcode.utils :as utils]))


(defn prep-input
  ([input]
   (prep-input input 12 2))
  ([input noun verb]
   (-> input
       (assoc 1 noun)
       (assoc 2 verb))))


(defn part1
  []
  (->> (utils/day2-input)
       (prep-input)
       (ic/run-intcode)
       (first)))


(defn values-match
  [number]
  (let [noun (quot number 100)
        verb (rem number 100)]
    (= 19690720 (first
                  (ic/run-intcode
                    (prep-input (utils/day2-input) noun verb))))))


(defn part2
  "Brute force search"
  []
  (some #(when (values-match %) %) (range 9999)))
