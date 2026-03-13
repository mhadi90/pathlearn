# PathLearn - Plateforme de Formation en Ligne

![CI/CD Pipeline](https://github.com/mhadi90/pathlearn/actions/workflows/ci-cd.yml/badge.svg)

Plateforme de formation développée en architecture microservices.

**Projet universitaire** - Master 2 TIIL, Université de Bretagne Occidentale

## Architecture

Architecture microservices avec API Gateway :

- **API Gateway** (Port 8080) - Point d'entrée unique
- **Auth Service** (Port 8082) - Authentification et inscriptions
- **Course Service** (Port 8083) - Gestion des formations
- **Frontend Mobile** - Application mobile

## Technologies

- **Backend** : Spring Boot 4.0.2, Java 21
- **Build** : Gradle 8.14+
- **Base de données** : H2 (en mémoire)
- **Gateway** : Spring Cloud Gateway
- **Sécurité** : JWT
- **Containerisation** : Docker, Docker Compose
- **CI/CD** : GitHub Actions
- **Documentation** : OpenAPI 3.0

## Démarrage Rapide

### Prérequis

- Docker Desktop
- Git

### Installation
```bash
# Cloner le projet
git clone https://github.com/mhadi90/pathlearn.git
cd pathlearn

# Lancer tous les services
docker compose up -d

# Vérifier l'état
docker compose ps
```

### Accès aux Services

| Service | URL | Description |
|---------|-----|-------------|
| Production | https://gateway-production-c9e4.up.railway.app | API déployée |
| Gateway (local) | http://localhost:8080 | Point d'entrée |
| Auth Service | http://localhost:8082 | Authentification |
| Course Service | http://localhost:8083 | Catalogue |
| H2 Console (Auth) | http://localhost:8082/h2-console | Base de données |
| H2 Console (Course) | http://localhost:8083/h2-console | Base de données |

### Test de l'API
```bash
# Créer un compte
curl -X POST https://gateway-production-c9e4.up.railway.app/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "zineb.es-saih@pathlearn.com",
    "password": "Zineb2002",
    "nom": "Es-saih",
    "prenom": "Zineb",
    "role": "APPRENTI"
  }'

# Se connecter
curl -X POST https://gateway-production-c9e4.up.railway.app/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "zineb@pathlearn.com",
    "password": "zineb123"
  }'

# Liste des cours (copier le token de la réponse précédente)
curl https://gateway-production-c9e4.up.railway.app/courses \
  -H "Authorization: Bearer {TOKEN}"
```

## Structure du Projet
```
pathlearn/
├── gateway/                  # API Gateway
├── auth-service/             # Service d'authentification
├── enrollment_service/       # Service des inscriptions
├── course/                   # Service de gestion des cours
├── docs/                     # Documentation
│   ├── api/                  # Spécifications OpenAPI
│   ├── error-handling/       # Gestion d'erreurs
│   ├── ARCHITECTURE.md
│   ├── QUICKSTART.md
│   └── CI-CD-GUIDE.md
├── .github/workflows/
│   └── ci-cd.yml             # Pipeline CI/CD
├── docker-compose.yml
└── README.md
```

## Documentation

- [Architecture Détaillée](docs/ARCHITECTURE.md)
- [Guide de Démarrage](docs/QUICKSTART.md)
- [Documentation API](docs/api/README.md)
- [Pipeline CI/CD](docs/CI-CD-GUIDE.md)
- [Gestion d'Erreurs](docs/error-handling/GUIDE.md)

## Commandes Utiles
```bash
# Démarrer
docker compose up -d

# Arrêter
docker compose down

# Voir les logs
docker compose logs -f gateway

# Rebuild complet
docker compose down -v
docker compose up -d --build

# Redémarrer un service
docker compose restart gateway
```

## Rôles Utilisateurs

| Rôle | Permissions |
|------|-------------|
| VISITEUR | Consulter les formations |
| APPRENTI | S'inscrire et suivre des cours |
| FORMATEUR | Créer et gérer des formations |
| ADMIN | Accès complet |

## Pipeline CI/CD

Le pipeline GitHub Actions s'exécute automatiquement à chaque push sur `main` :

- Build Gateway Service
- Build Auth Service
- Build Course Service
- Analyse de qualité du code
- Upload des artifacts

Voir les résultats : https://github.com/mhadi90/pathlearn/actions

## Équipe

- **Mohamed** - Backend (Gateway, Auth, Course Services)
- **Katia KADDOUR CHERIF** -Frontend Mobile
- **Zineb Es-saih** - DevOps & Infrastructure
- **Imad El Barkouki** -  Course Service

**Formation** : Master 2 TIIL  
**Année** : 2025-2026

## Liens Utiles

- **Repository** : https://github.com/mhadi90/pathlearn
- **API Production** : https://gateway-production-c9e4.up.railway.app
- **CI/CD** : https://github.com/mhadi90/pathlearn/actions
- **Documentation API** : Voir `docs/api/pathlearn-api.yaml` (OpenAPI 3.0)

## Licence

Projet universitaire - UBO M2 TIIL