package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.AppService;

import javax.persistence.PersistenceException;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AppService appService;

    @Autowired
    public AdminController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("")
    public String showUserList(Model model) {
        model.addAttribute("users", appService.findAllUsers());
        appService.loadUserByUsername("123");
        return "user-list";
    }

    @GetMapping(value = "/new")
    public String addUserForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("allRoles", appService.findAllRoles());

        return "user-form";
    }

    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable(value = "id", required = true) long userId, Model model) {
        try {
            model.addAttribute("user", appService.findUser(userId));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "redirect:/admin";
        }
        model.addAttribute("allRoles", appService.findAllRoles());

        return "user-form";
    }

    @PostMapping()
    public String createOrUpdateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", appService.findAllRoles());
            return "user-form";
        }
        try {
            appService.createOrUpdateUser(user);
        } catch (PersistenceException e) {
            // TODO: set error
            model.addAttribute("PersistenceError", String.format("User with email %s exists", user.getEmail()));
            model.addAttribute("allRoles", appService.findAllRoles());
            e.printStackTrace();
            return "user-form";
        }

        return "redirect:/admin";
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") long userId) {
        appService.deleteUser(userId);

        return "redirect:/admin";
    }

    /*
    @PutMapping
    public String addNewUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        return "redirect:/admin";
    }

    @PatchMapping
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        return "redirect:/admin";
    }

    @DeleteMapping
    public String deleteUser(@RequestParam(value = "id", required = true, defaultValue = "") long id) {
        return "redirect:/admin";
    }*/
}
