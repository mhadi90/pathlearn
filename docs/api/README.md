# Documentation API PathLearn

## Fichier

`pathlearn-api.yaml` - Toutes les spécifications de l'API en OpenAPI 3.0

## Comment Visualiser

**Méthode rapide :**
1. Va sur https://editor.swagger.io/
2. Copie tout le contenu de `pathlearn-api.yaml`
3. Colle dans l'éditeur
4. Voilà ! Tu peux tester les endpoints directement

## Authentification

Pour utiliser l'API, il faut d'abord récupérer un token :
```bash
# Se connecter
curl -X POST https://gateway-production-c9e4.up.railway.app/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "zineb.es-saih@example.com", "password": "Zineb2002"}'
```

Ensuite utilise le token dans les requêtes :
```bash
curl https://gateway-production-c9e4.up.railway.app/auth/me \
  -H "Authorization: Bearer TON_TOKEN_ICI"
```

## Endpoints Importants

**Authentification**
- POST `/auth/register` - Créer un compte
- POST `/auth/login` - Se connecter
- GET `/auth/me` - Mon profil

**Formations**
- GET `/courses` - Liste des cours
- GET `/courses/{id}` - Détails d'un cours
- POST `/courses` - Créer un cours (FORMATEUR seulement)

**Inscriptions**
- POST `/inscriptions` - S'inscrire à un cours
- PUT `/inscriptions/{id}/progression` - Mettre à jour ma progression

## Rôles

- VISITEUR - Juste consulter
- APPRENTI - Suivre des cours
- FORMATEUR - Créer des cours
- ADMIN - Tout faire

## URL de Base

- Production : https://gateway-production-c9e4.up.railway.app
- Local : http://localhost:8080

## Notes

Le fichier YAML contient tous les détails (schémas, exemples, codes d'erreur, etc.).