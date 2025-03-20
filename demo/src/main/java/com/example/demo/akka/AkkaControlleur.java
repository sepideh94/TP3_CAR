package com.example.demo.akka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import akka.actor.ActorSystem;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import akka.actor.ActorRef;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.io.IOException;


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
    
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        akkaService.uploadFile(file);
        return "redirect:/akka/home?success=uploaded";
    }
    
    @GetMapping("/search")
    public ModelAndView getWordCount(@RequestParam("word") String word) throws Exception {
        Map<String, Integer> wordCounts = akkaService.wordCount();
        int count = wordCounts.getOrDefault(word, 0);

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("searchedWord", word);
        modelAndView.addObject("occurrence", count);

        return modelAndView;
    }

    
}
