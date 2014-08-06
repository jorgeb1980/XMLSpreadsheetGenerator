package jars.busqueda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractListModel;

public class ModeloListaFicheros extends AbstractListModel<String> {
	/**
	 * Generado por eclipse para serialización
	 */
	private static final long serialVersionUID = 6560570616700652378L;
	private List<String> modelo = null;

	public ModeloListaFicheros() {
		this.modelo = new ArrayList<String>();
	}

	public String getElementAt(int index) {
		return this.modelo.get(index);
	}

	public int getSize() {
		return this.modelo.size();
	}

	public void añadirEntrada(String directorio) {
		this.modelo.add(directorio);
		Collections.sort(this.modelo);
		fireContentsChanged(this, 0, this.modelo.size());
	}

	public void eliminarEntrada(int índice) {
		this.modelo.remove(índice);
		fireContentsChanged(this, 0, this.modelo.size());
	}
}
