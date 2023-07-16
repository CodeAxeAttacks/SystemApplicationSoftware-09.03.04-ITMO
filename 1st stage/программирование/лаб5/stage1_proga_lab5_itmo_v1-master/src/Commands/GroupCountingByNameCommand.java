package Commands;

import java.util.Map;
import java.util.stream.Collectors;
import CommandsSupport.*;
import Support.*;
import JsonSupport.*;

public class GroupCountingByNameCommand implements Command {
    private final LabWorkCollection collection;

    public GroupCountingByNameCommand(LabWorkCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() {
        Map<String, Long> groups = collection.getAll()
                .stream()
                .collect(Collectors.groupingBy(LabWork::getName, Collectors.counting()));

        groups.forEach((name, count) -> System.out.println(name + ": " + count));
    }

    @Override
    public String getDescription() {
        return "group_counting_by_name";
    }
}
