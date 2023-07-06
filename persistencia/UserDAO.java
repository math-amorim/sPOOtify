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

public class UserDAO {

    private static UserDAO instance = null; 
    private static PlaylistDAO playlistDAO = null; 

    private PreparedStatement selectNewId; 
    private PreparedStatement insert;
    private PreparedStatement delete; 
    private PreparedStatement selectAll; 
    private PreparedStatement update; 

    private UserDAO() throws ClassNotFoundException, SQLException, SelectException{
        Connection conexao = Conexao.getConexao(); 
        
        selectNewId = conexao.prepareStatement("select nextval('id_usuarios')"); 
        insert = conexao.prepareStatement("insert into usuarios values (?, ?, ?, ?, ?)"); 
        delete = conexao.prepareStatement("delete from usuarios where id = ?"); 
        selectAll = conexao.prepareStatement("select * from usuarios"); 
        update = conexao.prepareStatement("update usuarios set nome = ?, senha = ?, email = ? where id = ?"); 

       playlistDAO = PlaylistDAO.getInstance(); 

    }

    public static UserDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
        if(instance == null){
            instance = new UserDAO(); 
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
            throw new SelectException("Erro ao buscar novo id da tabela de usuários!"); 
        }
        return 0; 
    }

    public void inserirUser(Usuario user, Playlist curtidas) throws InsertException, SelectException{
        try{
            user.setIdUser(selectNewId());
            insert.setInt(1, user.getIdUser()); 
            insert.setString(2, user.getUser()); 
            insert.setString(3, user.getNome());
            insert.setString(4, new String(user.getSenha())); 
            insert.setString(5, user.getEmail());
            insert.execute(); 
            // insere a playlist padrão de musicas curtidas 
            playlistDAO.inserirPlaylist(user.getIdUser(), curtidas); 
        }catch (SQLException e) {
            throw new InsertException("Erro ao inserir user");
        }
    }


    public void removerUser(Usuario user) throws DeleteException{
        // deletar todas as playlists que estão envolvidas a esse user fazer dps
        try{
            delete.setInt(1, user.getIdUser()); 
            delete.executeUpdate(); 
        }  catch (SQLException e){
            throw new DeleteException("Erro ao deletar usuário."); 
        }
    }

    public void update(Usuario user) throws UpdateException{
        try{ 
            update.setString(1, user.getNome());
            update.setString(2, (user.getSenha()).toString()); 
            update.setString(3, user.getEmail());
            update.setInt(4, user.getIdUser());

            update.executeUpdate(); 
        }catch (SQLException e){
            throw new UpdateException("Erro ao atualizar o user"); 
        }
    }

    public List<Usuario> selectAll() throws SelectException{
        List<Usuario> users = new LinkedList<Usuario>(); 
        
        try{
            ResultSet rs = selectAll.executeQuery(); 
            while(rs.next()){
                int id = rs.getInt(1); 
                String user = rs.getString(2);
                String nome = rs.getString(3); 
                char[] senha = (rs.getString(4)).toCharArray(); 
                String email = rs.getString(5); 

                // Playlist
                Usuario u = new Usuario(); 
                u.setIdUser(id);
                u.setUser(user);
                u.setNome(nome);
                u.setSenha(senha);
                u.setEmail(email);

                users.add(u); 
            }
        }catch (SQLException e){
            throw new SelectException("Erro ao buscar usuário."); 
        }
        return users;
    }

}
