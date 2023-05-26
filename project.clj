(defproject chess "0.1.0-SNAPSHOT"
  :repositories [["jitpack.io" "https://jitpack.io"]]
  :dependencies [
                 [org.clojure/clojure "1.10.3"]
                 [ring/ring-core "1.10.0"]
                 [ring/ring-jetty-adapter "1.10.0"]
                 [ring/ring-mock "0.4.0"]
                 [org.jsoup/jsoup "1.16.1"]
                 [hiccup "1.0.5"]
                 [compojure "1.7.0"]
                 [clojure.java-time "1.2.0"]
                 [com.github.bhlangonijr/chesslib "1.3.3"]
                 [commons-io "2.12.0"]]
  :plugins [
            [cider/cider-nrepl "0.30.0"]
            [lein-eftest "0.6.0"]]
  :javac-options ["-target" "17" "-source" "17"]
  :min-lein-version "2.0.0"
  :main ^:skip-aot chess.core
  :target-path "target/%s"
  :profiles {:uberjar
             {:aot :all
              :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :eftest {:multithread? false})
