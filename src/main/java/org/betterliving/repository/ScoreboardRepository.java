package org.betterliving.repository;

import org.betterliving.model.user.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardRepository implements Storeable<Student> {
	private static final String DB_URL = "jdbc:derby:betterlivingDB;create=true";

	public ScoreboardRepository() {
		try (Connection conn = DriverManager.getConnection(DB_URL)) {
			if (!tableExists(conn, "SCOREBOARD")) {
				try (Statement stmt = conn.createStatement()) {
					stmt.execute("CREATE TABLE SCOREBOARD (" +
							"id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
							"name VARCHAR(255), " +
							"score INT)");
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

	@Override
	public List<Student> findAll() {
		List<Student> students = new ArrayList<>();
		String sql = "SELECT * FROM SCOREBOARD ORDER BY score DESC";
		try (Connection conn = DriverManager.getConnection(DB_URL);
		     Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				Student s = new Student(rs.getString("name"), rs.getInt("id"));
				s.addScore(rs.getInt("score"));
				students.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return students;
	}

	public Student findByName(String name) {
		String sql = "SELECT * FROM SCOREBOARD WHERE name = ?";
		try (Connection conn = DriverManager.getConnection(DB_URL);
		     PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					Student s = new Student(rs.getString("name"), rs.getInt("id"));
					s.addScore(rs.getInt("score"));
					return s;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void save(Student student) {
		if (student.getId() == 0) {
			String sql = "INSERT INTO SCOREBOARD (name, score) VALUES (?, ?)";
			try (Connection conn = DriverManager.getConnection(DB_URL);
			     PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				pstmt.setString(1, student.getName());
				pstmt.setInt(2, student.getScore());
				pstmt.executeUpdate();
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						student.setId(generatedKeys.getInt(1));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			String sql = "UPDATE SCOREBOARD SET name = ?, score = ? WHERE id = ?";
			try (Connection conn = DriverManager.getConnection(DB_URL);
			     PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, student.getName());
				pstmt.setInt(2, student.getScore());
				pstmt.setInt(3, student.getId());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteById(int id) {
		String sql = "DELETE FROM SCOREBOARD WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(DB_URL);
		     PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
