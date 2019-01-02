package exceptions;

public class PoorCoverageException extends Exception {
    public PoorCoverageException(String message) {
        super("Requested tests quantity is less than required for good coverage: " + message);
    }
}
