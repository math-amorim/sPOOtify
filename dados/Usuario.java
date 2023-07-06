package dados;
import java.util.LinkedList;
import java.util.List;

public class Usuario {
    private int idUser;
    private String nome, user, email;
    private char[] senha; 
    private List<Playlist> playlists = new LinkedList<>();    
    
    
    public int getIdUser() {
        return idUser;
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public char[] getSenha() {
        return senha;
    }
    public void setSenha(char[] senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
 
    public boolean adicionarPlaylist(Playlist playlist){ 
        if(playlists.add(playlist)) {
        	return true; 
        }else {
        	return false;
        }
    }

    public void setPlaylists( List<Playlist>  p) {
        this.playlists = p;
    }
    
    public List<Playlist> getPlaylists() {
        return playlists;
    }


    public String toString() {
        return "User: " + user + "\nEmail: " + email + "\n";
    }
   
}
