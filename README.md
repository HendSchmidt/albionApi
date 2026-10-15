# Albion API (Spring Boot 3 + Java 21) ⚔️🛡️

API RESTful para cálculo de viabilidade econômica de produção, retorno de recursos (RRR), consumo de nutrição/taxa de barraca, receitas culinárias e equipamentos no jogo **Albion Online**.

Integrada ao frontend interativo [albionFrontend](https://github.com/HendSchmidt/albionFrontend).

---

## 📋 Pré-requisitos

Para rodar este projeto em sua máquina local, certifique-se de possuir instalado:

- **Java JDK 21** ou superior (verifique com `java -version`)
- **Git** (verifique com `git --version`)
- **Maven 3.9+** (opcional, o repositório já inclui o wrapper `./mvnw`)

---

## 🚀 Como Executar em Ambiente Local

### 1. Clonar o repositório

```bash
git clone https://github.com/HendSchmidt/albionApi.git
cd albionApi
```

### 2. Executar a aplicação com Maven Wrapper

O projeto já vem configurado com Maven Wrapper (`mvnw` no Linux/macOS ou `mvnw.cmd` no Windows). Não é necessário ter o Maven instalado globalmente:

#### No Windows (PowerShell / Prompt de Comando):
```powershell
.\mvnw.cmd clean spring-boot:run
```

#### No Linux / macOS:
```bash
chmod +x ./mvnw
./mvnw clean spring-boot:run
```

> **Dica:** Se você já tiver o Maven instalado globalmente, basta executar `mvn clean spring-boot:run`.

A API iniciará por padrão na porta **`8080`**:
```
Started ApiApplication in 2.5 seconds (process running at http://localhost:8080)
```

---

## 🗄️ Banco de Dados H2 e Seeder de Receitas

A API utiliza o banco de dados embutido **H2** persistido em arquivo local (`./data/albiondb`), garantindo que nenhuma receita salva seja perdida ao reiniciar o servidor.

- **Carga Automática:** Ao iniciar, o `DatabaseSeeder` popula automaticamente as **18 receitas oficiais de culinária** do Albion Online caso ainda não existam no banco.
- **H2 Console Web:** Acesse pelo navegador em [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - **JDBC URL:** `jdbc:h2:file:./data/albiondb;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE`
  - **User Name:** `sa`
  - **Password:** *(deixar em branco)*

---

## 📡 Endpoints Principais

A API possui suporte nativo a CORS (`CorsConfig`) habilitado para `http://localhost:3000` e origens locais:

### 1. Cálculo de Viabilidade de Craft (Equipamentos / Culinária / Alquimia)
- **Método:** `POST`
- **Rotas disponíveis (com ou sem contexto):**
  - `http://localhost:8080/calculaViabilidadePorRecurso`
  - `http://localhost:8080/projecaoFaturamento/calculaViabilidadePorRecurso`
  - `http://localhost:8080/albionApi/projecaoFaturamento/calculaViabilidadePorRecurso`

**Exemplo de Payload (JSON):**
```json
{
  "nomeItem": "Capa de Caerleon do Adepto",
  "categoriaProducao": "EQUIPAMENTOS",
  "quantidadeParaProducao": 1,
  "taxaRetorno": 0.0,
  "precoVendaUnitario": 91000.0,
  "taxaEstacaoPorCemNutricao": 800.0,
  "itemValue": 160.0,
  "contaPremium": true,
  "vendaInstantanea": false,
  "recursos": [
    { "nome": "Capa do Adepto", "quantidade": 1, "valor": 15900.0 },
    { "nome": "Ornamento de Caerleon do Adepto", "quantidade": 1, "valor": 12800.0 },
    { "nome": "Coração Sombrio", "quantidade": 1, "valor": 46500.0 }
  ]
}
```

### 2. Venda de Comida para Barraquinhas (Nutrição)
- **Método:** `POST`
- **Rotas:** `/calculaVendaComidaBarraquinha` ou `/projecaoFaturamento/calculaVendaComidaBarraquinha`

### 3. Catálogo de Receitas Salvas
- **Listar receitas:** `GET /itensFabricados` ou `/albionApi/itensFabricados`
- **Salvar nova receita:** `POST /itensFabricados`
- **Buscar por ID:** `GET /itensFabricados/{id}`
- **Excluir receita:** `DELETE /itensFabricados/{id}`

---

## 🧪 Como Rodar os Testes Automatizados

Para executar toda a suíte de testes unitários e de integração:

```bash
# Windows
.\mvnw.cmd test

# Linux / macOS
./mvnw test
```

---

## 🤝 Conexão com o Frontend

1. Deixe o backend rodando em `http://localhost:8080`.
2. No repositório [albionFrontend](https://github.com/HendSchmidt/albionFrontend), execute `npm run dev`.
3. O frontend conectará automaticamente com o backend em `http://localhost:8080` com CORS já liberado.
