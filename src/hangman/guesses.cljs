(ns hangman.guesses
  (:require [clojure.set :as set]
            [clojure.string :as string]
            [hangman.word :as word]
            [reagent.core :as r]))

(def guesses (r/atom #{}))

(defn as-set []
  @guesses)

(defn has? [letter]
  (contains? (as-set) (string/lower-case letter)))

(defn correct []
  (set/intersection (word/as-set-of-letters) (as-set)))

(defn incorrect []
  (set/difference (as-set) (word/as-set-of-letters)))

(defn remaining []
  (set/difference (word/as-set-of-letters) (as-set)))

(defn record! [letter]
  (swap! guesses conj (string/lower-case letter)))

(defn reset! []
  (swap! guesses (identity #{})))
