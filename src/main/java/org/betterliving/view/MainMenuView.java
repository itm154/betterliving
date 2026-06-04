package org.betterliving.view;

import org.betterliving.controller.LearningModuleController;
import org.betterliving.controller.QuestionController;
import org.betterliving.controller.QuizSetController;
import org.betterliving.controller.ScoreboardController;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {
	private final QuestionController controller;
	private final LearningModuleController moduleController;
	private final QuizSetController quizSetController;
	private final ScoreboardController scoreboardController;
	private final boolean isTeacher;

	public MainMenuView(QuestionController qscontroller, LearningModuleController lmcontroller, boolean isTeacher) {
		this(qscontroller, lmcontroller,
				new org.betterliving.controller.QuizSetController(new org.betterliving.repository.QuizSetRepository()),
				new org.betterliving.controller.ScoreboardController(new org.betterliving.repository.ScoreboardRepository()),
				isTeacher);
	}

	public MainMenuView(QuestionController qscontroller, LearningModuleController lmcontroller,
	                    QuizSetController quizSetController, boolean isTeacher) {
		this(qscontroller, lmcontroller, quizSetController,
				new org.betterliving.controller.ScoreboardController(new org.betterliving.repository.ScoreboardRepository()),
				isTeacher);
	}

	public MainMenuView(QuestionController qscontroller, LearningModuleController lmcontroller,
	                    QuizSetController quizSetController, ScoreboardController scoreboardController, boolean isTeacher) {
		this.isTeacher = isTeacher;
		this.controller = qscontroller;
		this.moduleController = lmcontroller;
		this.quizSetController = quizSetController;
		this.scoreboardController = scoreboardController;

		setTitle("BetterLiving - SDG 13: Climate Action Dashboard");
		setSize(1920, 1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(0, 1, 10, 10));

		JButton viewModuleBtn = new JButton("Open SDG 13: Climate Action Learning Module");
		viewModuleBtn.setFont(new Font("Arial", Font.BOLD, 18));
		viewModuleBtn.addActionListener(e -> new LearningModuleListView(moduleController, isTeacher));
		add(viewModuleBtn);

		JButton viewQuizSetsBtn = new JButton("Open SDG 13: Climate Action Quiz Sets");
		viewQuizSetsBtn.setFont(new Font("Arial", Font.BOLD, 18));
		viewQuizSetsBtn
				.addActionListener(e -> new QuizSetListView(quizSetController, controller, scoreboardController, isTeacher));
		add(viewQuizSetsBtn);

		JButton scoreboardBtn = new JButton("View Scoreboard");
		scoreboardBtn.setFont(new Font("Arial", Font.BOLD, 18));
		scoreboardBtn.addActionListener(e -> new ScoreboardListView(scoreboardController, isTeacher));
		add(scoreboardBtn);

		JButton switchRoleBtn = new JButton(isTeacher ? "Switch to Student Mode" : "Switch to Teacher Mode");
		switchRoleBtn.setFont(new Font("Arial", Font.BOLD, 18));
		switchRoleBtn.addActionListener(e -> {
			dispose();
			new MainMenuView(controller, moduleController, quizSetController, scoreboardController, !isTeacher);
		});
		add(switchRoleBtn);

		setVisible(true);
	}
}
