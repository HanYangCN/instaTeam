package com.hanyang.instateam.dao;

import com.hanyang.instateam.model.Project;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDaoImpl implements ProjectDao {
  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public List<Project> findAll() {
    Session session = sessionFactory.openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Project> criteria = builder.createQuery(Project.class);
    criteria.from(Project.class);
    List<Project> projects = session.createQuery(criteria).getResultList();
    session.close();
    return projects;
  }

  @Override
  public Project findById(Long id) {
    Session session = sessionFactory.openSession();
    Project project = session.get(Project.class, id);
    Hibernate.initialize(project.getCollaborators());
    Hibernate.initialize(project.getRolesNeeded());
    session.close();
    return project;
  }

  @Override
  public void save(Project project) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    session.saveOrUpdate(project);
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void delete(Project project) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    session.delete(project);
    session.getTransaction().commit();
    session.close();
  }
}
