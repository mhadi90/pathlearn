# Guide de Démarrage Rapide - PathLearn

## Installation

### 1. Prérequis
```bash
docker --version    # Docker 20.x+
git --version       # Git 2.x+
```

### 2. Cloner le Projet
```bash
git clone https://github.com/mhadi90/pathlearn.git
cd pathlearn
```

### 3. Lancer les Services
```bash
docker compose up -d
```

Attendre 30-60 secondes pour le démarrage.

### 4. Vérifier
```bash
docker compose ps
```

Résultat attendu :
```
NAME                STATUS
pathlearn-gateway   Up
pathlearn-auth      Up
pathlearn-course    Up
```

## Tester l'Installation

### Test 1 : Gateway
```bash
curl http://localhost:8080/actuator/health
```

### Test 2 : Créer un Compte
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "zineb",
    "email": "zineb@pathlearn.com",
    "password": "test123"
  }'
```

### Test 3 : Se Connecter
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "zineb",
    "password": "test123"
  }'
```

Copier le `token` retourné.

### Test 4 : Accéder aux Cours
```bash
curl http://localhost:8080/courses \
  -H "Authorization: Bearer {TOKEN}"
```

## Accès aux Consoles H2

### Auth Service
- URL : http://localhost:8082/h2-console
- JDBC URL : `jdbc:h2:mem:auth_db`
- Username : `sa`
- Password : (vide)

### Course Service
- URL : http://localhost:8083/h2-console
- JDBC URL : `jdbc:h2:mem:course_db`
- Username : `sa`
- Password : (vide)

## Commandes Utiles
```bash
# Démarrer
docker compose up -d

# Arrêter
docker compose down

# Voir les logs
docker compose logs -f

# Rebuild
docker compose up -d --build

# Redémarrer un service
docker compose restart gateway
```

## Résolution de Problèmes

### Service ne démarre pas
```bash
docker compose logs auth-service
```

### Port déjà utilisé

Vérifier les ports 8080, 8082, 8083 :
```bash
netstat -an | findstr "8080 8082 8083"
```

### Reset complet
```bash
docker compose down -v
docker compose up -d --build
```

## Développement

### Modifier le Code
```bash
# Rebuild le service modifié
docker compose up -d --build gateway
```

### Variables d'Environnement

Modifier dans `docker-compose.yml` :
```yaml
environment:
  SERVER_PORT: 8080
  JWT_SECRET: votre-secret
```

## Prochaines Étapes

- Voir ARCHITECTURE.md pour l'architecture
- Voir CI-CD-GUIDE.md pour le pipeline
- Voir error-handling/GUIDE.md pour les erreurs