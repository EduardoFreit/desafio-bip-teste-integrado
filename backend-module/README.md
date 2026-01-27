# Backend - Desafio BIP

Este projeto é o backend do desafio BIP, desenvolvido em Java com Spring Boot. Ele fornece APIs REST para a gestão de benefícios e integrações com o frontend Angular.

## Pré-requisitos

- Java 17 ou superior
- Maven 3.8+
- Banco de dados relacional (ex: PostgreSQL, H2, MySQL)

## Instalação e Execução

1. Clone o repositório (caso ainda não tenha feito):
   ```sh
   git clone <url-do-repositorio>
   ```
2. Acesse a pasta do backend:
   ```sh
   cd backend-module
   ```
3. Configure o banco de dados em `src/main/resources/application-prd.properties` conforme necessário.
4. Compile e execute o projeto:
   ```sh
   mvn clean package
   java -jar target/backend-module-*.jar
   ```
   Ou, para rodar direto pelo Maven:
   ```sh
   mvn spring-boot:run
   ```

## Endpoints

A documentação dos endpoints está disponível via Swagger/OpenAPI:
- Acesse: http://localhost:8080/backend-module/swagger-ui/index.html

## Scripts úteis

- `mvn test` — Executa os testes automatizados
- `mvn clean package` — Gera o artefato JAR para deploy

## Estrutura de Pastas

- `src/main/java/` — Código-fonte principal
- `src/main/resources/` — Configurações e scripts SQL
- `src/test/java/` — Testes automatizados

## Observações

- O backend expõe a especificação OpenAPI em `/backend-module/v3/api-docs` para integração com o frontend.
- Para modificar as configurações utilizadas nos testes, atualize o arquivo `src/main/resources/application-test.properties`.

## Contato

Dúvidas ou sugestões? Entre em contato com o responsável pelo projeto.
