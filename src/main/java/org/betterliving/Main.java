package org.betterliving;

import org.betterliving.controller.LearningModuleController;
import org.betterliving.controller.QuestionController;
import org.betterliving.controller.QuizSetController;
import org.betterliving.controller.ScoreboardController;
import org.betterliving.repository.*;
import org.betterliving.view.MainMenuView;

import javax.swing.*;

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
		DatabaseSeeder.seed(qsController, lmController);

		// Launch GUI
		SwingUtilities.invokeLater(() -> new MainMenuView(qsController, lmController, qSetController, sbController, false));
	}
}
