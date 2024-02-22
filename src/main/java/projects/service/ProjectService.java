package projects.service;


import java.util.List;

import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectService {
		private ProjectDao projectDao = new ProjectDao();
		public Project addProject(Project project) {
			return projectDao.insertProject(project);
		}
		public List<Project> fetchAllProjects() {
			return fetchAllProjects();
		}
	}

