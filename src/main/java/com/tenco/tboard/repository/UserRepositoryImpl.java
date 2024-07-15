package com.tenco.tboard.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tenco.tboard.model.User;
import com.tenco.tboard.repository.interfaces.UserRepository;
import com.tenco.tboard.util.DBUtil;

public class UserRepositoryImpl implements UserRepository {

	private static final String INSERT_USER_SQL = " INSERT INTO users(username, password, email) VALUES (?, ?, ?) ";
	private static final String DELETE_USER_SQL = " DELETE FROM users WHERE id = ? ";
	private static final String SELECT_USER_BY_USERNAME = " SELECT * FROM uesrs WHERE username = ? ";
	private static final String SELECT_USER_BY_USERNAME_AND_PASSWORD = " SELECT * FROM uesrs WHERE username = ? AND password = ? ";
	private static final String SELECT_ALL_USERS = " SELECT * FROM users ";

	@Override
	public void addUser(User user) {
		try (Connection conn = DBUtil.getConnection()) {
			conn.setAutoCommit(false);
			// username 중복 확인 필요
			// email 중복 확인 필요
			try (PreparedStatement pstmt = conn.prepareStatement(INSERT_USER_SQL)) {
				pstmt.setString(1, user.getUsername());
				pstmt.setString(2, user.getPassword());
				pstmt.setString(3, user.getEmail());
				pstmt.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteUser(int id) {
		try (Connection conn = DBUtil.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(DELETE_USER_SQL)) {
				pstmt.setInt(1, id);
				int rowCount = pstmt.executeUpdate();
				if (rowCount == 1) {
					System.out.println("1개 데이터가 삭제됨");
				} else {
					System.out.println("삭제된 데이터가 없습니다");
				}
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public User getUserByUsername(String username) {
		User user = null;
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_BY_USERNAME)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				user = User.builder().id(rs.getInt("id")).username(rs.getString("username"))
						.password(rs.getString("password")).email(rs.getString("email"))
						.createAt(rs.getTimestamp("created_at")).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User getUserByUsernameAndPassword(String username, String password) {
		User user = null;
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD)) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				user = User.builder().id(rs.getInt("id")).username(rs.getString("username"))
						.password(rs.getString("password")).email(rs.getString("email"))
						.createAt(rs.getTimestamp("created_at")).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> userList = new ArrayList<User>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_USERS)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				User user = User.builder().id(rs.getInt("id")).username(rs.getString("username"))
						.password(rs.getString("password")).email(rs.getString("email"))
						.createAt(rs.getTimestamp("created_at")).build();
				userList.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}
}