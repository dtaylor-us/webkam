BASEDIR=$(PWD)
APPNAME=webkam
PROJECTID=kamestery

system-prep:
	curl -O https://download.clojure.org/install/linux-install-1.10.1.469.sh
	chmod +x linux-install-1.10.1.469.sh
	sudo ./linux-install-1.10.1.469.sh
	rm ./linux-install-1.10.1.469.sh

deps:
	rm -rf $(PWD)/node_modules; npm i

compile:
	npx shadow-cljs compile app web

node-repl:
	npx shadow-cljs node-repl app

watch-cljs:
	npm run start-dev

server-repl:
	nodemon start

webpack-release:
	npx webpack --mode production --config webpack.config.js

release: webpack-release
	npx shadow-cljs release app web

fmt:
	@read -p "Enter Path to Clojure File: " cljfile; \
	clj -A:zprint  -Sverbose < $$cljfile

commit:
	git add -A
	git commit -m "wip"

container:
	gcloud builds submit --tag gcr.io/$(PROJECTID)/$(APPNAME)

deploy: container
	gcloud beta run deploy --image gcr.io/$(PROJECTID)/$(APPNAME) --platform managed
