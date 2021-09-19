#!/bin/bash

set -e

lein clean
lein eftest
lein uberjar
