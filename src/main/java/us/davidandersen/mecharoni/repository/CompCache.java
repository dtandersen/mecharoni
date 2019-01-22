package us.davidandersen.mecharoni.repository;

import java.util.List;
import java.util.Objects;
import us.davidandersen.mecharoni.entity.Component;

public class CompCache
{
	private final List<Component> components;

	public CompCache(final List<Component> components)
	{
		this.components = components;
	}

	public Component getComp(final String name)
	{
		return components.stream()
				.filter(comp -> Objects.equals(name, comp.getName()) || Objects.equals(name, comp.getFriendlyName()))
				.findFirst().get();
	}
}
