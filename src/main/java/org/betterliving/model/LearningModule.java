package org.betterliving.model;

public class LearningModule implements Identifiable {
	private int id;
	private String title;
	private String contentText;
	private byte[] imageBytes;

	public LearningModule(int id, String title, String contentText, byte[] imageBytes) {
		this.id = id;
		this.title = title;
		this.contentText = contentText;
		this.imageBytes = imageBytes;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}
}
