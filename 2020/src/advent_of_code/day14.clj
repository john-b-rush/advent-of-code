(ns advent-of-code.day14
  (:require
    [clojure.string :as str]))


(defn build-mask-instructions
  [input]
  (let [mask (first input)
        instruts (->> (rest input)
                      (map #(re-find #"mem\[(.*)\] = (.*)" %))
                      (map (fn [[_ address value]]
                             [address (read-string value)])))]
    [mask instruts]))


(defn build-mask-ops
  "Creates a set of operations for the mask. sets the value to zero or 1, otherwise ignores"
  [mask]
  (for [[i x] (map-indexed (fn [idx itm] [idx itm]) (reverse mask))
        :when (not= \X x)]
    (case x
      \0
      #(bit-clear % i)

      \1
      #(bit-set % i))))


;; Part 1
#_(->> "inputs/day14.txt"
     (slurp)
     (#(str/split % #"mask = "))
     (rest)
     (map str/split-lines)
     (map build-mask-instructions)
     (reduce
       (fn [acc inst-group]
         (let [mask-ops (build-mask-ops (first inst-group))
               insts (second inst-group)]
           ;; This hot garbage might be better as a for loop on each instuction (mem[x] = y)
           (reduce
             (fn [acc [mem value]]
               (assoc
                 acc
                 mem
                  ;; apply the bitmask operations to each value (bit-set bit-clear)
                 (reduce
                   (fn [acc f]
                     (f acc))
                   value
                   mask-ops)))
             acc
             insts)))
       {})
     (vals)
     (reduce +))


;; Part 2
(defn get-addresses
  "Gets all the addresses that could be 'floating'. Not pretty"
  [mem mask]
  (loop [all-numbers [mem]
         mask (reverse mask)
         incr 0]
    (let [current-mask (first mask)]
      (case current-mask
        nil
        all-numbers
        \0
        (recur all-numbers (rest mask) (inc incr))

        \1
        (recur (map #(bit-set % incr) all-numbers) (rest mask) (inc incr))

        \X
        (recur
          (reduce concat
                  (for [number all-numbers]
                    (do (print number "\n")
                        [(bit-clear number incr) (bit-set number incr)])))
          (rest mask)
          (inc incr))))))


#_(->> "inputs/day14.txt"
     (slurp)
     (#(str/split % #"mask = "))
     (rest)
     (map str/split-lines)
     (map build-mask-instructions)
     (reduce
       (fn [acc inst-group]
         (let [mask (first inst-group)
               insts (second inst-group)]
           (reduce
             merge
             acc
             (for [[mem value] insts
                   :let [addresses (get-addresses (read-string mem) mask)]]
               (reduce
                 merge
                 (for [address addresses]
                   {address value}))))))

       {})
     (vals)
     (reduce +))
