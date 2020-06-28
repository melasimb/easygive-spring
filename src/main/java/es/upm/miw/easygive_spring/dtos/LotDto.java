package es.upm.miw.easygive_spring.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import es.upm.miw.easygive_spring.documents.Lot;

import javax.validation.constraints.NotNull;
import java.util.Base64;

@JsonInclude(Include.NON_NULL)
public class LotDto {

    private String id;

    @NotNull
    private String image;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String schedule;

    @NotNull
    private Boolean wish;

    @NotNull
    private Boolean food;

    @NotNull
    private Boolean delivered;

    @NotNull
    private String username;

    private String location;

    public LotDto() {
        // Empty for framework
    }

    public LotDto(String image, String title, String description, String schedule, Boolean wish, Boolean food, Boolean delivered, String username, String location) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.schedule = schedule;
        this.wish = wish;
        this.food = food;
        this.delivered = delivered;
        this.username = username;
        this.location = location;
    }

    public LotDto(Lot lot) {
        this.id = lot.getId();
        this.image = Base64.getEncoder().encodeToString(lot.getImage().getData());
        this.title = lot.getTitle();
        this.description = lot.getDescription();
        this.schedule = lot.getSchedule();
        this.wish = lot.getWish();
        this.food = lot.getFood();
        this.delivered = lot.getDelivered();
        this.username = lot.getUser().getUsername();
        this.location = lot.getUser().getLocation();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Boolean getWish() {
        return wish;
    }

    public void setWish(Boolean wish) {
        this.wish = wish;
    }

    public Boolean getFood() {
        return food;
    }

    public void setFood(Boolean food) {
        this.food = food;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "LotDto{" +
                "id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", schedule='" + schedule + '\'' +
                ", wish=" + wish +
                ", food=" + food +
                ", delivered=" + delivered +
                ", username='" + username + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
