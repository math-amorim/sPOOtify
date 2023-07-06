package dados;
import java.util.LinkedList;
import java.util.List;

public class Artista {
    private String nome; 
    private int idade, idArtista; 
    private List<Musica> musicasProduzidas = new LinkedList<Musica>();

    public int getIdArtista() {
        return idArtista;
    }
    public void setIdArtista(int idArtista) {
        this.idArtista = idArtista;
    }
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }
    public void setIdade(int idade) {
        this.idade = idade;
    }
    
    public void setMusicasProduzidas(List<Musica> l) {
    	this.musicasProduzidas = l; 
    }
    
    public List<Musica> getMusicasProduzidas() {
        return musicasProduzidas;
    }
    public void adicionarMusicasProduzidas(Musica musica) {
       musicasProduzidas.add(musica); 
    }
    
    public String toString() {
        return nome;
    } 
    
}
