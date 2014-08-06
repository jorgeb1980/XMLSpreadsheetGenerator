package jars.busqueda;

import javax.swing.AbstractListModel;

public final class ModeloListaCadenas extends AbstractListModel<String> {
	/**
	 * Generado por eclipse para serialización.
	 */
	private static final long serialVersionUID = -2081591990343054530L;
	String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

	public ModeloListaCadenas(PanelBúsqueda paramPanelBúsqueda) {
	}

	public int getSize() {
		return this.strings.length;
	}

	public String getElementAt(int i) {
		return this.strings[i];
	}
}
