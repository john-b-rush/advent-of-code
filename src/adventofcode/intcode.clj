(ns adventofcode.intcode
  (:require
    [clojure.core.async
     :as a
     :refer [>! <! >!! <!! go chan buffer close! thread
             go-loop
             alts! alts!! timeout]]
    [clojure.string :as str]))


(defn pointer->params
  [pointer program number-of-params write-last-param]
  (->> (nth program pointer)
       (format (case number-of-params
                 3 "%05d"
                 2 "%04d"
                 1 "%03d"))
       (seq)
       (drop-last 2)
       (map (comp read-string str))
       (reverse)
       (map-indexed
         (fn [idx v]
           (let [value-at-param (nth program (+ pointer 1 idx))]
             (cond
                ;; writes go to the location in this parameter  
               (and write-last-param (= (- number-of-params 1) idx)) value-at-param

               (= 1 v) value-at-param

               :else (nth program value-at-param)))))))


(defn pointer->3params
  [pointer program write-last-param]
  (pointer->params pointer program 3 write-last-param))


(defn pointer->2params
  ([pointer program]
   (pointer->params pointer program 2 true))
  ([pointer program write-last-param]
   (pointer->params pointer program 2 write-last-param)))


(defn pointer->1params
  [pointer program]
  (pointer->params pointer program 1 false))


(pointer->1params 0 [104 10])


(defn add-multiply
  [pointer program f]
  (let [[first-param
         second-param
         third-param] (pointer->3params pointer program true)]
    [(+ 4 pointer)
     (assoc
       program
       third-param
       (f first-param
          second-param))]))


(defn add
  [pointer program]
  (add-multiply pointer program +))


(defn multiply
  [pointer program]
  (add-multiply pointer program *))


(defn input
  [pointer program in]
  [(+ 2 pointer)
   (assoc
     program
     (nth program (+ 1 pointer))
     (<!! in))])


(defn output
  [pointer program out]
  (let [[first-param] (pointer->1params pointer program)]
    (>!! out first-param)
    [(+ 2 pointer)
     program]))


(defn jump-if-fn
  [pointer program f]
  (let [[first-param
         second-param] (pointer->2params pointer program false)]
    (if (f (not= 0 first-param))
      [second-param program]
      [(+ 3 pointer) program])))


(defn jump-if-true
  [pointer program]
  (jump-if-fn pointer program true?))


(defn jump-if-false
  [pointer program]
  (jump-if-fn pointer program false?))


(defn compare-with-fn
  [pointer program f]
  (let [[first-param
         second-param
         third-param] (pointer->3params pointer program true)]
    [(+ 4 pointer)
     (assoc
       program
       third-param
       (if (f first-param second-param)
         1
         0))]))


(defn less-than
  [pointer program]
  (compare-with-fn pointer program <))


(defn equals
  [pointer program]
  (compare-with-fn pointer program =))


(defn get-opscode
  [opscode]
  (as-> opscode n
        (format "%05d" n)
        (seq n)
        (take-last 2 n)
        (str/join "" n)
        (str/replace n #"^0+" "")
        (read-string n)))


(defn intcode
  ([in program]
   (intcode in program false))
  ([in program debug?]
   (let [process (chan)
         out (chan)]
    ;; Initalize the processing
     (go (>! process [0 program]))
     (go-loop []
       (let [[pointer program] (<! process)
             ops-code (get-opscode (nth program pointer))]
         (case ops-code
           1  (go (>! process (add pointer program)))
           2  (go (>! process (multiply pointer program)))
           3  (go (>! process (input pointer program in)))
           4  (go (>! process (output pointer program out)))
           5  (go (>! process (jump-if-true pointer program)))
           6  (go (>! process (jump-if-false pointer program)))
           7  (go (>! process (less-than pointer program)))
           8  (go (>! process (equals pointer program)))
           99 (do
                (when debug?
                  (>!! out program))
                (close! in)
                (close! out))
           (throw (ex-info "Invalid opcode" {:opscode ops-code})))
         (recur)))
     out)))


(defn run-intcode
  ([program]
   (run-intcode program nil true))
  ([program inputs]
   (run-intcode program inputs false))
  ([program inputs debug?]
   (let [in (chan)
         out (intcode in program debug?)
         outputs (a/into [] out)]
     (when inputs
       (a/onto-chan in inputs))
     (<!! outputs))))


(comment
  (run-intcode [1 2 3 2 99])
  (run-intcode [3 0 4 0 99] [11])
  (run-intcode [1 1 1 4 99 5 6 0 99])
  (run-intcode [3 9 8 9 10 9 4 9 99 -1 8] [8])
  (run-intcode [3 21 1008 21 8 20 1005 20 22 107 8 21 20 1006 20 31 1106 0 36 98 0 0 1002 21 125 20 4 20 1105 1 46 104 999 1105 1 46 1101 1000 1 20 4 20 1105 1 46 98 99] [7])
)
