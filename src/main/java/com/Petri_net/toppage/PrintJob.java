package com.Petri_net.toppage;

import java.util.ArrayList;
import java.util.List;

public interface PrintJob {

    default String getPrinterName(){
        return null;
    }

    default List<String> getList(){
        return null;
    }

//    ArrayList<String> list = null;

}
