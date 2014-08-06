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
	private Map<String, Map<String, List<String>>> caché = null;
	private static BuscadorClases elBuscador = null;
	private FileFilter filtroJarZip = null;

	private BuscadorClases() {
		this.caché = new HashMap<String, Map<String, List<String>>>();
		this.filtroJarZip = new FiltroJarZip();
	}

	public static BuscadorClases obtenerBuscador() {
		if (elBuscador == null) {
			elBuscador = new BuscadorClases();
		}
		return elBuscador;
	}

	public List<Map<String, List<String>>> buscarClases(
			List<String> directorios, String patrón) {
		añadirDirectoriosALaCaché(directorios);
		return buscarEnCaché(directorios, patrón.toUpperCase());
	}

	private void añadirDirectoriosALaCaché(List<String> directorios) {
		for (String directorio : directorios) {
			if (!this.caché.containsKey(directorio)) {
				this.caché.put(directorio, leerJarsDirectorio(directorio));
			}
		}
	}

	private Map<String, List<String>> buscarEnDirectorioCaché(
			String directorio, String patrón) {
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		Map<String, List<String>> ficherosJar;
		if (this.caché.containsKey(directorio)) {
			ficherosJar = this.caché.get(directorio);
			for (Iterator<String> it = ficherosJar.keySet().iterator(); it
					.hasNext();) {
				String ficheroJar = (String) it.next();

				List<String> ficherosClass = ficherosJar.get(ficheroJar);
				for (String ficheroClass : ficherosClass) {
					if (ficheroClass.toUpperCase().contains(patrón)) {
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

	private List<Map<String, List<String>>> buscarEnCaché(
			List<String> directorios, String patrón) {
		List<Map<String, List<String>>> ret = new LinkedList<Map<String, List<String>>>();
		for (String directorio : directorios) {
			Map<String, List<String>> tabla = buscarEnDirectorioCaché(
					directorio, patrón);
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
