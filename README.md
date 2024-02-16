# Management Employee System

> Microserviço para administrar uma empresa e seus funcionários. 
> Este projeto oferece integração com a AWS, infraestrutura criada via terraform, autenticação com token JWT e uso do Spring WebFlux

## 💻 Pré-requisitos

Antes de começar, verifique se você atendeu aos seguintes requisitos:
* Você tem instalado e configurado na sua máquina o `Java 17`.
* Você tem instalado e configurado `Terraform`.
* Você tem instalado e configurado o gerenciador de dependêcias `Maven`.
* Você tenha conhecimento em desenvolvimento com a linguagem `Java` e o framework `Spring Boot` e cloud `AWS`.
* Você possui o `Postman` instalado
* Ter a `aws-cli` instalada

## 🔨 Preparando ambiente

Para que o projeto funcione, você precisará provisionar os recursos necessários na `AWS`.
Instale a `aws-cli` que é a linha de comando da aws e configure com as credencias da sua conta com o comando:

```
aws configure
```

Após isso navegue até as pasta `terraform` do projeto e excute os comandos para criar a infra:

```
terraform init
```

```
terraform plan
```

```
terraform apply
```

Opós isso troque os `arns` dos recursos da aws que foram criados para sua conta no `application-local.yml` do projeto.
Também certifique-se de configurar o user que assumirá a role dentro do arquivo `/home/user/./aws/credentials`:
```
[assume_role]
aws_access_key_id=********
aws_secret_access_key=*********
```

## 🚀 Executando o projeto

Para criar o build do projeto execute o comando:
```
mvn clean install
```

Para iniciar a aplicação execute o comando:
```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```