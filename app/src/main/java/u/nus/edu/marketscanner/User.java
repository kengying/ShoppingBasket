package u.nus.edu.marketscanner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PANDA on 14/6/2017.
 */

public class User implements Parcelable {

    private String username;
    private String password;
    private String lastName;
    private String firstName;
    private Long no_of_carts;

    public User(){
        this.username = "";
        this.password = "";
        this.lastName = "";
        this.firstName = "";
        this.no_of_carts = null;
    }

  //  public User() {
        //required empty constructor for Firebase
  //  }

    public User(String username, String password, String lastName, String firstName, Long no_of_carts){
        this.username = username;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.no_of_carts = no_of_carts;
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.no_of_carts = no_of_carts;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getNo_of_carts() {
        return no_of_carts;
    }

    public void setNo_of_carts(Long no_of_carts) {
        this.no_of_carts = no_of_carts;
    }

    @Override
    public String toString() {
        return "User{" +
                " username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", no_of_carts='" + no_of_carts + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(lastName);
        dest.writeString(firstName);
        dest.writeLong(no_of_carts);
    }

    // Parcelling part
    public User(Parcel in){
        this.username = in.readString();
        this.password = in.readString();
        this.lastName = in.readString();
        this.firstName = in.readString();
        this.no_of_carts = in.readLong();
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel pc) {
            return new User(pc);
        }
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
