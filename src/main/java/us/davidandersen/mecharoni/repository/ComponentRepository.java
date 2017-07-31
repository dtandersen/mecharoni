package us.davidandersen.mecharoni.repository;

import java.util.List;
import us.davidandersen.mecharoni.entity.Component;

public interface ComponentRepository
{
	List<Component> all() throws Exception;

	List<Component> clanComponents() throws Exception;

	List<Component> isComponents() throws Exception;
}
