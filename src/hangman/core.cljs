(ns hangman.core
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [hangman.constants :as const]
            [hangman.gallows :as gallows]
            [reagent.core :as r]))

(enable-console-print!)


(def guesses (r/atom #{}))
(def started (r/atom false))
(def word (r/atom ""))

(defn word-letters-and-spaces []
  (map str @word))

(defn word-letters []
  (filter #(not= % " ") (map str/lower-case (word-letters-and-spaces))))

(defn word-set []
  (apply sorted-set (word-letters)))

(defn correct []
  (set/intersection (word-set) @guesses))

(defn incorrect []
  (set/difference @guesses (word-set)))

(defn step []
  (count (incorrect)))

(defn remaining []
  (set/difference (word-set) @guesses))

(defn lost? []
  (= (step) const/hanged))

(defn won? []
  (= (count (correct)) (count (word-set))))

(defn over? []
  (or (won?) (lost?)))

(defn record-guess [letter]
  (swap! guesses conj letter))

(defn reset []
  (swap! guesses #(identity #{}))
  (swap! started #(identity false))
  (swap! word #(identity "")))

(defn start []
  (if-not (= @word "")
    (swap! started #(identity true))))

(defn letter-buttons []
  (vec
    (cons
      :div
      (map
        #(vector :button {:disabled (contains? @guesses %1)
                          :on-click (fn [] (record-guess %1))} %1)
        const/abcs))))

(defn next-button []
  (if (over?)
    [:button {:on-click #(reset)} "New Game"]
    [letter-buttons]))

(defn form []
  [:div
   [:input {:on-change (fn [e] (swap! word #(-> e .-target .-value)))
            :type "password"
            :value @word}]
   [:button {:on-click start} "Start"]])

(defn display-field [letter]
  (if (contains? @guesses (str/lower-case letter))
    letter
    (if (= letter " ")
      " "
      "_")))

(defn blanks []
  [:pre (str/join " " (map #(display-field %) (word-letters-and-spaces)))])

(defn controls []
  (if @started
    [:div
     [blanks]
     [next-button]]
    [form]))

(defn view []
  [:div
   [gallows/render (step)]
   [controls]])

(r/render-component [view] (.-body js/document))
