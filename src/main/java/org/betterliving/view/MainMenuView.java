package org.betterliving.view;

import org.betterliving.controller.QuestionController;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {
	private final QuestionController controller;
	private final boolean isTeacher;

	public MainMenuView(QuestionController controller) {
		// Defaults to false (Student). Change to true here if you want to test Teacher mode!
		this(controller, false); 
	}

	public MainMenuView(QuestionController controller, boolean isTeacher) {
		this.controller = controller;
		this.isTeacher = isTeacher;

		setTitle("BetterLiving - SDG 13 Dashboard");
		setSize(1920, 1080); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setLocationRelativeTo(null); 
		setLayout(new GridLayout(4, 1, 10, 10)); 


		JButton viewModuleBtn = new JButton("Open SDG 13 Learning Module");
		viewModuleBtn.setFont(new Font("Arial", Font.BOLD, 18));
		
		viewModuleBtn.addActionListener(e -> new LearningModuleListView(isTeacher));
		add(viewModuleBtn);

		JButton startQuizBtn = new JButton("Start Quiz"); 
		startQuizBtn.setFont(new Font("Arial", Font.BOLD, 18)); 
		startQuizBtn.addActionListener(e -> new QuestionView(controller)); 
		add(startQuizBtn); 

		JButton listQuestionsBtn = new JButton("Question List Manager");
		listQuestionsBtn.setFont(new Font("Arial", Font.BOLD, 18)); 
		listQuestionsBtn.addActionListener(e -> new QuestionListView(controller));

		if (isTeacher) {//only teachers can access question management page
			add(listQuestionsBtn);
		}

		setVisible(true); 
	}
}