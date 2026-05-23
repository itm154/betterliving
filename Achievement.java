interface RewardSystem {
    String getFeedback(int score, int totalQuestions);
}

public class Achievement implements RewardSystem {
    @Override
    public String getFeedback(int score, int totalQuestions) {
        double percentage = ((double) score / totalQuestions) * 100;
        
        if (percentage == 100) {
            return "Perfect! You earned the Earth Defender Badge! 🌍";
        } else if (percentage >= 50) {
            return "Good job! Keep learning about climate action! 🌱";
        } else {
            return "Don't give up! Review the modules and try again! 📚";
        }
    }
}