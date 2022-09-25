package com.example.lab6.pojo;

import java.util.ArrayList;

public class Wizards {
    private ArrayList<Wizard> model;
    public Wizards(){
        model = new ArrayList<>();
    }

    public Wizards(ArrayList<Wizard> model) {
        this.model = model;
    }

    public ArrayList<Wizard> getModel() {
        return model;
    }

    public void setModel(ArrayList<Wizard> model) {
        this.model = model;
    }
}
