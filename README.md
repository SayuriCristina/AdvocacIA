# AdvocacIA — Back-end (Spring Boot)

O **AdvocacIA** é uma plataforma gamificada de estudos para o exame da Ordem dos Advogados do Brasil (OAB).
Este repositório contém o **back-end** responsável pela API REST, autenticação e persistência de dados que viabilizam funcionalidades como cadastro/autenticação de usuários, gerenciamento de questões e pontuação por acertos.

---

## 🧱 Arquitetura e Tecnologias

* Spring Boot 3.5.6 (Web, Validation)
* Spring Security com JWT (JJWT 0.12.5)
* Spring Data JPA (MySQL em desenvolvimento; PostgreSQL em produção; H2 em testes)
* OpenAPI/Swagger UI (springdoc-openapi)
* Java 17 e Maven

**Desenvolvimento:**
* MySQL local: `jdbc:mysql://localhost/db_advocacIA`
* Variáveis de ambiente: `DB_USERNAME` (padrão: `root`), `DB_PASSWORD` (padrão: `root`)

**Produção:**
* PostgreSQL (Neon): configurado via variáveis de ambiente (`POSTGRESHOST`, `POSTGRESPORT`, `POSTGRESDATABASE`, `POSTGRESUSER`, `POSTGRESPASSWORD`)

**Testes:**
* H2 em memória, habilitando execução isolada e reprodutível

---

## 🗃️ Modelagem de Dados (resumo)

* `Usuario`: nome, usuário (e-mail), senha (BCrypt), pontos.
* `Questao`: enunciado (texto longo), alternativas A–D, resposta correta, assunto, fase da OAB (1ª ou 2ª), dificuldade (fácil/médio/difícil), tipo de questão (teórica/prática).

---

## 🔐 Segurança e Autenticação

Autenticação stateless com JWT:

* Fluxo: `/usuarios/logar` emite um token JWT (expiração padrão: 60 minutos).
* Envio do token: use o cabeçalho `Authorization: Bearer <token>`.
* Hash de senhas com `BCryptPasswordEncoder`.
* Filtro `OncePerRequestFilter` valida o token e popula o contexto de segurança.

**Endpoints públicos:**
`POST /usuarios/logar`, `POST /usuarios/cadastrar`, `GET /questao/all`, `GET /questao/{id}`
e a documentação (`/swagger-ui/**`, `/v3/api-docs/**`).
Demais rotas requerem autenticação.

---

## 🚀 Endpoints principais

**Usuários (`/usuarios`):**

* `POST /cadastrar`: cadastro de usuário.
* `POST /logar`: autenticação; retorna token JWT.
* `PUT /atualizar`: atualização de dados (autenticado).
* `GET /all`: lista usuários (autenticado).
* `GET /{id}`: busca por id (autenticado).
* `PUT /{id}/adicionar-pontos`: incrementa pontuação (autenticado).

**Questões (`/questao`):**

* `POST /post`: cria questão (autenticado).
* `POST /post/batch`: cria questões em lote (autenticado).
* `PUT /put`: atualiza questão (autenticado).
* `GET /all`: lista todas.
* `GET /{id}`: busca por id.
* `GET /assunto/{assunto}`: filtra por assunto (autenticado).
* `DELETE /{id}`: remove questão (autenticado).

---

## 📘 Documentação da API

A documentação interativa está disponível em tempo de execução:

* Swagger UI: [`/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)
* OpenAPI JSON: [`/v3/api-docs`](http://localhost:8080/v3/api-docs)

> 💡 Observação: na UI, insira apenas o token JWT (sem o prefixo `"Bearer "`), pois o esquema o adiciona automaticamente.

---

## 💻 Como Rodar Localmente

### 🧩 Requisitos

* [Java JDK 17+](https://adoptium.net/)
* [Maven](https://maven.apache.org/)
* Uma IDE (Eclipse, IntelliJ IDEA ou VS Code)
* [MySQL](https://dev.mysql.com/downloads/) configurado (ex.: via MySQL Workbench)

### ⚙️ Passo a passo

1. **Importe o projeto** como um projeto Maven na IDE.

2. **Instale as dependências:**

   ```bash
   mvn clean install
   ```

3. **O perfil de desenvolvimento está pré-configurado:**
   O arquivo `src/main/resources/application.properties` já possui:

   ```properties
   spring.profiles.active=dev
   ```

4. **Configure as credenciais do MySQL (opcional):**
   O arquivo `src/main/resources/application-dev.properties` já possui configurações padrão. Se quiser usar credenciais customizadas, set as variáveis de ambiente:

   ```bash
   set DB_USERNAME=seu_usuario_do_mysql
   set DB_PASSWORD=sua_senha_do_mysql
   ```

   Ou edite diretamente em `application-dev.properties` (padrão: `root`/`root`)

5. **Execute o back-end:**

   ```bash
   mvn spring-boot:run
   ```

   ou, pela IDE, execute a classe principal anotada com `@SpringBootApplication`.

> ⚠️ Mantenha o **MySQL Workbench aberto** durante a execução para evitar falhas de conexão.

6. **Acesse o Swagger da API:**

   ```
   http://localhost:8080/swagger-ui/index.html#/
   ```

   (a porta pode variar conforme sua configuração)

---

## 🧪 Execução de Testes

Os testes são automatizados com **JUnit 5** e **Spring Boot Test**, usando H2 em memória e `TestRestTemplate` para validar o comportamento ponta a ponta dos controladores com segurança JWT.

Comando:

```bash
mvn test
```

O pacote de testes cobre:

* Autenticação e emissão de token (`/usuarios/logar`).
* Cadastro, atualização e deduplicação de usuários.
* Listagem e busca de usuários e questões (incluindo por id e por assunto).
* CRUD completo de questões (criação, atualização e exclusão) com verificação de códigos HTTP e corpo da resposta.
* Obtenção e injeção do token nos headers via utilitários de teste.

Características dos testes:

* `@SpringBootTest(webEnvironment = RANDOM_PORT)`: levanta o contexto web real para testes de integração de API.
* Banco H2 isolado e volátil garantido por `src/test/resources/application.properties`.
* Builders e helpers específicos (ex.: criação de payloads e inclusão de JWT no header) promovem legibilidade e repetibilidade.
* Asserções verificam status, payload e efeitos de persistência, assegurando contratos da API.

---

## ☁️ Deploy e Serviços em Nuvem

O projeto está configurado para execução e deploy em ambiente **cloud-native**, com suporte a containers e pipelines contínuos.

### 🔹 Infraestrutura e serviços utilizados

* **Docker**
  O repositório inclui um `Dockerfile` configurado para build e execução da aplicação Spring Boot em ambiente isolado.
  Isso garante portabilidade e compatibilidade em produção e desenvolvimento.

* **Neon (PostgreSQL em nuvem)**
  Serviço de banco de dados gerenciado hospedado em **servidores AWS**, utilizado como banco de produção remoto.
  Ele provê alta disponibilidade e integração com ferramentas CI/CD.

* **Render**
  Plataforma utilizada para **deploy automático e contínuo** da aplicação back-end, consumindo a imagem Docker e integrando-se diretamente ao repositório GitHub.
  O Render executa o build a partir do Dockerfile e disponibiliza o serviço em uma URL pública.

Esses serviços garantem escalabilidade, segurança e facilidade de manutenção para o back-end do AdvocacIA.
 em `application-prod.properties`.
  Fornece alta disponibilidade, escalabilidade e integração com ferramentas CI/CD.
  Configuração via variáveis de ambiente: `POSTGRESHOST`, `POSTGRESPORT`, `POSTGRESDATABASE`, `POSTGRESUSER`, `POSTGRESPASSWORD`.

* **Render**
  Plataforma utilizada para **deploy automático e contínuo** da aplicação back-end, consumindo a imagem Docker e integrando-se diretamente ao repositório GitHub.
  O Render executa o build a partir do `Dockerfile` e disponibiliza o serviço em uma URL pública com ativação automática do perfil `prod`