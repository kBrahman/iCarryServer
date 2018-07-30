package zig.i.carry.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrderAd.class, name = "orderAd"),
        @JsonSubTypes.Type(value = OrderAd.class, name = "offerAd")})

@MappedSuperclass
public abstract class Ad {

    Ad() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

//    @OneToMany
//    private List<Contact> contacts;

    private String countryFrom;
    private String countryTo;
    private String cityFrom;
    private String cityTo;
    private String description;
    private String userLogin;
    private String price;
    private String currency;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public void setId(Long id) {
        this.id = id;
    }

//    public void setContacts(List<Contact> contacts) {
//        this.contacts = contacts;
//    }

    public void setCountryFrom(String countryFrom) {
        this.countryFrom = countryFrom;
    }

    public void setCountryTo(String countryTo) {
        this.countryTo = countryTo;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Ad{" +
                "id=" + id +
                ", countryFrom='" + countryFrom + '\'' +
                ", countryTo='" + countryTo + '\'' +
                ", cityFrom='" + cityFrom + '\'' +
                ", cityTo='" + cityTo + '\'' +
                ", description='" + description + '\'' +
                ", userLogin='" + userLogin + '\'' +
                ", price='" + price + '\'' +
                ", currency='" + currency + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
