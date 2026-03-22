## Table des matières

1. [Introduction](#introduction)
2. [Fonctionnalités](#fonctionnalités)
3. [Architecture technique](#architecture-technique)
4. [Technologies utilisées](#technologies-utilisées)
5. [Installation et déploiement](#installation-et-déploiement)
6. [Modèle de données](#modèle-de-données)
7. [API REST](#api-rest)
8. [Sécurité](#sécurité)
9. [Tests](#tests)
10. [Évolutions futures](#évolutions-futures)

---

## Introduction

Dans un environnement professionnel moderne, la gestion des congés et absences est cruciale pour une planification efficace des ressources humaines. Les méthodes manuelles (Excel, formulaires papier) entraînent des erreurs et un manque de visibilité.

**RestTime** propose une solution numérique intuitive et performante pour :

* Automatiser les demandes et validations de congés
* Faciliter la communication entre employés, managers et RH
* Assurer la sécurité et la traçabilité des données

**Périmètre :**

* Gestion des utilisateurs et des rôles
* Demandes et validations de congés
* Déclaration d’absences et justificatifs
* Tableau de bord pour le service RH

---

## Fonctionnalités

### Gestion des utilisateurs

* Création, modification et suppression des comptes
* Attribution de rôles (Employé, Manager, RH, Administrateur)
* Gestion des informations personnelles et mots de passe
* Authentification sécurisée et déconnexion

### Gestion des congés

* Soumission de demandes de congés avec type (annuel, maladie, exceptionnel)
* Consultation du solde de congés
* Historique des demandes approuvées/refusées
* Annulation de demandes avant approbation
* Gestion automatique des week-ends et jours fériés

### Gestion des absences

* Déclaration d’absences imprévues
* Gestion des justificatifs (certificats médicaux, attestations)
* Suivi des absences par RH et manager

### Validation et notifications

* Workflow hiérarchique : Employé → Manager → RH
* Notifications automatiques pour chaque action
* Historique et traçabilité des décisions

---

## Architecture technique

L’architecture suit le modèle client-serveur :

```
Frontend Angular ←→ Backend Spring Boot ←→ PostgreSQL
```

**Backend :** Spring Boot avec Spring Security, JPA/Hibernate, gestion JWT
**Frontend :** Angular, modules modulaires, communication via HttpClient
**Base de données :** PostgreSQL 15, relationnelle, optimisée pour les flux RH

**Structure du projet :**

* Backend : Controller, Service, Repository, Entity, Security, DTO/Mapper
* Frontend : Modules, Components, Services, Guards, Routing, Shared

---

## Technologies utilisées

* **Backend :** Spring Boot, Java 17, Maven
* **Frontend :** Angular 15+
* **Base de données :** PostgreSQL 16
* **Outils de dev :** IntelliJ IDEA, Git/GitHub, Postman, Swagger UI

---

## Installation et déploiement

### Pré-requis

* Java 17
* Node.js + Angular CLI
* PostgreSQL 16

### Déploiement

1. Cloner le projet : `git clone (https://github.com/amhine/RestTime.git)`
2. Configurer la base de données et variables d’environnement :

```bash
DB_URL_DEV=jdbc:postgresql://localhost:5432/nom_db
DB_USER_DEV=user
DB_PASSWORD_DEV=password
```

3. Backend :

```bash
cd backend
mvn clean install
java -jar target/resttime.jar
```

4. Frontend :

```bash
cd frontend
npm install
ng build --prod
```

5. Servir le frontend via Nginx ou Apache et vérifier l’accès via le navigateur

---

## Modèle de données

**Entités principales :**

* Utilisateur (User) : id, nom, prénom, email, motDePasse, rôle, soldeCongé
* Rôle (Role) : id, nomRole, description
* Demande de congé (LeaveRequest) : id, dateDébut, dateFin, typeCongé, statut
* Type de congé (LeaveType) : id, nomType, description, nombreJoursMax
* Absence (Absence) : id, dateAbsence, motif, idUtilisateur
* Notification : id, message, dateEnvoi, vu
* Historique : id, action, dateAction

**Relations :**

* Utilisateur possède un Rôle
* Utilisateur peut avoir plusieurs demandes de congé et absences
* Une Demande peut générer plusieurs Notifications et Historique

---

## API REST

**Endpoints principaux :**

* `POST /api/auth/login` : authentification
* `POST /api/auth/register` : création d’utilisateur
* `GET /api/users` : liste des utilisateurs (admin)
* `GET /api/leaves` : récupérer demandes
* `POST /api/leaves` : soumettre une demande
* `PUT /api/leaves/{id}` : validation/refus
* `GET /api/notifications` : notifications
* `GET /api/absences` : consulter absences

**Format :** JSON pour toutes les requêtes et réponses

---

## Sécurité

* Authentification via JWT
* Gestion des rôles et permissions (Employé, Manager, RH, Admin)
* Communication sécurisée HTTPS
* Chiffrement des mots de passe
* Conformité RGPD

---

## Tests

* **Unitaires :** JUnit 5, Mockito (backend), Karma/Jasmine (frontend)
* **Intégration :** tests API REST, flux métier complet

---

## Évolutions futures

* Application mobile (Android/iOS)
* Rapports analytiques avancés
* Notifications push et alertes temps réel
* Intégration ERP / paie
* Gestion multi-entreprise et multi-sites

---

