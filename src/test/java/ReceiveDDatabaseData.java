import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReceiveDDatabaseData {

    public static void init(Connection connection) {
        String sql;

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/create_table.sql"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            sql = sb.toString();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
