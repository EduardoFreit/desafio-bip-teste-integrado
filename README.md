# Desafio Fullstack Integrado

Este repositório contém um projeto multi-módulo Java, integrando EJB e um backend Spring Boot (API REST), além do frontend Angular.

## Estrutura do Projeto

- `ejb-module/` — Módulo com lógica de negócios usando EJB
- `backend-module/` — Backend REST em Spring Boot (integra EJB e expõe APIs)
- `frontend/` — Aplicação Angular (ver README próprio)
- `db/` — Schemas e scripts SQL (agora migrados para o backend-module)

## Build e Execução

Este projeto utiliza Maven como gerenciador de dependências e build. O arquivo `pom.xml` na raiz define a estrutura multi-módulo e configura Java 17 como versão padrão.

### Passos para rodar o backend

1. Java 17+ e Maven instalados.
2. Compile todos os módulos:
   ```sh
   mvn clean install
   ```
3. Suba o backend Spring Boot:
   ```sh
   cd backend-module
   mvn spring-boot:run
   ```
### Passos para rodar o frontend
1. Certifique-se de que o backend está rodando (veja instruções acima).
2. Acesse a pasta do frontend:
   ```sh
   cd frontend
   ```
3. Instale as dependências:
   ```sh
   npm install
   ```
4. Gere os serviços e modelos TypeScript a partir do contrato OpenAPI do backend:
   ```sh
   npm run generate-api
   ```
   > Este comando garante que o frontend esteja sincronizado com a API backend.
5. Inicie o servidor de desenvolvimento Angular:
   ```sh
   npm run start
   ```
6. Acesse a aplicação em: http://localhost:4200

### Banco de Dados

- O projeto utiliza o banco de dados H2 (em memória) para facilitar testes e desenvolvimento.
- Os schemas e scripts SQL que estavam em `db/` foram transferidos para o diretório `backend-module/src/main/resources`.
- Para acompanhar os registros do banco, ao subir o backend, acesse o console do H2 em:
  - http://localhost:8080/backend-module/h2-console

  Use as credenciais e JDBC URL configurados em `application-prd.properties`.

## Tecnologias dos Testes Unitários

O projeto utiliza diferentes frameworks e bibliotecas para garantir a qualidade do código em cada módulo:

- **Backend (Spring Boot):**
   - JUnit 5 (Jupiter)
   - Spring Boot Test
   - Mockito
   - AssertJ


- **Frontend (Angular):**
   - Jasmine
   - Karma
   - TestBed (Angular Testing Utilities)
   - @angular/core/testing
   - ngx-toastr (para testes de notificações)

- **EJB Module:**
   - JUnit 5 (Jupiter)
   - Mockito

Os testes cobrem cenários de sucesso e falha, incluindo integração, exceções e validação de regras de negócio.

## Observações

- O backend integra o módulo EJB para operações de negócio distribuídas.
- O frontend Angular consome as APIs REST expostas pelo backend.
- O projeto utiliza OpenAPI para gerar automaticamente os serviços e objetos TypeScript do frontend, garantindo integração e tipagem consistente entre backend e frontend.
- Para detalhes de cada módulo, consulte os READMEs específicos em cada pasta.

## Contato

Dúvidas ou sugestões? Entre em contato com o responsável pelo projeto.
