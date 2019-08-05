package api.gliger.glg.instaoauth.model;

import android.support.annotation.Nullable;

public class Profile {
    private String id;
    private String username;
    private String profilePicture;
    private String fullName;
    private String bio;
    private String website;
    private boolean isBusinessProfile;
    private Count counts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isBusinessProfile() {
        return isBusinessProfile;
    }

    public void setBusinessProfile(boolean businessProfile) {
        isBusinessProfile = businessProfile;
    }

    public Count getCounts() {
        return counts;
    }

    public void setCounts(Count counts) {
        this.counts = counts;
    }

    @Nullable
    @Override
    public String toString() {
        return "Profile Data : "
                + "ID-" + id
                + "\nUsername-" + username
                + "\nProfilePicture-" + profilePicture
                + "\nFullName-" + fullName
                + "\nBio-" + bio
                + "\nWebsite-" + website
                + "\nIsBusiness-" + isBusinessProfile
                + "\nCounts-"
                +"\n\tMedia-"  + counts.getMedia()
                +"\n\tFollows-"+ counts.getFollows()
                +"\n\tFollowed By-" + counts.getFollowedBy();
    }
}
