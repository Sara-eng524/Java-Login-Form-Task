
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

// هنا عملنا User class لتخزين بيانات كل مستخدم
class User {
    private String username;
    private String password;
    private String role;

    // Constructor: يستقبل البيانات ويحفظها داخل الـ object
    User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters: تسمح لنا بقراءة بيانات المستخدم
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

// هنا عملنا صفحة الترحيب التي تظهر بعد نجاح تسجيل الدخول
class WelcomePage extends Frame {

    WelcomePage(User user) {
        super("Welcome");

        setLayout(new GridLayout(2, 1, 10, 10));

        Label welcome = new Label(
                "Hello, " + user.getUsername(),
                Label.CENTER
        );
        welcome.setFont(new Font("SansSerif", Font.BOLD, 20));

        Label userRole = new Label(
                "Role: " + user.getRole(),
                Label.CENTER
        );

        add(welcome);
        add(userRole);

        // هنا أضفنا طريقة إغلاق نافذة الترحيب
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(350, 180);
        setLocationRelativeTo(null);
    }
}

public class LoginFormTask extends Frame {

    // هنا جهزنا قائمة المستخدمين وعدّاد محاولات كلمة المرور
    private final ArrayList<User> users = new ArrayList<>();
    private int failedPasswordAttempts = 0;
    private Button loginButton;

    public LoginFormTask() {
        super("Login");
        loadUsers();

        setLayout(new GridBagLayout());
        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));

        form.add(new Label("Username:", Label.RIGHT));
        TextField username = new TextField(18);
        form.add(username);

        form.add(new Label("Password:", Label.RIGHT));
        TextField password = new TextField(18);
        password.setEchoChar('*');
        form.add(password);

        form.add(new Label("Role:", Label.RIGHT));
        Choice role = new Choice();
        role.add("Student");
        role.add("TA");
        role.add("Professor");
        form.add(role);

        content.add(form, BorderLayout.CENTER);

        Panel buttons = new Panel(
                new FlowLayout(FlowLayout.CENTER, 15, 0)
        );

        Button cancel = new Button("Cancel");
        loginButton = new Button("Login");

        buttons.add(cancel);
        buttons.add(loginButton);
        content.add(buttons, BorderLayout.SOUTH);

        add(content);

        // هنا نفذنا التحقق من البيانات عند الضغط على Login
        loginButton.addActionListener(e -> {
            String enteredUsername = username.getText().trim();
            String enteredPassword = password.getText();
            String selectedRole = role.getSelectedItem();

            // أولاً: التأكد أن الخانات ليست فارغة
            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter username and password."
                );
                return;
            }

            // ثانياً: البحث عن المستخدم
            User user = findUser(enteredUsername);

            if (user == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Username not found."
                );
                return;
            }

            // ثالثاً: التحقق من كلمة المرور
            if (!user.getPassword().equals(enteredPassword)) {
                failedPasswordAttempts++;

                if (failedPasswordAttempts >= 3) {
                    loginButton.setEnabled(false);

                    JOptionPane.showMessageDialog(
                            this,
                            "Too many incorrect password attempts. Login disabled."
                    );
                } else {
                    int remainingAttempts = 3 - failedPasswordAttempts;

                    JOptionPane.showMessageDialog(
                            this,
                            "Incorrect password. Remaining attempts: "
                                    + remainingAttempts
                    );
                }

                return;
            }

            // رابعاً: التأكد أن الـ Role المختار يطابق المستخدم
            if (!user.getRole().equals(selectedRole)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Incorrect role selected."
                );
                return;
            }

            // هنا فتحنا صفحة الترحيب بعد نجاح التحقق
            WelcomePage welcomePage = new WelcomePage(user);
            welcomePage.setVisible(true);
            dispose();
        });

        // هنا جعلنا زر Cancel يغلق نافذة تسجيل الدخول
        cancel.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(420, 340);
        setLocationRelativeTo(null);
    }

    // هنا أضفنا ثلاثة مستخدمين، واحد لكل Role
    private void loadUsers() {
        users.add(new User("student1", "stu123", "Student"));
        users.add(new User("ta1", "ta123", "TA"));
        users.add(new User("prof1", "prof123", "Professor"));
    }

    // هنا بحثنا في القائمة عن مستخدم بالاسم
    private User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        new LoginFormTask().setVisible(true);
    }
}
