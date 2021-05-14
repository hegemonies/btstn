all:
	./gradlew --no-daemon shadowJar
	docker build --tag btstn/api-proxy:dev --file api-proxy/Dockerfile ./api-proxy
	docker build --tag btstn/news-grabber:dev --file news-grabber/Dockerfile ./news-grabber

api-proxy:
	./gradlew --no-daemon api-proxy:shadowJar
	docker build --tag btstn/api-proxy:dev --file api-proxy/Dockerfile ./api-proxy
