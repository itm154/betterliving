package org.betterliving.controller;

import org.betterliving.model.LearningModule;
import org.betterliving.repository.LearningModuleRepository;
import java.util.List;

public class LearningModuleController {
	private final LearningModuleRepository repository;

	public LearningModuleController(LearningModuleRepository repository) {
		this.repository = new LearningModuleRepository();
	}

	public List<LearningModule> getAllModules() {
		return repository.findAll();
	}

	public void createNewModule() {
		LearningModule newModule = new LearningModule(0, "New Module Title", "Enter content here...", "");
		repository.save(newModule);
	}

	public void updateModule(LearningModule module) {
		repository.save(module);
	}

	public void deleteModule(int id) {
		repository.deleteById(id);
	}
}
