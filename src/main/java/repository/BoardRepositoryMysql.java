package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.Board;

public class BoardRepositoryMysql implements BoardRepository {

	private final DBManager dbManager;

	private static BoardRepositoryMysql instance;


	public static BoardRepositoryMysql getInstance() {
		if (instance == null) {
			synchronized (BoardRepositoryMysql.class) {
				instance = new BoardRepositoryMysql();
			}
		}
		return instance;
	}

	public BoardRepositoryMysql() {
		this.dbManager = DBManager.getInstance();
	}

	@Override
	public void addPost(Board board) {
		try (Connection conn = dbManager.getConnection();
			 PreparedStatement preparedStatement = conn.prepareStatement("insert into board values(?,?,?,?,?)");
		) {
			preparedStatement.setString(1, board.getWriter());
			preparedStatement.setTimestamp(2, Timestamp.valueOf(board.getTime())); // 반대로 매핑할때는 toTimestamp()
			preparedStatement.setString(3, board.getTitle());
			preparedStatement.setString(4, board.getContents());
			preparedStatement.setInt(5, 0);

			if (preparedStatement.executeUpdate() != 1) {
				System.out.println("fail"); // logger로 바꾸기
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Collection<Board> findAll() {
		try (Connection conn = dbManager.getConnection();
			 PreparedStatement preparedStatement = conn.prepareStatement("select * from board");
		) {
			ResultSet resultSet = preparedStatement.executeQuery();
			List<Board> boards = new ArrayList<>();
			while (resultSet.next()) {
				String boardContents = resultSet.getString("board_contents");
				String boardWriter = resultSet.getString("board_writer");
				LocalDateTime boardTime = resultSet.getTimestamp("board_time").toLocalDateTime();
				String boardTitle = resultSet.getString("board_title");
				Long id = resultSet.getLong("id");
				boards.add(Board.of(id, boardContents, boardTitle, boardTime, boardWriter));
			}
			return boards;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
