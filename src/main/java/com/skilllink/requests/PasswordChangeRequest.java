package com.skilllink.requests;

/**
 * Request object for handling password change operations.
 * Contains the user's email, old password, and new password.
 *
 * Author: Hrittija Bhattacharjee
 */

public class PasswordChangeRequest {
    private String email;
    private String oldPassword;
    private String newPassword;

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
