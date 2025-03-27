package JsonSupport;

import Support.LabWorkCollection;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

public class JsonWriter {
    private final LabWorkCollection collection;
    private final String fileName;

    public JsonWriter(LabWorkCollection collection, String fileName) {
        this.collection = collection;
        this.fileName = fileName;
    }

    public void writeToJson() throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(collection.getAll());
        FileWriter fileWriter = new FileWriter("testing.json");
        fileWriter.write(json);
        fileWriter.close();
    }
}
