package com.cydeo.controller;

import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.RoleService;
import com.cydeo.service.UserService;
import com.cydeo.validations.UserValidations;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@PreAuthorize("hasAuthority('Admin')")
@RequestMapping("/user")
public class UserController {
     private final RoleService roleService;
     private final UserService userService;
     private final UserValidations userValidations;
    @GetMapping("/create")
    public String userCreate(Model model){
        model.addAttribute("user",new UserDTO());
        model.addAttribute("roles",roleService.findAll());
        model.addAttribute("userList",userService.findAll());
        return "user/create";
    }
    @PostMapping("/save")
    public String userSave(@Valid @ModelAttribute("user") UserDTO user, BindingResult bindingResult, Model model){

        bindingResult = userValidations.addCustomValidationsCreate(user,bindingResult);

        if(bindingResult.hasErrors()){

            model.addAttribute("roles",roleService.findAll());
            model.addAttribute("userList",userService.findAll());

            return "user/create";
        }
        userService.save(user);
        return "redirect:/user/create";
    }

    @GetMapping("/delete/{userName}")
    public String userDelete(@PathVariable("userName") String username){
        userService.delete(username);
        return "redirect:/user/create";
    }
    @GetMapping("/update/{userName}")
    public String userUpdate(@PathVariable("userName") String username, Model model){

        model.addAttribute("user",userService.findById(username));
        model.addAttribute("roles",roleService.findAll());
        model.addAttribute("userList",userService.findAll());

        return "/user/update";
    }

    @PostMapping("/update-save")
    public String userUpdate(@Valid @ModelAttribute("user") UserDTO user, BindingResult bindingResult, Model model){
//        bindingResult = userValidations.addCustomValidationsUpdate(user,bindingResult);

        if (bindingResult.hasFieldErrors("firstName") || bindingResult.hasFieldErrors("lastName")
                || bindingResult.hasFieldErrors("userName")|| bindingResult.hasFieldErrors("phone")
                || bindingResult.hasFieldErrors("role")|| bindingResult.hasFieldErrors("gender")){
            model.addAttribute("roles",roleService.findAll());
            model.addAttribute("userList",userService.findAll());
            return "/user/update";
        }

        userService.update(user);

        return "redirect:/user/create";
    }


}
