package CodeAxeAttacks.weblab4.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "point")
public class Entry {
    @Id
    @GeneratedValue
    @JsonIgnore
    private long id;

    private double x;

    private double y;

    private double r;

    private boolean result;
    private String username;

    public Entry(double x, double y, double r, String username) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.username = username;
        check();
    }

    private boolean checkTriangle() {
        return (r >= 0 && x >= 0 && y >= 0 && y <= (r/2 - x)) || (r <= 0 && x <= 0 && y <= 0 && y >= (r/2 - x));
    }

    private boolean checkRectangle() {
        return (r >= 0 && x <= r/2 && y >= -r && x >= 0 && y <= 0) || (r <= 0 && x >= r/2 && y <= -r && x <= 0 && y >= 0);
    }

    private boolean checkCircle() {
        return (r >= 0 && x <= 0 && y <= 0 && x * x + y * y <= r * r / 4) || (r <= 0 && x >= 0 && y >= 0 && x * x + y * y <= r * r / 4);
    }


    public void check() {
        result = checkTriangle() || checkRectangle() || checkCircle();
    }
}
