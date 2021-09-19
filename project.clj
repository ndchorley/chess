(defproject chess "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.3"]
                [ring/ring-core "1.9.4"]
                [ring/ring-jetty-adapter "1.9.4"]
                [org.jsoup/jsoup "1.14.2"]
                [hiccup "1.0.5"]
                [compojure "1.6.2"]]
  :plugins [[cider/cider-nrepl "0.26.0"]
            [lein-eftest "0.5.9"]]
  :javac-options ["-target" "17" "-source" "17"]
  :min-lein-version "2.0.0"
  :main ^:skip-aot chess.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
