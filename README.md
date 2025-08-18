# 📚 Exercícios de Prática com Java 21

Este repositório contém desafios práticos para treinar conceitos modernos do **Java 21**, aplicando em **casos de uso de negócio** (sem foco apenas em algoritmos).  
Cada exercício deve ser implementado em um **módulo Maven separado**, com testes automatizados (JUnit 5) e um `README.md` próprio explicando decisões e trade-offs.  
Versões opcionais em **Spring Boot** podem ser criadas para expor APIs ou métricas.

---

## Exercício A — Agendamento de Consultas Médicas com Políticas por Especialidade
**Objetivo:** construir um serviço de marcação/cancelamento de consultas evitando conflitos e aplicando políticas específicas por especialidade.

**Requisitos:**
- Tipos de agenda: Clínico Geral, Cardiologia, Dermatologia (regras diferentes).
- Criar, remarcar, cancelar e listar por período.
- Prevenção de overbooking e bloqueios administrativos.
- Lista de espera que notifica o próximo paciente elegível.

**Critérios de aceitação:**
- Recusa de agendamentos conflitantes.
- Reagendamentos mantêm histórico.
- Lista de espera atendida na ordem correta.

**Restrições:** persistência em memória, logs legíveis, testes de conflito e reagendamento.  
**Entregáveis:** módulo `clinic-scheduling`.  
**Extensões:** endpoint REST para busca de horários, regras de prioridade (idoso/urgência).

---

## Exercício B — Orquestração de Pagamentos Interbancários (PIX/TEF/DOC) com Janelas de Liquidação
**Objetivo:** processar ordens de pagamento com diferentes trilhas (PIX instantâneo, TEF interno, DOC com janela).

**Requisitos:**
- Ingestão de ordens em lote; trilha definida por regra (valor, banco, horário).
- Compliance: limite diário e marcação de suspeitas.
- Reconciliação: relatório final de liquidadas, pendentes e rejeitadas.

**Critérios de aceitação:**
- PIX liquida instantaneamente dentro do horário.
- Limite diário respeitado; suspeitas segregadas.
- Relatório consistente com o lote.

**Restrições:** falhas intermitentes simuladas nos gateways.  
**Entregáveis:** módulo `payments-orchestration`.  
**Extensões:** métricas de throughput/latência, retries configuráveis por trilha.

---

## Exercício C — Telemetria IoT: Ingestão, Agregação e Alertas em Tempo Quase-Real
**Objetivo:** consumir leituras de sensores (temperatura, vibração, umidade) e gerar agregações por janela de tempo com alertas.

**Requisitos:**
- Ingestão de eventos (mock) com timestamp, deviceId e métricas.
- Janelas deslizantes para média/máximo por device.
- Limiar dinâmico por dispositivo.
- Emissão de alertas e enfileiramento de ações corretivas.

**Critérios de aceitação:**
- Agregações corretas por janela e device.
- Alertas apenas quando extrapolar limiar.
- Sumário final com contagem de eventos e alertas.

**Restrições:** backpressure em alta taxa de eventos.  
**Entregáveis:** módulo `iot-telemetry`.  
**Extensões:** comparação virtual threads vs. thread pool, exportação JSON.

---

## Exercício D — Matching de Corridas em Plataforma de Mobilidade
**Objetivo:** construir um *matcher* que aloca motoristas a chamadas considerando proximidade, ETA e restrições.

**Requisitos:**
- Modelo de motoristas com estado e atributos.
- Solicitação de corrida com filtros (cadeirante, pet, mala grande).
- Ranqueamento por ETA + requisitos, com empates resolvidos por ordem de chegada.
- Expiração da oferta e reoferta em caso de rejeição.

**Critérios de aceitação:**
- Oferta inicial para motorista com melhor escore.
- Expiração e reoferta corretas.
- Métricas: taxa de aceitação, tempo médio até alocação.

**Restrições:** simular atrasos/respostas dos motoristas.  
**Entregáveis:** módulo `ride-matching`.  
**Extensões:** batching em picos, política de cooldown por motorista.

---

## Exercício E — Gestão de Assinaturas de Streaming com Regras de Faturamento
**Objetivo:** gerenciar assinaturas com troca de plano, *trial*, cancelamento e faturamento pró-rata.

**Requisitos:**
- Planos (Básico, Padrão, Premium).
- Eventos: início de *trial*, conversão, upgrade/downgrade, cancelamento.
- Faturamento pró-rata por período.
- Emissão de invoice e extrato por assinante.

**Critérios de aceitação:**
- *Trial* não cobra; conversão cobra proporcional.
- Upgrade imediato gera cobrança complementar.
- Extrato consistente com eventos.

**Restrições:** config externa para planos e impostos.  
**Entregáveis:** módulo `subscriptions-billing`.  
**Extensões:** simulador de conta futura, regras promocionais (cupom/meses grátis).

---

## Exercício F — Controle de Acesso Físico Corporativo com Auditoria e Anomalias
**Objetivo:** processar eventos de catracas/portas com regras de acesso por perfil e detecção de anomalias.

**Requisitos:**
- Perfis: Funcionário, Terceiro, Visitante.
- Eventos de entrada/saída com janela para par válido.
- Anomalias: entradas duplicadas, saída sem entrada, acesso a área restrita.
- Relatório diário de acessos e anomalias.

**Critérios de aceitação:**
- Acesso permitido/negado conforme perfil e horário.
- Todas anomalias detectadas corretamente.
- Relatório preserva ordem dos eventos.

**Restrições:** tolerância a eventos fora de ordem.  
**Entregáveis:** módulo `physical-access-control`.  
**Extensões:** dupla validação em áreas críticas, cálculo de tempo em área.

---

## 📌 Regras Gerais
- Cada exercício deve ser um **módulo Maven separado**.
- Implementar testes com **JUnit 5**, cobrindo caminhos de sucesso e falha.
- Criar um **README.md** por módulo com decisões e trade-offs.
- Versões em **Spring Boot** são opcionais, para expor APIs ou métricas.
