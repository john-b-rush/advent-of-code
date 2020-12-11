(ns advent-of-code.day8
  (:require
    [clojure.string :as str]))

(def acc (atom 0))

(swap! acc + 10)

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


(assoc-in example [0 0] "ss")

;; Part 2
; some interesting challenges here: Need to keep track of prev changed jmp/nop
; can't just check next, need to roll back to prev then to prev. then to prev? 
; backtracking algo! - what I've seen before

(loop
  [acc 0
   pointer 0
   seen #{}
   inst example]
  (if (= pointer (count inst))
    acc
  (let [[operation value] (nth inst pointer)]
    (if (contains? seen pointer)
      (recur acc pointer seen (assoc-in inst [pointer 0] (case operation
                                                           "nop" "jmp"
                                                           "jmp" "nop")))
      (case operation
        "nop"
        (recur acc (inc pointer) (conj seen pointer) inst)

        "jmp"
        (recur acc (+ pointer value) (conj seen pointer) inst)

        "acc" 
        (recur (+ acc value) (inc pointer) (conj seen pointer) inst))))))


(loop
  [acc 0
   pointer 0
   seen #{}
   instr example]
  (let [[operation value] (nth inputs pointer)]
    (if (contains? seen pointer)
      acc
      (case operation
        "nop"
        (if (contains? seen (inc pointer))
        ;; will loop back on itself
        (recur acc pointer seen (assoc-in instr [pointer 0] "jump"))
        (recur acc (inc pointer) (conj seen pointer) instr))

        "jmp"
        (if (contains? seen (+ pointer value))
          ;; will loop back on itself
          (recur acc pointer seen (assoc-in inst [pointer 0] "nop"))
          (recur acc (+ pointer value) (conj seen pointer) instr))

        "acc" 
        (recur (+ acc value) (inc pointer) (conj seen pointer) instr)))))

