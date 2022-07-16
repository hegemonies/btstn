.PHONY: api-proxy

build-all-jib:
	./gradlew --no-daemon jibDockerBuild

build-api-proxy:
	./gradlew --no-daemon api-proxy:jibDockerBuild

build-news-grabber-db-api:
	./gradlew --no-daemon newsgrabber:jibDockerBuild

push-api-proxy:
	docker tag btstn/api-proxy:dev lt:5000/api-proxy:dev
	docker push lt:5000/api-proxy:dev

push-news-grabber:
	docker tag btstn/news-grabber:dev lt:5000/news-grabber:dev
	docker push lt:5000/news-grabber:dev

push-all: push-api-proxy push-news-grabber
