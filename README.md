# SmartTruck - Sistema de Chatbot para Diagnóstico de Caminhões

## 📋 Sobre o Projeto
O SmartTruck é um sistema de chatbot que ajuda a diagnosticar problemas em caminhões através de códigos de erro. Usando WhatsApp ou Telegram, os usuários podem enviar códigos de erro e receber soluções automaticamente através de Inteligência Artificial.

## 🎯 Objetivo do Projeto
Este projeto serve como exemplo prático de uma aplicação Java moderna, utilizando:
- Spring Boot para criar APIs REST
- Chatbots (WhatsApp e Telegram)
- Inteligência Artificial para respostas automáticas
- Arquitetura Limpa (Clean Architecture)

## 📁 Estrutura de Pastas

### `/src/main/java/com/smarttruck`
Aqui fica todo o código Java do projeto, organizado em pacotes seguindo a Arquitetura Limpa:

#### 📦 `/application`
A camada de aplicação coordena o fluxo de dados entre a camada de apresentação e a camada de domínio.

- `/service`: Serviços que implementam a lógica de negócio usando as interfaces do domínio
- `/usecase`: Casos de uso da aplicação, que definem as operações que o sistema pode realizar

#### 🛠 `/config`
Configurações do Spring Boot e outros frameworks.
- Configuração de beans
- Configuração de segurança
- Configuração dos chatbots

#### 🏭 `/domain`
O coração do sistema! Aqui ficam as regras de negócio puras.

- `/model`: As entidades principais do sistema (classes que representam conceitos do negócio)
- `/repository`: Interfaces que definem como acessar os dados (sem detalhes de implementação)

#### 🔧 `/infrastructure`
Implementações concretas de interfaces definidas no domínio.

- `/persistence`: Implementações dos repositórios usando JPA
- `/restclient`: Clientes REST para APIs externas

#### 🎨 `/presentation`
A interface com o mundo exterior!

- `/controller`: Controllers da API REST
- `/dto`: Objetos de Transferência de Dados (DTOs)
- `/mapper`: Conversores entre DTOs e modelos do domínio

#### 🔄 `/shared`
Componentes compartilhados entre todas as camadas.
- Utilitários
- Constantes
- Exceções personalizadas

### `/src/main/resources`
Arquivos de configuração e recursos.
- `application.yml`: Configurações do Spring Boot
- Templates de mensagens
- Scripts SQL (se necessário)

### `/src/test`
Testes automatizados, seguindo a mesma estrutura do código principal.
- Testes unitários
- Testes de integração
- Testes de API

## 🔄 Fluxo de Dados na Arquitetura Limpa
1. O usuário envia mensagem pelo WhatsApp/Telegram
2. A mensagem chega na camada de Presentation (Controllers)
3. O Controller converte a requisição em um caso de uso
4. O caso de uso coordena a lógica usando os serviços
5. Os serviços usam as entidades do domínio e repositórios
6. Os repositórios salvam/recuperam dados usando a infraestrutura
7. A resposta volta pelo mesmo caminho até o usuário

## 🚀 Como Executar o Projeto
1. Clone o repositório
2. Configure as variáveis de ambiente para os bots:
   ```properties
   TELEGRAM_BOT_USERNAME=seu_bot
   TELEGRAM_BOT_TOKEN=seu_token
   WHATSAPP_VERIFY_TOKEN=seu_token
   WHATSAPP_ACCESS_TOKEN=seu_token
   WHATSAPP_APP_SECRET=seu_secret
   ```
3. Execute o projeto usando Maven:
   ```bash
   mvn spring-boot:run
   ```

## 📚 Links Úteis para Estudo
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Arquitetura Limpa](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [API do Telegram](https://core.telegram.org/bots/api)
- [API do WhatsApp](https://developers.facebook.com/docs/whatsapp/api/overview)
