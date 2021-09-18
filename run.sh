#!/bin/bash

set -e

java -jar target/uberjar/chess-0.1.0-SNAPSHOT-standalone.jar \
     clojure.main \
     -m chess.core
