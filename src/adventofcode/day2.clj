(ns adventofcode.day2
  (:require
    [adventofcode.utils :as utils]))


(defn read-param
  "Reads the value of a parameter"
  [input index offset]
  (nth input (nth input (+ index offset))))


(defn dispatch-opcodes
  "Process opcodes"
  ([input]
   (dispatch-opcodes input 0))
  ([input index]
   (loop [input input
          index index]
     (let [opcode (nth input index)]
       (if (= opcode 99)
         input
         (recur
           (assoc input
                  (nth input (+ index 3))
                  ((cond (= opcode 1) +
                         (= opcode 2) *)
                   (read-param input index 1)
                   (read-param input index 2)))
           (+ index 4)))))))


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
       (dispatch-opcodes)
       (first)))


;(defn prep-input
  ;" To do this, before running the program, replace position 1 with the value 12 and replace position 2 with the value 2"
  ;[input]
  ;(-> input
    ;(assoc 1 66)
    ;(assoc 2 35)))

