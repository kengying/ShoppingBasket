package u.nus.edu.marketscanner;

/**
 * Created by PANDA on 5/6/2017.
 */

public class Item {

    public String item_Name;
    public Double item_Price;
    public Long item_Id;
    public String item_Status;
    public String item_Image;

    public Item(){}

    public Item(Long id, String name, Double item_Price, String image, String status){
        this.item_Id = id;
        this.item_Name = name;
        this.item_Price = item_Price;
        this.item_Image = image;
        this.item_Status = status;
    }

    public String getItem_Name() {
        return item_Name;
    }

    public void setItem_Name(String item_Name) {
        this.item_Name = item_Name;
    }

    public Double getItem_Price() {
        return item_Price;
    }

    public String getItem_Price_String() {
        return String.valueOf(item_Price);
    }

    public void setItem_Price(Double item_Price) {
        this.item_Price = item_Price;
    }

    public Long getItem_Id() {
        return item_Id;
    }

    public void setItem_Id(Long item_Id) {
        this.item_Id = item_Id;
    }

    public String getItem_Status() {
        return item_Status;
    }

    public void setItem_Status(String item_Status) {
        this.item_Status = item_Status;
    }

    public String getItem_Image() {
        return item_Image;
    }

    public int getItem_Image_int() {
        return Integer.parseInt(item_Image);
    }

    public void setItem_Image(String item_Image) {
        this.item_Image = item_Image;
    }

    @Override
    public String toString() {
        return "Item{" +
                "item_Name='" + item_Name + '\'' +
                ", item_Price=" + item_Price +
                ", id=" + item_Id +
                ", item_Status=" + item_Status +
                ", image='" + item_Image + '\'' +
                '}';
    }
}
