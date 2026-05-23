interface InfoViewer {
    String getPageContent(int pageNumber);
    int getTotalPages();
}

public class Information implements InfoViewer {
    private final String[] learningPages = {
        "<html><center><h3>Module 1: What is Climate Change?</h3><p>Global shifts in temperatures caused primarily by human fossil fuel use.</p></center></html>",
        "<html><center><h3>Module 2: Clean Renewable Energy</h3><p>Using solar, wind, and geothermal frameworks to drop global emissions.</p></center></html>"
    };

    @Override
    public String getPageContent(int pageNumber) {
        if (pageNumber >= 0 && pageNumber < learningPages.length) {
            return learningPages[pageNumber];
        }
        return "End of module reached.";
    }

    @Override
    public int getTotalPages() {
        return learningPages.length;
    }
}

