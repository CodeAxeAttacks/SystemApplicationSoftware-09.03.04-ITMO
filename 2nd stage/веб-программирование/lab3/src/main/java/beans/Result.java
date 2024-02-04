package beans;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Result implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "x", nullable = false)
    private BigDecimal x;
    @Column(name = "y", nullable = false)
    private BigDecimal y;
    @Column(name = "r", nullable = false)
    private BigDecimal r;
    @Column(name = "hit", nullable = false)
    private Boolean hit;
    @Column(name = "request_time", nullable = false)
    private String requestTime;

    public Result(Result sourceResult) {
        this.id = sourceResult.id;
        this.x = sourceResult.getX();
        this.y = sourceResult.getY();
        this.r = sourceResult.getR();
        this.hit = checkHit();
        this.requestTime = sourceResult.requestTime;
    }

    private boolean checkHit() {
        BigDecimal x = new BigDecimal(String.valueOf(this.x).replace(',', '.'));
        BigDecimal y = new BigDecimal(String.valueOf(this.y).replace(',', '.'));
        BigDecimal r = new BigDecimal(String.valueOf(this.r).replace(',', '.'));

        BigDecimal halfR = r.divide(BigDecimal.valueOf(2));

        boolean circle = x.compareTo(BigDecimal.ZERO) >= 0
                && y.compareTo(BigDecimal.ZERO) <= 0
                && x.pow(2).add(y.pow(2)).compareTo(r.pow(2)) <= 0;

        boolean triangle = x.compareTo(BigDecimal.ZERO) <= 0
                && y.compareTo(BigDecimal.ZERO) >= 0
                && y.compareTo(BigDecimal.valueOf(0.5).multiply(r).add(BigDecimal.valueOf(0.5).multiply(x))) <= 0;

        boolean rectangle = x.compareTo(BigDecimal.ZERO) <= 0
                && y.compareTo(BigDecimal.ZERO) <= 0
                && x.compareTo(BigDecimal.ZERO.subtract(r)) >= 0
                && y.compareTo(BigDecimal.ZERO.subtract(halfR)) >= 0;

        return circle || triangle || rectangle;
    }

    public String getStringSuccess() {
        return hit ? "Hit" : "Miss";
    }

    public String getClassSuccess() {
        return hit ? "hit" : "miss";
    }
}