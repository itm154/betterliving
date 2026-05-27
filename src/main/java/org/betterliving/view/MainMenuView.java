package org.betterliving.view;

import org.betterliving.controller.QuestionController;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {
	private final QuestionController controller;

	public MainMenuView(QuestionController controller) {
		this.controller = controller;

		setTitle("BetterLiving - Main Menu");
		setSize(1920, 1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(3, 1, 10, 10));

		JButton startQuizBtn = new JButton("Start Quiz");
		startQuizBtn.setFont(new Font("Arial", Font.BOLD, 18));
		startQuizBtn.addActionListener(e -> new QuestionView(controller));

		// FUTURE NOTE: If user is not teacher dont show this button
		JButton listQuestionsBtn = new JButton("Question List");
		listQuestionsBtn.setFont(new Font("Arial", Font.BOLD, 18));
		listQuestionsBtn.addActionListener(e -> new QuestionListView(controller));

		JButton anotherButton = new JButton("Some other button");
		anotherButton.setFont(new Font("Arial", Font.BOLD, 18));

		add(startQuizBtn);
		add(listQuestionsBtn);
		add(anotherButton);

		setVisible(true);
	}
}
