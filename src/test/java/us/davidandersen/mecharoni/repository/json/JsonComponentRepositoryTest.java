package us.davidandersen.mecharoni.repository.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.davidandersen.mecharoni.entity.Component;

public class JsonComponentRepositoryTest
{
	@Test
	void test() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		final JsonComponentRepository repo = new JsonComponentRepository();
		final List<Component> all = repo.all();
		final Component comp = all.stream().filter(c -> Objects.equals(c.getName(), "AutoCannon20")).findFirst().get();

		assertThat(comp.getName(), equalTo("AutoCannon20"));
		assertThat(comp.getMinHeatPenaltyLevel(), equalTo(2));
		assertThat(comp.getHeatPenalty(), equalTo(24f));
		assertThat(comp.getHeatPenaltyId(), equalTo(9));
	}
}
