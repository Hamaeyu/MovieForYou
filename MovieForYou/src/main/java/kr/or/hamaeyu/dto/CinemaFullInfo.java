package kr.or.hamaeyu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Builder @Data @EqualsAndHashCode
public class CinemaFullInfo {
	private int theaterId;
	private String name;
	private String address;
	private int typeId;
	private String type;
	private int regionId;
	private String region;
	private double lat;
	private double lng;
}
