package u.nus.edu.marketscanner;

/**
 * Created by PANDA on 5/6/2017.
 */

public class Item {

    public String name;
    public Double price;
    public Long id;
    public Boolean status;
    public String image;

    public Item(){}

    public Item(Long id, String name, Double price, String image, Boolean status){
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.status = status;
    }

}
