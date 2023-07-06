package apresentacaoMenuSpooti;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import dados.*;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;
import excecoes.UpdateException;
import negocio.Aplicativo;
import persistencia.ArtistasMusicasDAO;
import apresentacao.*;
import java.awt.Font;
import java.awt.Color; 

public class VerMusica extends JPanel {

	private JTextField textFieldNome;
	private Aplicativo app  = Aplicativo.getInstance(); 
	private Boolean edit = false; 
	private JButton btnEditar;
	private JList<Artista> list;
	private JScrollPane scrollPane; 
	private JTextField textFieldGenero;
	private Musica music; 
	private JPanel panelCheckBoxes; 
	private JLabel error;
	private CardLayout cardLayout;
	private JPanel cards; 
	private ArtistasMusicasDAO adicionarArtistaMusicaDAO = ArtistasMusicasDAO.getInstance(); 
	
	public VerMusica(CardLayout cardLayout, JPanel cards) throws ClassNotFoundException, SQLException, SelectException {
		setLayout(null); 
		this.cards = cards; 
		this.cardLayout = cardLayout; 

		JLabel nomePlaylist = new JLabel("Nome: ");
		nomePlaylist.setBounds(107, 58, 62, 15);
		add(nomePlaylist);
		
		textFieldNome = new JTextField();
		textFieldNome.setBounds(187, 56, 130, 19);
		textFieldNome.setEditable(false);
		textFieldNome.setHorizontalAlignment(SwingConstants.LEFT);
		add(textFieldNome);
		textFieldNome.setColumns(10);
		
		JLabel lblMusicas = new JLabel("Artistas:");
		lblMusicas.setBounds(107, 178, 62, 15);
		add(lblMusicas);
				
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.setBounds(81, 303, 103, 25);
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cards, "Listas");
			}
		});
		add(btnVoltar);
		
		btnEditar = new JButton("Editar");
		btnEditar.setBounds(290, 303, 117, 25);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			   try {
				editarMusica();
			} catch (SelectException | UpdateException | IOException | InsertException |DeleteException e1) {
				e1.printStackTrace();
				}
			}
		});
		add(btnEditar);
		
		JLabel lblGnero = new JLabel("Gênero:");
		lblGnero.setBounds(107, 99, 70, 15);
		add(lblGnero);
		
		textFieldGenero = new JTextField();
		textFieldGenero.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldGenero.setEditable(false);
		textFieldGenero.setColumns(10);
		textFieldGenero.setBounds(187, 97, 130, 19);
		add(textFieldGenero);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(187, 178, 140, 77);
		add(scrollPane); 
		
		JButton btnPlay = new JButton("▷");
		btnPlay.setFont(new Font("Dialog", Font.BOLD, 14));
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  MenuSpooti menuSpooti = (MenuSpooti) SwingUtilities.getAncestorOfClass(MenuSpooti.class, VerMusica.this);
				  if(menuSpooti.getTrackList().size() == 0) {
					  menuSpooti.tocarMusica(music);
				  }else {
					  menuSpooti.getTrackList().clear(); 
					  menuSpooti.tocarMusica(music);

				  }
				}
		});

		btnPlay.setBounds(351, 12, 56, 19);
		add(btnPlay);
		
		JButton btnDelete = new JButton("Deletar");
		btnDelete.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int resposta = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar?", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		        if (resposta == JOptionPane.YES_OPTION) {
		        	
		        	try {
						app.removerMusica(music);
					} catch (DeleteException | SelectException e1) {
						e1.printStackTrace();
					}

		        	cardLayout.show(cards, "Home");
		        }
			}
		});

		btnDelete.setForeground(new Color(0, 0, 0));
		btnDelete.setBounds(66, 11, 103, 20);
		add(btnDelete);
		
		error = new JLabel("");
		error.setHorizontalAlignment(SwingConstants.CENTER);
		error.setBounds(94, 267, 298, 15);
		add(error);
		
	}
		
	public void updateMusica(Musica musica) throws SelectException, DeleteException {
		this.music = musica; 
		app.updateArtistasMusicas(); 
		textFieldNome.setText(musica.getNome());
		textFieldGenero.setText(musica.getGenero());

		DefaultListModel<Artista> artistas = new DefaultListModel<>();
        for (Artista artista : musica.getArtistas()) {
            artistas.addElement(artista);
        }

        list = new JList<>(artistas);
        scrollPane.setViewportView(list);
	}
	
	
	public void editarMusica() throws SelectException, DeleteException, UpdateException, IOException, InsertException {
		if (edit == true) {
			if(textFieldNome.getText().isEmpty() || textFieldGenero.getText().isEmpty()) {
				error.setForeground(Color.red);
				error.setText("Todos campos devem estar preenchidos, até o path");
			}else {
		        btnEditar.setText("Editar");
		        edit = false;
		        String novoNome = textFieldNome.getText();
	            String novoGenero = textFieldGenero.getText();
	    
		        music.setNome(novoNome);
		        music.setGenero(novoGenero);
		        app.editarMusica(music);
		        music.getArtistas().clear(); 
		        
				Component[] components = panelCheckBoxes.getComponents();
			    for (Component component : components) {
			        if (component instanceof JCheckBox) {
			            JCheckBox checkBox = (JCheckBox) component;
			              
			            if (checkBox.isSelected()) {
			                String artistaSelecionado = checkBox.getText();
			                Artista artista = app.buscarArtistaPorNome(artistaSelecionado);
			                
			                artista.adicionarMusicasProduzidas(music);
			                music.adicionarArtistas(artista);
			                	
			            	} 
			            }
			        }
	
				for(Artista a : music.getArtistas()) {
			    	 adicionarArtistaMusicaDAO.adicionarArtistaMusica(a.getIdArtista(), music.getIdMusica());
			    }
			    
		        textFieldNome.setEditable(false);
	            textFieldGenero.setEditable(false);    
	            
	            error.setText(null);
		        updateMusica(music); 
		        scrollPane.setViewportView(list);
			}
	    } else {
	    	btnEditar.setText("Salvar");
            edit = true;
    	 
            textFieldNome.setEditable(true);
            textFieldGenero.setEditable(true);
            
            panelCheckBoxes = new JPanel();
    		panelCheckBoxes.setLayout(new GridLayout(0, 1));
    		panelCheckBoxes.removeAll();
    		for (Artista artista : app.getArtistasSistema()) {
   			    JCheckBox checkBoxMusica = new JCheckBox(artista.getNome());
   			    panelCheckBoxes.add(checkBoxMusica);
   			}
    		scrollPane.setViewportView(panelCheckBoxes);            
	    }
		
	}
}
