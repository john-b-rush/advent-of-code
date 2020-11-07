(ns adventofcode.intcode-test
  (:require
    [adventofcode.intcode :refer [run-intcode]]
    [clojure.test :refer :all]))


(deftest intcode-program
  (testing "Program ends on a 99"
    (is (= [[99]] (run-intcode [99]))))
  (testing "Day 2: add/multiply opcodes"
    (is (= [[2 0 0 0 99]] (run-intcode [1 0 0 0 99])))
    (is (= [[2 3 0 6 99]] (run-intcode [2 3 0 3 99])))
    (is (= [[30 1 1 4 2 5 6 0 99]]
           (run-intcode [1 1 1 4 99 5 6 0 99]))))
  (testing "Day 5: Inputs/outputs"
    (is (= [11]
           (run-intcode [3 0 4 0 99] [11]))))
  (testing "Day 5: Immediate/Parameter Modes"
    (is (= [[1002 4 3 4 99]]
           (run-intcode [1002,4,3,4,33])))
    (is (= [[1101 100 -1 4 99]]
           (run-intcode [1101,100,-1,4,0]))))
  (testing "Day 5: Comparisons"
    (is (= [1] (run-intcode [3 9 8 9 10 9 4 9 99 -1 8] [8])))
    (is (= [0] (run-intcode [3 9 8 9 10 9 4 9 99 -1 8] [7])))
    (is (= [0] (run-intcode [3 9 7 9 10 9 4 9 99 -1 8] [8])))
    (is (= [1] (run-intcode [3 9 7 9 10 9 4 9 99 -1 8] [6])))
    (is (= [1] (run-intcode [3 3 1108 -1 8 3 4 3 99] [8])))
    (is (= [0] (run-intcode [3 3 1108 -1 8 3 4 3 99] [7])))
    (is (= [0] (run-intcode [3 3 1107 -1 8 3 4 3 99] [8])))
    (is (= [1] (run-intcode [3 3 1107 -1 8 3 4 3 99] [6])))
    (is (= [0] (run-intcode [3 12 6 12 15 1 13 14 13 4 13 99 -1 0 1 9] [0])))
    (is (= [1] (run-intcode [3 12 6 12 15 1 13 14 13 4 13 99 -1 0 1 9] [1])))
    (is (= [0] (run-intcode [3 3 1105 -1 9 1101 0 0 12 4 12 99 1] [0]) [0]))
    (is (= [1] (run-intcode [3 3 1105 -1 9 1101 0 0 12 4 12 99 1] [1]) [1]))
    (is (= [999] (run-intcode [3 21 1008 21 8 20 1005 20 22 107 8 21 20 1006 20 31 1106 0 36 98 0 0 1002 21 125 20 4 20 1105 1 46 104 999 1105 1 46 1101 1000 1 20 4 20 1105 1 46 98 99] [7]) ))
    (is (= [1000] (run-intcode [3 21 1008 21 8 20 1005 20 22 107 8 21 20 1006 20 31 1106 0 36 98 0 0 1002 21 125 20 4 20 1105 1 46 104 999 1105 1 46 1101 1000 1 20 4 20 1105 1 46 98 99] [8]) ))
    (is (= [1001] (run-intcode [3 21 1008 21 8 20 1005 20 22 107 8 21 20 1006 20 31 1106 0 36 98 0 0 1002 21 125 20 4 20 1105 1 46 104 999 1105 1 46 1101 1000 1 20 4 20 1105 1 46 98 99] [9]) )))
  (testing "day 7 multipule inputs"
    (is (= [3] (run-intcode [3 0 3 2 1 0 2 3 4 3 99] [1 2]))))

  )

