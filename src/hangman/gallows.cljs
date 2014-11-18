(ns hangman.gallows)

(def frames [
  (str
    "*-----*  \n"
    "|     |  \n"
    "|        \n"
    "|        \n"
    "|        \n"
    "|        \n"
    "*        \n")
  (str
    "*-----*  \n"
    "|     |  \n"
    "|     O  \n"
    "|        \n"
    "|        \n"
    "|        \n"
    "*        \n")
  (str
    "*-----*  \n"
    "|     |  \n"
    "|     O  \n"
    "|     |  \n"
    "|        \n"
    "|        \n"
    "*        \n")
  (str
    "*-----*  \n"
    "|     |  \n"
    "|     O  \n"
    "|    /|  \n"
    "|        \n"
    "|        \n"
    "*        \n")
  (str
    "*-----*  \n"
    "|     |  \n"
    "|     O  \n"
    "|    /|\\ \n"
    "|        \n"
    "|        \n"
    "*        \n")
  (str
    "*-----*  \n"
    "|     |  \n"
    "|     O  \n"
    "|    /|\\ \n"
    "|    /   \n"
    "|        \n"
    "*        \n")
  (str
    "*-----*  \n"
    "|     |  \n"
    "|     O  \n"
    "|    /|\\ \n"
    "|    / \\ \n"
    "|        \n"
    "*U*DEAD*\n")])

(defn render [frame]
  [:pre (nth frames frame)]
  )
