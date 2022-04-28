package com.polarbookshop.dispatcherservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
public class OrderAcceptedMessage {

	@JsonProperty("orderid")
	private Long orderId;
}


