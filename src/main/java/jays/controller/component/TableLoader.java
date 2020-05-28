package jays.controller.component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.List;

public abstract class TableLoader <T>{
    private ObservableList<T> data = FXCollections.observableArrayList();
    private TableView<T> tableView;

    public TableLoader(TableView tableView){
        this.tableView = tableView;
        loadCell();
        tableView.setItems(data);
    }


    public abstract void loadCell();

    public void loadData(List<T> list){
        data.clear();
        data.addAll(list);
    }

    public void loadData(T ... data){
        this.data.clear();
        this.data.addAll(data);
    }

    public void add(T data){
        this.data.addAll(data);
    }

    public ObservableList<T> getList(){
        return data;
    }

    public void clearList(){
        data.clear();
    }
}
