(ns adventofcode.day7
  (:require
    [adventofcode.intcode :refer [intcode]]
    [adventofcode.utils :as utils]
    [clojure.core.async
     :as a
     :refer [>! <! >!! <!! go chan buffer close! thread
             go-loop
             alts! alts!! timeout]]

    [clojure.math.combinatorics :as combo]))


#_(defn prep-signal
  [phase-setting input feedback-loop?]
  (if feedback-loop?
    (cons phase-setting (cons input (lazy-seq (repeat phase-setting))))
    [phase-setting input]))


#_(defn calc-signal
  [program phase-sequence feedback-loop?]
  (loop [program program
         phase-sequence phase-sequence
         prev-outputs 0]
    (if (empty? phase-sequence)
      prev-outputs
      (recur program
             (rest phase-sequence)
             (ic/run-intcode
               program
               (prep-signal
                 (first phase-sequence)
                 prev-outputs
                 feedback-loop?))))))

(defn setup-amps
  [program phase-settings]
  (let [in (chan)
        ampA-out (intcode in program)
        ampB-out (intcode ampA-out program)
        ampC-out (intcode ampB-out program)
        ampD-out (intcode ampC-out program)
        ampE-out (intcode ampD-out program)
       ] 
    (print "\n\n\n " phase-settings)
    ;; setup the phase-settings    
    (>!! ampD-out (nth phase-settings 4))
    (>!! ampC-out (nth phase-settings 3))
    (>!! ampB-out (nth phase-settings 2))
    (>!! ampA-out (nth phase-settings 1))
    (>!! in (nth phase-settings 0))
    
    ;; kick it off
    (>!! in 0)

    ;; wait for outputs
    (<!! ampE-out)))


(defn setup-amps-w-feedback
  [program phase-settings]
  (let [in (chan)
        ampA-out (intcode in program)
        ampB-out (intcode ampA-out program)
        ampC-out (intcode ampB-out program)
        ampD-out (intcode ampC-out program)
        ampE-out (intcode ampD-out program)
        last-out (atom 0) 
       ] 
    ;; setup the phase-settings    
    (>!! ampD-out (nth phase-settings 4))
    (>!! ampC-out (nth phase-settings 3))
    (>!! ampB-out (nth phase-settings 2))
    (>!! ampA-out (nth phase-settings 1))
    (>!! in (nth phase-settings 0))
    
    ;; kick it off & wait for outputs
    (while (>!! in @last-out)
      (reset! 
        last-out
        (<!! ampE-out)))

    @last-out))


(defn part-amplifers
  [program]
  (->> (range 5)
       (combo/permutations)
       (map #(setup-amps program %))
       (reduce max)
       ))

(defn part-amplifers-w-feedback
  [program]
  (->> (range 5 10)
       (combo/permutations)
       (map #(setup-amps-w-feedback program %))
       (reduce max)
       ))

(defn gow
  []
(for [combo (part-amplifers [])]
  (do
  (print "\n" combo)
  (setup-amps 
   [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 0 0]
   combo))
  ))

#_(defn part1
  []
  (part-amplifers (utils/day7-input)))


#_(defn part2
  []
  (part-amplifers-w-feedback (utils/day7-input)))

(comment
  (gow)
  (part-amplifers [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 0 0])
  (part-amplifers [3 31 3 32 1002 32 10 32 1001 31 -2 31 1007 31 0 33 1002 33 7 33 1 33 31 31 1 32 31 31 4 31 99 0 0 0])
  (setup-amps 
   [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 0 0]
    [4 3 2 1 0] )
    ;[0 1 2 3 4] )

  
  (setup-amps
    [3 12 3 11 1002 11 10 9 104 0 99 0 0]

    [0 1 2 3 4])
  
  (setup-amps-w-feedback
    [3 12 3 11 1002 11 10 9 104 0 99 0 0]

    [0 1 2 3 4])
  
  (part-amplifers-w-feedback [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 0 0 5])

  )
  

