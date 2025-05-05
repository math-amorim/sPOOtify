package apresentacaoMenuSpooti;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import negocio.Aplicativo;
import persistencia.ArtistasMusicasDAO;
import dados.*;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;
import excecoes.UpdateException;

import java.awt.Color; 

public class VerArtista extends JPanel {

	private JTextField textFieldNome;
	private Aplicativo app =  Aplicativo.getInstance(); 
	private Boolean edit = false; 
	private JButton btnEditar; 
	private JTextField textFieldIdade;
	private JList<Musica> list;
	private JScrollPane scrollPane; 
	private Artista artista; 
	private JPanel panelCheckBoxes; 
	private JButton btnDelete;
	private JPanel cards; 
	private CardLayout cardLayout; 
	private ArtistasMusicasDAO adicionarMusicasArtistaDAO = ArtistasMusicasDAO.getInstance(); 

	
	public VerArtista(CardLayout cardLayout, JPanel cards) throws ClassNotFoundException, SQLException, SelectException {	
		setLayout(null);
		this.cardLayout = cardLayout; 
		this.cards = cards; 
		
		JLabel nomeArtista = new JLabel("Nome: ");
		nomeArtista.setBounds(101, 76, 62, 15);
		add(nomeArtista);
		
		textFieldNome = new JTextField();
		textFieldNome.setBounds(168, 74, 149, 19);
		textFieldNome.setEditable(false);
		textFieldNome.setHorizontalAlignment(SwingConstants.LEFT);
		add(textFieldNome);
		textFieldNome.setColumns(10);
		
		JLabel lblMusicas = new JLabel("Músicas:");
		lblMusicas.setBounds(101, 147, 62, 15);
		add(lblMusicas);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.setBounds(101, 274, 103, 25);
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cards, "Listas");
			}
		});
		add(btnVoltar);
		
		btnEditar = new JButton("Editar");
		btnEditar.setBounds(254, 274, 117, 25);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					editarArtista();
				} catch (InsertException | UpdateException | SelectException | DeleteException e1) {
					e1.printStackTrace();
				} 
			}
		});
		add(btnEditar);
		
		JLabel lblIdade = new JLabel("Idade:");
		lblIdade.setBounds(101, 103, 62, 15);
		add(lblIdade);
		
		textFieldIdade = new JTextField();
		textFieldIdade.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldIdade.setEditable(false);
		textFieldIdade.setColumns(10);
		textFieldIdade.setBounds(168, 105, 149, 19);
		add(textFieldIdade);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(168, 148, 149, 100);
		add(scrollPane);
		
		btnDelete = new JButton("Deletar");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int resposta = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar?", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		        if (resposta == JOptionPane.YES_OPTION) {
		        	try {
						app.removerArtista(artista);
					} catch (DeleteException | SelectException e1) {
						e1.printStackTrace();
					}
		        	cardLayout.show(cards, "Home");
		        }
			}
		});
		btnDelete.setForeground(Color.BLACK);
		btnDelete.setBounds(294, 24, 103, 20);
		add(btnDelete);
		
	}
	
	public void updateArtista(Artista artista) throws SelectException, DeleteException {
		this.artista = artista; 
		textFieldNome.setText(artista.getNome());
		textFieldIdade.setText(Integer.toString(artista.getIdade()));
		app.updateArtistasMusicas();
		
		DefaultListModel<Musica> musicas = new DefaultListModel<>();
        for (Musica musica : artista.getMusicasProduzidas()) {
            musicas.addElement(musica);
        }
        
		list = new JList<>(musicas);
        scrollPane.setViewportView(list);
	}
	
	public void editarArtista() throws SelectException, DeleteException, InsertException, UpdateException {
		if (edit == true) {
	        btnEditar.setText("Editar");
	        edit = false;
	        String novoNome = textFieldNome.getText();
            String novaIdade = textFieldIdade.getText();
	    
			artista.setNome(novoNome);
	        artista.setIdade(Integer.parseInt(novaIdade));
	        app.editarArtista(artista);
	        artista.getMusicasProduzidas().clear(); 
	        
	        textFieldNome.setEditable(false);
	        textFieldIdade.setEditable(false);
	    
			Component[] components = panelCheckBoxes.getComponents();
			for (Component component : components) {
		        if (component instanceof JCheckBox) {
		            JCheckBox checkBox = (JCheckBox) component;
		              
		            if (checkBox.isSelected()) {
		                String musicaSelecionada = checkBox.getText();
		                Musica musica = app.buscarMusicaPorNome(musicaSelecionada);
		                
		                musica.adicionarArtistas(artista);
		                artista.adicionarMusicasProduzidas(musica);
		                
		              } 
		            }
		        }

		    for(Musica m : artista.getMusicasProduzidas()){
		    	  adicionarMusicasArtistaDAO.adicionarArtistaMusica(artista.getIdArtista(), m.getIdMusica());		    	 
		    }
		    
	        textFieldNome.setEditable(false);
	        updateArtista(artista); 
	        scrollPane.setViewportView(list);
	    }else{
	    	btnEditar.setText("Salvar");
            edit = true;
            
            textFieldNome.setEditable(true);
            textFieldIdade.setEditable(true);
            
            panelCheckBoxes = new JPanel();
    		panelCheckBoxes.setLayout(new GridLayout(0, 1));
    		panelCheckBoxes.removeAll();
    		
			for (Musica musica : app.getMusicasSistema()) {
   			    JCheckBox checkBoxMusica = new JCheckBox(musica.getNome());
   			    panelCheckBoxes.add(checkBoxMusica);
   			}
    		
    		scrollPane.setViewportView(panelCheckBoxes);
	    }
	}
}