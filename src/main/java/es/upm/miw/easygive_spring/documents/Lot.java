package es.upm.miw.easygive_spring.documents;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Lot {

    @Id
    private String id;

    private Binary image;

    private String title;

    private String description;

    private String schedule;

    private Boolean wish;

    private Boolean food;

    private Boolean delivered;

    @DBRef
    private User user;

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public Binary getImage() {
        return image;
    }

    public void setImage(Binary image) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Lot{" +
                "id='" + id + '\'' +
                ", image=" + image +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", schedule='" + schedule + '\'' +
                ", wish=" + wish +
                ", food=" + food +
                ", delivered=" + delivered +
                ", user=" + user +
                '}';
    }

    public static class Builder {
        private Lot lot;

        private Builder() {
            this.lot = new Lot();
        }

        public Builder image(Binary image) {
            this.lot.image = image;
            return this;
        }

        public Builder title(String title) {
            this.lot.title = title;
            return this;
        }

        public Builder description(String description) {
            this.lot.description = description;
            return this;
        }

        public Builder schedule(String schedule) {
            this.lot.schedule = schedule;
            return this;
        }

        public Builder wish(Boolean wish) {
            this.lot.wish = wish;
            return this;
        }

        public Builder food(Boolean food) {
            this.lot.food = food;
            return this;
        }

        public Builder delivered(Boolean delivered) {
            this.lot.delivered = delivered;
            return this;
        }

        public Builder user(User user) {
            this.lot.user = user;
            return this;
        }

        public Lot build() {
            return this.lot;
        }
    }
}
