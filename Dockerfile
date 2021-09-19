FROM clojure:openjdk-17-lein-2.9.6

COPY . /work

WORKDIR /work

RUN ./build.sh

EXPOSE $PORT

CMD ./run.sh