package olegpash.lab7.server.db;

import olegpash.lab7.common.entities.Coordinates;
import olegpash.lab7.common.entities.MusicBand;
import olegpash.lab7.common.entities.Studio;
import olegpash.lab7.common.entities.enums.MusicGenre;
import olegpash.lab7.common.exceptions.DatabaseException;
import olegpash.lab7.server.interfaces.DBConnectable;
import olegpash.lab7.server.util.StringEncryptor;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class DBManager {

    private final DBConnectable dbConnector;
    private final ReentrantLock reentrantLock = new ReentrantLock();

    public DBManager(DBConnectable dbConnector) {
        this.dbConnector = dbConnector;
    }

    public HashSet<MusicBand> loadCollection() throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String selectCollectionQuery = "SELECT * FROM s335097musicbands";
            Statement statement = connection.createStatement();
            ResultSet collectionSet = statement.executeQuery(selectCollectionQuery);
            HashSet<MusicBand> resultSet = new HashSet<>();
            while (collectionSet.next()) {
                Studio bandsStudio = null;
                if (collectionSet.getString("studioAddress") != null) {
                    bandsStudio = new Studio(collectionSet.getString("studioAddress"));
                }
                Coordinates bandsCoordinates = new Coordinates(collectionSet.getFloat("x"),
                        collectionSet.getFloat("y"));
                String bandsGenre = collectionSet.getString("musicGenre");
                MusicGenre musicGenre = null;
                if (bandsGenre != null) {
                    musicGenre = MusicGenre.valueOf(bandsGenre);
                }
                MusicBand musicBand = new MusicBand(
                        collectionSet.getDate("creationDate").toLocalDate(),
                        collectionSet.getLong("id"),
                        collectionSet.getString("name"),
                        bandsCoordinates,
                        collectionSet.getLong("numberOfParticipants"),
                        collectionSet.getString("description"),
                        musicGenre,
                        bandsStudio);
                resultSet.add(musicBand);
            }
            return resultSet;
        });
    }

    public Long addElement(MusicBand musicBand, String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String addElementQuery = "INSERT INTO s335097musicbands "
                    + "(creationDate, name, x, y, numberOfParticipants, description, "
                    + "musicGenre, studioAddress, owner_id) "
                    + "SELECT ?, ?, ?, ?, ?, ?, ?, ?, id "
                    + "FROM s335097users "
                    + "WHERE s335097users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(addElementQuery,
                    Statement.RETURN_GENERATED_KEYS);
            Coordinates coordinates = musicBand.getCoordinates();
            preparedStatement.setDate(1, Date.valueOf(musicBand.getCreationDate()));
            preparedStatement.setString(2, musicBand.getName());
            preparedStatement.setFloat(3, (float) coordinates.getX());
            preparedStatement.setFloat(4, coordinates.getY());
            preparedStatement.setLong(5, musicBand.getNumberOfParticipants());
            preparedStatement.setString(6, musicBand.getDescription());
            if (musicBand.getGenre() == null) {
                preparedStatement.setString(7, null);
            } else {
                preparedStatement.setString(7, musicBand.getGenre().toString());
            }
            if (musicBand.getStudio() == null) {
                preparedStatement.setString(8, null);
            } else {
                preparedStatement.setString(8, musicBand.getStudio().getAddress());
            }
            preparedStatement.setString(9, username);

            preparedStatement.executeUpdate();
            ResultSet result = preparedStatement.getGeneratedKeys();
            result.next();

            return result.getLong(1);
        });
    }

    public boolean removeById(Long id, String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String removeQuery = "DELETE FROM s335097musicbands "
                    + "USING s335097users "
                    + "WHERE s335097musicbands.id = ? "
                    + "AND s335097musicbands.owner_id = s335097users.id AND s335097users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(removeQuery);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, username);

            int deletedBands = preparedStatement.executeUpdate();
            return deletedBands > 0;
        });
    }

    public boolean updateById(MusicBand musicBand, Long id, String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            connection.createStatement().execute("BEGIN TRANSACTION;");
            String updateQuery = "UPDATE s335097musicbands "
                    + "SET name = ?, "
                    + "x = ?, "
                    + "y = ?, "
                    + "numberOfParticipants = ?, "
                    + "description = ?, "
                    + "musicGenre = ?, "
                    + "studioAddress = ? "
                    + "FROM s335097users "
                    + "WHERE s335097musicbands.id = ? "
                    + "AND s335097musicbands.owner_id = s335097users.id AND s335097users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            Coordinates coordinates = musicBand.getCoordinates();
            preparedStatement.setString(1, musicBand.getName());
            preparedStatement.setFloat(2, (float) coordinates.getX());
            preparedStatement.setFloat(3, coordinates.getY());
            preparedStatement.setLong(4, musicBand.getNumberOfParticipants());
            preparedStatement.setString(5, musicBand.getDescription());
            if (musicBand.getGenre() == null) {
                preparedStatement.setString(6, null);
            } else {
                preparedStatement.setString(6, musicBand.getGenre().toString());
            }
            if (musicBand.getStudio() == null) {
                preparedStatement.setString(7, null);
            } else {
                preparedStatement.setString(7, musicBand.getStudio().getAddress());
            }
            preparedStatement.setLong(8, id);
            preparedStatement.setString(9, username);

            int updatedRows = preparedStatement.executeUpdate();
            connection.createStatement().execute("COMMIT;");

            return updatedRows > 0;
        });
    }

    public boolean checkBandExistence(Long id) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String existenceQuery = "SELECT COUNT (*) "
                    + "FROM s335097musicbands "
                    + "WHERE s335097musicbands.id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(existenceQuery);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return resultSet.getInt("count") > 0;
        });
    }

    public List<Long> clear(String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String clearQuery = "DELETE FROM s335097musicbands "
                    + "USING s335097users "
                    + "WHERE s335097musicbands.owner_id = s335097users.id AND s335097users.login = ? "
                    + "RETURNING s335097musicbands.id;";
            PreparedStatement preparedStatement = connection.prepareStatement(clearQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Long> resultingList = new ArrayList<>();
            while (resultSet.next()) {
                resultingList.add(resultSet.getLong("id"));
            }
            return resultingList;
        });
    }

    public void addUser(String username, String password) throws DatabaseException {
        dbConnector.handleQuery((Connection connection) -> {
            String addUserQuery = "INSERT INTO s335097users (login, password) "
                    + "VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, StringEncryptor.encryptString(password));

            preparedStatement.executeUpdate();
        });
    }

    public String getPassword(String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String getPasswordQuery = "SELECT (password) "
                    + "FROM s335097users "
                    + "WHERE s335097users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(getPasswordQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
            return null;
        });
    }

    public boolean checkUsersExistence(String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String existenceQuery = "SELECT COUNT (*) "
                    + "FROM s335097users "
                    + "WHERE s335097users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(existenceQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return resultSet.getInt("count") > 0;
        });
    }

    public List<Long> getIdsOfUsersElements(String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String getIdsQuery = "SELECT s335097musicbands.id FROM s335097musicbands, s335097users "
                    + "WHERE s335097musicbands.owner_id = s335097users.id AND s335097users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(getIdsQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Long> resultingList = new ArrayList<>();
            while (resultSet.next()) {
                resultingList.add(resultSet.getLong("id"));
            }

            return resultingList;
        });
    }

    public boolean validateUser(String username, String password) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) ->
                StringEncryptor.encryptString(password).equals(getPassword(username)));
    }
}
