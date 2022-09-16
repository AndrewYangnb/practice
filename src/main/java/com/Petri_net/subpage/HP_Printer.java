package com.Petri_net.subpage;

import com.Petri_net.toppage.PrintJob;

public class HP_Printer implements PrintJob {
    String printerName = "Canon";

    @Override
    public String getPrinterName() {
        return printerName;
    }
}

class Main{
    public static void main(String[] args) {
        PrintJob hp_request = new HP_Printer();
        System.out.println(hp_request.getPrinterName());
    }
}
