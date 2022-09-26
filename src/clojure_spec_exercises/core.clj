(ns clojure-spec-exercises.core
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.gen.alpha :as gen]
            [clojure.core.specs.alpha :as sp]
            [clojure.set]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

;;;  Collections:
;; Write spec definitions that conform:
; 1. A set of keywords or strings, no more than 5 total.
; (s/conform #(and (set? %) (and (> (count %) 5) (or (keyword? %) (string? %)))))  ; function needs to take a set,
;make sure all elements are either strings or keywords, and count the total
(defn set-key-string-5-or-less? [coll] (and (<= (count coll) 5) (and (set? coll) (or (every? string? coll) (every? keyword? coll)))))
(s/def ::b-set-of-keys-strings-5-or-less set-key-string-5-or-less?)

; 1a. A set of no more than 5 total.
;(s/conform #((and (set? %) (>= (count %) 5) col)
(defn set-5-or-less? [coll] (and (<= (count coll) 5) (set? coll)))
(s/def ::b-set-5-or-less set-5-or-less?)

; 2. A vector of non-nil elements
(defn non-nil-vector? [vec] (and (not (.contains vec nil)) (vector? vec)))
(s/def ::b-non-nil-vector non-nil-vector?)

;;;   Sequences:
; 1. A sequence of an odd number of keywords.
(defn seq-odd-num-keywords? [seq] (and (seq? seq) (and (odd? (count seq)) (every? keyword? seq))))
(s/def ::b-seq-odd-num-keywords seq-odd-num-keywords?)

; 2. A sequence of strings, each string of even length.
(defn seq-strings-of-even-length? [seq] (and (every? string? seq) (and (seq? seq) (every? #(even? (count %)) seq))))
(s/def ::b-seq-strings-of-even-length seq-strings-of-even-length?)

; 3. A sequence that has a keyword :hello as one of its elements.
(defn seq-contains-keyword-hello? [seq] (and (seq? seq) (.contains seq :hello)))
(s/def ::b-seq-contains-keyword-hello seq-contains-keyword-hello?)

;;; Functions:
(defn b-length2-to-3? [coll] (or (= (count coll) 2)
                                     (= (count coll) 3)))
(s/def ::b-length-two-to-three b-length2-to-3?)
(s/def ::b-seqable seqable?)
(s/def ::b-number number?)
(defn greater-than-equal-zero? [number] (and (::b-number number)(<= 0 number)))
(s/def ::greater-than-equal-zero greater-than-equal-zero?)
; 1. Write a function f that takes 2 or 3 parameters and a spec to validate them. It returns the nth element of a given sequence
; if it’s there, and nil or the third parameter (if given) if the element isn’t there. Validation:
;   - The first parameter must be a non-negative number
;   - The second parameter must be seqable
;   - No requirements on the last parameter
;   - The function must give a spec error if called with fewer than 2 or more than 3 parameters
;   Instrument the function and check that the parameters are validated as required.
(defn two-or-three-params
  "a must be a non-negative number, b must be seqable, and c is optional and can be anything."
  [a b c]
  (if (< a (count b))
    (nth b a)
    (if c
      c
      nil)
  ))

; (s/fdef clojure-spec-exercises.core/two-or-three-params
;   :args (s/and ::b-length-two-to-three
;           (s/and
;             :arg-one ::greater-than-zero
;             :arg-two ::b-seqable
;             (s/cat :arg-three (s/cat :any (s/? any?))))))
(s/fdef clojure-spec-exercises.core/two-or-three-params
  :args (s/and ::b-length-two-to-three
    (s/or
      :arg-one (s/cat :number ::greater-than-equal-zero)
      :arg-two (s/cat :coll ::b-seqable)
      :arg-three (s/cat :any (s/? any?)))))

; 2. Write a function g that takes an arbitrary even number of arguments and returns a hash map of pairs. For instance, given :a 1 :b 2,
; it returns {:a 1, :b 2}. Validation (using sequences spec):
;   - Even number of parameters
;   - Elements in positions 0, 2, 4,... must be keywords
;   - Elements in positions 1, 3, 5, … cannot be keywords (can be anything else)
;   - Zero parameters are allowed; an empty hash map is returned.
;   Instrument the function and check that the parameters are validated as required.
(defn g
  "takes an arbitrary even number of arguments, returns a hashmap of pairs"
  [& args]
  )

; (s/fdef clojure-spec-exercises.core/g
;   :args )
