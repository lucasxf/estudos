# 🧭 Snapshot Consolidado — Projeto `subscriptions-billing` (CQRS + ES + DDD + Saga)

> Este snapshot consolida toda a arquitetura, estrutura de código e diretrizes do projeto **subscriptions-billing**, baseado em **CQRS + Event Sourcing + Domain-Driven Design (DDD)**.  
> Ele serve como **ponto de partida** para continuidade dos estudos, evolução do código e preparação de **Lucas Xavier Ferreira** para processos seletivos de engenharia de software em empresas de ponta.

---

## 🧩 1. Contexto e Objetivo

O projeto **`subscriptions-billing`** foi concebido como o exercício **E** do plano de estudos prático em *Java 21 com Spring Boot e Maven* — com o objetivo de consolidar os conceitos de **Event Sourcing**, **CQRS**, **Process Managers (Sagas)** e **arquitetura reativa orientada a eventos**.

### 🎯 Objetivo técnico
Modelar e implementar um sistema de **gestão de assinaturas de streaming**, com operações de criação de conta, reserva e vinculação de nome de usuário, início de período de teste (*trial*), conversão de plano, upgrade/downgrade e cancelamento.

---

## 🧱 2. Arquitetura e Pilares

A arquitetura segue três camadas principais:

1. **Application Layer** — coordena casos de uso e orquestra fluxos cross-domain.  
   - Contém *controllers*, *command handlers* e (em breve) *process managers (sagas)*.

2. **Domain Layer** — núcleo da modelagem de negócio.  
   - Contém *aggregates*, *commands*, *events* e *value objects*.

3. **Infrastructure Layer** — suporte técnico e persistência de eventos.  
   - Contém *repositories*, *event store*, *serialization*, *messaging* e *event bus*.

O projeto adota **Event Sourcing** como mecanismo de persistência e **CQRS** como estratégia de separação entre o *write-side* (comandos, decisões, eventos) e o *read-side* (projeções, queries).

---

## 📂 3. Estrutura de Pastas

```bash
├───main
│   └───java
│       └───exercicio_e
│           └───subscriptions_billing
│               │   Application.java
│               │
│               ├───application
│               │   ├───api
│               │   │   │   AccountController.java
│               │   │   │   SubscriptionController.java
│               │   │   │
│               │   │   └───dto
│               │   │           AccountResponse.java
│               │   │           CreateAccountRequest.java
│               │   │
│               │   ├───commands
│               │   │       AccountCommandHandler.java
│               │   │       SubscriptionCommandHandler.java
│               │   │
│               │   └───process
│               │           AccountSignupProcessManager.java  ← (WIP)
│               │
│               ├───domain
│               │   ├───account
│               │   │   │   AccountAggregate.java
│               │   │   │   AccountStatus.java
│               │   │   │
│               │   │   ├───command
│               │   │   │       AccountCommand.java
│               │   │   │
│               │   │   └───event
│               │   │           AccountEvent.java
│               │   │
│               │   ├───subscription
│               │   │   │   SubscriptionAggregate.java
│               │   │   │   SubscriptionState.java
│               │   │   │
│               │   │   ├───command
│               │   │   │       SubscriptionCommand.java
│               │   │   │
│               │   │   ├───event
│               │   │   │       TrialStarted.java
│               │   │   │       SubscriptionConverted.java
│               │   │   │       SubscriptionCanceled.java
│               │   │   │       PlanUpgraded.java
│               │   │   │       PlanDowngraded.java
│               │   │   │
│               │   │   └───plan
│               │   │           Plan.java
│               │   │
│               │   ├───username
│               │   │   │   UsernameAggregate.java
│               │   │   │   UsernameStatus.java
│               │   │   │
│               │   │   ├───command
│               │   │   │       UsernameCommand.java
│               │   │   │
│               │   │   └───event
│               │   │           UsernameEvent.java
│               │   │
│               │   └───exception
│               │           AccountCreationException.java
│               │           DomainException.java
│               │           InvalidAccountException.java
│               │           InvalidUsernameException.java
│               │           UsernameUnavailableException.java
│               │
│               └───infrastructure
│                   ├───eventstore
│                   │       EventStore.java
│                   │       StoredEvent.java
│                   │
│                   ├───messaging
│                   │       EventBus.java
│                   │       EventEnvelope.java
│                   │       EventHandler.java
│                   │
│                   ├───repository
│                   │   │   AbstractEventSourcingRepository.java
│                   │   │   AccountRepository.java
│                   │   │   SubscriptionRepository.java
│                   │   │   UsernameRepository.java
│                   │   │   SubscriptionReadRepository.java
│                   │   │
│                   │   └───impl
│                   │           AccountRepositoryImpl.java
│                   │           SubscriptionRepositoryImpl.java
│                   │           UsernameRepositoryImpl.java
│                   │
│                   └───serialization
│                           EventMapper.java
│                           EventTypeRegistry.java
│                           SerializedBatch.java
│
└───test
    └───java
        └───exercicio_e
            └───subscriptions_billing
                └───domain
                    └───account
                            AccountAggregateTest.java
```

---

## ⚙️ 4. Ciclo de Vida do Fluxo de Criação de Conta

### 🧵 Passo a Passo
1. **AccountController** recebe um `POST /account` com `CreateAccountRequest`.  
2. Converte para `CreateAccountCommand`.  
3. **AccountCommandHandler (ACH)** executa:  
   - Reserva do username (via `UsernameAggregate` + `UsernameRepository`).  
   - Criação da conta (`AccountAggregate`).  
   - Reclamação (claim) do username.  
   - Início do trial (`SubscriptionAggregate`).  
4. Cada repositório (`UR`, `AR`, `SR`) persiste eventos em memória via `EventStore`.  
5. Cada `append()` retorna uma lista de `StoredEvent`, que será publicada no **EventBus**.  
6. O **Process Manager (Saga)** (`AccountSignupPM`) orquestra a sequência cross-domain:  
   - Ao receber `UsernameReserved`, emite `CreateAccountCommand`.  
   - Ao receber `AccountCreated`, emite `ClaimUsernameCommand`.  
   - Ao receber `UsernameClaimed`, emite `StartTrialCommand`.  
7. Quando `TrialStarted` é publicado, o **AccountAggregate** pode transitar para o estado `ACTIVE`.  

---

## 🧠 5. Conceitos Fundamentais de Domínio

| Conceito | Papel no Projeto | Exemplo |
|-----------|------------------|----------|
| **Aggregate Root** | Unidade de consistência transacional e fonte de verdade do estado. | `AccountAggregate`, `UsernameAggregate`, `SubscriptionAggregate` |
| **Command** | Intenção do usuário ou sistema para alterar o estado. | `CreateAccount`, `StartTrial`, `ClaimUsername` |
| **Event** | Fato consumado imutável publicado após uma decisão. | `AccountCreated`, `TrialStarted`, `UsernameClaimed` |
| **Repository** | Persiste e reconstitui o histórico de eventos de um Aggregate. | `AccountRepository`, `UsernameRepository` |
| **Event Store** | Infraestrutura que grava e lê eventos versionados. | `EventStore` + `StoredEvent` |
| **Event Bus** | Canal de publicação e assinatura de eventos entre domínios. | `EventBus`, `EventEnvelope`, `EventHandler` |
| **Process Manager (Saga)** | Orquestra processos de longa duração entre Aggregates distintos. | `AccountSignupProcessManager` |
| **Projection (Read Model)** | Materializa dados do write-side para leitura otimizada. | `SubscriptionReadRepository` (WIP) |

---

## 🔄 6. Papel do Process Manager (Saga)

O **AccountSignupProcessManager** será responsável por reagir aos eventos e emitir comandos subsequentes, garantindo consistência eventual entre `Username`, `Account` e `Subscription` sem acoplamento direto entre domínios.

### 📜 Fluxo orquestrado:
| Evento recebido | Ação do Process Manager |
|-----------------|--------------------------|
| `UsernameReserved` | Emite `CreateAccountCommand` |
| `AccountCreated` | Emite `ClaimUsernameCommand` |
| `UsernameClaimed` | Emite `StartTrialCommand` |
| `TrialStarted` | Atualiza `AccountStatus` → `ACTIVE` (via evento refletido ou projection) |

### 💡 Observação:
> O Process Manager reage a **eventos**, não comandos — mas **pode emitir novos comandos** para iniciar novas transações de domínio.

---

## 🧪 7. Testes e Qualidade

Os testes unitários seguem a convenção:
```
src/test/java/exercicio_e/subscriptions_billing/domain/account/AccountAggregateTest.java
```

Cada *Aggregate* deve ter seu próprio teste de decisão (decide) e reconstituição (replay).  
Exemplo: validar que `AccountAggregate.decide(CreateAccount)` emite `AccountCreated` e muda o estado para `NEW`.

---

## 🚀 8. Backlog / Próximos Passos / Evoluções Futuras

> 🟢 **P0** = execução imediata | 🟡 **P1** = curto prazo | 🔵 **P2** = médio prazo

### 🟢 **P0 — Imediato**
1. **Implementar o Process Manager (Saga)**  
   Criar `AccountSignupProcessManager` na camada `application.process`, reagindo aos eventos `UsernameReserved`, `AccountCreated`, `UsernameClaimed`, e `TrialStarted`.  
   Deve emitir comandos (`CreateAccount`, `ClaimUsername`, `StartTrial`) conforme a orquestração.

2. **Integrar EventBus com EventStore**  
   - Publicar eventos após cada `append()` nos repositórios.  
   - Converter `StoredEvent` → `EventEnvelope` com `EventMapper`.  
   - Suportar `subscribe()` dinâmico para o Process Manager.

3. **Melhorar cobertura de testes unitários (JUnit 5)**  
   - Criar cenários de sucesso e falha nos Aggregates e CommandHandlers.  
   - Validar versionamento e consistência do replay.

### 🟡 **P1 — Curto Prazo**
1. **Adicionar Observabilidade e Logging**  
   - Integrar `SLF4J` + `Logback`.  
   - Logs de correlação (`correlationId`, `causationId`).  
   - Métricas básicas via Actuator (quando migrar para Spring Boot).

2. **Projeções (Read Side)**  
   - Implementar `SubscriptionProjection` para consultas rápidas de status e plano.  
   - Sincronizar a partir de eventos `TrialStarted`, `SubscriptionConverted`, etc.

3. **Documentação OpenAPI + Swagger UI**  
   - Gerar docs automáticas da camada REST.  

### 🔵 **P2 — Médio Prazo**
1. **Persistência real (PostgreSQL ou MongoDB)**  
   - Modelar EventStore real.  
   - Avaliar trade-offs entre consistência e disponibilidade (CAP).

2. **Caching (Redis)**  
   - Cachear leituras e projeções.  

3. **Mensageria real (Kafka/RabbitMQ)**  
   - Substituir EventBus in-memory por integração com Kafka.  

4. **Containerização (Docker)**  
   - Empacotar o módulo `subscriptions-billing` com Dockerfile e docker-compose para dependências (DB + Kafka).

---

## 📘 9. Convenções e Abreviações

| Sigla/Abreviação | Significado                                                                   |
| ---------------- | ----------------------------------------------------------------------------- |
| **<D>CH**        | `<D>` CommandHandler (ex.: AccountCommandHandler, SubscriptionCommandHandler) |
| **Acc**          | Account                                                                       |
| **AccAgg**       | AccountAggregate                                                              |
| **AccRepo**      | AccountRepository                                                             |
| **ACH**          | AccountCommandHandler                                                         |
| **Agg**          | Aggregate                                                                     |
| **CMD**          | Command                                                                       |
| **EB**           | EventBus                                                                      |
| **ENV**          | EventEnvelope                                                                 |
| **Evt**          | Event                                                                         |
| **Hndlr**        | Handler                                                                       |
| **Mngr/Mgr**     | Manager                                                                       |
| **PM**           | ProcessManager                                                                |
| **Pub/Sub**      | Publish/Subscribe `OU` Publisher/Subscriber                                   |
| **Repo**         | Repository                                                                    |
| **Req**          | Request                                                                       |
| **Resp**         | Response                                                                      |
| **StEvt**        | StoredEvent                                                                   |
| **Subs**         | Subscription                                                                  |
| **SubsAgg**      | SubscriptionAggregate                                                         |
| **SubsRepo**     | SubscriptionRepository                                                        |
| **UnAgg**        | UsernameAggregate                                                             |
| **UnR**          | UsernameRepository                                                            |
| **VO**           | Value Object                                                                  |
| **WIP**          | Work In Progress / Em andamento                                               |

---

## 💡 10. Notas Finais

Este projeto funciona como um **laboratório profissional de engenharia moderna**, consolidando fundamentos aplicáveis em entrevistas técnicas de alto nível.  
Ao dominar os fluxos de **Event Sourcing + CQRS + Process Managers**, você estará preparado para discutir **consistência eventual, compensações de design, versionamento e idempotência** — tópicos muito relevantes para a atuação de engenharia de software em alto nível.
