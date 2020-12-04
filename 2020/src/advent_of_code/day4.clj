(ns advent-of-code.day4
  (:require
    [clojure.string :as str]))


(def inputs
  (-> "inputs/day4.txt"
      (slurp)
      (str/split #"\n\n")))


(def example
  ["ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
byr:1937 iyr:2017 cid:147 hgt:183cm"
   "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
hcl:#cfa07d byr:1929"
   "hcl:#ae17e1 iyr:2013
eyr:2024 ecl:brn pid:760753108 byr:1931
hgt:179cm"
   "hcl:#cfa07d eyr:2025 pid:166559648
iyr:2011 ecl:brn hgt:59in"])


(defn has-fields?
  [passport]
  (and
    (str/includes? passport "byr")
    (str/includes? passport "iyr")
    (str/includes? passport "eyr")
    (str/includes? passport "hgt")
    (str/includes? passport "hcl")
    (str/includes? passport "ecl")
    (str/includes? passport "pid")))


;; Part 1
(->> inputs
     (filter has-fields?)
     (count))


(defn- check-fields
  [[_ field value]]
  (try
    (case field
      "byr"
      (<= 1920 (read-string value) 2002)

      "iyr"
      (<= 2010 (read-string value) 2020)

      "eyr"
      (<= 2020 (read-string value) 2030)

      "hgt"
      (let [[_ amount system] (re-find #"(\d+)([a-z]+)" value)]
        (case system
          "cm"
          (<= 150 (read-string amount) 193)
          "in"
          (<= 59 (read-string amount) 76)

          false))

      "hcl"
      (not (nil? (re-find #"^#[0-9a-f]{6,}$" value)))

      "ecl"
      (contains? #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} value)

      "pid"
      (= 9 (count value))

      "cid"
      true)
    (catch Exception _
      false)))


(def invalids
  (->  "eyr:1972 cid:100
hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926

iyr:2019
hcl:#602927 eyr:1967 hgt:170cm
ecl:grn pid:012533040 byr:1946

hcl:dab227 iyr:2012
ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277

hgt:59cm ecl:zzz
eyr:2038 hcl:74454a iyr:2023
pid:3556412378 byr:2007"
       (str/split #"\n\n")))


(def valids
  (-> "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
hcl:#623a2f

eyr:2029 ecl:blu cid:129 byr:1989
iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm

hcl:#888785
hgt:164cm byr:2001 iyr:2015 cid:88
pid:545766238 ecl:hzl
eyr:2022

iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719"
      (str/split #"\n\n")))


(map println (map check-passport invalids))
(map fields-valid? invalids)


(defn check-passport
  [value]
  (->> value
       (re-seq  #"(\w+):(#?\w+)")
       (map #(vector % (check-fields %)))))


(defn fields-valid?
  [value]
  (->> value
       (re-seq  #"(\w+):(#?\w+)")
       (map check-fields)
       (every? true?)))


(->> inputs
     (filter has-fields?)
     (filter fields-valid?)
     count)
