package apresentacaoMenuSpootiCadastros;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import negocio.*;
import persistencia.ArtistasMusicasDAO;
import dados.*;
import excecoes.InsertException;
import excecoes.SelectException;
import apresentacaoMenuSpooti.*; 



public class CadastrarArtista extends JPanel {
	private JTextField textFieldArtistaNome;
	private JTextField textFieldArtistaIdade;
	private Aplicativo app; 
	private JPanel panelCheckBoxes; 
	private JLabel errorLabel; 
	private ArtistasMusicasDAO adicionarMusicasDAO; 
	private JPanel cards; 
	private CardLayout cardLayout; 
	
	public CadastrarArtista(CardLayout cardLayout, JPanel cards, Listas lista) throws ClassNotFoundException, SQLException, SelectException {
		adicionarMusicasDAO = ArtistasMusicasDAO.getInstance();
		app = Aplicativo.getInstance();
		this.cards = cards;
		this.cardLayout = cardLayout; 
		setLayout(null);
		
		
		JLabel lblNovaPlaylist = new JLabel("Novo Artista");
		lblNovaPlaylist.setHorizontalAlignment(SwingConstants.CENTER);
		lblNovaPlaylist.setBounds(150, 32, 133, 15);
		add(lblNovaPlaylist);
		
		JLabel lblNomeMusica = new JLabel("Nome:");
		lblNomeMusica.setBounds(80, 76, 52, 15);
		add(lblNomeMusica);
		
		JLabel lblGeneroMusica = new JLabel("Idade:");
		lblGeneroMusica.setBounds(80, 113, 62, 15);
		add(lblGeneroMusica);
		
		JLabel lblArtistasMusica = new JLabel("Músicas:");
		lblArtistasMusica.setBounds(80, 140, 62, 15);
		add(lblArtistasMusica);
		
		errorLabel = new JLabel("");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setBounds(80, 272, 290, 15);
		add(errorLabel);
		
		textFieldArtistaNome = new JTextField();
		textFieldArtistaNome.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldArtistaNome.setBounds(150, 74, 146, 19);
		add(textFieldArtistaNome);
		textFieldArtistaNome.setColumns(10);
		
		textFieldArtistaIdade = new JTextField();
		textFieldArtistaIdade.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldArtistaIdade.setColumns(10);
		textFieldArtistaIdade.setBounds(150, 111, 146, 19);
		add(textFieldArtistaIdade);
		
		JScrollPane panelMusicas = new JScrollPane();
		panelMusicas.setBounds(150, 143, 146, 117);

		panelCheckBoxes = new JPanel();
		panelCheckBoxes.setLayout(new GridLayout(0, 1));

		panelMusicas.setViewportView(panelCheckBoxes);
		add(panelMusicas);
		
		// CADASTRA ARTISTA
		JButton btnCriarArtista = new JButton("Criar");
		btnCriarArtista.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textFieldArtistaNome.getText().isEmpty() || textFieldArtistaIdade.getText().isEmpty()) {
					errorLabel.setForeground(Color.red); 
					errorLabel.setText("Todos os campos devem ser preenchidos");
				}else {
				
				Artista artista = new Artista(); 
				artista.setNome(textFieldArtistaNome.getText());
				try {
					 artista.setIdade(Integer.parseInt(textFieldArtistaIdade.getText()));
					 Component[] components = panelCheckBoxes.getComponents();
					    for (Component component : components) {
					        if (component instanceof JCheckBox) {
					            JCheckBox checkBox = (JCheckBox) component;
					             if (checkBox.isSelected()) {
					                String musicaSelecionada = checkBox.getText();
					                Musica musica =  app.buscarMusicaPorNome(musicaSelecionada);
					                if (musica != null) {
					                	  artista.adicionarMusicasProduzidas(musica);
					                	}
					                }
					            }
					        }
						// se passar 
						if(app.adicionarArtista(artista) == true) { 
							for(Musica musica : artista.getMusicasProduzidas()) {
								// adiciona na tabela de relacionamento a lista de musicas produzidas!
								adicionarMusicasDAO.adicionarArtistaMusica(artista.getIdArtista(), musica.getIdMusica()); 
							}
							textFieldArtistaNome.setText(null); 
							textFieldArtistaIdade.setText(null); 
							for(Component component : components) {
							      if (component instanceof JCheckBox) {
							          JCheckBox checkBox = (JCheckBox) component;
							          checkBox.setSelected(false); 
							     }
							} 
							errorLabel.setForeground(Color.green); 
							lista.updateTipo(2, null);
							errorLabel.setText("Artista cadastrado com sucesso."); 
					}
				}catch(NumberFormatException f) {
					errorLabel.setForeground(Color.red); 
					errorLabel.setText("Idade deve ser um número."); 
				   } catch (SelectException e1) {
					e1.printStackTrace();
				} catch (InsertException e1) {
					e1.printStackTrace();
				}
			   }
			}
		});
		
		btnCriarArtista.setBounds(253, 299, 117, 30);
		add(btnCriarArtista);
		
		
		// volta para o menu anterior 
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldArtistaNome.setText(null);
				textFieldArtistaIdade.setText(null);
				errorLabel.setText(null);
				cardLayout.show(cards, "Listas"); 
			}
		});
		btnVoltar.setFont(new Font("Dialog", Font.BOLD, 12));
		btnVoltar.setBounds(80, 299, 87, 30);
		add(btnVoltar);
		
	}
	
	// da update nas músicas disponiveis para ele dar check 
	public void updateListaMusica() {
		panelCheckBoxes.removeAll();
		 for (Musica musica : app.getMusicasSistema()) {
			    JCheckBox checkBoxMusica = new JCheckBox(musica.getNome());
			    panelCheckBoxes.add(checkBoxMusica);
			}
	 }
	
	// reseta a label de erro 
	public void nullErrorLabel() {
        this.errorLabel.setText(null);
    }

}
