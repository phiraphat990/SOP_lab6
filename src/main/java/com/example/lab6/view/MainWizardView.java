package com.example.lab6.view;

import com.example.lab6.pojo.Wizard;
import com.example.lab6.pojo.Wizards;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Route("mainPage.it")
public class MainWizardView extends VerticalLayout {
    private TextField fullName, money;
    private RadioButtonGroup<String> genderGroup;
    private ComboBox<String> position, school, house;
    private Button previous, create, update, delete, next;

    private HorizontalLayout buttonGroup;

    private Wizards wizards;

    private int index = -1;

    public MainWizardView() {
        fullName = new TextField();
        fullName.setPlaceholder("Fullname");

        genderGroup = new RadioButtonGroup<>();
        genderGroup.setItems("Male", "Female");

        position = new ComboBox<>();
        position.setItems("Student", "Teacher");
        position.setPlaceholder("Position");

        money = new TextField();
        money.setPrefixComponent(new Span("$"));
        money.setLabel("Dollars");

        school = new ComboBox<>();
        school.setItems("Hogwarts", "Beauxbatons", "Durmstrang");
        school.setPlaceholder("School");

        house = new ComboBox<>();
        house.setItems("Gryffindor", "Ravenclaw", "Hufflepuff", "Slyther");
        house.setPlaceholder("House");

        previous = new Button("<<");
        create = new Button("create");
        update = new Button("update");
        delete = new Button("delete");
        next = new Button(">>");

        buttonGroup = new HorizontalLayout();

        buttonGroup.add(previous, create, update, delete, next);

        this.add(fullName, genderGroup, position, money, school, house, buttonGroup);

        this.wizards = this.getAllWizard();

        previous.addClickListener(event -> {
            --this.index;
            if (this.index <= 0) {
                this.index = this.wizards.getModel().toArray().length - 1;
            }
            this.setAllItem();
        });

        next.addClickListener(event -> {
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
        fullName.setValue(wizard.getName());

        if (wizard.getSex().equals("m")) {
            this.genderGroup.setValue("Male");
        }
        else {
            this.genderGroup.setValue("Female");
        }
        position.setValue(wizard.getPosition());
        money.setValue(String.valueOf(wizard.getMoney()));
        school.setValue(wizard.getSchool());
        house.setValue(wizard.getHouse());
    }

    public void createWizard() {
        Wizard wizard = new Wizard();
        wizard.setSex(genderGroup.getValue());
        wizard.setName(fullName.getValue());
        wizard.setSchool(school.getValue());
        wizard.setHouse(house.getValue());
        wizard.setMoney(Integer.parseInt((money.getValue())));
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
            new Notification("Wizard has been Create", 1000).open();
            this.wizards = this.getAllWizard();
        }
        else {
            new Notification("Add Failed", 1000).open();
        }
    }

    public void updateWizard() {
        Wizard wizard = this.wizards.getModel().get(this.index);
        wizard.setSex(genderGroup.getValue());
        wizard.setName(fullName.getValue());
        wizard.setSchool(school.getValue());
        wizard.setHouse(house.getValue());
        wizard.setMoney(Integer.parseInt(money.getValue()));
        wizard.setPosition(position.getValue());

        System.out.println(wizard.get_id());

        Boolean out = WebClient.create()
                .post()
                .uri("http://localhost:8080/updateWizard")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(wizard)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (out) {
            new Notification("Wizard has been Update", 1000).open();
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
            new Notification("Wizard has been Delete", 1000).open();
            this.wizards = this.getAllWizard();
            this.setAllItem();
        }
        else {
            new Notification("Delete Failed", 1000).open();
        }
    }
}