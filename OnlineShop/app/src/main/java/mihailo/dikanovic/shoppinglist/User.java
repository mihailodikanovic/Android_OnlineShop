package mihailo.dikanovic.shoppinglist;

public class User
{
    private String ID;
    private String username;
    private String mail;
    private String password;
    private boolean admin;

    public User(String ID, String username, String mail, String password, boolean admin) {
        this.ID = ID;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
