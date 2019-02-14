package project.persistence.entities;

import javax.persistence.*;

/**
 * Beeeeeeeeeeeeeeers are what the app is about.
 */

@Entity
@Table(name="beers")
public class Beer {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "beer_id")
    private Long beerId;

    @Column(name = "beer_name")
    private String name;

    @Column(name = "beer_link")
    private String linkToVinbudin;

    @Column(name = "beer_alcohol")
    private float alcohol;

    @Column(name = "beer_taste")
    private String taste;

    @Column(name = "beer_volume")
    private int volume;

//    @Column(name = "beer_stars")
//    private float stars;

    @Column(name = "beer_price")
    private int price;


    public Long getBeerId() {
        return beerId;
    }

    public void setBeerId(Long beerId) {
        this.beerId = beerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkToVinbudin() {
        return linkToVinbudin;
    }

    public void setLinkToVinbudin(String linkToVinbudin) {
        this.linkToVinbudin = linkToVinbudin;
    }

    public float getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(float alcohol) {
        this.alcohol = alcohol;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

//    public float getStars() {
//        return stars;
//    }
//
//    public void setStars(float stars) {
//        this.stars = stars;
//    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }





    public Beer() {
    }



}
