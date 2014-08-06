package jars.busqueda;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class BuscadorClases {
	private static final String[] TERMINACIONES = { ".jar", ".zip", ".ear",
			".war" };
	// private static final String SUFIJO_CLASS = ".class";
	private Map<String, Map<String, List<String>>> cach� = null;
	private static BuscadorClases elBuscador = null;
	private FileFilter filtroJarZip = null;

	private BuscadorClases() {
		this.cach� = new HashMap<String, Map<String, List<String>>>();
		this.filtroJarZip = new FiltroJarZip();
	}

	public static BuscadorClases obtenerBuscador() {
		if (elBuscador == null) {
			elBuscador = new BuscadorClases();
		}
		return elBuscador;
	}

	public List<Map<String, List<String>>> buscarClases(
			List<String> directorios, String patr�n) {
		a�adirDirectoriosALaCach�(directorios);
		return buscarEnCach�(directorios, patr�n.toUpperCase());
	}

	private void a�adirDirectoriosALaCach�(List<String> directorios) {
		for (String directorio : directorios) {
			if (!this.cach�.containsKey(directorio)) {
				this.cach�.put(directorio, leerJarsDirectorio(directorio));
			}
		}
	}

	private Map<String, List<String>> buscarEnDirectorioCach�(
			String directorio, String patr�n) {
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		Map<String, List<String>> ficherosJar;
		if (this.cach�.containsKey(directorio)) {
			ficherosJar = this.cach�.get(directorio);
			for (Iterator<String> it = ficherosJar.keySet().iterator(); it
					.hasNext();) {
				String ficheroJar = (String) it.next();

				List<String> ficherosClass = ficherosJar.get(ficheroJar);
				for (String ficheroClass : ficherosClass) {
					if (ficheroClass.toUpperCase().contains(patr�n)) {
						List<String> laLista = ret.get(ficheroJar);
						if (laLista == null) {
							laLista = new LinkedList<String>();
							ret.put(ficheroJar, laLista);
						}
						laLista.add(ficheroClass);
					}
				}
			}
		}
		return ret;
	}

	private Map<String, List<String>> leerJarsDirectorio(String directorio) {
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		if (directorio != null) {
			File dirFile = new File(directorio);
			if ((dirFile.exists()) && (dirFile.isDirectory())) {
				File[] ficheros = dirFile.listFiles(this.filtroJarZip);
				for (File fichero : ficheros) {
					String rutaFichero = fichero.getPath();
					List<String> lista = obtenerClases(fichero);
					ret.put(rutaFichero, lista);
				}
			}
		}
		return ret;
	}

	private List<Map<String, List<String>>> buscarEnCach�(
			List<String> directorios, String patr�n) {
		List<Map<String, List<String>>> ret = new LinkedList<Map<String, List<String>>>();
		for (String directorio : directorios) {
			Map<String, List<String>> tabla = buscarEnDirectorioCach�(
					directorio, patr�n);
			if ((tabla != null) && (tabla.size() > 0)) {
				ret.add(tabla);
			}
		}
		return ret;
	}

	private List<String> obtenerClases(File fichero) {
		List<String> ret = new LinkedList<String>();
		try {
			JarFile jf = new JarFile(fichero);
			Enumeration<JarEntry> entradas = jf.entries();
			while (entradas.hasMoreElements()) {
				JarEntry entrada = (JarEntry) entradas.nextElement();
				if (!entrada.isDirectory()) {
					ret.add(entrada.getName());
				}
			}
			jf.close();
		} catch (IOException ioe) {
			System.err.println("---> Error al procesar " + fichero.getPath());
			ioe.printStackTrace();
		}
		return ret;
	}

	private class FiltroJarZip implements FileFilter {
		private FiltroJarZip() {
		}

		public boolean accept(File pathname) {
			String nombreFichero = pathname.getName();
			boolean aceptar = false;
			int i = 0;
			int longitud = BuscadorClases.TERMINACIONES.length;
			while ((!aceptar) && (i < longitud)) {
				aceptar = nombreFichero
						.endsWith(BuscadorClases.TERMINACIONES[(i++)]);
			}
			return aceptar;
		}
	}
}
