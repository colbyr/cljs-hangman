(ns hangman.constants
  (:require [hangman.gallows :as gallows]))

(def abcs (sorted-set "a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m" "n" "o" "p" "q" "r" "s" "t" "u" "v" "w" "x" "y" "z"))

(def hanged (dec (count gallows/frames)))
