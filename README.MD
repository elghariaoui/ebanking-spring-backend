# ebanking-spring-boot-backend

Ce projet est une application Java basée sur Spring Boot et Maven, et utilise Spring Data JPA pour la gestion des données. Il fournit une API RESTful pour la gestion des comptes bancaires, avec des opérations de création, lecture, mise à jour et suppression (CRUD).

## Prérequis

- Java 17 ou supérieur
- Maven 3.8+
- (Optionnel) MySQL ou autre base de données compatible (H2 est utilisé par défaut)

## Installation

Clonez le dépôt:

```bash
git clone https://github.com/elghariaoui/ebanking-spring-backend.git
cd votre-projet
mvn clean install
```
## Lancement de l'application
Pour lancer l'application, utilisez la commande suivante:

```bash
mvn spring-boot:run
```
L'application sera accessible à l'adresse `http://localhost:8085`.
## Configuration de la base de données
Si vous utilisez MySQL, assurez-vous de configurer les paramètres de connexion dans le fichier `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/votre_base_de_donnees
spring.datasource.username=votre_utilisateur
spring.datasource.password=votre_mot_de_passe
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
Si vous utilisez H2, aucune configuration supplémentaire n'est nécessaire car H2 est intégré par défaut.

## Fonctionnalités

- Gestion des comptes avec CRUD (Create, Read, Update, Delete)
- API RESTful pour interagir avec les produits
- Intégration avec Spring Data JPA pour la persistance des données
- Utilisation des DTOs pour la validation des données
- Documentation de l'API avec Swagger

