package com.example.lab6.repository;

import com.example.lab6.pojo.Wizard;
import com.example.lab6.pojo.Wizards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WizardService {
    @Autowired
    private WizardRepository repository;
    public WizardService(WizardRepository repository){
        this.repository = repository;
    }

    public Wizards retrieveWizard(){
        return new Wizards((ArrayList<Wizard>) repository.findAll());
    }

    public Wizard createWizard(Wizard wizard){
        return  repository.insert(wizard);
    }

    public  boolean deleteWizard(Wizard wizard){
        try {
            repository.delete(wizard);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean updateWizard(Wizard wizard){
//        return repository.save(wizard);
        try {
            repository.save(wizard);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
