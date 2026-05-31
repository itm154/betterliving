package org.betterliving.repository;

import org.betterliving.model.LearningModule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LearningModuleRepository {
	private static final String DB_URL = "jdbc:derby:betterlivingDB;create=true";

	public LearningModuleRepository() {
		createTableIfNotExists();
	}

	private void createTableIfNotExists() {
		String sql = "CREATE TABLE LEARNING_MODULES (" +
				"ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
				"TITLE VARCHAR(255), " +
				"CONTENT VARCHAR(4000), " +
				"IMAGE_PATH VARCHAR(255))";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			if (!e.getSQLState().equals("X0Y32")) {
				e.printStackTrace();
			}
		}
	}

	public List<LearningModule> getAllModules() {
		List<LearningModule> modules = new ArrayList<>();
		String sql = "SELECT * FROM LEARNING_MODULES";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				modules.add(new LearningModule(
						rs.getInt("ID"),
						rs.getString("TITLE"),
						rs.getString("CONTENT"),
						rs.getString("IMAGE_PATH")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return modules;
	}

	public void addModule(LearningModule module) {
		String sql = "INSERT INTO LEARNING_MODULES (TITLE, CONTENT, IMAGE_PATH) VALUES (?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, module.getTitle());
			pstmt.setString(2, module.getContentText());
			pstmt.setString(3, module.getImagePath());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateModule(LearningModule module) {
		String sql = "UPDATE LEARNING_MODULES SET TITLE = ?, CONTENT = ?, IMAGE_PATH = ? WHERE ID = ?";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, module.getTitle());
			pstmt.setString(2, module.getContentText());
			pstmt.setString(3, module.getImagePath());
			pstmt.setInt(4, module.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteModule(int id) {
		String sql = "DELETE FROM LEARNING_MODULES WHERE ID = ?";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
