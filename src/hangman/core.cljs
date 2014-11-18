(ns hangman.core
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [hangman.constants :as const]
            [hangman.gallows :as gallows]
            [hangman.guesses :as guesses]
            [hangman.word :as word]
            [reagent.core :as r]))

(enable-console-print!)


(def started (r/atom false))

(defn step []
  (count (guesses/incorrect)))

(defn lost? []
  (= (step) const/hanged))

(defn won? []
  (= (count (guesses/correct)) (count (word/as-set-of-letters))))

(defn over? []
  (or (won?) (lost?)))

(defn new-game! []
  (guesses/reset!)
  (swap! started #(identity false))
  (word/reset!))

(defn start []
  (if-not (= (word/as-string) "")
    (swap! started #(identity true))))

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
  [:div
   [:input {:on-change #(word/update! (-> % .-target .-value))
            :type "password"
            :value (word/as-string)}]
   [:button {:on-click start} "Start"]])

(defn display-field [letter]
  (if (guesses/has? letter)
    letter
    (if (= letter " ") " " "_")))

(defn blanks []
  [:pre (str/join " " (map #(display-field %1) (word/as-letters-with-spaces)))])

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
