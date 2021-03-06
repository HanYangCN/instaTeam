package com.hanyang.instateam.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Pattern(regexp = "[0-9a-zA-Z]+")
  private String name;

  @NotNull
  @Size(min = 1)
  private String description;

  @Pattern(regexp = "[0-9a-zA-Z]+")
  private String status;

  @ManyToMany
  private List<Role> rolesNeeded;

  @ManyToMany
  private List<Collaborator> collaborators;

  public Project(){}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<Role> getRolesNeeded() {
    return rolesNeeded;
  }

  public void setRolesNeeded(List<Role> rolesNeeded) {
    this.rolesNeeded = rolesNeeded;
  }

  public List<Collaborator> getCollaborators() {
    return collaborators;
  }

  public void setCollaborators(List<Collaborator> collaborators) {
    this.collaborators = collaborators;
  }
}
