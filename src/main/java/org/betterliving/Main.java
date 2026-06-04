package org.betterliving;

import org.betterliving.controller.LearningModuleController;
import org.betterliving.controller.QuestionController;
import org.betterliving.controller.QuizSetController;
import org.betterliving.controller.ScoreboardController;
import org.betterliving.repository.LearningModuleRepository;
import org.betterliving.repository.QuestionRepository;
import org.betterliving.repository.QuizSetRepository;
import org.betterliving.repository.ScoreboardRepository;
import org.betterliving.view.MainMenuView;

import javax.swing.*;
import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		// Initialize backend
		QuizSetRepository qSetRepository = new QuizSetRepository();
		QuizSetController qSetController = new QuizSetController(qSetRepository);

		QuestionRepository qsRepository = new QuestionRepository();
		QuestionController qsController = new QuestionController(qsRepository);

		LearningModuleRepository lmRepository = new LearningModuleRepository();
		LearningModuleController lmController = new LearningModuleController(lmRepository);

		ScoreboardRepository sbRepository = new ScoreboardRepository();
		ScoreboardController sbController = new ScoreboardController(sbRepository);

		// Add preset datas
		org.betterliving.repository.DatabaseSeeder.seed(qsController);

		// Launch GUI
		SwingUtilities.invokeLater(() -> new MainMenuView(qsController, lmController, qSetController, sbController, true));
	}
}
