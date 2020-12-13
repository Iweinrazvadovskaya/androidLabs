package by.bstu.razvod.lab4.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts", indices = {
        @Index(value = {"contact_name"}, unique = false),
        @Index(value = {"contact_email"}, unique = false),
        @Index(value = {"phone_number"}, unique = false),
})
public class ContactEntity {
    @PrimaryKey(autoGenerate = true)
    private long contactID;

    @ColumnInfo(name = "contact_name")
    private String contactName;

    @ColumnInfo(name = "contact_email")
    private String contactEmail;

    @ColumnInfo(name = "contact_location")
    private String contactLocation;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @ColumnInfo(name = "link_social_network")
    private String linkSocialNetwork;

    @ColumnInfo(name = "favorite_contact")
    private int favoriteContact;

    public long getContactID() {
        return contactID;
    }

    public void setContactID(long contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactLocation() {
        return contactLocation;
    }

    public void setContactLocation(String contactLocation) {
        this.contactLocation = contactLocation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLinkSocialNetwork() {
        return linkSocialNetwork;
    }

    public void setLinkSocialNetwork(String linkSocialNetwork) {
        this.linkSocialNetwork = linkSocialNetwork;
    }

    public int getFavoriteContact() {
        return favoriteContact;
    }

    public void setFavoriteContact(int favoriteContact) {
        this.favoriteContact = favoriteContact;
    }

    public ContactEntity(long contactID, String contactName, String contactEmail, String contactLocation, String phoneNumber, String linkSocialNetwork, int favoriteContact) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactLocation = contactLocation;
        this.phoneNumber = phoneNumber;
        this.linkSocialNetwork = linkSocialNetwork;
        this.favoriteContact = favoriteContact;
    }
}
