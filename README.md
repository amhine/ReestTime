

# 🕰️ RestTime 

RestTime est une application web d’entreprise moderne conçue pour gérer le pointage des employés et les demandes de congés. Elle aide les services RH à suivre les employees, les absences et à améliorer l’efficacité des processus internes.

---

## 📌 Contexte du Projet

RestTime est un outil complet destiné aux employés et aux ressources humaines. Il offre une solution fluide pour :

* Suivre les employees
* Gérer les cycles de congés
* Faciliter la communication interne

Grâce à un tableau de bord moderne et interactif ainsi qu’à des workflows automatisés, l’application réduit les tâches administratives et améliore la productivité globale.

---

## 🚀 Fonctionnalités Principales

### 👤 Employé

* **Authentification sécurisée** : Connexion via JWT
* **Pointage** : Système simple de check-in / check-out
* **Demandes de congés** : Soumission facile (annuel, maladie, etc.)
* **Ajout de justificatifs** : Upload de fichiers PDF
* **Tableau de bord personnel** :

  * Solde de congés
  * Historique de présence
  * Statut des demandes
* **Notifications en temps réel** : Validation ou refus des demandes
* **Annonces** : Consultation des communications globales

---

### 👔 RH / Administrateur

* **Gestion des employés** : CRUD complet des utilisateurs
* **Validation des congés** : Accepter ou refuser en un clic
* **Suivi des présences** : Vue en temps réel des pointages
* **Statistiques & rapports** : Analyse globale de l’activité
* **Communication interne** : Publication d’annonces
* **Historique système** : Traçabilité des actions (audit)

---

## 🏗️ Architecture du Projet

RestTime suit une architecture propre, modulaire et en couches :

* **Frontend** : Application SPA moderne avec Angular
* **Backend** : API REST Spring Boot
* **Base de données** : PostgreSQL

### Points techniques clés :

* **API RESTful** : Communication stateless
* **Sécurité JWT** : Authentification sécurisée
* **Architecture Dockerisée** : Déploiement simplifié et cohérent

---

## 🛠️ Stack Technique

### 🔙 Backend

* Spring Boot 3
* Spring Security (JWT)
* Hibernate / JPA
* PostgreSQL

### 🎨 Frontend

* Angular 17
* Tailwind CSS 
* RxJS

### ⚙️ DevOps

* Docker
* Docker Compose
* GitHub Actions (CI/CD)

---

## ⚙️ Installation & Exécution

### 1. Cloner le projet

```bash
git clone https://github.com/amhine/ReestTime.git
cd restTime_v
```

### 2. Lancer l’application

```bash
docker compose up --build
```

### 3. Accéder à l’application

* **Frontend** : [http://localhost:8080](http://localhost:8080)
* **Backend API** : [http://localhost:8083/api](http://localhost:8083/api)

> ⚠️ Assurez-vous que les ports **8080** et **8083** sont libres.

---

## 📊 Diagrammes


<div style="text-align: center;">
  
<img width="1078" height="777" alt="image" src="https://github.com/user-attachments/assets/e0d34c87-b889-4127-a385-c04988deb13c" />
</div>

<img width="745" height="1141" alt="Diagramme " src="https://github.com/user-attachments/assets/5fbf5a10-0a6c-4eee-9081-0a69ae457d76" />


---



## 📌 Notes Supplémentaires

* 🔐 Toutes les routes sécurisées nécessitent un **token JWT**
* 📂 Upload de fichiers via `MultipartFile`
* 🔔 Notifications en temps réel via **WebSocket**
* 📈 Architecture conçue pour être **scalable et maintenable**

