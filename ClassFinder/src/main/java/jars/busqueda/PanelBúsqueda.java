package jars.busqueda;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

public class PanelB�squeda extends JPanel {
	/**
	 * Generado por eclipse para serialización
	 */
	private static final long serialVersionUID = -6396120998133277126L;
	private JFileChooser di�logoFicheros = null;
	private FileFilter filtroDirectorios = null;
	private JLabel etiquetaTiempo�ltimaConsulta;
	private JButton jButton1;
	private JButton jButton2;
	private JButton jButton3;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel6;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JSeparator jSeparator1;
	private JList<String> listaDirectorios;
	private JTable tablaResultados;
	private JTextField textoPatr�nB�squeda;

	public PanelB�squeda() {
		initComponents();
		this.listaDirectorios.setModel(new ModeloListaFicheros());
		this.tablaResultados.setModel(new ModeloTablaResultados());
		this.di�logoFicheros = new JFileChooser();
		this.filtroDirectorios = new FiltroDirectorios(this);
		this.di�logoFicheros.setFileFilter(this.filtroDirectorios);
		this.di�logoFicheros.setMultiSelectionEnabled(false);
		this.di�logoFicheros.setFileSelectionMode(1);
	}

	private void initComponents() {
		this.jLabel1 = new JLabel();
		this.etiquetaTiempo�ltimaConsulta = new JLabel();
		this.jLabel2 = new JLabel();
		this.jScrollPane1 = new JScrollPane();
		this.listaDirectorios = new JList<String>();
		this.jButton1 = new JButton();
		this.jLabel3 = new JLabel();
		this.jSeparator1 = new JSeparator();
		this.jLabel4 = new JLabel();
		this.jScrollPane2 = new JScrollPane();
		this.tablaResultados = new JTable();
		this.jButton2 = new JButton();
		this.jButton3 = new JButton();
		this.jLabel5 = new JLabel();
		this.textoPatr�nB�squeda = new JTextField();
		this.jLabel6 = new JLabel();

		this.jLabel1.setText("Tiempo de la �ltima consulta:");

		this.jLabel2.setText("Lista de directorios");

		this.listaDirectorios.setModel(new ModeloListaCadenas(this));

		this.jScrollPane1.setViewportView(this.listaDirectorios);

		this.jButton1.setText("...");
		this.jButton1.setPreferredSize(new Dimension(23, 23));
		this.jButton1.addActionListener(new ListenerDirectorio(this));

		this.jLabel3.setText("A�adir directorio");

		this.jLabel4.setText("Resultados de la consulta");

		this.tablaResultados.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null, null }, { null, null, null, null },
				{ null, null, null, null }, { null, null, null, null } },
				new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));

		this.jScrollPane2.setViewportView(this.tablaResultados);

		this.jButton2.setText("Buscar");
		this.jButton2.addActionListener(new ListenerBuscar(this));

		this.jButton3.setText("X");
		this.jButton3.setPreferredSize(new Dimension(25, 23));
		this.jButton3.addActionListener(new ListenerEliminar(this));

		this.jLabel5.setText("Eliminar directorio");

		this.textoPatr�nB�squeda.setPreferredSize(new Dimension(260, 20));

		this.jLabel6.setText("Patr�n de b�squeda:");

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		// Código autogenerado por netbeans para el layout de la página...
		// buffff
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createParallelGroup(
																GroupLayout.Alignment.LEADING)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createParallelGroup(
																								GroupLayout.Alignment.LEADING)
																								.addGroup(
																										layout.createSequentialGroup()
																												.addComponent(
																														this.jScrollPane1,
																														-1,
																														473,
																														32767)
																												.addContainerGap())
																								.addGroup(
																										GroupLayout.Alignment.TRAILING,
																										layout.createSequentialGroup()
																												.addComponent(
																														this.jLabel2)
																												.addPreferredGap(
																														LayoutStyle.ComponentPlacement.RELATED,
																														269,
																														32767)
																												.addComponent(
																														this.jLabel3)
																												.addPreferredGap(
																														LayoutStyle.ComponentPlacement.RELATED)
																												.addComponent(
																														this.jButton1,
																														-2,
																														-1,
																														-2)
																												.addGap(18,
																														18,
																														18))
																								.addGroup(
																										layout.createSequentialGroup()
																												.addComponent(
																														this.jLabel4)
																												.addContainerGap(
																														361,
																														32767))
																								.addGroup(
																										GroupLayout.Alignment.TRAILING,
																										layout.createSequentialGroup()
																												.addGroup(
																														layout.createParallelGroup(
																																GroupLayout.Alignment.TRAILING)
																																.addComponent(
																																		this.jScrollPane2,
																																		GroupLayout.Alignment.LEADING,
																																		-1,
																																		473,
																																		32767)
																																.addComponent(
																																		this.jSeparator1,
																																		-1,
																																		473,
																																		32767))
																												.addContainerGap()))
																				.addGroup(
																						GroupLayout.Alignment.TRAILING,
																						layout.createSequentialGroup()
																								.addComponent(
																										this.jLabel6)
																								.addPreferredGap(
																										LayoutStyle.ComponentPlacement.RELATED)
																								.addComponent(
																										this.textoPatr�nB�squeda,
																										-2,
																										-1,
																										-2)
																								.addGap(18,
																										18,
																										18)
																								.addComponent(
																										this.jButton2)
																								.addContainerGap()))
																.addGroup(
																		GroupLayout.Alignment.TRAILING,
																		layout.createSequentialGroup()
																				.addComponent(
																						this.jLabel5)
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						this.jButton3,
																						-2,
																						39,
																						-2)
																				.addContainerGap()))
												.addGroup(
														GroupLayout.Alignment.TRAILING,
														layout.createSequentialGroup()
																.addComponent(
																		this.jLabel1)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		this.etiquetaTiempo�ltimaConsulta)
																.addGap(81, 81,
																		81)))));

		layout.setVerticalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(this.jLabel2)
												.addComponent(this.jLabel3)
												.addComponent(this.jButton1,
														-2, -1, -2))
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(this.jScrollPane1, -2, 114, -2)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(this.jButton3,
														-2, -1, -2)
												.addComponent(this.jLabel5))
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(this.jSeparator1, -2, -1, -2)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(this.jLabel4)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(this.jScrollPane2, -1, 100, 32767)
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(this.jButton2)
												.addComponent(
														this.textoPatr�nB�squeda,
														-2, -1, -2)
												.addComponent(this.jLabel6))
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(this.jLabel1)
												.addComponent(
														this.etiquetaTiempo�ltimaConsulta))
								.addContainerGap()));
	}

	void bot�nBuscar(ActionEvent evt) {
		String patr�n = this.textoPatr�nB�squeda.getText();
		if ((patr�n != null) && (patr�n.trim().length() > 0)) {
			ListModel<String> modelo = this.listaDirectorios.getModel();
			int tamListaEntrada = modelo.getSize();
			if (tamListaEntrada > 0) {
				List<String> directorios = new LinkedList<String>();
				for (int i = 0; i < tamListaEntrada; i++) {
					directorios.add((String) modelo.getElementAt(i));
				}
				long comienzo = new Date().getTime();

				List<Map<String, List<String>>> ret = BuscadorClases
						.obtenerBuscador().buscarClases(directorios,
								patr�n.trim());

				long total = new Date().getTime() - comienzo;
				this.etiquetaTiempo�ltimaConsulta.setText(Long.toString(total) + " mseg.");
				this.tablaResultados.setModel(new ModeloTablaResultados(ret));
			}
		}
	}

	void bot�nEliminarDirectorio(ActionEvent evt) {
		int �ndice = this.listaDirectorios.getSelectedIndex();
		if (�ndice >= 0) {
			((ModeloListaFicheros) this.listaDirectorios.getModel())
					.eliminarEntrada(�ndice);
		}
	}

	void bot�nA�adirDirectorio(ActionEvent evt) {
		int resultado = this.di�logoFicheros.showOpenDialog(this);
		switch (resultado) {
		case 0:
			((ModeloListaFicheros) this.listaDirectorios.getModel())
					.a�adirEntrada(this.di�logoFicheros.getSelectedFile()
							.getPath());

			break;
		}
	}
}
