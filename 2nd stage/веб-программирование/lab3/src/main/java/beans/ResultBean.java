package beans;

import database.ResultInterface;
import lombok.Data;
import lombok.Getter;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
public class ResultBean implements Serializable {

    @Inject
    private ResultInterface resultInterface;

    private Result currResult;
    private List<Result> resultList;
    @Getter
    private String source;

    @PostConstruct
    private void initialize() {
        currResult = new Result();
        updateLocal();
    }

    private void updateLocal() {
        resultList = resultInterface.getAll();
    }

    public void addResult() {
        Result copyResult = new Result(currResult);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String requestTime = dateFormat.format(new Date(System.currentTimeMillis()));
        copyResult.setRequestTime(requestTime);
        resultInterface.save(copyResult);
        updateLocal();
    }

    public void clearResults() {
        resultInterface.clear();
        resultList = resultInterface.getAll();
        updateLocal();
    }

    public void setSource(String source) {
        this.source = source;
    }
}
