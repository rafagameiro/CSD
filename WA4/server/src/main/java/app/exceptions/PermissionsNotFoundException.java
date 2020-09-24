package app.exceptions;

public class PermissionsNotFoundException extends RepositoryException {
    public PermissionsNotFoundException(String username) {
        super("No permitions found for " + username + "!");
    }
}
