.PHONY: api-proxy

build-api-proxy:
	make -C frontend build
	cp -rf frontend/build/* kotlin/api-proxy/resources/frontend/
	make -C kotlin build-api-proxy
	rm -rf kotlin/api-proxy/resources/frontend/
	mkdir kotlin/api-proxy/resources/frontend/
	touch kotlin/api-proxy/resources/frontend/.gitkeep
