(ns adventofcode.intcode-test
  (:require
    [adventofcode.intcode :refer :all]
    [clojure.test :refer :all]))


(deftest dispatch-opcodes-test
  (testing "Day 2 - add/multiply opcodes"
    (is (= (dispatch-opcodes [1 0 0 0 99]) [2 0 0 0 99]))
    (is (= (dispatch-opcodes [2 3 0 3 99]) [2 3 0 6 99]))
    (is (= (dispatch-opcodes [1 1 1 4 99 5 6 0 99]) [30 1 1 4 2 5 6 0 99])))
  (testing "Day 5- in/out opcodes"
    (is (= (run-program [1 0 0 0 99] nil 0 true) [2 0 0 0 99]))
    (is (= (run-program [2 3 0 3 99] nil  0 true) [2 3 0 6 99]))
    (is (= (run-program [1 1 1 4 99 5 6 0 99] nil 0 true) [30 1 1 4 2 5 6 0 99]))
    (is (= (run-program [3 0 4 0 99] 10) [10])))
  (testing "Day 5 - in/out and param modes"
    (is (= (read-param [2,4,3,4,33] 0 1) 33))
    (is (= (read-param [1002,4,3,4,33] 0 1) 33))
    (is (= (read-param [1002,4,3,4,33] 0 2) 3))
    (is (= (read-param [11002,4,3,4,33] 0 3) 4))
    (is (= (read-param [2,4,3,4,33] 0 3) 33))
    (is (= (read-param [2,4,3,4,33] 0 1) 33)))
  (testing "param modes"
    (is (= (mode 11102 1) 1))
    (is (= (mode 11102 2) 1))
    (is (= (mode 11102 3) 1))
    (is (= (mode 10002 1) 0))
    (is (= (mode 10002 2) 0))
    (is (= (mode 10002 3) 1))
    (is (= (mode 2 1) 0))
    (is (= (mode 2 2) 0))
    (is (= (mode 2 3) 0)))
  (testing "jump if true"
    (is (= (jump-if-true [5 3 3 1] 0) 1))
    (is (= (jump-if-true [5 3 3 0] 0) 3))
    (is (= (jump-if-true [105 0 3 10] 0) 3))
    (is (= (jump-if-true [105 1 3 10] 0) 10)))
  (testing "jump if false"
    (is (= (jump-if-false [5 3 3 1] 0) 3))
    (is (= (jump-if-false [5 3 3 0] 0) 0))
    (is (= (jump-if-false [105 0 3 10] 0) 10))
    (is (= (jump-if-false [105 1 3 10] 0) 3)))
  (testing "less than"
    (is (= (less-than [5 1 2 0] 0) [1 1 2 0]))
    (is (= (less-than [5 0 2 0] 0) [0 0 2 0]))
    (is (= (less-than [1105 1 2 0] 0) [1 1 2 0]))
    (is (= (less-than [1105 3 2 0] 0) [0 3 2 0])))
  (testing "equal"
    (is (= (equal [5 2 2 0] 0) [1 2 2 0]))
    (is (= (equal [5 0 2 0] 0) [0 0 2 0]))
    (is (= (equal [1105 2 2 0] 0) [1 2 2 0]))
    (is (= (equal [1105 0 2 0] 0) [0 0 2 0])))
  (testing "day 5 part 2"
    (is (= (run-program [3 9 8 9 10 9 4 9 99 -1 8] 8) [1]))
    (is (= (run-program [3 9 8 9 10 9 4 9 99 -1 8] 7) [0]))
    (is (= (run-program [3 9 7 9 10 9 4 9 99 -1 8] 8) [0]))
    (is (= (run-program [3 9 7 9 10 9 4 9 99 -1 8] 6) [1]))
    (is (= (run-program [3 3 1108 -1 8 3 4 3 99] 8) [1]))
    (is (= (run-program [3 3 1108 -1 8 3 4 3 99] 7) [0]))
    (is (= (run-program [3 3 1107 -1 8 3 4 3 99] 8) [0]))
    (is (= (run-program [3 3 1107 -1 8 3 4 3 99] 6) [1]))
    (is (= (run-program [3 12 6 12 15 1 13 14 13 4 13 99 -1 0 1 9] 0) [0]))
    (is (= (run-program [3 12 6 12 15 1 13 14 13 4 13 99 -1 0 1 9] 1) [1]))
    (is (= (run-program [3 3 1105 -1 9 1101 0 0 12 4 12 99 1] 0) [0]))
    (is (= (run-program [3 3 1105 -1 9 1101 0 0 12 4 12 99 1] 1) [1]))
    (is (= (run-program [3 21 1008 21 8 20 1005 20 22 107 8 21 20 1006 20 31 1106 0 36 98 0 0 1002 21 125 20 4 20 1105 1 46 104 999 1105 1 46 1101 1000 1 20 4 20 1105 1 46 98 99] 7) [999]))
    (is (= (run-program [3 21 1008 21 8 20 1005 20 22 107 8 21 20 1006 20 31 1106 0 36 98 0 0 1002 21 125 20 4 20 1105 1 46 104 999 1105 1 46 1101 1000 1 20 4 20 1105 1 46 98 99] 8) [1000]))
    (is (= (run-program [3 21 1008 21 8 20 1005 20 22 107 8 21 20 1006 20 31 1106 0 36 98 0 0 1002 21 125 20 4 20 1105 1 46 104 999 1105 1 46 1101 1000 1 20 4 20 1105 1 46 98 99] 9) [1001]))))

