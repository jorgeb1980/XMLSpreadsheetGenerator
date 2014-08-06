package jars.busqueda;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class ListenerEliminar implements ActionListener {
	private PanelBúsqueda panel;

	public ListenerEliminar(PanelBúsqueda paramPanelBúsqueda) {
		panel = paramPanelBúsqueda;
	}

	public void actionPerformed(ActionEvent evt) {
		panel.botónEliminarDirectorio(evt);
	}
}
