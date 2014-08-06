package jars.busqueda;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class ModeloTablaResultados extends AbstractTableModel {
	/**
	 * Generado por eclipse para serialización
	 */
	private static final long serialVersionUID = 1636480537993041534L;
	private List<String[]> modelo = null;

	public ModeloTablaResultados() {
		this.modelo = new ArrayList<String[]>();
	}

	public ModeloTablaResultados(
			List<Map<String, List<String>>> resultadoBúsqueda) {
		this();
		if (resultadoBúsqueda != null) {
			for (Iterator<Map<String, List<String>>> it = resultadoBúsqueda
					.iterator(); it.hasNext();) {
				Map<String, List<String>> tabla = it.next();
				for (Iterator<String> itClave = tabla.keySet().iterator(); itClave
						.hasNext();) {
					String ficheroJar = (String) itClave.next();
					boolean primero = true;
					for (String clase : tabla.get(ficheroJar)) {
						String columnaFichero = "";
						if (primero) {
							columnaFichero = ficheroJar;
							primero = false;
						}
						this.modelo.add(new String[] { columnaFichero, clase });
					}
				}
			}
		}
	}

	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		return this.modelo.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return ((String[]) this.modelo.get(rowIndex))[columnIndex];
	}

	public String getColumnName(int column) {
		String ret = "";
		if (column == 0) {
			ret = "Fichero";
		} else if (column == 1) {
			ret = "Clase";
		}
		return ret;
	}
}
