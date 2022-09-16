package com.Petri_net.subpage;
import com.Petri_net.toppage.*;

import java.util.ArrayList;
import java.util.List;

public class Canon implements Printer{
    public boolean flag;

    public ArrayList<List<String>> list_ed;

    public Canon() {
        this.flag = true;
        this.list_ed = new ArrayList<>();
    }

    @Override
    public Boolean getFlag() {
        return this.flag;
    }

    @Override
    public ArrayList<List<String>> getList_ed() {
        return this.list_ed;
    }

    @Override
    public void print(PrintJob pj) {
        this.flag = false;
        ArrayList<String> listed = new ArrayList<>();
        for (String s : pj.getList()) {
            String s_ed = "Canon: " + s;
            listed.add(s_ed);
        }
        this.list_ed.add(listed);
        this.flag = true;

    }

}
