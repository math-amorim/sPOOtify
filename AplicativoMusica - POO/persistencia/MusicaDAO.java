package persistencia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import excecoes.*;
import dados.*;

public class MusicaDAO {
    private static MusicaDAO instance = null; 
    private PreparedStatement selectNewId, select, insert, delete, selectAll, update;

    public static MusicaDAO getInstance() throws ClassNotFoundException, SQLException{
        if(instance == null){
            instance = new MusicaDAO(); 
        }
        return instance; 
    }

    private MusicaDAO() throws ClassNotFoundException, SQLException{
        Connection conexao = Conexao.getConexao(); 

        selectNewId = conexao.prepareStatement("select nextval('id_musicas')"); 
        insert = conexao.prepareStatement("insert into musicas values (?, ?, ?, ?)"); 
        select = conexao.prepareStatement("select * from musicas where id = ?"); 
        update = conexao.prepareStatement("update musicas set nome = ?, genero = ? where id=? "); 
        delete = conexao.prepareStatement("delete from musicas where id = ?"); 
        selectAll = conexao.prepareStatement("select * from musicas"); 

    }

    private int selectNewId() throws SelectException{
        try{ 
            ResultSet rs = selectNewId.executeQuery(); 
            if(rs.next()){
                return rs.getInt(1); 
            }
        }catch(SQLException e){
            throw new SelectException("Erro ao buscar novo id da tabela de músicas!"); 
        }
        return 0; 
    }


    public boolean inserirMusica(Musica musica) throws InsertException, SelectException, IOException{
        try { 
            musica.setIdMusica(selectNewId());
            insert.setInt(1, musica.getIdMusica()); 
            insert.setString(2, musica.getNome());
            insert.setString(3, musica.getGenero());
            insert.setBytes(4, musica.getMusica());
            insert.execute(); 
            return true; 
        }catch (SQLException e){
            throw new InsertException("Erro ao inserir nova música!");
        }
    }

    public void updateMusica(Musica musica) throws UpdateException, IOException{
        try{
            update.setString(1, musica.getNome()); 
            update.setString(2, musica.getGenero()); 
            update.setInt(3, musica.getIdMusica());
            update.executeUpdate(); 
        }catch(SQLException e){
            throw new UpdateException("Erro ao atualizar música."); 
        }
    }

    public void removerMusica(Musica musica) throws DeleteException{
        try{
            delete.setInt(1, musica.getIdMusica()); 
            delete.executeUpdate(); 
        }catch(SQLException e){
            throw new DeleteException("Erro ao deletar música!"); 
        }
    }

    public Musica getMusica(int id) throws SelectException{
        try{
            select.setInt(1, id);
            ResultSet rs = select.executeQuery(); 
            while(rs.next()){
                int id_musica = rs.getInt(1); 
                String nome = rs.getString(2); 
                String genero = rs.getString(3); 
                byte[] musica = rs.getBytes(4); 

                Musica m = new Musica(); 
                m.setIdMusica(id_musica);
                m.setNome(nome);
                m.setGenero(genero);
                m.setMusica(musica);

                return m; 
            }
        }catch (SQLException e){ 
            throw new SelectException("Erro ao buscar música do sistema."); 
        }
        return null;
    }

    public List<Musica> getAllMusicasSistema() throws SelectException{
        List<Musica> musicas = new LinkedList<Musica>(); 
        try{
            ResultSet rs = selectAll.executeQuery(); 
            while(rs.next()){
                int id = rs.getInt(1); 
                String nome = rs.getString(2); 
                String genero = rs.getString(3); 
                byte[] musica = rs.getBytes(4); 

                Musica m = new Musica(); 
                m.setIdMusica(id);
                m.setNome(nome);
                m.setGenero(genero);
                m.setMusica(musica);

                musicas.add(m); 
            }
        }catch (SQLException e){ 
            throw new SelectException("Erro ao buscar músicas do sistema."); 
        }
        return musicas; 
    }
}
