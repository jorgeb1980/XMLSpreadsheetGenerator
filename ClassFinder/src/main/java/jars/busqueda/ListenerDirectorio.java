package jars.busqueda;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class ListenerDirectorio implements ActionListener {

	private PanelB�squeda panel;

	public ListenerDirectorio(PanelB�squeda paramPanelB�squeda) {
		panel = paramPanelB�squeda;
	}

	public void actionPerformed(ActionEvent evt) {
		panel.bot�nA�adirDirectorio(evt);
	}
}
