#!/bin/bash

set -e

lein clean
lein test
lein uberjar

PORT=9400 java -jar target/uberjar/chess-0.1.0-SNAPSHOT-standalone.jar \
     clojure.main \
     -m chess.core
