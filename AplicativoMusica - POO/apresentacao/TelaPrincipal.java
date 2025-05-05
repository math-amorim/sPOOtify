package apresentacao;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.CardLayout;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TelaPrincipal extends JPanel {
	private CardLayout cardLayout;
	private JPanel cards; 

	public TelaPrincipal(CardLayout cardL, JPanel cards) {
		setLayout(null);
		this.cardLayout = cardL; 
		this.cards = cards;
		
		JLabel lblSpootify = new JLabel("sPOOtify");
		lblSpootify.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpootify.setFont(new Font("Dialog", Font.BOLD, 16));
		lblSpootify.setBounds(240, 90, 123, 27);
		add(lblSpootify);
		
		JButton btnCriarConta = new JButton("Criar Conta");
		btnCriarConta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cards,"Tela Cadastro"); 
			}
		});
		btnCriarConta.setBounds(240, 200, 117, 25);
		add(btnCriarConta);
		
		JButton btnLogIn = new JButton("Log In");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cards, "Tela Log In");
			}
		});
		btnLogIn.setBounds(240, 260, 117, 25);
		add(btnLogIn);

	}

}
