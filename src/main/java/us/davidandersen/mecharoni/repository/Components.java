package us.davidandersen.mecharoni.repository;

import java.util.List;
import java.util.Objects;
import us.davidandersen.mecharoni.entity.Component;

public class Components
{
	private final List<Component> components;

	public Components(final List<Component> components)
	{
		this.components = components;
	}

	public Component getComponentByName(final String name)
	{
		return components.stream()
				.filter(comp -> Objects.equals(name, comp.getName()) || Objects.equals(name, comp.getFriendlyName()))
				.findFirst().get();
	}
}
