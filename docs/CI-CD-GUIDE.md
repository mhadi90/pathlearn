# Pipeline CI/CD - PathLearn

## 🎯 Vue d'ensemble

Pipeline automatisé avec GitHub Actions pour build, tests et déploiement.

## 🔄 Déclencheurs

Le pipeline s'exécute automatiquement sur :
- **Push** sur les branches `main` et `develop`
- **Pull Requests** vers `main`

## 📋 Jobs du Pipeline

### 1. Build Enrollment Service ✅
- Compile le code avec Gradle 8.14
- Exécute les tests unitaires
- Upload le JAR comme artifact

### 2. Build Course Service ⏸️
- Actuellement désactivé (`if: false`)
- À activer quand le code de Katia sera intégré

### 3. Code Quality
- Analyse statique du code
- Checkstyle (optionnel)

### 4. Docker Build 🐳
- Build des images Docker
- Push sur Docker Hub (main seulement)
- Tags : `latest` et `{commit-sha}`

### 5. Integration Tests
- Lance PostgreSQL en service
- Exécute les tests d'intégration
- Vérifie la connexion DB

### 6. Swagger Validation
- Valide les specs OpenAPI
- Vérifie la syntaxe YAML

## 🔑 Secrets Requis

Dans **Settings → Secrets and variables → Actions** :

| Secret | Description | Exemple |
|--------|-------------|---------|
| `DOCKER_USERNAME` | Username Docker Hub | `zineb` |
| `DOCKER_PASSWORD` | Password Docker Hub | `***` |

## 🎨 Badge de Statut

Ajoute ce badge dans ton README.md :
```markdown
![CI/CD](https://github.com/USERNAME/pathlearn/actions/workflows/ci-cd.yml/badge.svg)
```

## 📊 Résultats

- ✅ **Success** : Tous les jobs passent
- ❌ **Failure** : Un ou plusieurs jobs échouent
- ⏸️ **Skipped** : Job désactivé

## 🔧 Configuration Locale

Pour tester localement avant de push :
```bash
# Build
cd enrollment-service
./gradlew clean build

# Tests
./gradlew test

# Docker
docker build -t pathlearn-enrollment .
```

## 🐛 Dépannage

**Erreur "gradlew: Permission denied"**
```bash
git update-index --chmod=+x enrollment-service/gradlew
git commit -m "Fix gradlew permissions"
```

**Erreur Docker Hub login**
- Vérifier que les secrets sont bien configurés
- Utiliser un Access Token au lieu du password

**Tests échouent**
- Vérifier la connexion PostgreSQL
- Vérifier les variables d'environnement