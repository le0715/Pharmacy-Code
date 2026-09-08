package pharmacy.app;

import pharmacy.domain.*;


import javax.swing.SwingUtilities;
import javax.swing.UIManager;

//patient dashboard
public final class PharmacyManagementApp {

    private PharmacyManagementApp() {

    }

    public static void main(String[] commandLineArguments) {
        applySystemLookAndFeel();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    private static void applySystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception lookAndFeelException) {
          
        }
    }
}
