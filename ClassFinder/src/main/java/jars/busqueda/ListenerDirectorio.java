package jars.busqueda;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class ListenerDirectorio implements ActionListener {

	private PanelBúsqueda panel;

	public ListenerDirectorio(PanelBúsqueda paramPanelBúsqueda) {
		panel = paramPanelBúsqueda;
	}

	public void actionPerformed(ActionEvent evt) {
		panel.botónAñadirDirectorio(evt);
	}
}
