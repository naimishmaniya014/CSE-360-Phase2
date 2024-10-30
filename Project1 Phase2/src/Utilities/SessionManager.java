package Utilities;

import models.User;
import models.Role;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private Role currentRole;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Role getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(Role currentRole) {
        this.currentRole = currentRole;
    }

    public void clearSession() {
        this.currentUser = null;
        this.currentRole = null;
    }
}
