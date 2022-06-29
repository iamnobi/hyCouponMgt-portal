package ocean.acs.models.data_object.kernel;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageExtensionDO {

	@JsonProperty("criticalityIndicator")
	private Boolean criticalityIndicator;

	@JsonProperty("data")
	private Map<String, Object> data;

	@JsonProperty("id")
	private String id;

	@JsonProperty("name")
	private String name;

}
