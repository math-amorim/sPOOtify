package apresentacao;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import dados.*;
import excecoes.InsertException;
import excecoes.SelectException;
import negocio.Aplicativo; 

public class TelaCadastrar extends JPanel {
	private JTextField textFieldUser;
	private JTextField textFieldNome;
	private JPasswordField passwordFieldSenha;
	private JTextField textFieldEmail;
	private CardLayout cardLayout; 
	private JPanel cards; 
	private Aplicativo app = Aplicativo.getInstance(); 
	
	public TelaCadastrar(CardLayout cardL, JPanel cards)  throws ClassNotFoundException, SQLException, SelectException {
		setLayout(null);
		this.cardLayout = cardL; 
		this.cards = cards; 
		JLabel errorLabel = new JLabel("");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		JLabel lblCadastrar = new JLabel("Cadastrar");
		lblCadastrar.setHorizontalAlignment(SwingConstants.CENTER);
		lblCadastrar.setFont(new Font("Dialog", Font.BOLD, 15));
		lblCadastrar.setBounds(250, 49, 97, 15);
		add(lblCadastrar);
		
		JLabel lblUser = new JLabel("User:");
		lblUser.setBounds(178, 134, 70, 15);
		add(lblUser);
		
		JLabel lblNome = new JLabel("Nome: ");
		lblNome.setBounds(178, 178, 49, 15);
		add(lblNome);
		
		JLabel lblSenha = new JLabel("Senha: ");
		lblSenha.setBounds(178, 221, 58, 15);
		add(lblSenha);
		
		JLabel lblEmail = new JLabel("Email: ");
		lblEmail.setBounds(178, 263, 53, 15);
		add(lblEmail);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldUser.setText(null); 
				textFieldNome.setText(null); 
				textFieldEmail.setText(null); 
				passwordFieldSenha.setText(null); 
				errorLabel.setText(null); 
				cardLayout.show(cards, "Tela Principal");
			}
		});
		btnVoltar.setBounds(153, 392, 117, 25);
		add(btnVoltar);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Usuario user;
				user = new Usuario();
				if(app.verUser(textFieldUser.getText()) == true){
					errorLabel.setForeground(Color.red); 
					errorLabel.setText("Usuário já existe."); 
				}else{ 
				  if(textFieldUser.getText().isEmpty() || textFieldNome.getText().isEmpty() || textFieldEmail.getText().isEmpty() || passwordFieldSenha.getPassword().length == 0) {
					  errorLabel.setForeground(Color.red);
					  errorLabel.setText("Preencha todos os campos.");
				  }else {
					user.setUser(textFieldUser.getText());
					user.setNome(textFieldNome.getText());
					user.setEmail(textFieldEmail.getText());
					user.setSenha(passwordFieldSenha.getPassword()); 
					Playlist playlist = new Playlist(); 
			        playlist.setNome("Músicas Curtidas");
					try {
						if(app.cadastrarUser(user, playlist)) {
							textFieldUser.setText(null); 
							textFieldNome.setText(null); 
							textFieldEmail.setText(null); 
							passwordFieldSenha.setText(null);
							errorLabel.setForeground(Color.green); 
							errorLabel.setText("User cadastrado com sucesso."); 
						}
					} catch (InsertException | SelectException e1) {
						e1.printStackTrace();
					}
				  }
			    }
			}
		});; 
		btnCadastrar.setBounds(365, 392, 117, 25);
		add(btnCadastrar);
		
		textFieldUser = new JTextField();
		textFieldUser.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldUser.setBounds(250, 132, 130, 19);
		add(textFieldUser);
		textFieldUser.setColumns(10);
		
		textFieldNome = new JTextField();
		textFieldNome.setBounds(250, 176, 130, 19);
		add(textFieldNome);
		textFieldNome.setColumns(10);
		
		passwordFieldSenha = new JPasswordField();
		passwordFieldSenha.setBounds(250, 219, 133, 18);
		add(passwordFieldSenha);
		
		textFieldEmail = new JTextField();
		textFieldEmail.setBounds(250, 261, 133, 19);
		add(textFieldEmail);
		textFieldEmail.setColumns(10);
		
		errorLabel.setBounds(169, 346, 313, 15);
		add(errorLabel);

	}
}
