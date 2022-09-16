package com.Petri_net;

import com.Petri_net.subpage.*;
import com.Petri_net.toppage.PrintJob;
import com.Petri_net.toppage.Printer;

import java.util.ArrayList;

public class Main {

    public ArrayList<PrintJob> jobs = new ArrayList<>();

    Printer hp = new HP();
    Printer canon = new Canon();
    Printer epson = new Epson();


    public void newJob(){
        PrintJob job1 =  new HP_Print_Request.Builder().add("Star Trek").add("Discovery").add("Season 4").build();
        jobs.add(job1);
        PrintJob job2 =  new Canon_Print_Request.Builder().add("Scorpion").add("Season 1").build();
        jobs.add(job2);
        PrintJob job3 =  new Epson_Print_Request.Builder().add("Holmes").add("Season 1").build();
        jobs.add(job3);
        PrintJob job4 =  new HP_Print_Request.Builder().add("Harry Potter").add("Episode 1").build();
        jobs.add(job4);
        PrintJob job5 =  new Epson_Print_Request.Builder().add("Holmes").add("Season 2").build();
        jobs.add(job5);
    }

    public void exec(){
        for (PrintJob pj : jobs) {
            switch (pj.getPrinterName()) {
                case "HP":
                    if (hp.getFlag()) { hp.print(pj); }
                    break;
                case "CANON":
                    if (canon.getFlag()) { canon.print(pj); }
                    break;
                case "EPSON":
                    if (epson.getFlag()) { epson.print(pj); }
                    break;
                default:
                    System.out.println("This Printer is not here.");
            }
        }
    }

    public void start(){
        newJob();
        exec();
        System.out.println(hp.getList_ed());
        System.out.println(canon.getList_ed());
        System.out.println(epson.getList_ed());
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
