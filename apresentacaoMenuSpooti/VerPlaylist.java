package apresentacaoMenuSpooti;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import apresentacao.MenuSpooti;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import dados.*;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;
import excecoes.UpdateException;
import negocio.Aplicativo;
import persistencia.PlaylistMusicasDAO;

import javax.swing.JScrollPane;
import java.awt.Color;

public class VerPlaylist extends JPanel {

	private JTextField textFieldNome;
	private Aplicativo app = Aplicativo.getInstance(); 
	private PlaylistMusicasDAO  pm = PlaylistMusicasDAO.getInstance(); 
	private Boolean edit = false; 
	private JButton btnEditar; 
	private JList<Musica> list;
	private String username; 
	private Usuario user; 
	private JScrollPane scrollPane; 
	private Playlist playlist; 
	private JPanel panelCheckBoxes; 
	private JButton btnDelete; 
	private JPanel cards;
	private CardLayout cardLayout; 
	
	public VerPlaylist(CardLayout cardLayout, JPanel cards) throws ClassNotFoundException, SQLException, SelectException {	
		setLayout(null);
		this.cardLayout = cardLayout; 
		this.cards = cards; 

		JLabel nomePlaylist = new JLabel("Nome: ");
		nomePlaylist.setBounds(81, 76, 62, 15);
		add(nomePlaylist);
		
		textFieldNome = new JTextField();
		textFieldNome.setBounds(161, 74, 153, 19);
		textFieldNome.setEditable(false);
		textFieldNome.setHorizontalAlignment(SwingConstants.LEFT);
		add(textFieldNome);
		textFieldNome.setColumns(10);
		
		JLabel lblMusicas = new JLabel("Músicas:");
		lblMusicas.setBounds(81, 120, 62, 15);
		add(lblMusicas);
		
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.setBounds(100, 285, 103, 25);
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cards, "Listas");
			}
		});
		add(btnVoltar);
		
		btnEditar = new JButton("Editar");
		btnEditar.setBounds(284, 285, 117, 25);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					editarPlaylist();
				} catch (UpdateException | InsertException | SelectException e1) {
					e1.printStackTrace();
				}
			}
		});
		add(btnEditar);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(149, 120, 213, 153);
		add(scrollPane);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 
				MenuSpooti menuSpooti = (MenuSpooti) SwingUtilities.getAncestorOfClass(MenuSpooti.class, VerPlaylist.this);
				 try {
					menuSpooti.tocarPlaylist(pm.getMusicasPlaylist(playlist));
				} catch (SelectException e1) {
					e1.printStackTrace();
				}

			}
		});
		btnPlay.setBounds(192, 43, 97, 19);
		add(btnPlay);
		
		btnDelete = new JButton("Deletar");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int resposta = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar?", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		        if (resposta == JOptionPane.YES_OPTION) {
		        	try {
						app.removerPlaylist(playlist);
					} catch (DeleteException e1) {
						e1.printStackTrace();
					} 
		        	cardLayout.show(cards, "Home");
		        }
			}
		});
		
		btnDelete.setForeground(Color.BLACK);
		btnDelete.setBounds(335, 40, 103, 20);
		add(btnDelete);
		
      

	}
	
	 public void updatePlaylist(Playlist playlist) throws SelectException {
		 	this.playlist = playlist; 
		 	if(app.buscarUsuarioPorUser(username).getPlaylists().indexOf(playlist) == 0){
		 		btnDelete.setVisible(false);
		 	}else {
		 		btnDelete.setVisible(true);
		 	}
		 	
	        textFieldNome.setText(playlist.getNome());
	        DefaultListModel<Musica> musicas = new DefaultListModel<>();
	        for (Musica musica : app.getMusicasPlaylist(playlist)) {
	            musicas.addElement(musica);
	        }
	        list = new JList<>(musicas);
	        scrollPane.setViewportView(list);
	    }
	 
	 public void updateUsernameLabel(String username) {
	        this.username = username; 
	        this.user = app.buscarUsuarioPorUser(username);
	 }
	
	public void editarPlaylist() throws SelectException, UpdateException, InsertException {
		    if (edit == true) {
		        btnEditar.setText("Editar");
		        edit = false;
		        if (user.getPlaylists().indexOf(playlist) != 0) {
		            String novoNome = textFieldNome.getText();
		            playlist.setNome(novoNome);
		            app.editarPlaylist(playlist);
		        }
		   
		        playlist.getMusicaPlayList().clear();
		        Component[] components = panelCheckBoxes.getComponents();
		        for (Component component : components) {
		            if (component instanceof JCheckBox) {
		                JCheckBox checkBox = (JCheckBox) component;

		                if (checkBox.isSelected()) {
		                    String musicaSelecionada = checkBox.getText();
		                    Musica musica = app.buscarMusicaPorNome(musicaSelecionada);
		                        playlist.adicionarMusicaPlaylist(musica);
		                }
		            }
		        }
		        for(Musica musica : playlist.getMusicaPlayList()) {
		        	pm.adicionarMusicaPlaylist(playlist.getIdPlaylist(), musica.getIdMusica()); 
		        }
		        textFieldNome.setEditable(false);
		        updatePlaylist(playlist);
		        scrollPane.setViewportView(list);
		    } else {
		        btnEditar.setText("Salvar");
		        edit = true;
		        if (user.getPlaylists().indexOf(playlist) != 0) {
		            textFieldNome.setEditable(true);
		        }
		        panelCheckBoxes = new JPanel();
		        panelCheckBoxes.setLayout(new GridLayout(0, 1));
		        panelCheckBoxes.removeAll();
		        for (Musica musica : app.getMusicasSistema()) {
		            JCheckBox checkBoxMusica = new JCheckBox(musica.getNome());
		            panelCheckBoxes.add(checkBoxMusica);
		            if (playlist.getMusicaPlayList().contains(musica)) {
		                checkBoxMusica.setSelected(true);
		            }
		        }
		       // playlist.getMusicaPlayList().clear();
		        scrollPane.setViewportView(panelCheckBoxes);
		    }
	}
}