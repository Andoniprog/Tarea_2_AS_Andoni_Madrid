package cl.ucn.disc.arqsist.library.service;

/**
 * Indicates that a requested library entity could not be found.
 */
public final class NotFoundException extends RuntimeException {

    /**
     * Creates an exception with a description of the missing entity.
     *
     * @param message details identifying the missing entity.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
