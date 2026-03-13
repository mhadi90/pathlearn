# Guide - Gestion d'Erreurs Globale PathLearn

## Installation

### 1. Copier les fichiers

Créer le package `exception` dans votre service :
```
src/main/java/com/pathlearn/[service-name]/exception/
├── ErrorResponse.java
├── CustomExceptions.java
└── GlobalExceptionHandler.java
```

### 2. Adapter le package

Modifier la première ligne de chaque fichier :
```java
// Pour enrollment-service
package com.pathlearn.enrollment.exception;

// Pour course-service
package com.pathlearn.course.exception;
```

### 3. Configuration

Dans `application.properties` :
```properties
spring.mvc.throw-exception-if-no-handler-found=true
spring.web.resources.add-mappings=false
```

## Utilisation

### Ressource non trouvée
```java
@GetMapping("/{id}")
public Formation getFormationById(@PathVariable Long id) {
    return formationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Formation", id));
}
```

Réponse :
```json
{
  "timestamp": "2026-03-12 16:30:00",
  "status": 404,
  "error": "Ressource Non Trouvée",
  "message": "Formation non trouvé(e) avec l'id : 5",
  "path": "/api/formations/5"
}
```

### Validation métier
```java
@PostMapping("/inscriptions")
public Inscription inscrire(@RequestBody InscriptionDTO dto) {
    if (inscriptionRepository.exists(userId, formationId)) {
        throw new ResourceAlreadyExistsException("Inscription", "formation", formationId);
    }
    return inscriptionService.inscrire(dto);
}
```

### Règle métier
```java
if (progression.getPourcentage() < 80) {
    throw new BusinessException("Minimum 80% requis pour compléter le cours");
}
```

## Exceptions disponibles

| Exception | Code HTTP | Usage |
|-----------|-----------|-------|
| ResourceNotFoundException | 404 | Ressource introuvable |
| BadRequestException | 400 | Requête invalide |
| UnauthorizedException | 401 | Authentification requise |
| ForbiddenException | 403 | Accès interdit |
| ResourceAlreadyExistsException | 409 | Doublon |
| BusinessException | 422 | Règle métier violée |


### À faire
```java
// Messages clairs
throw new ResourceNotFoundException("Formation", formationId);

// Validation explicite
if (montant < 0) {
    throw new BadRequestException("Le montant ne peut pas être négatif");
}
```

### À éviter
```java
// Messages vagues
throw new Exception("Erreur");

// Détails techniques exposés
throw new Exception(ex.getStackTrace().toString());

// Ignorer les erreurs
try { ... } catch (Exception e) { }
```

## Checklist

- [ ] Copier les 3 fichiers Java
- [ ] Adapter les packages
- [ ] Activer la configuration
- [ ] Remplacer les exceptions génériques
- [ ] Tester les endpoints