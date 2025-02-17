# Authorizer API

## Requisitos
Para executar a aplicação, certifique-se de que possui os seguintes requisitos instalados:

### **Execução via Gradle**
- Java 17+
- Gradle (caso não queira usar o wrapper `gradlew` incluído no projeto)

### **Execução via Docker**
- Docker

---

## Como Executar

### **Usando Gradle**
1. Clone o repositório:
   ```sh
   git clone git@github.com:pauloemmilio/transaction-authorizer-service.git
   cd authorizer
   ```
2. Execute a aplicação usando o Gradle Wrapper:
   ```sh
   ./gradlew bootRun
   ```
   Caso esteja no Windows (cmd/powershell):
   ```cmd
   gradlew bootRun
   ```

A aplicação estará disponível em `http://localhost:8080`

### **Usando Docker**
1. Clone o repositório:
   ```sh
   git clone git@github.com:pauloemmilio/transaction-authorizer-service.git
   cd authorizer
   ```
2. Construa a imagem Docker:
   ```sh
   docker build -t authorizer .
   ```
3. Execute o container:
   ```sh
   docker run -p 8080:8080 authorizer
   ```

A aplicação estará disponível em `http://localhost:8080`

---

## **Uso do H2 Console**

A aplicação utiliza um banco de dados H2 em memória.
Para acessar o H2 Console, abra:
```
http://localhost:8080/h2-console
```

Use as seguintes credenciais para conectar ao banco:
- **JDBC URL:** `jdbc:h2:mem:authorizer_db`
- **User:** `sa`
- **Password:** (deixe em branco)

Isso permite visualizar a estrutura do banco de dados junto com os dados das tabelas diretamente pelo navegador.

## **Uso do Swagger**

A documentação dos endpoints está disponível no Swagger:
```
http://localhost:8080/swagger-ui/index.html#/
```
Isso permite testar os endpoints diretamente pelo navegador.

---

## **Observação:**

- Foram criados alguns endpoints extras para facilitar possíveis testes. Neles foram feitos apenas validações básicas e, em casos de erro, esses endpoints irão retornar erros genéricos com HTTP Status 500.
