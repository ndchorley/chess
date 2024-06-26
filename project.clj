(defproject chess "0.1.0-SNAPSHOT"
  :repositories [["jitpack.io" "https://jitpack.io"]]
  :dependencies [
                 [org.clojure/clojure "1.11.3"]
                 [ring/ring-core "1.12.1"]
                 [ring/ring-jetty-adapter "1.12.1"]
                 [ring/ring-mock "0.4.0"]
                 [org.jsoup/jsoup "1.17.2"]
                 [hiccup "2.0.0-RC3"]
                 [compojure "1.7.1"]
                 [clojure.java-time "1.4.2"]
                 [com.github.bhlangonijr/chesslib "1.3.3"]
                 [commons-io "2.16.1"]]
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
