package jars.busqueda;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class ListenerEliminar implements ActionListener {
	private PanelB�squeda panel;

	public ListenerEliminar(PanelB�squeda paramPanelB�squeda) {
		panel = paramPanelB�squeda;
	}

	public void actionPerformed(ActionEvent evt) {
		panel.bot�nEliminarDirectorio(evt);
	}
}
