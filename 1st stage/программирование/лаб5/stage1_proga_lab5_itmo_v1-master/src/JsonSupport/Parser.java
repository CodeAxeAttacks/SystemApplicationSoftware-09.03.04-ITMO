package JsonSupport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import Support.LabWork;
import Support.LabWorkCollection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Parser {
    private String filename;

    public Parser(String filename) {
        this.filename = filename;
    }

    public LabWorkCollection parse() throws IOException, ParseException {
        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        List<LabWork> labWorks = gson.fromJson(reader, new TypeToken<List<LabWork>>(){}.getType());
        reader.close();

        LabWorkCollection labWorkCollection = new LabWorkCollection();

        for (LabWork labWork : labWorks) {
            labWork.setId(labWorkCollection.generateId());
            labWork.setCreationDate(new Date());
            labWorkCollection.add(labWork);
        }

        return labWorkCollection;
    }
}
