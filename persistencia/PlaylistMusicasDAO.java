package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dados.*;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;

public class PlaylistMusicasDAO {
    private static PlaylistMusicasDAO instance = null; 
    private PreparedStatement insert; 
    private PreparedStatement deleteMusica; 
    private PreparedStatement deletePlaylist; 
    private PreparedStatement selectNewId; 
    private PreparedStatement selectMusicasDaPlaylist; 
    private PreparedStatement deleteRelacao; 
    private MusicaDAO musicaDAO; 

    private PlaylistMusicasDAO() throws ClassNotFoundException, SQLException, SelectException{
        Connection conexao = Conexao.getConexao(); 
        musicaDAO = MusicaDAO.getInstance(); 
        selectNewId = conexao.prepareStatement("select nextval('id_playlists_tem_musicas')"); 
        insert = conexao.prepareStatement("insert into playlists_tem_musicas values (?, ?, ?)"); 
        deletePlaylist =  conexao.prepareStatement("delete from playlists_tem_musicas where id_playlist = ?"); 
        deleteMusica = conexao.prepareStatement("delete from playlists_tem_musicas where id_musica = ?"); 
        deleteRelacao = conexao.prepareStatement("delete from playlists_tem_musicas where id_musica = ? && id_playlist = ?");
        selectMusicasDaPlaylist  = conexao.prepareStatement("select * from playlists_tem_musicas where id_playlist = ?"); 
    }

    public static PlaylistMusicasDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
        if(instance == null){
            instance = new PlaylistMusicasDAO(); 
        }
        return instance; 
    }

    public int selectNewId() throws SelectException{
        try{
            ResultSet rs = selectNewId.executeQuery(); 
            if(rs.next()){
                return rs.getInt(1); 
            }
        }catch (SQLException e){
            throw new SelectException("Erro ao buscar novo id da tabela de playlists!"); 
        }
        return 0;
    }

    public void adicionarMusicaPlaylist(int idPlaylist, int idMusica) throws InsertException, SelectException {
        try {
            insert.setInt(1, selectNewId());
            insert.setInt(2, idPlaylist);
            insert.setInt(3, idMusica);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new InsertException("Erro ao adicionar música na playlist.");
        }
    }

    public void removerMusicaPlaylist(Musica musica) throws DeleteException{
        try{
            deleteMusica.setInt(1,musica.getIdMusica()); 
            deleteMusica.executeUpdate(); 
        }catch (SQLException e){
            throw new DeleteException("Erro ao deletar música da playlist."); 
        }
    }

    public void removerPlaylist(Playlist playlist) throws DeleteException{
         try{
            deletePlaylist.setInt(1,playlist.getIdPlaylist()); 
            deletePlaylist.executeUpdate(); 
        }catch (SQLException e){
            throw new DeleteException("Erro ao deletar."); 
        }
    }

    public void removerRelacao(Playlist p, Musica m) throws DeleteException{
        try{
            deleteRelacao.setInt(1,m.getIdMusica()); 
            deleteRelacao.setInt(2, p.getIdPlaylist());
            deleteRelacao.executeUpdate(); 
        }catch (SQLException e){
            throw new DeleteException("Erro ao deletar música da playlist."); 
        }

    }

    public List<Musica> getMusicasPlaylist(Playlist p) throws SelectException{
         List<Musica> musicas = new LinkedList<Musica>(); 
        try{
            selectMusicasDaPlaylist.setInt(1, p.getIdPlaylist());
            ResultSet rs = selectMusicasDaPlaylist.executeQuery(); 
            while(rs.next()){
                int id = rs.getInt("id_musica"); 
                musicas.add(musicaDAO.getMusica(id)); 
            }
        }catch (SQLException e){
            throw new SelectException("Erro ao buscar músicas."); 
        }
        return musicas;
    }
    
}
