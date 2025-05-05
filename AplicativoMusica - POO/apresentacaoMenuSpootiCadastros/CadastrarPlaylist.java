package apresentacaoMenuSpootiCadastros;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import negocio.*;
import persistencia.PlaylistDAO;
import persistencia.PlaylistMusicasDAO;
import dados.*;
import excecoes.InsertException;
import excecoes.SelectException;
import apresentacaoMenuSpooti.*;
import javax.swing.JScrollPane; 



public class CadastrarPlaylist extends JPanel {
	private JTextField textFieldPlaylistNome;
	private Aplicativo app; 
	private JPanel panelCheckBoxes; 
    private PlaylistDAO playlistDAO; 
	private JLabel errorLabel; 
	private PlaylistMusicasDAO adicionarMusica;
	private CardLayout cardLayout; 
	private JPanel cards; 
	
	public CadastrarPlaylist(CardLayout cardLayout, JPanel cards, Listas lista) throws ClassNotFoundException, SQLException, SelectException {
		this.cardLayout = cardLayout;
		this.cards = cards; 
		app = Aplicativo.getInstance();
		playlistDAO = PlaylistDAO.getInstance(); 
		adicionarMusica = PlaylistMusicasDAO.getInstance(); 
		setLayout(null);
		
		JLabel lblNovaPlaylist = new JLabel("Nova Playlist");
		lblNovaPlaylist.setHorizontalAlignment(SwingConstants.CENTER);
		lblNovaPlaylist.setBounds(149, 35, 133, 15);
		add(lblNovaPlaylist);
		
		JLabel lblNomePlaylist = new JLabel("Nome:");
		lblNomePlaylist.setHorizontalAlignment(SwingConstants.CENTER);
		lblNomePlaylist.setBounds(68, 76, 52, 15);
		add(lblNomePlaylist);
		
		JLabel lblMusicalPlaylist = new JLabel("Músicas:");
		lblMusicalPlaylist.setHorizontalAlignment(SwingConstants.CENTER);
		lblMusicalPlaylist.setBounds(68, 103, 62, 15);
		add(lblMusicalPlaylist);
		
		textFieldPlaylistNome = new JTextField();
		textFieldPlaylistNome.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldPlaylistNome.setBounds(149, 74, 158, 19);
		add(textFieldPlaylistNome);
		textFieldPlaylistNome.setColumns(10);
		
		JScrollPane panelMusicas = new JScrollPane();
		panelMusicas.setBounds(149, 107, 158, 134);

		panelCheckBoxes = new JPanel();
		panelCheckBoxes.setLayout(new GridLayout(0, 1));

		panelMusicas.setViewportView(panelCheckBoxes);
		add(panelMusicas);

		errorLabel = new JLabel("");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setBounds(79, 263, 290, 15);
		add(errorLabel);
		
		// CRIA NOVA PLAYLIST
		JButton btnCriarPlaylist = new JButton("Criar");
		btnCriarPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textFieldPlaylistNome.getText().isEmpty()) {
					errorLabel.setForeground(Color.red); 
					errorLabel.setText("Insira o nome da playlist");
				}else {
				
				Playlist playlist = new Playlist(); 
				playlist.setNome(textFieldPlaylistNome.getText());
			        
				Component[] components = panelCheckBoxes.getComponents();
			    for (Component component : components) {
			        if (component instanceof JCheckBox) {
			            JCheckBox checkBox = (JCheckBox) component;
			             if (checkBox.isSelected()) {
				                String musicaSelecionada = checkBox.getText();
				                Musica musica =  app.buscarMusicaPorNome(musicaSelecionada);			                
				                playlist.adicionarMusicaPlaylist(musica);     
			                }
			            }
			        }
				try {
					// se passar 
					if(app.getUsuario().adicionarPlaylist(playlist) == true) {
						// insere no banco essa nova playlist
						playlistDAO.inserirPlaylist(app.getUsuario().getIdUser(), playlist); 
						for(Musica musica : playlist.getMusicaPlayList()) {
							// e as músicas armazenadas na lista anterior 
			                adicionarMusica.adicionarMusicaPlaylist(playlist.getIdPlaylist(), musica.getIdMusica());
						}
						// atualiza a lista de playlist daquele user que estão no display
						app.updateListaPlaylistUsers(app.getUsuario());
						
						
						textFieldPlaylistNome.setText(null); 
						for(Component component : components) {
						      if (component instanceof JCheckBox) {
						          JCheckBox checkBox = (JCheckBox) component;
						          checkBox.setSelected(false); 
						     }
						} 
						
						errorLabel.setForeground(Color.green); 
						lista.updateTipo(1, app.getUsuario().getUser());
						errorLabel.setText("Playlist cadastrada com sucesso.");
					}
				} catch (SelectException | InsertException e1) {
					e1.printStackTrace();
				}
			  }
			}
		});
		btnCriarPlaylist.setBounds(257, 290, 117, 25);
		add(btnCriarPlaylist);

		JButton btnNewButton = new JButton("Voltar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldPlaylistNome.setText(null);
				errorLabel.setText(null);
				cardLayout.show(cards, "Listas"); 
			}
		});
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 12));
		btnNewButton.setBounds(79, 290, 89, 25);
		add(btnNewButton);
		
		
	}
	
	
	 public void nullErrorLabel() {
	        this.errorLabel.setText(null);
	    }
	 
	 public void updateListaMusicas() {
		 panelCheckBoxes.removeAll();
		 for (Musica musica : app.getMusicasSistema()) {
			    JCheckBox checkBoxMusica = new JCheckBox(musica.getNome());
			    panelCheckBoxes.add(checkBoxMusica);
			}
	 }
}
