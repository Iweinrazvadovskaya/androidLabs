package by.bstu.razvod.lab4.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class ContactEntity {
    @PrimaryKey(autoGenerate = true)
    public long contactID;

    @ColumnInfo(name = "contact_name")
    public String contactName;

    @ColumnInfo(name = "contact_email")
    public String contactEmail;

    @ColumnInfo(name = "contact_location")
    public String contactLocation;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    @ColumnInfo(name = "link_social_network")
    public String linkSocialNetwork;

    @ColumnInfo(name = "favorite_contact")
    public int favoriteContact;


    public ContactEntity(long contactID, String contactName, String contactEmail, String contactLocation, String phoneNumber, String linkSocialNetwork, int favoriteContact ){
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactLocation = contactLocation;
        this.phoneNumber = phoneNumber;
        this.linkSocialNetwork = linkSocialNetwork;
        this.favoriteContact = favoriteContact;
    }
}
