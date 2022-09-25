package com.example.lab6.view;

import com.example.lab6.pojo.Wizard;
import com.example.lab6.pojo.Wizards;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
@Route(value = ("mainPage.it"))
public class MainWizardView extends VerticalLayout {
    private TextField fullname;
    private NumberField dollars;
    private ComboBox<String> position, school,house;
    private Button back, create,update,delete,push;
    private Text gender;
    private RadioButtonGroup check;

    private Wizards wizards;
    private int index = -1;
    public MainWizardView(){
        fullname = new TextField();
        fullname.setPlaceholder("Fullname");

        gender = new Text("Gender :");
        check = new RadioButtonGroup<>();
        check.setItems("Male","Female");

        position = new ComboBox<>();
        position.setPlaceholder("Position");
        position.setItems("Student", "Teacher");

        dollars = new NumberField("Dollars");
        dollars.setPrefixComponent(new Span("$"));

        school = new ComboBox<>();
        school.setPlaceholder("School");
        school.setItems("Hogwarts", "Beauxbatons", "Durmstrang");

        house = new ComboBox<>();
        house.setPlaceholder("House");
        house.setItems("Gryffindor", "Ravenclaw", "Hufflepuff", "Slyther");

        back =new Button("<<");
        create = new Button("Create");
        update = new Button("Update");
        delete = new Button("Delete");
        push = new Button(">>");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(back,create,update,delete,push);
        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.add(check);
        add(fullname,gender,horizontalLayout2,position,dollars,school,house,horizontalLayout);

        this.wizards = this.getAllWizard();

        push.addClickListener(event -> {
            --this.index;
            if (this.index <= 0) {
                this.index = 0;
            }
            this.setAllItem();
        });

        back.addClickListener(event -> {
            ++ this.index;
            if (this.index >= this.wizards.getModel().toArray().length - 1) {
                this.index = this.wizards.getModel().toArray().length - 1;
            }
            this.setAllItem();
        });

        create.addClickListener(event -> {
            this.createWizard();
        });

        update.addClickListener(event -> {
            this.updateWizard();
        });

        delete.addClickListener(event -> {
            this.deleteWizard();
        });


    }
    public Wizards getAllWizard() {
        Wizards out = WebClient.create()
                .get()
                .uri("http://localhost:8080/wizards")
                .retrieve()
                .bodyToMono(Wizards.class)
                .block();

        return out;
    }
    public void setAllItem() {
        if (this.index <= 0) {
            this.index = 0;
        }
        if (this.index >= this.wizards.getModel().toArray().length - 1) {
            this.index = this.wizards.getModel().toArray().length - 1;
        }

        Wizard wizard = this.wizards.getModel().get(this.index);
        fullname.setValue(wizard.getName());

        if (wizard.getSex().equals("m")) {
            this.check.setValue("Male");
        }
        else {
            this.check.setValue("Female");
        }
        position.setValue(wizard.getPosition());
        dollars.setValue(Double.valueOf(wizard.getMoney()));
        school.setValue(wizard.getSchool());
        house.setValue(wizard.getHouse());
    }

    public void createWizard() {
        Wizard wizard = new Wizard();
        wizard.setSex((String) check.getValue());
        wizard.setName(fullname.getValue());
        wizard.setSchool(school.getValue());
        wizard.setHouse(house.getValue());
        wizard.setMoney(Double.parseDouble(String.valueOf(dollars.getValue())));
        wizard.setPosition(position.getValue());
        Boolean out = WebClient.create()
                .post()
                .uri("http://localhost:8080/addWizard")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(wizard)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (out) {
            new Notification("Add Successfully", 1000).open();
            this.wizards = this.getAllWizard();
        }
        else {
            new Notification("Add Failed", 1000).open();
        }
    }

    public void updateWizard() {
        Wizard wizard = this.wizards.getModel().get(this.index);
        wizard.setSex((String) check.getValue());
        wizard.setName(fullname.getValue());
        wizard.setSchool(school.getValue());
        wizard.setHouse(house.getValue());
        wizard.setMoney(Double.parseDouble(String.valueOf(dollars.getValue())));
        wizard.setPosition(position.getValue());

        System.out.println(wizard.get_id());

        boolean out = WebClient.create()
                .post()
                .uri("http://localhost:8080/updateWizard")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(wizard)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (out) {
            new Notification("Update Successfully", 1000).open();
            this.wizards = this.getAllWizard();
        }
        else {
            new Notification("Update Failed", 1000).open();
        }
    }

    public void deleteWizard() {
        Wizard wizard = this.wizards.getModel().get(this.index);
        Boolean out = WebClient.create()
                .post()
                .uri("http://localhost:8080/deleteWizard")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(wizard)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (out) {
            new Notification("Delete Successfully", 1000).open();
            this.wizards = this.getAllWizard();
            this.setAllItem();
        }
        else {
            new Notification("Delete Failed", 1000).open();
        }
    }

}
