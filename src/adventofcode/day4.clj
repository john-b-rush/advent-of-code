(ns adventofcode.day4)

;; I've lost work here. This isn't the compelete solution

(def min-value 172851)
(def max-value 675869)


(defn- old-matches?
  [value]
  (loop [value value
         which-digit 1
         prev-digit 99
         has-match false]
    (let [current-digit (int (quot (rem value (Math/pow 10 which-digit)) (Math/pow 10 (- which-digit 1))))
          has-match (or has-match
                        (and (not= prev-digit 99) (= prev-digit current-digit)))]
      (cond
        (> which-digit 7)
        has-match
        (<= current-digit prev-digit)
        (recur value (inc which-digit) current-digit has-match)
        :else
        false))))


(defn matching-passwords
  [value]
  (loop [value value
         pw-count 0]
    (if (>= value max-value)
      pw-count
      (if (old-matches? value)
        (recur (inc value) (inc pw-count))
        (recur (inc value) pw-count)))))


(defn- ->digit-groups
  [value]
  (partition-by identity
                (for [n  (str value)]
                  (- (byte n) 48))))

;; SO
(defn partition-between
  [pred? coll]
  (let [switch (reductions not= true (map pred? coll (rest coll)))]
    (map (partial map first) (partition-by second (map list coll switch)))))


;; TODO Fix 
(defn- increasing?
  [digit-groups]
  (->> digit-groups
       (partition-between (fn [a b] (<= (first a)  (first b))))
       (map count)
       (some #(> % 2))))
       ;(partition 2 1 (repeat 99))
       ;(map (partial apply <=))
       ;(every? true?)))
;  (every? true? (map (partial apply <=) (partition 2 1 (repeat 99) digit-groups))))

(defn- pairs?
  [digit-groups]
  (->> digit-groups
       (map count)
       true))
  ;(->> digit-groups
       ;(map count)
       ;(some #(> 2 %))))

(defn matches?
  [value]
  (let [digits (->digit-groups value)]
    (and (increasing? digits)
         (pairs? digits))))


(defn- ignore-multiple-matches
  [digit-groups]
  (->> digit-groups
       (map count)
       (some #(= % 2))))


(defn matches2?
  [value]
  (let [digit-groupings (->digit-groups value)]
    (and (increasing? digit-groupings)
         (pairs? digit-groupings))))
         ;(no-triples? digit-groupings))))


(defn find-passwords
  [match-fn]
  (->> (range min-value max-value)
       (map match-fn)
       (filter identity)
       (count)))

