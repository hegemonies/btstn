FROM openjdk:11.0.9-slim

ADD build/libs/news-grabber.jar /app/
ADD src/main/resources/auth.key /app/news-grabber/
ADD src/main/resources/dc.save /app/news-grabber/

CMD java -jar /app/news-grabber.jar
