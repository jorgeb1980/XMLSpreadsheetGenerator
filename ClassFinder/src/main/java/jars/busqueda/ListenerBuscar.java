package jars.busqueda;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class ListenerBuscar implements ActionListener {

	private PanelB�squeda panel;

	public ListenerBuscar(PanelB�squeda paramPanelB�squeda) {
		panel = paramPanelB�squeda;
	}

	public void actionPerformed(ActionEvent evt) {

		panel.bot�nBuscar(evt);
	}
}
