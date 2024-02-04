package database;

import beans.Result;

import java.util.List;

public interface ResultInterface {
    void save(Result result);

    boolean clear();

    List<Result> getAll();
}
