package ru.yandex.practicum.mymarket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.mymarket.config.PostgresSQLTestContainer;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
//@ImportTestcontainers(PostgresSQLTestContainer.class)
//@Testcontainers
class MyMarketAppApplicationTests extends PostgresSQLTestContainer {

	@Test
	void contextLoads() {
	}

	@Test
	void testDatasourceInitialized(@Autowired DataSource dataSource) {
		assertNotNull(dataSource);

		assertDoesNotThrow(() ->
			assertNotNull(dataSource.getConnection())
		);

	}

}