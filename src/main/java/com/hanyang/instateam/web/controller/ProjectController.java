package com.hanyang.instateam.web.controller;

import com.hanyang.instateam.model.Collaborator;
import com.hanyang.instateam.model.Project;
import com.hanyang.instateam.model.Role;
import com.hanyang.instateam.service.CollaboratorService;
import com.hanyang.instateam.service.ProjectService;
import com.hanyang.instateam.service.RoleService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProjectController {
  @Autowired
  private ProjectService projectService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private CollaboratorService collaboratorService;

  // index page
  @RequestMapping("/index")
  public String lsitProjects(Model model) {
    List<Project> projects = projectService.findAll();

    model.addAttribute("projects", projects);
    return "index";
  }

  @RequestMapping("/")
  public String home() {
    return "redirect:/index";
  }

  @RequestMapping("/addproject")
  public String formAddProject(Model model) {
    if (!model.containsAttribute("project")) {
      model.addAttribute("project", new Project());
    }

    model.addAttribute("action", "/index");
    model.addAttribute("submit", "Add");
    model.addAttribute("roles", roleService.findAll());
    model.addAttribute("roleId", null);

    return "edit_project";
  }

  @RequestMapping("/edit/{projectId}")
  public String formEditProject(@PathVariable Long projectId, Model model) {
    Project project = projectService.findById(projectId);
    List<Long> roleId = new ArrayList<>();
    List<Role> rolesNeeded = project.getRolesNeeded();
    for (Role role : rolesNeeded) {
      roleId.add(role.getId());
    }

    model.addAttribute("project", project);
    model.addAttribute("action", String.format("/project/%s", projectId));
    model.addAttribute("submit", "Save");
    model.addAttribute("roleId", roleId);
    model.addAttribute("roles", roleService.findAll());

    return "edit_project";
  }

  @RequestMapping("project_collaborators/{projectId}")
  public String formEditCollaborator(@PathVariable Long projectId, Model model) {
    Project project = projectService.findById(projectId);
    List<Role> roles = project.getRolesNeeded();
    List<Collaborator> collaborators = project.getCollaborators();
    List<Long> collaboratorsId = new ArrayList<>();
    for (Collaborator collaborator : collaborators) {
      collaboratorsId.add(collaborator.getId());
    }

    model.addAttribute("project", project);
    model.addAttribute("roles", roles);
    model.addAttribute("collaboratorsId", collaboratorsId);
    model.addAttribute("collaboratorService", collaboratorService);
    return "project_collaborators";
  }

  @RequestMapping(value = "/edit_collaborators/{projectId}", method = RequestMethod.POST)
  public String editCollaborator(@Valid Project project, BindingResult result, @PathVariable Long projectId, @RequestParam("projectCollaborators") List<Long> collaboratorId, RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.project", result);
      redirectAttributes.addFlashAttribute("project", project);

      return String.format("redirect:/edit_collaborators/%s", projectId);
    }
    List<Collaborator> collaborators = new ArrayList<>();

    if (collaboratorId != null) {
      for (Long id : collaboratorId) {
        if (id != null) collaborators.add(collaboratorService.findById(id));
      }
    }

    project.setCollaborators(collaborators);
    projectService.save(project);

    return String.format("redirect:/detail/%s", projectId);
  }

  @RequestMapping(value = "/index", method = RequestMethod.POST)
  public String addProject(@Valid Project project, BindingResult result, RedirectAttributes redirectAttributes, @RequestParam(value = "project_roles", required = false) List<Long> ids) {
    if (result.hasErrors()) {
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.project", result);

      redirectAttributes.addFlashAttribute("project", project);

      return "redirect:/addproject";
    }

    List<Role> roles = new ArrayList<>();
    if (ids != null) {
      for (Long id : ids) {
        roles.add(roleService.findById(id));
      }
    }
    project.setRolesNeeded(roles);

    projectService.save(project);

    return "redirect:/index";
  }

  @RequestMapping(value = "/project/{projectId}", method = RequestMethod.POST)
  public String editProject(@Valid Project project, BindingResult result, RedirectAttributes redirectAttributes, @RequestParam(value = "project_roles", required = false) List<Long> ids) {
    if (result.hasErrors()) {
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.project", result);

      redirectAttributes.addFlashAttribute("project", project);

      return String.format("redirect:/edit/%s", project.getId());
    }

    List<Role> roles = new ArrayList<>();

    if (ids != null) {
      for (Long id : ids) {
        roles.add(roleService.findById(id));
      }
    }

    project.setRolesNeeded(roles);

    projectService.save(project);

    return String.format("redirect:/detail/%s", project.getId());
  }

  @RequestMapping("/detail/{projectId}")
  public String projectDetail(@PathVariable Long projectId, Model model) {
    Project project = projectService.findById(projectId);
    //List<Role> roles = project.getRolesNeeded();
    List<Collaborator> collaborators = project.getCollaborators();

    model.addAttribute("project", project);
    //model.addAttribute("roles", roles);
    model.addAttribute("collaborators", collaborators);
    return "project_detail";
  }
}
