package negocio;
import dados.*;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;
import excecoes.UpdateException;
import persistencia.*;

import java.util.List;
import java.io.IOException;
import java.sql.SQLException;


public class Aplicativo {
    private Usuario usuarioAtual; 
	private static Aplicativo instance = null; 
    private UserDAO userDAO; 
    private PlaylistDAO playlistDAO; 
    private ArtistaDAO artistaDAO;
    private MusicaDAO musicasDAO; 
    private PlaylistMusicasDAO playlistMusicasDAO; 
    private ArtistasMusicasDAO artistasMusicasDAO;
    private List<Usuario>usuariosSistema;
    private List<Musica>musicasSistema; 
    private List<Artista>artistasSistema;


    public Aplicativo(String senha) throws ClassNotFoundException, SQLException, SelectException {
        Conexao.setSenha(senha);
        userDAO = UserDAO.getInstance(); 
        playlistDAO = PlaylistDAO.getInstance(); 
        musicasDAO = MusicaDAO.getInstance(); 
        artistaDAO = ArtistaDAO.getInstance(); 
        playlistMusicasDAO = PlaylistMusicasDAO.getInstance(); 
        artistasMusicasDAO = ArtistasMusicasDAO.getInstance(); 
        
        usuariosSistema = userDAO.selectAll(); 
        musicasSistema = musicasDAO.getAllMusicasSistema(); 
        artistasSistema = artistaDAO.selectAll(); 
    }


	public static Aplicativo getInstance() throws ClassNotFoundException, SQLException, SelectException {
        if(instance == null){
            instance = new Aplicativo("admin"); 
        }
        return instance; 
    }
	
	
    // SESSÃO DE USERS
    // Verifica se nome de usuário o user do usuário já existe. O lance é que este será o nosso codigo de identificação único. 
    public boolean verUser(String nome){
        for(Usuario usuario : usuariosSistema){
            if(usuario.getUser().equals(nome)){
                return true; 
            }
        }
            return false; 
    }

    public void editarUser() throws UpdateException {
    	userDAO.update(usuarioAtual);
    }
    
    // Serve para cadastrar o user. Primeiro usa a função verUser para verificar se já não existe nome de usuário cadastrado. Se não, ele vai adicionar o usuário ao sistema. 
    public boolean cadastrarUser(Usuario user, Playlist playlist) throws InsertException, SelectException{         
        userDAO.inserirUser(user, playlist);
        updateListaUsers(); 
        return true; 
    }

    // realiza a busca de um usuário pelo user. Se existir
    public Usuario buscarUsuarioPorUser(String user){ 
        for (Usuario usuario : usuariosSistema) {
            if (usuario.getUser().equals(user)) {
                return usuario;
            }
        }
        return null; 
    }
    
    // Serve para autenticar o log in do usuário. 
    public boolean autenticarUser(String user, char[] senha){ 
            Usuario userValidar = buscarUsuarioPorUser(user); 
            String senhaValidar = new String(userValidar.getSenha()); 
            if(!(senhaValidar.equals(new String(senha)))){
                return false; 
            }else{
                setarUsuario(userValidar);
                return true;    
            }
    }

    // Serve para retornar lista de user do sistema 
    public List<Usuario> getUsuariosSistema() throws SelectException{
        return usuariosSistema; 
    }

    // Edita playlist 
    public void editarPlaylist(Playlist p) throws UpdateException {
    	playlistDAO.update(p);
    }
    
    // Remove playlist única à user
    public void removerPlaylist(Playlist p) throws DeleteException{ 
        playlistMusicasDAO.removerPlaylist(p);
        playlistDAO.removerPlaylist(p);
    }
   
    // SESSÃO DE MÚSICAS
    
    // adiciona música à músicas do sistema 
    public boolean adicionarMusica(Musica musica) throws InsertException, SelectException, IOException{ 
        if(musicasDAO.inserirMusica(musica)) {
            updateListaMusicas(); 
        	return true;
        }else {
        	return false; 
        }
    }
    
    // edita a música
    public void editarMusica(Musica m) throws UpdateException, IOException {
    	musicasDAO.updateMusica(m);
    }

    // retorna dados da música usando o seu nome como parametro de busca 
    public Musica buscarMusicaPorNome(String nome){ 
        for (Musica musica : musicasSistema) {
            if (musica.getNome().equals(nome)) {
                return musica;
            }
        }
        return null; 
    }
    // retorna a lista de músicas cadastradas no sistema 
    public List<Musica> getMusicasSistema() {
        return musicasSistema;
    }

    // remove música do sistema 
    public void removerMusica(Musica m) throws DeleteException, SelectException{ 
       artistasMusicasDAO.removerMusica(m);
       playlistMusicasDAO.removerMusicaPlaylist(m);
       musicasDAO.removerMusica(m);
       updateListaMusicas(); 
    }

    // SESSÃO DE ARTISTAS
    // boolean que serve para verificar se um nome já existe antes de cadastrar o Artista ao sistema 
    public boolean verArtista(String nome, List<Artista>lista){
        for(Artista artista : lista){
            if(artista.getNome().equals(nome)){
                return true; 
            }
        }
            return false; 
    }

    // retorna informações de um artista usando nome como campo chave 
    public Artista buscarArtistaPorNome(String nome){ 
        for (Artista artista : artistasSistema) {
            if (artista.getNome().equals(nome)) {
                return artista;
            }
        }
        return null; 
    }
    
    // adiciona o artista ao sistema 
    public boolean adicionarArtista(Artista artista) throws SelectException, InsertException{ 
        if(verArtista(artista.getNome(), artistasSistema) ==  true){
            return false;
        }else{
            artistaDAO.inserirArtista(artista);
            updateListaArtistas(); 
            return true; 
        } 
    } 

    // edita o artista na DAO
    public void editarArtista(Artista a) throws UpdateException {
    	artistaDAO.update(a);
    }
    
    // retorna lista dos artistas já cadastrados no sistema 
    public List<Artista> getArtistasSistema() {
        return artistasSistema;
    }

    // remove artista cadastrado no sistema
    public void removerArtista(Artista a) throws DeleteException, SelectException{ 
        artistasMusicasDAO.removerArtista(a);
        artistaDAO.removerArtista(a);
        updateListaArtistas();
    }

    public void updateListaUsers() throws SelectException{ 
        usuariosSistema = userDAO.selectAll(); 
    }
    
    public void updateListaMusicas() throws SelectException{
        musicasSistema = musicasDAO.getAllMusicasSistema(); 
    }
    
    public void updateListaArtistas() throws SelectException{
        artistasSistema = artistaDAO.selectAll(); 
    }

    public void updateArtistasMusicas() throws SelectException, DeleteException{
    	for(Musica musica : musicasSistema) {
    		musica.setArtistas(artistasMusicasDAO.getArtistasParticipam(musica));
    	}
    	for(Artista artista : artistasSistema) {
    		artista.setMusicasProduzidas(artistasMusicasDAO.getMusicasProduzidasArtistas(artista));
    	}
    }
     
    public void updateListaPlaylistUsers(Usuario u) throws SelectException {
    	u.setPlaylists(playlistDAO.selectAll(u));
    }
    
    public List<Musica> getMusicasPlaylist(Playlist p) throws SelectException {
    	return playlistMusicasDAO.getMusicasPlaylist(p); 
    }

    public void setarUsuario(Usuario u){
        this.usuarioAtual = u; 
    }
    public void deslogarUsuario(){
        this.usuarioAtual = null; 
    }
    public Usuario getUsuario(){
        return this.usuarioAtual;
    }

}

