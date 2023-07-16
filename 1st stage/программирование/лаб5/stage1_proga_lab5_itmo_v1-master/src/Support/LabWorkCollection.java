package Support;

import Commands.*;
import CommandsSupport.Command;
import Support.LabWork;

import java.io.BufferedReader;
import java.util.*;

public class LabWorkCollection {
    public Map<String, Command> BufferOfCommandMap;
    private Hashtable<Long, LabWork> labWorks;
    InsertCommand insertCommand;
    UpdateCommand updateCommand;
    ReplaceIfGreater replaceIfGreater;
    FilterGraterThanMinimalPoint filterGraterThanMinimalPoint;
    ExecuteScriptCommand executeScriptCommand;
    ReplaceIfLowe replaceIfLowe;

    Date CDate = new Date();
    private long lastId = 0;
    public LabWorkCollection() {
        this.labWorks = new Hashtable<>();
    }

    public void add(LabWork labWork) {
        labWorks.put(labWork.getId(), labWork);
    }

    public void remove(long id) {
        labWorks.remove(id);
    }

    public LabWork getById(long id) {
        return labWorks.get(id);
    }

    public Collection<LabWork> getAll() {
        return labWorks.values();
    }

    public long generateId() {
        return ++lastId;
    }

    public int getSize() {
        return labWorks.size();
    }

    public java.util.Date getInitializationDate() {
        return CDate;
    }

    public void clearAll() {
        labWorks.clear();
    }

    public Map<String, Command> sendCommandMap(){
        return this.BufferOfCommandMap;
    }
    public void getMappingOfCommands(Map<String, Command> map) {
        this.BufferOfCommandMap = map;
    }
    public void changeReaders(BufferedReader bufferedReader){
        insertCommand.changeReader(bufferedReader);
        updateCommand.changeReader(bufferedReader);
        replaceIfGreater.changeReader(bufferedReader);
        filterGraterThanMinimalPoint.changeReader(bufferedReader);
        executeScriptCommand.changeReader(bufferedReader);
        replaceIfLowe.changeReader(bufferedReader);

    }
    public void getInsetCommand(InsertCommand insertCommand) {
        this.insertCommand = insertCommand;
    }
    public void getUpdateCommand(UpdateCommand updateCommand) {
        this.updateCommand = updateCommand;
    }
    public void getReplaceIfGreater(ReplaceIfGreater replaceIfGreater) {
        this.replaceIfGreater = replaceIfGreater;
    }
    public void getFilterGraterThanMinimalPoint(FilterGraterThanMinimalPoint filterGraterThanMinimalPoint) {
        this.filterGraterThanMinimalPoint = filterGraterThanMinimalPoint;
    }
    public void getExecuteScriptCommand(ExecuteScriptCommand executeScriptCommand) {
        this.executeScriptCommand = executeScriptCommand;
    }
    public void getReplaceIfLowe(ReplaceIfLowe replaceIfLowe) {
        this.replaceIfLowe = replaceIfLowe;
    }

}
