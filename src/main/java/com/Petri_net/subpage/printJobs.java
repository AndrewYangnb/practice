package com.Petri_net.subpage;
import com.Petri_net.toppage.*;

import java.util.ArrayList;

public class printJobs implements PrintJob{
    public String printerName;
    public ArrayList<String> list;

    private printJobs(Builder builder){
        this.printerName = builder.printerName;
        this.list = builder.l;
    }

    public static class Builder {
        public String printerName;
        private ArrayList<String> l = new ArrayList<>();

        public Builder(){ }

        public Builder printerName(String s){
            this.printerName = s.toUpperCase();
            return this;
        }

        public Builder add(String s) {
            this.l.add(s);
            return this;
        }

        public printJobs build() {
            return new printJobs(this);
        }
    }
}
