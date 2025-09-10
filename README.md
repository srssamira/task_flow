# Documentação Técnica: Task Flow

## 🚀 Visão Geral do Sistema

O **Task Flow** é uma aplicação Java, construída com o framework **Spring Boot**, projetada para auxiliar usuários no gerenciamento de suas tarefas diárias. O sistema oferece funcionalidades completas de **CRUD** (Create, Read, Update, Delete) para tarefas, além de uma característica fundamental: a geração de relatórios de produtividade com sugestões de otimização, obtidas através da integração com um serviço externo da **Stackspot (Quick Command)**.

### Objetivo Principal

O objetivo central do Task Flow é fornecer uma solução completa para gerenciamento de tarefas, combinando as funcionalidades básicas com uma **análise inteligente de produtividade**. A aplicação busca não apenas registrar o progresso, mas também oferecer **insights acionáveis** ao usuário, baseados na análise de seus dados de tarefas.

---

## 🏛️ Arquitetura e Estrutura

A arquitetura do sistema segue o padrão **Domain-Driven Design (DDD)**, com camadas bem definidas para garantir a separação de responsabilidades e a manutenibilidade do código.

### Camadas da Aplicação

* **`infrastructure`**: Responsável pela infraestrutura e apresentação da aplicação.
    * **`presentation.controllers`**: Contém os **controladores REST** (`TaskController`, `ReportController`), que atuam como a porta de entrada da API. Eles recebem requisições HTTP, delegam a lógica para a camada de serviço e retornam as respostas.
    * **`presentation.mappers`**: Garante a separação entre a camada de apresentação e as entidades de domínio, convertendo **entidades de domínio** em **DTOs** e vice-versa.
    * **`config`**: Inclui as classes de configuração do Spring, como a criação de *beans* (`RestTemplate`) para injeção de dependência.

* **`application`**: Define os **DTOs (Data Transfer Objects)**, que são os contratos de dados para comunicação entre as camadas.
    * **`dtos`**: Contém classes como `TaskCreateDTO` e `TaskResponseDTO`, que representam a estrutura de dados para entrada e saída, isolando a API do modelo de domínio interno.

* **`domain`**: O **coração do sistema**, onde a lógica de negócio reside.
    * **`services`**: Define as interfaces de serviço (`TaskService`, `ReportService`), que descrevem as operações de negócio, promovendo a **inversão de dependência**.
    * **`services.impl`**: Contém as implementações concretas dos serviços, como `TaskServiceImpl` e `ReportServiceImpl`, que orquestram a lógica.
    * **`entities`**: Contém as **entidades de domínio** (`TaskEntity`, `ReportEntity`), que representam o modelo de dados e a lógica de negócio associada.
    * **`services.stackspot`**: Pacote dedicado à integração com o serviço externo da Stackspot.

* **`persistence`**: Lida com a persistência de dados.
    * **`TaskRepository`**: Uma interface que estende `JpaRepository`, fornecendo operações de banco de dados (CRUD) de forma transparente através do **Spring Data JPA**, além de métodos de consulta personalizados.

---

## ⚙️ Funcionalidades e Fluxo de Execução

O sistema oferece gerenciamento de tarefas e a geração de relatórios de produtividade.

### 1. Gerenciamento de Tarefas

O `TaskController` expõe os seguintes endpoints:

* `POST /tasks`: Cria uma nova tarefa.
* `GET /tasks`: Lista todas as tarefas.
* `GET /tasks/{id}`: Busca uma tarefa específica por ID.
* `PUT /tasks/{id}`: Atualiza uma tarefa existente.
* `PATCH /tasks/{id}/complete`: Marca uma tarefa como concluída.
* `DELETE /tasks/{id}`: Deleta uma tarefa.

**Exemplo de Fluxo (Criação de Tarefa):**
1.  `TaskController` recebe o `TaskCreateDTO`.
2.  Chama o `TaskService`.
3.  `TaskServiceImpl` converte o DTO para `TaskEntity` e salva no `TaskRepository`.
4.  Retorna o `TaskResponseDTO` da tarefa salva.

### 2. Geração de Relatórios de Produtividade

Essa funcionalidade demonstra a integração com serviços externos. O `ReportController` e o `ReportServiceImpl` são os responsáveis.

* `GET /reports/productivity?days={dias}`: Gera um relatório para os últimos `dias`.

**Exemplo de Fluxo (Geração de Relatório):**
1.  `ReportController` recebe a requisição com o parâmetro `days`.
2.  Chama o `ReportService`.
3.  `ReportServiceImpl` busca as tarefas no `TaskRepository`.
4.  Inicia a chamada ao serviço externo da Stackspot via `QuickCommandService` para obter sugestões de otimização.
5.  O `ReportServiceImpl` entra em um **loop de polling** para aguardar o resultado da API externa, verificando o status a cada 2 segundos.
6.  Ao receber o resultado, ele o adiciona ao `ReportEntity`.
7.  Mapeia a entidade para um `ReportResponse` e o retorna.

---

## 🔒 Segurança e Configuração

* **`StackspotAuthenticator`**: Centraliza os detalhes de autenticação do Stackspot (`CLIENT_ID`, `CLIENT_KEY`, `REALM`), promovendo a separação de preocupações.
* **`QuickCommandService`**: Lida diretamente com a obtenção e uso do token de acesso e a comunicação com a API externa.
* **`StackspotConfig`**: Centraliza a criação de *beans*, como o `RestTemplate`, que é crucial para as chamadas HTTP a serviços externos e disponível para injeção de dependência em toda a aplicação.

A arquitetura e as funcionalidades do Task Flow demonstram um design robusto, separando claramente as preocupações e usando padrões de design maduros para criar uma aplicação escalável e fácil de manter.
