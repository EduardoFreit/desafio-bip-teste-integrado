# Frontend Angular - Desafio BIP

Este projeto é o frontend do desafio BIP, desenvolvido em Angular. Ele consome APIs REST e oferece uma interface para a gestão dos benefícios.

## Pré-requisitos

- Node.js (recomendado: versão 18.x ou superior)
- npm (instalado junto com o Node.js)
- Angular CLI (global):
  ```sh
  npm install -g @angular/cli
  ```

## Instalação

1. Clone o repositório (caso ainda não tenha feito):
   ```sh
   git clone <url-do-repositorio>
   ```
2. Acesse a pasta do frontend:
   ```sh
   cd frontend
   ```
3. Instale as dependências:
   ```sh
   npm install
   ```


## Como rodar o projeto

1. Certifique-se de que o backend-module (API) está rodando. Execute o comando correspondente na raiz do projeto backend (exemplo: `mvn spring-boot:run` ou `mvn clean package && java -jar ...`).

2. Gere as interfaces TypeScript dos serviços a partir do contrato OpenAPI do backend:
   ```sh
   npm run generate-api
   ```
   O comando `npm run generate-api` utiliza o arquivo de especificação OpenAPI do backend para gerar automaticamente os serviços e modelos TypeScript em `src/app/api`. Isso garante que o frontend esteja sempre sincronizado com a API backend, evitando erros de tipagem e facilitando a integração.

3. Inicie o servidor de desenvolvimento Angular:
   ```sh
   npm run start
   ```

Acesse: http://localhost:4200

## Estrutura de Pastas

- `src/app/` - Código-fonte dos componentes, serviços e módulos Angular
- `src/assets/` - Imagens e outros arquivos estáticos
- `src/environments/` - Configurações de ambiente

## Scripts úteis

- `npm start` - Inicia o servidor de desenvolvimento
- `npm run build` - Builda o projeto para produção
- `npm run generate-api ` - Gera automaticamente os serviços e modelos TypeScript a partir do back

## Observações

- Certifique-se de que a API backend esteja rodando antes de utilizar o frontend.
- Para customizações, edite os arquivos em `src/app/`.

## Contato

Dúvidas ou sugestões? Entre em contato com o responsável pelo projeto.
