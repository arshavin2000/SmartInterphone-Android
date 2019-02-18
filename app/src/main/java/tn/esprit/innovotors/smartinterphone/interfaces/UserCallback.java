package tn.esprit.innovotors.smartinterphone.interfaces;

import tn.esprit.innovotors.smartinterphone.models.User;

public interface UserCallback {

    void setUser(User user);

    void setError(String msg);
}
