FROM gradle
WORKDIR /app
COPY . ./
COPY wait-for-it.sh .

CMD ./wait-for-it.sh projektdb:5432 --timeout=0 -- gradle bootRun





