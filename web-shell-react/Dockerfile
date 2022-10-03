
FROM node:alpine as build

WORKDIR /frontend

COPY . .

ARG REACT_APP_HOST_IP_ADDRESS

ENV REACT_APP_HOST_IP_ADDRESS ${REACT_APP_HOST_IP_ADDRESS}

RUN npm install
CMD ["npx" "build" "serve"]

######################################################


