(ns hangman.core
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [hangman.constants :as const]
            [hangman.gallows :as gallows]
            [hangman.word :as word]
            [reagent.core :as r]))

(enable-console-print!)


(def guesses (r/atom #{}))
(def started (r/atom false))

(defn correct []
  (set/intersection (word/as-set-of-letters) @guesses))

(defn incorrect []
  (set/difference @guesses (word/as-set-of-letters)))

(defn step []
  (count (incorrect)))

(defn remaining []
  (set/difference (word/as-set-of-letters) @guesses))

(defn lost? []
  (= (step) const/hanged))

(defn won? []
  (= (count (correct)) (count (word/as-set-of-letters))))

(defn over? []
  (or (won?) (lost?)))

(defn record-guess [letter]
  (swap! guesses conj letter))

(defn reset []
  (swap! guesses #(identity #{}))
  (swap! started #(identity false))
  (word/update! ""))

(defn start []
  (if-not (= (word/as-string) "")
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
   [:input {:on-change #(word/update! (-> % .-target .-value))
            :type "password"
            :value (word/as-string)}]
   [:button {:on-click start} "Start"]])

(defn display-field [letter]
  (if (contains? @guesses (str/lower-case letter))
    letter
    (if (= letter " ")
      " "
      "_")))

(defn blanks []
  [:pre (str/join " " (map #(display-field %) (word/as-letters-with-spaces)))])

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
