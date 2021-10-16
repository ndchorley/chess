FROM clojure:openjdk-17-lein-2.9.6

RUN apt-get update && apt-get install git

COPY . /work

WORKDIR /work

RUN ./build.sh

RUN mkdir /events

EXPOSE $PORT

CMD ./run.sh
