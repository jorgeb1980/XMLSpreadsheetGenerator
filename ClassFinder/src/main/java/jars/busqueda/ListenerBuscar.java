package jars.busqueda;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class ListenerBuscar implements ActionListener {

	private PanelBúsqueda panel;

	public ListenerBuscar(PanelBúsqueda paramPanelBúsqueda) {
		panel = paramPanelBúsqueda;
	}

	public void actionPerformed(ActionEvent evt) {

		panel.botónBuscar(evt);
	}
}
