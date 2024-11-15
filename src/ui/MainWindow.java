package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import csv.Reader;
import gods.GreekGod;
import lists.DoubleList;

public class MainWindow extends JFrame {
	private int width = 1376;
	private int height = 768;

	private DoubleList<GreekGod> godsList;
	private DoubleList<GreekGod> currentDisplayList;

	private DefaultListModel<String> listModel;
	private JList<String> jList;

	private JTextArea godInfoArea;
	private JLabel godImageLabel;

	private JComboBox<String> mainTypeComboBox;
	private JComboBox<String> subTypeComboBox;

	public MainWindow() {
		setTitle("Greek Gods");
		setPreferredSize(new Dimension(width, height));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initializeData();
		initializeUI();

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initializeData() {
		godsList = new DoubleList<>();
		Reader reader = new Reader("res/csv/gods/greek_gods.csv");
		try {
			for (GreekGod god : reader.readAll()) {
				godsList.insertLast(god);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error leyendo el archivo CSV: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		currentDisplayList = godsList; // Inicialmente mostrar todos los dioses
	}

	private void initializeUI() {
		// Contenedor principal
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(mainPanel);

		// Panel izquierdo para la lista y controles
		JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
		leftPanel.setPreferredSize(new Dimension(400, height - 100));
		mainPanel.add(leftPanel, BorderLayout.WEST);

		// Modelo de lista y JList
		listModel = new DefaultListModel<>();
		jList = new JList<>(listModel);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				displayGodInfo(jList.getSelectedIndex());
			}
		});
		JScrollPane listScrollPane = new JScrollPane(jList);
		leftPanel.add(listScrollPane, BorderLayout.CENTER);

		// Panel de botones
		JPanel buttonsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		leftPanel.add(buttonsPanel, BorderLayout.SOUTH);

		// Botón para ordenar ascendente
		JButton btnSortAsc = new JButton("Ordenar Ascendente");
		btnSortAsc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sortList(true);
			}
		});
		buttonsPanel.add(btnSortAsc);

		// Botón para ordenar descendente
		JButton btnSortDesc = new JButton("Ordenar Descendente");
		btnSortDesc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sortList(false);
			}
		});
		buttonsPanel.add(btnSortDesc);

		// ComboBox para seleccionar main-type y mostrar conteo
		mainTypeComboBox = new JComboBox<>(new String[]{"Selecciona Tipo Principal", "god", "titan", "personification"});
		buttonsPanel.add(mainTypeComboBox);

		JButton btnCountMainType = new JButton("Contar por Tipo Principal");
		btnCountMainType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				countByMainType();
			}
		});
		buttonsPanel.add(btnCountMainType);

		// ComboBox para seleccionar sub-type y filtrar lista
		subTypeComboBox = new JComboBox<>(getAllSubTypes());
		buttonsPanel.add(subTypeComboBox);

		JButton btnFilterSubType = new JButton("Filtrar por Subtipo");
		btnFilterSubType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterBySubType();
			}
		});
		buttonsPanel.add(btnFilterSubType);

		JButton btnResetFilter = new JButton("Resetear Filtros");
		btnResetFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetFilters();
			}
		});
		buttonsPanel.add(btnResetFilter);

		// Panel derecho para detalles del dios
		JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
		mainPanel.add(rightPanel, BorderLayout.CENTER);

		// Área de texto para información del dios
		godInfoArea = new JTextArea();
		godInfoArea.setEditable(false);
		godInfoArea.setLineWrap(true);
		godInfoArea.setWrapStyleWord(true);
		JScrollPane infoScrollPane = new JScrollPane(godInfoArea);
		infoScrollPane.setPreferredSize(new Dimension(width - 450, height / 2));
		rightPanel.add(infoScrollPane, BorderLayout.CENTER);

		// Etiqueta para la imagen del dios
		godImageLabel = new JLabel();
		godImageLabel.setHorizontalAlignment(JLabel.CENTER);
		godImageLabel.setVerticalAlignment(JLabel.CENTER);
		godImageLabel.setPreferredSize(new Dimension(width - 450, height / 2));
		rightPanel.add(godImageLabel, BorderLayout.SOUTH);

		// Poblamos la lista inicialmente
		updateListDisplay();
	}

	private void sortList(boolean ascending) {
		// Convertir DoubleList a ArrayList para facilitar el ordenamiento
		java.util.List<GreekGod> tempList = new java.util.ArrayList<>();
		for (int i = 0; i < godsList.size(); i++) {
			tempList.add(godsList.get(i));
		}

		// Ordenar la lista
		if (ascending) {
			tempList.sort((g1, g2) -> g1.getNameEnglish().compareToIgnoreCase(g2.getNameEnglish()));
		} else {
			tempList.sort((g1, g2) -> g2.getNameEnglish().compareToIgnoreCase(g1.getNameEnglish()));
		}

		// Actualizar la lista actual
		DoubleList<GreekGod> sortedList = new DoubleList<>();
		for (GreekGod god : tempList) {
			sortedList.insertLast(god);
		}
		currentDisplayList = sortedList;

		updateListDisplay();
	}

	/**
	 * Cuenta el número de dioses según el tipo principal seleccionado.
	 */
	private void countByMainType() {
		String selectedType = (String) mainTypeComboBox.getSelectedItem();
		if (selectedType == null || selectedType.equals("Selecciona Tipo Principal")) {
			JOptionPane.showMessageDialog(this, "Por favor, selecciona un tipo principal válido.",
					"Información", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		int count = 0;
		for (int i = 0; i < godsList.size(); i++) {
			if (godsList.get(i).getMainType().equalsIgnoreCase(selectedType)) {
				count++;
			}
		}

		JOptionPane.showMessageDialog(this, "Número de " + selectedType + "(s): " + count,
				"Conteo por Tipo Principal", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Filtra la lista actual por el subtipo seleccionado.
	 */
	private void filterBySubType() {
		String selectedSubType = (String) subTypeComboBox.getSelectedItem();
		if (selectedSubType == null || selectedSubType.equals("Selecciona Subtipo")) {
			JOptionPane.showMessageDialog(this, "Por favor, selecciona un subtipo válido.",
					"Información", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		// Crear una nueva DoubleList para los dioses filtrados
		DoubleList<GreekGod> filteredList = new DoubleList<>();
		for (int i = 0; i < godsList.size(); i++) {
			GreekGod god = godsList.get(i);
			if (god.getSubType().equalsIgnoreCase(selectedSubType)) {
				filteredList.insertLast(god);
			}
		}

		currentDisplayList = filteredList;
		updateListDisplay();
	}

	/**
	 * Resetea los filtros aplicados y muestra la lista completa.
	 */
	private void resetFilters() {
		currentDisplayList = godsList;
		updateListDisplay();
		mainTypeComboBox.setSelectedIndex(0);
		subTypeComboBox.setSelectedIndex(0);
	}

	/**
	 * Actualiza la visualización de la lista basada en currentDisplayList.
	 */
	private void updateListDisplay() {
		listModel.clear();
		for (int i = 0; i < currentDisplayList.size(); i++) {
			listModel.addElement(currentDisplayList.get(i).toString());
		}
	}

	/**
	 * Muestra la información y la imagen del dios seleccionado.
	 * 
	 * @param index El índice del dios seleccionado en currentDisplayList.
	 */
	private void displayGodInfo(int index) {
		if (index < 0 || index >= currentDisplayList.size()) {
			godInfoArea.setText("");
			godImageLabel.setIcon(null);
			return;
		}

		GreekGod god = currentDisplayList.get(index);
		String info = "Nombre en Inglés: " + god.getNameEnglish() + "\n"
				+ "Nombre en Griego: " + god.getNameGreek() + "\n"
				+ "Tipo Principal: " + god.getMainType() + "\n"
				+ "Subtipo: " + god.getSubType() + "\n"
				+ "Descripción: " + god.getDescription();
		godInfoArea.setText(info);

		// Cargar y mostrar la imagen
		ImageIcon icon = new ImageIcon(god.getImagePath());
		Image img = icon.getImage();
		Image scaledImg = img.getScaledInstance(godImageLabel.getWidth(), godImageLabel.getHeight(), Image.SCALE_SMOOTH);
		godImageLabel.setIcon(new ImageIcon(scaledImg));
	}

	/**
	 * Obtiene todos los subtipos únicos de godsList.
	 * 
	 * @return Un arreglo de subtipos únicos.
	 */
	private String[] getAllSubTypes() {
		java.util.Set<String> subTypesSet = new java.util.HashSet<>();
		subTypesSet.add("Selecciona Subtipo");
		for (int i = 0; i < godsList.size(); i++) {
			subTypesSet.add(godsList.get(i).getSubType());
		}
		return subTypesSet.toArray(new String[0]);
	}
}
