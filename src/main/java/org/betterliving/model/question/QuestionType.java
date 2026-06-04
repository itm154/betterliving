package org.betterliving.model.question;

public enum QuestionType {
	MC("MC"),
	TF("TF"),
	SA("SA");

	private final String code;

	QuestionType(String code) {
		this.code = code;
	}

	public static QuestionType fromCode(String code) {
		for (QuestionType type : values()) {
			if (type.code.equalsIgnoreCase(code)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown question type code: " + code);
	}

	public String getCode() {
		return code;
	}
}
