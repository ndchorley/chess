FROM clojure:openjdk-17-lein-2.9.6

RUN apt-get update && apt-get -y install git

COPY . /work

WORKDIR /work

RUN ./build.sh

RUN git clone $GIT_REPO_URL /events

EXPOSE $PORT

CMD ./run.sh
