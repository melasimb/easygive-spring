package es.upm.miw.easygive_spring.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LotSearchDto {

    private Boolean food;

    private Boolean delivered;

    public LotSearchDto() {
        //empty for framework
    }

    public LotSearchDto(Boolean food, Boolean delivered ) {
        this.food = food;
        this.delivered = delivered;
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

    @Override
    public String toString() {
        return "LotSearchDto{" +
                "food=" + food +
                ", delivered=" + delivered +
                '}';
    }
}
