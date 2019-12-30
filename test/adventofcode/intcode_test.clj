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
    (is (= (mode 2 3) 0))))

