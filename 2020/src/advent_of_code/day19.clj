(ns advent-of-code.day19
  (:require
    [clojure.string :as str]))


(defn process-line
  [line]
  (let [[_ rulenum rules] (re-find #"(\d+): (.*)" line)]
    {rulenum
     (->> (str/split rules #" \| ")
          (map #(str/split % #" ")))}))


(def rule-map
  (->> "inputs/day19.txt"
       (slurp)
       (#(str/split % #"\n\n"))
       (first)
       (str/split-lines)
       (map process-line)
       (into {})))


(defn get-rule
  "Creates a Regex for the rules"
  [rule-number]
  (let [rule (get rule-map rule-number)]
    (if (nil? rule)
      (str/replace rule-number "\"" "")
      (let [rule-p1 (first rule)
            rule-p2 (second rule)]
        (if rule-p2
          (str/join (concat ["("]
                            (flatten (map get-rule rule-p1))
                            ["|"]
                            (flatten (map get-rule rule-p2))
                            [")"]))
          (str/join (flatten (map get-rule rule-p1))))))))


;; Part 1
(->> "inputs/day19.txt"
     (slurp)
     (#(str/split % #"\n\n"))
     (second)
     (str/split-lines)
     (map
       #(re-find
          (re-pattern (str/join ["^" (get-rule "0") "$"]))
          %))
     (remove nil?)
     count)


(defn get-rule2
  "Creates a Regex for the rules, with special 8 and 11 handling "
  [rule-number]
  (let [rule (get rule-map rule-number)]
    (if (nil? rule)
      (str/replace rule-number "\"" "")
      (case rule-number
        "8"
        (str/join (flatten
                    [(get-rule2 "42") "{1,}"]))

        "11"
        (str/join (flatten
                   ;; I'd prefer to use a recursive regex, but what the hell
                   ;; I paid for the the whole computer- might as well use it!
                    ["("
                     (for [x (range 1 20)]
                       [(repeat x (get-rule2 "42")) (repeat x (get-rule2 "31")) "|"])
                     [(repeat 20 (get-rule2 "420")) (repeat 20 (get-rule2 "31")) ")"]]))
        (let [rule-p1 (first rule)
              rule-p2 (second rule)]
          (if rule-p2
            (str/join (concat ["("]
                              (flatten (map get-rule2 rule-p1))
                              ["|"]
                              (flatten (map get-rule2 rule-p2))
                              [")"]))
            (str/join (flatten (map get-rule2 rule-p1)))))))))


;; Part 2
(->> "inputs/day19.txt"
     (slurp)
     (#(str/split % #"\n\n"))
     (second)
     (str/split-lines)
     (map
       #(re-find
          (re-pattern (str/join ["^" (get-rule2 "0") "$"]))
          %))
     (remove nil?)
     count)
