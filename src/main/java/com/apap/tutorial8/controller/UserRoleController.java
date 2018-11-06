package com.apap.tutorial8.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.apap.tutorial8.model.PassModel;
import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	private String addUserSubmit(@ModelAttribute UserRoleModel user) {
		userService.addUser(user);
		return "home";
	}
	
	@RequestMapping(value="/changePass",method=RequestMethod.POST)
    public ModelAndView updatePasswordSubmit(@ModelAttribute PassModel pass, Model model, RedirectAttributes redirect) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserRoleModel user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        String message = "";
        
        if(passwordEncoder.matches(pass.getOldPass(),user.getPassword())) {
        	if(pass.getNewPass2().equals(pass.getNewPass())) {
        		userService.changePass(user, pass.getNewPass());
        		message="Password telah berhasil diubah";
        	}
        	else {
        		message = "Konfirmasi password baru tidak sesuai";
        	}
        }
        else {
        	message="Password lama Anda tidak sesuai";
        }
        
        
        ModelAndView modelAndView = new ModelAndView("redirect:/updatePassword");
        redirect.addFlashAttribute("message",message);
        return modelAndView;
    }
}
