package com.austin.financetracker.dto;

/**
 * Data Transfer Object (DTO) for transferring user-related information
 * between different layers of the application (e.g., service and controller).
 * <p>
 * This class ensures only non-sensitive user data (such as UID, display name,
 * and email) is exposed, while keeping internal or sensitive fields hidden.
 */
public class UserDTO {

    /**
     * The unique identifier for the user, typically mapped to the Firebase UID.
     */
    private String userUid;

    /**
     * The display name chosen by the user.
     */
    private String displayName;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * Default constructor for frameworks and libraries that require it.
     */
    public UserDTO() {
    }

    /**
     * Retrieves the unique identifier of the user.
     *
     * @return the user UID
     */
    public String getUserUid() {
        return userUid;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param userUid the user UID to set
     */
    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    /**
     * Retrieves the display name of the user.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name for the user.
     *
     * @param displayName the display name to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Retrieves the email address of the user.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address for the user.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
