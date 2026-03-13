package com.pathlearn.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceptions personnalisées pour PathLearn
 * Chaque exception correspond à un code HTTP spécifique
 */

/**
 * Exception levée quand une ressource n'est pas trouvée (404)
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s non trouvé(e) avec l'id : %d", resourceName, id));
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s non trouvé(e) avec %s : %s", resourceName, fieldName, fieldValue));
    }
}

/**
 * Exception levée pour une requête invalide (400)
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }
}

/**
 * Exception levée pour un accès non autorisé (401)
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException() {
        super("Authentification requise");
    }
}

/**
 * Exception levée pour un accès interdit (403)
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
class ForbiddenException extends RuntimeException {
    
    public ForbiddenException(String message) {
        super(message);
    }
    
    public ForbiddenException() {
        super("Vous n'avez pas l'autorisation d'accéder à cette ressource");
    }
}

/**
 * Exception levée quand une ressource existe déjà (409)
 */
@ResponseStatus(HttpStatus.CONFLICT)
class ResourceAlreadyExistsException extends RuntimeException {
    
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
    
    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s existe déjà avec %s : %s", resourceName, fieldName, fieldValue));
    }
}

/**
 * Exception levée pour une erreur métier (422)
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
}