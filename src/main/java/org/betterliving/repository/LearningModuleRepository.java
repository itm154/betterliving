package org.betterliving.repository;

import org.betterliving.model.LearningModule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LearningModuleRepository implements Storable<LearningModule> {
	private static final String DB_URL = "jdbc:derby:betterlivingDB;create=true";

	public LearningModuleRepository() {
		try (Connection conn = DriverManager.getConnection(DB_URL)) {
			if (!tableExists(conn, "LEARNING_MODULES")) {
				try (Statement stmt = conn.createStatement()) {
					stmt.execute("CREATE TABLE learning_modules (" +
							"id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
							"title VARCHAR(255), " +
							"content VARCHAR(4000), " +
							"image_data BLOB)");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean tableExists(Connection conn, String tableName) throws SQLException {
		DatabaseMetaData meta = conn.getMetaData();
		try (ResultSet rs = meta.getTables(null, null, tableName.toUpperCase(), null)) {
			return rs.next();
		}
	}

	public List<LearningModule> findAll() {
		List<LearningModule> modules = new ArrayList<>();
		String sql = "SELECT * FROM learning_modules";
		try (Connection conn = DriverManager.getConnection(DB_URL);
		     Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				modules.add(new LearningModule(
						rs.getInt("id"),
						rs.getString("title"),
						rs.getString("content"),
						rs.getBytes("image_data")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return modules;
	}

	public void save(LearningModule module) {
		if (module.getId() == 0) {
			String sql = "INSERT INTO learning_modules (title, content, image_data) VALUES (?, ?, ?)";
			try (Connection conn = DriverManager.getConnection(DB_URL);
			     PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				pstmt.setString(1, module.getTitle());
				pstmt.setString(2, module.getContentText());
				if (module.getImageBytes() != null) {
					pstmt.setBytes(3, module.getImageBytes());
				} else {
					pstmt.setNull(3, Types.BLOB);
				}
				pstmt.executeUpdate();
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						module.setId(generatedKeys.getInt(1));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			String sql = "UPDATE learning_modules SET title = ?, content = ?, image_data = ? WHERE id = ?";
			try (Connection conn = DriverManager.getConnection(DB_URL);
			     PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, module.getTitle());
				pstmt.setString(2, module.getContentText());
				if (module.getImageBytes() != null) {
					pstmt.setBytes(3, module.getImageBytes());
				} else {
					pstmt.setNull(3, Types.BLOB);
				}
				pstmt.setInt(4, module.getId());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteById(int id) {
		String sql = "DELETE FROM learning_modules WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(DB_URL);
		     PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
