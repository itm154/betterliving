package org.betterliving.repository;

import org.betterliving.controller.QuestionController;

import java.util.Arrays;

public class DatabaseSeeder {

	public static void seed(QuestionController qsController) {
		// Preset Questions, AI generated because im NOT coming with 20 questions myself
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
	}
}
