package com.example.lab6.controller;

import com.example.lab6.pojo.Wizard;
import com.example.lab6.pojo.Wizards;
import com.example.lab6.repository.WizardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class WizardController {
    @Autowired
    private WizardService service;
    public WizardController(WizardService service){
        this.service = service;
    }
    @RequestMapping(value ="/wizards", method = RequestMethod.GET)
    public ResponseEntity<?> getAllWizard(){
        return ResponseEntity.ok(service.retrieveWizard());
    }

    @RequestMapping(value ="/addWizard", method = RequestMethod.POST)
    public  ResponseEntity<?> addWizard(@RequestBody Wizard wizard){
        return ResponseEntity.ok(service.createWizard(wizard));
    }

    @RequestMapping(value ="/updateWizard", method = RequestMethod.POST)
    public  boolean update(@RequestBody Wizard wizard){
        return service.updateWizard(wizard);
    }

    @RequestMapping(value ="/deleteWizard", method = RequestMethod.POST)
    public boolean delete(@RequestBody Wizard wizard){
        return service.deleteWizard(wizard);
    }
}
