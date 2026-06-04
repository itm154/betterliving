package org.betterliving.repository;

import org.betterliving.model.question.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionRepository implements Storable<Question> {
	private static final String DB_URL = "jdbc:derby:betterlivingDB;create=true";

	public QuestionRepository() {
		try (Connection conn = DriverManager.getConnection(DB_URL)) {
			if (!tableExists(conn, "QUESTIONS")) {
				try (Statement stmt = conn.createStatement()) {
					stmt.execute("CREATE TABLE questions (" +
							"id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
							"type VARCHAR(10), " +
							"text VARCHAR(1000), " +
							"correct_answer VARCHAR(500), " +
							"points INT, " +
							"mcq_options VARCHAR(1000), " +
							"quiz_set_id INT, " +
							"FOREIGN KEY (quiz_set_id) REFERENCES quiz_sets(id) ON DELETE CASCADE)");
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
		String sql = "INSERT INTO questions (type, text, correct_answer, points, mcq_options, quiz_set_id) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(DB_URL);
		     PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, q.getQuestionType().getCode());
			pstmt.setString(2, q.getQuestionText());
			pstmt.setString(3, q.getCorrectAnswer());
			pstmt.setInt(4, q.getPoints());

			if (q instanceof MultipleChoiceQuestion mcq) {
				pstmt.setString(5, String.join("||", mcq.getOptions()));
			} else {
				pstmt.setNull(5, Types.VARCHAR);
			}
			pstmt.setInt(6, q.getQuizSetId() == 0 ? 1 : q.getQuizSetId());

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
		String sql = "SELECT * FROM questions";
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

	public List<Question> findByQuizSetId(int quizSetId) {
		List<Question> questions = new ArrayList<>();
		String sql = "SELECT * FROM questions WHERE quiz_set_id = ?";
		try (Connection conn = DriverManager.getConnection(DB_URL);
		     PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, quizSetId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					questions.add(mapResultSetToQuestion(rs));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return questions;
	}

	public void deleteById(int id) {
		String sql = "DELETE FROM questions WHERE id = ?";
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
		String optionsRaw = rs.getString("mcq_options");
		int quizSetId = rs.getInt("quiz_set_id");

		QuestionType qType = QuestionType.fromCode(type);
		Question q = switch (qType) {
			case MC -> {
				List<String> options = (optionsRaw != null) ? Arrays.asList(optionsRaw.split("\\|\\|")) : new ArrayList<>();
				yield new MultipleChoiceQuestion(id, text, ans, points, options);
			}
			case SA -> new ShortAnswerQuestion(id, text, ans, points);
			case TF -> new TrueFalseQuestion(id, text, Boolean.parseBoolean(ans), points);
		};
		q.setQuizSetId(quizSetId);
		return q;
	}
}
