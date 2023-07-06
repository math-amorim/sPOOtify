package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dados.Playlist;
import dados.Usuario;
import excecoes.*; 

public class PlaylistDAO {
    private static PlaylistDAO instance = null; 
    private PreparedStatement selectNewId; 
    private PreparedStatement insert;
    private PreparedStatement delete; 
    private PreparedStatement selectAll; 
    private PreparedStatement update; 
    
    private PlaylistDAO() throws ClassNotFoundException, SQLException, SelectException{
        Connection conexao = Conexao.getConexao(); 

        selectNewId = conexao.prepareStatement("select nextval('playlists_id')"); 
        insert = conexao.prepareStatement("insert into playlists values (?, ?, ?)"); 
        delete = conexao.prepareStatement("delete from playlists where id = ?"); 
        selectAll = conexao.prepareStatement("select * from playlists where id_usuario = ?"); 
        update = conexao.prepareStatement("update playlists set nome = ? where id = ?"); 

    }

    public static PlaylistDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
        if(instance == null){
            instance = new PlaylistDAO(); 
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


    public boolean inserirPlaylist(int user, Playlist playlist) throws SelectException, InsertException{
         try{	 
            playlist.setIdPlaylist(selectNewId());
            insert.setInt(1, playlist.getIdPlaylist()); 
            insert.setString(2, playlist.getNome()); 
            insert.setInt(3, user);
            insert.execute(); 
            return true; 
        }catch (SQLException e) {
            throw new InsertException("Erro ao inserir playlist");
        }

    }

    public void removerPlaylist(Playlist playlist) throws DeleteException{
        try{
            delete.setInt(1, playlist.getIdPlaylist()); 
            delete.executeUpdate(); 
        }catch (SQLException e){
            throw new DeleteException("Erro ao deletar Playlist."); 
        }
    }

    public void update(Playlist p) throws UpdateException{
        try{ 
            update.setString(1, p.getNome());
            update.setInt(2, p.getIdPlaylist());
            update.executeUpdate(); 
        }catch (SQLException e){
            throw new UpdateException("Erro ao atualizar a playlist!"); 
        }
    }

   public List<Playlist> selectAll(Usuario u) throws SelectException{
        List<Playlist> playlists = new LinkedList<Playlist>(); 
        try{
            selectAll.setInt(1, u.getIdUser());
            ResultSet rs = selectAll.executeQuery(); 
            while(rs.next()){
                int id = rs.getInt(1); 
                String nome = rs.getString(2); 

                Playlist p = new Playlist(); 
                p.setIdPlaylist(id);
                p.setNome(nome);

                playlists.add(p);  
            }
        }catch (SQLException e){ 
            throw new SelectException("Erro ao buscar playlists do user."); 
        }
        return playlists; 
    }



}
