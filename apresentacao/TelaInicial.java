package apresentacao;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import excecoes.InsertException;
import excecoes.SelectException;
import javazoom.jl.decoder.JavaLayerException;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import java.awt.CardLayout;

public class TelaInicial extends JFrame {
    private CardLayout cardLayout;
	private JPanel contentPane;
	private TelaCadastrar telaCadastrar;
	private TelaLogIn telaLogIn; 
	private MenuSpooti telaSpootify;
	private TelaPrincipal telaPrincipal;
	
	public static void main(String[] args)  throws ClassNotFoundException, SQLException, SelectException, FileNotFoundException, InsertException, JavaLayerException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaInicial frame = new TelaInicial();
					frame.setLocationRelativeTo(null); 
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public TelaInicial()  throws ClassNotFoundException, SQLException, SelectException, FileNotFoundException, InsertException, JavaLayerException {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 486);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		cardLayout = new CardLayout(); 
		contentPane.setLayout(cardLayout);
		
		telaCadastrar = new TelaCadastrar(cardLayout,contentPane); 
		telaLogIn = new TelaLogIn(cardLayout,contentPane); 
		telaPrincipal = new TelaPrincipal(cardLayout,contentPane); 
		telaSpootify = new MenuSpooti(cardLayout,contentPane); 
		
		contentPane.add(telaPrincipal,"Tela Principal");
		contentPane.add(telaCadastrar,"Tela Cadastro");
		contentPane.add(telaLogIn,"Tela Log In");
		contentPane.add(telaSpootify, "Menu Spootify"); 

		
	}
}