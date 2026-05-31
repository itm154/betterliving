package org.betterliving.view;

import org.betterliving.controller.QuestionController;
import org.betterliving.controller.LearningModuleController;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {
	private final QuestionController controller;
	private final LearningModuleController moduleController;
	private final boolean isTeacher;

	public MainMenuView(QuestionController qscontroller, LearningModuleController lmcontroller, boolean isTeacher) {
		this.isTeacher = isTeacher;
		this.controller = qscontroller;
		this.moduleController = lmcontroller;

		setTitle("BetterLiving - SDG 13: Climate Action Dashboard");
		setSize(1920, 1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(4, 1, 10, 10));

		JButton viewModuleBtn = new JButton("Open SDG 13: Climate Action Learning Module");
		viewModuleBtn.setFont(new Font("Arial", Font.BOLD, 18));

		viewModuleBtn.addActionListener(e -> new LearningModuleListView(moduleController, isTeacher));
		add(viewModuleBtn);

		JButton startQuizBtn = new JButton("Start Quiz");
		startQuizBtn.setFont(new Font("Arial", Font.BOLD, 18));
		startQuizBtn.addActionListener(e -> new QuestionView(qscontroller));
		add(startQuizBtn);

		JButton listQuestionsBtn = new JButton("Question List Manager");
		listQuestionsBtn.setFont(new Font("Arial", Font.BOLD, 18));
		listQuestionsBtn.addActionListener(e -> new QuestionListView(qscontroller));

		if (isTeacher) { // only teachers can access question management page
			add(listQuestionsBtn);
		}

		setVisible(true);
	}
}
