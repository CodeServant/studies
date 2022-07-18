package subscribers.Zad1;

import java.util.Optional;
import java.util.Set;

public enum Status {
	OK(1), WRONG_REQUEST(2), NO_NEW_INFO(3), AUTH_ERROR(4),  NO_TOPIC(5);

	private Integer code;

	Status(Integer code) {
		this.code = code;
	}

	public Integer code() {
		return code;
	}

	public static Status toStatus(Integer cd) {
		Optional<Status> val = Set.of(Status.values()).stream().filter((c) -> cd == c.code()).findFirst();
		return val.get();
	}
}
