package dados;
import java.util.LinkedList;
import java.util.List;


public class Musica {
    private String nome, genero; 
    private int idMusica; 
    private List<Artista> artistas = new LinkedList<Artista>();
    private byte[] musica; 

    public int getIdMusica() {
        return idMusica;
    }
    public void setIdMusica(int idMusica) {
        this.idMusica = idMusica;
    }
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }

    public List<Artista> getArtistas() {
        return artistas;
    }

    public void setArtistas(List<Artista> l) {
    	this.artistas = l; 
    }
    
    public void adicionarArtistas(Artista artista) {
        artistas.add(artista); 
    }
    

    public byte[] getMusica(){
        return musica; 
    }
    public void setMusica(byte[] m){
        this.musica = m; 
    }
    
    public String toString() {
        return nome;
    }  

}
