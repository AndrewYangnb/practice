package com.Petri_net.subpage;
import com.Petri_net.toppage.*;

import java.util.ArrayList;
import java.util.List;

public class HP implements Printer{
    public boolean flag;
    public ArrayList<List<String>> list_ed;

    public HP() {
        this.flag = true;
        this.list_ed= new ArrayList<>();
    }

    @Override
    public Boolean getFlag(){
        return this.flag;
    }

    @Override
    public ArrayList<List<String>> getList_ed(){
        return this.list_ed;
    }
    @Override
    public void print(PrintJob pj) {
        this.flag = false;
        ArrayList<String> listed = new ArrayList<>();
        for (String s : pj.getList()) {
            String s_ed = "HP: " + s;
            listed.add(s_ed);
        }
        this.list_ed.add(listed);
//        listed.clear();
        this.flag = true;
    }
}
