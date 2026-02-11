package ru.yandex.practicum.mymarket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;
import ru.yandex.practicum.mymarket.config.PostgresSQLTestContainer;

@SpringBootTest
//@ImportTestcontainers(PostgresSQLTestContainer.class)
//@Testcontainers
class MyMarketAppApplicationTests extends PostgresSQLTestContainer {
	@Autowired
	R2dbcEntityTemplate template;

	@Test
	void contextLoads() {
	}

	@Test
	void testDatabaseAccessible() {
		StepVerifier.create(template.getDatabaseClient().sql("SELECT 1").fetch().one())
				.expectNextCount(1)
				.verifyComplete();
	}

}