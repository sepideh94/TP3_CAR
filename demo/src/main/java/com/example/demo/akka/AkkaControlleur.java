package com.example.demo.akka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import akka.actor.ActorSystem;

@Controller
@RequestMapping("/akka")
public class AkkaControlleur {

    private final AkkaService akkaService;
    private final ActorSystem system;
   

    @Autowired  
    public AkkaControlleur(AkkaService akkaService) {
        this.akkaService = akkaService;
        this.system = akkaService.getSystem();  
    }
    
    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("/home");  
    }
    
    @PostMapping("/init")
    public String initActors() {
        akkaService.initActors();
        return "home";
    }
    
}
