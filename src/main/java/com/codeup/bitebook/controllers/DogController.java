//package com.codeup.bitebook.controllers;
//
//import com.codeup.bitebook.models.Dog;
//import com.codeup.bitebook.models.EmailService;
//import com.codeup.bitebook.repositories.DogRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.List;
//import java.util.Optional;
//
//@AllArgsConstructor
//
//@Controller
//@RequestMapping("/dogs")
//public class DogController {
//    private DogRepository dogDao;
//private EmailService emailService;
//
//    @GetMapping
//    @ResponseBody
//    public String index() {
//        List<Dog> dogs = dogDao.findAll();
//
//        System.out.println(dogs);
//
//        List<Dog> sDogs = dogDao.searchByName("u");
//        System.out.println(sDogs);
//
//        return "show all dogs here";
//    }
//
//    @GetMapping("/{id}")
//    @ResponseBody
//    public String fetchById(@PathVariable Long id) {
//        Optional<Dog> optionalDog = dogDao.findById(id);
//        if(optionalDog.isEmpty()) {
//            return "no dog found. return a 404 instead";
//        }
//        Dog dog = optionalDog.get();
//        return dog.toString();
//    }
//
//    @GetMapping("/create")
//    public String create() {
//        return "/dogs/create";
//    }
//
////    @PostMapping("/create")
////    @ResponseBody
////    public String create(@RequestParam Long id
////            , @RequestParam String dogName
////            , @RequestParam int age
////            , @RequestParam String ownerName) {
////        System.out.printf("%d %s %d %s\n", id, dogName, age, ownerName);
////        Dog dog = new Dog(id, dogName, age);
////        emailService.prepareAndSend(dog,"Hello Doggy","Your dog name is " + dog.getName());
////
////        dogDao.save(dog);
////
////        return "dog created???";
////    }
//
//    @GetMapping("/{id}/delete")
//    @ResponseBody
//    public String delete(@PathVariable Long id) {
//        dogDao.deleteById(id);
//        return "dog " + id + " deleted";
//    }
//
//}