(ns hangman.word
  (:require [clojure.set :as set]
            [clojure.string :as string]
            [hangman.constants :as const]
            [reagent.core :as r]))

(def word (r/atom ""))

(defn as-string [] @word)

(defn as-letters-with-spaces []
  (map str (as-string)))

(defn as-letters []
  (filter #(contains? const/abcs %) (map string/lower-case (as-letters-with-spaces))))

(defn as-set-of-letters []
  (apply sorted-set (as-letters)))

(defn update! [new-word]
  (swap! word #(identity new-word)))

(defn clear! []
  (reset! word ""))
