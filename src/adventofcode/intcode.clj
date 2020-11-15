(ns adventofcode.intcode
  (:require
    [clojure.core.async
     :as a
     :refer [>! <! >!! <!! go chan buffer close! thread
             go-loop
             alts! alts!! timeout]]
    [clojure.string :as str]))


(defn get-opscode
  [{:keys [pointer program]}]
  (try
    (as-> (nth program pointer) n
          (format "%05d" n)
          (seq n)
          (take-last 2 n)
          (str/join "" n)
          (str/replace n #"^0+" "")
          (read-string n))
    (catch Exception ex
      (print "\n--\n" pointer program)
      (throw ex))))


(defn assoc-expand-vec
  [m k v]
  (assoc
    (if (< (count m) k)
      (into m (repeat (- k (count m)) 0))
      m)
    k
    v))


(defn get-mode
  [opscode offest]
  (let [value (->> opscode
                   (format "%05d")
                   (seq)
                   (drop-last 2)
                   (map (comp read-string str))
                   (reverse))]
    (nth value (- offest 1))))


(defn parameter->value
  [{:keys [pointer program relative-base] :as record} offset]
  (let [mode (get-mode (nth program pointer) offset)
        value-at-param (nth program (+ pointer offset))]
    (cond
      (= 2 mode)
      (if (<= (count program) (+ relative-base value-at-param))
        relative-base
        (nth program (+ relative-base value-at-param)))

      (= 1 mode)
      value-at-param

      :else
      (if (<= (count program) value-at-param)
        0
        (nth program value-at-param)))))


(defn parameter->location
  [{:keys [pointer program relative-base] :as record} offset]
  (let [mode (get-mode (nth program pointer) offset)
        value-at-param (nth program (+ pointer offset))]
    (cond
      (= 2 mode)
      (+ relative-base value-at-param)

      (= 0 mode)
      value-at-param

      :else
      (throw (Exception. "invalid location parameter mode")))))


(defn add-multiply
  [{:keys [pointer program] :as record} f]
  (let [first-param (parameter->value record 1)
        second-param (parameter->value record 2)
        third-param (parameter->location record 3)]
    (assoc
      record
      :pointer (+ 4 pointer)
      :program (assoc-expand-vec
                 program
                 third-param
                 (f first-param
                    second-param)))))


(defn add
  [record]
  (add-multiply record +))


(defn multiply
  [record]
  (add-multiply record *))


(defn input
  [{:keys [pointer program] :as record} in]
  (let [first-param (parameter->location record 1)]
    (assoc
      record
      :pointer (+ 2 pointer)
      :program (assoc-expand-vec
                 program
                 first-param
                 (<!! in)))))


(defn output
  [{:keys [pointer program] :as record} out]
  (let [first-param (parameter->value record 1)]
    (>!! out first-param)
    (assoc
      record
      :pointer (+ 2 pointer))))


(defn jump-if-fn
  [{:keys [pointer program] :as record} f]
  (let [first-param (parameter->value record 1)
        second-param (parameter->value record 2)]
    (if (f (not= 0 first-param))
      (assoc record :pointer second-param)
      (assoc record :pointer (+ 3 pointer)))))


(defn jump-if-true
  [record]
  (jump-if-fn record true?))


(defn jump-if-false
  [record]
  (jump-if-fn record false?))


(defn compare-with-fn
  [{:keys [pointer program] :as record} f]
  (let [first-param (parameter->value record 1)
        second-param (parameter->value record 2)
        third-param (parameter->location record 3)]
    (assoc
      record
      :pointer (+ 4 pointer)
      :program (assoc-expand-vec
                 program
                 third-param
                 (if (f first-param second-param)
                   1
                   0)))))


(defn less-than
  [record]
  (compare-with-fn record <))


(defn equals
  [record]
  (compare-with-fn record =))


(defn change-relative-base
  [{:keys [pointer relative-base] :as record}]
  (let [first-param (parameter->value record 1)]
    (assoc record
           :pointer (+ 2 pointer)
           :relative-base (+ relative-base first-param))))


(defn intcode
  ([in program]
   (intcode in program false))
  ([in program debug?]
   (let [process (chan)
         out (chan)]
    ;; Initalize the processing
     (go (>!
           process
           {:pointer 0
            :program program
            :relative-base 0}))
     (go-loop []
       (let [{:keys [pointer program] :as record} (<! process)
             ops-code (get-opscode record)]
         ;(print "\n" record)
         (case ops-code
           1  (go (>! process (add record)))
           2  (go (>! process (multiply record)))
           3  (go (>! process (input record in)))
           4  (go (>! process (output record out)))
           5  (go (>! process (jump-if-true record)))
           6  (go (>! process (jump-if-false record)))
           7  (go (>! process (less-than record)))
           8  (go (>! process (equals record)))
           9  (go (>! process (change-relative-base record)))
           99 (do
                (when debug?
                  (>!! out program))
                (close! in)
                (close! out))
           (throw (ex-info "Invalid opcode" {:opscode ops-code})))
         (recur)))
     out)))


;; very large size
; into existing (repeat (- loc (count v)) 0)


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

  (run-intcode [109 1 204 -1 1001 100 1 100 1008 100 16 101 1006 101 0 99] [])
  (run-intcode [104,1125899906842624,99] [])
)
