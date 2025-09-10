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

* **`QuickCommandService`**: Lida diretamente com a obtenção e uso do token de acesso e a comunicação com a API externa.
* **`StackspotConfig`**: Centraliza a criação de *beans*, como o `RestTemplate`, que é crucial para as chamadas HTTP a serviços externos e disponível para injeção de dependência em toda a aplicação.

-----

## 🚀 Análise e Projeto do Software

### **1. Projeto Arquitetural**

O projeto arquitetural do sistema **Task Flow** foi concebido com base na arquitetura em **Camadas (Layered Architecture)**, seguindo os princípios do **Domain-Driven Design (DDD)**. Essa escolha visa garantir uma forte separação de interesses, alta coesão dentro de cada camada e baixo acoplamento entre elas.

A arquitetura é dividida em quatro camadas principais, onde cada uma tem uma responsabilidade bem definida e se comunica apenas com a camada imediatamente abaixo, exceto pela camada de apresentação, que delega para a de domínio.

* **Camada de Apresentação (`infrastructure.presentation`)**: A interface do sistema. Lida com a entrada e saída de dados, expondo endpoints REST para o cliente. Não contém lógica de negócio.
* **Camada de Aplicação (`application`)**: Atua como um orquestrador. Contém os DTOs e coordena o fluxo de dados entre as camadas de apresentação e de domínio.
* **Camada de Domínio (`domain`)**: O coração do sistema. Contém toda a lógica de negócio, as regras e o modelo de dados. É a camada mais importante e deve ser agnóstica a frameworks e tecnologias externas.
* **Camada de Persistência (`persistence`)**: Responsável pela interação com o banco de dados. Encapsula toda a lógica de acesso a dados.

**Diagrama de Arquitetura em Camadas:**
![arquitetura-em-camadas](assets/WhatsApp%20Image%202025-09-10%20at%2000.01.50.jpeg)
-----

### **2. Projeto de Componentes**

O sistema é modular e composto por diversos componentes (classes, interfaces e pacotes) que se organizam de acordo com a arquitetura em camadas.

#### **Diagrama de Fluxo de Processo (Criação de Tarefa)**
![diagrama-de-fluxo-de-processo](assets/WhatsApp%20Image%202025-09-10%20at%2000.02.21.jpeg)

Este diagrama ilustra o fluxo de dados e de controle para a criação de uma tarefa no sistema.

#### **Diagrama de Sequência (Geração de Relatório de Produtividade)**
![diagrama-de-sequencia](assets/WhatsApp%20Image%202025-09-10%20at%2000.01.24.jpeg)

Este diagrama detalha a interação dinâmica entre os objetos para uma das funcionalidades mais importantes do sistema.

-----

### **3. Propriedades e Princípios de Projeto**

A arquitetura do **Task Flow** foi projetada com base em princípios de design de software que garantem um sistema robusto e de fácil manutenção.

* **Separação de Interesses (Separation of Concerns)**: Cada camada tem uma responsabilidade única e bem definida. Por exemplo, o `TaskController` lida apenas com requisições HTTP e delega a lógica para o `TaskService`, que, por sua vez, foca na lógica de negócio e delega a persistência para o `TaskRepository`.

* **Injeção de Dependência (Dependency Injection)**: As dependências entre os componentes são gerenciadas pelo contêiner do Spring. Isso é visível nos construtores das classes, como em `TaskController` e `TaskServiceImpl`, que recebem suas dependências (`TaskService` e `TaskRepository`, respectivamente).


  **Exemplo de Código:**

  ```java
  // O Spring injeta a implementação de TaskService
  public class TaskController {
      TaskService taskService;
      public TaskController(TaskService taskService) {
          this.taskService = taskService;
      }
  }

  // O Spring injeta a implementação de TaskRepository
  public class TaskServiceImpl implements TaskService {
      TaskRepository taskRepository;
      public TaskServiceImpl(TaskRepository taskRepository) {
          this.taskRepository = taskRepository;
      }
  }
  ```

* **Coesão (Cohesion)**: O código dentro de um componente é logicamente relacionado. A alta coesão é evidente na arquitetura, onde cada classe (e método) foca em uma única tarefa. Por exemplo, `QuickCommandService` lida exclusivamente com a comunicação com a API externa.

* **Acoplamento (Coupling)**: O acoplamento entre as camadas é baixo. O `ReportController` não sabe os detalhes de como o relatório é gerado, apenas que ele precisa chamar o `ReportService`. Essa independência permite que a implementação do serviço possa ser alterada sem afetar o controlador. A utilização de interfaces (`TaskService`, `ReportService`) reforça esse princípio.

* **Padrão Repository**: A interface `TaskRepository` abstrai a camada de acesso a dados. O serviço de domínio (`TaskServiceImpl`) não precisa saber se os dados vêm de um banco de dados relacional, NoSQL ou de um arquivo; ele interage apenas com a interface do repositório.

-----

### **4. Métricas do Código-Fonte**

Para avaliar a qualidade do código, foram consideradas as seguintes métricas. O cálculo pode ser realizado usando plugins como o **JaCoCo** (para cobertura de código), **SonarQube** ou o plugin **Maven/Gradle Checkstyle**.

* **Tamanho (Lines of Code - LOC)**: O tamanho de cada classe e método. Classes como `TaskServiceImpl` e `ReportServiceImpl` são as maiores, pois orquestram a lógica, enquanto DTOs e repositórios são pequenos, refletindo sua responsabilidade única.
* **Coesão (Cohesion)**: Avaliada qualitativamente pela análise da distribuição de responsabilidades. A alta coesão é evidente na arquitetura, onde cada classe (e método) foca em uma única tarefa.
* **Acoplamento (Coupling)**: Medido pela quantidade de dependências entre as classes. O acoplamento é baixo, pois as dependências são injetadas via interfaces, minimizando a conexão direta entre classes concretas.
* **Complexidade Ciclomática (Cyclomatic Complexity)**: Mede o número de caminhos independentes em um método. O `ReportServiceImpl` tem uma complexidade maior devido ao loop de polling, mas isso é uma característica inerente à lógica de esperar por uma resposta assíncrona. A complexidade dos métodos CRUD simples (`createTask`, `deleteTask`) é baixa, o que é esperado.

### **5. Objetos de Melhoria do Sistema**

Com base na arquitetura e nas funcionalidades atuais do **Task Flow**, identificamos diversos pontos para futuras melhorias que podem expandir o sistema e torná-lo mais robusto e escalável.

* **Refatoração do Polling**: A estratégia de *polling* (`Thread.sleep`) para aguardar a resposta da API externa pode ser ineficiente e bloquear o *thread* de execução. Uma melhoria seria a implementação de um mecanismo **assíncrono**, utilizando **`CompletableFuture`** ou uma solução de **Webhooks**. Isso liberaria o *thread* principal para processar outras requisições enquanto aguarda a resposta.
* **Tratamento de Erros e Exceções**: A lógica atual de tratamento de erros é simples (`throw new RuntimeException`). Uma abordagem mais robusta seria a criação de **exceções personalizadas** (`TaskNotFoundException`, `ReportGenerationException`) e um manipulador global de exceções (`@ControllerAdvice`). Isso melhoraria a clareza e a consistência das respostas de erro da API.
* **Autenticação e Autorização**: Atualmente, o sistema não possui autenticação de usuário. A adição de um mecanismo de segurança, como **OAuth2** ou **JWT (JSON Web Tokens)**, é fundamental para garantir que apenas usuários autorizados possam criar e gerenciar suas próprias tarefas e relatórios.
* **Otimização do `ReportServiceImpl`**: A lógica de `removeSurroundingQuotes` poderia ser aprimorada para lidar com diferentes formatos de resposta da API externa de maneira mais resiliente, talvez utilizando a própria biblioteca Jackson para deserialização condicional.
* **Testes Automatizados**: A documentação atual descreve a arquitetura, mas a qualidade do código pode ser validada com **testes de unidade** (para a lógica de negócio) e **testes de integração** (para o fluxo completo da API com o banco de dados e serviços externos). Isso garantiria a estabilidade e evitaria regressões futuras.
* **Refatoração de Arquitetura:** A introdução de um padrão de microsserviços poderia ser considerada para separar a funcionalidade de geração de relatórios em um serviço independente. Isso permitiria escalabilidade independente e facilitaria a manutenção, isolando a lógica de negócio relacionada a relatórios e tarefas de formas distintas. É importante, também, ressaltar que, de forma independente, esses microsserviços poderiam ser desenvolvidos em diferentes linguagens ou tecnologias, conforme a necessidade e seguir um padrão arquitetural mais adequado para cada caso (aplicando uma arquitetura mais voltada para a hexagonal no caso do serviço de tarefas e MVC para o serviço de relatórios, por exemplo).
* **Lógica de validation**: A validação dos dados de entrada (DTOs) pode ser aprimorada utilizando anotações do **Bean Validation** (como `@NotNull`, `@Size`, etc.) para garantir que os dados recebidos estejam corretos antes de serem processados.
* **Atualização dos agentes de geração dos relatórios automatizados:** A integração com a Stackspot pode ser expandida para permitir a configuração dinâmica dos agentes de geração de relatórios, possibilitando que os usuários escolham diferentes modelos ou parâmetros para personalizar suas análises de produtividade. É possível, também, a criação de agentes personalizados para atender necessidades específicas dos usuários, e não necessariamente as necessidades dos estudantes da área de tecnologia.
