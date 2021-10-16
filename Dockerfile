FROM clojure:openjdk-17-lein-2.9.6

RUN apt-get update && apt-get -y install git

COPY . /work

WORKDIR /work

RUN ./build.sh

ARG GIT_REPO_URL
RUN echo $GIT_REPO_URL

EXPOSE $PORT

CMD ./run.sh
