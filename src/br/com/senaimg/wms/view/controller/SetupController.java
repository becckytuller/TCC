/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senaimg.wms.view.controller;

import br.com.senaimg.wms.language.Lang;
import br.com.senaimg.wms.language.Language;
import br.com.senaimg.wms.model.sistema.ImageFile;
import br.com.senaimg.wms.model.sistema.Profile;
import br.com.senaimg.wms.model.sistema.SecQuestion;
import br.com.senaimg.wms.model.sistema.Settings;
import br.com.senaimg.wms.model.sistema.User;
import br.com.senaimg.wms.model.sistema.enums.CssTheme;
import br.com.senaimg.wms.model.sistema.enums.Gender;
import br.com.senaimg.wms.model.sistema.enums.ImageCategory;
import br.com.senaimg.wms.model.sistema.enums.UserStatus;
import br.com.senaimg.wms.util.DateUtil;
import br.com.senaimg.wms.util.Encryption;
import br.com.senaimg.wms.util.SystemImageUtil;
import br.com.senaimg.wms.util.ValidateFieldUtil;
import br.com.senaimg.wms.view.layout.AlertCustom;
import br.com.senaimg.wms.view.layout.Wizard;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author ÁlefeLucas
 */
public class SetupController implements Initializable {

    @FXML
    private StackPane rootPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private StackPane titlePane;
    @FXML
    private Label titleLabel;
    @FXML
    private HBox footPane;
    @FXML
    private Button buttonBack;
    @FXML
    private Button buttonNext;
    @FXML
    private Button buttonDone;
    @FXML
    private Button buttonCancel;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tab1;
    @FXML
    private Tab tab2;
    @FXML
    private Tab tab3;
    @FXML
    private VBox centerPane;
    @FXML
    private Label languageLabel;
    @FXML
    private RadioButton ptButton;
    @FXML
    private ToggleGroup language;
    @FXML
    private RadioButton enButton;
    @FXML
    private Label measurementLabel;
    @FXML
    private Label themeLabel;
    @FXML
    private RadioButton darkButton;
    @FXML
    private ToggleGroup theme;
    @FXML
    private RadioButton whiteButton;
    @FXML
    private BorderPane borderPane1;
    @FXML
    private GridPane formGridPane;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label birthDateLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label passwordConfirmLabel;
    @FXML
    private Label secQuestionLabel;
    @FXML
    private Label secAnswerLabel;
    @FXML
    private TextField nameTf;
    @FXML
    private Tooltip nameToolTip;
    @FXML
    private TextField emailTf;
    @FXML
    private Tooltip emailToolTip;
    @FXML
    private TextField phoneTf;
    @FXML
    private Tooltip phoneToolTip;
    @FXML
    private DatePicker birthdateDp;
    @FXML
    private Tooltip dateToolTip;
    @FXML
    private ComboBox<Gender> genderCb;
    @FXML
    private TextField usernameTf;
    @FXML
    private Tooltip usernameToolTip;
    @FXML
    private PasswordField passwordPf;
    @FXML
    private PasswordField passwordConfirmPf;
    @FXML
    private Tooltip passwordToolTip;
    @FXML
    private TextField secQuestionPf;
    @FXML
    private TextField secAnswerPf;
    @FXML
    private Tooltip secAnswerToolTip;
    @FXML
    private VBox rightPane;
    @FXML
    private Label profilePicLabel;
    @FXML
    private Circle circleImage;
    @FXML
    private Button selectImageButton;
    @FXML
    private Text adviceText;
    @FXML
    private Label doneLabel;
    private ImageFile imageUser;
    private List<User> users = User.list();
    private Wizard wizard;
    private User newUser;

    private String themeStr = "stylesheet";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        wizard = new Wizard(buttonNext, buttonBack, buttonDone, buttonCancel, tabPane, tab1, tab2, tab3);
        setTexts();

        cleanProfileImage();
        loadUsers();
        setGenders();
        setDatePicker();
        setFieldsListeners();
        setTabListeners();
        wizard.stepDone();

        setLanguageListener();
        setThemeListener();
    }

    private void setLanguageListener() {
        language.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == ptButton) {
                Lang.loadLanguage(Language.PTBR);
            } else {
                Lang.loadLanguage(Language.ENUS);
            }

            setTexts();
        });
    }

    private void setTabListeners() {
        ObservableList<Tab> tabs = tabPane.getTabs();

        tab1.selectedProperty().addListener((observable, oldValue, newValue) -> {
            wizard.stepDone();
        });
        tab2.selectedProperty().addListener((observable, oldValue, newValue) -> {
            wizard.stepDone();
        });

    }

    private void setTexts() {
        nameLabel.setText(Lang.get(nameLabel.getText()));
        emailLabel.setText(Lang.get(emailLabel.getText()));
        phoneLabel.setText(Lang.get(phoneLabel.getText()));
        birthDateLabel.setText(Lang.get(birthDateLabel.getText()));
        genderLabel.setText(Lang.get(genderLabel.getText()));
        usernameLabel.setText(Lang.get(usernameLabel.getText()));
        passwordLabel.setText(Lang.get(passwordLabel.getText()));
        passwordConfirmLabel.setText(Lang.get(passwordConfirmLabel.getText()));
        secQuestionLabel.setText(Lang.get(secQuestionLabel.getText()));
        secAnswerLabel.setText(Lang.get(secAnswerLabel.getText()));
        selectImageButton.setText(Lang.get(selectImageButton.getText()));
        profilePicLabel.setText(Lang.get(profilePicLabel.getText()));
        adviceText.setText(Lang.get(adviceText.getText()));
        titleLabel.setText(Lang.get(titleLabel.getText()));
        secAnswerPf.setPromptText(Lang.get(secAnswerPf.getPromptText()));
        buttonBack.setText(Lang.get(buttonBack.getText()));
        buttonCancel.setText(Lang.get(buttonCancel.getText()));
        buttonDone.setText(Lang.get(buttonDone.getText()));
        buttonNext.setText(Lang.get(buttonNext.getText()));
        ObservableList<Tab> tabs = tabPane.getTabs();
        for (Tab tab : tabs) {
            tab.setText(Lang.get(tab.getText()));
        }
        languageLabel.setText(Lang.get(languageLabel.getText()));
        themeLabel.setText(Lang.get(themeLabel.getText()));
        ptButton.setText(Lang.get(ptButton.getText()));
        enButton.setText(Lang.get(enButton.getText()));
        darkButton.setText(Lang.get(darkButton.getText()));
        whiteButton.setText(Lang.get(whiteButton.getText()));
        doneLabel.setText(Lang.get(doneLabel.getText()));
        
    }

    private void setFieldsListeners() {
        birthdateDp.valueProperty().addListener((o, oldV, newV) -> verifyBirthDate());
        usernameTf.textProperty().addListener((o, oldV, newV) -> verifyUsername());
        emailTf.textProperty().addListener((o, oldV, newV) -> verifyEmail());
        phoneTf.textProperty().addListener((o, oldV, newV) -> verifyPhone());
        nameTf.textProperty().addListener((o, oldV, newV) -> verifyName());
        passwordPf.textProperty().addListener((observable, oldValue, newValue) -> verifyPassword());
        secAnswerPf.textProperty().addListener((observable, oldValue, newValue) -> verifyAnswer(newValue));
        passwordConfirmPf.textProperty().addListener((observable, oldValue, newValue) -> verifyPassword());
    }

    private void setDatePicker() {
        String pattern = Lang.get("dd/MM/yyyy");

        birthdateDp.setPromptText(pattern);
        birthdateDp.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    private void loadUsers() {
        users = User.list();
    }

    private void setGenders() {
        genderCb.getItems().addAll(Gender.MALE, Gender.FEMALE, Gender.OTHER);
        genderCb.setValue(Gender.MALE);
    }

    private void setProfileImage(Image image) {
        ImagePattern pattern = new ImagePattern(image);
        circleImage.setFill(pattern);
    }

    private void cleanProfileImage() {
        imageUser = null;
        setProfileImage(SystemImageUtil.getImage(SystemImageUtil.DEFAULT_USER));
    }

    private Optional<ButtonType> showAlertSure() {
        Alert alertModify = new AlertCustom(Alert.AlertType.CONFIRMATION, Lang.get("Are you sure you want to proceed?"), themeStr, ButtonType.YES, ButtonType.NO);
        alertModify.setTitle(Lang.get("New User"));
        Optional<ButtonType> result = alertModify.showAndWait();
        return result;
    }

    private void showAlertError() {
        Alert alertInvalid = new AlertCustom(Alert.AlertType.ERROR, Lang.get("Fill the data correctly"), themeStr, ButtonType.OK);
        alertInvalid.setTitle(Lang.get("Error"));
        alertInvalid.show();
    }

    private Boolean isAnswerValid(String answer) {
        Boolean isAnswerOk;
        isAnswerOk = answer.length() >= 4;
        return isAnswerOk;
    }

    private Boolean isPasswordMatching(String typedPassword, String typedConfirm) {
        Boolean isPasswordMatching;
        isPasswordMatching = typedPassword.equals(typedConfirm);
        return isPasswordMatching;
    }

    private Boolean verifyUsername() {
        String newV = usernameTf.getText();
        Boolean usernameAvailable = true;
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(newV.trim())) {
                usernameAvailable = false;
                usernameToolTip.setText(Lang.get("Username already registered"));

                showToolTip(usernameToolTip, usernameTf);
                break;
            }

            usernameToolTip.setText("");
            usernameToolTip.hide();
        }

        boolean usernameIsValid = usernameAvailable;

        if (usernameAvailable) {
            if (!ValidateFieldUtil.isValidUsername(newV.trim()) && !newV.trim().isEmpty()) {
                usernameIsValid = false;

                usernameToolTip.setText(Lang.get("USERNAME_NOT_VALID"));
                showToolTip(usernameToolTip, usernameTf);

            } else {
                usernameIsValid = true;
                usernameToolTip.setText("");
                usernameToolTip.hide();
            }
        }
        return usernameIsValid;
    }

    private void showToolTip(Tooltip tooltip, Node node) {
        Window window = rootPane.getScene().getWindow();
        Bounds bounds = node.localToScreen(node.getBoundsInLocal());
        double x = bounds.getMinX();
        double y = bounds.getMaxY();
        tooltip.show(window, x, y);
    }

    @FXML
    private void selectImageHandle(ActionEvent event) {
        File file = chooseFile();

        if (file != null) {
            try {
                setImageFromFile(file);

            } catch (IOException ex) {
                ex.printStackTrace();

            }

        }

    }

    private void setImageFromFile(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        String fileName = file.getName();

        ImageFile imageFile = new ImageFile(fileName, bytes, ImageCategory.USER_PROF);
        imageUser = imageFile;

        Image img = imageUser.getImage();
        if (img == null) {
            cleanProfileImage();
        } else {
            setProfileImage(img);
        }
    }

    private File chooseFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(Lang.get(" All image files "), "*.png", "*.jpg", "*.jpeg", "*.jpe", "*.jfif", "*.gif", "*.tif", "*.tiff", "*.ico"),
                new FileChooser.ExtensionFilter(" PNG ", "*.png"),
                new FileChooser.ExtensionFilter(" JPEG ", "*.jpg", "*.jpeg", "*.jpe", "*.jfif"),
                new FileChooser.ExtensionFilter(" GIF ", "*.gif"),
                new FileChooser.ExtensionFilter(" TIFF ", "*.tif", "*.tiff"),
                new FileChooser.ExtensionFilter(" ICO ", "*.ico")
        );
        File file = fc.showOpenDialog(null);
        return file;
    }

    private boolean verifyPassword() {
        boolean isValid = true;
        if (!passwordConfirmPf.getText().equals(passwordPf.getText()) && !passwordConfirmPf.getText().isEmpty()) {
            passwordToolTip.setText(Lang.get("Passwords don't match."));
            showToolTip(passwordToolTip, passwordConfirmPf);
            isValid = false;
        } else if (!ValidateFieldUtil.isValidPassword(passwordPf.getText())) {
            passwordToolTip.setText(Lang.get("PASSWORD_NOT_VALID"));
            showToolTip(passwordToolTip, passwordConfirmPf);
            isValid = false;
        } else {
            passwordToolTip.setText("");
            passwordToolTip.hide();
        }

        return isValid;
    }

    private void verifyAnswer(String newValue) {
        String value = "";
        if (newValue.length() < 4) {
            value = (4 - newValue.length()) + Lang.get(" characters left");
            secAnswerToolTip.setText(value);
            showToolTip(secAnswerToolTip, secAnswerPf);
        } else {
            secAnswerToolTip.setText(value);
            secAnswerToolTip.hide();
        }

    }

    private boolean verifyEmail() {

        boolean isValid = ValidateFieldUtil.isValidEmailAddress(emailTf.getText().trim()) || emailTf.getText().trim().isEmpty();

        if (!isValid) {
            emailToolTip.setText(Lang.get("Email is invalid."));
            showToolTip(emailToolTip, emailTf);
        } else {
            emailToolTip.setText("");
            emailToolTip.hide();
        }

        return isValid;
    }

    private boolean verifyPhone() {

        boolean isValid = ValidateFieldUtil.isValidPhone(phoneTf.getText().trim()) || phoneTf.getText().trim().isEmpty();

        if (!isValid) {
            phoneToolTip.setText(Lang.get("Phone number is invalid."));
            showToolTip(phoneToolTip, phoneTf);
        } else {
            phoneToolTip.setText("");
            phoneToolTip.hide();
        }

        return isValid;
    }

    private boolean verifyName() {

        boolean isValid = ValidateFieldUtil.isValidName(nameTf.getText().trim()) || nameTf.getText().trim().isEmpty();

        if (!isValid) {
            nameToolTip.setText(Lang.get("Name is invalid."));
            showToolTip(nameToolTip, nameTf);
        } else {
            nameToolTip.setText("");
            nameToolTip.hide();
        }

        return isValid;
    }

    private boolean verifyBirthDate() {
        LocalDate localDate = birthdateDp.getValue();
        boolean isValid = true;
        if (localDate == null) {
            isValid = false;
        } else if (!DateUtil.isPast(localDate)) {
            isValid = false;
        }

        if (!isValid && localDate != null) {
            dateToolTip.setText(Lang.get("Date is invalid"));
            showToolTip(dateToolTip, birthdateDp);
        } else {
            dateToolTip.setText("");
            dateToolTip.hide();
        }

        return isValid;
    }

    @FXML
    private void backHandle(ActionEvent event) {
        wizard.back();
    }

    @FXML
    private void nextHandle(ActionEvent event) {
        if (tab2.isSelected()) {
            Date birthDate = null;
            if (birthdateDp.getValue() != null) {
                birthDate = DateUtil.toDate(birthdateDp.getValue());
            }

            Gender gender = genderCb.getValue();
            String typedPassword = passwordPf.getText();
            String typedConfirm = passwordConfirmPf.getText();

            String name = nameTf.getText().trim();
            String email = emailTf.getText().trim().toLowerCase();
            String phone = phoneTf.getText().trim();
            String username = usernameTf.getText().trim().toLowerCase();
            String question = secQuestionPf.getText().trim();
            String answer = secAnswerPf.getText().trim();

            nameTf.setText(name);
            emailTf.setText(email);
            phoneTf.setText(phone);
            usernameTf.setText(username);
            secQuestionPf.setText(question);
            secAnswerPf.setText(answer);

            Boolean isValid;

            Boolean areAllFilled;

            areAllFilled = !(name.isEmpty()
                    || email.isEmpty()
                    || phone.isEmpty()
                    || username.isEmpty()
                    || typedPassword.isEmpty()
                    || typedConfirm.isEmpty()
                    || question.isEmpty()
                    || answer.isEmpty()
                    || birthDate == null
                    || typedPassword.length() < 8);

            isValid = isPasswordMatching(typedPassword, typedConfirm)
                    && isAnswerValid(answer)
                    && areAllFilled
                    && verifyUsername()
                    && verifyEmail()
                    && verifyPhone()
                    && verifyName()
                    && verifyBirthDate()
                    && verifyPassword();

            if (isValid) {

                Optional<ButtonType> result = showAlertSure();
                if (result.get() == ButtonType.YES) {
                    byte[] password = Encryption.encrypt(typedPassword);
                    SecQuestion secQuestion = new SecQuestion(question, answer);
                    newUser = new User(name, username, password, email, null, imageUser, birthDate, gender, phone, secQuestion, UserStatus.ON);
                    //newUser.insert();

                    loadUsers();
                    wizard.next();
                }
            } else {
                showAlertError();
            }
        } else {
            wizard.next();
        }

    }

    @FXML
    private void doneHandle(ActionEvent event) {
        Language language;

        CssTheme theme;
        Toggle selectedLang = this.language.getSelectedToggle();
        Toggle selectedTheme = this.theme.getSelectedToggle();

        if (selectedLang == ptButton) {
            language = Language.PTBR;
        } else {
            language = Language.ENUS;
        }

        if (selectedTheme == darkButton) {
            theme = CssTheme.DARK;
        } else {
            theme = CssTheme.LIGHT;
        }
        Settings settings = new Settings(new Date(), language, theme);

        List<Profile> profiles = Profile.list();
        Profile admin = null;

        for (Profile p : profiles) {
            if (p.getName().equalsIgnoreCase("Admin")) {
                admin = p;
            }
        }

        newUser.setProfile(admin);
        settings.merge();
        newUser.insert();
        finished.setValue(Boolean.TRUE);
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelHandle(ActionEvent event) {
        closeWindow();
    }

    public BooleanProperty finished = new SimpleBooleanProperty(false);

    private void setThemeListener() {
        theme.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            String path = "br/com/senaimg/wms/view/";
            String css = "setup.css";
            String theme;
            if (newValue == darkButton) {
                adviceText.setFill(Color.WHITE);

                theme = "stylesheet";
            } else {
                adviceText.setFill(Color.BLACK);
                theme = "stylesheetLight";
            }
            themeStr = theme;
            ObservableList<String> stylesheets = rootPane.getStylesheets();
            stylesheets.clear();
            stylesheets.add(path + theme + "/" + css);

        });
    }
}
