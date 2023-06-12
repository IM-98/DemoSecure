# demosecure

Projet demo pour mettre en place un back avec securisation par token JWT

# Technos
 - Spring Boot
 - Spring Security
 - JWT Token
 - JPA
 - MariaDB


# Pre-requis
- Avoir un serveur MariaDB

## Base de données
- Vous devez créer une base de données **demosecure**.

```SQL
create database demosecure;
```

- Vous devez avoir une variable d'environnement **MYSQL_ROOT_PASS** qui a pour valeur le mot de passe root du serveur de base de données
- Importez le fichier dump `sql/dump-demosecure.sql` (via DBeaver par exemple) dans cette base.

# Installation

C'est un projet maven, il faut donc mettre à jour le projet maven (récupération des librairies) et builder le projet.
Ensuite, vous pouvez lancer le projet.

# Tests
Vous trouverez dans le répertoire **postman**, une collection Postman pour tester les routes. Elle nécessite de créer une **variable d'environnement** nommée **token** dans Postman.

- La requête **login/signup john** cree un user en base
- La requête **login/signup marc** cree un user en base
- La requête **login/signin bad password** test une connexion avec mot de passe erroné
- La requête **login/signin john** test une connexion avec mot de passe correct
  - on récupère le token renvoyé par cette requête et on l'injecte dans la variable d'environnement
- La requête **user/get user test** test la route **api/user/hello** qui est sécurisée
  - le token est injecté automatiquement dans la requête

# Ce que vous devez faire
Votre responsable vous fait remarquer que vous ne créez que des users. Il vous demande de créer une route qui permette de créer des users ADMIN.
Cette route ne serait accessible qu'aux utilisateurs avec le rôle ADMIN.
Cela signifie que le premier compte ADMIN devra être créé manuellement directement en base de données.

Vous devez également ajouter deux requêtes dans la collection POSTMAN pour tester votre travail.
- Une requête qui crée un compte admin avec un token qui appartient à un compte ADMIN
- Une requête qui tente de créer un compte admin avec un token qui appartient à un compte non ADMIN. La demande est censée être refusée.
- Vous testerez à chaque fois le retour pour valider ou non le test.

