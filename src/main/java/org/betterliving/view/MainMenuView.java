// Class : MainMenuView (UserDashboardView)
// Contributor : Adryana Nathalia
// Tester : Muhammad Ashrul Fahmi
// Explanataion : Main dashboard connecting all controllers (QuizSet, Question,
//              LearningModule, Scoreboard). Provides unified interface for users

package org.betterliving.view;

import org.betterliving.controller.LearningModuleController;
import org.betterliving.controller.QuestionController;
import org.betterliving.controller.QuizSetController;
import org.betterliving.controller.ScoreboardController;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {
	private final QuestionController qsController;
	private final LearningModuleController lmController;
	private final QuizSetController qSetController;
	private final ScoreboardController sbController;
	private final boolean isTeacher;

	public MainMenuView(QuestionController qsController, LearningModuleController lmController,
	                    QuizSetController qSetController, ScoreboardController sbController, boolean isTeacher) {
		this.isTeacher = isTeacher;
		this.qsController = qsController;
		this.lmController = lmController;
		this.qSetController = qSetController;
		this.sbController = sbController;

		setTitle("BetterLiving - SDG 13: Climate Action Dashboard");
		setSize(1920, 1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(0, 1, 10, 10));

		JButton viewModuleBtn = new JButton("Open SDG 13: Climate Action Learning Module");
		viewModuleBtn.setFont(new Font("Arial", Font.BOLD, 18));
		viewModuleBtn.addActionListener(e -> new LearningModuleListView(this.lmController, isTeacher));
		add(viewModuleBtn);

		JButton viewQuizSetsBtn = new JButton("Open SDG 13: Climate Action Quiz Sets");
		viewQuizSetsBtn.setFont(new Font("Arial", Font.BOLD, 18));
		viewQuizSetsBtn
				.addActionListener(e -> new QuizSetListView(qSetController, this.qsController, sbController, isTeacher));
		add(viewQuizSetsBtn);

		JButton scoreboardBtn = new JButton("View Scoreboard");
		scoreboardBtn.setFont(new Font("Arial", Font.BOLD, 18));
		scoreboardBtn.addActionListener(e -> new ScoreboardListView(sbController, isTeacher));
		add(scoreboardBtn);

		JButton switchRoleBtn = new JButton(isTeacher ? "Switch to Student Mode" : "Switch to Teacher Mode");
		switchRoleBtn.setFont(new Font("Arial", Font.BOLD, 18));
		switchRoleBtn.addActionListener(e -> {
			dispose();
			new MainMenuView(this.qsController, this.lmController, qSetController, sbController, !isTeacher);
		});
		add(switchRoleBtn);

		setVisible(true);
	}
}
