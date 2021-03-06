BASEDIR=$(PWD)
APPNAME=webkam
PROJECTID=kamestery
REGION=us-east1

system-prep:
	curl -O https://download.clojure.org/install/linux-install-1.10.1.469.sh
	chmod +x linux-install-1.10.1.469.sh
	./linux-install-1.10.1.469.sh
	rm ./linux-install-1.10.1.469.sh
	curl -sL https://deb.nodesource.com/setup_13.x | bash -
	apt-get install build-essential nodejs -y

deps:
	rm -rf $(PWD)/node_modules; npm i

compile:
	npx shadow-cljs compile app common web

node-repl:
	npx shadow-cljs node-repl app

watch-cljs:
	npm run start-dev

server-repl:
	nodemon start

webpack-release:
	npx webpack --mode production --config webpack.config.js

release: webpack-release
	npx shadow-cljs release app common web

fmt:
	@read -p "Enter Path to Clojure File: " cljfile; \
	clj -A:zprint  -Sverbose < $$cljfile

commit:
	git add -A
	git commit -m "wip"

container:
	gcloud builds submit --tag gcr.io/$(PROJECTID)/$(APPNAME)

deploy: container
	gcloud beta run deploy $(APPNAME) --image gcr.io/$(PROJECTID)/$(APPNAME) --platform managed --region $(REGION)
