package jars.busqueda;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class FiltroDirectorios extends FileFilter {
	public FiltroDirectorios(PanelB�squeda paramPanelB�squeda) {
	}

	public boolean accept(File f) {
		return f.isDirectory();
	}

	public String getDescription() {
		return "Directorios";
	}
}
