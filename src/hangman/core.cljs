(ns hangman.core
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [hangman.constants :as const]
            [hangman.gallows :as gallows]
            [hangman.guesses :as guesses]
            [hangman.word :as word]
            [reagent.core :as r]))

(enable-console-print!)

(defn started? []
  (not= (word/as-string) ""))

(defn step []
  (count (guesses/incorrect)))

(defn lost? []
  (= (step) const/hanged))

(defn won? []
  (= (count (guesses/correct)) (count (word/as-set-of-letters))))

(defn over? []
  (or (won?) (lost?)))

(defn new-game! []
  (guesses/clear!)
  (word/clear!))

(defn letter-buttons []
  (vec
    (cons
      :div
      (map
        #(vector :button {:disabled (guesses/has? %1)
                          :on-click (fn [] (guesses/record! %1))} %1)
        const/abcs))))

(defn next-button []
  (if (over?)
    [:button {:on-click #(new-game!)} "New Game"]
    [letter-buttons]))

(defn form []
  (let [next-word (r/atom "")]
    (fn []
      [:form {:on-submit (fn [e]
                           (.preventDefault e)
                           (word/update! @next-word))}
       [:input {:on-change #(swap! next-word (fn [] (-> % .-target .-value)))
                :type "password"
                :value @next-word}]
       [:button {:type "submit"} "Start"]])))

(defn display-field [letter]
  (if (or (guesses/has? letter) (over?))
    letter
    (if (= letter " ") " " "_")))

(defn blanks []
  [:pre (str/join " " (map #(display-field %1) (word/as-letters-with-spaces)))])

(defn controls []
  (if (started?)
    [:div
     [blanks]
     [next-button]]
    [form]))

(defn view []
  [:div
   [gallows/render (step)]
   [controls]])

(r/render-component [view] (.-body js/document))
