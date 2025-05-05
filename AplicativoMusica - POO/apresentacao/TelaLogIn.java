package apresentacao;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import apresentacaoMenuSpooti.Listas;
import apresentacaoMenuSpooti.VerUser;
import negocio.Aplicativo;
import persistencia.PlaylistDAO;

import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import excecoes.SelectException;

public class TelaLogIn extends JPanel{
	private JTextField txtInsiraAoUser;
	private CardLayout cardLayout; 
	private JPanel cards;  
	private Aplicativo app = Aplicativo.getInstance(); 
	private PlaylistDAO playlistDAO = PlaylistDAO.getInstance(); 
	private JPasswordField passwordField;
	
	public String getUser() {
		return txtInsiraAoUser.getText(); 
	}
	
	public TelaLogIn(CardLayout cardL, JPanel cards)  throws ClassNotFoundException, SQLException, SelectException {
		setLayout(null);
		this.cards = cards;
		this.cardLayout = cardL; 
		
		JLabel errorLabel = new JLabel("");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblLogIn = new JLabel("Log In");
		lblLogIn.setFont(new Font("Dialog", Font.BOLD, 15));
		lblLogIn.setBounds(270, 70, 59, 15);
		add(lblLogIn);
		
		JLabel lblUser = new JLabel("User: ");
		lblUser.setFont(new Font("Dialog", Font.BOLD, 14));
		lblUser.setBounds(170, 182, 70, 15);
		add(lblUser);
		
		JLabel lblSenha = new JLabel("Senha: ");
		lblSenha.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSenha.setBounds(170, 251, 70, 15);
		add(lblSenha);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtInsiraAoUser.setText(null); 
				passwordField.setText(null);
				errorLabel.setText(null);
				cardLayout.show(cards, "Tela Principal"); 
			}
		});
		btnVoltar.setBounds(133, 399, 117, 25);
		add(btnVoltar);
		
		JButton btnLogIn = new JButton("Log In");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtInsiraAoUser.getText().isEmpty() || passwordField.getPassword().length == 0) {
					errorLabel.setForeground(Color.red);
					errorLabel.setText("Preencha todos os campos"); 
				}else {
				
				String user = txtInsiraAoUser.getText(); 
				char[] senha = passwordField.getPassword(); 
				
				
				if(app.verUser(user) == false){ 
					errorLabel.setForeground(Color.red); 
					errorLabel.setText("Usuário não existe."); 
				}else{ 			
					if(app.autenticarUser(user, senha) == false){ 
						errorLabel.setForeground(Color.red); 
						errorLabel.setText("Senha não coincide.");
					}else{
						if(app.autenticarUser(user, senha) == true) {
							// se logar certinho, seta as playlist do user com todas relacionadas a seu id no banco 
							try {
								app.buscarUsuarioPorUser(user).setPlaylists(playlistDAO.selectAll(app.buscarUsuarioPorUser(user)));
							} catch (SelectException e1) {
								e1.printStackTrace();
							}
							MenuSpooti menuSpootiPanel = (MenuSpooti) cards.getComponent(3);
							menuSpootiPanel.updateUsernameLabel(txtInsiraAoUser.getText());
							
							app.setarUsuario(app.buscarUsuarioPorUser(user));							
							
							txtInsiraAoUser.setText(null); 
							passwordField.setText(null);
							errorLabel.setText(null);
							
							cardLayout.show(cards, "Menu Spootify");
						}
					}
				}
			  }
			}
		});
		btnLogIn.setBounds(354, 399, 117, 25);
		add(btnLogIn);
		
		txtInsiraAoUser = new JTextField();
		txtInsiraAoUser.setBounds(245, 180, 114, 19);
		add(txtInsiraAoUser);
		txtInsiraAoUser.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(245, 249, 114, 19);
		add(passwordField);
		
		errorLabel.setBounds(204, 342, 169, 15);
		add(errorLabel);
	}
}
