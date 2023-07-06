package apresentacaoMenuSpootiCadastros;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import negocio.*;
import persistencia.ArtistasMusicasDAO;
import persistencia.PlaylistMusicasDAO;
import dados.*;
import excecoes.InsertException;
import excecoes.SelectException;
import apresentacaoMenuSpooti.*; 


public class CadastrarMusica extends JPanel {
	private JTextField textFieldMusicaNome;
	private JTextField textFieldGeneroMusica;
	private Aplicativo app; 
	private JPanel panelCheckBoxes; 
	private JLabel errorLabel; 
	private String path; 
	private ArtistasMusicasDAO adicionarArtistasDAO; 
	private PlaylistMusicasDAO adicionarMusicaDAO;
	private Musica insertCurtida; 
	private CardLayout cardLayout;
	private JPanel cards; 
	
	public CadastrarMusica(CardLayout cardLayout, JPanel cards, Listas lista) throws ClassNotFoundException, SQLException, SelectException {
		adicionarArtistasDAO = ArtistasMusicasDAO.getInstance();
		adicionarMusicaDAO = PlaylistMusicasDAO.getInstance(); 
		app = Aplicativo.getInstance();
		this.cards = cards;
		this.cardLayout = cardLayout; 
		
		setLayout(null);
		
		JLabel lblNovaPlaylist = new JLabel("Nova Música");
		lblNovaPlaylist.setHorizontalAlignment(SwingConstants.CENTER);
		lblNovaPlaylist.setBounds(152, 34, 133, 15);
		add(lblNovaPlaylist);
		
		JLabel lblNomeMusica = new JLabel("Nome:");
		lblNomeMusica.setBounds(82, 76, 52, 15);
		add(lblNomeMusica);
		
		JLabel lblGeneroMusica = new JLabel("Gênero:");
		lblGeneroMusica.setBounds(82, 103, 62, 15);
		add(lblGeneroMusica);
		
		JLabel lblArtistasMusica = new JLabel("Artistas:");
		lblArtistasMusica.setHorizontalAlignment(SwingConstants.CENTER);
		lblArtistasMusica.setBounds(82, 158, 62, 15);
		add(lblArtistasMusica);

		errorLabel = new JLabel("");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setBounds(82, 276, 298, 15);
		add(errorLabel);
		
		textFieldMusicaNome = new JTextField();
		textFieldMusicaNome.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldMusicaNome.setBounds(152, 74, 148, 19);
		add(textFieldMusicaNome);
		textFieldMusicaNome.setColumns(10);
		
		textFieldGeneroMusica = new JTextField();
		textFieldGeneroMusica.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldGeneroMusica.setColumns(10);
		textFieldGeneroMusica.setBounds(152, 101, 148, 19);
		add(textFieldGeneroMusica);
	
		
		JScrollPane panelArtistas = new JScrollPane();
		panelArtistas.setBounds(152, 159, 148, 109);

		panelCheckBoxes = new JPanel();
		panelCheckBoxes.setLayout(new GridLayout(0, 1));

		panelArtistas.setViewportView(panelCheckBoxes);
		add(panelArtistas);

		JCheckBox favoritar = new JCheckBox("");
		favoritar.setBounds(399, 76, 21, 23);
		add(favoritar);
		
		
		// CADASTRA A NOVA MÚSICA
		JButton btnCriarMusica = new JButton("Criar");
		btnCriarMusica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Musica musica = new Musica(); 
				if(textFieldMusicaNome.getText().isEmpty() || textFieldGeneroMusica.getText().isEmpty() || path.isEmpty()) {
					errorLabel.setForeground(Color.red); 
					errorLabel.setText("Todos os campos devem ser preenchidos");
				}else {
				
				musica.setNome(textFieldMusicaNome.getText());
				musica.setGenero(textFieldGeneroMusica.getText());
				
				File file = new File(path);
				byte[] arquivoBytes = new byte[(int) file.length()];
				try (FileInputStream fileInputStream = new FileInputStream(file)) {
				    fileInputStream.read(arquivoBytes);
				} catch (IOException f) {
				    System.err.println("Erro ao ler o arquivo MP3");
				}
				musica.setMusica(arquivoBytes);
				
				Component[] components = panelCheckBoxes.getComponents();
			    for (Component component : components) {
			        if (component instanceof JCheckBox) {
			            JCheckBox checkBox = (JCheckBox) component;
			             if (checkBox.isSelected()) {
			                String artistaSelecionado = checkBox.getText();
			                Artista artista =  app.buscarArtistaPorNome(artistaSelecionado);
			                if (artista != null) {
			                      musica.adicionarArtistas(artista);
			                	}
			                }
			            }
			        }
				try {  			  
					insertCurtida = null; 
				    if(favoritar.isSelected()) {
				    	insertCurtida = musica; 
				    	favoritar.setSelected(false); 
				    }
				    
					if(app.adicionarMusica(musica)) {
						// se passar, vai adicionar relação entre playlist e a música 
						if(insertCurtida != null) {
							adicionarMusicaDAO.adicionarMusicaPlaylist(app.getUsuario().getPlaylists().get(0).getIdPlaylist(), insertCurtida.getIdMusica());
						}
						app.updateListaPlaylistUsers(app.getUsuario());
						// e tb relacionar o artista à musica, para cada artista registrado anteriormente. 
						for(Artista artista : musica.getArtistas()) {
							adicionarArtistasDAO.adicionarArtistaMusica(artista.getIdArtista(), musica.getIdMusica());
						}
						// e ai reseta campos de texto relaxionados ao cadastro de música
						textFieldMusicaNome.setText(null); 
						textFieldGeneroMusica.setText(null);
					
						for(Component component : components) {
						      if (component instanceof JCheckBox) {
						          JCheckBox checkBox = (JCheckBox) component;
						          checkBox.setSelected(false); 
						     }
						} 
						errorLabel.setForeground(Color.green); 
						lista.updateTipo(3, null);
						errorLabel.setText("Música cadastrada com sucesso."); 
						}
				} catch (InsertException | SelectException| IOException e1) {
					e1.printStackTrace();
					}
				}
			}
		});
		btnCriarMusica.setBounds(263, 297, 117, 25);
		add(btnCriarMusica);
	
		// volta ao menu anterior 
		JButton btnNewButton = new JButton("Voltar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				errorLabel.setText(null);
				cardLayout.show(cards, "Listas"); 
			}
		});
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 12));
		btnNewButton.setBounds(82, 297, 84, 25);
		add(btnNewButton);
		
		JLabel lblPath = new JLabel("Path:");
		lblPath.setBounds(82, 132, 38, 15);
		add(lblPath);
		
        JFrame frame = new JFrame("Escolha Arquivo");
		
		JLabel lblFavoritar = new JLabel("favoritar");
		lblFavoritar.setBounds(336, 78, 70, 15);
		add(lblFavoritar);
		
		// usa para pegar caminho do arquivo 
		JButton buttonPath = new JButton("Arquivo");
		buttonPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				 int result = chooser.showOpenDialog(frame);
		         if (result == JFileChooser.APPROVE_OPTION) {
		        	 File f = chooser.getSelectedFile();
		        	 path = f.getAbsolutePath();
		         }
			}
		});
		buttonPath.setBounds(152, 132, 138, 15);
		add(buttonPath);
	}
	
	// tira o check de cada check box
	public void updateListaArtistas() {
		 panelCheckBoxes.removeAll();
		 for (Artista artista : app.getArtistasSistema()) {
			    JCheckBox checkBoxMusica = new JCheckBox(artista.getNome());
			    panelCheckBoxes.add(checkBoxMusica);
			}
	 }
	
	 public void nullErrorLabel() {
	        this.errorLabel.setText(null);
	    }
}
