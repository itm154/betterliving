import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class AppGUI extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);

    private Information infoModule = new Information(); 
    private Achievement gameEngine = new Achievement(); 
    
    private ArrayList<QuizModule> allQuizzes = new ArrayList<>();
    private QuizModule currentActiveQuiz = null; 
    
    private ArrayList<InfoPage> modulePages = new ArrayList<>();
    
    private ArrayList<User> registeredUsers = new ArrayList<>();
    private final String USER_FILE = "users.txt"; 
    private final String MODULE_FILE = "modules.txt"; 
    private final String QUIZ_FILE = "quizzes.txt"; 
    private User loggedInUser; 
    
    private int currentPageIndex = 0;
    private int currentQuizIndex = 0;
    private int score = 0;

    private JButton editInfoBtn = new JButton("Edit Info Modules");
    private JButton editQuizBtn = new JButton("Edit Quiz Bank");

    public AppGUI() {
        super("BetterLiving: SDG 13 Climate Action Module");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 700); 
        setResizable(false);
        setLocationRelativeTo(null);

        // Load all data from our local text files
        loadUserDataFromFile();
        loadModuleDataFromFile();
        loadQuizDataFromFile();

        mainContainer.add(buildLoginPanel(), "LOGIN");
        mainContainer.add(buildSignupPanel(), "SIGNUP");
        mainContainer.add(buildDashboardPanel(), "DASHBOARD");
        mainContainer.add(buildInfoPanel(), "INFO_MODULE");
        mainContainer.add(buildQuizPanel(), "QUIZ_MODULE");
        mainContainer.add(buildAchievementPanel(), "ACHIEVEMENT");
        mainContainer.add(buildInfoUpdatePanel(), "INFO_UPDATE");
        mainContainer.add(buildQuizUpdatePanel(), "QUIZ_UPDATE");

        add(mainContainer);
        cardLayout.show(mainContainer, "LOGIN"); 
    }

    // ==========================================
    // LOCAL TXT FILE LOADERS
    // ==========================================
    private void loadUserDataFromFile() {
        File file = new File(USER_FILE);
        try {
            if (!file.exists()) {
                file.createNewFile();
                saveUserToFile(new Student("1", "default_student", "123"));
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length == 4) {
                    if (data[3].equals("Lecturer")) {
                        registeredUsers.add(new Lecturer(data[0], data[1], data[2]));
                    } else {
                        registeredUsers.add(new Student(data[0], data[1], data[2]));
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void saveUserToFile(User newUser) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            bw.write(newUser.toFileFormat());
            bw.newLine();
        } catch (IOException e) { System.out.println("Error saving user: " + e.getMessage()); }
    }

    private void loadModuleDataFromFile() {
        File file = new File(MODULE_FILE);
        try {
            if (!file.exists()) {
                file.createNewFile();
                modulePages.add(new InfoPage("Welcome to the Climate Action Module! Lecturers can add new pages and images.", ""));
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 1) {
                    // Convert the safe text back into real newlines
                    String text = parts[0].replace("\\n", "\n");
                    String imgPath = parts.length == 2 ? parts[1] : "";
                    modulePages.add(new InfoPage(text, imgPath));
                }
            }
            br.close();
        } catch (IOException e) { System.out.println("Error loading modules"); }
    }

    private void loadQuizDataFromFile() {
        File file = new File(QUIZ_FILE);
        try {
            if (!file.exists()) {
                file.createNewFile();
                // Create a default fallback quiz
                QuizModule defaultQuiz = new QuizModule("Default Climate Quiz");
                defaultQuiz.addQuestion(new TFQuestion("Deforestation causes an increase in atmospheric CO2.", "True"));
                defaultQuiz.addQuestion(new MCQQuestion("What is a clean renewable energy resource?", "Coal", "Solar Energy", "Natural Gas", "Petroleum", "Solar Energy"));
                allQuizzes.add(defaultQuiz);
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;
                
                String qName = parts[0];
                String qType = parts[1];
                String qText = parts[2];

                // Find if this quiz already exists, or create a new group for it
                QuizModule targetQuiz = null;
                for (QuizModule qm : allQuizzes) {
                    if (qm.getName().equals(qName)) { targetQuiz = qm; break; }
                }
                if (targetQuiz == null) {
                    targetQuiz = new QuizModule(qName);
                    allQuizzes.add(targetQuiz);
                }

                if (qType.equals("MCQ") && parts.length == 8) {
                    targetQuiz.addQuestion(new MCQQuestion(qText, parts[3], parts[4], parts[5], parts[6], parts[7]));
                } else if (qType.equals("TF") && parts.length == 4) {
                    targetQuiz.addQuestion(new TFQuestion(qText, parts[3]));
                }
            }
            br.close();
        } catch (IOException e) { System.out.println("Error loading quizzes"); }
    }

    // ==========================================
    // LOGIN & SIGNUP PANELS
    // ==========================================
    private JPanel buildLoginPanel() {
        JPanel pane = new JPanel(new GridBagLayout());
        pane.setBackground(new Color(235, 245, 241));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;

        gbc.gridy = 0; pane.add(new JLabel("<html><center><h2>BetterLiving</h2>Log In</center></html>"), gbc);

        JTextField idField = new JTextField(15);
        idField.setBorder(BorderFactory.createTitledBorder("User ID"));
        gbc.gridy = 1; pane.add(idField, gbc);

        JPasswordField passField = new JPasswordField(15);
        passField.setBorder(BorderFactory.createTitledBorder("Password"));
        gbc.gridy = 2; pane.add(passField, gbc);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(40, 167, 69));
        loginBtn.setForeground(Color.WHITE);
        gbc.gridy = 3; pane.add(loginBtn, gbc);

        JButton goToSignupBtn = new JButton("No account? Sign up here");
        goToSignupBtn.setContentAreaFilled(false);
        goToSignupBtn.setBorderPainted(false);
        goToSignupBtn.setForeground(Color.BLUE);
        gbc.gridy = 4; pane.add(goToSignupBtn, gbc);

        JButton exitBtn = new JButton("Exit Application");
        exitBtn.setBackground(new Color(220, 53, 69));
        exitBtn.setForeground(Color.WHITE);
        gbc.gridy = 5; pane.add(exitBtn, gbc);

        loginBtn.addActionListener(e -> {
            String enteredId = idField.getText().trim();
            String enteredPass = new String(passField.getPassword()).trim();
            loggedInUser = null;

            for (User u : registeredUsers) {
                if (u.authenticate(enteredId, enteredPass)) {
                    loggedInUser = u;
                    break;
                }
            }

            if (loggedInUser != null) {
                idField.setText(""); passField.setText("");
                
                if (loggedInUser.getRole().equals("Lecturer")) {
                    editInfoBtn.setVisible(true); editQuizBtn.setVisible(true);
                } else {
                    editInfoBtn.setVisible(false); editQuizBtn.setVisible(false);
                }
                
                JOptionPane.showMessageDialog(pane, "Welcome " + loggedInUser.getRole() + " " + loggedInUser.getUsername() + "!");
                cardLayout.show(mainContainer, "DASHBOARD");
            } else {
                JOptionPane.showMessageDialog(pane, "Invalid ID or Password", "Auth Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        goToSignupBtn.addActionListener(e -> cardLayout.show(mainContainer, "SIGNUP"));
        exitBtn.addActionListener(e -> System.exit(0)); 

        return pane;
    }

    private JPanel buildSignupPanel() {
        JPanel pane = new JPanel(new GridBagLayout());
        pane.setBackground(new Color(235, 245, 241));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;

        gbc.gridy = 0; pane.add(new JLabel("<html><center><h2>BetterLiving</h2>Create Account</center></html>"), gbc);

        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Student", "Lecturer"});
        roleBox.setBorder(BorderFactory.createTitledBorder("Select Role"));
        gbc.gridy = 1; pane.add(roleBox, gbc);

        JTextField idField = new JTextField(15);
        idField.setBorder(BorderFactory.createTitledBorder("Create ID (Numbers only)"));
        gbc.gridy = 2; pane.add(idField, gbc);

        JTextField userField = new JTextField(15);
        userField.setBorder(BorderFactory.createTitledBorder("Choose Username"));
        gbc.gridy = 3; pane.add(userField, gbc);

        JPasswordField passField = new JPasswordField(15);
        passField.setBorder(BorderFactory.createTitledBorder("Choose Password"));
        gbc.gridy = 4; pane.add(passField, gbc);

        JButton signupBtn = new JButton("Create Account");
        signupBtn.setBackground(new Color(0, 123, 255));
        signupBtn.setForeground(Color.WHITE);
        gbc.gridy = 5; pane.add(signupBtn, gbc);

        JButton goToLoginBtn = new JButton("Already have an account? Log in");
        goToLoginBtn.setContentAreaFilled(false);
        goToLoginBtn.setBorderPainted(false);
        goToLoginBtn.setForeground(Color.BLUE);
        gbc.gridy = 6; pane.add(goToLoginBtn, gbc);

        signupBtn.addActionListener(e -> {
            String newRole = roleBox.getSelectedItem().toString();
            String newId = idField.getText().trim();
            String newUser = userField.getText().trim();
            String newPass = new String(passField.getPassword()).trim();

            if (newId.isEmpty() || newUser.isEmpty() || newPass.isEmpty()) {
                JOptionPane.showMessageDialog(pane, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean idExists = false;
            for (User u : registeredUsers) {
                if (u.getId().equals(newId)) { idExists = true; break; }
            }

            if (idExists) {
                JOptionPane.showMessageDialog(pane, "Error: User ID already taken!", "Primary Key Error", JOptionPane.ERROR_MESSAGE);
            } else {
                User createdUser;
                if (newRole.equals("Lecturer")) createdUser = new Lecturer(newId, newUser, newPass);
                else createdUser = new Student(newId, newUser, newPass);

                registeredUsers.add(createdUser);
                saveUserToFile(createdUser);

                JOptionPane.showMessageDialog(pane, "Account Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                idField.setText(""); userField.setText(""); passField.setText("");
                cardLayout.show(mainContainer, "LOGIN");
            }
        });

        goToLoginBtn.addActionListener(e -> cardLayout.show(mainContainer, "LOGIN"));
        return pane;
    }

    // ==========================================
    // DASHBOARD PANEL 
    // ==========================================
    private JPanel buildDashboardPanel() {
        JPanel pane = new JPanel(new BorderLayout());
        pane.setBackground(Color.WHITE);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setBackground(Color.WHITE);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setPreferredSize(new Dimension(80, 28));
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 11));
        topBar.add(logoutBtn);
        pane.add(topBar, BorderLayout.NORTH);

        JPanel centerMenu = new JPanel(new GridLayout(0, 1, 20, 20)); 
        centerMenu.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        centerMenu.setBackground(Color.WHITE);

        centerMenu.add(new JLabel("Welcome to BetterLiving!", SwingConstants.CENTER));

        JButton learnBtn = new JButton("Learning Modules");
        JButton quizBtn = new JButton("Take a Quiz");
        centerMenu.add(learnBtn);
        centerMenu.add(quizBtn);

        editInfoBtn.setBackground(new Color(255, 193, 7)); 
        editQuizBtn.setBackground(new Color(255, 193, 7)); 
        centerMenu.add(editInfoBtn);
        centerMenu.add(editQuizBtn);

        pane.add(centerMenu, BorderLayout.CENTER);

        learnBtn.addActionListener(e -> {
            if(modulePages.isEmpty()){
                 JOptionPane.showMessageDialog(pane, "No modules available yet."); return;
            }
            currentPageIndex = 0;
            updateInfoScreen();
            cardLayout.show(mainContainer, "INFO_MODULE");
        });

        quizBtn.addActionListener(e -> {
            if (allQuizzes.isEmpty()) {
                JOptionPane.showMessageDialog(pane, "No quizzes have been created yet!"); return;
            }
            
            QuizModule[] quizArray = allQuizzes.toArray(new QuizModule[0]);
            QuizModule selectedQuiz = (QuizModule) JOptionPane.showInputDialog(
                    pane, "Select a Quiz to attempt:", "Quiz Selection",
                    JOptionPane.QUESTION_MESSAGE, null, quizArray, quizArray[0]);

            if (selectedQuiz != null && !selectedQuiz.getQuestions().isEmpty()) {
                currentActiveQuiz = selectedQuiz;
                currentQuizIndex = 0; score = 0;
                updateQuizScreen();
                cardLayout.show(mainContainer, "QUIZ_MODULE");
            } else if (selectedQuiz != null) {
                JOptionPane.showMessageDialog(pane, "This quiz currently has no questions!");
            }
        });

        editInfoBtn.addActionListener(e -> cardLayout.show(mainContainer, "INFO_UPDATE"));
        editQuizBtn.addActionListener(e -> cardLayout.show(mainContainer, "QUIZ_UPDATE"));

        logoutBtn.addActionListener(e -> {
            loggedInUser = null; 
            cardLayout.show(mainContainer, "LOGIN");
        });

        return pane;
    }

    // ==========================================
    // LECTURER EDIT PANELS (WITH FILE SAVING)
    // ==========================================
    private JPanel buildInfoUpdatePanel() {
        JPanel pane = new JPanel(new BorderLayout(10, 10));
        pane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pane.setBackground(Color.WHITE);
        
        pane.add(new JLabel("<html><h2>Add New Information Module</h2></html>", SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBackground(Color.WHITE);

        JTextArea infoArea = new JTextArea(8, 20);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setBorder(BorderFactory.createTitledBorder("Type new learning content here:"));
        centerPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);

        JPanel imageUploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imageUploadPanel.setBackground(Color.WHITE);
        JButton attachImgBtn = new JButton("Attach Image (Optional)");
        JLabel selectedImgLabel = new JLabel("No image selected");
        
        final String[] currentImagePath = {""}; 

        attachImgBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(pane);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                currentImagePath[0] = selectedFile.getAbsolutePath();
                selectedImgLabel.setText(selectedFile.getName());
            }
        });

        imageUploadPanel.add(attachImgBtn);
        imageUploadPanel.add(selectedImgLabel);
        centerPanel.add(imageUploadPanel, BorderLayout.SOUTH);

        pane.add(centerPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(Color.WHITE);
        JButton backBtn = new JButton("Back to Dashboard");
        JButton saveBtn = new JButton("Save Content to File");
        saveBtn.setBackground(new Color(40, 167, 69));
        saveBtn.setForeground(Color.WHITE);

        saveBtn.addActionListener(e -> {
            if(!infoArea.getText().trim().isEmpty()) {
                // Instantly update the array
                modulePages.add(new InfoPage(infoArea.getText(), currentImagePath[0]));
                
                // --- SAVE TO MODULES.TXT ---
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(MODULE_FILE, true))) {
                    // Turn invisible return/enter spaces into the \n tag
                    String safeText = infoArea.getText().replace("\n", "\\n");
                    bw.write(safeText + "|" + currentImagePath[0]);
                    bw.newLine();
                    JOptionPane.showMessageDialog(pane, "Saved to modules.txt successfully!");
                } catch (IOException ex) { 
                    JOptionPane.showMessageDialog(pane, "File Write Error!");
                }
                
                infoArea.setText("");
                selectedImgLabel.setText("No image selected");
                currentImagePath[0] = ""; 
            } else {
                JOptionPane.showMessageDialog(pane, "Content cannot be empty!");
            }
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainContainer, "DASHBOARD"));

        bottom.add(backBtn);
        bottom.add(saveBtn);
        pane.add(bottom, BorderLayout.SOUTH);
        return pane;
    }

    private QuizModule quizBeingEdited = null; 

    private JPanel buildQuizUpdatePanel() {
        JPanel pane = new JPanel(new BorderLayout(10, 10));
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pane.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createTitledBorder("Step 1: Name Your Quiz (If existing, it adds to it)"));
        JTextField quizNameField = new JTextField(12);
        JButton createQuizBtn = new JButton("Load / Create Quiz");
        topPanel.add(new JLabel("Quiz Name:"));
        topPanel.add(quizNameField);
        topPanel.add(createQuizBtn);

        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createTitledBorder("Step 2: Add Questions"));
        
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.setBackground(Color.WHITE);
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Multiple Choice (MCQ)", "True / False (TF)"});
        typePanel.add(new JLabel("Select Format:"));
        typePanel.add(typeBox);
        formContainer.add(typePanel);

        CardLayout qCards = new CardLayout();
        JPanel qCardsPanel = new JPanel(qCards);
        
        JPanel tfCard = new JPanel(new GridLayout(2, 1, 5, 5));
        tfCard.setBackground(Color.WHITE);
        JTextField tfQText = new JTextField();
        tfCard.add(new JLabel("Question Text:")); tfCard.add(tfQText);
        JComboBox<String> tfAnsBox = new JComboBox<>(new String[]{"True", "False"});
        JPanel tfAnsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tfAnsPanel.setBackground(Color.WHITE);
        tfAnsPanel.add(new JLabel("Correct Answer:")); tfAnsPanel.add(tfAnsBox);
        tfCard.add(tfAnsPanel);

        JPanel mcqCard = new JPanel(new GridLayout(6, 2, 2, 2));
        mcqCard.setBackground(Color.WHITE);
        JTextField mcqQText = new JTextField();
        JTextField optA = new JTextField(); JTextField optB = new JTextField();
        JTextField optC = new JTextField(); JTextField optD = new JTextField();
        JComboBox<String> mcqAnsBox = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        
        mcqCard.add(new JLabel("Question Text:")); mcqCard.add(mcqQText);
        mcqCard.add(new JLabel("Option A:")); mcqCard.add(optA);
        mcqCard.add(new JLabel("Option B:")); mcqCard.add(optB);
        mcqCard.add(new JLabel("Option C:")); mcqCard.add(optC);
        mcqCard.add(new JLabel("Option D:")); mcqCard.add(optD);
        mcqCard.add(new JLabel("Correct Option:")); mcqCard.add(mcqAnsBox);

        qCardsPanel.add(mcqCard, "MCQ");
        qCardsPanel.add(tfCard, "TF");
        
        typeBox.addActionListener(e -> {
            if (typeBox.getSelectedIndex() == 0) qCards.show(qCardsPanel, "MCQ");
            else qCards.show(qCardsPanel, "TF");
        });

        formContainer.add(qCardsPanel);

        JPanel addBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addBtnPanel.setBackground(Color.WHITE);
        JButton addQBtn = new JButton("Save Question to File");
        addQBtn.setBackground(new Color(0, 123, 255));
        addQBtn.setForeground(Color.WHITE);
        JLabel countLabel = new JLabel("Questions Added: 0");
        addBtnPanel.add(addQBtn);
        addBtnPanel.add(countLabel);
        formContainer.add(addBtnPanel);

        setPanelEnabled(formContainer, false);

        createQuizBtn.addActionListener(e -> {
            String qName = quizNameField.getText().trim();
            if (qName.isEmpty()) {
                JOptionPane.showMessageDialog(pane, "Please enter a quiz name!"); return;
            }
            
            // Look to see if quiz exists so we can add to it, else create new
            quizBeingEdited = null;
            for (QuizModule qm : allQuizzes) {
                if (qm.getName().equalsIgnoreCase(qName)) { quizBeingEdited = qm; break; }
            }
            if (quizBeingEdited == null) {
                quizBeingEdited = new QuizModule(qName);
                allQuizzes.add(quizBeingEdited); 
                JOptionPane.showMessageDialog(pane, "New Quiz Created!");
            } else {
                JOptionPane.showMessageDialog(pane, "Quiz Found! You are now adding to it.");
            }
            
            setPanelEnabled(formContainer, true);
            quizNameField.setEnabled(false);
            createQuizBtn.setEnabled(false);
            countLabel.setText("Total Questions: " + quizBeingEdited.getQuestions().size());
        });

        addQBtn.addActionListener(e -> {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(QUIZ_FILE, true))) {
                if (typeBox.getSelectedIndex() == 0) { // MCQ
                    String qT = mcqQText.getText().trim();
                    String a = optA.getText().trim(); String b = optB.getText().trim();
                    String c = optC.getText().trim(); String d = optD.getText().trim();
                    if (qT.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()) {
                        JOptionPane.showMessageDialog(pane, "Fill in all MCQ fields!"); return;
                    }
                    String correctStr = "";
                    switch (mcqAnsBox.getSelectedIndex()) {
                        case 0: correctStr = a; break; case 1: correctStr = b; break;
                        case 2: correctStr = c; break; case 3: correctStr = d; break;
                    }
                    
                    quizBeingEdited.addQuestion(new MCQQuestion(qT, a, b, c, d, correctStr));
                    
                    // --- SAVE TO QUIZZES.TXT ---
                    bw.write(quizBeingEdited.getName() + "|MCQ|" + qT + "|" + a + "|" + b + "|" + c + "|" + d + "|" + correctStr);
                    
                    mcqQText.setText(""); optA.setText(""); optB.setText(""); optC.setText(""); optD.setText("");
                } else { // TF
                    String qT = tfQText.getText().trim();
                    if (qT.isEmpty()) {
                        JOptionPane.showMessageDialog(pane, "Enter Question Text!"); return;
                    }
                    String ans = tfAnsBox.getSelectedItem().toString();
                    
                    quizBeingEdited.addQuestion(new TFQuestion(qT, ans));
                    
                    // --- SAVE TO QUIZZES.TXT ---
                    bw.write(quizBeingEdited.getName() + "|TF|" + qT + "|" + ans);
                    
                    tfQText.setText("");
                }
                bw.newLine();
                countLabel.setText("Total Questions: " + quizBeingEdited.getQuestions().size());
                JOptionPane.showMessageDialog(pane, "Saved successfully to quizzes.txt!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(pane, "File Write Error!");
            }
        });

        pane.add(topPanel, BorderLayout.NORTH);
        pane.add(formContainer, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(Color.WHITE);
        JButton backBtn = new JButton("Save and Exit to Dashboard");
        backBtn.addActionListener(e -> {
            quizNameField.setEnabled(true);
            createQuizBtn.setEnabled(true);
            quizNameField.setText("");
            quizBeingEdited = null;
            setPanelEnabled(formContainer, false);
            countLabel.setText("Questions Added: 0");
            cardLayout.show(mainContainer, "DASHBOARD");
        });
        bottom.add(backBtn);
        pane.add(bottom, BorderLayout.SOUTH);

        return pane;
    }

    private void setPanelEnabled(JPanel panel, Boolean isEnabled) {
        panel.setEnabled(isEnabled);
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) setPanelEnabled((JPanel) component, isEnabled);
            component.setEnabled(isEnabled);
        }
    }

    private JPanel buildAchievementPanel() {
        JPanel pane = new JPanel(new BorderLayout());
        pane.setBackground(Color.WHITE);
        pane.add(new JLabel("<html><center><h2>Achievements</h2><p>Coming Soon!</p></center></html>", SwingConstants.CENTER), BorderLayout.CENTER);
        
        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        JButton backBtn = new JButton("Back to Dashboard");
        backBtn.addActionListener(e -> cardLayout.show(mainContainer, "DASHBOARD"));
        bottom.add(backBtn);
        pane.add(bottom, BorderLayout.SOUTH);
        return pane;
    }

    // ==========================================
    // LEARNING & QUIZ VIEW PANELS
    // ==========================================
    private JLabel infoImageLabel = new JLabel("", SwingConstants.CENTER);
    private JLabel infoContentLabel = new JLabel("", SwingConstants.CENTER);
    private JButton nextInfoBtn = new JButton("Next");

    private JPanel buildInfoPanel() {
        JPanel pane = new JPanel(new BorderLayout(15, 15));
        pane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pane.setBackground(Color.WHITE);

        JPanel contentStack = new JPanel(new BorderLayout(10, 10));
        contentStack.setBackground(Color.WHITE);
        
        infoImageLabel.setPreferredSize(new Dimension(300, 200)); 
        contentStack.add(infoImageLabel, BorderLayout.NORTH);
        contentStack.add(infoContentLabel, BorderLayout.CENTER);

        pane.add(contentStack, BorderLayout.CENTER);

        JPanel navPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton backBtn = new JButton("Menu");
        navPanel.add(backBtn);
        navPanel.add(nextInfoBtn);
        pane.add(navPanel, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> cardLayout.show(mainContainer, "DASHBOARD"));
        
        nextInfoBtn.addActionListener(e -> {
            currentPageIndex++;
            if (currentPageIndex < modulePages.size()) updateInfoScreen();
            else cardLayout.show(mainContainer, "DASHBOARD");
        });

        return pane;
    }

    private void updateInfoScreen() {
        if(modulePages.isEmpty()) return;
        
        InfoPage currentPage = modulePages.get(currentPageIndex);
        
        infoContentLabel.setText("<html><center>" + currentPage.getTextContent() + "</center></html>");
        
        if (currentPage.getImagePath() != null && !currentPage.getImagePath().isEmpty()) {
            try {
                ImageIcon originalIcon = new ImageIcon(currentPage.getImagePath());
                Image scaledImg = originalIcon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
                infoImageLabel.setIcon(new ImageIcon(scaledImg));
                infoImageLabel.setText("");
            } catch (Exception ex) {
                infoImageLabel.setIcon(null);
                infoImageLabel.setText("[Image missing or broken]");
            }
        } else {
            infoImageLabel.setIcon(null); 
            infoImageLabel.setText("");
        }

        if (currentPageIndex == modulePages.size() - 1) nextInfoBtn.setText("Finish");
        else nextInfoBtn.setText("Next");
    }

    private JLabel quizQuestionLabel = new JLabel("", SwingConstants.CENTER);
    private JPanel optionsPanel = new JPanel(new FlowLayout());

    private JPanel buildQuizPanel() {
        JPanel pane = new JPanel(new BorderLayout(15, 15));
        pane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pane.setBackground(Color.WHITE);

        pane.add(quizQuestionLabel, BorderLayout.NORTH);
        pane.add(optionsPanel, BorderLayout.CENTER);

        return pane;
    }

    private void updateQuizScreen() {
        optionsPanel.removeAll();

        if (currentActiveQuiz == null || currentActiveQuiz.getQuestions().isEmpty()) return;

        if (currentQuizIndex >= currentActiveQuiz.getQuestions().size()) {
            String feedback = gameEngine.getFeedback(score, currentActiveQuiz.getQuestions().size());
            quizQuestionLabel.setText("<html><center>Quiz Finished!<br>Score: " + score + "<br>" + feedback + "</center></html>");
            
            JButton backBtn = new JButton("Back to Menu");
            backBtn.addActionListener(e -> cardLayout.show(mainContainer, "DASHBOARD"));
            optionsPanel.add(backBtn);
        } else {
            Question currentQ = currentActiveQuiz.getQuestions().get(currentQuizIndex);
            quizQuestionLabel.setText("<html><center>" + currentQ.getQuestionText() + "</center></html>");

            if (currentQ instanceof TFQuestion) {
                JButton trueBtn = new JButton("True"); JButton falseBtn = new JButton("False");
                trueBtn.addActionListener(e -> handleAnswerSubmit("True"));
                falseBtn.addActionListener(e -> handleAnswerSubmit("False"));
                optionsPanel.add(trueBtn); optionsPanel.add(falseBtn);
            } else if (currentQ instanceof MCQQuestion) {
                MCQQuestion mcq = (MCQQuestion) currentQ;
                for (String option : mcq.getOptions()) {
                    JButton optBtn = new JButton(option);
                    optBtn.addActionListener(e -> handleAnswerSubmit(option));
                    optionsPanel.add(optBtn);
                }
            }
        }
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void handleAnswerSubmit(String selection) {
        if (currentActiveQuiz.getQuestions().get(currentQuizIndex).evaluateAnswer(selection)) score++;
        currentQuizIndex++;
        updateQuizScreen();
    }
}

// ==========================================
// DATA STRUCTURES
// ==========================================
class QuizModule {
    private String quizName;
    private ArrayList<Question> questions;

    public QuizModule(String quizName) {
        this.quizName = quizName;
        this.questions = new ArrayList<>();
    }
    public String getName() { return quizName; }
    public void addQuestion(Question q) { questions.add(q); }
    public ArrayList<Question> getQuestions() { return questions; }

    @Override
    public String toString() {
        return quizName + " (" + questions.size() + " Questions)"; 
    }
}

class InfoPage {
    private String textContent;
    private String imagePath; 

    public InfoPage(String textContent, String imagePath) {
        this.textContent = textContent;
        this.imagePath = imagePath;
    }
    public String getTextContent() { return textContent; }
    public String getImagePath() { return imagePath; }
}