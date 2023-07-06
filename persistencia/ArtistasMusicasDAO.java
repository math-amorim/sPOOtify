package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dados.Artista;
import dados.Musica;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;

public class ArtistasMusicasDAO {
  private static ArtistasMusicasDAO instance = null; 
    private PreparedStatement insert; 
    private PreparedStatement deleteMusica; 
    private PreparedStatement deleteArtista; 
    private PreparedStatement selectArtistasMusica; 
    private PreparedStatement selectMusicasArtista; 
    private PreparedStatement deleteRelacao; 
    private PreparedStatement selectNewId; 
    private MusicaDAO musicaDAO; 
    private ArtistaDAO artistaDAO; 

    private ArtistasMusicasDAO() throws ClassNotFoundException, SQLException, SelectException{
        Connection conexao = Conexao.getConexao(); 
        
        musicaDAO = MusicaDAO.getInstance(); 
        artistaDAO = ArtistaDAO.getInstance(); 
        insert = conexao.prepareStatement("insert into artistas_tem_musicas values (?, ?, ?)"); 
        selectNewId = conexao.prepareStatement("select nextval('id_artistas')"); 
        deleteArtista = conexao.prepareStatement("delete from artistas_tem_musicas where id_artista = ?");
        deleteMusica = conexao.prepareStatement("delete from artistas_tem_musicas where id_musica = ?"); 
        deleteRelacao = conexao.prepareStatement("delete from artistas_tem_musicas where id_musica = ?, id_artista=?"); 
        selectArtistasMusica = conexao.prepareStatement("select * from artistas_tem_musicas where id_artista=?"); 
        selectMusicasArtista = conexao.prepareStatement("select * from artistas_tem_musicas where id_musica=?");     
    
    }

    public static ArtistasMusicasDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
        if(instance == null){
            instance = new ArtistasMusicasDAO(); 
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
            throw new SelectException("Erro ao buscar novo id da tabela de relação entre artista e música!"); 
        }
        return 0;
    }
    
    public void adicionarArtistaMusica(int idArtista, int idMusica) throws InsertException, SelectException {
        try {
            insert.setInt(1, selectNewId()); 
            insert.setInt(2, idArtista);
            insert.setInt(3, idMusica);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new InsertException("Erro ao adicionar relação.");
        }
    }
    

    public void removerMusica(Musica musica) throws DeleteException{
        try{
            deleteMusica.setInt(1,musica.getIdMusica()); 
            deleteMusica.executeUpdate(); 
        }  catch (SQLException e){
            throw new DeleteException("Erro ao deletar música da playlist."); 
        }
    }

    public void removerArtista(Artista artista) throws DeleteException{
         try{
            deleteArtista.setInt(1,artista.getIdArtista()); 
            deleteArtista.executeUpdate(); 
        }  catch (SQLException e){
            throw new DeleteException("Erro ao deletar."); 
        }
    }

    public void removerRelacao(Artista a, Musica m) throws DeleteException{
        try{
            deleteRelacao.setInt(1,m.getIdMusica()); 
            deleteRelacao.setInt(2, a.getIdArtista());
            deleteRelacao.executeUpdate(); 
        }  catch (SQLException e){
            throw new DeleteException("Erro ao deletar música da playlist."); 
        }

    }

    public List<Musica> getMusicasProduzidasArtistas(Artista a) throws SelectException{
        List<Musica> musicas = new LinkedList<Musica>(); 
        try{
            selectArtistasMusica.setInt(1, a.getIdArtista());
            ResultSet rs = selectArtistasMusica.executeQuery(); 
            while(rs.next()){
                int id = rs.getInt("id_musica"); 
                musicas.add(musicaDAO.getMusica(id)); 
            }
        }catch (SQLException e){
            throw new SelectException("Erro ao buscar músicas produzidas pelo artista."); 
        }
        return musicas;
    }

    public List<Artista> getArtistasParticipam(Musica m) throws SelectException{
        List<Artista> artistas = new LinkedList<Artista>(); 
        try{
            selectMusicasArtista.setInt(1, m.getIdMusica());
            ResultSet rs = selectMusicasArtista.executeQuery(); 

            while(rs.next()){
                int id = rs.getInt("id_artista"); 
                artistas.add(artistaDAO.getArtista(id)); 
            }
        }catch (SQLException e){
            throw new SelectException("Erro ao buscar artistas que participam dessa música."); 
        }
        return artistas;
    }
    

}
