package CodeAxeAttacks.weblab4.DTO;

import lombok.Data;
import lombok.NonNull;

@Data
public class EntryDTO {
    @NonNull
    private double x;

    @NonNull
    private double y;

    @NonNull
    private double r;

    @NonNull
    private String username;
}
