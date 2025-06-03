# Système de Billetterie d'Événements en Temps Réel

## Un système de billetterie d'événements en temps réel avec une implémentation avancée du modèle Producteur-Consommateur utilisant la programmation concurrente en Java.

Ce projet comprend un backend développé avec Spring Boot (Java), une interface utilisateur frontend développée avec React (TypeScript) et utilise MySQL comme base de données. Le projet vise à :

1.  Résoudre le problème classique du producteur-consommateur.
2.  Mettre en œuvre le multi-threading et la concurrence.
3.  Utiliser les principes fondamentaux de la programmation orientée objet et de la conception logicielle.
4.  Fournir à la fois une interface en ligne de commande (CLI) et une application web, fonctionnant séparément.

## Technologies Utilisées

*   **Backend :** Spring Boot (Java)
*   **Frontend :** React (TypeScript) avec Vite
*   **Base de données :** MySQL (Workbench recommandé pour la gestion)

## Environnements de Développement Recommandés

*   **Backend :** IntelliJ IDEA (Community Edition ou Ultimate)
*   **Frontend :** Visual Studio Code

## Prérequis

Assurez-vous de disposer d'une connexion Internet et d'avoir installé les éléments suivants sur votre système :

*   **Java :** JDK 17 ou une version ultérieure.
*   **Node.js :** Version LTS (Long Term Support) recommandée. Inclut npm (Node Package Manager).
*   **MySQL :** Serveur MySQL et un outil de gestion comme MySQL Workbench.
*   **Maven :** Généralement inclus avec IntelliJ IDEA pour les projets Spring Boot, sinon installez-le séparément.
*   **Git :** Pour cloner le dépôt.
*   **IDE :** IntelliJ IDEA (version 2023.3.1 ou ultérieure recommandée) et Visual Studio Code.

## Instructions d'Installation

### 1. Cloner le Dépôt

Ouvrez un terminal ou une invite de commande, naviguez jusqu'au répertoire où vous souhaitez cloner le projet, et exécutez la commande suivante :

```bash
git clone https://github.com/MohamedBENIAICH/Java_Concurentiel.git
cd Java_Concurentiel
```

Le projet contient deux dossiers principaux : `backend` et `frontend`.

### 2. Configuration du Backend (Spring Boot)

1.  **Ouvrir le projet Backend :** Lancez IntelliJ IDEA et ouvrez le dossier `backend` situé à la racine du projet cloné (`Java_Concurentiel/backend`). IntelliJ devrait détecter automatiquement qu'il s'agit d'un projet Maven et télécharger les dépendances nécessaires.
2.  **Configurer la Base de Données :**
    *   Ouvrez le fichier de configuration `application.properties` situé dans `backend/src/main/resources/`.
    *   **Première exécution :** Modifiez la propriété `spring.jpa.hibernate.ddl-auto` et définissez sa valeur sur `create`. Cela permettra à Hibernate de créer automatiquement le schéma de la base de données et les tables lors du premier lancement.
        ```properties
        spring.jpa.hibernate.ddl-auto=create
        ```
    *   **Configurer les accès à MySQL :** Mettez à jour les propriétés suivantes avec vos informations de connexion à la base de données MySQL locale. Assurez-vous d'avoir préalablement créé une base de données (schéma) vide dans MySQL (par exemple, nommée `event_ticketing_db`).
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/event_ticketing_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
        spring.datasource.username=VOTRE_UTILISATEUR_MYSQL
        spring.datasource.password=VOTRE_MOT_DE_PASSE_MYSQL
        ```
        Remplacez `event_ticketing_db`, `VOTRE_UTILISATEUR_MYSQL`, et `VOTRE_MOT_DE_PASSE_MYSQL` par vos propres valeurs.

### 3. Configuration du Frontend (React + Vite)

1.  **Ouvrir le projet Frontend :** Lancez Visual Studio Code et ouvrez le dossier `frontend` situé à la racine du projet cloné (`Java_Concurentiel/frontend`).
2.  **Installer les Dépendances :** Ouvrez un terminal intégré dans VS Code (Terminal > Nouveau Terminal) et exécutez la commande suivante pour installer toutes les dépendances du projet frontend :
    ```bash
    npm install
    ```

### 4. Configuration de la Base de Données (MySQL)

1.  **Lancer MySQL Workbench :** Démarrez MySQL Workbench et connectez-vous à votre instance de serveur MySQL locale.
2.  **Créer un Schéma :** Créez un nouveau schéma (base de données) si vous ne l'avez pas déjà fait. Cliquez sur l'icône 

`Create a new schema in the connected server` ou exécutez la requête SQL `CREATE SCHEMA event_ticketing_db;` (en remplaçant `event_ticketing_db` par le nom que vous avez choisi et configuré dans `application.properties`).

## Démarrage du Système

### 1. Démarrer le Backend (Spring Boot)

1.  **Premier Lancement (Création des tables) :**
    *   Assurez-vous que la propriété `spring.jpa.hibernate.ddl-auto` est bien sur `create` dans `application.properties`.
    *   Dans IntelliJ IDEA, trouvez la classe principale de l'application : `backend/src/main/java/com/thilina_jayasinghe/w2052199/RealTimeEventTicketingSystem/RealTimeEventTicketingSystemApplication.java`.
    *   Exécutez cette classe (clic droit > Run). Le serveur backend démarrera.
    *   Vérifiez dans MySQL Workbench que les tables nécessaires (configuration, vendors, customers, tickets, etc.) ont été créées dans votre schéma.
    *   Une fois les tables créées, **arrêtez l'application** backend dans IntelliJ.
2.  **Modifier la Configuration Hibernate :**
    *   Retournez dans le fichier `application.properties`.
    *   Changez la valeur de `spring.jpa.hibernate.ddl-auto` de `create` à `update`. Cela empêchera Hibernate de recréer les tables à chaque démarrage et préservera les données.
        ```properties
        spring.jpa.hibernate.ddl-auto=update
        ```
3.  **Redémarrer le Backend :**
    *   Relancez la classe `RealTimeEventTicketingSystemApplication.java` comme précédemment.
    *   Le backend devrait maintenant être opérationnel et accessible sur `http://localhost:9090` (ou le port configuré si différent).

### 2. Démarrer le Frontend (React + Vite)

1.  **Lancer le Serveur de Développement :**
    *   Dans le terminal de Visual Studio Code (ouvert dans le dossier `frontend`), exécutez la commande suivante :
        ```bash
        npm run dev
        ```
    *   Cette commande démarre le serveur de développement Vite.
2.  **Accéder à l'Application Web :**
    *   Ouvrez votre navigateur web et allez à l'adresse indiquée par Vite dans le terminal (généralement `http://localhost:5173`, mais vérifiez la sortie du terminal).

### 3. Utiliser l'Interface en Ligne de Commande (CLI) - Optionnel

Le projet inclut également une interface en ligne de commande.

1.  **Exécuter la CLI :**
    *   Dans IntelliJ IDEA, naviguez jusqu'à la classe `backend/src/main/java/com/thilina_jayasinghe/w2052199/RealTimeEventTicketingSystem/cli/EventTicketingSystem.java`.
    *   Exécutez cette classe (clic droit > Run).
    *   Suivez les instructions affichées dans la console pour interagir avec le système via la CLI.
2.  **Fichiers de sortie de la CLI :**
    *   Après avoir utilisé et quitté la CLI, vous pouvez consulter les fichiers `tickets.json` (historique des transactions) et `system_config_settings.json` (derniers paramètres de configuration utilisés) qui sont générés à la racine du module `backend`.

## Utilisation de l'Application Web

Une fois le backend et le frontend démarrés, vous pouvez utiliser l'application web :

1.  **Configuration Initiale :** Configurez les paramètres du système via l'interface.
2.  **Enregistrement :** Enregistrez les vendeurs (vendors) et les clients (customers).
3.  **Logs :** Accédez à la section des logs pour suivre les événements.
4.  **Démarrer la Simulation :** Lancez le processus de vente/achat de billets.
5.  **Suivi en Temps Réel :** Consultez les logs en temps réel ou la section "TicketPool" pour voir l'état des billets disponibles.
6.  **Catalogue des Billets :** Visualisez les billets achetés dans la section "Tickets".

## Structure du Projet

```
Java_Concurentiel/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/thilina_jayasinghe/w2052199/RealTimeEventTicketingSystem/  # Code source Java (contrôleurs, services, modèles, CLI, etc.)
│   │   │   └── resources/
│   │   │       └── application.properties  # Fichier de configuration Spring Boot
│   │   └── test/                     # Tests unitaires et d'intégration
│   ├── pom.xml                       # Fichier de configuration Maven
│   └── ...
├── frontend/
│   ├── public/                       # Fichiers statiques
│   ├── src/                          # Code source React (composants, services, etc.)
│   ├── index.html                    # Point d'entrée HTML
│   ├── package.json                  # Dépendances et scripts npm
│   ├── vite.config.ts                # Configuration de Vite
│   ├── tsconfig.json                 # Configuration TypeScript
│   └── ...
└── README.md                         # Ce fichier
```

## Points d'API Principaux (Backend)

Le backend expose une API REST sur `http://localhost:9090`.

| Endpoint                         | Méthode | Description                                      |
| :------------------------------- | :------ | :----------------------------------------------- |
| `/api/save/config`               | POST    | Sauvegarder les détails de configuration         |
| `/api/save/vendor`               | POST    | Enregistrer un nouveau vendeur                   |
| `/api/get/vendor`                | GET     | Obtenir la liste des vendeurs enregistrés        |
| `/api/delete/vendor/{vendorId}`  | DELETE  | Supprimer un vendeur spécifique                  |
| `/api/save/customer`             | POST    | Enregistrer un nouveau client                    |
| `/api/get/customer`              | GET     | Obtenir la liste des clients enregistrés         |
| `/api/delete/customer/{customerId}` | DELETE  | Supprimer un client spécifique                   |
| `/api/save/ticket`               | POST    | Enregistrer un billet (généralement interne)     |
| `/api/get/tickets`               | GET     | Obtenir la liste des billets enregistrés (achetés) |
| `/api/start`                     | POST    | Démarrer les threads (simulation producteur/consommateur) |
| `/api/stop`                      | POST    | Arrêter les threads en cours                     |
| `/api/reset`                     | POST    | Effacer les logs ou réinitialiser l'état       |

## Connexion WebSocket

*   **URL WebSocket :** `ws://localhost:9090/ws-native`
*   **Objectif :** Fournir des mises à jour en temps réel sur l'état du `TicketPool` et les messages de log à l'interface utilisateur frontend.

### Messages Supportés

| Type de Message | Direction | Description                               |
| :-------------- | :-------- | :---------------------------------------- |
| `status`        | Réception | Mises à jour sur l'état du `TicketPool`   |
| `logs`          | Réception | Mises à jour des messages de log          |
| *Tout message*  | Envoi     | Messages envoyés depuis le client vers le backend (si applicable) |

## Dépannage

*   **Le Backend ne démarre pas ?**
    *   Vérifiez attentivement les identifiants de la base de données (`url`, `username`, `password`) dans `application.properties`.
    *   Assurez-vous que votre serveur MySQL est démarré et accessible sur le port spécifié (généralement 3306).
    *   Vérifiez que la base de données/schéma spécifié dans l'URL JDBC existe bien.
    *   Consultez les logs de démarrage de Spring Boot dans la console IntelliJ pour des messages d'erreur détaillés.
*   **Problèmes avec le Frontend ?**
    *   Assurez-vous d'avoir exécuté `npm install` dans le dossier `frontend` pour installer toutes les dépendances.
    *   Vérifiez qu'aucune erreur ne s'affiche dans la console du terminal où `npm run dev` est exécuté.
    *   Ouvrez les outils de développement de votre navigateur (généralement F12) et vérifiez la console pour des erreurs JavaScript ou des problèmes de connexion réseau (par exemple, échec de la connexion à l'API backend).
    *   Assurez-vous que le backend est bien démarré et accessible depuis le frontend (généralement sur `http://localhost:9090`).

## Contributeurs

Ce projet est basé sur le travail initial de [thilinajayasinghe](https://github.com/thilinajayasinghe) et a été adapté et modifié par les contributeurs suivants :

*   [MohamedBENIAICH](https://github.com/MohamedBENIAICH)
*   [DiarraIbra](https://github.com/DiarraIbra)
*   [issamidbenahmed](https://github.com/issamidbenahmed)

N'hésitez pas à contribuer en ouvrant des issues ou des pull requests !

