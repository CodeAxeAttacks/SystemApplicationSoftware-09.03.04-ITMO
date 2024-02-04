package beans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@ToString
public class TimeKeeper implements Serializable {
    private String time;

    private String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
    }

    public TimeKeeper() {
        this.time = formatDate(new Date());
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void updateTime() {
        time = formatDate(new Date());
    }
}
