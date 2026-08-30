FROM node

WORKDIR /app

ARG CLIENT_PORT

COPY . .

RUN npm install

RUN npm install -g @angular/cli

EXPOSE "${CLIENT_PORT}"

CMD ["npm", "start"]