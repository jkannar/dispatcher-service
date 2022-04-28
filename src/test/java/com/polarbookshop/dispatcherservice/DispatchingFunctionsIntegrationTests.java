package com.polarbookshop.dispatcherservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.support.GenericMessage;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@FunctionalSpringBootTest
@Disabled("These tests are only necessary when using the functions alone (no bindings)")
class DispatchingFunctionsIntegrationTests {

	@Autowired
	private FunctionCatalog catalog;

	@Test
	void packOrder() {
		Function<OrderAcceptedMessage, Long> pack = catalog.lookup(Function.class, "pack");
		Long orderId = 121L;
		assertThat(pack.apply(new OrderAcceptedMessage(orderId))).isEqualTo(orderId);
	}

	@Test
	void labelOrder() {
		Function<Flux<Long>, Flux<OrderDispatchedMessage>> label = catalog.lookup(Function.class, "label");
		Flux<Long> orderFlux = Flux.just(121L);

		StepVerifier.create(label.apply(orderFlux))
				.expectNextMatches(dispatchedOrder ->
						dispatchedOrder.equals(new OrderDispatchedMessage(121L)))
				.verifyComplete();
	}

	@Test
	void packAndLabelOrder() throws IOException {
		Function<OrderAcceptedMessage, Flux<OrderDispatchedMessage>> packAndLabel =
				catalog.lookup(Function.class, "pack|label");
		long orderId = 121;

//		List fluxAsList = packAndLabel.apply(new OrderAcceptedMessage(orderId)).collectList().block();
//		OrderDispatchedMessage message = (new ObjectMapper()).readValue((byte[]) ((GenericMessage) fluxAsList.get(0)).getPayload(), OrderDispatchedMessage.class);

		StepVerifier.create(packAndLabel.apply(new OrderAcceptedMessage(orderId)))
				.expectNextMatches(dispatchedOrder ->
						dispatchedOrder.equals(new OrderDispatchedMessage(orderId)))
				.verifyComplete();
	}
}