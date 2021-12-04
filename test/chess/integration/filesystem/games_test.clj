(ns chess.integration.filesystem.games-test
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [clojure.test :refer :all]
   [chess.setup :refer :all]
   [chess.filesystem :as filesystem]
   [java-time]))

(declare add-game)

(deftest
  games-are-found-at-all-levels-within-the-configured-directory

  (let [directory (create-directory)]
    (add-game
     (string/join
      "\n"
      ["[Event \"Meltwater\"]"
       "[Date \"2021.10.03\"]"
       "[White \"Carlsen\"]"
       "[Black \"Aronian\"]"
       "[Result \"0-1\"]"
       "1. d4 Nf6 2. c4 e6 3. Nc3 Bb4 4. a3 Bxc3+ 5. bxc3 b6 *"]
      )
     "carlsen-aronian.pgn"
     directory)

    (add-game
     (string/join
      "\n"
      ["[Event \"Simul\"]"
       "[Date \"1924.10.11\"]"
       "[White \"Capablanca\"]"
       "[Black \"Shipley\"]"
       "[Result \"1-0\"]"
       "1. e4 e6 2. d4 d5 3. Nc3 Nf6 4. Bg5 Bb4 *"]
      )
     "endgame-vs-shipley.pgn"
     directory
     "champions"
     "capablanca")

    (add-game
     (string/join
      "\n"
      ["[Event \"Harrow Swiss\"]"
       "[Date \"2021.11.25\"]"
       "[White \"James Lyons\"]"
       "[Black \"Nicky Chorley\"]"
       "[Result \"1/2-1/2\"]"
       "1. d4 d5 2. e4 e6 3. exd5 exd5 *"]
      )
     "r2-lyons.pgn"
     directory
     "mine"
     "harrow-swiss"
     "2021")

    (let [games (filesystem/games-in directory)]
      (is
       (=
        (into #{} games)
        #{
          {:white "Carlsen"
           :black "Aronian"
           :date (java-time/local-date 2021 10 3)
           :result :black-won}

          {:white "Capablanca"
           :black "Shipley"
           :date (java-time/local-date 1924 10 11)
           :result :white-won}

          {:white "James Lyons"
           :black "Nicky Chorley"
           :date (java-time/local-date 2021 11 25)
           :result :draw}})))))

(defn- add-game [pgn-text file-name directory & subdirectories]
  (let [file
        (apply
         io/file
         (concat (conj subdirectories directory) [file-name]))]
    (io/make-parents file)
    (spit file pgn-text)))
