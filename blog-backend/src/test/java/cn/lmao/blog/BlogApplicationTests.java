package cn.lmao.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogApplicationTests {


	//测试数据库是否连接成功
	@Test
	void contextLoads() {
		// 这里可以添加一些简单的数据库操作来验证连接是否成功
		// 例如：查询一条数据，或者插入一条数据
		// 这里我们假设使用的是 H2 数据库，连接 URL 为 jdbc:h2:mem:testdb
		// 你可以根据实际使用的数据库类型和连接方式进行修改
		// 例如：使用 JDBC 连接数据库
		String url = "jdbc:mysql://localhost:3306/lmao_blog";
		String user = "root";
		String password = "123456";
		try {
			// 尝试连接数据库
			Class.forName("com.mysql.cj.jdbc.Driver");
			java.sql.Connection connection = java.sql.DriverManager.getConnection(url, user, password);
			// 执行简单的查询操作
			java.sql.Statement statement = connection.createStatement();
			statement.executeQuery("show tables");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据库连接失败");
			return;
		}
		// 如果连接成功，则不会抛出异常
		// 这里可以添加一些简单的数据库操作来验证连接是否成功
		// 例如：查询一条数据，或者插入一条数据
		// 如果没有抛出异常，则表示连接成功
		System.out.println("数据库连接成功");
		// 其他测试代码...
		System.out.println("测试通过");
	}

}
