package singh.durgesh.com.applocker.model;

/**
 * Created by RSharma on 6/21/2017.
 */

public class Contact
{
    public String CName;
    public String CPhone;
    public Contact()
    {

    }


    public Contact(final String CPhone)
    {
        this.CPhone=CPhone;
    }
    public boolean isBlocked;

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public String getCPhone() {
        return CPhone;
    }

    public void setCPhone(String CPhone) {
        this.CPhone = CPhone;
    }

}
