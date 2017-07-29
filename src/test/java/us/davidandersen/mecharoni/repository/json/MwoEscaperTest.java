package us.davidandersen.mecharoni.repository.json;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class MwoEscaperTest
{
	@Test
	public void test()
	{
		assertThat(MwoEscaper.unescape(null), equalTo(null));
		assertThat(MwoEscaper.unescape(""), equalTo(""));
		assertThat(MwoEscaper.unescape("a"), equalTo("a"));
		assertThat(MwoEscaper.unescape("AC\\/20"), equalTo("AC/20"));
	}
}
