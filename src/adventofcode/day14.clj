(ns adventofcode.day14
  (:require
    [adventofcode.utils :as utils]
    [clojure.string :as str]
    [com.stuartsierra.dependency :as dep]))


(defn split-input-output
  [string]
  (str/split string #"=>"))


(def d14s1
  "9 ORE => 2 A
8 ORE => 3 B
7 ORE => 5 C
3 A, 4 B => 1 AB
5 B, 7 C => 1 BC
4 C, 1 A => 1 CA
2 AB, 3 BC, 4 CA => 1 FUEL")


(def d14s2
  "157 ORE => 5 NZVS
165 ORE => 6 DCFZ
44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL
12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ
179 ORE => 7 PSHF
177 ORE => 5 HKGWZ
7 DCFZ, 7 PSHF => 2 XJWVT
165 ORE => 2 GPVTF
3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT")

;; (fuel->X)
;; (x->y)

(defn to-input-seq
  "\"2 AB, 3 BC, 4 CA\" -> [AB AB BC BC BC CA CA CA CA]"
  [inputs]
  (->>
    (str/split inputs #", ")
    (map #(str/split % #" "))
    (mapv (fn [x]
            (repeat (read-string (first x)) (second x))))
    (flatten)))


(defn reactions
  [inputs]
  (->> inputs
       (str/split-lines)
       (map #(str/split % #" => "))
       (map (fn [x]
              (let [[amount output] (str/split (second x) #" ")
                    inputs (first x)]
                {output {:amount (read-string amount)
                         :requires (to-input-seq inputs)}})))
       (reduce merge)))


(defn graph
  [inputs]
  (->> inputs
       (str/split-lines)
       (map #(str/split % #" => "))
       (map (fn [x]
              (let [[_ output] (str/split (second x) #" ")
                    inputs (set (to-input-seq (first x)))]
                (map (fn [y] [output y]) inputs))))
       (reduce concat)
       (reduce (fn [g input] (dep/depend g (first input) (second input))) (dep/graph))))


(defn process
  [reactions fuels fuel-name]
  (let [reaction (get reactions fuel-name)
        number-of-items (->> fuels
                             (filter #(= fuel-name %))
                             (count))]
    (flatten
      (concat
        (repeat (quot number-of-items (:amount reaction)) (:requires reaction))
        (when (< 0 (rem number-of-items (:amount reaction)))
          (:requires reaction))
        (->> fuels
             (remove #(= fuel-name %)))))))


(defn topo-sort
  [input]
  (-> (graph input)
      (dep/topo-sort)
      (reverse)
      (drop-last)))


(defn ore-needed
  [input]
  (let [reactions (reactions input)
        topo (topo-sort input)]
    (count (reduce (partial process reactions) ["FUEL"] topo))))


(def d14s0
  "10 ORE => 1 A
1 A => 2 B
10 B => 1 FUEL")


(def d14s1
  "9 ORE => 2 A
8 ORE => 3 B
7 ORE => 5 C
3 A, 4 B => 1 AB
5 B, 7 C => 1 BC
4 C, 1 A => 1 CA
2 AB, 3 BC, 4 CA => 1 FUEL")


;[1 "FUEL"]
;[1 [[2 "AB"] [3 "BC"] [4 "CA"]]]
;;;...
;[2 [9 1]

(defn lookup
  [reaction item]
  (if (= "ORE" item)
    1
    (/  (reduce + (map (partial lookup reaction) (:requires (get reaction item)))) (:amount (get reaction item)))))

;; (lookup b "FUEL")
;; AB 145/6
;; 3 * 9/2 + 4 * 8/3
;; 

(def d14s3
  "157 ORE => 5 NZVS
165 ORE => 6 DCFZ
44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL
12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ
179 ORE => 7 PSHF
177 ORE => 5 HKGWZ
7 DCFZ, 7 PSHF => 2 XJWVT
165 ORE => 2 GPVTF
3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT")


(defn fuel-makes
  [input]
  (let [curr-reactions (reactions input)]
    (int
      (Math/floor
        (/
          1000000000000
          (get
            (->> input
                 (topo-sort)
                 (reverse)
                 (reduce
                   (fn [mapping x]
                     (let [reaction (get curr-reactions x)]
                       (assoc
                         mapping
                         x
                         (/
                           (reduce + (map (partial get mapping) (:requires reaction)))
                           (:amount reaction)))))
                   {"ORE" 1}))
            "FUEL"))))))


#_(print (fuel-makes (utils/day14-input)))


(comment
  (ore-needed (utils/day14-input))
  (ore-needed d14s1)
  (ore-needed d14s0)
  (reactions d14s0)
  (graph d14s0)
)

