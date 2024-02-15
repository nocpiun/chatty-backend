<div align="center">

# Chatty Backend

[![Author](https://img.shields.io/badge/Author-NriotHrreion-red.svg "Author")](https://github.com/NriotHrreion)
[![LICENSE](https://img.shields.io/badge/License-MIT-green.svg "LICENSE")](./LICENSE)
[![Stars](https://img.shields.io/github/stars/nocpiun/chatty-backend.svg?label=Stars&style=flat)](https://github.com/nocpiun/chatty-backend/stargazers)

> A simple chat app

</div>

## Description

Chatty is a simple chat app written in Java + Typescript + React.

## Deploy

1. Download `Chatty.jar` from [releases](https://github.com/nocpiun/chatty-backend/releases)

2. Create a `config.yml` at the same directory with `Chatty.jar`

3. Open and edit the `config.yml`

```yml
sqlServer: "localhost"
sqlUserName: "root"
sqlPassword: "123456"
serverPort: 7000
salt: "abcde" # Change it to a better salt on your own
```

4. Launch the backend server

```bash
java -jar Chatty.jar
```

5. Meanwhile, you should see [the frontend deployment instructions](https://github.com/nocpiun/chatty-frontend#deploy) and launch Chatty frontend app. Then, you can start using Chatty.

## LICENSE

[MIT](./LICENSE)
