package nl.miwnn.ch19.binarybros.brobook.dto;

/*
 * @author Mart Stukje
 * Supports the form to add student information
 * */

public class UserInfoFormDTO extends BaseUserFormDTO {

    private String bio;
    private String residence;
    private String existingProfilePictureId;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getExistingProfilePictureId() {
        return existingProfilePictureId;
    }

    public void setExistingProfilePictureId(String existingProfilePictureId) {
        this.existingProfilePictureId = existingProfilePictureId;
    }
}
