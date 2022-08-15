package com.Petri_net.toppage;

import java.util.ArrayList;
import java.util.List;

public interface Printer {
    void print(PrintJob pj);

    default Boolean getFlag(){
        return null;
    }

    default ArrayList<List<String>> getList_ed(){
        return null;
    }

}
