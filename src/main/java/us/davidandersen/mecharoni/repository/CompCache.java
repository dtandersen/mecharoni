package us.davidandersen.mecharoni.repository;

import java.util.List;
import java.util.Objects;
import us.davidandersen.mecharoni.entity.BasicComponent;

public class CompCache
{
	private final List<BasicComponent> components;

	public CompCache(final List<BasicComponent> components)
	{
		this.components = components;
	}

	public BasicComponent getComp(final String name)
	{
		return components.stream()
				.filter(comp -> Objects.equals(name, comp.getName()) || Objects.equals(name, comp.getFriendlyName()))
				.findFirst().get();
	}
}
