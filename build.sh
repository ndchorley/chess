#!/bin/bash

set -e

lein clean
lein test
lein uberjar
