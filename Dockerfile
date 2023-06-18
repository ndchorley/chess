FROM clojure:temurin-17-lein-2.10.0

RUN apt-get update && apt-get -y install git

COPY . /work

WORKDIR /work

RUN ./build.sh

ARG GIT_REPO_URL
RUN git clone --depth=1 $GIT_REPO_URL /games && rm -r /games/.git*

EXPOSE $PORT

CMD ./run.sh
