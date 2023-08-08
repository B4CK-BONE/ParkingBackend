package cat.soft.src.parking.model.room;

import cat.soft.src.parking.model.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostCreateRoomReq {
	private Integer idx;

	public Room toEntity() {
		return Room.builder()
			.idx(idx)
			.build();
	}
}
