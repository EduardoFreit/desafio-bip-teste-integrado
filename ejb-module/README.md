# EJB Module - Desafio BIP

Este módulo implementa a lógica de negócios utilizando Enterprise JavaBeans (EJB) para o desafio BIP. Ele pode ser utilizado em conjunto com o backend principal para fornecer serviços distribuídos, em particular transferência de valores entre contas/benefícios.

Para garantir a integridade das operações de transferência e evitar condições de corrida, foi adotado o controle de concorrência via optimistic locking (bloqueio otimista) nas entidades. Assim, se duas transações tentarem alterar o mesmo benefício simultaneamente, apenas a primeira será efetivada e as demais receberão uma exceção, garantindo consistência dos dados.

## Pré-requisitos

- Java 17 ou superior
- Maven 3.8+

## Instalação e Deploy

1. Clone o repositório (caso ainda não tenha feito):
   ```sh
   git clone <url-do-repositorio>
   ```
2. Acesse a pasta do módulo EJB:
   ```sh
   cd ejb-module
   ```
3. Compile o projeto:
   ```sh
   mvn clean package
   ```

## Estrutura de Pastas

- `src/main/java/com/example/ejb/exception/` — Exceções customizadas 
- `src/main/java/com/example/ejb/model/` — Entidades e modelos (Beneficio)
- `src/main/java/com/example/ejb/service/` — Serviços EJB (BeneficioEjbService)
- `src/main/resources/` — Configurações e recursos
- `src/test/java/` — Testes automatizados

## Scripts úteis

- `mvn test` — Executa os testes automatizados
- `mvn clean package` — Gera o artefato JAR para deploy

## Observações

- Este módulo pode ser utilizado por outros módulos Java EE ou aplicações externas, como exemplo, o módulo backend do projeto.

## Contato

Dúvidas ou sugestões? Entre em contato com o responsável pelo projeto.
