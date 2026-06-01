package org.betterliving.repository;

import org.betterliving.model.QuizSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizSetRepository {
	private static final String DB_URL = "jdbc:derby:betterlivingDB;create=true";

	public QuizSetRepository() {
		try (Connection conn = DriverManager.getConnection(DB_URL)) {
			if (!tableExists(conn, "QUIZ_SETS")) {
				try (Statement stmt = conn.createStatement()) {
					stmt.execute("CREATE TABLE QUIZ_SETS (" +
							"id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
							"name VARCHAR(255))");
				}
			}

			// Seed the preset quiz set if empty
			if (findAll().isEmpty()) {
				save(new QuizSet(0, "Preset SDG 13 Quiz"));
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

	public List<QuizSet> findAll() {
		List<QuizSet> sets = new ArrayList<>();
		String sql = "SELECT * FROM QUIZ_SETS";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				sets.add(new QuizSet(rs.getInt("id"), rs.getString("name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sets;
	}

	public QuizSet findById(int id) {
		String sql = "SELECT * FROM QUIZ_SETS WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new QuizSet(rs.getInt("id"), rs.getString("name"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void save(QuizSet set) {
		// The passed ID isnt actually used in the database, if id=0 it just means the
		// set is not in the database yet
		// The database assigns the ID automatically
		// If ID is the same, update
		if (set.getId() == 0) {
			String sql = "INSERT INTO QUIZ_SETS (name) VALUES (?)";
			try (Connection conn = DriverManager.getConnection(DB_URL);
					PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				pstmt.setString(1, set.getName());
				pstmt.executeUpdate();
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						set.setId(generatedKeys.getInt(1));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			String sql = "UPDATE QUIZ_SETS SET name = ? WHERE id = ?";
			try (Connection conn = DriverManager.getConnection(DB_URL);
					PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, set.getName());
				pstmt.setInt(2, set.getId());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteById(int id) {
		String sqlSet = "DELETE FROM QUIZ_SETS WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sqlSet)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
