package jars.busqueda;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Principal {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame principal = new JFrame("Aplicaci�n de b�squeda de clases");

		JPanel panel = new PanelB�squeda();
		principal.setContentPane(panel);
		principal.setSize(panel.getPreferredSize());
		principal.setDefaultCloseOperation(3);

		principal.setLocationRelativeTo(null);

		principal.setVisible(true);
	}
}
