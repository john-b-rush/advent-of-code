(ns advent-of-code.day8
  (:require
    [clojure.string :as str]))


(def inputs
  (->> "inputs/day8.txt"
       (slurp)
       (str/split-lines)
       (map #(str/split % #" "))
       (mapv (fn [[op value]]
               [op (Integer/parseInt value 10)]))))


(def example
  (->>
    "nop +0
acc +1
jmp +4
acc +3
jmp -3
acc -99
acc +1
jmp -4
acc +6"
    (str/split-lines)
    (map #(str/split % #" "))
    (mapv (fn [[op value]]
            [op (Integer/parseInt value 10)]))))

;; Part 1
(loop
  [acc 0
   pointer 0
   seen #{}]
  (let [[operation value] (nth inputs pointer)]
    (if (contains? seen pointer)
      acc
      (case operation
        "nop"
        (recur acc (inc pointer) (conj seen pointer))

        "jmp"
        (recur acc (+ pointer value) (conj seen pointer))

        "acc"
        (recur (+ acc value) (inc pointer) (conj seen pointer))))))


;; Part 2
(defn- process-inst
  [acc pointer seen inst check]
  (loop
    [acc acc
     pointer pointer
     seen seen
     inst inst]
    (if (= pointer (count inst))
      acc
      (let [[operation value] (nth inst pointer)]
        (if (contains? seen pointer)
          nil
          (case operation
            "nop"
            (or
              (when check 
                (process-inst acc pointer seen (assoc-in inst [pointer 0] "jmp") false))
              (recur acc (inc pointer) (conj seen pointer) inst))

            "jmp"
            (or
              (when check
                (process-inst acc pointer seen (assoc-in inst [pointer 0] "nop") false))
              (recur acc (+ pointer value) (conj seen pointer) inst))


            "acc"
            (recur (+ acc value) (inc pointer) (conj seen pointer) inst)))))))


(process-inst 0 0 #{} inputs true)
