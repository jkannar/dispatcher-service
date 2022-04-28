package com.polarbookshop.dispatcherservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDispatchedMessage {

	@JsonProperty("orderid")
	private Long orderId;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OrderDispatchedMessage) {
			return orderId.equals(((OrderDispatchedMessage) obj).getOrderId());
		} else {
			return false;
		}
	}
}
