package com.Petri_net.subpage;

import com.Petri_net.toppage.PrintJob;

import java.util.ArrayList;
import java.util.List;

public class HP_Print_Request implements PrintJob {
    String printerName = "HP";
    public ArrayList<String> list;

    private HP_Print_Request(Builder builder){
        this.list = builder.l;
    }
    @Override
    public String getPrinterName() {
        return printerName.toUpperCase();
    }

    @Override
    public List<String> getList(){
        return this.list;
    }

    public static class Builder {

        private ArrayList<String> l = new ArrayList<>();

        public Builder(){ }

        public Builder add(String s) {
            this.l.add(s);
            return this;
        }

        public HP_Print_Request build() {
            return new HP_Print_Request(this);
        }
    }

}
