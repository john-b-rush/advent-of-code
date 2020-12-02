(ns advent-of-code.day4
  (:require
    [clojure.math.combinatorics :as combo]
    [clojure.string :as str])
  (:import
    java.math.BigInteger
    java.security.MessageDigest))


(defn md5
  [^String s]
  (->> s
       .getBytes
       (.digest (MessageDigest/getInstance "MD5"))
       (BigInteger. 1)
       (format "%032x")))

;; Part 1
#_(loop [number 0]
  (if (=  "00000" (subs (md5 (str/join "" ["iwrupvqb" number])) 0 5))
    number
    (recur (inc number))))


;; Part 2
#_(->> (range 100000000)
     (pmap #(subs (md5 (str/join "" ["iwrupvqb" %])) 0 6))
     (take-while #(not= "000000" %))
     (count))
