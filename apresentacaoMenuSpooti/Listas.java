package apresentacaoMenuSpooti;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import negocio.Aplicativo;
import dados.Artista;
import dados.Musica;
import dados.Playlist;
import excecoes.DeleteException;
import excecoes.SelectException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import apresentacaoMenuSpootiCadastros.*;
import dados.Usuario; 

public class Listas extends JPanel {
    private Aplicativo app = Aplicativo.getInstance();
    private int tipo;
    private JButton btnAdicionar;
    private JLabel lblPlaceholderTitulo;
    private JList<?> list;
    private JPanel cards;
    private CardLayout cardLayout;
    private JScrollPane scrollPane;

    public Listas(CardLayout cardLayout, JPanel cards) throws ClassNotFoundException, SQLException, SelectException {
        setLayout(null);
        this.cards = cards;
        this.cardLayout = cardLayout;

        scrollPane = new JScrollPane();
        scrollPane.setBounds(52, 86, 313, 158);
        add(scrollPane);

        btnAdicionar = new JButton("placeholder adicionar");
        btnAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch (tipo) {
                    case 1:
                        adicionarPlaylist();
                        break;
                    case 2:
                        adicionarArtista();
                        break;
                    case 3:
                        adicionarMusica();
                        break;
                    default:
                        break;
                }
            }
        });
        btnAdicionar.setFont(new Font("Dialog", Font.BOLD, 9));
        btnAdicionar.setBounds(148, 283, 117, 25);
        add(btnAdicionar);

        lblPlaceholderTitulo = new JLabel("placeholder titulo");
        lblPlaceholderTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblPlaceholderTitulo.setBounds(116, 42, 179, 15);
        add(lblPlaceholderTitulo);

        JLabel lblMensagemUsuario = new JLabel("");
        lblMensagemUsuario.setBounds(134, 293, 151, 15);
        add(lblMensagemUsuario);
    }

    
    // FUNÇÕES
    
    public void updateTipo(int tipo, String user) throws SelectException {
        this.tipo = tipo;
        switch (tipo) {

            case 1:
            	
                btnAdicionar.setText("Nova playlist");
                lblPlaceholderTitulo.setText("Playlists");
                app.updateListaPlaylistUsers(app.getUsuario());
                DefaultListModel<Playlist> playlists = new DefaultListModel<Playlist>();
                for (Playlist playlist : app.getUsuario().getPlaylists()) {
                    playlists.addElement(playlist);
                }
                list = new JList<>(playlists);
                list.addListSelectionListener(new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            Object selectedValue = list.getSelectedValue();
                            VerPlaylist playlist = (VerPlaylist) cards.getComponent(5); 	
                            try {
								playlist.updatePlaylist((Playlist) selectedValue);
							} catch (SelectException e1) {
								e1.printStackTrace();
							}
                            cardLayout.show(cards, "Info Playlist");
                        }
                    }
                });
                
                break;

            case 2:
                btnAdicionar.setText("Novo artista");
                lblPlaceholderTitulo.setText("Artistas");
                DefaultListModel<Artista> artistas = new DefaultListModel<Artista>();
                for (Artista artista : app.getArtistasSistema()) {
                    artistas.addElement(artista);
                }
                list = new JList<>(artistas);
                list.addListSelectionListener(new ListSelectionListener() {
                    
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            Object selectedValue = list.getSelectedValue();
                            VerArtista artista = (VerArtista) cards.getComponent(4); 	
                            try {
								artista.updateArtista((Artista) selectedValue);
							} catch (SelectException | DeleteException e1) {
								e1.printStackTrace();
							}
                            cardLayout.show(cards, "Info Artista");
                        }
                    }
                });
                
                break;

            case 3:
                btnAdicionar.setText("Nova música");
                lblPlaceholderTitulo.setText("Músicas");

                DefaultListModel<Musica> musicas = new DefaultListModel<Musica>();
                
                
                for (Musica musica : app.getMusicasSistema()) {
                    musicas.addElement(musica);
                }
                list = new JList<>(musicas);
                //list.setCellRenderer(new MusicListRenderer());

                list.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //if (e.getClickCount() ==1 && e.getButton() == MouseEvent.BUTTON1 && !isClickOnButton(e)) {
                          //  int index = list.locationToIndex(e.getPoint());
                            //Musica musica = musicas.getElementAt(index);
                         //   tocarPreview(musica);
                       // } else if (e.getClickCount() == 2) {
                            int index = list.locationToIndex(e.getPoint());
                            Musica musica = musicas.getElementAt(index);
                            VerMusica telaInfoMusica = (VerMusica) cards.getComponent(3);
                            try {
								telaInfoMusica.updateMusica(musica);
							} catch (SelectException | DeleteException e1) {
								e1.printStackTrace();
							}
                            cardLayout.show(cards, "Info Música");
                        //}
                    }

                    private boolean isClickOnButton(MouseEvent e) {
                        int index = list.locationToIndex(e.getPoint());
                        Rectangle cellBounds = list.getCellBounds(index, index);
                        MusicListRenderer renderer = (MusicListRenderer) list.getCellRenderer(); 
                        Component component = renderer.getListCellRendererComponent(
                                list, list.getModel().getElementAt(index), index, false, false);

                        if (component instanceof Container) {
                            Container container = (Container) component;
                            Component componentAtPoint = SwingUtilities.getDeepestComponentAt(container, e.getX() - cellBounds.x, e.getY() - cellBounds.y);
                            return componentAtPoint instanceof JButton;
                        }

                        return false;
                    }
                });
                break;
        }
        
        scrollPane.setViewportView(list);
    }

    private void adicionarPlaylist() {
    	CadastrarPlaylist c = (CadastrarPlaylist) cards.getComponent(8); 
    	c.updateListaMusicas();
        cardLayout.show(cards, "Cadastrar Playlist");
    }

    private void adicionarArtista() {
    	CadastrarArtista c = (CadastrarArtista) cards.getComponent(6); 
    	c.updateListaMusica();
        cardLayout.show(cards, "Cadastrar Artista");
    }

    private void adicionarMusica() {
    	CadastrarMusica c = (CadastrarMusica) cards.getComponent(7); 
    	c.updateListaArtistas();
        cardLayout.show(cards, "Cadastrar Música");
    }
    public class MusicListRenderer extends DefaultListCellRenderer {
        
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(component, BorderLayout.CENTER);

            if (value instanceof Musica) {
                JButton btnPreview = new JButton("Preview");
                panel.add(btnPreview, BorderLayout.EAST);

                btnPreview.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Musica musica = (Musica) value;
                        tocarPreview(musica);
                    }
                });
            }

            return panel;
        }
        
    }
    
    // toca preview da música 
    private void tocarPreview(Musica musica) {
	    Thread thread = new Thread(new Runnable() {
	        public void run() {
	    	try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(musica.getMusica());
				Player player = new Player(byteArrayInputStream);
				player.play(300);
				} catch (JavaLayerException f) {
				f.printStackTrace();
				} 
	    	}
	       });
	    	  thread.start();
	    
    }
    
    
}