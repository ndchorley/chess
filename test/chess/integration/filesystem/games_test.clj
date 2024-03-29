(ns chess.integration.filesystem.games-test
  (:require
   [clojure.test :refer :all]
   [chess.game-builder :refer :all]
   [chess.filesystem :as filesystem]
   [chess.integration.filesystem.setup :refer :all]
   [chess.contracts.games]
   [java-time]))

(deftest
  games-are-found-at-all-levels-within-the-configured-directory

  (let [games-directory (create-directory)]
    (add-game
     (-> a-game
         (played-on "2021.10.03")
         (between-white "Carlsen")
         (and-black "Aronian")
         (with-result "0-1")
         as-pgn)
     "carlsen-aronian.pgn"
     games-directory)

    (add-game
     (-> a-game
         (played-on "1924.10.11")
         (between-white "Capablanca")
         (and-black "Shipley")
         (with-result "1-0")
         as-pgn)
     "endgame-vs-shipley.pgn"
     games-directory
     "champions"
     "capablanca")

    (add-game
     (-> a-game
         (played-on "2021.11.25")
         (between-white "James Lyons")
         (and-black "Nicky Chorley")
         (with-result "1/2-1/2")
         as-pgn)
     "r2-lyons.pgn"
     games-directory
     "mine"
     "harrow-swiss"
     "2021")

    (let [games (filesystem/games-in games-directory)]
      (is
       (=
        #{
          {:white "Carlsen"
           :black "Aronian"
           :date (java-time/local-date 2021 10 3)
           :result :black-won
           :round 1
           :path "/carlsen-aronian"}

          {:white "Capablanca"
           :black "Shipley"
           :date (java-time/local-date 1924 10 11)
           :result :white-won
           :round 1
           :path "/champions/capablanca/endgame-vs-shipley"}

          {:white "James Lyons"
           :black "Nicky Chorley"
           :date (java-time/local-date 2021 11 25)
           :result :draw
           :round 1
           :path "/mine/harrow-swiss/2021/r2-lyons"}}
        (into #{} games))))))

(let [games-directory (create-directory)]
  (add-game
   (-> a-game (played-on "1984.08.06") as-pgn)
   "1984.pgn"
   games-directory)

  (add-game
   (-> a-game (played-on "2021.10.03") as-pgn)
   "2021.pgn"
   games-directory)

  (chess.contracts.games/games-are-returned-by-date-descending-from
   (partial filesystem/games-in games-directory)))

(deftest files-without-a-pgn-extension-are-ignored
  (let [games-directory (create-directory)]
    (add-file
     "8/8/8/5N1p/8/6pk/8/5K2 w - - 0 1"
     "r4-fenton.fen"
     games-directory)

    (is (empty? (filesystem/games-in games-directory)))))

(deftest invalid-pgn-files-are-ignored
  (let [games-directory (create-directory)]
    (add-file
     "NOT A VALID PGN"
     "some-game.pgn"
     games-directory)

    (is (empty? (filesystem/games-in games-directory)))))
