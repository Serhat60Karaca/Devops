package com.example.project1.controller;

import com.example.project1.entity.Person;
import com.example.project1.service.PersonService;
import com.example.project1.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class PersonController {
    @Autowired
    private PersonService service;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/person_register")
    public String personRegister(Model model) {
        model.addAttribute("person", new Person());
        return "personRegister";
    }

    @GetMapping("/person")
    public ModelAndView getAllPerson() {
        List<Person> list = service.getAllPerson();
        return new ModelAndView("allPerson", "person", list);
    }

    @PostMapping("/save")
    public String addPerson(@RequestParam("name") String name,
                            @RequestParam("address") String address,
                            @RequestParam("image") MultipartFile image) throws IOException
    //, @RequestParam("image") MultipartFile file) throws IOException
    {
        String originalFilename = image.getOriginalFilename();
        Path cleanPath = Paths.get(originalFilename).normalize();
        String fileName = cleanPath.toString().replaceAll("\\s", "");
        Person p = new Person();
        p.setImage(fileName);
        p.setAddress(address);
        p.setName(name);

        service.save(p);
        String uploadDir = "src/main/resources/static/images/";

        fileName = p.getId() + "-" + fileName;
        FileUploadUtil.saveFile(uploadDir, fileName, image);
        return "redirect:/person";
    }



    @GetMapping("/deletePerson/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        service.deleteById(id);
        return "redirect:/person";
    }

    @GetMapping("/editPerson/{id}")
    public String editPerson(@PathVariable("id") int id, Model model) {
        Person b = service.getPersonById(id);
        if (b != null) {
            model.addAttribute("person", b);
            return "personEdit";
        } else {

            return "error";
        }
    }
}
