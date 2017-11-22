package com.hanyang.instateam.web.controller;

import com.hanyang.instateam.model.Collaborator;
import com.hanyang.instateam.service.CollaboratorService;
import com.hanyang.instateam.service.RoleService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CollaboratorController {
  @Autowired
  private CollaboratorService collaboratorService;

  @Autowired
  private RoleService roleService;

  @RequestMapping("/collaborators")
  public String listCollaborators(Model model) {
    model.addAttribute("collaborator", new Collaborator());
    model.addAttribute("collaborators", collaboratorService.findAll());
    model.addAttribute("roles", roleService.findAll());
    return "collaborators";
  }

  @RequestMapping(value = "/addcollaborator", method = RequestMethod.POST)
  public String addCollaborator(@Valid Collaborator collaborator, BindingResult result, RedirectAttributes redirectAttributes) {
    System.out.println(collaborator.getName() + (collaborator.getRole() == null ? "null" : "not null"));
    if (result.hasErrors()) {
      System.out.println("Error");
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.collaborator", result);
      redirectAttributes.addFlashAttribute("collaborator", collaborator);

      return "redirect:/collaborators";
    }

    System.out.println("No error");
    collaboratorService.save(collaborator);
    return "redirect:/collaborators";
  }
}
