package smu.DAO;

import smu.DTO.AssociazioneCartaPortafoglio;

import java.sql.SQLException;

public interface AssociazioneCartaPortafoglioDAO {
    public boolean insert(AssociazioneCartaPortafoglio associazione) throws SQLException;
    public boolean delete(AssociazioneCartaPortafoglio associazione) throws SQLException;
    public boolean update(AssociazioneCartaPortafoglio associazione) throws SQLException;

}
