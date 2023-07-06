package apresentacaoMenuSpooti;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import negocio.Aplicativo;

import java.awt.CardLayout;
import java.awt.Color;

import dados.*;
import excecoes.SelectException;
import excecoes.UpdateException;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class VerUser extends JPanel {
	private JTextField textFieldUser;
	private JTextField textFieldNome;
	private JTextField textFieldEmail;
	private JPasswordField passwordFieldSenha;
	private Aplicativo app = Aplicativo.getInstance(); 
	private Boolean edit = false; 
	private JButton btnEditar;
	private JLabel error; 
	private CardLayout cardLayout; 
	private JPanel cards; 
	
	public VerUser(CardLayout cardLayout, JPanel cards) throws ClassNotFoundException, SQLException, SelectException {
		setLayout(null);
		this.cards = cards;
		this.cardLayout = cardLayout; 

		JLabel lblPerfilDoUsurio = new JLabel("Perfil do usuário");
		lblPerfilDoUsurio.setHorizontalAlignment(SwingConstants.CENTER);
		lblPerfilDoUsurio.setBounds(172, 31, 133, 15);
		add(lblPerfilDoUsurio);

		
		JLabel lblUser = new JLabel("User:");
		lblUser.setBounds(108, 88, 38, 15);
		add(lblUser);
		
		JLabel lblNome = new JLabel("Nome: ");
		lblNome.setBounds(108, 125, 49, 15);
		add(lblNome);
		
		JLabel lblSenha = new JLabel("Senha: ");
		lblSenha.setBounds(108, 165, 63, 15);
		add(lblSenha);
		
		JLabel lblEmail = new JLabel("Email: ");
		lblEmail.setBounds(108, 217, 49, 19);
		add(lblEmail);
		
		textFieldUser = new JTextField();
		textFieldUser.setEditable(false);
		textFieldUser.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldUser.setBounds(175, 88, 130, 19);
		add(textFieldUser);
		textFieldUser.setColumns(10);
		
		textFieldNome = new JTextField();
		textFieldNome.setEditable(false);
		textFieldNome.setBounds(175, 123, 130, 19);
		add(textFieldNome);
		textFieldNome.setColumns(10);
		
		passwordFieldSenha = new JPasswordField();
		passwordFieldSenha.setEditable(false);
		passwordFieldSenha.setBounds(175, 163, 133, 19);
		add(passwordFieldSenha);
		
		textFieldEmail = new JTextField();
		textFieldEmail.setEditable(false);
		textFieldEmail.setBounds(175, 217, 133, 19);
		add(textFieldEmail);
		textFieldEmail.setColumns(10);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edit = false; 
				textFieldNome.setEditable(false);
		        textFieldEmail.setEditable(false);    
		        passwordFieldSenha.setEditable(false); 
		        btnEditar.setText("Editar");
				error.setText(null);
				cardLayout.show(cards, "Home");
			}
		});

		btnVoltar.setBounds(43, 303, 103, 25);
		add(btnVoltar);
		
		btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					editarUser();
				} catch (UpdateException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnEditar.setBounds(294, 303, 117, 25);
		add(btnEditar);
		
		error = new JLabel("");
		error.setHorizontalAlignment(SwingConstants.CENTER);
		error.setBounds(86, 263, 298, 15);
		add(error);
		
	}
	
	public void updateUser() {
		textFieldUser.setText(app.getUsuario().getUser());
		textFieldNome.setText(app.getUsuario().getNome());
		textFieldEmail.setText(app.getUsuario().getEmail());
		passwordFieldSenha.setText(app.getUsuario().getSenha().toString());
	}
	
	public void editarUser() throws UpdateException {
		if (edit == true) {
	        if(textFieldNome.getText().isEmpty() || textFieldEmail.getText().isEmpty() || passwordFieldSenha.getPassword().length == 0) {
	        	error.setForeground(Color.red);
	        	error.setText("Preencha todos os campos a serem editados");
	        }else {
	        	btnEditar.setText("Editar");
		        edit = false;
	        	error.setText(null);
		        String novoNome = textFieldNome.getText();
	            char[] novaSenha = passwordFieldSenha.getPassword();
	            String novoEmail= textFieldEmail.getText(); 
	            
	            app.getUsuario().setNome(novoNome);
	            app.getUsuario().setSenha(novaSenha);
	            app.getUsuario().setEmail(novoEmail);
	            
	            app.editarUser();

		        textFieldNome.setEditable(false);
		        textFieldEmail.setEditable(false);    
		        passwordFieldSenha.setEditable(false); 
	        }
	       } else {
	    	btnEditar.setText("Salvar");
            edit = true;
            
            textFieldNome.setEditable(true);
	        textFieldEmail.setEditable(true);    
	        passwordFieldSenha.setEditable(true); 
	    }
		
	}
}