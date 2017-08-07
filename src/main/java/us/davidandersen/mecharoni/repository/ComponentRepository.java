package us.davidandersen.mecharoni.repository;

import java.util.List;
import us.davidandersen.mecharoni.entity.BasicComponent;

public interface ComponentRepository
{
	List<BasicComponent> all() throws Exception;

	List<BasicComponent> clanComponents() throws Exception;

	List<BasicComponent> isComponents() throws Exception;
}
