(ns adventofcode.core
  (:require [adventofcode.utils :as utils]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(foo "yo")

(defn fuel-needed
  "Day 1:
  Calculate the fuel needed to launch the ship. 
  Mass/3 round down, then subtract 2"
  [mass]
  (-> mass
    (quot 3)
    (int)
    (- 2)
    (max 0)))

(defn totes-fuel-needed
  "Day 1 part 2:
  Need fuel for the fuel. 
  Calc fuel need + fuel for fuel itself
  "
  [mass]
  (let [ fuel (fuel-needed mass)]
    (if (pos? fuel)
      (+ fuel (totes-fuel-needed fuel))
      fuel)))

;(defn read-param
  ;"Reads the value of a parameter"
  ;[input index offset]
  ;(nth input (nth input (+ index offset))))

;(defn operation
  ;" Perform an operation based for the opinfo computer"
  ;[input index op-fn]
  ;(let [first-val (read-param input index 1)
        ;second-val (read-param input index 2)
        ;output-indx (nth input (+ index 3))
        ;output (assoc input output-indx (op-fn first-val second-val))
        ;next-indx (+ index 4)]
    ;(dispatch-opcodes output next-indx)))

;(defn dispatch-opcodes
  ;"Process opcodes"
  ;([input]
   ;(dispatch-opcodes input 0))
  ;([input index]
    ;(let [opcode (nth input index)]
    ;(cond
      ;(= opcode 1) (operation input index +)
      ;(= opcode 2) (operation input index *)
      ;(= opcode 99) input))))


;(defn prep-input
  ;" To do this, before running the program, replace position 1 with the value 12 and replace position 2 with the value 2"
  ;[input]
  ;(-> input
    ;(assoc 1 66)
    ;(assoc 2 35)))

3
