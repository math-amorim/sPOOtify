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
import excecoes.UpdateException;

public class ArtistaDAO {
    private static ArtistaDAO instance = null; 

    private PreparedStatement selectNewId; 
    private PreparedStatement insert;
    private PreparedStatement delete; 
    private PreparedStatement selectAll; 
    private PreparedStatement update; 
    private PreparedStatement select; 

    private ArtistaDAO() throws ClassNotFoundException, SQLException, SelectException{
        Connection conexao = Conexao.getConexao(); 

        selectNewId = conexao.prepareStatement("select nextval('id_artistas')"); 
        insert = conexao.prepareStatement("insert into artistas values (?, ?, ?)"); 
        delete = conexao.prepareStatement("delete from artistas where id = ?"); 
        selectAll = conexao.prepareStatement("select * from artistas"); 
        update = conexao.prepareStatement("update artistas set nome = ?, idade = ? where id = ?"); 
        select = conexao.prepareStatement("select * from artistas where id = ?"); 

    }

    public static ArtistaDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
        if(instance == null){
            instance = new ArtistaDAO(); 
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
            throw new SelectException("Erro ao buscar novo id da tabela de artistas!"); 
        }
        return 0; 
    }


    public void inserirArtista(Artista artista) throws SelectException, InsertException{
        try{
            artista.setIdArtista(selectNewId());
            insert.setInt(1, artista.getIdArtista()); 
            insert.setString(2, artista.getNome());
            insert.setInt(3, artista.getIdade()); 
            insert.execute();
        }catch (SQLException e) {
            throw new InsertException("Erro ao inserir artista");
        }

    }

    public void removerArtista(Artista artista) throws DeleteException{
        try{
            delete.setInt(1, artista.getIdArtista()); 
            delete.executeUpdate(); 
        }  catch (SQLException e){
            throw new DeleteException("Erro ao deletar artista."); 
        }
    }
    
    public void update(Artista a) throws UpdateException{
        try{ 
            update.setString(1, a.getNome());
            update.setInt(2, a.getIdade());
            update.setInt(3, a.getIdArtista());
            update.executeUpdate(); 
        }catch (SQLException e){
            throw new UpdateException("Erro ao atualizar o artista"); 
        }
    }

    public Artista getArtista(int id) throws SelectException{
        try{
            select.setInt(1, id);
            ResultSet rs = select.executeQuery(); 
            while(rs.next()){
                int id_artista = rs.getInt(1); 
                String nome = rs.getString(2); 
                int idade = rs.getInt(3); 

                Artista a = new Artista(); 
                a.setIdArtista(id_artista);
                a.setNome(nome);
                a.setIdade(idade);

                return a; 
            }
        }catch (SQLException e){ 
            throw new SelectException("Erro ao buscar artista no sistema."); 
        }
        return null;
    }

    public List<Artista> selectAll() throws SelectException{
        List<Artista> artistas = new LinkedList<Artista>(); 
        try{
            ResultSet rs = selectAll.executeQuery(); 
            while(rs.next()){
                int id = rs.getInt(1); 
                String nome = rs.getString(2); 
                int idade = rs.getInt(3); 

                Artista a = new Artista(); 
                a.setIdArtista(id);
                a.setNome(nome);
                a.setIdade(idade);

                artistas.add(a); 
            }
        }catch (SQLException e){ 
            throw new SelectException("Erro ao buscar artistas."); 
        }
        return artistas; 
    }

}
