package org.betterliving.repository;

import org.betterliving.controller.LearningModuleController;
import org.betterliving.controller.QuestionController;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class DatabaseSeeder {

	public static void seed(QuestionController qsController, LearningModuleController lmController) {
		// Preset Questions, mostly AI generated because i CANNOT come up with 20 questions myself
		if (qsController.getQuestionsForQuizSet(1).isEmpty()) {
			// --- TRUE / FALSE QUESTIONS (5 Points) ---
			qsController.addTrueFalse("Is carbon dioxide the only greenhouse gas driving climate change?", false, 5, 1);
			qsController.addTrueFalse("The Paris Agreement aims to limit global warming to well below 2 degrees Celsius.",
					true, 5, 1);
			qsController.addTrueFalse(
					"Deforestation contributes to climate change by reducing the Earth's capacity to absorb CO2.", true, 5, 1);
			qsController.addTrueFalse(
					"Renewable energy sources like solar and wind emit as much CO2 as coal when generating electricity.", false,
					5, 1);
			qsController.addTrueFalse("The greenhouse effect is a naturally occurring process that keeps Earth habitable.",
					true, 5, 1);
			qsController.addTrueFalse("Glaciers and ice sheets melting causes sea levels to fall.", false, 5, 1);

			// --- SHORT ANSWER QUESTIONS (10 Points) ---
			qsController.addShortAnswer("What gas is released into the atmosphere primarily by burning fossil fuels?",
					"Carbon Dioxide", 10, 1);
			qsController.addShortAnswer("What international treaty on climate change was adopted in 2015?", "Paris Agreement",
					10, 1);
			qsController.addShortAnswer(
					"What is the term for the long-term shift in global weather patterns and temperatures?", "Climate Change", 10,
					1);
			qsController.addShortAnswer("What is the main unit used to measure carbon footprints?", "Metric Tons", 10, 1);
			qsController.addShortAnswer("What acronym represents the UN panel that assesses climate change science?", "IPCC",
					10, 1);
			qsController.addShortAnswer(
					"What term describes achieving a balance between emitted and absorbed greenhouse gases?", "Net Zero", 10, 1);

			// --- MULTIPLE CHOICE QUESTIONS (15 Points) ---
			qsController.addMultipleChoice(
					"Which sector is globally responsible for the highest percentage of greenhouse gas emissions?", "Energy", 15,
					Arrays.asList("Agriculture", "Transportation", "Energy", "Manufacturing"), 1);

			qsController.addMultipleChoice("What is the primary goal of SDG 13?", "Climate Action", 15,
					Arrays.asList("Clean Water", "Climate Action", "Affordable Energy", "Life Below Water"), 1);

			qsController.addMultipleChoice("Which of the following is considered a major 'carbon sink'?", "Oceans", 15,
					Arrays.asList("Oceans", "Deserts", "Cities", "Atmosphere"), 1);

			qsController.addMultipleChoice(
					"What phenomenon causes the ocean to become more acidic as it absorbs excess CO2?", "Ocean Acidification", 15,
					Arrays.asList("Coral Bleaching", "Ocean Acidification", "Thermal Expansion", "Eutrophication"), 1);

			qsController.addMultipleChoice("Which of these is a renewable energy source?", "Geothermal", 15,
					Arrays.asList("Natural Gas", "Coal", "Nuclear", "Geothermal"), 1);

			qsController.addMultipleChoice("What does the 'cap and trade' system look to regulate?", "Carbon Emissions", 15,
					Arrays.asList("Water Usage", "Deforestation", "Carbon Emissions", "Plastic Waste"), 1);

			qsController.addMultipleChoice("Which country historically has emitted the most cumulative CO2?", "United States",
					15,
					Arrays.asList("China", "United States", "India", "Germany"), 1);

			qsController.addMultipleChoice(
					"What is the term for adapting to the current and future impacts of climate change?", "Climate Adaptation",
					15,
					Arrays.asList("Climate Mitigation", "Climate Adaptation", "Carbon Offsetting", "Geoengineering"), 1);
		}

		// Preset Learning Modules
		if (lmController.getAllModules().isEmpty()) {
			// Copy this block of code to add more modules
			// Add your images to /src/main/resources directory, reference it using its file name
			lmController.addModule(
					"Introduction to SDG 13: Climate Action",
					"Sustainable Development Goal 13 (SDG 13) calls for urgent action to combat climate change and its impacts. Climate change is a global challenge that disrupts national economies and affects lives, costing people, communities, and countries dearly today and even more tomorrow.",
					loadImage("intro.png")
			);

			lmController.addModule(
					"Impacts of Climate Change",
					"Climate change leads to rising sea levels, more extreme weather events, and disruptions to ecosystems. It threatens food security, water supply, and human health, particularly in vulnerable communities.",
					loadImage("second.png")
			);

			lmController.addModule(
					"What can be done?",
					"To combat climate change, we can reduce greenhouse gas emissions by transitioning to renewable energy, improving energy efficiency, and adopting sustainable practices. Additionally, we can support policies that promote climate action and invest in climate adaptation measures.",
					loadImage("third.png")
			);

			lmController.addModule(
					"Who is responsible?",
					"Everyone has a role to play in addressing climate change. Governments, businesses, and individuals must work together to implement solutions and create a sustainable future for all.",
					loadImage("fourth.png")
			);

			lmController.addModule(
					"How are humans affected?",
					"Climate change affects human health, livelihoods, and security. It can lead to increased heat-related illnesses, food and water scarcity, displacement due to extreme weather events, and heightened risks of conflict over resources.",
					loadImage("fifth.png")
			);

			lmController.addModule(
					"How about animals?",
					"Climate change also has significant impacts on wildlife and ecosystems. Species are facing habitat loss, changing migration patterns, and increased vulnerability to diseases and predators.",
					loadImage("sixth.png")
			);

			lmController.addModule(
					"How about the Earth?",
					"Climate change is causing widespread environmental changes, including melting glaciers, rising sea levels, and shifts in weather patterns. These changes threaten biodiversity and the overall health of our planet.",
					loadImage("seventh.png")
			);

			lmController.addModule(
					"Why should we care?",
					"Addressing climate change is crucial for the well-being of current and future generations. It is a moral imperative to protect our planet and ensure a sustainable future for all living beings.",
					loadImage("eight.png")
			);

			lmController.addModule(
					"Moral of the story?",
					"The moral of the story is that we all have a responsibility to take action against climate change for the sake of our planet and future generations.",
					loadImage("ninth.png")
			);

			lmController.addModule(
					"We are the World",
					"We are the world, we are the children, we are the ones who make a brighter day, so let's start giving. There's a choice we're making, we're saving our own lives. It's true we'll make a better day, just you and me.",
					loadImage("tenth.png")
			);
		}
	}

	private static byte[] loadImage(String filename) {
		try {
			Path imagePath = Paths.get("src/main/resources/images/" + filename);
			if (Files.exists(imagePath)) {
				return Files.readAllBytes(imagePath);
			} else {
				try (InputStream is = DatabaseSeeder.class.getResourceAsStream("/images/" + filename)) {
					if (is != null) {
						return is.readAllBytes();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

