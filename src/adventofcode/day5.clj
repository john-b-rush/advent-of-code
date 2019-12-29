(ns adventofcode.day5
  (:require [adventofcode.utils :as utils]))


(defn add
  "adds"
  [instructions index]
  (assoc instructions
   (nth instructions (+ index 3))
   (+ (read-param instructions index 1)
      (read-param instructions index 2)))
  (+ index 4))

(defn multiply
  "multiplies"
  [instructions index]
  (assoc instructions
   (nth instructions (+ index 3))
   (* (read-param instructions index 1)
      (read-param instructions index 2)))
  (+ index 4))



(defn dispatch-opcodes
  "Process opcodes"
  ([instrutions]
   (dispatch-opcodes instrutions 0))
  ([instrutions index]
    (loop [instrutions input
           index index]
        (let [opcode (nth instrutions index)]
          (if (= opcode 99)
            nil)
          (recur 
            (cond 
              (= opcode 1) (add instructions index)
              (= opcode 2) (multiply instructions index)
              (= opcode 3) (input! instructions index)
              (= opcode 4) (output! instructions index)))))))



