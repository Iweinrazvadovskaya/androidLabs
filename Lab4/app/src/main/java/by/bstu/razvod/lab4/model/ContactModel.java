package by.bstu.razvod.lab4.model;

import java.io.Serializable;
import java.util.Objects;

public class ContactModel implements Serializable {
    private int contactID;
    private String contactName;
    private String contactEmail;
    private String contactLocation;
    private String phoneNumber;
    private String linkSocialNetwork;

    public int getId()
    {
        return contactID;
    }

    public String getContactName()
    {
        return contactName;
    }
    public String getEmail()
    {
        return contactEmail;
    }
    public String getLocation()
    {
        return contactLocation;
    }
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    public String getLinkSocialNetwork()
    {
        return linkSocialNetwork;
    }

    public ContactModel(int contactID, String contactName, String contactEmail, String contactLocation, String phoneNumber, String linkSocialNetwork){
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactLocation = contactLocation;
        this.phoneNumber = phoneNumber;
        this.linkSocialNetwork = linkSocialNetwork;
    }

    public ContactModel(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactModel that = (ContactModel) o;
        return contactID == that.contactID &&
                Objects.equals(contactName, that.contactName) &&
                Objects.equals(contactEmail, that.contactEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactID, contactName, contactEmail, contactLocation, phoneNumber, linkSocialNetwork);
    }
}
