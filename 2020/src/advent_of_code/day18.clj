(ns advent-of-code.day18
  (:require
    [clojure.string :as str]))


(defn new-math
  [string idx]
  (loop [idx idx
         running-value 0
         op +]
    (if (= (count string) idx)
      [running-value idx]
      (let [value (nth string idx)]
        (case value
          \+
          (recur (inc idx) running-value +)

          \*
          (recur (inc idx) running-value *)

          \(
          (let [[sub-value offset] (new-math string (inc idx))]
            (recur
              (inc offset)
              (op running-value sub-value)
              nil))

          \)
          [running-value idx]

          (recur (inc idx) (op running-value (read-string (str value))) nil))))))


(defn process-line
  [line]
  (-> line
      (str/replace #"\s+" "")
      (new-math 0)
      (first)))


;;Part 1
(->> "inputs/day18.txt"
     (slurp)
     (str/split-lines)
     (map process-line)
     (reduce +))


(defn line-ops
  [line]
  (reduce
    (fn [acc item]
      (case item
        \+
        (conj acc +)
        \*
        (conj acc *)
        \space
        acc
        \(
        (conj acc \()
        \)
        (conj acc \))
        (conj acc (read-string (str item)))))
    []
    line))


(declare adv-math+ adv-math* parens)


(defn- adv-math
  [idx items]
  (print "adm" idx items "\n")
  (->>
    items
    (parens idx)
    (adv-math+)
    (adv-math*)))


(defn- parens
  [idx items]
  (print idx items "\n")
  (loop [new-items []
         idx idx]
    (if (= (count items) idx)
      [idx new-items]
      (let [value (nth items idx)]
        (case value
          \(
          (let [[offset [sub-value]] (adv-math (inc idx) items)]
            (print sub-value offset)
            (recur
              (conj new-items sub-value)
              (inc offset)))

          \)
          [idx new-items]

          (recur (conj new-items value) (inc idx)))))))


(defn- adv-math+
  [[idx items]]
  (print "pls" idx items "\n")
  [idx
   (->>
     items
     (reduce
       (fn [[acc prev] item]
         (cond
           (= + prev)
           [(conj
              (subvec acc
                      0 (- (count acc) 2))
              (+ (nth acc (- (count acc) 2))
                 item))
            item]

           :else
           [(conj acc item) item]))
       [[] nil])
     (first))])


(defn- adv-math*
  [[idx items]]
  [idx
   (->>
     items
     (reduce
       (fn [[acc prev] item]
         (cond
           (= * prev)
           [(conj
              (subvec acc
                      0 (- (count acc) 2))
              (* (nth acc (- (count acc) 2))
                 item))
            item]

           :else
           [(conj acc item) item]))
       [[] nil])
     (first))])


(defn run-line
  [line]
  (->> line
       (line-ops)
       (adv-math 0)
       (second)
       (first)))

;;Part 2
(->> "inputs/day18.txt"
     (slurp)
     (str/split-lines)
     (map run-line)
     (reduce +))
