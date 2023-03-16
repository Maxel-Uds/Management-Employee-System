# Management Employee System

> Microserviço para administrar uma empresa e seus funcionários. 
> Este projeto oferece integração com a AWS, infraestrutura criada via terraform, autenticação com token JWT e uso do Spring WebFlux

### Ajustes e melhorias

O projeto ainda está em desenvolvimento e as próximas atualizações serão voltadas nas seguintes tarefas:

- [x] Implementação da geração do token JWT
- [ ] Criação da infraestrutura pelo terraform
- [ ] Criar a lógica para possibilitar o uso da API

## 💻 Pré-requisitos

Antes de começar, verifique se você atendeu aos seguintes requisitos:
* Você tem instalado e configurado na sua máquina o `Java 17`.
* Você tem instalado e configurado `Terraform`.
* Você tem instalado e configurado o gerenciador de dependêcias `Maven`.
* Você tenha conhecimento em desenvolvimento com a linguagem `Java` e o framework `Spring Boot`.

## 🚀 Executando o projeto

Para criar o build do projeto execute o comando:
```
mvn clean install
```

Para iniciar a aplicação execute o comando:
```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```