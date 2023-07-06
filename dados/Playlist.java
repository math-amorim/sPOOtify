package dados;
import java.util.List; 
import java.util.LinkedList;


public class Playlist {
    private int idPlaylist; 
    private String nome; 
    private List<Musica> musicasPlaylist = new LinkedList<Musica>(); 

    public int getIdPlaylist() {
        return idPlaylist;
    }
    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public List<Musica> getMusicaPlayList(){
        return musicasPlaylist; 
    }
    public void adicionarMusicaPlaylist(Musica musica){
        musicasPlaylist.add(musica); 
    }

    public String toString() {
        return nome;   
    }  

}
