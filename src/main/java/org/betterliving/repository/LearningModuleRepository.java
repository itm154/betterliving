package org.betterliving.repository;

import org.betterliving.model.LearningModule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LearningModuleRepository implements Storeable<LearningModule> {
	private static final String DB_URL = "jdbc:derby:betterlivingDB;create=true";

	public LearningModuleRepository() {
		try (Connection conn = DriverManager.getConnection(DB_URL)) {
			if (!tableExists(conn, "LEARNING_MODULES")) {
				try (Statement stmt = conn.createStatement()) {
					stmt.execute("CREATE TABLE LEARNING_MODULES (" +
							"ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
							"TITLE VARCHAR(255), " +
							"CONTENT VARCHAR(4000), " +
							"IMAGE_DATA BLOB)");
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
		String sql = "SELECT * FROM LEARNING_MODULES";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				modules.add(new LearningModule(
						rs.getInt("ID"),
						rs.getString("TITLE"),
						rs.getString("CONTENT"),
						rs.getBytes("IMAGE_DATA")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return modules;
	}

	public void save(LearningModule module) {
		if (module.getId() == 0) {
			String sql = "INSERT INTO LEARNING_MODULES (TITLE, CONTENT, IMAGE_DATA) VALUES (?, ?, ?)";
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
			String sql = "UPDATE LEARNING_MODULES SET TITLE = ?, CONTENT = ?, IMAGE_DATA = ? WHERE ID = ?";
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
