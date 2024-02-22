package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale.Category;
import java.util.Objects;
import java.util.Optional;

import projects.entity.Project;
import projects.exception.DbException;
import provided.util.DaoBase;



public class ProjectDao extends DaoBase {
	@SuppressWarnings("unused")
	private static final String CATEGORY_TABLE = "category";
	@SuppressWarnings("unused")
	private static final String MATERIAL_TABLE = "material";
	private static final String PROJECT_TABLE = "project";
	@SuppressWarnings("unused")
	private static final String PROJECT_CATEGORY_TABLE = "project_category";
	@SuppressWarnings("unused")
	private static final String STEP_TABLE = "step";
 public Project insertProject(Project project) {
	 
	 String sql = ""
			 + " INSERT INTO " + PROJECT_TABLE + " "
			 + " (project_name, estimated_hours, actual_hours, difficulty, notes) "
			 + " VALUES "
			 + " (?, ?, ?, ? ,?)";
	 System.out.println(sql);
	 
	 try (Connection conn = DbConnection.getConnection()) {
		 startTransaction(conn);
		 
		 try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			 setParameter(stmt, 1, project.getProjectName(), String.class);
			 setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
			 setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
			 setParameter(stmt, 4, project.getDifficulty(), Integer.class);
			 setParameter(stmt, 5, project.getNotes(), String.class);
			 
			 stmt.executeUpdate();
			 
			 Integer projectId = getLastInsertId(conn, PROJECT_TABLE);
			 commitTransaction(conn);
			 
			 project.setProjectId(projectId);
			 return project;
		 }
		 catch(Exception e) {
			 rollbackTransaction(conn);
			 throw new DbException(e);
			 
		 }
	 }
 catch(SQLException e) {
	 throw new DbException(e); 
 }
 }
	 public Optional<Project> fetchProjectById(Integer projectId) {
		 @SuppressWarnings("unused")
		String sql = "SELECT * FROM " +  PROJECT_TABLE + " WHERE project_id = ?";
		 
		 try (Connection conn = DbConnection.getConnection()) {
			 startTransaction(conn);
			 
			 try {
				 Project project = null;
				 return Optional.ofNullable(project);
				 	
				 	try(PreparedStatement stmt = conn.prepareStatement(sql)) {
				 		setParameter(stmt, 1, projectId, Integer.class);
				 		
				 			try(ResultSet rs = stmt.executeQuery()) {
				 				if(rs.next()) {
				 					project = extract(rs, Project.class);
				 			}
				 			}
				 			}
				 	if(Objects.nonNull(project)) {
				 		project.getMaterials().addAll(fetchMaterialsForProject(conn, projectId));
				 		project.getSteps().addAll(fetchStepsForProject(conn, projectId));
				 		project.getCategories().addAll(fetchCategoriesForProject(conn, projectId));
				 			}
				 	commitTransaction(conn);
				 	return Optional.ofNullable(project);
			 				}
							 catch(Exception e) {
								 rollbackTransaction(conn);
								 throw new DbException(e);
			 				}
		 					}
							 catch(SQLException e) {
								 throw new DbException(e);
		 }
		
				 }
			 }
		 
 
 