package apresentacao;

import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


import java.awt.CardLayout;
import java.awt.Cursor;

import javax.swing.JButton;
import java.awt.Font;
import apresentacaoMenuSpooti.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import apresentacaoMenuSpootiCadastros.*; 
import dados.*;
import excecoes.SelectException;
import javazoom.jl.decoder.JavaLayerException;
import negocio.Aplicativo;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MenuSpooti extends JPanel {
	private Aplicativo app = Aplicativo.getInstance(); 
	private CardLayout cardLayoutMenu;
    private JLabel lblUser;
	private Listas lista; 
	private Home home;
	private VerMusica musica; 
	private VerPlaylist playlist; 
	private VerArtista artista; 
	private VerUser userForm; 
    private CadastrarArtista artistaForm; 
    private CadastrarMusica musicaForm; 
    private CadastrarPlaylist playlistForm; 
	private JButton play; 
	private JLabel nomeMusica; 
	private JLabel nomeArtistas;
	private PausablePlayer player;
    private List<Musica> listMusic = new LinkedList<>();  


	
	private Musica music;
	private boolean playing = false; 
	private JButton buttonSkip;
	private JButton btnLogOut;
    

    public MenuSpooti(CardLayout cardL, JPanel panel) throws ClassNotFoundException, SQLException, SelectException, JavaLayerException {
		setLayout(new GridLayout(0, 1, 0, 0));
		
	 
		JLayeredPane painelVarios = new JLayeredPane();
		add(painelVarios);
				
		JPanel menuLateral = new JPanel();
		menuLateral.setBounds(12, 49, 66, 193);
		painelVarios.add(menuLateral);
		menuLateral.setLayout(null);
			
		
		
		JPanel painelPrincipalSPOOtify = new JPanel();
		painelPrincipalSPOOtify.setBounds(84, 43, 465, 344);
		painelVarios.add(painelPrincipalSPOOtify);
		cardLayoutMenu = new CardLayout(); 
		painelPrincipalSPOOtify.setLayout(cardLayoutMenu);
		
		lblUser = new JLabel("user");
		lblUser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				VerUser user = (VerUser) painelPrincipalSPOOtify.getComponent(2); 	
				user.updateUser();
				cardLayoutMenu.show(painelPrincipalSPOOtify, "Formulário Usuário");
			}
		});
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblUser.setBounds(477, 22, 70, 9);
		painelVarios.add(lblUser);
		
		// cria para adicionar ao card 
		lista = new Listas(cardLayoutMenu, painelPrincipalSPOOtify); 
		home = new Home(cardLayoutMenu, painelPrincipalSPOOtify); 
		userForm = new VerUser(cardLayoutMenu, painelPrincipalSPOOtify); 
		musica = new VerMusica(cardLayoutMenu, painelPrincipalSPOOtify); 
		artista = new VerArtista(cardLayoutMenu, painelPrincipalSPOOtify); 
		playlist = new VerPlaylist(cardLayoutMenu, painelPrincipalSPOOtify); 
		artistaForm = new CadastrarArtista(cardLayoutMenu, painelPrincipalSPOOtify, lista); 
		musicaForm = new CadastrarMusica(cardLayoutMenu, painelPrincipalSPOOtify, lista); 
		playlistForm = new CadastrarPlaylist(cardLayoutMenu, painelPrincipalSPOOtify, lista); 
		
		// adiciona ao card pah 
		painelPrincipalSPOOtify.add(home, "Home"); 
		painelPrincipalSPOOtify.add(lista, "Listas"); 
		painelPrincipalSPOOtify.add(userForm, "Formulário Usuário");
		painelPrincipalSPOOtify.add(musica, "Info Música");
		painelPrincipalSPOOtify.add(artista, "Info Artista");
		painelPrincipalSPOOtify.add(playlist, "Info Playlist");
		painelPrincipalSPOOtify.add(artistaForm, "Cadastrar Artista");
		painelPrincipalSPOOtify.add(musicaForm, "Cadastrar Música");
		painelPrincipalSPOOtify.add(playlistForm, "Cadastrar Playlist");

		
		
		JLabel lblHome = new JLabel("Home");
		lblHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblHome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayoutMenu.show(painelPrincipalSPOOtify, "Home");
			}
		});
		lblHome.setHorizontalAlignment(SwingConstants.CENTER);
		lblHome.setFont(new Font("Dialog", Font.BOLD, 11));
		lblHome.setBounds(12, 25, 42, 15);
		menuLateral.add(lblHome);
		
		JLabel lblPlaylists = new JLabel("Playlists");
		lblPlaylists.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblPlaylists.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				Listas lista = (Listas) painelPrincipalSPOOtify.getComponent(1);
				try {
					lista.updateTipo(1, lblUser.getText());
				} catch (SelectException e1) {
					e1.printStackTrace();
				} 
				CadastrarMusica cm = (CadastrarMusica) painelPrincipalSPOOtify.getComponent(7);
				cm.nullErrorLabel();
				CadastrarArtista ca = (CadastrarArtista) painelPrincipalSPOOtify.getComponent(6);
				ca.nullErrorLabel();
				CadastrarPlaylist cp = (CadastrarPlaylist) painelPrincipalSPOOtify.getComponent(8);
				cp.nullErrorLabel();
				cardLayoutMenu.show(painelPrincipalSPOOtify, "Listas");
			}
		});
		lblPlaylists.setFont(new Font("Dialog", Font.BOLD, 11));
		lblPlaylists.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlaylists.setBounds(0, 65, 70, 15);
		menuLateral.add(lblPlaylists);
		
		JLabel lblArtistas = new JLabel("Artistas");
		lblArtistas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblArtistas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Listas lista = (Listas) painelPrincipalSPOOtify.getComponent(1);
				try {
					lista.updateTipo(2, lblUser.getText());
				} catch (SelectException e1) {
					e1.printStackTrace();
				} 
				CadastrarMusica cm = (CadastrarMusica) painelPrincipalSPOOtify.getComponent(7);
				cm.nullErrorLabel();
				CadastrarPlaylist cp = (CadastrarPlaylist) painelPrincipalSPOOtify.getComponent(8);
				cp.nullErrorLabel();
				CadastrarArtista ca = (CadastrarArtista) painelPrincipalSPOOtify.getComponent(6);
				ca.nullErrorLabel();
				cardLayoutMenu.show(painelPrincipalSPOOtify,  "Listas");
			}
		});
		lblArtistas.setFont(new Font("Dialog", Font.BOLD, 11));
		lblArtistas.setHorizontalAlignment(SwingConstants.CENTER);
		lblArtistas.setBounds(0, 140, 70, 15);
		menuLateral.add(lblArtistas);
		
		JLabel lblMsicas = new JLabel("Músicas");
		lblMsicas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblMsicas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Listas lista = (Listas) painelPrincipalSPOOtify.getComponent(1);
				try {
					lista.updateTipo(3, lblUser.getText());
				} catch (SelectException e1) {
					e1.printStackTrace();
				} 
				CadastrarMusica cm = (CadastrarMusica) painelPrincipalSPOOtify.getComponent(7);
				cm.nullErrorLabel();
				CadastrarArtista ca = (CadastrarArtista) painelPrincipalSPOOtify.getComponent(6);
				ca.nullErrorLabel();
				CadastrarPlaylist cp = (CadastrarPlaylist) painelPrincipalSPOOtify.getComponent(8);
				cp.nullErrorLabel();
				cardLayoutMenu.show(painelPrincipalSPOOtify, "Listas"); 
			}
		});
		lblMsicas.setFont(new Font("Dialog", Font.BOLD, 11));
		lblMsicas.setHorizontalAlignment(SwingConstants.CENTER);
		lblMsicas.setBounds(0, 105, 70, 15);
		menuLateral.add(lblMsicas);
		
		
		
		JPanel abaPlayer = new JPanel();
		abaPlayer.setBounds(57, 399, 491, 46);
		painelVarios.add(abaPlayer);
		abaPlayer.setLayout(null);
		
		play = new JButton("▷");
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				play.setText("||");
				player(music); 
			}
		});
		play.setBounds(224, 12, 45, 27);
		play.setFont(new Font("Dialog", Font.BOLD, 14));
		abaPlayer.add(play);
		
		nomeMusica = new JLabel("");
		nomeMusica.setBounds(12, 12, 132, 15);
		abaPlayer.add(nomeMusica);
		
		nomeArtistas = new JLabel("");
		nomeArtistas.setBounds(12, 24, 183, 15);
		abaPlayer.add(nomeArtistas);
		
		buttonSkip = new JButton(">>|");
		buttonSkip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.stop(); 
				if(listMusic.indexOf(music) == listMusic.size()-1) {
					nomeMusica.setText(null);
					nomeArtistas.setText(null);
					listMusic.clear();  
					player.stop(); 
				}else {
					player.stop(); 
					tocarMusica(listMusic.get(listMusic.indexOf(music)+1)); 
				}
			}
		});
		buttonSkip.setBounds(315, 13, 58, 25);
		abaPlayer.add(buttonSkip);
		
		btnLogOut = new JButton("Sair");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.deslogarUsuario();
				updateUsernameLabel(null); 
				cardLayoutMenu.show(painelPrincipalSPOOtify, "Home");
				try {
					lista.updateTipo(0, null);
				} catch (SelectException e1) {
					e1.printStackTrace();
				}
				cardL.show(panel, "Tela Principal");
			}
		});
		btnLogOut.setBounds(12, 18, 66, 17);
		painelVarios.add(btnLogOut);
	     
		

	}
	
	public void tocarPlaylist(List<Musica> musicas){
		 	this.listMusic = musicas;
		    if (listMusic.isEmpty()) {
		        return;
		    }

		    tocarMusica(listMusic.get(0));
	}
	
	
	public void tocarMusica(Musica musica){
		if(playing) {
			player.stop(); 
		}
		this.music = musica;
	    nomeMusica.setText(musica.getNome());

	    StringBuilder str = new StringBuilder();
	    for (Artista artista : musica.getArtistas()) {
	        str.append(artista.getNome()).append(", ");
	    }
	    if (str.length() > 2) {
	        str.setLength(str.length() - 2);
	    }
	    String artistas = str.toString();
	    nomeArtistas.setText(artistas);

	    playing = true;
	    play.setText("||");

	    Thread thread = new Thread(new Runnable() {
	        public void run() {
	            try {
	            	ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(musica.getMusica());
	                player = new PausablePlayer(byteArrayInputStream);
	                player.play();
	            } catch (JavaLayerException f) {
	                f.printStackTrace();
	            }
	        }
	    });
	    thread.start();
	}
	
	public void player(Musica musica) {
	    if (playing) {
	        play.setText("▷");
            player.pause();   
            playing = false;
	    } else {
	        play.setText("||");
	        player.resume(); 
	        playing = true;
	    }
	}
	
	 public void updateUsernameLabel(String username) {
	        lblUser.setText(username);	   
	        playlist.updateUsernameLabel(username);
	    }
	 public String getUsernameLabel() {
	       return  lblUser.getText();
	    }
	 public List<Musica> getTrackList(){
		 return this.listMusic; 
	 }
	 
}