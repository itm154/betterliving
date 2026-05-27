package org.betterliving.repository;

import org.betterliving.model.question.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {
	private static final String DB_URL = "jdbc:derby:betterlivingDB;create=true";

	public QuestionRepository() {
		try (Connection conn = DriverManager.getConnection(DB_URL)) {
			if (!tableExists(conn, "QUESTIONS")) {
				try (Statement stmt = conn.createStatement()) {
					stmt.execute("CREATE TABLE QUESTIONS (" +
							"id INT PRIMARY KEY, " +
							"type VARCHAR(10), " +
							"text VARCHAR(1000), " +
							"correct_answer VARCHAR(500), " +
							"points INT)");
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

	public void save(Question q) {
		String sql = "INSERT INTO QUESTIONS (type, text, correct_answer, points) VALUES (?, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, q.getQuestionType());
			pstmt.setString(2, q.getQuestionText());
			pstmt.setString(3, q.getCorrectAnswer());
			pstmt.setInt(4, q.getQuestionPoints());
			pstmt.executeUpdate();

			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					q.setId(generatedKeys.getInt(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Question> findAll() {
		List<Question> questions = new ArrayList<>();
		String sql = "SELECT * FROM QUESTIONS";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				questions.add(mapResultSetToQuestion(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return questions;
	}

	public void deleteById(int id) {
		String sql = "DELETE FROM QUESTIONS WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Question mapResultSetToQuestion(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String type = rs.getString("type");
		String text = rs.getString("text");
		String ans = rs.getString("correct_answer");
		int points = rs.getInt("points");

		return switch (type) {
			case "MC" -> new MultipleChoiceQuestion(id, text, ans, points);
			case "SA" -> new ShortAnswerQuestion(id, text, ans, points);
			case "TF" -> new TrueFalseQuestion(id, text, Boolean.parseBoolean(ans), points);
			default -> throw new IllegalArgumentException("Unknown question type: " + type);
		};
	}
}
